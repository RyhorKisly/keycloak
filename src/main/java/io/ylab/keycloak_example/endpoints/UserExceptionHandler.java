package io.ylab.keycloak_example.endpoints;

import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for user-related endpoints.
 */
@RestControllerAdvice
@Slf4j
public class UserExceptionHandler {

    /**
     * Handles general bed request exceptions and returns an Bed Request response.
     * @param ex The exception to handle
     * @return ResponseEntity with a Bed Request status
     */
    @ExceptionHandler({
            NotFoundException.class,
    })
    public ResponseEntity<?> handleDedRequest(NotFoundException ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

        /**
         * Handles general exceptions and returns an Internal Server Error response.
         * @param ex The exception to handle
         * @return ResponseEntity with an Internal Server Error status
         */
    @ExceptionHandler({
            Exception.class
    })
    public ResponseEntity<?> handleInnerError(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}