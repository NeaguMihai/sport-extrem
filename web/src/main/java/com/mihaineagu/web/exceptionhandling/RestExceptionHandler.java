package com.mihaineagu.web.exceptionhandling;

import com.mihaineagu.web.exceptions.DuplicateEntityExceptions;
import com.mihaineagu.web.exceptions.FailSaveException;
import com.mihaineagu.web.exceptions.RessourceNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.jni.Error;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class RestExceptionHandler  {

    private static final Logger logger = LogManager.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(DuplicateEntityExceptions.class)
    protected ResponseEntity<ErrorResponse> handleConflict(DuplicateEntityExceptions exception) {

        ErrorResponse response = new ErrorResponse();
        response.setErrorMessage("This entity already exists!");
        response.setErrorCode("Bad Request");
        response.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RessourceNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleNotFoundRessource(RessourceNotFoundException exception) {

        ErrorResponse response = new ErrorResponse();
        response.setErrorMessage("Requested entity not found!");
        response.setErrorCode("NOT_FOUND");
        response.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(FailSaveException.class)
    protected ResponseEntity<ErrorResponse> handleFaiSaveException(FailSaveException exception) {

        ErrorResponse response = new ErrorResponse();
        response.setErrorMessage("The operation failed, please try again.");
        response.setErrorCode("BAD_GATEAWAY");
        response.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY );
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handlePageNotFound(Exception e){


        logger.error(e);
        ErrorResponse response = new ErrorResponse();
        response.setErrorMessage("Page not found!");
        response.setErrorCode("NOT_FOUND");
        response.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Error.class)
    protected ResponseEntity<ErrorResponse> handleServerError() {


        ErrorResponse response = new ErrorResponse();
        response.setErrorMessage("Internal error");
        response.setErrorCode("InternalError");
        response.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    protected ResponseEntity<ErrorResponse> handleNothingToDelete(Exception e) {

        logger.error(e);
        ErrorResponse response = new ErrorResponse();
        response.setErrorMessage("Please check the request and try again.");
        response.setErrorCode(HttpStatus.BAD_REQUEST.toString());
        response.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected  ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(){
        ErrorResponse response = new ErrorResponse();
        response.setErrorMessage("Please check the request and try again.");
        response.setErrorCode(HttpStatus.BAD_REQUEST.toString());
        response.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
