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

package io.mapping.api.billsplit.settings;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import io.mapping.api.billsplit.settings.models.APISettings;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Logger;

/**
 * A reader for obtaining {@link APISettings} objects from a JSON file.
 */
public class JsonFileSettingsReader implements SettingsReader {
	private String mFileName;

	@Inject
	private Logger mLogger;

	@Override
	public APISettings getSettings() {
		Reader reader;
		APISettings settings = null;

		try {
			reader = new InputStreamReader(this.getClass().getResourceAsStream(mFileName));
			settings = new ObjectMapper().readValue(reader, APISettings.class);
		} catch (FileNotFoundException e) {
			mLogger.severe("FileNotFoundException trying to read settings file " + mFileName);
			e.printStackTrace();
		} catch (JsonMappingException e) {
			mLogger.severe("JsonMappingException trying to read settings file " + mFileName);
			e.printStackTrace();
		} catch (JsonParseException e) {
			mLogger.severe("JsonParseException trying to read settings file " + mFileName);
			e.printStackTrace();
		} catch (IOException e) {
			mLogger.severe("IOException trying to read settings file " + mFileName);
			e.printStackTrace();
		}

		return settings;
	}

	/**
	 * An internal Builder class used to instantiate readers.
	 */
	public static class Builder {
		private String mFileName;

		public Builder fileName(String fileName) {
			mFileName = fileName;
			return this;
		}

		public JsonFileSettingsReader build() {
			JsonFileSettingsReader jsonFileSettingsReader = new JsonFileSettingsReader();

			jsonFileSettingsReader.mFileName = mFileName;

			return jsonFileSettingsReader;
		}
	}
}
