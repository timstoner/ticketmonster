package com.example.ticketmonster.event;

import org.springframework.context.ApplicationEvent;

public class BotEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;

	private String message;

	public BotEvent(Object source, String message) {
		super(source);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
