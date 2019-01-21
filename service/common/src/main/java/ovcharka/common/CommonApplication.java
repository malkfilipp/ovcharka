package ovcharka.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ovcharka.common.config.AuthConfig;

@SpringBootApplication
@EnableConfigurationProperties(AuthConfig.class)
public class CommonApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommonApplication.class, args);
    }
}

