package com.example.ticketmonster.rest;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.example.ticketmonster.model.Booking;
import com.example.ticketmonster.request.BookingRequest;

public interface BookingService extends BaseService<Booking> {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	public Response createBooking(BookingRequest bookingRequest);

}
