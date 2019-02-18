package ovcharka.apigateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ovcharka.apigateway.payload.responce.StatsResponse;
import ovcharka.apigateway.service.ApiClient;
import ovcharka.common.controller.AbstractController;
import ovcharka.common.payload.response.AbstractResponse;

@RestController
public class ApiController extends AbstractController {

    private final ApiClient apiClient;

    @Autowired
    public ApiController(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @GetMapping("/stats")
    ResponseEntity<AbstractResponse> getStats(@RequestParam String username) {
        return getResponse(
                () -> new StatsResponse(
                        apiClient.getUserStats(username)
                )
        );
    }
}
