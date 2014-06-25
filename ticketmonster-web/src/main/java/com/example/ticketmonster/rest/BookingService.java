package com.example.ticketmonster.rest;

import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;

import com.example.ticketmonster.dto.BookingDTO;
import com.example.ticketmonster.model.Booking;
import com.example.ticketmonster.request.BookingRequest;

@Path("/bookings")
public interface BookingService extends BaseService<BookingDTO> {

	public Response createBooking(BookingRequest bookingRequest);

	List<Booking> getAll(MultivaluedHashMap<String, String> parameters);
}
