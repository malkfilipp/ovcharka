package ovcharka.nlp.similarity;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class SentenceSimilarityCalculatorTest {

    private Logger logger = LoggerFactory.getLogger(SentenceSimilarityCalculatorTest.class);

    @Test
    public void sentencesShouldBeSimilar() {
        checkSentences("src/test/resources/similar.txt", true);
    }

    @Test
    public void sentencesShouldNotBeSimilar() {
        checkSentences("src/test/resources/not-similar.txt", false);
    }

    private void checkSentences(String filename, boolean shouldBeSimilar) {
        var file = new File((filename));

        var calculator = new SentenceSimilarityCalculator();
        try (Scanner scanner = new Scanner(file)) {

            while (true) {
                String words;
                String first;
                String second;

                if (!scanner.hasNextLine()) break;

                words = scanner.nextLine();
                logger.info("Comparing words " + words);

                if (!scanner.hasNextLine()) {
                    logger.error("No definition provided for the first word");
                    break;
                }
                first = scanner.nextLine();

                if (!scanner.hasNextLine()) {
                    logger.error("No definition provided for the second word");
                    break;
                }
                second = scanner.nextLine();

                var areSimilar = calculator.areSimilar(first, second);

                if (areSimilar != shouldBeSimilar) {
                    if (shouldBeSimilar)
                        logger.error("Words should be similar while they are not");
                    else
                        logger.error("Words should not be similar while they are");
                }

                assertEquals(areSimilar, shouldBeSimilar);

                if (!scanner.hasNextLine()) break;
                scanner.nextLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
