package ovcharka.common.payload;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BooleanResponse extends AbstractResponse<Boolean> {

    public BooleanResponse(boolean value) {
        super(SUCCESS, value, null);
    }
}
