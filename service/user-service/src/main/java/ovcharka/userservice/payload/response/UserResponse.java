package ovcharka.userservice.payload.response;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ovcharka.common.payload.AbstractResponse;
import ovcharka.userservice.domain.User;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserResponse extends AbstractResponse<User> {

    public UserResponse(User user) {
        super(AbstractResponse.SUCCESS, user, null);
    }
}
