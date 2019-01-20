package ovcharka.nlp.similarity;

public interface SimilarityCalculator {
    boolean areSimilar(String first, String second);

    double calcSimilarityScore(String first, String second);
}
