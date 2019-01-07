package user;

import admin.domain.Concept;
import admin.repository.ConceptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootApplication
@EntityScan("admin.domain")
@ComponentScan("admin.repository")
@EnableNeo4jRepositories("admin.repository")
public class UserApplication implements CommandLineRunner {

    private Set<String> words;

    private final ConceptRepository conceptRepository;

    @Autowired
    public UserApplication(ConceptRepository conceptRepository) {
        this.conceptRepository = conceptRepository;
    }

    private static Random rand = new Random(System.nanoTime());

    private String getNextRandomWord() {
        return words.stream()
                    .skip(rand.nextInt(words.size()))
                    .findFirst()
                    .orElse(null);
    }

    private void loadGraph() {
        words = conceptRepository.getAll()
                                 .stream()
                                 .map(Concept::getWord)
                                 .collect(Collectors.toSet());
//        System.out.println("words = " + words);
    }

    public static void main(String[] args) {
        var app = new SpringApplication(UserApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    private Scanner scanner = new Scanner(System.in);
    private int max = 0;
    private int score = 0;
    private int debt = 0;

    @Override
    public void run(String... args) {
        loadGraph();

        while (words.size() > 0) {
            var word = getNextRandomWord();
            askAndCheck(word);

            while (debt < 0 && words.size() > 0) {
                word = conceptRepository.getClosest(word)
                                        .stream()
                                        .map(Concept::getWord)
                                        .filter(words::contains)
                                        .findFirst().orElse(null);

                if (word == null) break;
                askAndCheck(word);
            }
        }

        System.out.print("Grade: " + getFinalGrade());
    }

    private void askAndCheck(String word) {
        words.remove(word);
        System.out.println("Question: " + word);
        var keyword = conceptRepository.findByWord(word);

        System.out.println("Enter answer: ");
        var userAnswer = scanner.nextLine();
        var actualAnswer = keyword.getDefinition();

        var wordScore = keyword.getScore();
        max += wordScore;
        reevaluate(userAnswer, actualAnswer, wordScore);
        System.out.println("Questions left: " + words);
    }

    private void reevaluate(String userAnswer, String actualAnswer, int wordScore) {
        if (isSimilar(userAnswer, actualAnswer)) {
            score += wordScore;
            if (debt > 0)
                debt = Math.max(debt - wordScore, 0);
        } else {
            debt += wordScore;
        }

        System.out.println("max = " + max);
        System.out.println("score = " + score);
        System.out.println("debt = " + debt);
    }

    public boolean isSimilar(String userAnswer, String actualAnswer) {
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