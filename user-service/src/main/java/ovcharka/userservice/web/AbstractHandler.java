package ovcharka.userservice.web;

import org.springframework.web.reactive.function.server.ServerResponse;
import ovcharka.userservice.payload.response.ErrorResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.status;
import static ovcharka.userservice.payload.response.AbstractResponse.INTERNAL_ERROR_MESSAGE;
import static ovcharka.userservice.payload.response.AbstractResponse.TIMEOUT;
import static ovcharka.userservice.payload.response.AbstractResponse.TIMEOUT_MESSAGE;
import static reactor.core.publisher.Mono.just;

// TODO: 17/01/2019 move to a common part of concept-service and user-service
public abstract class AbstractHandler {

    protected <T> Mono<ServerResponse> getResponse(Mono<T> successResponse, Class<T> successResponseClass,
                                                   ServerResponse.BodyBuilder errorStatus, String errorMessage) {
        return successResponse.flatMap(resp -> getSuccessResponse(just(resp), successResponseClass))
                              .switchIfEmpty(getErrorResponse(errorStatus, errorMessage))
                              .timeout(TIMEOUT, getErrorResponse(status(INTERNAL_SERVER_ERROR), TIMEOUT_MESSAGE));
    }

    protected <T> Mono<ServerResponse> getResponse(Mono<T> response, Class<T> responseClass) {
        return getResponse(response, responseClass,
                           status(INTERNAL_SERVER_ERROR), INTERNAL_ERROR_MESSAGE);
    }

    protected Mono<ServerResponse> getErrorResponse(ServerResponse.BodyBuilder status, String errorMessage) {
        return status.body(fromObject(new ErrorResponse(errorMessage)));
    }

    protected <T> Mono<ServerResponse> getSuccessResponse(Mono<T> response, Class<T> responseClass) {
        return ok().body(response, responseClass);
    }
}
