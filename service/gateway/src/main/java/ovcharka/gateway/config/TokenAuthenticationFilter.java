package ovcharka.gateway.config;

import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import ovcharka.common.config.AuthConfig;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final AuthConfig authConfig;

    TokenAuthenticationFilter(AuthConfig authConfig) {
        this.authConfig = authConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        var header = request.getHeader(authConfig.getHeader());

        if (header == null || !header.startsWith(authConfig.getPrefix())) {
            chain.doFilter(request, response);
            return;
        }

        var token = header.replace(authConfig.getPrefix(), "");
        try {
            var claims = Jwts.parser()
                             .setSigningKey(authConfig.getSecret().getBytes())
                             .parseClaimsJws(token)
                             .getBody();

            var username = claims.getSubject();

            if (username != null) {
                @SuppressWarnings("unchecked")
                var authorities = (List<String>) claims.get("authorities");
                var auth = new UsernamePasswordAuthenticationToken(
                        username, null, authorities.stream()
                                                   .map(SimpleGrantedAuthority::new)
                                                   .collect(Collectors.toList())
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        } catch (Exception e) {
            SecurityContextHolder.clearContext();
        }
        chain.doFilter(request, response);
    }

}