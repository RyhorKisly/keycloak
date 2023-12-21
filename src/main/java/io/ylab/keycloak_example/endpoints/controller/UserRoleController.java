package io.ylab.keycloak_example.endpoints.controller;

import io.ylab.keycloak_example.core.RoleExistException;
import io.ylab.keycloak_example.core.enums.UserRole;
import io.ylab.keycloak_example.services.api.KeycloakUserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controller to manage user roles within Keycloak.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping
public class UserRoleController {
    private final KeycloakUserRoleService userRoleService;
    private static final String ROLE_EXIST = "User already has a role";

    /**
     * Retrieves the roles associated with a user.
     *
     * @param id User ID
     * @return ResponseEntity with the user's roles
     */
    @GetMapping("/users/{id}/roles")
    public ResponseEntity<?> updateRoleToUser(
            @PathVariable UUID id
    ) {
        return new ResponseEntity<>(userRoleService.getRole(id), HttpStatus.OK);
    }

    /**
     * Adds a role to a user.
     *
     * @param id   User ID
     * @param role UserRole enum representing the role to be added
     * @return ResponseEntity indicating success or failure
     */
    @PostMapping("/users/{id}/roles/{role}")
    public ResponseEntity<?> addRoleToUser(
            @PathVariable UUID id,
            @PathVariable UserRole role
    ) {
        userRoleService.addRoleToUser(id, role)
                .orElseThrow(() -> new RoleExistException(ROLE_EXIST));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Updates a user's role.
     *
     * @param id   User ID
     * @param role UserRole enum representing the updated role
     * @return ResponseEntity indicating success or failure
     */
    @PatchMapping("/users/{id}/roles/{role}")
    public ResponseEntity<?> updateRoleToUser(
            @PathVariable UUID id,
            @PathVariable UserRole role
    ) {
        userRoleService.updateRoleToUser(id, role);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
