package com.example.ticketmonster.rest.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.ticketmonster.model.Section;
import com.example.ticketmonster.model.factory.SectionFactory;
import com.example.ticketmonster.rest.SectionService;
import com.example.ticketmonster.rest.dto.SectionDTO;

public class SectionServiceImpl extends BaseEntityService<Section, SectionDTO>
		implements SectionService {

	private static Logger LOG = LoggerFactory
			.getLogger(SectionServiceImpl.class);

	public SectionServiceImpl() {
		super(Section.class);
		LOG.debug("Creating Section Service");
	}

	@Override
	protected SectionDTO buildDTO(Section entity) {
		return null;
	}

	@Override
	protected Section buildEntity(SectionDTO dto) {
		Section entity = getSingleInstance(dto.getId());

		if (entity == null) {
			entity = SectionFactory.buildSection(dto, getEntityManager());
		}

		return entity;
	}

	@Override
	protected String getFindAllQuery() {
		return null;
	}

	@Override
	protected String getFindByIdQuery() {
		return null;
	}

}