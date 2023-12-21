package io.ylab.keycloak_example.endpoints.controller;

import io.ylab.keycloak_example.core.RoleExistException;
import io.ylab.keycloak_example.core.enums.UserRole;
import io.ylab.keycloak_example.services.api.KeycloakUserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class UserRoleController {
    private final KeycloakUserRoleService userRoleService;
    private static final String ROLE_EXIST = "User already has a role";

    @GetMapping("/users/{id}/roles")
    public ResponseEntity<?> updateRoleToUser(
            @PathVariable UUID id
    ) {
        return new ResponseEntity<>(userRoleService.getRole(id), HttpStatus.OK);
    }
    @PostMapping("/users/{id}/roles/{role}")
    public ResponseEntity<?> addRoleToUser(
            @PathVariable UUID id,
            @PathVariable UserRole role
    ) {
        userRoleService.addRoleToUser(id, role)
                .orElseThrow(() -> new RoleExistException(ROLE_EXIST));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/users/{id}/roles/{role}")
    public ResponseEntity<?> updateRoleToUser(
            @PathVariable UUID id,
            @PathVariable UserRole role
    ) {
        userRoleService.updateRoleToUser(id, role);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
