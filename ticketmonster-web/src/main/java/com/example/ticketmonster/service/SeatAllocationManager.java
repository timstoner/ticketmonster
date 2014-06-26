package com.example.ticketmonster.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import com.example.ticketmonster.model.Performance;
import com.example.ticketmonster.model.Seat;
import com.example.ticketmonster.model.Section;
import com.example.ticketmonster.model.SectionAllocation;
import com.example.ticketmonster.rest.SeatAllocationException;

@Component
public class SeatAllocationManager {

	@PersistenceContext
	EntityManager entityManager;

	public AllocatedSeats allocateSeats(Section section,
			Performance performance, int seatCount, boolean contiguous) {
		SectionAllocation sectionAllocation = retrieveSectionAllocationExclusively(
				section, performance);
		List<Seat> seats = sectionAllocation.allocateSeats(seatCount,
				contiguous);
		return new AllocatedSeats(sectionAllocation, seats);
	}

	public void deallocateSeats(Section section, Performance performance,
			List<Seat> seats) {
		SectionAllocation sectionAllocation = retrieveSectionAllocationExclusively(
				section, performance);
		for (Seat seat : seats) {
			if (!seat.getSection().equals(section)) {
				throw new SeatAllocationException(
						"All seats must be in the same section!");
			}
			sectionAllocation.deallocate(seat);
		}
	}

	private SectionAllocation retrieveSectionAllocationExclusively(
			Section section, Performance performance) {
		SectionAllocation sectionAllocationStatus = (SectionAllocation) entityManager
				.createQuery(
						"select s from SectionAllocation s where "
								+ "s.performance.id = :performanceId and "
								+ "s.section.id = :sectionId")
				.setParameter("performanceId", performance.getId())
				.setParameter("sectionId", section.getId()).getSingleResult();
		entityManager.lock(sectionAllocationStatus,
				LockModeType.PESSIMISTIC_WRITE);
		return sectionAllocationStatus;
	}
}
