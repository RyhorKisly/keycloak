package io.ylab.keycloak_example.config;

import io.ylab.keycloak_example.config.properties.KeycloakProperties;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up Keycloak authentication
 */
@Configuration
@RequiredArgsConstructor
public class KeycloakConfig {
    private final KeycloakProperties properties;

    /**
     * Bean method to create and configure a Keycloak instance.
     * @return Configured Keycloak instance
     */
    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(properties.getUrlAuth())
                .realm(properties.getRealm())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(properties.getClientId())
                .clientSecret(properties.getClientSecret())
                .build();
    }

}
