package com.codecool.spaceship.controller;

import com.codecool.spaceship.model.exception.*;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = DataNotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(Exception exception) {
        return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(404), exception.getMessage())).build();
    }

    @ExceptionHandler(value = {AccessDeniedException.class, SecurityException.class})
    public ResponseEntity<Object> handleSecurityException(Exception exception) {
        String message = exception.getMessage() == null ? "You don't have permission to access this data." : exception.getMessage();
        return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), message)).build();
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException() {
        return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), "Incorrect username or password.")).build();
    }

    @ExceptionHandler(value = {IllegalOperationException.class, InvalidLevelException.class, NoSuchPartException.class,
            StorageException.class, UpgradeNotAvailableException.class, IllegalArgumentException.class})
    public ResponseEntity<Object> handleCustomException(Exception exception) {
        return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), exception.getMessage())).build();
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception exception) {
        return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(500), exception.getMessage())).build();
    }

}
