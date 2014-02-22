package com.egonny.facepunch.fragments;

import android.app.ListFragment;
import android.widget.AbsListView;

public abstract class EndlessListFragment extends ListFragment implements AbsListView.OnScrollListener {


	@Override
	public void onScrollStateChanged(AbsListView listView, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE) {
			if (listView.getLastVisiblePosition() >= listView.getCount() - 5) {
				loadNext();
			}
		}
	}

	private void loadNext() {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// Must be empty
	}
}
