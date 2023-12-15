package io.ylab.keycloak_example.core.dto;

import io.ylab.keycloak_example.core.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDTO {

    /**
     * userName in Keycloak
     */
    private String login;
    private String email;
    private String password;
    private String fio;
    private UserRole role;
}
