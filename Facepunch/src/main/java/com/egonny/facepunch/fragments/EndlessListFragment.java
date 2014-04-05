package com.egonny.facepunch.fragments;

import android.app.ListFragment;
import android.widget.AbsListView;

public abstract class EndlessListFragment extends ListFragment implements AbsListView.OnScrollListener {

	private int mTopPage;
	private int mBottomPage;

	public final static int PAGE_LOAD_THRESHOLD = 5;

	@Override
	public void onScrollStateChanged(AbsListView listView, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE) {
			if (listView.getLastVisiblePosition() >= listView.getCount() - PAGE_LOAD_THRESHOLD) {
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// Must be empty
	}
}
