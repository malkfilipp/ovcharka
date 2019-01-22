package ovcharka.gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ovcharka.common.controller.AbstractController;
import ovcharka.common.payload.request.MessageRequest;
import ovcharka.common.payload.response.AbstractResponse;
import ovcharka.common.payload.response.MessageResponse;

import java.security.Principal;

@RestController
public class GatewayController extends AbstractController {

    private final GatewayClient gatewayClient;

    @Autowired
    public GatewayController(GatewayClient gatewayClient) {
        this.gatewayClient = gatewayClient;
    }

    @PostMapping("/sendMessage")
    public ResponseEntity<AbstractResponse> sendMessage(Principal principal,
                                                        @RequestBody MessageRequest request) {
        return getResponse(
                () -> new MessageResponse(
                        gatewayClient.sendMessage(principal.getName(), request.getMessage())
                )
        );
    }
}
