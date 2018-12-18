package nlp.similarity;

public class SentenceSimilarityCalculatorTest {
    public static void main(String[] args) {
//                var sentence1 = "Consumers would still have to get a descrambling security card from their cable operator to plug into the set.";
//        var sentence1 = "a food that is eaten routinely and in such quantities that it constitutes a dominant portion" +
//                " of a standard diet for a given people  supplying a large fraction of energy needs";
        var sentence1 = "A software program that transforms high-level source code that is written by a developer in " +
                "a high-level programming language into a low level object code (binary code) in machine language, which can be understood by the processor. ";
//        var sentence2 = "To watch pay television, consumers would insert into the set a security card provided by their cable service.";
        var sentence2 = "A computer program that transforms computer code written in one programming language (the " +
                "source language) into another programming language (the target language).";

        var calculator = new SentenceSimilarityCalculator();

        var start = System.nanoTime();
        System.out.println(calculator.calcSimilarityScore(sentence1, sentence2));

        System.out.println((System.nanoTime() - start) / 1e6);
    }
}
