package ovcharka.trainingservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ovcharka.common.controller.AbstractController;
import ovcharka.common.payload.response.AbstractResponse;
import ovcharka.common.payload.response.MessageResponse;
import ovcharka.trainingservice.payload.request.UserMessageRequest;
import ovcharka.trainingservice.service.TrainingService;

@RestController
public class TrainingController extends AbstractController {

    private final TrainingService trainingService;

    @Autowired
    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @PostMapping
    public ResponseEntity<AbstractResponse> sendMessage(@RequestBody UserMessageRequest messageRequest) {
        return getResponse(
                () -> new MessageResponse(
                        trainingService.processMessage(messageRequest.getUsername(),
                                                       messageRequest.getMessage())
                )
        );
    }
}
