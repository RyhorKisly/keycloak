package io.ylab.keycloak_example.endpoints.controller;

import io.ylab.keycloak_example.core.dto.UserCreateDTO;
import io.ylab.keycloak_example.services.api.IKeycloakService;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
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
    private final IKeycloakService keycloakService;

    /**
     * Retrieves the current user. userId gets from the token
     * @return ResponseEntity containing the current user representation
     */
    @GetMapping("/me")
    public ResponseEntity<UserRepresentation> getUser() {
        String userId = getUserIdFromToken();
        return new ResponseEntity<>( keycloakService.getUserById(userId), HttpStatus.OK);
    }

    /**
     * Retrieves the current user. userId gets from url
     * @return ResponseEntity containing the current user representation
     */
    @GetMapping("/{uuid}")
    public ResponseEntity<UserRepresentation> getUser(
            @PathVariable UUID uuid
    ) {
        //todo добавить право доступа - админ
        return new ResponseEntity<>( keycloakService.getUserById(uuid.toString()), HttpStatus.OK);
    }

    /**
     * Retrieves all users.
     * @return ResponseEntity containing a list of user representations
     */
    @GetMapping
    public ResponseEntity<List<UserRepresentation>> getUsers() {
        return new ResponseEntity<>(keycloakService.getUsers(), HttpStatus.OK);
    }

    /**
     * Updates a user by userId by Admin
     * @param uuid The UUID of the user to update
     * @param userCreateDTO The updated user data
     * @return ResponseEntity containing the updated user representation
     */
    @PutMapping("/{uuid}")
    public ResponseEntity<UserRepresentation> updateUser(
            @PathVariable UUID uuid,
            @RequestBody UserCreateDTO userCreateDTO
    ) {
        //todo прописать прово доступа - админ
        UserRepresentation user = keycloakService.updateUser(uuid.toString(), userCreateDTO);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Updates the current user. userId gets from the token
     * @param userCreateDTO The updated user data
     * @return ResponseEntity containing the updated user representation
     */
    @PutMapping("/me")
    public ResponseEntity<UserRepresentation> updateUser(
            @RequestBody UserCreateDTO userCreateDTO
    ) {
        //todo прописать прова доступа, протестить
        String userId = getUserIdFromToken();

        UserRepresentation user = keycloakService.updateUser(userId, userCreateDTO);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Retrieves the user ID from the token.
     * @return The user ID extracted from the token
     */
    private String getUserIdFromToken() {
        //todo прописать прова доступа всем авторизированным
        Jwt jwt = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            jwt = ((JwtAuthenticationToken) authentication).getToken();
        }

        //todo обработать ошибку
        String userId = jwt.getClaim("sub");

        return userId;
    }

}
