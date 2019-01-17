package ovcharka.userservice.payload.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ovcharka.userservice.domain.User;

@EqualsAndHashCode(callSuper = true)
@Data
public class WordResponse  extends AbstractResponse<String> {

    public WordResponse(String word) {
        super(AbstractResponse.SUCCESS, word, null);
    }

}
