package io.ylab.keycloak_example.core.dto;

import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) representing a response from Keycloak service.
 * @param <T> Type parameter representing the DTO content
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeycloakServiceResponseDTO<T> {

    /**
     * DTO content.
     */
    private T dto;

    /**
     * Response object.
     */
    private Response response;
}
