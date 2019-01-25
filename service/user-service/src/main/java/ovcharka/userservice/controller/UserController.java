package ovcharka.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ovcharka.common.controller.AbstractController;
import ovcharka.common.payload.response.AbstractResponse;
import ovcharka.common.payload.response.BooleanResponse;
import ovcharka.userservice.domain.Role;
import ovcharka.userservice.domain.User;
import ovcharka.userservice.payload.request.UserStatsUpdateRequest;
import ovcharka.userservice.payload.request.UserUpdateRequest;
import ovcharka.userservice.payload.response.UserResponse;
import ovcharka.userservice.service.UserService;

@RestController
public class UserController extends AbstractController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    ResponseEntity<AbstractResponse> getUser(@RequestParam String username) {
        return getResponse(
                () -> new UserResponse(
                        userService.findByUsername(username)
                )
        );
    }

    @PostMapping("/create")
    ResponseEntity<AbstractResponse> create(@RequestBody UserUpdateRequest request) {
        return getResponse(
                () -> {
                    userService.create(
                            new User(request.getName(),
                                     request.getUsername(),
                                     request.getPassword(),
                                     Role.valueOf(request.getRole()))
                    );
                    return new BooleanResponse(true);
                }
        );
    }

    @PostMapping("/update")
    ResponseEntity<AbstractResponse> updateUser(@RequestBody UserUpdateRequest request) {
        return getResponse(
                () -> {
                    userService.updateByUsername(
                            new User(request.getName(),
                                     request.getUsername(),
                                     request.getPassword(),
                                     Role.valueOf(request.getRole()))
                    );
                    return new BooleanResponse(true);
                }
        );
    }

    @PostMapping("/stats")
    ResponseEntity<AbstractResponse> updateUserStats(@RequestBody UserStatsUpdateRequest request) {
        return getResponse(
                () -> {
                    userService.updateUserStats(request.getUsername(), request.getGrade());
                    return new BooleanResponse(true);
                }
        );
    }
}
