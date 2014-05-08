package com.example.ticketmonster.event;

import org.springframework.context.ApplicationEvent;

public class BookingEvent extends ApplicationEvent {
	public enum EventType {
		CREATED, CANCELLED, DELETED
	}

	private static final long serialVersionUID = 1L;

	private long id;

	private EventType eventType;

	public BookingEvent(Object source, long id, EventType type) {
		super(source);
		this.id = id;
		this.eventType = type;
	}

	public long getBookingId() {
		return id;
	}

	public EventType getEventType() {
		return eventType;
	}

}
