package ovcharka.conceptservice.payload.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ErrorResponse extends AbstractResponse<Object> {

    public ErrorResponse(String message) {
        super(AbstractResponse.ERROR, null, message);
    }

}
