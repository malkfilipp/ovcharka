package ovcharka.tokenservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ovcharka.common.client.AbstractClient;
import ovcharka.userservice.payload.response.UserResponse;

@Service
class TokenClient extends AbstractClient {

    @Autowired
    TokenClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
        super(restTemplate, objectMapper);
    }

    void checkPresenceOf(String username) {
        var url = "http://user-service?username=" + username;
        getData(url, UserResponse.class);
    }
}
