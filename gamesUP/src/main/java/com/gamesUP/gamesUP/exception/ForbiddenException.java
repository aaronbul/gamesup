package com.gamesUP.gamesUP.exception;

public class ForbiddenException extends RuntimeException {
    
    public ForbiddenException(String message) {
        super(message);
    }
    
    public ForbiddenException() {
        super("Accès interdit");
    }
} 