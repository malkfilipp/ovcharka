package ovcharka.nlp.similarity;

import ovcharka.nlp.disambiguation.Lemmatizer;

import java.util.*;
import java.util.stream.Collectors;

public class SentenceSimilarityCalculator implements SimilarityCalculator {
    private Lemmatizer lemmatizer = new Lemmatizer();
    private WordSimilarityCalculator wordSimilarityCalculator = new WordSimilarityCalculator();

    @Override
    public boolean areSimilar(String first, String second) {
//        return calcSimilarityScore(first, second) > 0.6;
        return first.equals(second);
    }

    @Override
    public double calcSimilarityScore(String sentence1, String sentence2) {
        var terms1 = new HashSet<>(lemmatizer.lemmatize(sentence1));
        var terms2 = new HashSet<>(lemmatizer.lemmatize(sentence2));

        System.out.println("terms1 = " + terms1);
        System.out.println("terms2 = " + terms2);

        var union = getUnion(terms1, terms2);

        var vector1 = getSemanticVector(union, terms1);
        var vector2 = getSemanticVector(union, terms2);

        return getCos(vector1, vector2);
    }

    private static HashSet<String> getUnion(Set<String> terms1, Set<String> terms2) {
        var union = new HashSet<String>();
        union.addAll(terms1);
        union.addAll(terms2);
        return union;
    }

    private List<Double> getSemanticVector(Set<String> union, Set<String> terms) {
        return union.stream()
                    .map(i -> terms.stream()
                                   .map(j -> wordSimilarityCalculator.calcSimilarityScore(i, j))
                                   .mapToDouble(Double::doubleValue).max().orElse(0))
                    .map(score -> (score == Double.MAX_VALUE) ? 1 : score)
                    .collect(Collectors.toList());
    }

    private static double getCos(List<Double> vector1, List<Double> vector2) {
        var dotProduct = 0.0;
        var norm1 = 0.0;
        var norm2 = 0.0;

        for (int i = 0; i < vector1.size(); i++) {
            dotProduct += vector1.get(i) * vector2.get(i);
            norm1 += Math.pow(vector1.get(i), 2);
            norm2 += Math.pow(vector2.get(i), 2);
        }

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }
}