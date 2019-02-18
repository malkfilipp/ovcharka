package ovcharka.apigateway.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ovcharka.common.client.AbstractClient;
import ovcharka.common.payload.response.BooleanResponse;
import ovcharka.conceptservice.domain.Concept;
import ovcharka.conceptservice.payload.response.ConceptResponse;
import ovcharka.conceptservice.payload.response.WordListResponse;
import ovcharka.userservice.domain.Stats;
import ovcharka.userservice.payload.request.UserStatsUpdateRequest;
import ovcharka.userservice.payload.response.UserResponse;

import java.util.List;

@Service
public class ApiClient extends AbstractClient {

    @Autowired
    public ApiClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
        super(restTemplate, objectMapper);
    }

    public Stats getUserStats(String username) {
        var url = "http://user-service?username=" + username;
        return getData(url, UserResponse.class).getStats();
    }
}
