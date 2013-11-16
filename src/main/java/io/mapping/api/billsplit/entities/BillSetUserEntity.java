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
import java.util.UUID;

@Entity
@NamedQueries({
		@NamedQuery(name = "billSetUserEntity.findByUserId", query = "from BillSetUserEntity where user.id = :id")
})
public class BillSetUserEntity {
	private UUID mId;
	private BillSetEntity mBillSet;
	private UserEntity mUser;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(unique = true)
	public UUID getId() {
		return mId;
	}
	public void setId(UUID id) {
		mId = id;
	}

	@ManyToOne(optional = false)
	public BillSetEntity getBillSet() {
		return mBillSet;
	}
	public void setBillSet(BillSetEntity billSet) {
		mBillSet = billSet;
	}

	@ManyToOne(optional = false)
	public UserEntity getUser() {
		return mUser;
	}
	public void setUser(UserEntity user) {
		mUser = user;
	}

	public static class Builder {
		private final BillSetUserEntity mBillSetUserEntity;

		public Builder() {
			mBillSetUserEntity = new BillSetUserEntity();
		}

		public Builder billSet(BillSetEntity billSet) {
			mBillSetUserEntity.setBillSet(billSet);

			return this;
		}

		public Builder user(UserEntity user) {
			mBillSetUserEntity.setUser(user);

			return this;
		}

		public BillSetUserEntity build() {
			return mBillSetUserEntity;
		}
	}
}
