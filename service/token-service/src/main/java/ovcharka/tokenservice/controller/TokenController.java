package ovcharka.tokenservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ovcharka.common.controller.AbstractController;
import ovcharka.common.payload.response.AbstractResponse;
import ovcharka.common.payload.response.MessageResponse;
import ovcharka.tokenservice.payload.request.ConfirmTokenRequest;
import ovcharka.tokenservice.payload.request.GenerateTokenRequest;
import ovcharka.tokenservice.service.TokenService;

@RestController
public class TokenController extends AbstractController {

    private final TokenService tokenService;

    @Autowired
    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }


    @PostMapping("/generate")
    ResponseEntity<AbstractResponse> generate(@RequestBody GenerateTokenRequest request) {
        return getResponse(
                () -> new MessageResponse(
                        tokenService.create(request.getUsername())
                )
        );
    }

    @PostMapping("/confirm")
    ResponseEntity<AbstractResponse> confirm(@RequestBody ConfirmTokenRequest request) {
        return getResponse(
                () -> new MessageResponse(
                        tokenService.getUsername(request.getToken())
                )
        );
    }
}
