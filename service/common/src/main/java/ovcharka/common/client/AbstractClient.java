package ovcharka.common.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ovcharka.common.payload.response.ErrorResponse;
import ovcharka.common.payload.response.AbstractResponse;

import java.io.IOException;
import java.util.function.Supplier;

import static ovcharka.common.payload.response.AbstractResponse.SUCCESS;

@Service
public abstract class AbstractClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    protected AbstractClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    protected  <U, T> T postData(String url, U request, Class<? extends AbstractResponse<T>> responseClass) {
        return getDataFromResponse(() -> restTemplate.postForObject(url, request, responseClass));
    }

    protected  <T> T getData(String url, Class<? extends AbstractResponse<T>> responseClass) {
        return getDataFromResponse(() -> restTemplate.getForObject(url, responseClass));
    }

    private <T> T getDataFromResponse(Supplier<AbstractResponse<T>> responseSupplier) {
        try {
            var response = responseSupplier.get();
            if (response == null || !response.getStatus().equals(SUCCESS))
                throw new IllegalStateException("Can't read the response from the server:" + response);

            return response.getData();

        } catch (HttpStatusCodeException e) {
            var responseBody = e.getResponseBodyAsString();
            throw getExceptionFrom(responseBody);
        }
    }

    private RuntimeException getExceptionFrom(String response) {
        try {
            var errorResponse = objectMapper.readValue(response, ErrorResponse.class);
            return new IllegalStateException(errorResponse.getMessage());
        } catch (IOException e) {
            return new IllegalStateException("Can't read the error response from the server: " + e);
        }
    }
}
