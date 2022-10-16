package com.coding.challenge.booking.controller;

import com.coding.challenge.booking.error.exception.BookingValidationException;
import com.coding.challenge.booking.input.BookingInput;
import com.coding.challenge.booking.output.BookingOutput;
import com.coding.challenge.booking.service.BookingService;
import com.coding.challenge.booking.validation.BookingValidator;
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

    @Autowired
    private BookingValidator bookingValidator;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookingOutput> createBooking(@Valid @RequestBody BookingInput input) throws Exception {
        bookingValidator.validateInput(input);
        return new ResponseEntity<>(bookingService.createBooking(input), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BookingOutput>> getAllBookings() {
        return new ResponseEntity<>(bookingService.getAllBookings(), HttpStatus.OK);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<BookingOutput> updateBooking(@PathVariable long id, @Valid @RequestBody BookingInput input) throws Exception {
        bookingValidator.validateInput(input);
        return new ResponseEntity<>(bookingService.updateBooking(id, input), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<BookingOutput> getBooking(@PathVariable long id) throws Exception {
        return new ResponseEntity<>(bookingService.getBooking(id), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable long id) throws Exception {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping(path = "/availabilities")
    public ResponseEntity<List<LocalDate>> getAvailableDates(@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                             @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) throws Exception {
        if (startDate == null) {
            startDate = LocalDate.now();
        }

        if (endDate == null) {
            endDate = startDate.plusMonths(1);
        }

        bookingValidator.validateAvailabilitiesDate(startDate, endDate);
        return new ResponseEntity<>(bookingService.getAvailableDates(startDate, endDate), HttpStatus.OK);
    }
}
