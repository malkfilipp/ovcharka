package user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.telegram.telegrambots.ApiContextInitializer;
import user.config.UserProperties;

@SpringBootApplication
@EnableConfigurationProperties({UserProperties.class})
@EntityScan("admin.domain")
@ComponentScan({"user", "admin.repository"})
@EnableNeo4jRepositories("admin.repository")
public class UserApplication {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(UserApplication.class, args);
    }
}
