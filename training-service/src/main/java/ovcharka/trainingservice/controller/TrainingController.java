package ovcharka.trainingservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ovcharka.common.web.AbstractResponse;
import ovcharka.common.web.ErrorResponse;
import ovcharka.common.web.MessageResponse;
import ovcharka.trainingservice.payload.request.MessageRequest;
import ovcharka.trainingservice.service.TrainingService;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
public class TrainingController {

    private final TrainingService trainingService;

    @Autowired
    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @PostMapping("/sendMessage")
    public ResponseEntity<AbstractResponse> sendMessage(@RequestBody MessageRequest messageRequest) {
        try {
            var s = trainingService.processMessage(messageRequest.getUsername(),
                                                   messageRequest.getMessage());
            return new ResponseEntity<>(new MessageResponse(s), OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), BAD_REQUEST);
        }


    }
}
