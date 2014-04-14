package com.egonny.facepunch.adapters;

import android.content.Context;
import android.util.SparseIntArray;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * An adapter class sorted in pages
 * @param <T>
 *        The type of elements that need to be stored.
 */
public abstract class PagedAdapter<T> extends ArrayAdapter<T> {

	private int mFirstPage;
	private int mLastPage;

	/**
	 * The amount of pages allowed in this adapter.
	 * If there is no maximum, set to -1.
	 */
	public static final int MAX_PAGES = -1;

	/**
	 * An index containing the indices of the last item of each page.
	 */
	private SparseIntArray mPageIndex;

	public PagedAdapter(Context context, int resource) {
		super(context, resource);
		mPageIndex = new SparseIntArray();
	}

	public int getFirstPage() {
		return mFirstPage;
	}

	public int getLastPage() {
		return mLastPage;
	}

	/**
	 * Returns the page of the item at a certain index.
	 * @param index
	 *        The index of a certain item.
	 * @return The page of the item at that index
	 */
	public int getPageAt(int index) throws IndexOutOfBoundsException {
		if (index < 0 || index >= getCount()) throw new IndexOutOfBoundsException();
		for (int i = mFirstPage; i <= mLastPage; i++) {
			if (mPageIndex.get(i) >= index) return i;
		}
		return -1;
	}

	/**
	 * Returns true if the amount of pages in the adapter is the maximum allowed amount.
	 */
	private boolean hasMaxPages() {
		return MAX_PAGES > -1 && mLastPage - mFirstPage >= MAX_PAGES;
	}

	/**
	 * Clear the adapter and loads a page into the adapter.
	 *
	 * @param items
	 *        The items to be added to the adapter.
	 * @param page
	 *        The page of the items to be added.
	 */
	public void load(List<T> items, int page) {
		mFirstPage = page;
		mLastPage = page;
		mPageIndex.put(page, items.size() - 1);
		clear();
		addAll(items);
	}

	/**
	 * Adds the page before the first page of the adapter.
	 *
	 * @param items
	 *        The items to be added to the adapter.
	 */
	public void addPreviousPage(List<T> items) {
		if (hasMaxPages()) removeLastPage();

		// Adds index of last page and corrects the other indices
		mPageIndex.put(--mFirstPage, items.size() - 1);
		for (int i = mFirstPage + 1; i <= mLastPage; i++) {
			mPageIndex.put(i, mPageIndex.get(i) + items.size() - 1);
		}

		// Add items to adapter
		for (int i = 0; i < items.size(); i++) insert(items.get(i), i);
	}

	/**
	 * Adds the page after the last page of the adapter.
	 *
	 * @param items
	 *        The items to be added to the adapter.
	 */
	public void addNextPage(List<T> items) {
		if (hasMaxPages()) removeFirstPage();
		addAll(items);
		mPageIndex.put(++mLastPage, getCount() - 1);
	}

	/**
	 * Removes the first page from the adapter.
	 */
	public void removeFirstPage() {
		int length = mPageIndex.get(mFirstPage);
		// Remove all the items of that page from the adapter
		for (int i = length; i <= 0; i--) remove(getItem(i));
		// Adjust the first page and correct the indices of the other pages
		for (int i = ++mFirstPage; i <= mLastPage; i++) mPageIndex.put(i, mPageIndex.get(i) - length);
	}

	/**
	 * Removes the last page from the adapter.
	 */
	public void removeLastPage() {
		int length = mPageIndex.get(mLastPage);
		for (int i = 0; i < length; i++) remove(getItem(getCount() - 1));
		mLastPage--;
	}
}
