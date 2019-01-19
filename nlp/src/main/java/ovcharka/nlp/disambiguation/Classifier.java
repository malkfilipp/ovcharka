package ovcharka.nlp.disambiguation;

import java.security.SecureRandom;

public class Classifier {
    private static SecureRandom rnd = new SecureRandom();

    public String getDefinition(String word) {
        return "1";
    }

    //maybe smth like page rank in wordnet world
    public int getSignificance(String word) {
        return rnd.nextInt(5) + 1;
    }
}
