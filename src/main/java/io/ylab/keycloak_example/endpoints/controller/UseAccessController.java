package io.ylab.keycloak_example.endpoints.controller;

import io.ylab.keycloak_example.core.dto.TokenDTO;
import io.ylab.keycloak_example.core.dto.UserAuthorizeDTO;
import io.ylab.keycloak_example.services.api.KeycloakUserAccessService;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing user access operations.
 */
@RestController
@RequiredArgsConstructor
public class UseAccessController {

    /**
     * Service interface for handling user access related operations.
     */
    private final KeycloakUserAccessService accessService;

    /**
     * Authorizes a user.
     *
     * @param dto The UserAuthorizeDTO containing user authorization details
     * @return ResponseEntity containing the authorization token
     */
    @PostMapping("/users/auth")
    public ResponseEntity<AccessTokenResponse> authorize(
            @RequestBody UserAuthorizeDTO dto
    ) {
        AccessTokenResponse response = accessService.authorize(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/users/refresh")
    public ResponseEntity<AccessTokenResponse> refreshToken(
            @RequestBody TokenDTO token
            ) {
        return new ResponseEntity<>(accessService.refresh(token.getToken()), HttpStatus.OK);

    }

}
