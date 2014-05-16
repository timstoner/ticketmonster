package com.example.ticketmonster.rest;

public class SeatAllocationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SeatAllocationException() {
	}

	public SeatAllocationException(String s) {
		super(s);
	}

	public SeatAllocationException(String s, Throwable throwable) {
		super(s, throwable);
	}

	public SeatAllocationException(Throwable throwable) {
		super(throwable);
	}
}
