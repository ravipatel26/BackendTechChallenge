package com.coding.challenge.booking.controller;

import com.coding.challenge.booking.input.BookingInput;
import com.coding.challenge.booking.output.BookingOutput;
import com.coding.challenge.booking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

//
//    @Operation(summary = "Get the list of all reservations.")
//    @GetMapping(path = BASE_PATH)
//    public Stream<Booking> getBookingList() {
//        return bookingService.findAll()
//                .stream()
//                .map(Booking::createFrom);
//    }
//
//    @Operation(summary = "Get the information of the reservation with the given id.")

//    @Operation(summary = "Get information of the availability of the campsite for a given date range with the default being 1 month since today.")
//    @GetMapping(path = BASE_AVAILABLE_PATH)
//    public List<LocalDate> getAvailabilitiesBetween(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//                                                    @RequestParam(required = false)
//                                                    @Parameter(name = "Start date",
//                                                            description = "Start date (included), default: today")
//                                                    LocalDate start,
//                                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//                                                    @RequestParam(required = false)
//                                                    @Parameter(name = "End date",
//                                                            description = "End date (excluded), default: start date + 1 month")
//                                                    LocalDate end) {
//        if (start == null) {
//            start = LocalDate.now();
//        }
//        if (end == null) {
//            end = start.plusMonths(1);
//        }
//        if (start.isAfter(end)) {
//            throw new BadRequestException(
//                    MessageFormat.format("Start date {0} is after end date {1}", start, end));
//        }
//        log.info("Get availabilities between {} and {}", start, end);
//        return bookingService.getAvailabilities(start, end);
//    }
//
//    @DeleteMapping(path = BASE_PATH + "/{id}")
//    public void deleteBooking(@PathVariable long id) {
//        log.info("Delete booking {}", id);
//        try {
//            bookingService.deleteById(id);
//        } catch (EmptyResultDataAccessException e) {
//            throw new BookingNotFoundException(id);
//        }
//    }
}
