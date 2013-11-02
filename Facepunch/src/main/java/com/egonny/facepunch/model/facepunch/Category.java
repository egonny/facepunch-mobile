package com.egonny.facepunch.model.facepunch;

import com.egonny.facepunch.model.menu.MenuListHeader;

import java.util.ArrayList;
import java.util.List;

public class Category extends MenuListHeader {

	private String name;
	private List<Subforum> subforums;

	public Category(String name) {
		super(name);
		this.name = name;
		this.subforums = new ArrayList<Subforum>();
	}

	public void addSubforum(Subforum subforum) {
		subforums.add(subforum);
		subforum.setCategory(this);
	}

	public void removeSubforum(Subforum subforum) {
		subforums.remove(subforum);
		subforum.setCategory(null);
	}

	public List<Subforum> getSubforums() {
		return new ArrayList<Subforum>(subforums);
	}
}
