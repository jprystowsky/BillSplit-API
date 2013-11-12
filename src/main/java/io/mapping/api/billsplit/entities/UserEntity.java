package io.mapping.api.billsplit.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@NamedQueries({
		@NamedQuery(name = "user.findById", query = "from UserEntity where id = :id"),
		@NamedQuery(name = "user.findByGoogleId", query = "from UserEntity where googleId = :googleId")
})
public class UserEntity {
	private UUID mId;
	private String mGoogleId;
	private String mEmail;
	private String mFirstName;
	private String mLastName;

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

	@Column(nullable = false)
	public String getGoogleId() {
		return mGoogleId;
	}
	public void setGoogleId(String googleId) {
		mGoogleId = googleId;
	}

	@Column(nullable = false)
	public String getEmail() {
		return mEmail;
	}
	public void setEmail(String email) {
		mEmail = email;
	}

	@Column(nullable = false)
	public String getFirstName() {
		return mFirstName;
	}
	public void setFirstName(String firstName) {
		mFirstName = firstName;
	}

	@Column(nullable = false)
	public String getLastName() {
		return mLastName;
	}
	public void setLastName(String lastName) {
		mLastName = lastName;
	}

	public static class Builder {
		private UserEntity mUserEntity;

		public Builder() {
			mUserEntity = new UserEntity();
		}

		public Builder googleId(String googleId) {
			mUserEntity.mGoogleId = googleId;
			return this;
		}

		public Builder email(String email) {
			mUserEntity.mEmail = email;
			return this;
		}

		public Builder firstName(String firstName) {
			mUserEntity.mFirstName = firstName;
			return this;
		}

		public Builder lastName(String lastName) {
			mUserEntity.mLastName = lastName;
			return this;
		}

		public UserEntity build() {
			return mUserEntity;
		}
	}
}
