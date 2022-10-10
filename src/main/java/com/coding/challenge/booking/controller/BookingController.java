package com.coding.challenge.booking.controller;

import com.coding.challenge.booking.input.BookingInput;
import com.coding.challenge.booking.output.BookingOutput;
import com.coding.challenge.booking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public BookingOutput createBooking(BookingInput input) { //TODO: response entity instead for status code?
        return bookingService.createBooking(input);
    }
}
