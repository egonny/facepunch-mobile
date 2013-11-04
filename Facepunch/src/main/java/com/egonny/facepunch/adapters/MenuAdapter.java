package com.egonny.facepunch.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.egonny.facepunch.model.menu.MenuListHeader;
import com.egonny.facepunch.model.menu.MenuListItem;
import com.egonny.facepunch.util.headeradapter.HeaderAdapter;
import com.egonny.facepunch.util.headeradapter.HeaderListHeader;
import com.egonny.facepunch.util.headeradapter.HeaderListItem;

public class MenuAdapter extends HeaderAdapter<MenuListHeader, MenuListItem> {

	private MenuListItem mSelectedItem;

	public MenuAdapter(Context context) {
		super(context, 0);
	}

	public MenuListItem getSelectedItem() {
		return mSelectedItem;
	}

	public void setSelectedItem(MenuListItem item) {
		this.mSelectedItem = item;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		if (getItem(position) == mSelectedItem) {
			view = ((MenuListItem) getItem(position)).getSelectedView((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE),
					view);
		}
		return view;
	}
}
