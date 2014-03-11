package com.egonny.facepunch.fragments;

import android.app.ListFragment;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.AsyncTask;
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
import com.egonny.facepunch.activities.MainActivity;
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
	private boolean mLoading;
	private LinearLayout mErrorFooter;
	private boolean mError;

	private CategoryParseTask mParseTask;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mProgressFooter = (LinearLayout) inflater.inflate(R.layout.menu_footer_progress, null);
		mErrorFooter = (LinearLayout) inflater.inflate(R.layout.menu_footer_error, null);
		return inflater.inflate(R.layout.fragment_menu, container, false);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		setListAdapter(null);
		mProgressFooter = null;
		mErrorFooter = null;
	}

	public void onStart() {
		super.onStart();
		if (mAdapter == null) {
			// Required to add a footer before the adapter gets set, so Android doesn't freak out.
			getListView().addFooterView(mProgressFooter);
			getListView().removeFooterView(mProgressFooter);

			mAdapter = new MenuAdapter(getActivity());
			setListAdapter(mAdapter);
			load();
		} else {
			// I assume this is in case the fragment is retained, so we need to recreate the footers
			setListAdapter(mAdapter);
			setLoading(mLoading);
			if (mError) showErrorScreen();
			else hideErrorScreen();
		}
		Button retryButton = (Button) mErrorFooter.findViewById(R.id.menu_footer_error_button);
		retryButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				refreshForums();
			}
		});
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

	public String getUsername() {
		return ((MainActivity)getActivity()).getUsername();
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

	public void refreshAccountCategory() {
		mAdapter.removeHeader(mAccountCategory);
		mAccountCategory.clear();
		Resources res = getActivity().getResources();
		if (isLoggedIn()) {
			mAccountCategory.addItem(new ActionItem(getUsername(), R.drawable.ic_action_person, ActionItem.Action.PROFILE));

			String[] menuTitles = res.getStringArray(R.array.menu_account_logged_in);
			TypedArray menuIcons = res.obtainTypedArray(R.array.menu_account_logged_in_icons);

			if (menuIcons == null || menuIcons.length() != menuTitles.length) {
				throw new IllegalStateException("The amount of menu titles and menu icons are not equal.");
			}
			ActionItem pm = new ActionItem(menuTitles[0], menuIcons.getResourceId(0, -1), ActionItem.Action.PM);
			pm.setCounter(2); // Just to test counter
			mAccountCategory.addItem(pm);
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
		mAdapter.addHeader(mAccountCategory, 0);
	}

	public void refreshForums() {
		//TODO not really ideal, find a way to refresh the forums without having to reload the account category.
		mAdapter.clear();
		refreshAccountCategory();
		setLoading(true);
		hideErrorScreen();
		getCategories();
	}

	private void addCategories(List<Category> categories) {
		for (Category category : categories) {
			MenuListHeader header = new MenuListHeader(category.getName());
			for (Subforum subforum : category.getSubforums()) {
				header.addItem(new MenuSubforum(subforum.getTitle(), subforum.getId()));
			}
			mAdapter.addHeader(header);
		}
		mAdapter.notifyDataSetChanged();
	}

	private void setLoading(boolean loading) {
		mLoading = loading;
		ListView l = getListView();
		if (l != null) {
			if (loading) l.addFooterView(mProgressFooter);
			else l.removeFooterView(mProgressFooter);
		}
	}

	private void showErrorScreen() {
		showErrorScreen(R.string.menu_error_generic);
	}

	private void showErrorScreen(int resid) {
		getListView().addFooterView(mErrorFooter);
		TextView errorText = (TextView) mErrorFooter.findViewById(R.id.menu_footer_error_text);
		errorText.setText(resid);
		mError = true;
	}

	private void hideErrorScreen() {
		getListView().removeFooterView(mErrorFooter);
		mError = false;
	}

	private boolean isLoggedIn() {
		return !((MainActivity) getActivity()).getSessionHash().equals("");
	}

	private void getCategories() {
		FPApplication.getInstance().addToRequestQueue(new StringRequest("http://www.facepunch.com/forum.php",
				new Response.Listener<String>() {
					@Override
					public void onResponse(String s) {
						mParseTask = new CategoryParseTask();
						mParseTask.execute(s);
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						showErrorScreen(R.string.menu_error_generic);
						setLoading(false);
					}
				}), "menu");
	}

	private class CategoryParseTask extends AsyncTask<String, Integer, List<Category>> {

		@Override
		protected List<Category> doInBackground(String... strings) {
			String s = strings[0];
			return FPParser.parseCategories(s);
		}

		@Override
		protected void onPostExecute(List<Category> categories) {
			super.onPostExecute(categories);
			addCategories(categories);
			setLoading(false);
		}
	}
}