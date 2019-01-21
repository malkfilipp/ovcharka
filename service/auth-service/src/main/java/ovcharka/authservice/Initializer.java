package ovcharka.authservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ovcharka.authservice.service.UserDetailsServiceImpl;
import ovcharka.userservice.domain.Role;
import ovcharka.userservice.payload.request.UserUpdateRequest;

@Configuration
public class Initializer {

    private final UserDetailsServiceImpl userDetailsService;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public Initializer(UserDetailsServiceImpl userDetailsService, BCryptPasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }


    @EventListener(ApplicationReadyEvent.class)
    public void init(ApplicationReadyEvent event) {
        userDetailsService.updateUser(
                new UserUpdateRequest("John", "john",
                                      passwordEncoder.encode("password1"),
                                      Role.USER.toString())
        );

        userDetailsService.updateUser(
                new UserUpdateRequest("Mary", "mary",
                                      passwordEncoder.encode("password2"),
                                      Role.USER.toString())
        );
    }
}
