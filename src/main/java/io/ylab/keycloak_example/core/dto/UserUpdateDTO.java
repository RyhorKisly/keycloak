package io.ylab.keycloak_example.core.dto;

import io.ylab.keycloak_example.core.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing user update details.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDTO {
    /**
     * userName in Keycloak
     */
    private String login;

    /**
     * The email address of the user.
     */
    private String email;

    /**
     * The password for the user.
     */
    private String password;

    /**
     * The role of the user (e.g., ADMIN, USER).
     */
    private UserRole role;

    /**
     * The full name (FIO) of the user.
     */
    private String fio;
}
