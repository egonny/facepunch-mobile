package com.egonny.facepunch.fragments;

import android.app.ListFragment;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.egonny.facepunch.FPApplication;
import com.egonny.facepunch.R;
import com.egonny.facepunch.adapters.MenuAdapter;
import com.egonny.facepunch.model.facepunch.Category;
import com.egonny.facepunch.model.facepunch.Subforum;
import com.egonny.facepunch.model.menu.ActionItem;
import com.egonny.facepunch.model.menu.MenuListHeader;
import com.egonny.facepunch.model.menu.MenuListItem;
import com.egonny.facepunch.model.menu.MenuSubforum;
import com.egonny.facepunch.util.FPParser;

import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends ListFragment {

	private MenuAdapter mAdapter;
	private onItemClickListener mListener;
	private MenuListHeader mAccountCategory;

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

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		MenuListItem item = (MenuListItem) l.getItemAtPosition(position);
		if (mListener != null && mAdapter.getSelectedItem() != item) {
			if (item instanceof MenuSubforum) {
				mListener.onSubforumClick((MenuSubforum) item);
			} else {
				mListener.onActionClick(((ActionItem) item).getAction());
			}
		}
		mAdapter.setSelectedItem(item);
		mAdapter.notifyDataSetChanged();
	}

	public interface onItemClickListener {
		void onSubforumClick(MenuSubforum subforum);
		void onActionClick(ActionItem.Action action);
	}

	public void setItemClickListener(onItemClickListener listener) {
		mListener = listener;
	}

	public void load() {
		assert mAdapter != null;
		if (!mAdapter.isEmpty()) mAdapter.clear();

		// Add account category
		mAccountCategory = new MenuListHeader(getResources().getString(R.string.menu_account_category));
		Resources res = getActivity().getResources();
		if (isLoggedIn()) {
			// TODO: add item for user profile


			String[] menuTitles = res.getStringArray(R.array.menu_account_logged_in);
			TypedArray menuIcons = res.obtainTypedArray(R.array.menu_account_logged_in_icons);

			if (menuIcons == null || menuIcons.length() != menuTitles.length) {
				throw new IllegalStateException("The amount of menu titles and menu icons are not equal.");
			}
			mAccountCategory.addItem(new ActionItem(menuTitles[0], menuIcons.getResourceId(0, -1), ActionItem.Action.PM));
			mAccountCategory.addItem(new ActionItem(menuTitles[1], menuIcons.getResourceId(1, -1), ActionItem.Action.SUBSCRIBED));
			menuIcons.recycle();
		} else {
			mAccountCategory.addItem(new ActionItem(res.getString(R.string.menu_login), R.drawable.ic_login, ActionItem.Action.LOG_IN));
		}
		String[] menuTitles = res.getStringArray(R.array.menu_account);
		TypedArray menuIcons = res.obtainTypedArray(R.array.menu_account_icons);
		if (menuIcons == null || menuIcons.length() != menuTitles.length) {
			throw new IllegalStateException("The amount of menu titles and menu icons are not equal.");
		}
		mAccountCategory.addItem(new ActionItem(menuTitles[0], menuIcons.getResourceId(0, -1), ActionItem.Action.POPULAR));

		refreshAccountCategory();

		// Add forums
		getCategories(new CategoryCallback() {
			@Override
			public void onResult(boolean success, List<Category> categories) {
				if (success) {
					for (Category category : categories) {
						MenuListHeader header = new MenuListHeader(category.getName());
						for (Subforum subforum : category.getSubforums()) {
							if (subforum.getId() == 6) {
								MenuSubforum x = new MenuSubforum(subforum.getTitle(), subforum.getId());
								x.setCounter(99);
								header.addItem(x);
							} else {
								header.addItem(new MenuSubforum(subforum.getTitle(), subforum.getId()));
							}
						}
						mAdapter.addHeader(header);
					}
					mAdapter.notifyDataSetChanged();
				}
			}
		});
	}

	private void refreshAccountCategory() {
		mAdapter.removeHeader(mAccountCategory);
		mAdapter.addHeader(mAccountCategory);
	}

	private boolean isLoggedIn() {
		return false;
	}

	private interface CategoryCallback {
		void onResult(boolean success, List<Category> categories);
	}

	private void getCategories(final CategoryCallback callback) {
		FPApplication.getInstance().addToRequestQueue(new StringRequest("http://www.facepunch.com/forum.php",
				new Response.Listener<String>() {
					@Override
					public void onResponse(String s) {
						callback.onResult(true, FPParser.parseCategories(s));
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						callback.onResult(false, new ArrayList<Category>());
					}
				}), "menu");
	}
}