package admin;

import admin.config.AdminProperties;
import admin.service.ConceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

import java.io.IOException;

@SpringBootApplication
@EnableConfigurationProperties({AdminProperties.class})
@EnableNeo4jRepositories("admin.repository")
public class AdminApplication implements CommandLineRunner {

    private final ConceptService conceptService;

    @Autowired
    public AdminApplication(ConceptService conceptService) {
        this.conceptService = conceptService;
    }

    public static void main(String[] args) {
        var app = new SpringApplication(AdminApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    @Override
    public void run(String... args) throws IOException {
        conceptService.generate();
    }
}
