package com.mihaineagu.web.exceptions;

public class RessourceNotFoundException extends RuntimeException{
    public RessourceNotFoundException() {
    }

    public RessourceNotFoundException(String message) {
        super(message);
    }
}
