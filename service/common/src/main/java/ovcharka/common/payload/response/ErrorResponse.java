package ovcharka.common.payload.response;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ErrorResponse extends AbstractResponse<Object> {

    public ErrorResponse(String message) {
        super(ERROR, null, message);
    }
}
