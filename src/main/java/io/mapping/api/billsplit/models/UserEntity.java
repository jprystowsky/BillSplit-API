package io.mapping.api.billsplit.models;

import javax.persistence.*;

@Entity
@Table(name = "\"user\"")
@NamedQueries({
		@NamedQuery(name = "user.findById", query = "from UserEntity where id = :id"),
		@NamedQuery(name = "user.findByGoogleId", query = "from UserEntity where googleId = :googleId")
})
public class UserEntity {
	private long mId;
	private String mGoogleId;
	private String mEmail;
	private String mFirstName;
	private String mLastName;

	@Id
	@SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "user_id_seq")
	public long getId() {
		return mId;
	}
	public void setId(long id) {
		mId = id;
	}

	@Column(name = "google_id")
	public String getGoogleId() {
		return mGoogleId;
	}
	public void setGoogleId(String googleId) {
		mGoogleId = googleId;
	}

	@Column
	public String getEmail() {
		return mEmail;
	}
	public void setEmail(String email) {
		mEmail = email;
	}

	@Column(name = "first_name")
	public String getFirstName() {
		return mFirstName;
	}
	public void setFirstName(String firstName) {
		mFirstName = firstName;
	}

	@Column(name = "last_name")
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
