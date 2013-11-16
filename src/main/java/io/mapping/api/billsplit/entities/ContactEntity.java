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
 * A set of contact information.
 */

@Entity
public class ContactEntity {
	private UUID mID;

	private Collection<AddressLineEntity> mAddressLines;
	private String mCity;
	private String mState;
	private String mZipCode;

	private String mPhoneNumber;

	private String mWebsite;

	private Collection<ContactPersonEntity> mPersons;

	private ContactTypeEntity mContactType;

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

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	public Collection<AddressLineEntity> getAddressLines() {
		return mAddressLines;
	}
	public void setAddressLines(Collection<AddressLineEntity> addressLines) {
		mAddressLines = addressLines;
	}

	@Column(length = 32)
	public String getCity() {
		return mCity;
	}
	public void setCity(String city) {
		mCity = city;
	}

	@Column(length = 16)
	public String getState() {
		return mState;
	}
	public void setState(String state) {
		mState = state;
	}

	@Column(length = 10)
	public String getZipCode() {
		return mZipCode;
	}
	public void setZipCode(String zipCode) {
		mZipCode = zipCode;
	}

	@Column(length = 24)
	public String getPhoneNumber() {
		return mPhoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		mPhoneNumber = phoneNumber;
	}

	@Column(length = 128)
	public String getWebsite() {
		return mWebsite;
	}
	public void setWebsite(String website) {
		mWebsite = website;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	public Collection<ContactPersonEntity> getPersons() {
		return mPersons;
	}
	public void setPersons(Collection<ContactPersonEntity> persons) {
		mPersons = persons;
	}

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	public ContactTypeEntity getContactType() {
		return mContactType;
	}
	public void setContactType(ContactTypeEntity contactType) {
		mContactType = contactType;
	}
}
