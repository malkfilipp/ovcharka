package ovcharka.userservice.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import ovcharka.userservice.domain.WordList;
import reactor.core.publisher.Mono;

@Repository
public interface WordListRepository extends ReactiveMongoRepository<WordList, String> {

    Mono<WordList> findByUserId(String userId);
}
