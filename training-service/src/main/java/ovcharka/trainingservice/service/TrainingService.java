package ovcharka.trainingservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ovcharka.conceptservice.domain.Concept;
import ovcharka.nlp.similarity.SentenceSimilarityCalculator;
import ovcharka.trainingservice.domain.Training;
import ovcharka.trainingservice.repository.TrainingRepository;

import java.security.SecureRandom;

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

        if (userId == null)
            throw new IllegalArgumentException("No such user");

        var training = trainingRepository.findByUserId(userId);

        if (training.isPresent()) {
            return training.get().getCurWord();
        } else {
            var newTraining = Training.of(userId);
            newTraining.setWords(trainingClient.getAllWords());
            trainingRepository.save(newTraining);
            return getRandomWord(username);
        }
    }

    private Training getTraining(String username) {
        var userId = trainingClient.getUserIdByUsername(username);

        if (userId == null)
            throw new IllegalArgumentException("No such user");

        var training = trainingRepository.findByUserId(userId);

        if (training.isPresent())
            return training.get();
        else
            throw new IllegalStateException("No current trainings for user with username: " + username);
    }

    private String getOnEndResponse(String username) {
        var training = getTraining(username);
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

        var words = training.getWords();

        if (training.getQuestionsLeft() > 0 && !words.isEmpty()) {

            if (training.getDebt() > 0) {
                var word = getRelatedWordIfAvailable(concept, training);
                if (word != null)
                    return word;
            }
            return getRandomWord(username);
        }

        var grade = getFinalGrade(training.getScore(), training.getMax());
        trainingClient.updateUserStats(username, grade);
        trainingRepository.delete(training);
        return grade;
    }

    private String getRandomWord(String username) {
        var training = getTraining(username);
        var words = training.getWords();

        if (words.isEmpty())
            throw new IllegalStateException("No words left");

        var index = rand.nextInt(words.size());
        var word = words.get(index);
        words.remove(index);
        training.setCurWord(word);
        training.setQuestionsLeft(training.getQuestionsLeft() - 1);
        trainingRepository.save(training);

        return word;
    }


    private String getRelatedWordIfAvailable(Concept concept, Training training) {
        var words = training.getWords();

        var relatedWord = concept
                .getRelated()
                .stream()
                .filter(words::contains)
                .findFirst();

        if (relatedWord.isPresent()) {
            var word = relatedWord.get();
            words.remove(word);
            training.setCurWord(word);
            training.setQuestionsLeft(training.getQuestionsLeft() - 1);
            trainingRepository.save(training);
            return word;
        }
        return null;
    }

    private void reevaluate(String answer, Concept concept, Training training) {
        var conceptScore = concept.getScore();
        var debt = training.getDebt();

        if (calculator.areSimilar(answer, concept.getDefinition())) {
            training.setScore(training.getScore() + conceptScore);
            if (debt > 0)
                training.setDebt(max(debt - conceptScore, 0));
        } else {
            training.setDebt(debt + conceptScore);
        }
        training.setMax(training.getMax() + conceptScore);
    }

    private String getFinalGrade(int score, int max) {
        var grade = (double) score / max;
        if (grade >= 0.9) {
            return "A";
        } else if (grade >= 0.8) {
            return "B";
        } else if (grade >= 0.7) {
            return "C";
        } else if (grade >= 0.6) {
            return "D";
        } else {
            return "F";
        }
    }
}
