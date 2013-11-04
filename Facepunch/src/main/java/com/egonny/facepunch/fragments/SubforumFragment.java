package com.egonny.facepunch.fragments;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.egonny.facepunch.FPApplication;
import com.egonny.facepunch.R;
import com.egonny.facepunch.adapters.MenuAdapter;
import com.egonny.facepunch.adapters.SubforumAdapter;
import com.egonny.facepunch.model.facepunch.FPThread;
import com.egonny.facepunch.model.facepunch.Subforum;
import com.egonny.facepunch.util.FPParser;

import java.util.ArrayList;
import java.util.List;

public class SubforumFragment extends ListFragment {

	private SubforumAdapter mAdapter;
	private onThreadClickListener mListener;

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
	}

	public void setAdapter(SubforumAdapter adapter) {
		mAdapter = adapter;
		setListAdapter(adapter);
	}

	public interface onThreadClickListener {
		void onThreadClick(FPThread thread);
	}

	public void load(Subforum subforum) {
		mSubforum = subforum;
		mCurrentPage = 1;
		mAdapter.clear();
		getThreads(subforum, 1, new SubforumCallback() {
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

	}

	private interface SubforumCallback {
		void onResult(boolean success, List<FPThread> threads);
	}

	private void getThreads(Subforum subforum, int page, final SubforumCallback callback) {
		FPApplication.getInstance().addToRequestQueue(
			new StringRequest("http://facepunch.com/forumdisplay.php?f=" + subforum.getId(),
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