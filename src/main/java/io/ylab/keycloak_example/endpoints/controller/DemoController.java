package io.ylab.keycloak_example.endpoints.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class to demonstrate endpoints for Keycloak integration.
 * Provides basic demo endpoints.
 */
@RestController
@RequestMapping("/api/v1/demo")
public class DemoController {

    /**
     * Method handling GET requests at the base endpoint for all roles.
     *
     * @return a simple greeting message
     */
    @GetMapping
    public String hello() {
        return "Hello from keycloak_example demo api";
    }

    /**
     * Method handling GET requests at '/hello-2' endpoint.
     * Restricted for ADMIN role.
     *
     * @return a greeting message for ADMIN users
     */
    @GetMapping("/hello-2")
//    @Secured("ROLE_ADMIN")
//    @RolesAllowed("ADMIN")
//    @PreAuthorize("hasRole('ADMIN')")
    public String hello2() {
        return "Hello from keycloak_example demo api for ADMIN";
    }
}
