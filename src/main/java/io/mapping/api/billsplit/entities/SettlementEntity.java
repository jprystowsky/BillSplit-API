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
import java.util.Date;
import java.util.UUID;

/**
 * The settlement, i.e., resolution, of a set of bills.
 */

@Entity
public class SettlementEntity {
	private UUID mID;
	private Date mDate;
	private String mComments;
	private Collection<BillEntity> mBills;

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

	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	public Date getDate() {
		return mDate;
	}
	public void setDate(Date date) {
		mDate = date;
	}

	public String getComments() {
		return mComments;
	}
	public void setComments(String comments) {
		mComments = comments;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "settlement")
	public Collection<BillEntity> getBills() {
		return mBills;
	}
	public void setBills(Collection<BillEntity> bills) {
		mBills = bills;
	}
}
