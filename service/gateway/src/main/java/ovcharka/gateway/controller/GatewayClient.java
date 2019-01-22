package ovcharka.gateway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ovcharka.common.client.AbstractClient;
import ovcharka.common.payload.response.MessageResponse;
import ovcharka.trainingservice.payload.request.UserMessageRequest;

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
}
