package io.ylab.keycloak_example.services;

import io.ylab.keycloak_example.config.properties.KeycloakProperties;
import io.ylab.keycloak_example.core.dto.UserAuthorizeDTO;
import io.ylab.keycloak_example.services.api.KeycloakUserAccessService;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KeycloakUserAccessServiceImpl implements KeycloakUserAccessService {

    private final KeycloakProperties properties;
    private static final String GRANT_TYPE = "grant_type";
    private static final String REFRESH_TOKEN = "refresh_token";
    private static final String CLIENT_ID = "client_id";
    private static final String CLIENT_SECRET = "client_secret";

    @Override
    public AccessTokenResponse authorize(UserAuthorizeDTO dto) {
        Keycloak userKeycloak = setUserKeycloak(dto);
        return userKeycloak.tokenManager().getAccessToken();
    }

    @Override
    public AccessTokenResponse refresh(String token) {

        RestTemplate restTemplate = new RestTemplate();

        // Устанавливаем параметры запроса
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Параметры запроса на получение токена
        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add(GRANT_TYPE, REFRESH_TOKEN);
        requestParams.add(CLIENT_ID, properties.getClientId());
        requestParams.add(CLIENT_SECRET, properties.getClientSecret());
        requestParams.add(REFRESH_TOKEN, token);

        // Формируем запрос
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestParams, headers);

        // Отправляем запрос на получение токена
        ResponseEntity<AccessTokenResponse> response = restTemplate.postForEntity(
                properties.getUrlAuth() + "/realms/" + properties.getRealm() + "/protocol/openid-connect/token",
                request,
                AccessTokenResponse.class
        );

        return response.getBody();
    }

    /**
     * Sets up and returns a Keycloak instance based on the provided UserAuthorizeDTO credentials.
     *
     * @param dto UserAuthorizeDTO containing login and password credentials
     * @return Configured Keycloak instance
     */
    private Keycloak setUserKeycloak(UserAuthorizeDTO dto) {
        return KeycloakBuilder.builder()
                .serverUrl(properties.getUrlAuth())
                .realm(properties.getRealm())
                .grantType(OAuth2Constants.PASSWORD)
                .username(dto.getLogin())
                .password(dto.getPassword())
                .clientId(properties.getClientId())
                .clientSecret(properties.getClientSecret())
                .build();
    }
}
