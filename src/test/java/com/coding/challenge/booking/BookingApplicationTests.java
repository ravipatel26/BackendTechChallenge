package com.coding.challenge.booking;

import com.coding.challenge.booking.input.BookingInput;
import com.coding.challenge.booking.output.BookingOutput;
import com.coding.challenge.booking.output.ErrorOutput;
import com.coding.challenge.booking.persistance.BookingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BookingApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper jsonMapper;

	@Autowired
	private BookingRepository bookingRepository;

	private static final String ENDPOINT = "/v1/booking";

	@AfterEach
	private void cleanUp() {
		bookingRepository.deleteAll();
	}

	@Test
	public void post_createBooking_shouldReturn201() throws Exception {
		BookingInput input = getBookingInput();
		MvcResult response = mockMvc.perform(post(ENDPOINT).content(jsonMapper.writeValueAsString(input)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andReturn();
		BookingOutput output = jsonMapper.readValue(response.getResponse().getContentAsString(), BookingOutput.class);
		assertNotNull(output.getBookingId());
		assertEquals(input.getFirstName(), output.getFirstName());
		assertEquals(input.getLastName(), output.getLastName());
		assertEquals(input.getEmail(), output.getEmail());
		assertEquals(input.getArrivalDate(), output.getArrivalDate());
		assertEquals(input.getDepartureDate(), output.getDepartureDate());
	}

	@Test
	public void post_createBookingWithInvalidPayload_shouldReturn400() throws Exception {
		BookingInput input = getBookingInput();
		input.setFirstName(null);
		MvcResult response = mockMvc.perform(post(ENDPOINT).content(jsonMapper.writeValueAsString(input)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andReturn();
		ErrorOutput output = jsonMapper.readValue(response.getResponse().getContentAsString(), ErrorOutput.class);
		assertNotNull(output.getErrorMessages());
		assertEquals("First name cannot be blank", output.getErrorMessages().get(0));
	}

	@Test
	public void put_updateBooking_shouldReturn200() throws Exception {
		BookingInput input = getBookingInput();
		MvcResult createResponse = mockMvc.perform(post(ENDPOINT).content(jsonMapper.writeValueAsString(input)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andReturn();
		BookingOutput createOutput = jsonMapper.readValue(createResponse.getResponse().getContentAsString(), BookingOutput.class);

		input.setLastName("User2");
		input.setFirstName("Test2");
		input.setEmail("another@email.com");

		MvcResult updateResponse = mockMvc.perform(put(ENDPOINT + "/" + createOutput.getBookingId()).content(jsonMapper.writeValueAsString(input)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		BookingOutput updateOutput = jsonMapper.readValue(updateResponse.getResponse().getContentAsString(), BookingOutput.class);

		assertNotNull(updateOutput.getBookingId());
		assertEquals(input.getFirstName(), updateOutput.getFirstName());
		assertEquals(input.getLastName(), updateOutput.getLastName());
		assertEquals(input.getEmail(), updateOutput.getEmail());
		assertEquals(input.getArrivalDate(), updateOutput.getArrivalDate());
		assertEquals(input.getDepartureDate(), updateOutput.getDepartureDate());
	}

	@Test
	public void put_updateBookingWithInvalidPayload_shouldReturn400() throws Exception {
		BookingInput input = getBookingInput();
		input.setFirstName(null);
		MvcResult response = mockMvc.perform(put(ENDPOINT + "/" + "1").content(jsonMapper.writeValueAsString(input)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andReturn();
		ErrorOutput output = jsonMapper.readValue(response.getResponse().getContentAsString(), ErrorOutput.class);
		assertNotNull(output.getErrorMessages());
		assertEquals("First name cannot be blank", output.getErrorMessages().get(0));
	}

	@Test
	public void put_updateBookingWithInvalidId_shouldReturn404() throws Exception {
		BookingInput input = getBookingInput();
		MvcResult response = mockMvc.perform(put(ENDPOINT + "/" + "12345").content(jsonMapper.writeValueAsString(input)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andReturn();
		ErrorOutput output = jsonMapper.readValue(response.getResponse().getContentAsString(), ErrorOutput.class);
		assertEquals("Booking not found", output.getErrorMessage());
	}

	@Test
	public void get_getBookingById_shouldReturn200() throws Exception {
		BookingInput input = getBookingInput();
		MvcResult createResponse = mockMvc.perform(post(ENDPOINT).content(jsonMapper.writeValueAsString(input)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andReturn();
		BookingOutput createOutput = jsonMapper.readValue(createResponse.getResponse().getContentAsString(), BookingOutput.class);

		MvcResult getResponse = mockMvc.perform(get(ENDPOINT + "/" + createOutput.getBookingId()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		BookingOutput getOutput = jsonMapper.readValue(getResponse.getResponse().getContentAsString(), BookingOutput.class);

		assertNotNull(getOutput.getBookingId());
		assertEquals(input.getFirstName(), getOutput.getFirstName());
		assertEquals(input.getLastName(), getOutput.getLastName());
		assertEquals(input.getEmail(), getOutput.getEmail());
		assertEquals(input.getArrivalDate(), getOutput.getArrivalDate());
		assertEquals(input.getDepartureDate(), getOutput.getDepartureDate());
	}

	@Test
	public void get_getBookingWithWrongId_shouldReturn404() throws Exception {
		MvcResult response = mockMvc.perform(get(ENDPOINT + "/" + "12345").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andReturn();
		ErrorOutput output = jsonMapper.readValue(response.getResponse().getContentAsString(), ErrorOutput.class);
		assertEquals("Booking not found", output.getErrorMessage());
	}

	@Test
	public void delete_deletedBookingById_shouldReturn204() throws Exception {
		BookingInput input = getBookingInput();
		MvcResult response = mockMvc.perform(post(ENDPOINT).content(jsonMapper.writeValueAsString(input)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andReturn();
		BookingOutput createOutput = jsonMapper.readValue(response.getResponse().getContentAsString(), BookingOutput.class);

		mockMvc.perform(delete(ENDPOINT + "/" + createOutput.getBookingId())).andExpect(status().isNoContent());

		assertEquals(0, bookingRepository.count());
	}

	@Test
	public void delete_deletedBookingWithInvalidId_shouldReturn404() throws Exception {
		MvcResult response = mockMvc.perform(delete(ENDPOINT + "/" + "12345"))
				.andExpect(status().isNotFound())
				.andReturn();
		ErrorOutput output = jsonMapper.readValue(response.getResponse().getContentAsString(), ErrorOutput.class);
		assertEquals("Booking not found", output.getErrorMessage());
	}

	@Test
	public void get_allBookingsWhenNoBookings_shouldReturn200() throws Exception {
		MvcResult getResponse = mockMvc.perform(get(ENDPOINT).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		String response = getResponse.getResponse().getContentAsString();
		CollectionType typeReference = TypeFactory.defaultInstance().constructCollectionType(List.class, BookingOutput.class);
		List<BookingOutput> output = jsonMapper.readValue(response, typeReference);

		assertNotNull(output);
		assertTrue(output.isEmpty());
	}

	@Test
	public void get_allBookingsWithOneBooking_shouldReturn200() throws Exception {
		BookingInput input = getBookingInput();
		mockMvc.perform(post(ENDPOINT).content(jsonMapper.writeValueAsString(input)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andReturn();

		MvcResult getResponse = mockMvc.perform(get(ENDPOINT).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		String response = getResponse.getResponse().getContentAsString();
		CollectionType typeReference = TypeFactory.defaultInstance().constructCollectionType(List.class, BookingOutput.class);
		List<BookingOutput> getOutput = jsonMapper.readValue(response, typeReference);

		assertNotNull(getOutput);
		assertFalse(getOutput.isEmpty());
		assertEquals(input.getFirstName(), getOutput.get(0).getFirstName());
		assertEquals(input.getLastName(), getOutput.get(0).getLastName());
		assertEquals(input.getEmail(), getOutput.get(0).getEmail());
		assertEquals(input.getArrivalDate(), getOutput.get(0).getArrivalDate());
		assertEquals(input.getDepartureDate(), getOutput.get(0).getDepartureDate());
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
