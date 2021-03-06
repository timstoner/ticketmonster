package com.example.ticketmonster.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.ticketmonster.request.BotRequest;
import com.example.ticketmonster.service.BotManager;
import com.example.ticketmonster.service.BotState;

@Path("/bot")
public class BotService {

	@Autowired
	private BotManager botManager;

	/**
	 * Produces a JSON representation of the bot's log, containing a maximum of
	 * 50 messages logged by the Bot.
	 * 
	 * @return The JSON representation of the Bot's log
	 */
	@Path("messages")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> getMessages() {
		return botManager.fetchLog();
	}

	/**
	 * Produces a representation of the bot's current state. This is a string -
	 * "RUNNING" or "NOT_RUNNING" depending on whether the bot is active.
	 * 
	 * @return The represntation of the Bot's current state.
	 */
	@Path("status")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBotStatus() {
		BotState state = botManager.isBotActive() ? BotState.START
				: BotState.STOP;
		return Response.ok(state).build();
	}

	/**
	 * Updates the state of the Bot with the provided state. This may trigger
	 * the bot to start itself, stop itself, or stop and delete all existing
	 * bookings.
	 * 
	 * @param updatedStatus
	 *            The new state of the Bot. Only the state property is
	 *            considered; any messages provided are ignored.
	 * @return An empty HTTP 201 response.
	 */
	@Path("status")
	@PUT
	@POST
	public Response updateBotStatus(BotRequest request) {
		switch (request.getState()) {
		case STOP:
			botManager.stop();
			break;
		case RESET:
			botManager.deleteAll();
			break;
		case START:
			botManager.start();
			break;
		default:
			break;
		}

		return Response.noContent().build();
	}

}
