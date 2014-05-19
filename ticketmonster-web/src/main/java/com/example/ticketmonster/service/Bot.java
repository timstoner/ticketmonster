package com.example.ticketmonster.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.MultivaluedHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

import com.example.ticketmonster.event.BotEvent;
import com.example.ticketmonster.model.Performance;
import com.example.ticketmonster.model.Show;
import com.example.ticketmonster.model.TicketPrice;
import com.example.ticketmonster.request.BookingRequest;
import com.example.ticketmonster.request.TicketRequest;
import com.example.ticketmonster.rest.BookingService;
import com.example.ticketmonster.rest.ShowService;

@Component
public class Bot implements ApplicationEventPublisherAware {
	private final static Logger LOG = LoggerFactory.getLogger(Bot.class);

	class BotTask extends TimerTask {

		private BotEvent event;

		@Override
		public void run() {
			MultivaluedHashMap<String, String> empty = new MultivaluedHashMap<String, String>();
			// Select a show at random
			// Show show = selectAtRandom(showService.getAll(empty));
			Show show = null;

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
			// Response response = bookingService.createBooking(bookingRequest);
			// if (response.getStatus() == Response.Status.OK.getStatusCode()) {
			// message.append("SUCCESSFUL\n").append(
			// "~~~~~~~~~~~~~~~~~~~~~~~~~\n");
			// } else {
			// message.append("FAILED:\n")
			// .append(((Map<String, Object>) response.getEntity())
			// .get("errors"))
			// .append("~~~~~~~~~~~~~~~~~~~~~~~~~\n");
			// }

			event = new BotEvent(this, message.toString());
			eventPublisher.publishEvent(event);
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

	private ApplicationEventPublisher eventPublisher;

	// @Inject
	// @BotMessage
	// Event<String> event;

	public Timer start() {
		String startMessage = "Bot Started";

		LOG.info(startMessage);

		fireBotEvent(startMessage);

		BotTask task = new BotTask();

		Timer timer = new Timer();
		timer.schedule(task, 0, DURATION);

		return timer;
	}

	public void stop(Timer timer) {
		String stopMessage = "Bot Stopped";

		LOG.info(stopMessage);

		fireBotEvent(stopMessage);

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

	@Override
	public void setApplicationEventPublisher(
			ApplicationEventPublisher applicationEventPublisher) {
		this.eventPublisher = applicationEventPublisher;

	}

	private void fireBotEvent(String message) {
		ApplicationEvent event = new BotEvent(this, message);
		eventPublisher.publishEvent(event);
	}
}
