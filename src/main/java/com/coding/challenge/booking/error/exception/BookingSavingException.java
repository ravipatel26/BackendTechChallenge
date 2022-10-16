package com.coding.challenge.booking.error.exception;

public class BookingSavingException extends Exception {

    public BookingSavingException() {
        super("Unable to complete booking transaction. Please try again.");
    }
}
