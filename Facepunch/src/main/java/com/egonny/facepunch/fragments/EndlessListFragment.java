package com.egonny.facepunch.fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import com.egonny.facepunch.R;
import com.egonny.facepunch.adapters.PagedAdapter;

import java.util.List;

public abstract class EndlessListFragment<T> extends ListFragment implements AbsListView.OnScrollListener {

	protected PagedAdapter<T> mAdapter;
	protected int mPageCount = -1;

	//TODO: Remove LinearLayouts from headers/footers, use <merge> instead?
	/**
	 * The view that will be used to show the progressbar as header/footer
	 */
	protected LinearLayout mProgressFooter;

	/**
	 * Gives the position that is currently loading. Returns null if not loading.
	 */
	protected Position mLoading;

	/**
	 * The view that will be used to show errors as header/footer
	 */
	protected LinearLayout mErrorFooter;

	/**
	 * Gives the position that shows an error. Returns null if not loading.
	 */
	protected Position mError;

	/**
	 * The view that will be shown as header and, when pressed, will load a previous page.
	 */
	protected LinearLayout mHeaderButton;

	public static final int BOTTOM_PAGE_LOAD_THRESHOLD = 5;

	public void onStart() {
		super.onStart();
		if (mAdapter == null) {
			// Required to add a footer before the adapter gets set, so Android doesn't freak out.
			mAdapter = getAdapter();
			setListAdapter(mAdapter);
		} else {
			// I assume this is in case the fragment is retained, so we need to recreate the footers
			setListAdapter(mAdapter);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Load main view
		View root = inflater.inflate(R.layout.fragment_endless_layout, null);
		// Load headers and footers (to be added dynamically)
		mProgressFooter = (LinearLayout) inflater.inflate(R.layout.endless_list_progress, null);
		mErrorFooter = (LinearLayout) inflater.inflate(R.layout.endless_list_error, null);
		return root;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		setListAdapter(null);
	}

	/**
	 * This function should return the adapter needed for this ListFragment to run.
	 */
	protected abstract PagedAdapter<T> getAdapter();

	/**
	 * Returns the amount of pages this list can at most contain.
	 * @return The amount of pages this list can contain. Returns -1 if there is no limit.
	 */
	public int getPageCount() {
		return mPageCount;
	}

	/**
	 * Sets the amount of pages this list can at most contain.
	 * @param pageCount
	 *        The amount of pages this list can contain.
	 */
	public void setPageCount(int pageCount) throws IllegalArgumentException {
		if (pageCount <= 0) throw new IllegalArgumentException("Invalid pagecount");
		this.mPageCount = pageCount;
	}

	/**
	 * This function gets called whenever a page needs to be loaded.
	 * Once the page is prepared, return it using {@link #setPage(java.util.List, int)}.
	 * If there was an error, use the method {@link #setError(String error)}.
	 *
	 * @param page
	 *        The page that needs to be retrieved.
	 */
	protected abstract void getPage(int page);

	/**
	 * Adds a certain page to the list.
	 * If the page does not precede or succeed a page in the adapter and the adapter is not empty,
	 * then the page will not be added (to ensure the are no gaps in the pages).
	 *
	 * @param items
	 *        The items to be added to the list.
	 * @param page
	 *        The page of the items.
	 */
	protected void setPage(List<T> items, int page) {
		if (mAdapter.getCount() == 0) {
			mAdapter.load(items, page);
		} else if (mAdapter.getFirstPage() - 1 == page) {
			mAdapter.addPreviousPage(items);
		} else if (mAdapter.getLastPage() + 1 == page) {
			mAdapter.addNextPage(items);
		} else return;
		setLoading(false, null);
	}

	public void load(int page) {
		if (page < 1) throw new IllegalArgumentException("Invalid page");
		mAdapter.clear();
		setLoading(true, Position.GENERAL);
		getPage(page);
	}

	/**
	 * Loads the page after the last one currently loaded.
	 */
	protected void loadNext() {
		setLoading(true, Position.FOOTER);
		getPage(mAdapter.getLastPage() + 1);
	}

	/**
	 * Loads the page before the first one currently loaded.
	 */
	protected void loadPrevious() {
		setLoading(true, Position.HEADER);
		getPage(mAdapter.getFirstPage() - 1);
	}

	public boolean isLoading() {
		return mLoading != null;
	}

	protected void setLoading(boolean loading, Position position) {

	}

	protected void setError(String error) {

	}

	protected void setError(int resId) {

	}

	@Override
	public void onScrollStateChanged(AbsListView listView, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE) {
			if (listView.getLastVisiblePosition() >= listView.getCount() - BOTTOM_PAGE_LOAD_THRESHOLD) {
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// Must be empty
	}

	protected enum Position {
		HEADER,
		FOOTER,
		GENERAL
	}
}
