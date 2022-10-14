package com.coding.challenge.booking.service;

import com.coding.challenge.booking.entity.BookingEntity;
import com.coding.challenge.booking.input.BookingInput;
import com.coding.challenge.booking.mapper.BookingMapper;
import com.coding.challenge.booking.output.BookingOutput;
import com.coding.challenge.booking.persistance.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    // TODO transactional if multiple calls in a method?
    public BookingOutput createBooking(BookingInput input) {
        BookingEntity entity = BookingMapper.INSTANCE.mapInputToEntity(input);
        BookingEntity responseEntity = bookingRepository.save(entity); //TODO: before save check if available
        return BookingMapper.INSTANCE.mapEntityToOutput(responseEntity);
    }

    public List<BookingOutput> getAllBookings() {
        return bookingRepository.findAll().stream().map(BookingMapper.INSTANCE::mapEntityToOutput).collect(Collectors.toList());
    }

    public BookingOutput updateBooking(long id, BookingInput input) { // todo should return 200 only?
        BookingEntity entity = bookingRepository.findById(id).orElseThrow(() -> new RuntimeException("fix me")); // TODO: add exception if not found
        entity.setEmail(input.getEmail());
        entity.setFirstName(input.getFirstName());
        entity.setLastName(input.getLastName());
        entity.setArrivalDate(input.getArrivalDate());
        entity.setDepartureDate(input.getDepartureDate());

        BookingEntity responseEntity = bookingRepository.save(entity); //TODO: before save check if available
        return BookingMapper.INSTANCE.mapEntityToOutput(responseEntity);
    }

    public BookingOutput getBooking(long id) {
        BookingEntity responseEntity = bookingRepository.findById(id).orElseThrow(() -> new RuntimeException("fix me")); // TODO: add exception if not found
        return BookingMapper.INSTANCE.mapEntityToOutput(responseEntity);
    }

    public void deleteBooking(long id) {
        if (!bookingRepository.existsById(id)) {
            new RuntimeException("fix me"); // TODO: add exception if not found
        }
        bookingRepository.deleteById(id);
    }

    public List<LocalDate> getAvailableDates(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> availableDates = startDate.datesUntil(endDate).collect(Collectors.toList());
        List<BookingEntity> reservedDates = bookingRepository.getAllBookedDatesBetween(startDate, endDate);
        Set<LocalDate> dates = new HashSet<>();
        for (BookingEntity entity : reservedDates) {
            dates.addAll(entity.getArrivalDate().datesUntil(entity.getDepartureDate()).collect(Collectors.toList()));
        }

        availableDates.removeAll(new ArrayList<>(dates));

        return availableDates;
    }
}
