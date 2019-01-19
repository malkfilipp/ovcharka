package ovcharka.common.web;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MessageResponse extends AbstractResponse<String> {

    public MessageResponse(String message) {
        super(AbstractResponse.SUCCESS, message, null);
    }
}
