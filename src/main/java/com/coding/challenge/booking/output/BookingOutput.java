package com.coding.challenge.booking.output;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingOutput {

    private String bookingId;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate arrivalDate;
    private LocalDate departureDate;
}
