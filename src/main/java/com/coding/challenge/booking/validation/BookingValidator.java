package com.coding.challenge.booking.validation;

import com.coding.challenge.booking.error.exception.BookingValidationException;
import com.coding.challenge.booking.input.BookingInput;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@Component
public class BookingValidator {

    private static final Integer MIN_RESERVATION_DAYS_IN_ADVANCE = 1;
    private static final Integer MAX_RESERVATION_DAYS = 3;

    public void validateInput(BookingInput input) throws BookingValidationException {
        List<String> errors = new ArrayList<>();

        validateFirstName(input.getFirstName(), errors);
        validateLastName(input.getLastName(), errors);
        validateEmail(input.getEmail(), errors);
        validateArrivalDate(input.getArrivalDate(), errors);
        validateDepartureDate(input.getDepartureDate(), errors);

        if (errors.isEmpty()) {
            validateDateConstraint(input.getArrivalDate(), input.getDepartureDate(), errors);
        }

        if (!errors.isEmpty()) {
            throw new BookingValidationException(errors);
        }
    }

    public void validateAvailabilitiesDate(LocalDate startDate, LocalDate endDate) throws BookingValidationException {
        List<String> errors = new ArrayList<>();

        if (startDate.isBefore(LocalDate.now())) {
            errors.add("Start date must be not be in the past");
        }

        if (endDate.isBefore(LocalDate.now())) {
            errors.add("End date must be not be in the past");
        }

        if (startDate.isAfter(endDate)) {
            errors.add("Start date cannot be after end date");
        }

        if (!errors.isEmpty()) {
            throw new BookingValidationException(errors);
        }
    }

    private void validateFirstName(String firstName, List<String> errors) {
        if (ObjectUtils.isEmpty(firstName)) {
            errors.add("First name cannot be blank");
        }
    }

    private void validateLastName(String lastName, List<String> errors) {
        if (ObjectUtils.isEmpty(lastName)) {
            errors.add("Last name cannot be blank");
        }
    }

    private void validateEmail(String email, List<String> errors) {
        if (ObjectUtils.isEmpty(email)) {
            errors.add("Email cannot be blank");
            return;
        }

        if (!EmailValidator.getInstance().isValid(email)) {
            errors.add("Email is not valid");
        }
    }

    private void validateArrivalDate(LocalDate arrivalDate, List<String> errors) {
        if (ObjectUtils.isEmpty(arrivalDate)) {
            errors.add("Arrival date cannot be blank");
            return;
        }

        if (!GenericValidator.isDate(arrivalDate.toString(), "yyyy-MM-dd", true)) {
            errors.add("Arrival date must be in yyyy-MM-dd format");
        }
    }

    private void validateDepartureDate(LocalDate departureDate, List<String> errors) {
        if (ObjectUtils.isEmpty(departureDate)) {
            errors.add("Departure date cannot be blank");
            return;
        }

        if (!GenericValidator.isDate(departureDate.toString(), "yyyy-MM-dd", true)) {
            errors.add("Departure date must be in yyyy-MM-dd format");
        }
    }

    private void validateDateConstraint(LocalDate arrivalDate, LocalDate departureDate, List<String> errors) {
        if (arrivalDate.isBefore(LocalDate.now().plusDays(MIN_RESERVATION_DAYS_IN_ADVANCE))) {
            errors.add("Arrival date must be reserved minimum 1 day(s) ahead of arrival");
            return;
        }

        if (departureDate.isAfter(LocalDate.now().plusMonths(1))) {
            errors.add("Departure date can be reserved up to 1 month in advance");
            return;
        }

        if (arrivalDate.isAfter(departureDate)) {
            errors.add("Arrival date must be before departure date");
            return;
        }

        if (DAYS.between(arrivalDate, departureDate) > MAX_RESERVATION_DAYS) {
            errors.add("Booking can be reserved for max 3 days");
        }
    }

}
