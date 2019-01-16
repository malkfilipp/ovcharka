package ovcharka.conceptservice.payload.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SuccessResponse extends AbstractResponse<Object> {

    public SuccessResponse() {
        super(AbstractResponse.SUCCESS, null, null);
    }

}
