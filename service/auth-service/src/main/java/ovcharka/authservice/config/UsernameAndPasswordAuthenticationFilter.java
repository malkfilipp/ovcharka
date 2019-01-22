package ovcharka.authservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ovcharka.authservice.payload.LoginRequest;
import ovcharka.common.config.AuthConfig;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class UsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authManager;

    private final AuthConfig authConfig;

    UsernameAndPasswordAuthenticationFilter(AuthenticationManager authManager, AuthConfig authConfig) {
        this.authManager = authManager;
        this.authConfig = authConfig;

        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(authConfig.getLoginUri(), "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        try {
            var credentials = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);
            var authToken = new UsernamePasswordAuthenticationToken(
                    credentials.getUsername(), credentials.getPassword(), emptyList()
            );
            return authManager.authenticate(authToken);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication auth) {

        var now = System.currentTimeMillis();
        var token = Jwts
                .builder()
                .setSubject(auth.getName())
                .claim("authorities", auth.getAuthorities()
                                          .stream()
                                          .map(GrantedAuthority::getAuthority)
                                          .collect(toList()))
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + authConfig.getExpiration() * 1000))  // in milliseconds
                .signWith(SignatureAlgorithm.HS512, authConfig.getSecret().getBytes())
                .compact();

        response.addHeader(authConfig.getHeader(), authConfig.getPrefix() + token);
    }
}