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
import java.util.Collection;
import java.util.UUID;

/**
 * A set of bills, the users of those bills, and the delegates of those bills.
 */

@Entity
@NamedQueries({
		@NamedQuery(name = "billSetEntity.findById", query = "from BillSetEntity where id = :id"),
		@NamedQuery(name = "billSetEntity.findByUserId", query = "from BillSetEntity bse join bse.users u where u.id = :id")
})
public class BillSetEntity {
	private UUID mId;
	private String name;
	private Collection<BillEntity> mBills;
	private Collection<UserEntity> mUsers;
	private Collection<BillDelegateEntity> mBillDelegates;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(unique = true, nullable = false)
	public UUID getId() {
		return mId;
	}
	public void setId(UUID id) {
		mId = id;
	}

	@Column(nullable = false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	public Collection<BillEntity> getBills() {
		return mBills;
	}
	public void setBills(Collection<BillEntity> bills) {
		mBills = bills;
	}

	@OneToMany(fetch = FetchType.LAZY)
	public Collection<UserEntity> getUsers() {
		return mUsers;
	}
	public void setUsers(Collection<UserEntity> users) {
		mUsers = users;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	public Collection<BillDelegateEntity> getBillDelegates() {
		return mBillDelegates;
	}
	public void setBillDelegates(Collection<BillDelegateEntity> billDelegates) {
		mBillDelegates = billDelegates;
	}
}
