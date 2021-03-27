package com.mihaineagu.web.exceptionhandling;

import com.mihaineagu.web.exceptions.DuplicateEntityExceptions;
import com.mihaineagu.web.exceptions.RessourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class RestExceptionHandler  {

    @ExceptionHandler(DuplicateEntityExceptions.class)
    protected ResponseEntity<ErrorResponse> handleConflict(DuplicateEntityExceptions exception) {

        ErrorResponse response = new ErrorResponse();
        response.setErrorMessage("This entity already exists!");
        response.setErrorCode("CONFLICT");
        response.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.valueOf(400));
    }

    @ExceptionHandler(RessourceNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleNotFoundRessource(RessourceNotFoundException exception) {

        ErrorResponse response = new ErrorResponse();
        response.setErrorMessage("Requested entity not found!");
        response.setErrorCode("NOT_FOUND");
        response.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}
