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
 * A biller -- someone who issues a bill to be paid.
 */

@Entity
public class BillerEntity {
	private UUID mID;
	private String mName;
	private Set<BillEntity> mBills;
	private ContactEntity mContactEntity;

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

	@Column(nullable = false, length = 64, unique = true)
	public String getName() {
		return mName;
	}
	public void setName(String name) {
		mName = name;
	}

	@OneToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH }, fetch = FetchType.EAGER, mappedBy = "biller")
	public Set<BillEntity> getBills() {
		return mBills;
	}
	public void setBills(Set<BillEntity> bills) {
		mBills = bills;
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	public ContactEntity getContactEntity() {
		return mContactEntity;
	}
	public void setContactEntity(ContactEntity contactEntity) {
		mContactEntity = contactEntity;
	}
}
