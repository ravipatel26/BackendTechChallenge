package com.coding.challenge.booking.service;

import com.coding.challenge.booking.entity.BookingEntity;
import com.coding.challenge.booking.error.exception.BookingNotFoundException;
import com.coding.challenge.booking.error.exception.BookingSavingException;
import com.coding.challenge.booking.error.exception.BookingValidationException;
import com.coding.challenge.booking.input.BookingInput;
import com.coding.challenge.booking.mapper.BookingMapper;
import com.coding.challenge.booking.output.BookingOutput;
import com.coding.challenge.booking.persistance.BookingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.OptimisticLockException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepositoryMock;

    @InjectMocks
    private BookingService bookingService = new BookingService();

    @Test
    public void createBooking_whenValidBookingInput_shouldReturnBookingOutput() throws Exception {
        BookingInput input = getBookingInput();
        when(bookingRepositoryMock.getAllBookedDatesBetween(any(LocalDate.class), any(LocalDate.class))).thenReturn(new ArrayList<>());
        when(bookingRepositoryMock.save(any(BookingEntity.class))).thenReturn(getBookingEntity());

        BookingOutput output = bookingService.createBooking(input);

        assertEquals(input.getFirstName(), output.getFirstName());
        assertEquals(input.getLastName(), output.getLastName());
        assertEquals(input.getEmail(), output.getEmail());
        assertEquals(input.getArrivalDate(), output.getArrivalDate());
        assertEquals(input.getDepartureDate(), output.getDepartureDate());
    }

    @Test
    public void createBooking_whenBookingDatesNotAvailable_shouldThrowBookingValidationException() {
        BookingInput input = getBookingInput();
        when(bookingRepositoryMock.getAllBookedDatesBetween(any(LocalDate.class), any(LocalDate.class))).thenReturn(getBookingEntities());

        assertThrows(BookingValidationException.class, () -> bookingService.createBooking(input));
    }

    @Test
    public void createBooking_whenUnableToSave_shouldThrowBookingSavingException() {
        BookingInput input = getBookingInput();
        when(bookingRepositoryMock.getAllBookedDatesBetween(any(LocalDate.class), any(LocalDate.class))).thenReturn(new ArrayList<>());
        when(bookingRepositoryMock.save(any(BookingEntity.class))).thenThrow(OptimisticLockException.class);

        Throwable expectedException = assertThrows(BookingSavingException.class, () -> bookingService.createBooking(input));
        assertEquals("Unable to complete booking transaction. Please try again.", expectedException.getMessage());
    }

    @Test
    public void updateBooking_whenValidBookingInput_shouldReturnBookingOutput() throws Exception {
        BookingInput input = getBookingInput();
        when(bookingRepositoryMock.findById(anyLong())).thenReturn(Optional.ofNullable(getBookingEntity()));
        when(bookingRepositoryMock.getAllBookedDatesBetween(any(LocalDate.class), any(LocalDate.class))).thenReturn(new ArrayList<>());
        when(bookingRepositoryMock.save(any(BookingEntity.class))).thenReturn(getBookingEntity());

        BookingOutput output = bookingService.updateBooking(1, input);

        assertEquals(input.getFirstName(), output.getFirstName());
        assertEquals(input.getLastName(), output.getLastName());
        assertEquals(input.getEmail(), output.getEmail());
        assertEquals(input.getArrivalDate(), output.getArrivalDate());
        assertEquals(input.getDepartureDate(), output.getDepartureDate());
    }

    @Test
    public void updateBooking_whenInvalidBookingId_shouldThrowBookingNotFoundException() {
        BookingInput input = getBookingInput();
        when(bookingRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

        Throwable expectedException = assertThrows(BookingNotFoundException.class, () -> bookingService.updateBooking(1, input));
        assertEquals("Booking not found", expectedException.getMessage());
    }


    @Test
    public void updateBooking_whenBookingDatesNotAvailable_shouldThrowBookingValidationException() {
        BookingInput input = getBookingInput();
        BookingEntity entity = getBookingEntity();
        entity.setArrivalDate(entity.getArrivalDate().plusDays(1));
        entity.setDepartureDate(entity.getDepartureDate().plusDays(1));
        when(bookingRepositoryMock.findById(anyLong())).thenReturn(Optional.of(entity));
        when(bookingRepositoryMock.getAllBookedDatesBetween(any(LocalDate.class), any(LocalDate.class))).thenReturn(getBookingEntities());

        assertThrows(BookingValidationException.class, () -> bookingService.updateBooking(1, input));
    }

    @Test
    public void updateBooking_whenUnableToSave_shouldThrowBookingSavingException() {
        BookingInput input = getBookingInput();
        when(bookingRepositoryMock.findById(anyLong())).thenReturn(Optional.ofNullable(getBookingEntity()));
        when(bookingRepositoryMock.getAllBookedDatesBetween(any(LocalDate.class), any(LocalDate.class))).thenReturn(new ArrayList<>());
        when(bookingRepositoryMock.save(any(BookingEntity.class))).thenThrow(OptimisticLockException.class);

        Throwable expectedException = assertThrows(BookingSavingException.class, () -> bookingService.updateBooking(1, input));
        assertEquals("Unable to complete booking transaction. Please try again.", expectedException.getMessage());
    }

    @Test
    public void getBooking_whenValidBookingInput_shouldReturnBookingOutput() throws Exception {
        BookingInput input = getBookingInput();
        when(bookingRepositoryMock.findById(anyLong())).thenReturn(Optional.ofNullable(getBookingEntity()));
        BookingOutput output = bookingService.getBooking(1);

        assertEquals(input.getFirstName(), output.getFirstName());
        assertEquals(input.getLastName(), output.getLastName());
        assertEquals(input.getEmail(), output.getEmail());
        assertEquals(input.getArrivalDate(), output.getArrivalDate());
        assertEquals(input.getDepartureDate(), output.getDepartureDate());
    }

    @Test
    public void getBooking_whenInvalidBookingId_shouldThrowBookingNotFoundException() {
        when(bookingRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());
        Throwable expectedException = assertThrows(BookingNotFoundException.class, () -> bookingService.getBooking(1));
        assertEquals("Booking not found", expectedException.getMessage());
    }

    @Test
    public void deleteBooking_whenValidBookingInput_shouldNotThrowException() {
        when(bookingRepositoryMock.existsById(anyLong())).thenReturn(true);
        try {
            bookingService.deleteBooking(1);
        } catch(Exception e) {
            fail();
        }
    }

    @Test
    public void deleteBooking_whenInvalidBookingId_shouldThrowBookingNotFoundException() {
        when(bookingRepositoryMock.existsById(anyLong())).thenReturn(false);
        Throwable expectedException = assertThrows(BookingNotFoundException.class, () -> bookingService.deleteBooking(1));
        assertEquals("Booking not found", expectedException.getMessage());
    }

    private BookingInput getBookingInput() {
        BookingInput input = new BookingInput();
        input.setFirstName("Test");
        input.setLastName("User");
        input.setEmail("test@email.com");
        input.setArrivalDate(LocalDate.now().plusDays(1));
        input.setDepartureDate(LocalDate.now().plusDays(3));
        return input;
    }

    private BookingEntity getBookingEntity() {
        return BookingMapper.INSTANCE.mapInputToEntity(getBookingInput());
    }

    private List<BookingEntity> getBookingEntities() {
        return List.of(getBookingEntity());
    }
}
