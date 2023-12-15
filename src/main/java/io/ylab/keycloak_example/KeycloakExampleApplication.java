package io.ylab.keycloak_example;

import io.ylab.keycloak_example.config.properties.KeycloakProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({KeycloakProperties.class})
public class KeycloakExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(KeycloakExampleApplication.class, args);
	}


}
