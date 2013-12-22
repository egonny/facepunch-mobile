package com.egonny.facepunch.model.facepunch;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class FPPost {

	private final long id;
	private final User author;
	private final String postTime;
	private String message;
	private boolean old;
	private TreeMap<String, Integer> ratings;
	private Map<String, String> ratingKeys;

	public FPPost(long id, User author, String postTime) {
		this.id = id;
		this.author = author;
		this.postTime = postTime;
	}

	public long getId() {
		return id;
	}

	public User getAuthor() {
		return author;
	}

	public String getPostTime() {
		return postTime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isOld() {
		return old;
	}

	public void setOld(boolean old) {
		this.old = old;
	}

	public boolean hasRatings() {
		return ratings != null;
	}

	public Map<String, Integer> getRatings() {
		return ratings;
	}

	public void setRatings(Map<String, Integer> ratings) {
		this.ratings = new TreeMap<String, Integer>(new RatingComparator(ratings));
		this.ratings.putAll(ratings);
	}

	public Iterator<String> getOrderedRatings() {
		return ratings.descendingKeySet().descendingIterator();
	}

	public int getRatingAmountOf(String rating) {
		return ratings.get(rating);
	}

	public void setRatingKey(String rating, String key) {
		ratingKeys.put(rating, key);
	}

	public String getKeyOfRating(String rating) {
		return ratingKeys.get(rating);
	}

	private class RatingComparator<String> implements Comparator<String> {

		Map map;

		private RatingComparator(Map map) {
			this.map = map;
		}

		@Override
		public int compare(String s1, String s2) {
			return ((Integer) map.get(s2)).compareTo((Integer) map.get(s1));
		}
	}
}
