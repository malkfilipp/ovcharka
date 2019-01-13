package ovcharka.conceptservice.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import ovcharka.conceptservice.domain.Concept;
import ovcharka.conceptservice.payload.ConceptsUpdateRequest;
import ovcharka.conceptservice.payload.WordUpdateRequest;
import ovcharka.conceptservice.service.ConceptService;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;
import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class ConceptHandler {

    private final ConceptService conceptService;

    @Autowired
    public ConceptHandler(ConceptService conceptService) {
        this.conceptService = conceptService;
    }

    Mono<ServerResponse> findOneOrAll(ServerRequest request) {
        return request.queryParam("word")
                      .map(word -> ok().body(conceptService.findByWord(word), Concept.class))
                      .orElseGet(() -> ok().body(conceptService.findAll(), Concept.class));
    }

    private final ParameterizedTypeReference<List<String>> stringListClass = new ParameterizedTypeReference<>() {};

    // without collectList could be just a concatenated string (not json array)
    Mono<ServerResponse> findAllWords(ServerRequest request) {
        return ok().body(fromPublisher(conceptService.findAllWords().collectList(), stringListClass));
    }

    Mono<ServerResponse> findRelated(ServerRequest request) {
        return request.queryParam("word")
                      .map(word -> ok().body(conceptService.findRelated(word).collectList(), stringListClass))
                      .orElse(badRequest().body(fromObject("Word parameter is required")));
    }

    // TODO: 13/01/2019 add error processing
    Mono<ServerResponse> updateConcepts(ServerRequest request) {
        return request.bodyToMono(ConceptsUpdateRequest.class)
                      .map(ConceptsUpdateRequest::getWords)
                      .doOnNext(conceptService::updateConcepts)
                      .then(ok().build());
    }

    Mono<ServerResponse> updateConcept(ServerRequest request) {
        return request.bodyToMono(WordUpdateRequest.class)
                      .map(req -> new Concept(null, req.getWord(), req.getDefinition(), req.getScore(), null))
                      .doOnNext(conceptService::updateConcept)
                      .then(ok().build());
    }
}
