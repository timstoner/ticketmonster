package com.example.ticketmonster.rest.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.ticketmonster.model.SectionAllocation;
import com.example.ticketmonster.rest.SectionAllocationService;
import com.example.ticketmonster.rest.dto.SectionAllocationDTO;

public class SectionAllocationServiceImpl extends
		BaseEntityService<SectionAllocation, SectionAllocationDTO> implements
		SectionAllocationService {

	private static final Logger LOG = LoggerFactory
			.getLogger(SectionAllocationServiceImpl.class);

	public SectionAllocationServiceImpl() {
		super(SectionAllocation.class);
		LOG.debug("Creating Section Allocation Service");
	}

	@Override
	protected SectionAllocationDTO buildDTO(SectionAllocation entity) {
		return entity.convertToDTO();
	}

	@Override
	protected SectionAllocation buildEntity(SectionAllocationDTO dto) {
		SectionAllocation entity = getSingleInstance(dto.getId());

		if (entity != null) {
			
		}

		return entity;
	}

	@Override
	protected String getFindAllQuery() {
		return SectionAllocation.getFindAllQuery();
	}

	@Override
	protected String getFindByIdQuery() {
		return SectionAllocation.getFindByIdQuery();
	}
}