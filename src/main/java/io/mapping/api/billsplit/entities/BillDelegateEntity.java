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

package io.mapping.api.billsplit.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * The delegate of a bill -- a user, along with an amount of responsibility.
 */

@Entity
public class BillDelegateEntity {
	private UUID mID;
	private UserEntity mUser;
	private BigDecimal mAmount;
	private String mComment;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(unique = true, nullable = false)
	public UUID getID() {
		return mID;
	}
	public void setID(UUID ID) {
		mID = ID;
	}

	@ManyToOne(optional = false, cascade = { CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH }, fetch = FetchType.EAGER)
	public UserEntity getUser() {
		return mUser;
	}
	public void setUser(UserEntity user) {
		mUser = user;
	}

	@Column(nullable = false, precision = 12, scale = 2)
	public BigDecimal getAmount() {
		return mAmount;
	}
	public void setAmount(BigDecimal amount) {
		mAmount = amount;
	}

	public String getComment() {
		return mComment;
	}
	public void setComment(String comment) {
		mComment = comment;
	}
}
