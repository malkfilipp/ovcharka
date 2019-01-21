package ovcharka.authservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ovcharka.common.client.AbstractClient;
import ovcharka.common.payload.BooleanResponse;
import ovcharka.userservice.payload.request.UserUpdateRequest;
import ovcharka.userservice.payload.response.UserResponse;

@Service
public class AuthService extends AbstractClient implements UserDetailsService {

    @Autowired
    AuthService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        super(restTemplate, objectMapper);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        var url = "http://user-service?username=" + username;
        var user = getData(url, UserResponse.class);

        var grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_" + user.getRole().toString());

        return new User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }

    public void updateUser(UserUpdateRequest request) {
        var url = "http://user-service";
        postData(url, request, BooleanResponse.class);
    }
}

