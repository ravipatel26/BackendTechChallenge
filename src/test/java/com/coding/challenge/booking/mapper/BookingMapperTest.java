package com.coding.challenge.booking.mapper;

import com.coding.challenge.booking.entity.BookingEntity;
import com.coding.challenge.booking.input.BookingInput;
import com.coding.challenge.booking.output.BookingOutput;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class BookingMapperTest {

    @Test
    public void test_mapInputToEntity() {
        BookingInput input = getBookingInput();
        BookingEntity entity = BookingMapper.INSTANCE.mapInputToEntity(input);
        assertNull(entity.getId());
        assertNull(entity.getVersion());
        assertEquals(input.getFirstName(), entity.getFirstName());
        assertEquals(input.getLastName(), entity.getLastName());
        assertEquals(input.getEmail(), entity.getEmail());
        assertEquals(input.getArrivalDate(), entity.getArrivalDate());
        assertEquals(input.getDepartureDate(), entity.getDepartureDate());
    }

    @Test
    public void test_mapEntityToOutput() {
        BookingEntity entity = BookingMapper.INSTANCE.mapInputToEntity(getBookingInput());
        entity.setId(1L);
        entity.setVersion(1L);

        BookingOutput output = BookingMapper.INSTANCE.mapEntityToOutput(entity);

        assertEquals(String.valueOf(entity.getId()), output.getBookingId());
        assertEquals(entity.getFirstName(), output.getFirstName());
        assertEquals(entity.getLastName(), output.getLastName());
        assertEquals(entity.getEmail(), output.getEmail());
        assertEquals(entity.getArrivalDate(), output.getArrivalDate());
        assertEquals(entity.getDepartureDate(), output.getDepartureDate());
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
