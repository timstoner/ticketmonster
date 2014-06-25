package com.example.ticketmonster.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.ticketmonster.model.Booking;
import com.example.ticketmonster.model.Performance;
import com.example.ticketmonster.model.Show;
import com.example.ticketmonster.model.TicketPrice;
import com.example.ticketmonster.request.BookingRequest;
import com.example.ticketmonster.request.TicketRequest;
import com.example.ticketmonster.rest.BookingService;
import com.example.ticketmonster.rest.ShowService;

@Component
public class BotService {

	private final static Logger LOG = LoggerFactory.getLogger(BotService.class);

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

	@Produce(uri="activemq:queue:bot")
	private ProducerTemplate template;

	private static final int MAX_LOG_SIZE = 50;

	private CircularFifoQueue<String> log;

	private ScheduledExecutorService scheduler;

	private ScheduledFuture<?> futureTask;

	public BotService() {
		log = new CircularFifoQueue<String>(MAX_LOG_SIZE);
		scheduler = Executors.newScheduledThreadPool(1);
	}

	public void start() {
		LOG.info("Starting Bot");

		String startMessage = "Bot Started";
		Runnable botTask = new BotTask();

		futureTask = scheduler.scheduleAtFixedRate(botTask, 1000, 1000,
				TimeUnit.MILLISECONDS);

		LOG.info(startMessage);
		fireBotEvent(startMessage);
	}

	public void stop() {
		LOG.info("Stopping Bot");

		// true means may interrupt runnable
		futureTask.cancel(true);

		String stopMessage = "Bot Stopped";
		LOG.info(stopMessage);
		fireBotEvent(stopMessage);
	}

	public void deleteAll() {
		stop();
		MultivaluedHashMap<String, String> empty = new MultivaluedHashMap<String, String>();
		
		for (Booking booking : bookingService.getAll(empty)) {
			bookingService.deleteById(booking.getId());

			String msg = "Deleted booking " + booking.getId() + " for "
					+ booking.getContactEmail() + "\n";
			LOG.debug(msg);
		}
	}

	public List<String> fetchLog() {
		List<String> logCopy;
		synchronized (log) {
			logCopy = new LinkedList<String>(log);
		}
		return logCopy;
	}

	public boolean isBotActive() {
		return futureTask != null && !futureTask.isDone();
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

	private void fireBotEvent(String message) {
		template.sendBody(message);
	}

	class BotTask implements Runnable {

		@Override
		public void run() {
			MultivaluedHashMap<String, String> empty = new MultivaluedHashMap<String, String>();
			// Select a show at random
			Show show = selectAtRandom(showService.getAll(empty));

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

			LOG.debug(message.toString());
		}
	}
}