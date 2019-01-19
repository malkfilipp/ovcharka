package ovcharka.common.web;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ErrorResponse extends AbstractResponse<Object> {

    public ErrorResponse(String message) {
        super(AbstractResponse.ERROR, null, message);
    }
}
