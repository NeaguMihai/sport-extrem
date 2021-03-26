package com.mihaineagu.web.exceptions;

public class DuplicateEntityExceptions extends RuntimeException{
    public DuplicateEntityExceptions() {
        super();
    }

    public DuplicateEntityExceptions(String message) {
        super(message);
    }
}
