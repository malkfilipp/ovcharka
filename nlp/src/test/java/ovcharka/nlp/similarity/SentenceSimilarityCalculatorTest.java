package ovcharka.nlp.similarity;

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
//
//        var sentence1 = "a general-purpose computer-programming language that is concurrent, class-based, " +
//                "object-oriented, and specifically designed to have as few implementation dependencies as possible.";
//        var sentence2 = "is a high-level, interpreted programming language that conforms to the ECMAScript specification. It is a language that is also characterized as dynamic, weakly typed, prototype-based and multi-paradigm.";
//        var sentence1 = "A general-purpose, imperative computer programming language, supporting structured programming, lexical variable scope and recursion, while a static type system prevents many unintended operations.";
//        var sentence2 = "A general-purpose programming language, supporting structured programming, with a static " +
//                "type system that prevents many unintended operations.";
//        var sentence1 = "A group of expectations that shape experience by making people especially sensitive to " +
//                "specific kinds of information";
//        var sentence2 = "An abstract data type that can store unique values, without any particular order.\n" +
//                "It is a computer implementation of the mathematical concept of a finite set.";
//        var sentence1 = "An abstract data that stores data in the form of key and value pairs where every key is " +
//                "unique.";
//        var sentence2 = "An abstract data type composed of a collection of (key, value) pairs, such that each possible key appears at most once in the collection.";

        var calculator = new SentenceSimilarityCalculator();

        var start = System.nanoTime();
        System.out.println(calculator.calcSimilarityScore(sentence1, sentence2));

        System.out.println((System.nanoTime() - start) / 1e6);
    }
}
