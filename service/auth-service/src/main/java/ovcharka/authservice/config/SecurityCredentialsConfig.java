package ovcharka.authservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ovcharka.common.config.AuthConfig;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
public class SecurityCredentialsConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    private final AuthConfig authConfig;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public SecurityCredentialsConfig(UserDetailsService userDetailsService, AuthConfig authConfig, BCryptPasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.authConfig = authConfig;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .sessionManagement().sessionCreationPolicy(STATELESS)
            .and()
            .exceptionHandling()
            .authenticationEntryPoint((req, resp, e) -> resp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
            .and()
            .addFilter(new UsernameAndPasswordAuthenticationFilter(authenticationManager(), authConfig))
            .authorizeRequests()
            .antMatchers(POST, authConfig.getUri()).permitAll()
            .anyRequest().authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
}