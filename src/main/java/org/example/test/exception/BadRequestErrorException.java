package org.example.test.exception;

public class BadRequestErrorException extends RuntimeException {
    public BadRequestErrorException(String message) {
        super(message);
    }
}
