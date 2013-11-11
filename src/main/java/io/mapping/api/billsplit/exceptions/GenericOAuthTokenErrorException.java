package io.mapping.api.billsplit.exceptions;

import javax.xml.ws.http.HTTPException;

public class GenericOAuthTokenErrorException extends HTTPException {
	private String mMessage;

	@Override
	public String getMessage() {
		return mMessage;
	}

	/**
	 * Constructor for the HTTPException
	 *
	 * @param statusCode <code>int</code> for the HTTP status code
	 */
	public GenericOAuthTokenErrorException(int statusCode) {
		super(statusCode);
	}

	public static class Builder {
		private final GenericOAuthTokenErrorException mGenericOAuthTokenErrorException;

		public Builder(int code) {
			mGenericOAuthTokenErrorException = new GenericOAuthTokenErrorException(code);
		}

		public Builder message(String message) {
			mGenericOAuthTokenErrorException.mMessage = message;
			return this;
		}

		public GenericOAuthTokenErrorException build() {
			return mGenericOAuthTokenErrorException;
		}
	}

	public static class Messages {
		public static final String USER_ALREADY_CONNECTED = "User is already connected";
		public static final String TOKEN_ID_MISMATCH = "Token's user ID doesn't match given user ID";
		public static final String CLIENT_ID_MISMATCH = "Token's client ID does not match app's client ID";
		public static final String TOKEN_VERIFICATION_FAILED = "Token verification failed";

	}
}
