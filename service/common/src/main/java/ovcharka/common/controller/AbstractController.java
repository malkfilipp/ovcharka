package ovcharka.common.controller;

import org.springframework.http.ResponseEntity;
import ovcharka.common.payload.response.AbstractResponse;
import ovcharka.common.payload.response.ErrorResponse;

import java.util.function.Supplier;

import static org.springframework.http.HttpStatus.*;

public abstract class AbstractController {

    protected ResponseEntity<AbstractResponse> getResponse(Supplier<AbstractResponse> successResponse) {
        try {
            return new ResponseEntity<>(successResponse.get(), OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), INTERNAL_SERVER_ERROR);
        }
    }
}
