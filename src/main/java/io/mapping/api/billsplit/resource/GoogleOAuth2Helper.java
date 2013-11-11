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

import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Tokeninfo;
import com.google.inject.Inject;
import io.mapping.api.billsplit.exceptions.GenericOAuthTokenErrorException;
import io.mapping.api.billsplit.oauth2.StateGenerator;
import io.mapping.api.billsplit.session.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GoogleOAuth2Helper implements OAuth2Helper {
	protected static final String KEY_ERROR = "error";

	@Inject
	private HttpTransport mHttpTransport;
	@Inject
	private JacksonFactory mJacksonFactory;
	@Inject
	private GoogleClientSecrets mGoogleClientSecrets;

	@Inject
	StateGenerator mStateGenerator;

	@Override
	public String getState(HttpServletRequest request) {
		return (String) request.getSession().getAttribute(SessionAttributes.STATE);
	}

	@Override
	public void setState(HttpServletRequest request, String state) {
		request.getSession().setAttribute(SessionAttributes.STATE, state);
	}

	@Override
	public void setState(HttpServletRequest request) {
		setState(request, mStateGenerator.generateState());
	}

	@Override
	public void removeState(HttpServletRequest request) {
		request.getSession().removeAttribute(SessionAttributes.STATE);
	}

	@Override
	public boolean checkState(HttpServletRequest request, HttpServletResponse response) {
		return request.getSession().getAttribute(SessionAttributes.STATE).equals(request.getSession().getAttribute(SessionAttributes.STATE));
	}

	@Override
	public String getToken(HttpServletRequest request) {
		return (String) request.getSession().getAttribute(SessionAttributes.TOKEN);
	}

	@Override
	public void setToken(HttpServletRequest request, String token) {
		request.getSession().setAttribute(SessionAttributes.TOKEN, token);
	}

	@Override
	public void removeToken(HttpServletRequest request) {
		request.getSession().removeAttribute(SessionAttributes.TOKEN);
	}

	@Override
	public GoogleTokenResponse parseGoogleToken(String token) throws IOException {
		return mJacksonFactory.fromString(token, GoogleTokenResponse.class);
	}

	@Override
	public GoogleCredential getGoogleCredential(GoogleTokenResponse tokenResponse) {
		// Create a credential representation of the token data
		return new GoogleCredential.Builder()
				.setJsonFactory(mJacksonFactory)
				.setTransport(mHttpTransport)
				.setClientSecrets(mGoogleClientSecrets)
				.build()
				.setFromTokenResponse(tokenResponse);
	}

	@Override
	public void checkGoogleTokenValidity(GoogleCredential credential, String userId) throws IOException {
		Oauth2 oauth2 = new Oauth2.Builder(mHttpTransport, mJacksonFactory, credential).build();
		Tokeninfo tokenInfo = oauth2.tokeninfo().setAccessToken(credential.getAccessToken()).execute();

		// Abort in case of token error
		if (tokenInfo.containsKey(KEY_ERROR)) {
			throw new GenericOAuthTokenErrorException
					.Builder(500)
					.message(tokenInfo.get(KEY_ERROR).toString())
					.build();
		}

		// Abort for mismatched ID
		if (!tokenInfo.getUserId().equals(userId)) {
			throw new GenericOAuthTokenErrorException
					.Builder(500)
					.message(GenericOAuthTokenErrorException.Messages.TOKEN_ID_MISMATCH)
					.build();
		}

		// Abort for mismatched app ID
		if (!tokenInfo.getIssuedTo().equals(mGoogleClientSecrets.getWeb().getClientId())) {
			throw new GenericOAuthTokenErrorException
					.Builder(500)
					.message(GenericOAuthTokenErrorException.Messages.CLIENT_ID_MISMATCH)
					.build();
		}
	}
}
