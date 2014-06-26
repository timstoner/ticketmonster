package com.example.ticketmonster.request;

import com.example.ticketmonster.service.BotState;

public class BotRequest {
	private BotState state;

	public BotState getState() {
		return state;
	}

	public void setState(BotState state) {
		this.state = state;
	}
}
