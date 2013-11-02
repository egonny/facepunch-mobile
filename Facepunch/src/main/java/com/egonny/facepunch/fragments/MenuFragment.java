package com.egonny.facepunch.fragments;

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
import com.egonny.facepunch.adapter.MenuAdapter;
import com.egonny.facepunch.model.facepunch.Category;
import com.egonny.facepunch.model.facepunch.Subforum;
import com.egonny.facepunch.util.FPParser;

import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends ListFragment {

	private MenuAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_menu, container, false);
	}

	public void onStart() {
		super.onStart();
		if (mAdapter == null) {
			setAdapter(new MenuAdapter(getActivity()));
			load();
		}
	}

	public void setAdapter(MenuAdapter adapter) {
		mAdapter = adapter;
		setListAdapter(adapter);
	}

	public void load() {
		assert mAdapter != null;
		if (!mAdapter.isEmpty()) mAdapter.clear();

		getCategories(new CategoryCallback() {
			@Override
			public void onResult(boolean success, List<Category> categories) {
				if (success) {

					for (Category category: categories) {
						mAdapter.addHeader(category);
						for (Subforum subforum: category.getSubforums()) {
							mAdapter.addItem(subforum);
						}
					}
					mAdapter.notifyDataSetChanged();
				}
			}
		});
	}

	private interface CategoryCallback {
		void onResult(boolean success, List<Category> categories);
	}

	private void getCategories(final CategoryCallback callback) {
		FPApplication.getInstance().getRequestQueue().add(new StringRequest("http://www.facepunch.com/forum.php",
				new Response.Listener<String>() {
					@Override
					public void onResponse(String s) {
						callback.onResult(true, FPParser.parseCategories(s));
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						callback.onResult(true, new ArrayList<Category>());
					}
				}));
	}
}