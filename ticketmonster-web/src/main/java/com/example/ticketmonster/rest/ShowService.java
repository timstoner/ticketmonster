package com.example.ticketmonster.rest;

import javax.ws.rs.Path;

import com.example.ticketmonster.rest.dto.ShowDTO;

@Path("/shows")
public interface ShowService extends BaseService<ShowDTO> {

}
