package com.coding.challenge.booking.error.handler;

import com.coding.challenge.booking.error.exception.BookingNotFoundException;
import com.coding.challenge.booking.error.exception.BookingSavingException;
import com.coding.challenge.booking.error.exception.BookingValidationException;
import com.coding.challenge.booking.output.ErrorOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Objects;

@RestControllerAdvice
public class BookingExceptionHandler {

    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<ErrorOutput> handleBookingNotFoundException(BookingNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorOutput(e.getMessage(), null));
    }

    @ExceptionHandler(BookingSavingException.class)
    public ResponseEntity<ErrorOutput> handleBookingSavingException(BookingSavingException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorOutput(e.getMessage(), null));
    }

    @ExceptionHandler(BookingValidationException.class)
    public ResponseEntity<ErrorOutput> handleBookingValidationException(BookingValidationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorOutput(null, e.getErrors()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorOutput> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        if (Objects.requireNonNull(e.getMessage()).contains("arrivalDate")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorOutput("Arrival date must be in yyyy-mm-dd format", null));
        }

        if (Objects.requireNonNull(e.getMessage()).contains("departureDate")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorOutput("Departure date must be in yyyy-mm-dd format", null));
        }

        return handleGenericException(e);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorOutput> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        if (Objects.requireNonNull(e.getMessage()).contains("Failed to convert value of type 'java.lang.String' to required type 'java.time.LocalDate")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorOutput("Start date and end date must be in yyyy-mm-dd format", null));
        }

        return handleGenericException(e);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorOutput> handleGenericException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorOutput(e.getMessage(), null));

        //return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorOutput("Unable to process booking request", null));
    }
}
