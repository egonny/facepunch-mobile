package com.egonny.facepunch.fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.egonny.facepunch.FPApplication;
import com.egonny.facepunch.R;
import com.egonny.facepunch.adapters.ThreadAdapter;
import com.egonny.facepunch.model.facepunch.FPPost;
import com.egonny.facepunch.model.facepunch.FPThread;
import com.egonny.facepunch.util.FPParser;
import com.egonny.facepunch.util.ImageLoaderHelper;

import java.util.ArrayList;
import java.util.List;

public class ThreadFragment extends ListFragment {

	private ThreadAdapter mAdapter;

	private FPThread mThread;
	private int mTopPage;
	private int mBottomPage;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (mAdapter == null) {
			setAdapter(new ThreadAdapter(getActivity()));
		}
		return inflater.inflate(R.layout.fragment_thread, container, false);
	}

	@Override
	public void onStart() {
		super.onStart();
		ListView listView = getListView();
		if (listView == null) return;
		listView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView listView, int scrollState) {
				// Pause disk cache access to ensure smoother scrolling
				RequestQueue queue = FPApplication.getInstance().getRequestQueue();
				if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
					queue.stop();
				} else {
					queue.start();
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
			}
		});
	}

	public void setAdapter(ThreadAdapter adapter) {
		mAdapter = adapter;
		setListAdapter(adapter);
	}

	public FPThread getThread() {
		return mThread;
	}

	public void load(FPThread thread) {
		load(thread, 1);
	}

	public void load(FPThread thread, int page) {
		mThread = thread;
		mTopPage = page;
		mBottomPage = page;
		mAdapter.clear();
		getPosts(page, new ThreadCallback() {
			@Override
			public void onResult(boolean success, List<FPPost> posts) {
				if (success) {
					mAdapter.addAll(posts);
					mAdapter.notifyDataSetChanged();
				}
			}
		});
	}

	public void reload(int page) {
		load(mThread, page);
	}

	public void reload() {
		load(mThread, mTopPage);
	}

	private void loadNext() {
		getPosts(++mBottomPage, new ThreadCallback() {
			@Override
			public void onResult(boolean success, List<FPPost> posts) {
				if (success) {
					mAdapter.addAll(posts);
					mAdapter.notifyDataSetChanged();
				}
			}
		});
	}

	private void loadPrevious() {
		//TODO implement this later
	}

	public int getCurrentPage() {
		return mTopPage;
	}

	private interface ThreadCallback {
		void onResult (boolean success, List<FPPost> posts);
	}

	private void getPosts(int page, final ThreadCallback callback) {
		FPApplication.getInstance().addToRequestQueue(
				new StringRequest("http://facepunch.com/showthread.php?t=" + mThread.getId() + "&page=" + page,
						new Response.Listener<String>() {
							@Override
							public void onResponse(String s) {
								callback.onResult(true, FPParser.parsePosts(s));
							}
						},
						new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError volleyError) {
								callback.onResult(true, new ArrayList<FPPost>());
							}
						}
				));
	}
}