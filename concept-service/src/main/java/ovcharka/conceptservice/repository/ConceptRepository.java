package ovcharka.conceptservice.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ovcharka.conceptservice.domain.Concept;
import reactor.core.publisher.Mono;

public interface ConceptRepository extends ReactiveMongoRepository<Concept, String> {

    Mono<Concept> findByWord(String word);
}
