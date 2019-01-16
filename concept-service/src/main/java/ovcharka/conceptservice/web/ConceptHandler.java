package ovcharka.conceptservice.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import ovcharka.conceptservice.domain.Concept;
import ovcharka.conceptservice.payload.request.ConceptsUpdateRequest;
import ovcharka.conceptservice.payload.request.WordUpdateRequest;
import ovcharka.conceptservice.payload.response.*;
import ovcharka.conceptservice.service.ConceptService;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.ServerResponse.*;
import static ovcharka.conceptservice.payload.response.AbstractResponse.*;
import static reactor.core.publisher.Mono.just;

@Component
public class ConceptHandler {

    private final ConceptService conceptService;

    @Autowired
    public ConceptHandler(ConceptService conceptService) {
        this.conceptService = conceptService;
    }

    private <T> Mono<ServerResponse> getServerResponse(Mono<T> response, Class<T> responseClass) {
        return getServerResponse(response, responseClass,
                                 status(INTERNAL_SERVER_ERROR), INTERNAL_ERROR_MESSAGE);
    }

    private <T> Mono<ServerResponse> getServerResponse(Mono<T> successResponse, Class<T> successResponseClass,
                                                       BodyBuilder errorStatus, String errorMessage) {
        return successResponse.flatMap(resp -> getSuccessResponse(just(resp), successResponseClass))
                              .switchIfEmpty(getErrorResponse(errorStatus, errorMessage))
                              .timeout(TIMEOUT, getErrorResponse(status(INTERNAL_SERVER_ERROR), TIMEOUT_MESSAGE));
    }

    private Mono<ServerResponse> getErrorResponse(BodyBuilder status, String timeoutMessage) {
        return status.body(fromObject(new ErrorResponse(timeoutMessage)));
    }

    private <T> Mono<ServerResponse> getSuccessResponse(Mono<T> response, Class<T> tClass) {
        return ok().body(response, tClass);
    }

    Mono<ServerResponse> findOneOrAll(ServerRequest request) {
        return request
                .queryParam("word")
                .map(word -> {
                    var response = conceptService
                            .findByWord(word)
                            .map(concept -> new ConceptResponse(SUCCESS, concept, null));
                    return getServerResponse(response, ConceptResponse.class, badRequest(), "No such word");
                })
                .orElseGet(() -> {
                               var response = conceptService
                                       .findAll()
                                       .collectList()
                                       .map(list -> new ConceptListResponse(SUCCESS, list, null));
                               return getServerResponse(response, ConceptListResponse.class);
                           }
                );
    }

    Mono<ServerResponse> findAllWords(ServerRequest request) {
        var response = conceptService
                .findAllWords()
                .collectList()
                .map(list -> new WordListResponse(SUCCESS, list, null));
        return getServerResponse(response, WordListResponse.class);
    }

    Mono<ServerResponse> findRelated(ServerRequest request) {
        return request.queryParam("word")
                      .map(word -> {
                          var response = conceptService
                                  .findRelated(word)
                                  .map(list -> new WordListResponse(SUCCESS, list, null));
                          return getServerResponse(response, WordListResponse.class, badRequest(), "No such word");
                      }).orElse(getErrorResponse(badRequest(), "Word parameter is required"));
    }

    Mono<ServerResponse> updateConcepts(ServerRequest request) {
        return request.bodyToMono(ConceptsUpdateRequest.class)
                      .map(ConceptsUpdateRequest::getWords)
                      .flatMap(words -> {
                          var response = conceptService
                                  .updateConcepts(words)
                                  .collectList()
                                  .map(it -> new SuccessResponse());
                          return getServerResponse(response, SuccessResponse.class);
                      });
    }

    Mono<ServerResponse> updateConcept(ServerRequest request) {
        return request.bodyToMono(WordUpdateRequest.class)
                      .map(req -> new Concept(null, req.getWord(), req.getDefinition(), req.getScore(), null))
                      .flatMap(concept -> {
                          var response = conceptService
                                  .updateConcept(concept)
                                  .map(it -> new SuccessResponse());
                          return getServerResponse(response, SuccessResponse.class);
                      });
    }
}
