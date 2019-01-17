package ovcharka.userservice.payload.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BooleanResponse extends AbstractResponse<Boolean> {

    public BooleanResponse(boolean value) {
        super(SUCCESS, value, null);
    }
}
