package com.example.ticketmonster.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.ticketmonster.model.Performance;
import com.example.ticketmonster.model.Show;
import com.example.ticketmonster.model.TicketPrice;
import com.example.ticketmonster.rest.BookingRequest;
import com.example.ticketmonster.rest.BookingService;
import com.example.ticketmonster.rest.ShowService;
import com.example.ticketmonster.rest.TicketRequest;
import com.example.ticketmonster.util.MultivaluedHashMap;

@Component
public class Bot {

	class BotTask extends TimerTask {

		@Override
		public void run() {
			// Select a show at random
			Show show = selectAtRandom(showService.getAll(MultivaluedHashMap
					.<String, String> empty()));

			// Select a performance at random
			Performance performance = selectAtRandom(show.getPerformances());

			String requestor = selectAtRandom(BOOKERS);

			BookingRequest bookingRequest = new BookingRequest(performance,
					requestor);

			List<TicketPrice> possibleTicketPrices = new ArrayList<TicketPrice>(
					show.getTicketPrices());

			List<Integer> indicies = selectAtRandom(MAX_TICKET_REQUESTS < possibleTicketPrices
					.size() ? MAX_TICKET_REQUESTS : possibleTicketPrices.size());

			StringBuilder message = new StringBuilder(
					"==========================\n").append("Booking by ")
					.append(requestor).append(" at ")
					.append(new Date().toString()).append("\n")
					.append(performance).append("\n")
					.append("~~~~~~~~~~~~~~~~~~~~~~~~~\n");

			for (int index : indicies) {
				int no = random.nextInt(MAX_TICKETS_PER_REQUEST);
				TicketPrice price = possibleTicketPrices.get(index);
				bookingRequest.addTicketRequest(new TicketRequest(price, no));
				message.append(no).append(" of ").append(price.getSection())
						.append("\n");

			}
			Response response = bookingService.createBooking(bookingRequest);
			if (response.getStatus() == Response.Status.OK.getStatusCode()) {
				message.append("SUCCESSFUL\n").append(
						"~~~~~~~~~~~~~~~~~~~~~~~~~\n");
			} else {
				message.append("FAILED:\n")
						.append(((Map<String, Object>) response.getEntity())
								.get("errors"))
						.append("~~~~~~~~~~~~~~~~~~~~~~~~~\n");
			}
			// TODO: fire bot message event
			// event.fire(message.toString());
		}

	}

	private static final Random random = new Random(System.nanoTime());

	/** Frequency with which the bot will book **/
	public static final long DURATION = TimeUnit.SECONDS.toMillis(3);

	/** Maximum number of ticket requests that will be filed **/
	public static int MAX_TICKET_REQUESTS = 100;

	/** Maximum number of tickets per request **/
	public static int MAX_TICKETS_PER_REQUEST = 100;

	public static String[] BOOKERS = { "anne@acme.com", "george@acme.com",
			"william@acme.com", "victoria@acme.com", "edward@acme.com",
			"elizabeth@acme.com", "mary@acme.com", "charles@acme.com",
			"james@acme.com", "henry@acme.com", "richard@acme.com",
			"john@acme.com", "stephen@acme.com" };

	@Autowired
	private ShowService showService;

	@Autowired
	private BookingService bookingService;

	// @Inject
	// @BotMessage
	// Event<String> event;

	public Timer start() {
		String startMessage = new StringBuilder("==========================\n")
				.append("Bot started at ").append(new Date().toString())
				.append("\n").toString();
		// TODO: first bot started event
		// event.fire(startMessage);
		BotTask task = new BotTask();

		Timer timer = new Timer();
		timer.schedule(task, 0, DURATION);

		return timer;
	}

	public void stop(Timer timer) {
		String stopMessage = new StringBuilder("==========================\n")
				.append("Bot stopped at ").append(new Date().toString())
				.append("\n").toString();
		// TODO: fire bot stopped event
		// event.fire(stopMessage);
		timer.cancel();
	}

	private <T> T selectAtRandom(List<T> list) {
		int i = random.nextInt(list.size());
		return list.get(i);
	}

	private <T> T selectAtRandom(T[] array) {
		int i = random.nextInt(array.length);
		return array[i];
	}

	private <T> T selectAtRandom(Collection<T> collection) {
		int item = random.nextInt(collection.size());
		int i = 0;
		for (T obj : collection) {
			if (i == item)
				return obj;
			i++;
		}
		throw new IllegalStateException();
	}

	private List<Integer> selectAtRandom(int max) {
		List<Integer> indicies = new ArrayList<Integer>();
		for (int i = 0; i < max;) {
			int r = random.nextInt(max);
			if (!indicies.contains(r)) {
				indicies.add(r);
				i++;
			}
		}
		return indicies;
	}
}
