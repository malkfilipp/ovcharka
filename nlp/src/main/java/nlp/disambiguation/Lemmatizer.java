package nlp.disambiguation;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import nlp.similarity.SentenceSimilarityCalculator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Lemmatizer {
    private static StanfordCoreNLP pipeline;

    static {
        var props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");
        pipeline = new StanfordCoreNLP(props);
    }

    private static final String STOPWORDS_TXT = "stopwords.txt";
    private static final Set<String> stopwords = new HashSet<>();

    static {
        var uri = getUri(STOPWORDS_TXT);
        try (var lines = Files.lines(Paths.get(uri))) {
            lines.forEach(stopwords::add);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load " + STOPWORDS_TXT, e);
        }
    }

    private static URI getUri(String filename) {
        var url = SentenceSimilarityCalculator.class.getClassLoader().getResource(filename);
        if (url == null)
            throw new UncheckedIOException(new FileNotFoundException(filename));

        URI uri;
        try {
            uri = url.toURI();
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException(ex);
        }
        return uri;
    }

    public List<String> lemmatize(String text) {
        var document = new Annotation(text);
        pipeline.annotate(document);
        var sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        return sentences.stream()
                        .flatMap(sentence -> sentence.get(CoreAnnotations.TokensAnnotation.class).stream())
                        .map(CoreLabel::lemma)
                        .filter(term -> !stopwords.contains(term))
                        .collect(Collectors.toList());
    }
}
