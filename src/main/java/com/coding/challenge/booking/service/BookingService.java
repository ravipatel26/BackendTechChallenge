package com.coding.challenge.booking.service;

import com.coding.challenge.booking.entity.BookingEntity;
import com.coding.challenge.booking.input.BookingInput;
import com.coding.challenge.booking.output.BookingOutput;
import com.coding.challenge.booking.persistance.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public BookingOutput createBooking(BookingInput input) {

        BookingEntity entity = bookingRepository.findAll().get(0);

        BookingOutput output = new BookingOutput();
        output.setBookingId(String.valueOf(entity.getId()));
        return output;
    }
}
