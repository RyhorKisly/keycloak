package io.ylab.keycloak_example.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing user creation details.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDTO {

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
     * The full name (FIO) of the user.
     */
    private String fio;
}
