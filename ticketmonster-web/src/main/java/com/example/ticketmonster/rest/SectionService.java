package com.example.ticketmonster.rest;

import javax.ws.rs.Path;

import com.example.ticketmonster.dto.SectionDTO;

@Path("/sections")
public interface SectionService extends BaseService<SectionDTO> {

}
