package com.example.ticketmonster.rest;

import javax.ws.rs.Path;

import com.example.ticketmonster.rest.dto.BookingDTO;

@Path("/bookings")
public interface BookingService extends BaseService<BookingDTO> {

}
