package ovcharka.userservice.payload.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SuccessResponse extends AbstractResponse<Object> {

    public SuccessResponse() {
        super(SUCCESS, null, null);
    }
}
