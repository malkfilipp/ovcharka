package ovcharka.conceptservice.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ConceptRouter {

    @Bean
    RouterFunction<ServerResponse> routes(ConceptHandler handler) {
        return route(GET("/concepts"), handler::findOneOrAll)
                .andRoute(GET("/words"), handler::findAllWords)
                .andRoute(POST("/words").and(contentType(APPLICATION_JSON)), handler::updateConcepts)
                .andRoute(POST("/word").and(contentType(APPLICATION_JSON)), handler::updateConcept)
                .andRoute(GET("/related"), handler::findRelated);
    }
}
