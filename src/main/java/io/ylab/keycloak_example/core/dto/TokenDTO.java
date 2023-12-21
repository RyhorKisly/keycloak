package io.ylab.keycloak_example.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing a token.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenDTO {

    /**
     * The token string.
     */
    String token;
}
