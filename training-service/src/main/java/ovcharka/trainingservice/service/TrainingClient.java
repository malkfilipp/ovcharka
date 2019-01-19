package ovcharka.trainingservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ovcharka.common.web.AbstractResponse;
import ovcharka.common.web.ErrorResponse;
import ovcharka.conceptservice.domain.Concept;
import ovcharka.conceptservice.payload.response.ConceptResponse;
import ovcharka.conceptservice.payload.response.WordListResponse;
import ovcharka.userservice.payload.response.UserResponse;

import java.io.IOException;
import java.util.List;

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

    private <T> T getData(String url, Class<? extends AbstractResponse<T>> responseClass) {
        try {
            var userResponse = restTemplate
                    .getForObject(url, responseClass);

            if (userResponse != null && userResponse.getStatus().equals(SUCCESS))
                return userResponse.getData();
            else
                throw new IllegalStateException("Can't read the response from the server:" + userResponse);
        } catch (HttpStatusCodeException e) {
            var response = e.getResponseBodyAsString();
            throw getExceptionFrom(response);
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

    String getUserIdByUsername(String username) {
        var url = "http://localhost:8082/users?username=" + username;
        return getData(url, UserResponse.class).getId();
    }

    void updateUserStats(String username, String grade) {

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
