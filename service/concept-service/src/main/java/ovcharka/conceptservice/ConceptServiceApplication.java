package ovcharka.conceptservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ConceptServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConceptServiceApplication.class, args);
    }
}