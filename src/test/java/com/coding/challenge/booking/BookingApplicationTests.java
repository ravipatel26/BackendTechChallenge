package com.coding.challenge.booking;

import com.coding.challenge.booking.input.BookingInput;
import com.coding.challenge.booking.output.BookingOutput;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
	public void test_create_endpoint() throws Exception {
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
	public void test_update_endpoint() throws Exception {
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
	public void test_get_endpoint() throws Exception {
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
	public void test_delete_endpoint() throws Exception {
		BookingInput input = getBookingInput();
		MvcResult response = mockMvc.perform(post(ENDPOINT).content(jsonMapper.writeValueAsString(input)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andReturn();
		BookingOutput createOutput = jsonMapper.readValue(response.getResponse().getContentAsString(), BookingOutput.class);

		mockMvc.perform(delete(ENDPOINT + "/" + createOutput.getBookingId())).andExpect(status().isNoContent());

		assertEquals(0, bookingRepository.count());
	}

	@Test
	public void test_get_all_endpoint() throws Exception {
		MvcResult getResponse = mockMvc.perform(get(ENDPOINT).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		String response = getResponse.getResponse().getContentAsString();
		CollectionType typeReference = TypeFactory.defaultInstance().constructCollectionType(List.class, BookingOutput.class);
		List<BookingOutput> output = jsonMapper.readValue(response, typeReference);

		assertNotNull(output);
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
