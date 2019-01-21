package ovcharka.trainingservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.web.client.RestTemplate;
import ovcharka.trainingservice.repository.TrainingRepository;

@Configuration
public class Initializer {

    private final TrainingRepository trainingRepository;

    @Autowired
    public Initializer(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init(ApplicationReadyEvent event) {
        trainingRepository.deleteAll();
    }
}
