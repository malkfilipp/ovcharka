package ovcharka.userservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import ovcharka.userservice.domain.User;
import ovcharka.userservice.domain.WordList;
import ovcharka.userservice.service.UserService;
import ovcharka.userservice.service.WordListService;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class Initializer {

    private final UserService userService;
    private final WordListService wordListService;

    @Autowired
    public Initializer(UserService userService, WordListService wordListService) {
        this.userService = userService;
        this.wordListService = wordListService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init(ApplicationReadyEvent event) {
        userService.deleteAll().then(
                wordListService.deleteAll()
        ).then(userService.updateByUsername(
                new User(null, "John", "john", "pw", null)
        )).then(userService.updateByUsername(
                new User(null, "Mary", "mary", "pw2", null)
        )).then(wordListService.update(
                "john",
                List.of("array", "bit", "buffer", "byte", "data", "hardware", "memory", "network",
                        "packet", "process", "protocol", "query", "software", "object", "variable",
                        "algorithm", "database", "transaction", "tree", "boolean", "bug", "C", "Java",
                        "compiler", "translator", "concurrency", "code", "map", "stack", "set", "heap",
                        "computer", "interface", "core", "Linux", "instruction", "multiprocessing",
                        "source code", "recursion", "register", "operating system", "computing", "cipher")

        )).then(
                wordListService.update("mary", new ArrayList<>())
        ).subscribe();
    }
}
