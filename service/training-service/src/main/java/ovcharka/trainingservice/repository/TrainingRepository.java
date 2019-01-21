package ovcharka.trainingservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ovcharka.trainingservice.domain.Training;

import java.util.Optional;

@Repository
public interface TrainingRepository extends MongoRepository<Training, String> {

    Optional<Training> findByUserId(String userId);
}
