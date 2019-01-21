package ovcharka.common.payload;

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

    private String status;
    private T data;
    private String message;
}
