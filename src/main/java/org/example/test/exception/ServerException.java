package org.example.test.exception;

public class ServerException extends RuntimeException {
    public ServerException(final String message) {
        super(message);
    }
}