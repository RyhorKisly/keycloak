package io.ylab.keycloak_example.endpoints.controller;

import io.ylab.keycloak_example.core.dto.KeycloakServiceResponseDTO;
import io.ylab.keycloak_example.core.dto.UserCreateDTO;
import io.ylab.keycloak_example.core.dto.UserDTO;
import io.ylab.keycloak_example.core.dto.UserUpdateDTO;
import io.ylab.keycloak_example.services.api.KeycloakUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller to manage user-related endpoints.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    /**
     * Service interface for Keycloak operations related to users.
     */
    private final KeycloakUserService keycloakUserService;

    /**
     * Registers a new user.
     *
     * @param dto The UserCreateDTO containing user details
     * @return ResponseEntity containing the registered user's details
     */
    @PostMapping
    public ResponseEntity<UserCreateDTO> create(
            @RequestBody UserCreateDTO dto
    ) {
        KeycloakServiceResponseDTO<UserCreateDTO> responseDto = keycloakUserService.create(dto);
        return new ResponseEntity<>(
                responseDto.getDto(),
                HttpStatusCode.valueOf(responseDto.getResponse().getStatus())
        );
    }

    /**
     * Retrieves the current user. userId gets from the token
     * @return ResponseEntity containing the current user representation
     */
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getUser() {
        String userId = getUserIdFromToken();
        UUID uuid = UUID.fromString(userId);
        return new ResponseEntity<>( keycloakUserService.getUserById(uuid), HttpStatus.OK);
    }

    /**
     * Retrieves the current user. userId gets from url
     * @return ResponseEntity containing the current user representation
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(
            @PathVariable UUID id
    ) {
        return new ResponseEntity<>( keycloakUserService.getUserById(id), HttpStatus.OK);
    }

    /**
     * Retrieves all users.
     * @return ResponseEntity containing a list of user representations
     */
    @GetMapping
    public ResponseEntity<List<UserDTO>> getUsers() {
        return new ResponseEntity<>(keycloakUserService.getUsers(), HttpStatus.OK);
    }

    /**
     * Updates a user by userId by Admin
     * @param id The UUID of the user to update
     * @param userUpdateDTO The updated user data
     * @return ResponseEntity containing the updated user representation
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable UUID id,
            @RequestBody UserUpdateDTO userUpdateDTO
    ) {
        UserDTO user = keycloakUserService.updateUser(id, userUpdateDTO);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Updates the current user. userId gets from the token
     * @param userUpdateDTO The updated user data
     * @return ResponseEntity containing the updated user representation
     */
    @PutMapping("/me")
    public ResponseEntity<UserDTO> updateUser(
            @RequestBody UserUpdateDTO userUpdateDTO
    ) {
        String userId = getUserIdFromToken();
        UUID uuid = UUID.fromString(userId);

        UserDTO user = keycloakUserService.updateUser(uuid, userUpdateDTO);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Retrieves the user ID from the token.
     * @return The user ID extracted from the token
     */
    private String getUserIdFromToken() {
        Jwt jwt = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            jwt = ((JwtAuthenticationToken) authentication).getToken();
        }

        return jwt.getClaim("sub");
    }

}
