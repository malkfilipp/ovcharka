package ovcharka.trainingservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ovcharka.common.client.AbstractClient;
import ovcharka.common.payload.response.BooleanResponse;
import ovcharka.conceptservice.domain.Concept;
import ovcharka.conceptservice.payload.response.ConceptResponse;
import ovcharka.conceptservice.payload.response.WordListResponse;
import ovcharka.userservice.payload.request.UserStatsUpdateRequest;
import ovcharka.userservice.payload.response.UserResponse;

import java.util.List;

@Service
class TrainingClient extends AbstractClient {

    @Autowired
    TrainingClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
        super(restTemplate, objectMapper);
    }

    String getUserIdByUsername(String username) {
        var url = "http://user-service?username=" + username;
        return getData(url, UserResponse.class).getId();
    }

    void updateUserStats(String username, String grade) {
        var url = "http://user-service/stats";
        postData(url, new UserStatsUpdateRequest(username, grade), BooleanResponse.class);
    }

    Concept getConcept(String word) {
        var url = "http://concept-service?word=" + word;
        return getData(url, ConceptResponse.class);
    }

    List<String> getAllWords() {
        var url = "http://concept-service/words";
        return getData(url, WordListResponse.class);
    }
}
