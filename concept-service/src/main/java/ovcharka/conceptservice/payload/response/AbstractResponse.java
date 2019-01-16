package ovcharka.conceptservice.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractResponse<T> {
    public static final String SUCCESS = "success";
    public static final String ERROR = "error";
    public static final String INTERNAL_ERROR_MESSAGE = "Internal Server Error";
    public static final String WRONG_PARAMETER = "Wrong Parameter";
    public static final String TIMEOUT_MESSAGE = "Database timeout after 1 second";

    public static final Duration TIMEOUT = Duration.ofSeconds(1);

    private String status;
    private T data;
    private String message;
}
