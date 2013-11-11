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

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface OAuth2Helper {
	public String getState(HttpServletRequest request);
	public void setState(HttpServletRequest request, String state);
	public void setState(HttpServletRequest request);
	public void removeState(HttpServletRequest request);
	public boolean checkState(HttpServletRequest request, HttpServletResponse response);

	public String getToken(HttpServletRequest request);
	public void setToken(HttpServletRequest request, String token);
	public void removeToken(HttpServletRequest request);

	public <T extends GoogleTokenResponse> T parseGoogleToken(String token) throws IOException;
	public <T extends GoogleCredential, U extends GoogleTokenResponse> T getGoogleCredential(U tokenResponse);
	public <T extends GoogleCredential> void checkGoogleTokenValidity(T credential, String userId) throws IOException;
}
