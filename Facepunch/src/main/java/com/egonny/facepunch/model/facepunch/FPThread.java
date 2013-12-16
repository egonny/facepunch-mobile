package com.egonny.facepunch.model.facepunch;

import android.os.Parcel;
import android.os.Parcelable;

public class FPThread implements Parcelable {

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

	public String getTitle() {
		return title;
	}

	public User getAuthor() {
		return author;
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

	/* Parcel interface */

	public static final Parcelable.Creator<FPThread> CREATOR
			= new Parcelable.Creator<FPThread>() {
		public FPThread createFromParcel(Parcel in) {
			return new FPThread(in);
		}

		public FPThread[] newArray(int size) {
			return new FPThread[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(title);
		dest.writeLong(id);
		dest.writeParcelable(author, 0);
		dest.writeByte((byte) (locked ? 1 : 0));
		dest.writeByte((byte) (old ? 1 : 0));
		dest.writeByte((byte) (sticky ? 1 : 0));
		dest.writeInt(pages);
		dest.writeInt(reading);
		dest.writeParcelable(lastPostAuthor, 0);
		dest.writeString(lastPostDate);
		dest.writeInt(newPosts);
		dest.writeString(description);
	}

	public FPThread(Parcel in) {
		title = in.readString();
		id = in.readLong();
		author = in.readParcelable(User.class.getClassLoader());
		setLocked(in.readByte() != 0);
		setOld(in.readByte() != 0);
		setSticky(in.readByte() != 0);
		setPages(in.readInt());
		setReading(in.readInt());
		setLastPostAuthor((User) in.readParcelable(User.class.getClassLoader()));
		setLastPostDate(in.readString());
		setNewPosts(in.readInt());
		description = in.readString();
	}
}
