package ovcharka.conceptservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import ovcharka.conceptservice.service.ConceptService;

import java.util.List;

@Configuration
public class Initializer {

    private final ConceptService conceptService;

    @Autowired
    public Initializer(ConceptService conceptService) {
        this.conceptService = conceptService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init(ApplicationReadyEvent event) {
        conceptService.updateConcepts(
                List.of("array", "bit", "buffer", "byte", "data", "hardware", "memory", "network",
                        "packet", "process", "protocol", "query", "software", "object", "variable",
                        "algorithm", "database", "transaction", "tree", "boolean", "bug", "C", "Java",
                        "compiler", "translator", "concurrency", "code", "map", "stack", "set", "heap",
                        "computer", "interface", "core", "Linux", "instruction", "multiprocessing",
                        "source code", "recursion", "register", "operating system", "computing", "cipher"));
    }
}
