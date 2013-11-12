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

package io.mapping.api.billsplit.resources;

import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.mapping.api.billsplit.exceptions.NoTokenException;
import io.mapping.api.billsplit.entities.UserEntity;
import io.mapping.api.billsplit.oauth2.OAuth2Helper;
import io.mapping.api.billsplit.sessions.SessionAttributes;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("/auth")
@Singleton
public class AuthResource {
	@Context
	private HttpServletRequest mRequest;
	@Context
	private HttpServletResponse mResponse;

	@Inject
	private JacksonFactory mJacksonFactory;
	@Inject
	private OAuth2Helper mOAuth2Helper;
	@Inject
	private EntityManager mEntityManager;

	@Inject
	private UserResource mUserResource;

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public UserEntity login() throws IOException {
		UserEntity userEntity = getSessionUser(mRequest);
		if (userEntity != null) {
			return userEntity;
		}

		String token = mOAuth2Helper.getToken(mRequest);
		if (token != null) {
			GoogleTokenResponse tokenResponse = mOAuth2Helper.parseGoogleToken(token);

			if (tokenResponse == null) {
				throw new NoTokenException(500);
			}

			String googleId = tokenResponse.parseIdToken().getPayload().getSubject();

			try {
				userEntity = (UserEntity) mEntityManager
						.createNamedQuery("user.findByGoogleId")
						.setParameter("googleId", googleId)
						.getSingleResult();
			} catch (NoResultException ex) {
				// UserEntity does not yet exist
				userEntity = mUserResource.createMe(mRequest);
			}

			setUserInSession(userEntity);

			return userEntity;
		} else {
			// Shouldn't be calling login if we don't already have a token
			return null;
		}
	}

	private void setUserInSession(UserEntity userEntity) {
		mRequest.getSession().setAttribute(SessionAttributes.USER, userEntity);
	}

	private UserEntity getSessionUser(HttpServletRequest request) {
		return (UserEntity) request.getSession().getAttribute(SessionAttributes.USER);
	}

	@DELETE
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean logout() {
		mOAuth2Helper.removeState(mRequest);
		mOAuth2Helper.removeToken(mRequest);

		mRequest.getSession().removeAttribute(SessionAttributes.USER);

		return true;
	}
}
