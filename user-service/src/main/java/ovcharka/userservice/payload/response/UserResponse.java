package ovcharka.userservice.payload.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ovcharka.userservice.domain.User;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserResponse extends AbstractResponse<User> {

    public UserResponse(User concept) {
        super(AbstractResponse.SUCCESS, concept, null);
    }
}
