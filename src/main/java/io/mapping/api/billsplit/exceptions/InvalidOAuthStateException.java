package io.mapping.api.billsplit.exceptions;

import javax.xml.ws.http.HTTPException;

public class InvalidOAuthStateException extends HTTPException {
	private static final String MESSAGE = "Invalid state parameter";

	/**
	 * Constructor for the HTTPException
	 *
	 * @param statusCode <code>int</code> for the HTTP status code
	 */
	public InvalidOAuthStateException(int statusCode) {
		super(statusCode);
	}

	@Override
	public String getMessage() {
		return MESSAGE;
	}
}
