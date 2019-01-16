package ovcharka.conceptservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ovcharka.conceptservice.domain.Concept;
import ovcharka.conceptservice.repository.ConceptRepository;
import ovcharka.nlp.disambiguation.Classifier;
import ovcharka.nlp.similarity.SimilarityCalculator;
import ovcharka.nlp.similarity.WordSimilarityCalculator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    public Mono<List<Concept>> findAll() {
        return conceptRepository.findAll().collectList();
    }

    public Mono<Concept> findByWord(String word) {
        return conceptRepository.findByWord(word);
    }

    public Mono<List<String>> findAllWords() {
        return conceptRepository.findAll()
                                .map(Concept::getWord)
                                .collectList();
    }

    public Mono<List<String>> findRelated(String word) {
        return conceptRepository.findByWord(word)
                                .map(Concept::getRelated);
    }

    public Mono<List<Concept>> updateConcepts(List<String> words) {
        return conceptRepository
                .deleteAll()
                .thenMany(
                        Flux.fromIterable(getConceptsFrom(words))
                            .flatMap(conceptRepository::save)
                ).collectList();
    }

    private List<Concept> getConceptsFrom(List<String> words) {
        var concepts = words
                .stream()
                .map(word -> new Concept(null, word,
                                         classifier.getDefinition(word),
                                         classifier.getSignificance(word),
                                         new ArrayList<>()))
                .collect(toList());

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

    public Mono<Concept> updateConcept(Concept concept) {
        return conceptRepository
                .findByWord(concept.getWord())
                .map(updated -> {
                    updated.setDefinition(concept.getDefinition());
                    updated.setScore(concept.getScore());
                    return updated;
                }).flatMap(conceptRepository::save)
                .switchIfEmpty(
                        Mono.defer(() -> findAllWords()
                                .map(list -> {
                                    list.add(concept.getWord());
                                    return list;
                                }).flatMap(this::updateConcepts)
                                .flatMap(list -> findByWord(concept.getWord())))
                );

    }
}
