package io.ylab.keycloak_example.core;

public class RoleExistException extends RuntimeException{
    public RoleExistException(String message) {
        super(message);
    }

}
