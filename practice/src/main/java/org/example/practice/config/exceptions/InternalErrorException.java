package org.example.practice.config.exceptions;

public class InternalErrorException extends RuntimeException {
    public InternalErrorException() {
        super("Internal Service Error");
    }

    public InternalErrorException(String message) {
        super(String.format("Internal Error: %s", message));
    }
}
