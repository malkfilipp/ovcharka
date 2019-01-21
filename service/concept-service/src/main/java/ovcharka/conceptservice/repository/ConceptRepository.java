package ovcharka.conceptservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ovcharka.conceptservice.domain.Concept;

import java.util.Optional;

@Repository
public interface ConceptRepository extends MongoRepository<Concept, String> {

    Optional<Concept> findByWord(String word);
}
