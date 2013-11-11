package io.mapping.api.billsplit.exceptions;

import javax.xml.ws.http.HTTPException;

public class NullOAuthCodeException extends HTTPException {
	private static final String MESSAGE = "Null OAuth code";

	/**
	 * Constructor for the HTTPException
	 *
	 * @param statusCode <code>int</code> for the HTTP status code
	 */
	public NullOAuthCodeException(int statusCode) {
		super(statusCode);
	}

	@Override
	public String getMessage() {
		return MESSAGE;
	}
}
