package ovcharka.userservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import ovcharka.userservice.domain.User;
import ovcharka.userservice.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class Initializer {

    private final UserService userService;

    @Autowired
    public Initializer(UserService userService) {
        this.userService = userService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init(ApplicationReadyEvent event) {
        userService.deleteAll();
        userService.updateByUsername(User.with("John", "john", "pw"));
        userService.updateByUsername(User.with("Mary", "mary", "pw2"));
    }
}
