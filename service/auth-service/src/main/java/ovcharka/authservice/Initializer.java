package ovcharka.authservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ovcharka.authservice.service.AuthService;
import ovcharka.userservice.domain.Role;
import ovcharka.userservice.payload.request.UserUpdateRequest;

@Configuration
public class Initializer {

    private final AuthService userDetailsService;

    private final BCryptPasswordEncoder encoder;

    @Autowired
    public Initializer(AuthService userDetailsService, BCryptPasswordEncoder encoder) {
        this.userDetailsService = userDetailsService;
        this.encoder = encoder;
    }


    @EventListener(ApplicationReadyEvent.class)
    public void init(ApplicationReadyEvent event) {
        userDetailsService.updateUser(
                new UserUpdateRequest("John", "john",
                                      encoder.encode("password1"),
                                      Role.ADMIN.toString())
        );

        userDetailsService.updateUser(
                new UserUpdateRequest("Mary", "mary",
                                      encoder.encode("password2"),
                                      Role.USER.toString())
        );
    }
}
