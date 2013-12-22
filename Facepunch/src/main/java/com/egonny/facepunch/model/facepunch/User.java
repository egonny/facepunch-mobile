package com.egonny.facepunch.model.facepunch;

import android.os.Parcel;
import android.os.Parcelable;
import com.egonny.facepunch.R;

public class User implements Parcelable {

	public enum UserGroup {
		REGULAR(-1), GOLD(R.color.gold_user), MOD(R.color.mod_user), BANNED(R.color.FP_red);

		int color;

		UserGroup(int color) {
			this.color = color;
		}

		public int getColor() {
			return color;
		}
	}

	private final String name;
	private final long id;
	private String joinDate;
	private int postcount;
	private UserGroup userGroup = UserGroup.REGULAR;

	public User(String name, long id) {
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public long getId() {
		return id;
	}

	public UserGroup getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(UserGroup userGroup) {
		this.userGroup = userGroup;
	}

	public void setUserGroup(String color) {
		if (color.equals("#A06000")) {
			setUserGroup(UserGroup.GOLD);
		} else if (color.equals("#00aa00")) {
			setUserGroup(UserGroup.MOD);
		} else if (color.equals("red")) {
			setUserGroup(UserGroup.BANNED);
		}
	}

	public String getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}

	public int getPostcount() {
		return postcount;
	}

	public void setPostcount(int postcount) {
		this.postcount = postcount;
	}

	@Override
	public String toString() {
		return name;
	}

	/* Parcel interface */

	public static final Parcelable.Creator<User> CREATOR
			= new Parcelable.Creator<User>() {
		public User createFromParcel(Parcel in) {
			return new User(in);
		}

		public User[] newArray(int size) {
			return new User[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeLong(id);
		dest.writeString(joinDate);
		dest.writeInt(postcount);
		dest.writeSerializable(userGroup);
	}

	public User(Parcel in) {
		name = in.readString();
		id = in.readLong();
		setJoinDate(in.readString());
		setPostcount(in.readInt());
		setUserGroup((UserGroup) in.readSerializable());
	}
}
