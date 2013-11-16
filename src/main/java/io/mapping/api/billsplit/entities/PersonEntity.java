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

@Entity
public class PersonEntity {
	private UUID mID;
	private String mName;
	private String mRole;
	private String mNotes;
	private Collection<ContactEntity> mContactEntities;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(unique = true)
	public UUID getID() {
		return mID;
	}
	public void setID(UUID ID) {
		mID = ID;
	}

	@Column(nullable = false)
	public String getName() {
		return mName;
	}
	public void setName(String name) {
		mName = name;
	}

	public String getRole() {
		return mRole;
	}
	public void setRole(String role) {
		mRole = role;
	}

	public String getNotes() {
		return mNotes;
	}
	public void setNotes(String notes) {
		mNotes = notes;
	}

	@OneToMany(cascade = CascadeType.REMOVE)
	public Collection<ContactEntity> getContactEntities() {
		return mContactEntities;
	}
	public void setContactEntities(Collection<ContactEntity> contactEntities) {
		mContactEntities = contactEntities;
	}
}
