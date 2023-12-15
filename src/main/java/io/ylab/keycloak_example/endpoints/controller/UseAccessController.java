package io.ylab.keycloak_example.endpoints.controller;

import io.ylab.keycloak_example.core.dto.KeycloakServiceResponseDTO;
import io.ylab.keycloak_example.core.dto.UserAuthorizeDTO;
import io.ylab.keycloak_example.core.dto.UserCreateDTO;
import io.ylab.keycloak_example.services.api.IKeycloakAccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing user access operations.
 */
@RestController
@RequiredArgsConstructor
public class UseAccessController {

    /**
     * Service interface for handling user access related operations.
     */
    private final IKeycloakAccessService accessService;

    /**
     * Registers a new user.
     *
     * @param dto The UserCreateDTO containing user details
     * @return ResponseEntity containing the registered user's details
     */
    @PostMapping("/registration")
    public ResponseEntity<UserCreateDTO> register(
            @RequestBody UserCreateDTO dto
    ) {
        KeycloakServiceResponseDTO<UserCreateDTO> responseDto = accessService.register(dto);
        return new ResponseEntity<>(
                responseDto.getDto(),
                HttpStatusCode.valueOf(responseDto.getResponse().getStatus())
        );
    }

    /**
     * Authorizes a user.
     *
     * @param dto The UserAuthorizeDTO containing user authorization details
     * @return ResponseEntity containing the authorization token
     */
    @PostMapping("/auth")
    public ResponseEntity<String> authorize(
            @RequestBody UserAuthorizeDTO dto
    ) {
        String token = accessService.authorize(dto);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

}
