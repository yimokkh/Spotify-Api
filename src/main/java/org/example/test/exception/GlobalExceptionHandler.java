package org.example.test.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.format.DateTimeParseException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({HttpClientErrorException.class, HttpMessageNotReadableException.class,
            MethodArgumentNotValidException.class, MissingServletRequestParameterException.class,
            ConstraintViolationException.class, JsonProcessingException.class,
            BadRequestErrorException.class, DateTimeParseException.class,
            IllegalArgumentException.class, InvalidDataException.class,
            MethodArgumentTypeMismatchException.class,
    })

    public ResponseEntity<ExceptionMessage> handleBadRequestException(Exception exception) {
        return new ResponseEntity<>(
                new ExceptionMessage(HttpStatus.BAD_REQUEST.value(), exception.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ExceptionMessage> handleInternalServerErrorException(
            RuntimeException exception) {
        return new ResponseEntity<>(
                new ExceptionMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ResourceNotFoundException.class, NoHandlerFoundException.class})
    public ResponseEntity<ExceptionMessage> handleNotFoundException(Exception exception) {
        return new ResponseEntity<>(
                new ExceptionMessage(HttpStatus.NOT_FOUND.value(), exception.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ExceptionMessage> handleMethodNotAllowed(Exception exception) {
        return new ResponseEntity<>(
                new ExceptionMessage(HttpStatus.METHOD_NOT_ALLOWED.value(), exception.getMessage()),
                HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<ExceptionMessage> serverException(Exception exception) {
        return new ResponseEntity<>(
                new ExceptionMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
