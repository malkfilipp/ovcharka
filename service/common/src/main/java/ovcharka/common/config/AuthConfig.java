package ovcharka.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("security.jwt")
@Getter
@Setter
public class AuthConfig {
    private String uri;
    private String header;
    private String prefix;
    private int expiration;
    private String secret;
}
