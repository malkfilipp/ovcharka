package ovcharka.trainingservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ovcharka.conceptservice.domain.Concept;
import ovcharka.nlp.similarity.SentenceSimilarityCalculator;
import ovcharka.trainingservice.domain.Training;
import ovcharka.trainingservice.repository.TrainingRepository;
import ovcharka.userservice.domain.Grade;

import java.security.SecureRandom;
import java.util.Optional;

import static java.lang.Math.max;

@Service
public class TrainingService {

    private static final String START_MESSAGE = "\\start";
    private static final String END_MESSAGE = "\\end";

    private final TrainingClient trainingClient;
    private final TrainingRepository trainingRepository;

    private final SecureRandom rand = new SecureRandom();
    private final SentenceSimilarityCalculator calculator = new SentenceSimilarityCalculator();

    @Autowired
    public TrainingService(TrainingClient trainingClient, TrainingRepository trainingRepository) {
        this.trainingClient = trainingClient;
        this.trainingRepository = trainingRepository;
    }

    public String processMessage(String username, String message) {
        switch (message) {
            case START_MESSAGE:
                return getOnStartResponse(username);

            case END_MESSAGE:
                return getOnEndResponse(username);

            default:
                return getResponse(username, message);
        }
    }

    private String getOnStartResponse(String username) {
        var userId = trainingClient.getUserIdByUsername(username);
        var training = trainingRepository.findByUserId(userId);

        if (training.isPresent()) {
            return training.get().getCurWord();
        } else {
            var newTraining = Training.of(userId);
            newTraining.setWords(trainingClient.getAllWords());
            var word = getRandomWord(newTraining);
            trainingRepository.save(newTraining);
            return word;
        }
    }

    private Training getTraining(String username) {
        var userId = trainingClient.getUserIdByUsername(username);
        var training = trainingRepository.findByUserId(userId);

        if (!training.isPresent())
            throw new IllegalArgumentException("No current trainings for user with username: " + username);

        return training.get();
    }

    private String getOnEndResponse(String username) {
        var training = getTraining(username);
        return endTrainingAndGetGrade(username, training);
    }

    private String endTrainingAndGetGrade(String username, Training training) {
        var grade = getFinalGrade(training.getScore(), training.getMax());

        trainingClient.updateUserStats(username, grade);
        trainingRepository.delete(training);

        return grade;
    }

    private String getResponse(String username, String answer) {
        var training = getTraining(username);
        var curWord = training.getCurWord();
        var concept = trainingClient.getConcept(curWord);

        reevaluate(answer, concept, training);

        var word = getNextWord(concept, training);
        if (word.isPresent()) {
            trainingRepository.save(training);
            return word.get();
        }

        return endTrainingAndGetGrade(username, training);
    }

    private Optional<String> getNextWord(Concept concept, Training training) {
        var words = training.getWords();

        if (training.getQuestionsLeft() > 0 && !words.isEmpty()) {
            if (training.getDebt() > 0) {
                var word = getRelatedWordIfAvailable(concept, training);
                if (word.isPresent()) return word;
            }
            return Optional.of(getRandomWord(training));
        }
        return Optional.empty();
    }

    private String getRandomWord(Training training) {
        var words = training.getWords();

        if (words.isEmpty())
            throw new IllegalArgumentException("No words left");

        var index = rand.nextInt(words.size());
        var word = words.get(index);
        words.remove(index);
        training.setCurWord(word);
        training.setQuestionsLeft(training.getQuestionsLeft() - 1);
        return word;
    }

    private Optional<String> getRelatedWordIfAvailable(Concept concept, Training training) {
        var words = training.getWords();

        var relatedWord = concept
                .getRelated()
                .stream()
                .filter(words::contains)
                .findFirst();

        if (!relatedWord.isPresent())
            return Optional.empty();

        var word = relatedWord.get();
        words.remove(word);
        training.setCurWord(word);
        training.setQuestionsLeft(training.getQuestionsLeft() - 1);
        return relatedWord;
    }

    private void reevaluate(String answer, Concept concept, Training training) {
        var conceptScore = concept.getScore();
        var debt = training.getDebt();

        System.out.println("TrainingService.reevaluate");
        System.out.println("word = " + concept.getWord());

        if (calculator.areSimilar(answer, concept.getDefinition())) {
            System.out.println("similar");
            training.setScore(training.getScore() + conceptScore);
            if (debt > 0)
                training.setDebt(max(debt - conceptScore, 0));
        } else {
            System.out.println("not similar");
            training.setDebt(debt + conceptScore);
        }
        training.setMax(training.getMax() + conceptScore);
    }

    private String getFinalGrade(int score, int max) {
        var res = (double) score / max;
        Grade grade;

        if (res >= 0.9) {
            grade = Grade.A;
        } else if (res >= 0.8) {
            grade = Grade.B;
        } else if (res >= 0.7) {
            grade = Grade.C;
        } else if (res >= 0.6) {
            grade = Grade.D;
        } else {
            grade = Grade.F;
        }

        return grade.getSymbol();
    }
}
