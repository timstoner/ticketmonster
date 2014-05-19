package com.example.ticketmonster.rest;

import javax.ws.rs.Path;

import com.example.ticketmonster.dto.ShowDTO;

@Path("/shows")
public interface ShowService extends BaseService<ShowDTO> {

}
