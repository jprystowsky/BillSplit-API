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

package io.mapping.api.billsplit.oauth2;

import com.google.inject.Inject;

import java.math.BigInteger;
import java.security.SecureRandom;

public class BigIntegerStateGenerator implements StateGenerator {
	@Inject
	SecureRandom mSecureRandom;

	private static final int STATE_SIZE = 32;
	private static final int STATE_INIT = 130;

	@Override
	public String generateState() {
		return new BigInteger(STATE_INIT, mSecureRandom).toString(STATE_SIZE);
	}
}
