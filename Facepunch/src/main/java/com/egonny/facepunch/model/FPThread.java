package com.egonny.facepunch.model;

public class FPThread {

	private final String title;
	private final long id;
	private final User author;
	private boolean locked;
	private boolean old;
	private boolean sticky;
	private int pages;
	private int reading;
	private User lastPostAuthor;
	private String lastPostDate;
	private int newPosts;
	private String description;

	public FPThread(String title, long id, User author) {
		this.title = title;
		this.id = id;
		this.author = author;
	}

	public long getId() {
		return id;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public boolean isOld() {
		return old;
	}

	public void setOld(boolean old) {
		this.old = old;
	}

	public boolean isSticky() {
		return sticky;
	}

	public void setSticky(boolean sticky) {
		this.sticky = sticky;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public int getReading() {
		return reading;
	}

	public void setReading(int reading) {
		this.reading = reading;
	}

	public User getLastPostAuthor() {
		return lastPostAuthor;
	}

	public void setLastPostAuthor(User lastPostAuthor) {
		this.lastPostAuthor = lastPostAuthor;
	}

	public String getLastPostDate() {
		return lastPostDate;
	}

	public void setLastPostDate(String lastPostDate) {
		this.lastPostDate = lastPostDate;
	}

	public int getNewPosts() {
		return newPosts;
	}

	public void setNewPosts(int newPosts) {
		this.newPosts = newPosts;
	}

	@Override
	public String toString() {
		return "Thread: " + title + " by " + author.toString() + ", id = " + id;
	}
}
