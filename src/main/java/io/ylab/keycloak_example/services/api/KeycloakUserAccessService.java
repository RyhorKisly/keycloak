package io.ylab.keycloak_example.services.api;

import io.ylab.keycloak_example.core.dto.UserAuthorizeDTO;
import org.keycloak.representations.AccessTokenResponse;

/**
 * Service interface for Keycloak access operations.
 */
public interface KeycloakUserAccessService {

    /**
     * Authorizes a user.
     * @param dto The UserAuthorizeDTO containing user authorization details
     * @return The authorization token
     */
    AccessTokenResponse authorize(UserAuthorizeDTO dto);

    /**
     * Refreshes an access token.
     * @param token The refresh token
     * @return The new access token
     */
    AccessTokenResponse refresh(String token);

}
