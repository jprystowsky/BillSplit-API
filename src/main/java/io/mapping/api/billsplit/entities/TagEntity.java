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
public class TagEntity {
	private UUID mID;
	private String mName;
	private TagEntity mParent;
	private Collection<TagEntity> mChildren;

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

	@Column(nullable = false, length = 32)
	public String getName() {
		return mName;
	}
	public void setName(String name) {
		mName = name;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	public TagEntity getParent() {
		return mParent;
	}
	public void setParent(TagEntity parent) {
		mParent = parent;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "parent", orphanRemoval = true)
	public Collection<TagEntity> getChildren() {
		return mChildren;
	}
	public void setChildren(Collection<TagEntity> children) {
		mChildren = children;
	}
}
