package com.egonny.facepunch.fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.egonny.facepunch.FPApplication;
import com.egonny.facepunch.R;
import com.egonny.facepunch.adapters.SubforumAdapter;
import com.egonny.facepunch.model.facepunch.FPPost;
import com.egonny.facepunch.model.facepunch.FPThread;
import com.egonny.facepunch.model.facepunch.Subforum;
import com.egonny.facepunch.model.menu.MenuListItem;
import com.egonny.facepunch.util.FPParser;

import java.util.ArrayList;
import java.util.List;

public class SubforumFragment extends ListFragment implements AbsListView.OnScrollListener {

	private SubforumAdapter mAdapter;
	private onItemClickListener mListener;

	private Subforum mSubforum;
	private int mCurrentPage;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_subforum, container, false);
	}

	public void onStart() {
		super.onStart();
		if (mAdapter == null) {
			setAdapter(new SubforumAdapter(getActivity()));
		}
		getListView().setOnScrollListener(this);
	}

	public void setAdapter(SubforumAdapter adapter) {
		mAdapter = adapter;
		setListAdapter(adapter);
	}

	@Override
	public void onScrollStateChanged(AbsListView listView, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE) {
			if (listView.getLastVisiblePosition() >= listView.getCount() - 5) {
				loadNext();
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// Must be empty
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		FPThread item = (FPThread) l.getItemAtPosition(position);
		if (mListener != null) {
			mListener.onThreadClick(item);
		}
	}

	public interface onItemClickListener {
		void onThreadClick(FPThread thread);
	}

	public void setItemClickListener(onItemClickListener listener) {
		mListener = listener;
	}

	public void load(Subforum subforum) {
		mSubforum = subforum;
		mCurrentPage = 1;
		mAdapter.clear();
		getThreads(1, new SubforumCallback() {
			@Override
			public void onResult(boolean success, List<FPThread> threads) {
				if (success) {
					mAdapter.addAll(threads);
					mAdapter.notifyDataSetChanged();
				}
			}
		});
	}

	private void loadNext() {
		getThreads(++mCurrentPage, new SubforumCallback() {
			@Override
			public void onResult(boolean success, List<FPThread> threads) {
				if (success) {
					mAdapter.addAll(threads);
					mAdapter.notifyDataSetChanged();
				}
			}
		});
	}

	private interface SubforumCallback {
		void onResult(boolean success, List<FPThread> threads);
	}

	private void getThreads(int page, final SubforumCallback callback) {
		FPApplication.getInstance().addToRequestQueue(
			new StringRequest("http://facepunch.com/forumdisplay.php?f=" + mSubforum.getId() + "&page=" + page,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String s) {
						callback.onResult(true, FPParser.parseThreads(s));
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						callback.onResult(true, new ArrayList<FPThread>());
					}
				}
		));
	}
}