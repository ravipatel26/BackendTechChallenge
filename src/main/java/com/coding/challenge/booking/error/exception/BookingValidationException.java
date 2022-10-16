package com.coding.challenge.booking.error.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class BookingValidationException extends Exception {

    private List<String> errors;

    public BookingValidationException(List<String> errors) {
        this.errors = errors;
    }
}
