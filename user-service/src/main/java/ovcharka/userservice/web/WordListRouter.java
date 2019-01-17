package ovcharka.userservice.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class WordListRouter {

    @Bean
    RouterFunction<ServerResponse> wordListRoutes(WordListHandler handler) {
        return route(POST("/users/words").and(contentType(APPLICATION_JSON)), handler::update)
                .andRoute(GET("/users/words/getRandomWord"), handler::getRandomWord)
                .andRoute(GET("/users/words/contains"), handler::contains)
                .andRoute(DELETE("/users/words"), handler::delete);
    }
}
