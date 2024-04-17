package org.example.test.exeption;

public class BadRequestErrorException extends RuntimeException {
    public BadRequestErrorException(String message) {
        super(message);
    }
}
