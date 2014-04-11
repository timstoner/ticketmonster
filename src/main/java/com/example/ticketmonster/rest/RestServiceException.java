package com.example.ticketmonster.rest;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class RestServiceException extends WebApplicationException {

	private static final long serialVersionUID = 1L;

	public RestServiceException() {
	}

	public RestServiceException(Response response) {
		super(response);
	}

	public RestServiceException(int status) {
		super(status);
	}

	public RestServiceException(Response.Status status) {
		super(status);
	}

	public RestServiceException(Throwable cause) {
		super(cause);
	}

	public RestServiceException(Throwable cause, Response response) {
		super(cause, response);
	}

	public RestServiceException(Throwable cause, int status) {
		super(cause, status);
	}

	public RestServiceException(Throwable cause, Response.Status status) {
		super(cause, status);
	}
}
