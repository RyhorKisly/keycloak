package io.ylab.keycloak_example.config;

import io.ylab.keycloak_example.core.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import io.ylab.keycloak_example.config.utils.JwtAuthConverter;

/**
 * Security configuration class responsible for configuring web security.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * The URI for retrieving JSON Web Keys (JWK) Set.
     */
    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    String jwkSetUri;

    /**
     * The converter responsible for extracting authentication details from JWT
     * and converting role to "ROLE_...".
     */
    private final JwtAuthConverter jwtAuthConverter;

    /**
     * Configures security filters and authorization rules.
     *
     * @param http The HTTP security object to configure security settings.
     * @return The configured security filter chain.
     * @throws Exception If an error occurs during security configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/auth").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/refresh").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/demo/hello-2").hasAnyRole(UserRole.ADMIN.name())
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter)))
                .sessionManagement(t -> t.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    /**
     * Configures the JWT Decoder with the JWK Set URI for token verification.
     *
     * @return The JWT Decoder configured with the JWK Set URI.
     */
    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(this.jwkSetUri).build();
    }

}
