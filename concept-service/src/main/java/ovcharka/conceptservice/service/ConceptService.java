package ovcharka.conceptservice.service;

import nlp.disambiguation.Classifier;
import nlp.similarity.SimilarityCalculator;
import nlp.similarity.WordSimilarityCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ovcharka.conceptservice.domain.Concept;
import ovcharka.conceptservice.payload.ConceptsUpdateRequest;
import ovcharka.conceptservice.payload.WordUpdateRequest;
import ovcharka.conceptservice.repository.ConceptRepository;
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

    public Flux<Concept> findAll() {
        return conceptRepository.findAll();
    }

    public Mono<Concept> findByWord(String word) {
        return conceptRepository.findByWord(word);
    }

    public Flux<String> findAllWords() {
        return conceptRepository.findAll().map(Concept::getWord);
    }

    public Flux<String> findRelated(String word) {
        return conceptRepository
                .findByWord(word)
                .map(Concept::getRelated)
                .flatMapMany(Flux::fromIterable);
    }

    // TODO: 13/01/2019 add success indicator
    public void updateConcepts(List<String> words) {
        conceptRepository
                .deleteAll()
                .thenMany(
                        Flux.fromIterable(getConceptsFrom(words))
                            .flatMap(conceptRepository::save)
                ).subscribe();
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

    public void updateConcept(Concept concept) {
        conceptRepository
                .findByWord(concept.getWord())
                .map(updated -> {
                    updated.setDefinition(concept.getDefinition());
                    updated.setScore(concept.getScore());
                    return updated;
                }).flatMap(conceptRepository::save)
                .switchIfEmpty(
                        Mono.defer(() -> findAllWords()
                                .collectList()
                                .map(list -> {
                                    list.add(concept.getWord());
                                    return list;
                                }).doOnNext(this::updateConcepts)
                                .then(Mono.empty()))
                ).subscribe();

    }
}
