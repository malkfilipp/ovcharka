package admin.service;

import admin.config.AdminProperties;
import admin.domain.Concept;
import admin.domain.Edge;
import admin.repository.ConceptRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import nlp.disambiguation.Classifier;
import nlp.similarity.SimilarityCalculator;
import nlp.similarity.WordSimilarityCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConceptService {
    private List<Concept> concepts = new ArrayList<>();
    private List<Edge> edges = new ArrayList<>();

    private SimilarityCalculator calculator = new WordSimilarityCalculator();
    private Classifier classifier = new Classifier();

    private final AdminProperties properties;
    private final ConceptRepository conceptRepository;

    @Autowired
    public ConceptService(AdminProperties properties, ConceptRepository conceptRepository) {
        this.properties = properties;
        this.conceptRepository = conceptRepository;
    }

    public void generate() throws IOException {
        conceptRepository.clear();

        loadConceptsFromList();
        var path = properties.getNeo4jImport().getPath();
        var conceptsFilename = properties.getNeo4jImport().getConceptsFilename();
        var conceptsFile = path + conceptsFilename;
        Files.write(Paths.get(conceptsFile), toCsv(concepts).getBytes());
        conceptRepository.loadConceptsFromCsv();

        calcEdges();
        var edgesFilename = properties.getNeo4jImport().getEdgesFilename();
        var edgesFile = path + edgesFilename;
        Files.write(Paths.get(edgesFile), toCsv(edges).getBytes());
        conceptRepository.loadEdgesFromCsv();

        conceptRepository.createIndexOnWord();
    }

    private void loadConceptsFromList() {
        var conceptList = properties.getInputConceptListFile();

        try (var lines = Files.lines(Paths.get(conceptList))) {
            lines.forEach(word -> concepts.add(
                    new Concept(null, word, classifier.getDefinition(word), classifier.getSignificance(word))
            ));
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load " + conceptList, e);
        }
    }

    private static <T> String toCsv(List<T> list) throws JsonProcessingException {
        if (list.size() == 0) return "";
        var mapper = new CsvMapper();
        var schema = mapper.schemaFor(list.get(0).getClass()).withHeader();
        return mapper.writer(schema).writeValueAsString(list);
    }

    private void calcEdges() {
        for (int i = 0; i < concepts.size(); i++)
            for (int j = i + 1; j < concepts.size(); j++) {
                var word1 = concepts.get(i).getWord();
                var word2 = concepts.get(j).getWord();
                var similarityScore = calculator.calcSimilarityScore(word1, word2);
                if (similarityScore > 0)
                    edges.add(new Edge(word1, word2, similarityScore));
            }
    }
}
