package com.egonny.facepunch.fragments;

import android.app.ListFragment;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
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

	private LinearLayout mProgressFooter;
	private LinearLayout mErrorFooter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mProgressFooter = (LinearLayout) inflater.inflate(R.layout.menu_footer_progress, null);
		mErrorFooter = (LinearLayout) inflater.inflate(R.layout.menu_footer_error, null);
		return inflater.inflate(R.layout.fragment_menu, container, false);
	}

	public void onStart() {
		super.onStart();
		if (mAdapter == null) {
			// Required to add a footer before the adapter gets set, so Android doesn't freak out.
			getListView().addFooterView(mProgressFooter);
			getListView().removeFooterView(mProgressFooter);

			Button retryButton = (Button) mErrorFooter.findViewById(R.id.menu_footer_error_button);
			retryButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					refreshForums();
				}
			});

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

		refreshForums();

	}

	private void refreshAccountCategory() {
		mAccountCategory.clear();
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

		mAdapter.removeHeader(mAccountCategory);
		mAdapter.addHeader(mAccountCategory);
	}

	private void refreshForums() {
		//TODO not really ideal, find a way to refresh the forums without having to reload the account category.
		mAdapter.clear();
		refreshAccountCategory();
		setLoading(true);
		hideErrorScreen();
		getCategories(new CategoryCallback() {
			@Override
			public void onResult(boolean success, List<Category> categories) {
				if (success) {
					for (Category category : categories) {
						MenuListHeader header = new MenuListHeader(category.getName());
						for (Subforum subforum : category.getSubforums()) {
							if (subforum.getId() == 6) {
								// Just a test to see if counter is working
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
				} else showErrorScreen(R.string.menu_error_generic);
				setLoading(false);
			}
		});
	}

	private void setLoading(boolean loading) {
		ListView l = getListView();
		if (loading) l.addFooterView(mProgressFooter);
		else l.removeFooterView(mProgressFooter);
	}

	private void showErrorScreen(int resid) {
		getListView().addFooterView(mErrorFooter);
		TextView errorText = (TextView) mErrorFooter.findViewById(R.id.menu_footer_error_text);
		errorText.setText(resid);
	}

	private void hideErrorScreen() {
		getListView().removeFooterView(mErrorFooter);
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