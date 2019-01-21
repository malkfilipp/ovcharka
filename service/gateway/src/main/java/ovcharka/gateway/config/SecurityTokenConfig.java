package ovcharka.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ovcharka.common.config.AuthConfig;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;


@EnableWebSecurity
public class SecurityTokenConfig extends WebSecurityConfigurerAdapter {

    private final AuthConfig authConfig;

    @Autowired
    public SecurityTokenConfig(AuthConfig authConfig) {
        this.authConfig = authConfig;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .sessionManagement().sessionCreationPolicy(STATELESS)
            .and()
            .exceptionHandling().authenticationEntryPoint((req, resp, e) -> resp.sendError(SC_UNAUTHORIZED))
            .and()
            .addFilterAfter(new TokenAuthenticationFilter(authConfig), UsernamePasswordAuthenticationFilter.class)
            .authorizeRequests()
            .antMatchers(POST, "/auth" + authConfig.getLoginUri()).permitAll()
            .antMatchers(POST, "/auth" + authConfig.getSignupUri()).permitAll()
            .antMatchers("/admin/**/**").hasRole("ADMIN")
            .anyRequest().authenticated();
    }
}