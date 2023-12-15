package io.ylab.keycloak_example.services.api;

import io.ylab.keycloak_example.core.dto.KeycloakServiceResponseDTO;
import io.ylab.keycloak_example.core.dto.UserCreateDTO;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

/**
 * Service interface for Keycloak operations related to users.
 */
public interface IKeycloakService {

    /**
     * Creates a user by Admin or some special role.
     * @param dto The UserCreateDTO containing user details
     * @return KeycloakServiceResponseDTO containing the response and user DTO
     */
    KeycloakServiceResponseDTO<UserCreateDTO> createByUser(UserCreateDTO dto);

    /**
     * Retrieves a user by user ID.
     * @param userId The ID of the user to retrieve
     * @return The UserRepresentation of the retrieved user
     */
    UserRepresentation getUserById(String userId);

    /**
     * Retrieves all users.
     * @return A list of UserRepresentation objects representing all users
     */
    List<UserRepresentation> getUsers();

    /**
     * Updates a user by user ID and new user details.
     * @param userId The ID of the user to update
     * @param dto The UserCreateDTO containing updated user details
     * @return The UserRepresentation of the updated user
     */
    UserRepresentation updateUser(String userId, UserCreateDTO dto);
}
