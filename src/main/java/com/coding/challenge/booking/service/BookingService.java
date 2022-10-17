package com.coding.challenge.booking.service;

import com.coding.challenge.booking.entity.BookingEntity;
import com.coding.challenge.booking.error.exception.BookingNotFoundException;
import com.coding.challenge.booking.error.exception.BookingSavingException;
import com.coding.challenge.booking.error.exception.BookingValidationException;
import com.coding.challenge.booking.input.BookingInput;
import com.coding.challenge.booking.mapper.BookingMapper;
import com.coding.challenge.booking.output.BookingOutput;
import com.coding.challenge.booking.persistance.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.OptimisticLockException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public BookingOutput createBooking(BookingInput input) throws Exception {
        if (!areBookingDatesAvailable(input)) {
            throw new RuntimeException("booking dates not available");
        }

        try {
            BookingEntity entity = BookingMapper.INSTANCE.mapInputToEntity(input);
            BookingEntity responseEntity = bookingRepository.save(entity);
            return BookingMapper.INSTANCE.mapEntityToOutput(responseEntity);
        } catch (OptimisticLockException e) {
            throw new Exception("Unable to complete booking creation. Please try again.");
        }
    }

    public List<BookingOutput> getAllBookings() {
        return bookingRepository.findAll().stream().map(BookingMapper.INSTANCE::mapEntityToOutput).collect(Collectors.toList());
    }

    public BookingOutput updateBooking(long id, BookingInput input) throws Exception {
        BookingEntity entity = bookingRepository.findById(id).orElseThrow(BookingNotFoundException::new);

        if (!areBookingDatesAvailableToUpdate(input, entity)) {
            throw new BookingValidationException(List.of("Booking dates not available"));
        }

        entity.setEmail(input.getEmail());
        entity.setFirstName(input.getFirstName());
        entity.setLastName(input.getLastName());
        entity.setArrivalDate(input.getArrivalDate());
        entity.setDepartureDate(input.getDepartureDate());

        try {
            BookingEntity responseEntity = bookingRepository.save(entity);
            return BookingMapper.INSTANCE.mapEntityToOutput(responseEntity);
        } catch (OptimisticLockException e) {
            throw new BookingSavingException();
        }
    }

    public BookingOutput getBooking(long id) throws Exception {
        BookingEntity responseEntity = bookingRepository.findById(id).orElseThrow(BookingNotFoundException::new);
        return BookingMapper.INSTANCE.mapEntityToOutput(responseEntity);
    }

    public void deleteBooking(long id) throws Exception {
        if (!bookingRepository.existsById(id)) {
            throw new BookingNotFoundException();
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

    private boolean areBookingDatesAvailable(BookingInput input) {
        List<LocalDate> availableDates = getAvailableDates(input.getArrivalDate(), input.getDepartureDate());
        return availableDates != null && new HashSet<>(availableDates).containsAll(input.getArrivalDate().datesUntil(input.getDepartureDate()).collect(Collectors.toList()));
    }

    private boolean areBookingDatesAvailableToUpdate(BookingInput newInput, BookingEntity oldEntity) {
        List<LocalDate> availableDates = getAvailableDates(newInput.getArrivalDate(), newInput.getDepartureDate());
        availableDates.addAll(oldEntity.getArrivalDate().datesUntil(oldEntity.getDepartureDate()).collect(Collectors.toList()));
        return availableDates != null && new HashSet<>(availableDates).containsAll(newInput.getArrivalDate().datesUntil(newInput.getDepartureDate()).collect(Collectors.toList()));
    }
}
