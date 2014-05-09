package com.example.ticketmonster.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;

import javax.ws.rs.core.MultivaluedHashMap;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.example.ticketmonster.event.BotEvent;
import com.example.ticketmonster.model.Booking;
import com.example.ticketmonster.rest.BookingService;

@Component
public class BotService implements ApplicationEventPublisherAware,
		ApplicationListener<BotEvent> {

	private final static Logger LOG = LoggerFactory.getLogger(BotService.class);

	private static final int MAX_LOG_SIZE = 50;

	private CircularFifoQueue<String> log;

	@Autowired
	private Bot bot;

	@Autowired
	private BookingService bookingService;

	private Timer timer;

	private ApplicationEventPublisher publisher;

	public BotService() {
		log = new CircularFifoQueue<String>(MAX_LOG_SIZE);
	}

	public void start() {
		synchronized (bot) {
			if (timer == null) {
				LOG.info("Starting bot");
				timer = bot.start();
			}
		}
	}

	public void stop() {
		synchronized (bot) {
			if (timer != null) {
				LOG.info("Stopping bot");
				bot.stop(timer);
				timer = null;
			}
		}
	}

	public void deleteAll() {
		synchronized (bot) {
			stop();
			MultivaluedHashMap<String, String> empty = new MultivaluedHashMap<String, String>();
			for (Booking booking : bookingService.getAll(empty)) {
				bookingService.deleteBooking(booking.getId());

				String msg = "Deleted booking " + booking.getId() + " for "
						+ booking.getContactEmail() + "\n";
				BotEvent event = new BotEvent(this, msg);
				publisher.publishEvent(event);
			}
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
		return (timer != null);
	}

	@Override
	public void setApplicationEventPublisher(
			ApplicationEventPublisher applicationEventPublisher) {
		this.publisher = applicationEventPublisher;
	}

	@Override
	public void onApplicationEvent(BotEvent event) {
		synchronized (log) {
			log.add(event.getMessage());
		}
	}

}