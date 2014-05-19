package com.example.ticketmonster.rest;

import javax.ws.rs.Path;

import com.example.ticketmonster.dto.MediaItemDTO;

@Path("/mediaitems")
public interface MediaItemService extends BaseService<MediaItemDTO> {

}
