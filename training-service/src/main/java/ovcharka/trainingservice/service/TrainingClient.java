package ovcharka.trainingservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ovcharka.common.web.AbstractResponse;
import ovcharka.common.web.BooleanResponse;
import ovcharka.common.web.ErrorResponse;
import ovcharka.conceptservice.domain.Concept;
import ovcharka.conceptservice.payload.response.ConceptResponse;
import ovcharka.conceptservice.payload.response.WordListResponse;
import ovcharka.userservice.payload.request.UserStatsUpdateRequest;
import ovcharka.userservice.payload.response.UserResponse;

import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

import static ovcharka.common.web.AbstractResponse.SUCCESS;

@Service
class TrainingClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    TrainingClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    private <U, T> T postData(String url, U request, Class<? extends AbstractResponse<T>> responseClass) {
        return getDataFromResponse(() -> restTemplate.postForObject(url, request, responseClass));
    }

    private <T> T getData(String url, Class<? extends AbstractResponse<T>> responseClass) {
        return getDataFromResponse(() -> restTemplate.getForObject(url, responseClass));
    }

    private <T> T getDataFromResponse(Supplier<AbstractResponse<T>> responseSupplier) {
        try {
            var response = responseSupplier.get();
            if (response == null || !response.getStatus().equals(SUCCESS))
                throw new IllegalStateException("Can't read the response from the server:" + response);

            return response.getData();

        } catch (HttpStatusCodeException e) {
            var responseBody = e.getResponseBodyAsString();
            throw getExceptionFrom(responseBody);
        }
    }

    private RuntimeException getExceptionFrom(String response) {
        try {
            var errorResponse = objectMapper.readValue(response, ErrorResponse.class);
            return new IllegalStateException(errorResponse.getMessage());
        } catch (IOException e) {
            return new IllegalStateException("Can't read the error response from the server: " + e);
        }
    }

    // TODO: 19/01/2019 change urls with load balancer
    String getUserIdByUsername(String username) {
        var url = "http://localhost:8082/users?username=" + username;
        return getData(url, UserResponse.class).getId();
    }

    void updateUserStats(String username, String grade) {
        var url = "http://localhost:8082/users/stats";
        postData(url, new UserStatsUpdateRequest(username, grade), BooleanResponse.class);
    }

    Concept getConcept(String word) {
        var url = "http://localhost:8081/concepts?word=" + word;
        return getData(url, ConceptResponse.class);
    }

    List<String> getAllWords() {
        var url = "http://localhost:8081/concepts/words";
        return getData(url, WordListResponse.class);
    }
}
