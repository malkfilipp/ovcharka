package ovcharka.conceptservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ovcharka.conceptservice.domain.Concept;
import ovcharka.conceptservice.repository.ConceptRepository;
import ovcharka.nlp.disambiguation.Classifier;
import ovcharka.nlp.similarity.SimilarityCalculator;
import ovcharka.nlp.similarity.WordSimilarityCalculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import static java.util.Comparator.reverseOrder;
import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toList;

@Service
public class ConceptService {

    private final ConceptRepository conceptRepository;

    private final Classifier classifier = new Classifier();
    private final SimilarityCalculator calculator = new WordSimilarityCalculator();

    @Autowired
    public ConceptService(ConceptRepository conceptRepository) {
        this.conceptRepository = conceptRepository;
    }

    public List<Concept> findAll() {
        return conceptRepository.findAll();
    }

    public Concept findByWord(String word) {
        var concept = conceptRepository.findByWord(word);

        if (concept.isPresent())
            return concept.get();
        else
            throw new IllegalArgumentException("No such word");
    }

    public List<String> findAllWords() {
        return conceptRepository
                .findAll()
                .stream()
                .map(Concept::getWord)
                .collect(toList());
    }

    public List<String> findRelated(String word) {
        var concept = conceptRepository.findByWord(word);

        if (concept.isPresent())
            return concept.get().getRelated();
        else
            throw new IllegalArgumentException("No such word");
    }

    public void updateConcepts(List<String> words) {
        conceptRepository.deleteAll();
        var concepts = getConceptsFrom(words);
        conceptRepository.saveAll(concepts);
    }

    private List<Concept> getConceptsFrom(List<String> words) {
        var concepts = words
                .stream()
                .map(word -> new Concept(null, word,
                                         classifier.getDefinition(word),
                                         classifier.getSignificance(word),
                                         new ArrayList<>())
                ).collect(toList());

        var map = new HashMap<String, Double>();
        concepts.forEach(
                concept -> {
                    concepts.stream()
                            .map(Concept::getWord)
                            .filter(word -> !concept.getWord().equals(word))
                            .forEach(word -> {
                                double score = calculator.calcSimilarityScore(concept.getWord(), word);
                                if (score > 0) map.put(word, score);
                            });

                    map.entrySet()
                       .stream()
                       .sorted(comparingByValue(reverseOrder()))
                       .limit(10)
                       .map(Entry::getKey)
                       .forEach(word -> concept.getRelated().add(word));

                    map.clear();
                }
        );
        return concepts;
    }

    public void updateConcept(Concept concept) {
        var found = conceptRepository
                .findByWord(concept.getWord());

        if (found.isPresent()) {
            var updated = found.get();
            updated.setDefinition(concept.getDefinition());
            updated.setScore(concept.getScore());
            conceptRepository.save(updated);
        } else {
            var words = findAllWords();
            words.add(concept.getWord());
            updateConcepts(words);
        }
    }
}
