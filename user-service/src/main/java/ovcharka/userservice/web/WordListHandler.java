package ovcharka.userservice.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import ovcharka.userservice.payload.request.WordListUpdateRequest;
import ovcharka.userservice.payload.response.BooleanResponse;
import ovcharka.userservice.payload.response.SuccessResponse;
import ovcharka.userservice.payload.response.WordResponse;
import ovcharka.userservice.service.WordListService;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;

@Component
public class WordListHandler extends AbstractHandler {

    private final WordListService wordListService;

    @Autowired
    public WordListHandler(WordListService wordListService) {
        this.wordListService = wordListService;
    }

    Mono<ServerResponse> update(ServerRequest request) {
        return request.bodyToMono(WordListUpdateRequest.class)
                      .flatMap(req -> {
                          var response = wordListService
                                  .update(req.getUsername(), req.getWords())
                                  .map(it -> new SuccessResponse());
                          return getResponse(response, SuccessResponse.class, badRequest(), "No such user");
                      });
    }

    Mono<ServerResponse> getRandomWord(ServerRequest request) {
        return request.queryParam("username")
                      .map(username -> {
                          var response = wordListService
                                  .getRandomWord(username)
                                  .map(WordResponse::new);
                          return getResponse(response, WordResponse.class, badRequest(), "No such user");
                      }).orElse(getErrorResponse(badRequest(), "Username parameter is required"))
                      .onErrorResume(e -> getErrorResponse(badRequest(), e.getMessage()));
    }

    Mono<ServerResponse> contains(ServerRequest request) {
        var username = request.queryParam("username");
        var word = request.queryParam("word");

        if (username.isEmpty() || word.isEmpty())
            return getErrorResponse(badRequest(), "Username and word parameters are required");

        var response = wordListService
                .contains(username.get(), word.get())
                .map(BooleanResponse::new);
        return getResponse(response, BooleanResponse.class, badRequest(), "No such user");
    }

    Mono<ServerResponse> delete(ServerRequest request) {
        var username = request.queryParam("username");
        var word = request.queryParam("word");

        if (username.isEmpty() || word.isEmpty())
            return getErrorResponse(badRequest(), "Username and word parameters are required");

        var response = wordListService
                .delete(username.get(), word.get())
                .map(BooleanResponse::new);
        return getResponse(response, BooleanResponse.class, badRequest(), "No such user");
    }
}
