package com.samda.card.game.exceptions;

public class ApiExceptionHandler extends RuntimeException{
    public ApiExceptionHandler() {
        super();

    }

    public ApiExceptionHandler(String message) {
        super(message);

    }
}
