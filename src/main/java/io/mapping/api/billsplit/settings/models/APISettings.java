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

package io.mapping.api.billsplit.settings.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * An internal model representing API settings.
 */
public class APISettings {
	private String mPersistenceUnitName;
	private String mApplicationName;

	@JsonProperty("persistenceUnitName")
	public String getPersistenceUnitName() {
		return mPersistenceUnitName;
	}

	@JsonProperty("applicationName")
	public String getApplicationName() {
		return mApplicationName;
	}

	public void setPersistenceUnitName(String persistenceUnitName) {
		mPersistenceUnitName = persistenceUnitName;
	}

	public void setApplicationName(String applicationName) {
		mApplicationName = applicationName;
	}
}
