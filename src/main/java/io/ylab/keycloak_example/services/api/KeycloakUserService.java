package io.ylab.keycloak_example.services.api;

import io.ylab.keycloak_example.core.dto.KeycloakServiceResponseDTO;
import io.ylab.keycloak_example.core.dto.UserCreateDTO;
import io.ylab.keycloak_example.core.dto.UserDTO;
import io.ylab.keycloak_example.core.dto.UserUpdateDTO;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for Keycloak operations related to users.
 */
public interface KeycloakUserService {

    /**
     * Creates a user by Admin or some special role.
     * @param dto The UserCreateDTO containing user details
     * @return KeycloakServiceResponseDTO containing the response and user DTO
     */
    KeycloakServiceResponseDTO<UserCreateDTO> create(UserCreateDTO dto);

    /**
     * Retrieves a user by user ID.
     * @param userId The ID of the user to retrieve
     * @return The UserRepresentation of the retrieved user
     */
    UserDTO getUserById(UUID userId);

    /**
     * Retrieves all users.
     * @return A list of UserRepresentation objects representing all users
     */
    List<UserDTO> getUsers();

    /**
     * Updates a user by user ID and new user details.
     * @param userId The ID of the user to update
     * @param dto The UserUpdateDTO containing updated user details
     * @return The UserRepresentation of the updated user
     */
    UserDTO updateUser(UUID userId, UserUpdateDTO dto);
}
