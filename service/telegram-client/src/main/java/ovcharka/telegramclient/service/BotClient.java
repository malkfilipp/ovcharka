package ovcharka.telegramclient.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ovcharka.common.client.AbstractClient;
import ovcharka.common.payload.response.MessageResponse;
import ovcharka.tokenservice.payload.request.ConfirmTokenRequest;
import ovcharka.trainingservice.payload.request.UserMessageRequest;

@Service
class BotClient extends AbstractClient {

    @Autowired
    BotClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
        super(restTemplate, objectMapper);
    }

    String getUsernameFrom(String token) {
        var url = "http://api-gateway/token/confirm";
        return postData(url, new ConfirmTokenRequest(token), MessageResponse.class);
    }

    String processTraining(String username, String message) {
        try {
            var url = "http://api-gateway/training";
            return postData(url, new UserMessageRequest(username, message), MessageResponse.class);
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
