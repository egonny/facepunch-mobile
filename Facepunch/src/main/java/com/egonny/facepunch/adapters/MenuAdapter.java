package com.egonny.facepunch.adapters;

import android.content.Context;
import com.egonny.facepunch.model.menu.MenuListHeader;
import com.egonny.facepunch.model.menu.MenuListItem;
import com.egonny.facepunch.model.menu.MenuListSubItem;
import com.egonny.facepunch.util.headeradapter.HeaderAdapter;

public class MenuAdapter extends HeaderAdapter<MenuListHeader, MenuListItem, MenuListSubItem> {

	private MenuListItem mSelectedItem;

	public MenuAdapter(Context context) {
		super(context, 0);
	}

	public MenuListItem getSelectedItem() {
		return mSelectedItem;
	}

	public void setSelectedItem(MenuListItem item) {
		if (mSelectedItem != null) mSelectedItem.setSelected(false);
		item.setSelected(true);
		this.mSelectedItem = item;
	}
}
