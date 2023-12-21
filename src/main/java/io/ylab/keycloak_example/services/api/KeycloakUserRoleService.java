package io.ylab.keycloak_example.services.api;

import io.ylab.keycloak_example.core.enums.UserRole;

import java.util.Optional;
import java.util.UUID;

/**
 * Service responsible for managing user roles within Keycloak.
 */
public interface KeycloakUserRoleService {

    /**
     * Retrieves the role associated with a specific user.
     *
     * @param userId User ID for whom the role needs to be retrieved
     * @return UserRole representing the user's role
     */
    UserRole getRole(UUID userId);

    /**
     * Adds a role to a user.
     *
     * @param userId User ID to whom the role will be added
     * @param role   UserRole enum representing the role to be added
     * @return Optional UserRole if added successfully, empty otherwise
     */
    Optional<UserRole> addRoleToUser(UUID userId, UserRole role);

    /**
     * Updates a user's role.
     *
     * @param userId User ID for whom the role will be updated
     * @param role   UserRole enum representing the updated role
     */
    void updateRoleToUser(UUID userId, UserRole role);

}
