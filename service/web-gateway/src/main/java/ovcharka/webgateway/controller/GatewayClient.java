package ovcharka.webgateway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ovcharka.apigateway.payload.responce.StatsResponse;
import ovcharka.common.client.AbstractClient;
import ovcharka.common.payload.response.MessageResponse;
import ovcharka.tokenservice.payload.request.GenerateTokenRequest;
import ovcharka.trainingservice.payload.request.UserMessageRequest;
import ovcharka.userservice.domain.Stats;

@Service
public class GatewayClient extends AbstractClient {

    @Autowired
    GatewayClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
        super(restTemplate, objectMapper);
    }

    String sendMessage(String username, String message) {
        var url = "http://training-service/";
        return postData(url, new UserMessageRequest(username, message), MessageResponse.class);
    }

    String generateTokenFor(String username) {
        var url = "http://token-service/generate";
        return postData(url, new GenerateTokenRequest(username), MessageResponse.class);
    }

    Stats getStats(String username) {
        var url = "http://api-gateway/stats?username=" + username;
        return getData(url, StatsResponse.class);
    }
}
