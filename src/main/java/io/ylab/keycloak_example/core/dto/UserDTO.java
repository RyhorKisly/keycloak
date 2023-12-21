package io.ylab.keycloak_example.core.dto;

import io.ylab.keycloak_example.core.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    /**
     * userName in Keycloak
     */
    private String login;
    private String email;
    private UserRole role;
    private String fio;
}
