package com.example.ticketmonster.rest.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.ticketmonster.dto.SectionDTO;
import com.example.ticketmonster.factory.SectionFactory;
import com.example.ticketmonster.model.Section;
import com.example.ticketmonster.rest.SectionService;

public class SectionServiceImpl extends BaseEntityService<Section, SectionDTO>
		implements SectionService {

	private static Logger LOG = LoggerFactory
			.getLogger(SectionServiceImpl.class);

	public SectionServiceImpl() {
		super(Section.class);
		LOG.debug("Creating Section Service");
	}

	@Override
	protected Section buildEntity(SectionDTO dto) {
		return SectionFactory.buildSection(dto, getEntityManager());
	}

	@Override
	protected String getFindAllQuery() {
		return SectionFactory.getFindAllQuery();
	}

	@Override
	protected String getFindByIdQuery() {
		return SectionFactory.getFindByIdQuery();
	}

}