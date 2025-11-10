package org.example.practice.config.exceptions;

public class InternalErrorException extends RuntimeException {
    public InternalErrorException() {
        super("Internal Service Error");
    }
}
