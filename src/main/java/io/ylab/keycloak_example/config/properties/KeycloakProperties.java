package io.ylab.keycloak_example.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "keycloak")
@Getter
@Setter
public class KeycloakProperties {
    private String realm;
    private String clientId;
    private String clientSecret;
    private String urlAuth;

}
