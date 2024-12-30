package com.example.toMeMail.exception;

import com.example.toMeMail.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    /*@ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse errorResponse = new ErrorResponse("Bad Request", List.of(ex.getMessage()));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse("User Not Found", List.of(ex.getMessage()));
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse("Internal Server Error", List.of(ex.getMessage()));
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }*/
    @ExceptionHandler(MessageAccessBeforeDueDateException.class)
    public ResponseEntity<String> handleAccessBeforeDueDate(MessageAccessBeforeDueDateException ex) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }
}
