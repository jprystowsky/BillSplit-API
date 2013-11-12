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

package io.mapping.api.billsplit.sessions;

import com.google.inject.Inject;
import io.mapping.api.billsplit.reflection.PackageNameProvider;

/**
 * A big bag of strings, used for consistency in {@link javax.servlet.http.HttpSession} access.
 */
public class SessionAttributesImpl implements SessionAttributes {
	private final String mPrefix;

	@Inject
	public SessionAttributesImpl(PackageNameProvider packageNameProvider) {
		mPrefix = packageNameProvider.getPackageName(this.getClass()) + ".";
	}

	@Override
	public String getAttribute(Attribute attribute) {
		switch (attribute) {
			case STATE:
				return mPrefix + "STATE";
			case TOKEN:
				return mPrefix + "TOKEN";
			case USER:
				return mPrefix + "USER";
		}
		return null;
	}
}
