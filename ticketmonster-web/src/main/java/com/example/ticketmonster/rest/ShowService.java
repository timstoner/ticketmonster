package com.example.ticketmonster.rest;

import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.core.MultivaluedHashMap;

import com.example.ticketmonster.dto.ShowDTO;
import com.example.ticketmonster.model.Show;

@Path("/shows")
public interface ShowService extends BaseService<ShowDTO> {

	List<Show> getAll(MultivaluedHashMap<String, String> empty);

}
