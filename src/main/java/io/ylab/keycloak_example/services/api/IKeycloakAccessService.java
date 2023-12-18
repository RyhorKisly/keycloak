package io.ylab.keycloak_example.services.api;

import io.ylab.keycloak_example.core.dto.KeycloakServiceResponseDTO;
import io.ylab.keycloak_example.core.dto.UserAuthorizeDTO;
import io.ylab.keycloak_example.core.dto.UserCreateDTO;
import org.keycloak.representations.AccessTokenResponse;

/**
 * Service interface for Keycloak access operations.
 */
public interface IKeycloakAccessService {

    /**
     * Registers a new user.
     * @param dto The UserCreateDTO containing user details
     * @return KeycloakServiceResponseDTO containing the response and user DTO
     */
    KeycloakServiceResponseDTO<UserCreateDTO> register(UserCreateDTO dto);

    /**
     * Authorizes a user.
     * @param dto The UserAuthorizeDTO containing user authorization details
     * @return The authorization token
     */
    AccessTokenResponse authorize(UserAuthorizeDTO dto);
    AccessTokenResponse refresh(String token);

}
