package io.mapping.api.billsplit.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "\"test\"")
@NamedQueries({
		@NamedQuery(name = "test.findById", query = "from Test where id = :id")
})
public class Test {
	private Long mId;
	private String mName;

	@Id
	public Long getId() {
		return mId;
	}

	public void setId(Long id) {
		mId = id;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}
}
