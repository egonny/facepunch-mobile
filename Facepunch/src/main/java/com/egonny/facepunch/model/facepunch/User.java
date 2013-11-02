package com.egonny.facepunch.model.facepunch;

public class User {
	public enum UserGroup {REGULAR, GOLD, MOD, BANNED}

	private final String name;
	private final long id;
	private String joinDate;
	private int postcount;
	private UserGroup userGroup = UserGroup.REGULAR;

	public User(String name, long id) {
		this.name = name;
		this.id = id;
	}

	public UserGroup getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(UserGroup userGroup) {
		this.userGroup = userGroup;
	}

	public void setUserGroup(String color) {

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
}
