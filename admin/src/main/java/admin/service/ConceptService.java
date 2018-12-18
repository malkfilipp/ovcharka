package admin.service;

import admin.config.ConceptServiceProperties;
import admin.util.CsvGenerator;
import admin.domain.Concept;
import admin.domain.Edge;
import nlp.disambiguation.Classifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import nlp.similarity.SimilarityCalculator;
import nlp.similarity.WordSimilarityCalculator;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConceptService {
    private List<Concept> concepts = new ArrayList<>();
    private static List<Edge> edges = new ArrayList<>();

    private SimilarityCalculator calculator = new WordSimilarityCalculator();
    private Classifier classifier = new Classifier();

    private final ConceptServiceProperties properties;

    @Autowired
    public ConceptService(ConceptServiceProperties properties) {
        this.properties = properties;
    }

    public void generateCsv() throws IOException {
        loadConcepts();
        CsvGenerator.generate(concepts, properties.getConceptsOutputPath());
        calcEdges();
        CsvGenerator.generate(edges, properties.getEdgesOutputPath());
    }

    private void loadConcepts() {
        var conceptList = properties.getConceptListPath();

        try (var lines = Files.lines(Paths.get(conceptList))) {
            lines.forEach(word -> {
                var concept = new Concept();
                concept.setWord(word);
                concept.setDefinition(classifier.getDefinition(word));
                concept.setScore(classifier.getSignificance(word));
                concepts.add(concept);
            } );
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load " + conceptList, e);
        }
    }

    private void calcEdges() {
        for (int i = 0; i < concepts.size(); i++)
            for (int j = i + 1; j < concepts.size(); j++) {
                var word1 = concepts.get(i).getWord();
                var word2 = concepts.get(j).getWord();
                edges.add(new Edge(word1, word2, calculator.calcSimilarityScore(word1, word2)));
            }
    }
}
