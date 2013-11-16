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
import java.util.Date;
import java.util.Set;
import java.util.UUID;

/**
 * A single bill.
 */

@Entity
public class BillEntity {
	private UUID mID;
	private BillerEntity mBiller;
	private Date mDate;
	private BigDecimal mAmount;
	private Set<TagEntity> mTags;
	private CategoryEntity mCategory;
	private String mNotes;
	private SettlementEntity mSettlement;

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

	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH }, optional = false)
	public BillerEntity getBiller() {
		return mBiller;
	}
	public void setBiller(BillerEntity biller) {
		mBiller = biller;
	}

	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	public Date getDate() {
		return mDate;
	}
	public void setDate(Date date) {
		mDate = date;
	}

	@Column(nullable = false, precision = 15, scale = 2)
	public BigDecimal getAmount() {
		return mAmount;
	}
	public void setAmount(BigDecimal amount) {
		mAmount = amount;
	}

	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	public Set<TagEntity> getTags() {
		return mTags;
	}
	public void setTags(Set<TagEntity> tags) {
		mTags = tags;
	}

	@ManyToOne(optional = false, cascade = { CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH }, fetch = FetchType.EAGER)
	public CategoryEntity getCategory() {
		return mCategory;
	}
	public void setCategory(CategoryEntity category) {
		mCategory = category;
	}

	public String getNotes() {
		return mNotes;
	}
	public void setNotes(String notes) {
		mNotes = notes;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	public SettlementEntity getSettlement() {
		return mSettlement;
	}
	public void setSettlement(SettlementEntity settlement) {
		mSettlement = settlement;
	}
}
