package io.mapping.api.billsplit.resource;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Person;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.mapping.api.billsplit.exceptions.NoTokenException;
import io.mapping.api.billsplit.models.User;
import io.mapping.api.billsplit.oauth2.OAuth2Helper;
import io.mapping.api.billsplit.session.SessionAttributes;
import io.mapping.api.billsplit.settings.SettingsReader;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("/user")
@Singleton
public class UserResource {
	@Inject
	private OAuth2Helper mOAuth2Helper;

	@Inject
	private EntityManager mEntityManager;

	@Inject
	private HttpTransport mHttpTransport;
	@Inject
	private JacksonFactory mJacksonFactory;

	@Inject
	private SettingsReader mSettingsReader;

	@GET
	@Path("me")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public User getMe(@Context HttpServletRequest request) throws IOException {
		return (User) request.getSession().getAttribute(SessionAttributes.USER);
	}

	@POST
	@Path("me")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public User createMe(@Context HttpServletRequest request) throws IOException {
		String token = mOAuth2Helper.getToken(request);

		if (token != null) {
			GoogleTokenResponse tokenResponse = mOAuth2Helper.parseGoogleToken(token);

			// Get Google internal ID and email address
			String googleId = tokenResponse.parseIdToken().getPayload().getSubject(),
					email = tokenResponse.parseIdToken().getPayload().getEmail();

			// Create a credential representation of the token data
			GoogleCredential credential = mOAuth2Helper.getGoogleCredential(tokenResponse);

			// Get name info from the Plus service
			Plus service = new Plus.Builder(mHttpTransport, mJacksonFactory, credential)
					.setApplicationName(mSettingsReader.getSettings().getApplicationName())
					.build();
			Person person = service.people().get(googleId).execute();

			EntityTransaction transaction = mEntityManager.getTransaction();
			transaction.begin();

			User user = new User
					.Builder()
					.googleId(googleId)
					.email(email)
					.firstName(person.getName().getGivenName())
					.lastName(person.getName().getFamilyName())
					.build();

			mEntityManager.persist(user);

			transaction.commit();

			// Return fresh to ensure it saved and to get 100% consistent values (e.g., id)
			return (User) mEntityManager
					.createNamedQuery("user.findByGoogleId")
					.setParameter("googleId", googleId)
					.getSingleResult();
		} else {
			throw new NoTokenException(500);
		}
	}
}