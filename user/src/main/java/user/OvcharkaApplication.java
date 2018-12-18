package user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;


import org.springframework.boot.CommandLineRunner;
import user.domain.Keyword;
import user.repository.KeywordRepository;

import java.util.*;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableNeo4jRepositories("user.repository")
public class OvcharkaApplication implements CommandLineRunner {

    private Set<String> words;

    private final KeywordRepository keywordRepository;

    @Autowired
    public OvcharkaApplication(KeywordRepository keywordRepository) {
        this.keywordRepository = keywordRepository;
    }

    private static Random rand = new Random(System.nanoTime());

    private String getNextRandomWord() {
        return words.stream()
                    .skip(rand.nextInt(words.size()))
                    .findFirst()
                    .orElse(null);
    }

    private void loadGraph() {
        words = keywordRepository.graph()
                                 .stream()
                                 .map(Keyword::getWord)
                                 .collect(Collectors.toSet());
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(OvcharkaApplication.class);
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
                word = keywordRepository.getClosest(word)
                                        .stream()
                                        .map(Keyword::getWord)
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
        var keyword = keywordRepository.findByWord(word);

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