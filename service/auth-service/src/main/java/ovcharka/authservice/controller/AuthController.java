package ovcharka.authservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ovcharka.authservice.payload.SignUpRequest;
import ovcharka.authservice.service.AuthService;
import ovcharka.common.controller.AbstractController;
import ovcharka.common.payload.response.AbstractResponse;
import ovcharka.common.payload.response.BooleanResponse;
import ovcharka.userservice.payload.request.UserUpdateRequest;

@RestController
public class AuthController extends AbstractController {

    private final AuthService authService;

    private final BCryptPasswordEncoder encoder;

    @Autowired
    public AuthController(AuthService authService, BCryptPasswordEncoder encoder) {
        this.authService = authService;
        this.encoder = encoder;
    }

    // TODO: 22/01/2019 error when username exists
    @PostMapping("/signup")
    ResponseEntity<AbstractResponse> signup(@RequestBody SignUpRequest request) {
        return getResponse(
                () -> {
                    authService.updateUser(
                            new UserUpdateRequest(
                                    request.getName(),
                                    request.getUsername(),
                                    encoder.encode(request.getPassword()),
                                    "USER"
                            )
                    );
                    return new BooleanResponse(true);
                }
        );
    }
}