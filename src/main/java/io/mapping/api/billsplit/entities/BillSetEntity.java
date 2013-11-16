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
import java.util.Set;
import java.util.UUID;

/**
 * A set of bills, the users of those bills, and the delegates of those bills.
 */

@Entity
@NamedQueries({
		@NamedQuery(name = "billSetEntity.findById", query = "from BillSetEntity bse left outer join bse.bills b where bse.id = :id and b.settlement is null"),
		@NamedQuery(name = "billSetEntity.findByUserId", query = "select bse from BillSetEntity bse join bse.users u left outer join bse.bills b where u.id = :userId and b.settlement is null"),
})
public class BillSetEntity {
	private UUID mId;
	private String name;
	private Set<BillEntity> mBills;
	private Set<UserEntity> mUsers;
	private Set<BillDelegateEntity> mBillDelegates;

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

	@Column(nullable = false, length = 64)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	public Set<BillEntity> getBills() {
		return mBills;
	}
	public void setBills(Set<BillEntity> bills) {
		mBills = bills;
	}

	@ManyToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH }, fetch = FetchType.EAGER)
	public Set<UserEntity> getUsers() {
		return mUsers;
	}
	public void setUsers(Set<UserEntity> users) {
		mUsers = users;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	public Set<BillDelegateEntity> getBillDelegates() {
		return mBillDelegates;
	}
	public void setBillDelegates(Set<BillDelegateEntity> billDelegates) {
		mBillDelegates = billDelegates;
	}
}
