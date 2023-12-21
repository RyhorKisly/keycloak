package io.ylab.keycloak_example.services.api;

import io.ylab.keycloak_example.core.enums.UserRole;

import java.util.Optional;
import java.util.UUID;

public interface KeycloakUserRoleService {

    UserRole getRole(UUID userId);
    Optional<UserRole> addRoleToUser(UUID userId, UserRole role);
    void updateRoleToUser(UUID userId, UserRole role);

}
