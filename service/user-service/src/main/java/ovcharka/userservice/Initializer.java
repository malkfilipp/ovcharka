package ovcharka.userservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ovcharka.userservice.domain.Role;
import ovcharka.userservice.domain.User;
import ovcharka.userservice.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static ovcharka.userservice.domain.Role.ADMIN;
import static ovcharka.userservice.domain.Role.USER;

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
//        userService.updateByUsername(User.with("John", "john", "password1"));
//        userService.updateByUsername(User.with("Mary", "mary", "password2"));
    }
}
