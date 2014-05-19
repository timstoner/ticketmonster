package com.example.ticketmonster.rest.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.ticketmonster.dto.SectionAllocationDTO;
import com.example.ticketmonster.factory.SectionAllocationFactory;
import com.example.ticketmonster.model.SectionAllocation;
import com.example.ticketmonster.rest.SectionAllocationService;

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
	protected SectionAllocation buildEntity(SectionAllocationDTO dto) {
		SectionAllocation entity = SectionAllocationFactory
				.buildEntity(dto, em);

		return entity;
	}

	@Override
	protected String getFindAllQuery() {
		return SectionAllocationFactory.getFindAllQuery();
	}

	@Override
	protected String getFindByIdQuery() {
		return SectionAllocationFactory.getFindByIdQuery();
	}
}