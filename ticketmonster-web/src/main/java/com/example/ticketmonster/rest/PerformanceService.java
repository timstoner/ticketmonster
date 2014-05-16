package com.example.ticketmonster.rest;

import javax.ws.rs.Path;

import com.example.ticketmonster.rest.dto.PerformanceDTO;

@Path("/performances")
public interface PerformanceService extends BaseService<PerformanceDTO> {
}
