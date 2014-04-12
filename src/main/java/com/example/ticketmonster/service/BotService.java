package com.example.ticketmonster.service;

import java.util.List;
import java.util.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.ticketmonster.model.Booking;
import com.example.ticketmonster.rest.BookingService;
import com.example.ticketmonster.util.CircularBuffer;
import com.example.ticketmonster.util.MultivaluedHashMap;

@Component
public class BotService {

	private final static Logger logger = LoggerFactory
			.getLogger(BotService.class);

	private static final int MAX_LOG_SIZE = 50;

	private CircularBuffer<String> log;

	@Autowired
	private Bot bot;

	@Autowired
	private BookingService bookingService;

	// @Inject
	// @BotMessage
	// private Event<String> event;

	private Timer timer;

	public BotService() {
		log = new CircularBuffer<String>(MAX_LOG_SIZE);
	}

	public void start() {
		synchronized (bot) {
			if (timer == null) {
				logger.info("Starting bot");
				timer = bot.start();
			}
		}
	}

	public void stop() {
		synchronized (bot) {
			if (timer != null) {
				logger.info("Stopping bot");
				bot.stop(timer);
				timer = null;
			}
		}
	}

	public void deleteAll() {
		synchronized (bot) {
			stop();
			for (Booking booking : bookingService.getAll(MultivaluedHashMap
					.<String, String> empty())) {
				bookingService.deleteBooking(booking.getId());
				// event.fire("Deleted booking " + booking.getId() + " for "
				// + booking.getContactEmail() + "\n");
			}
		}
	}

	// public void newBookingRequest(@Observes @BotMessage String
	// bookingRequest) {
	// log.add(bookingRequest);
	// }

	public List<String> fetchLog() {
		return log.getContents();
	}

	public boolean isBotActive() {
		return (timer != null);
	}

}