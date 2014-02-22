package com.egonny.facepunch.model.menu;

import android.view.LayoutInflater;
import android.view.View;
import com.egonny.facepunch.util.headeradapter.HeaderListItem;
import com.egonny.facepunch.util.headeradapter.HeaderListSubItem;

public class MenuListSubItem implements HeaderListSubItem {

	private String mTitle;
	private HeaderListItem mItem;

	public MenuListSubItem(String title) {
		mTitle = title;
	}

	@Override
	public HeaderListItem getItem() {
		return mItem;
	}

	public void setItem(HeaderListItem item) {
		mItem = item;
	}

	@Override
	public View getView(LayoutInflater inflater, View convertView) {
		return null;
	}
}
