package com.coding.challenge.booking.error.handler;

import com.coding.challenge.booking.output.ErrorOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BookingExceptionHandler {

    // TODO: proper validation error handling
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorOutput> handleGenericException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorOutput(e.getMessage()));
    }
}
