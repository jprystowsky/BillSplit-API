/*
 * Copyright 2013 Jacob Miles Prystowsky
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.mapping.api.billsplit.resource;

import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.mapping.api.billsplit.exceptions.InvalidOAuthStateException;
import io.mapping.api.billsplit.exceptions.NullOAuthCodeException;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.security.GeneralSecurityException;

import static io.mapping.api.billsplit.exceptions.GenericOAuthTokenErrorException.Builder;
import static io.mapping.api.billsplit.exceptions.GenericOAuthTokenErrorException.Messages;

@Path("/connect")
@Singleton
public class GoogleTokenResource {
	private static final String GOOGLE_REDIRECT_URI = "postmessage";

	@Inject
	private OAuth2Helper mOAuth2Helper;

	@Inject
	private EntityManager mEntityManager;

	@Inject
	private HttpTransport mHttpTransport;
	@Inject
	private JacksonFactory mJacksonFactory;
	@Inject
	private GoogleClientSecrets mGoogleClientSecrets;

	@POST
	@Path("google")
	@Produces(MediaType.APPLICATION_JSON)
	public GoogleTokenResponse connectGoogle(@Context HttpServletRequest request, @Context HttpServletResponse response, @QueryParam("state") final String state) throws IOException {
		// Check to see if they're already connected
		String token = mOAuth2Helper.getToken(request);
		if (token != null) {
			return mOAuth2Helper.parseGoogleToken(token);
		}

		// Allow forcing state
		if (state != null) {
			mOAuth2Helper.setState(request, state);
		}

		// Ensure the state parameter matches up
		if (!mOAuth2Helper.checkState(request, response)) {
			throw new InvalidOAuthStateException(500);
		}

		// Get the authorization code
		ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
		getContent(request.getInputStream(), resultStream);
		String code = new String(resultStream.toByteArray(), "UTF-8");
		if (code == null) {
			throw new NullOAuthCodeException(500);
		}

		// Upgrade the authorization code into an access token and refresh token
		GoogleTokenResponse tokenResponse;
		try {
			tokenResponse = new GoogleAuthorizationCodeTokenRequest(
					mHttpTransport, mJacksonFactory,
					mGoogleClientSecrets.getWeb().getClientId(), mGoogleClientSecrets.getWeb().getClientSecret(),
					code, GOOGLE_REDIRECT_URI)
					.execute();
		} catch (TokenResponseException ex) {
			throw new Builder(500).message(ex.getMessage()).build();
		}

		// Parse out the Google+ ID
		GoogleIdToken idToken = tokenResponse.parseIdToken();
		String userId = idToken.getPayload().getSubject();

		// Verify the token
		boolean verified = false;
		try {
			verified = new GoogleIdTokenVerifier.Builder(mHttpTransport, mJacksonFactory).build().verify(idToken);
		} catch (GeneralSecurityException e) {
			throw new Builder(500).message(e.getMessage()).build();
		}

		if (!verified) {
			throw new Builder(500).message(Messages.TOKEN_VERIFICATION_FAILED).build();
		}

		// Create a credential representation of the token data
		GoogleCredential credential = mOAuth2Helper.getGoogleCredential(tokenResponse);

		// Check token validity
		mOAuth2Helper.checkGoogleTokenValidity(credential, userId);

		// Store the token for next time
		mOAuth2Helper.setToken(request, mJacksonFactory.toString(tokenResponse));

		return tokenResponse;
	}

	/**
	 * Read the content of an InputStream.
	 *
	 * @param inputStream the InputStream to be read.
	 * @return the content of the InputStream as a ByteArrayOutputStream.
	 * @throws IOException
	 */
	static void getContent(InputStream inputStream, ByteArrayOutputStream outputStream) throws IOException {
		// Read the response into a buffered stream
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		int readChar;
		while ((readChar = reader.read()) != -1) {
			outputStream.write(readChar);
		}
		reader.close();
	}
}
