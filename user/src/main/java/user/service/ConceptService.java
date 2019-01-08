package user.service;

import admin.domain.Concept;
import admin.repository.ConceptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ConceptService {
    private static Random rand = new Random(System.nanoTime());

    private Set<String> words;
    private String cur = null;

    private int max = 0;
    private int score = 0;
    private int debt = 0;

    private int questionsLeft = 10;
    private boolean isStarted = false;

    private final ConceptRepository conceptRepository;

    @Autowired
    public ConceptService(ConceptRepository conceptRepository) {
        this.conceptRepository = conceptRepository;
    }

    private void loadConcepts() {
        words = conceptRepository
                .getAll()
                .stream()
                .map(Concept::getWord)
                .collect(Collectors.toSet());
    }

    private String getNextRandomWord() {
        cur = words.stream()
                   .skip(rand.nextInt(words.size()))
                   .findFirst().orElse(null);
        words.remove(cur);
        return cur;
    }

    public String process(String answer) {
        if (!isStarted) {
            loadConcepts();
            isStarted = true;
            return getNextRandomWord();
        } else {
            check(answer);

            if (!words.isEmpty() && questionsLeft-- > 0) {
                if (debt > 0) {
                    cur = conceptRepository
                            .getClosest(cur)
                            .stream()
                            .map(Concept::getWord)
                            .filter(words::contains)
                            .findFirst().orElse(null);

                    if (cur != null) {
                        words.remove(cur);
                        return cur;
                    }
                }
                return getNextRandomWord();
            }

            isStarted = false;
            questionsLeft = 10;
            return getFinalGrade();
        }
    }

    private void check(String userAnswer) {
        var concept = conceptRepository.findByWord(cur);
        var actualAnswer = concept.getDefinition();
        var conceptScore = concept.getScore();

        max += conceptScore;
        reevaluate(userAnswer, actualAnswer, conceptScore);
    }

    private void reevaluate(String userAnswer, String actualAnswer, int wordScore) {
        if (isSimilar(userAnswer, actualAnswer)) {
            score += wordScore;
            if (debt > 0)
                debt = Math.max(debt - wordScore, 0);
        } else {
            debt += wordScore;
        }
    }

    private boolean isSimilar(String userAnswer, String actualAnswer) {
        return userAnswer.equals(actualAnswer);
    }


    private String getFinalGrade() {
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