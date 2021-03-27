package com.mihaineagu.web.exceptions;

public class FailSaveException extends RuntimeException{

    public FailSaveException() {
        super();
    }

    public FailSaveException(String message) {
        super(message);
    }
}
