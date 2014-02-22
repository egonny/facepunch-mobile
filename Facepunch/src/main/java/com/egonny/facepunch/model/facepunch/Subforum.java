package com.egonny.facepunch.model.facepunch;

public class Subforum{

	private String title;
	private int id;
	private Category category;
	private int pages;

	public Subforum(String title, int id) {
		this.title = title;
		this.id = id;
		pages = 1;
	}

	public String getTitle() {
		return title;
	}

	public int getId() {
		return id;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	@Override
	public String toString() {
		return "Subforum: " + title + ", id=" + id;
	}
}
