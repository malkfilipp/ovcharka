package ovcharka.userservice.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import ovcharka.userservice.domain.Stats;
import ovcharka.userservice.domain.User;
import ovcharka.userservice.payload.request.UserUpdateRequest;
import ovcharka.userservice.payload.response.SuccessResponse;
import ovcharka.userservice.payload.response.UserResponse;
import ovcharka.userservice.service.UserService;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;

@Component
public class UserHandler extends AbstractHandler {

    private final UserService userService;

    @Autowired
    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    Mono<ServerResponse> findByUsername(ServerRequest request) {
        return request.queryParam("username")
                      .map(username -> {
                          var response = userService
                                  .findByUsername(username)
                                  .map(UserResponse::new);
                          return getResponse(response, UserResponse.class, badRequest(), "No such user");
                      }).orElse(getErrorResponse(badRequest(), "Username parameter is required"));
    }

    Mono<ServerResponse> updateUser(ServerRequest request) {
        return request.bodyToMono(UserUpdateRequest.class)
                      .map(req -> new User(null, req.getName(), req.getUsername(), req.getPassword(), null))
                      .flatMap(user -> {
                          var response = userService
                                  .updateByUsername(user)
                                  .map(it -> new SuccessResponse());
                          return getResponse(response, SuccessResponse.class);
                      });
    }
}
