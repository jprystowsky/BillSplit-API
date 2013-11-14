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

package io.mapping.api.billsplit.guice;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import io.mapping.api.billsplit.oauth2.BigIntegerStateGenerator;
import io.mapping.api.billsplit.oauth2.GoogleOAuth2Helper;
import io.mapping.api.billsplit.oauth2.OAuth2Helper;
import io.mapping.api.billsplit.oauth2.StateGenerator;
import io.mapping.api.billsplit.reflection.AutoPackageNameProviderAlgorithm;
import io.mapping.api.billsplit.reflection.PackageNameProvider;
import io.mapping.api.billsplit.reflection.PackageNameProviderAlgorithm;
import io.mapping.api.billsplit.reflection.RuntimePackageNameProvider;
import io.mapping.api.billsplit.resources.AuthResource;
import io.mapping.api.billsplit.resources.ConnectGoogleResource;
import io.mapping.api.billsplit.resources.UserResource;
import io.mapping.api.billsplit.sessions.SessionAttributes;
import io.mapping.api.billsplit.sessions.SessionAttributesImpl;
import io.mapping.api.billsplit.settings.JsonFileSettingsReader;
import io.mapping.api.billsplit.settings.SettingsReader;
import io.mapping.api.billsplit.settings.models.APISettings;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class BillSplitServletModule extends ServletModule {
	private static final String FILE_SETTINGS = "/settings.json";
	private static final String FILE_GOOGLE_OAUTH = "/google_oauth.json";

	private static final SettingsReader SETTINGS_READER = new JsonFileSettingsReader
			.Builder()
			.fileName(FILE_SETTINGS)
			.build();

	private static final JacksonFactory JSON_FACTORY = new JacksonFactory();

	@Override
	protected void configureServlets() {
		/**
		 * Bind resources (i.e., backings to RESTful endpoints)
		 */
		bind(ConnectGoogleResource.class);
		bind(AuthResource.class);
		bind(UserResource.class);

		/**
		 * Perform dependency injection (DI)
		 */
		bind(HttpTransport.class).to(NetHttpTransport.class).in(Scopes.SINGLETON);

		bind(StateGenerator.class).to(BigIntegerStateGenerator.class).in(Scopes.SINGLETON);
		bind(OAuth2Helper.class).to(GoogleOAuth2Helper.class).in(Scopes.SINGLETON);
		bind(PackageNameProvider.class).to(RuntimePackageNameProvider.class).in(Scopes.SINGLETON);
		bind(PackageNameProviderAlgorithm.class).to(AutoPackageNameProviderAlgorithm.class).in(Scopes.SINGLETON);
		bind(SessionAttributes.class).to(SessionAttributesImpl.class).in(Scopes.SINGLETON);

		/**
		 * Hook in Jackson
		 */
		bind(JacksonJsonProvider.class).in(Scopes.SINGLETON);

		/**
		 * Establish filtering.
		 */
		//filter("/*").through(CORSFilter.class);

		/**
		 * Serve it up, boss!
		 * Set the com.sun.jersey.spi.container.ContainerResponseFilters parameter to our class to indicate
		 * that it should handle {@link com.sun.jersey.spi.container.ContainerResponseFilter}s.
		 *
		 * Semicolon-delineate multiple items in a list.
		 */
		Map<String, String> params = new HashMap<>();
		params.put("com.sun.jersey.spi.container.ContainerResponseFilters", "io.mapping.api.billsplit.filters.CORSFilter");
		serve("/*").with(GuiceContainer.class, params);
	}

	@Provides
	@Singleton
	EntityManager provideEntityManager() {
		APISettings apiSettings = SETTINGS_READER.getSettings();

		return Persistence
				.createEntityManagerFactory(apiSettings.getPersistenceUnitName())
				.createEntityManager();
	}

	@Provides
	@Singleton
	GoogleClientSecrets provideGoogleClientSecrets() {
		GoogleClientSecrets googleClientSecrets = null;

		try {
			Reader reader = new InputStreamReader(this.getClass().getResourceAsStream(FILE_GOOGLE_OAUTH));
			googleClientSecrets = GoogleClientSecrets.load(JSON_FACTORY, reader);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return googleClientSecrets;
	}

	@Provides
	@Singleton
	SettingsReader provideSettingsReader() {
		return SETTINGS_READER;
	}
}
