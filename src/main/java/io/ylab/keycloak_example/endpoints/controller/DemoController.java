package io.ylab.keycloak_example.endpoints.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo")
public class DemoController {

    @GetMapping
    public String hello() {
        return "Hello from keycloak_example demo api";
    }

    @GetMapping("/hello-2")
//    @Secured("ROLE_ADMIN")
//    @RolesAllowed("ADMIN")
//    @PreAuthorize("hasRole('ADMIN')")
    public String hello2() {
        return "Hello from keycloak_example demo api for ADMIN";
    }
}
