package ovcharka.nlp.similarity;

public interface SimilarityCalculator {
    double calcSimilarityScore(String first, String second);

    boolean areSimilar(String first, String second);
}
