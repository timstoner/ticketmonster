package com.example.ticketmonster.rest;

import javax.ws.rs.Path;

import com.example.ticketmonster.dto.SectionAllocationDTO;

@Path("/sectionallocations")
public interface SectionAllocationService extends
		BaseService<SectionAllocationDTO> {

}
