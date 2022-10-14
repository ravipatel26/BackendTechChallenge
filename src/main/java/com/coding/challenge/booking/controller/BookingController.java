package com.coding.challenge.booking.controller;

import com.coding.challenge.booking.input.BookingInput;
import com.coding.challenge.booking.output.BookingOutput;
import com.coding.challenge.booking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookingOutput> createBooking(@Valid @RequestBody BookingInput input) { //TODO: response entity instead for status code?
        return new ResponseEntity<>(bookingService.createBooking(input), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BookingOutput>> getAllBookings() {
        return new ResponseEntity<>(bookingService.getAllBookings(), HttpStatus.OK);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<BookingOutput> updateBooking(@PathVariable long id, @Valid @RequestBody BookingInput input) {
        return new ResponseEntity<>(bookingService.updateBooking(id, input), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<BookingOutput> getBooking(@PathVariable long id) {
        return new ResponseEntity<>(bookingService.getBooking(id), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping(path = "/availabilities")
    public ResponseEntity<List<LocalDate>> getAvailableDates(@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                             @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        // TODO: what if start and end dates are in the past?
        if (startDate == null) {
            startDate = LocalDate.now();
        }

        if (endDate == null) {
            endDate = startDate.plusMonths(1);
        }

        if (startDate.isAfter(endDate)) {
            throw new RuntimeException("start date cannot be after edn date"); //TODO: fix this exception
        }

        return new ResponseEntity<>(bookingService.getAvailableDates(startDate, endDate), HttpStatus.OK);
    }
}
