package com.coding.challenge.booking.validation;

import com.coding.challenge.booking.error.exception.BookingValidationException;
import com.coding.challenge.booking.input.BookingInput;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BookingValidatorTest {

    private BookingValidator bookingValidator = new BookingValidator();

    @Test
    public void validateInput_whenValidInput_shouldNotThrowException() {
        try {
            bookingValidator.validateInput(getBookingInput());
        } catch(Exception e) {
            fail();
        }
    }

    @Test
    public void validateInput_whenEmptyFirstName_shouldThrowBookingValidationException() {
        BookingInput input = getBookingInput();
        input.setFirstName(null);
        BookingValidationException expectedException = assertThrows(BookingValidationException.class, () -> bookingValidator.validateInput(input));
        assertEquals("First name cannot be blank", expectedException.getErrors().get(0));
    }

    @Test
    public void validateInput_whenEmptyLastName_shouldThrowBookingValidationException() {
        BookingInput input = getBookingInput();
        input.setLastName(null);
        BookingValidationException expectedException = assertThrows(BookingValidationException.class, () -> bookingValidator.validateInput(input));
        assertEquals("Last name cannot be blank", expectedException.getErrors().get(0));
    }

    @Test
    public void validateInput_whenEmptyEmail_shouldThrowBookingValidationException() {
        BookingInput input = getBookingInput();
        input.setEmail(null);
        BookingValidationException expectedException = assertThrows(BookingValidationException.class, () -> bookingValidator.validateInput(input));
        assertEquals("Email cannot be blank", expectedException.getErrors().get(0));
    }

    @Test
    public void validateInput_whenInvalidEmail_shouldThrowBookingValidationException() {
        BookingInput input = getBookingInput();
        input.setEmail("invalid");
        BookingValidationException expectedException = assertThrows(BookingValidationException.class, () -> bookingValidator.validateInput(input));
        assertEquals("Email is not valid", expectedException.getErrors().get(0));
    }

    @Test
    public void validateInput_whenEmptyArrivalDate_shouldThrowBookingValidationException() {
        BookingInput input = getBookingInput();
        input.setArrivalDate(null);
        BookingValidationException expectedException = assertThrows(BookingValidationException.class, () -> bookingValidator.validateInput(input));
        assertEquals("Arrival date cannot be blank", expectedException.getErrors().get(0));
    }

    @Test
    public void validateInput_whenPastArrivalDate_shouldThrowBookingValidationException() {
        BookingInput input = getBookingInput();
        input.setArrivalDate(LocalDate.now().minusDays(1));
        BookingValidationException expectedException = assertThrows(BookingValidationException.class, () -> bookingValidator.validateInput(input));
        assertEquals("Arrival date must be reserved minimum 1 day(s) ahead of arrival", expectedException.getErrors().get(0));
    }

    @Test
    public void validateInput_whenArrivalDateIsAfterDepartureDate_shouldThrowBookingValidationException() {
        BookingInput input = getBookingInput();
        input.setArrivalDate(LocalDate.now().plusDays(1));
        input.setDepartureDate(LocalDate.now());
        BookingValidationException expectedException = assertThrows(BookingValidationException.class, () -> bookingValidator.validateInput(input));
        assertEquals("Arrival date must be before departure date", expectedException.getErrors().get(0));
    }

    @Test
    public void validateInput_whenArrivalDateAndDepartureDateAreTooLong_shouldThrowBookingValidationException() {
        BookingInput input = getBookingInput();
        input.setArrivalDate(LocalDate.now().plusDays(1));
        input.setDepartureDate(LocalDate.now().plusDays(5));
        BookingValidationException expectedException = assertThrows(BookingValidationException.class, () -> bookingValidator.validateInput(input));
        assertEquals("Booking can be reserved for max 3 days", expectedException.getErrors().get(0));
    }

    @Test
    public void validateInput_whenEmptyDepartureDate_shouldThrowBookingValidationException() {
        BookingInput input = getBookingInput();
        input.setDepartureDate(null);
        BookingValidationException expectedException = assertThrows(BookingValidationException.class, () -> bookingValidator.validateInput(input));
        assertEquals("Departure date cannot be blank", expectedException.getErrors().get(0));
    }

    @Test
    public void validateInput_whenDepartureDateIsAfter1Month_shouldThrowBookingValidationException() {
        BookingInput input = getBookingInput();
        input.setDepartureDate(LocalDate.now().plusMonths(2));
        BookingValidationException expectedException = assertThrows(BookingValidationException.class, () -> bookingValidator.validateInput(input));
        assertEquals("Departure date can be reserved up to 1 month in advance", expectedException.getErrors().get(0));
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
}
