package com.egonny.facepunch.model.menu;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.egonny.facepunch.R;
import com.egonny.facepunch.util.headeradapter.HeaderListHeader;
import com.egonny.facepunch.util.headeradapter.HeaderListItem;

import java.util.ArrayList;
import java.util.List;

public class MenuListHeader implements HeaderListHeader {

	private String mTitle;
	private List<HeaderListItem> mItems;

	public MenuListHeader(String title) {
		this.mTitle = title;

		mItems = new ArrayList<HeaderListItem>();
	}

	@Override
	public String getTitle() {
		return mTitle;
	}

	public void addItem(MenuListItem item) {
		mItems.add(item);
		item.setHeader(this);
	}

	public void removeItem(MenuListItem item) {
		item.setHeader(null);
		mItems.remove(item);
	}

	@Override
	public List<HeaderListItem> getItems() {
		return new ArrayList<HeaderListItem>(mItems);
	}

	@Override
	public int getItemCount() {
		return mItems.size();
	}

	@Override
	public View getView(LayoutInflater inflater, View convertView) {
		convertView = inflater.inflate(R.layout.menu_header_layout, null);
		TextView textView = (TextView) convertView.findViewById(R.id.menu_header_title);
		textView.setText(getTitle());
		return convertView;
	}

	public void clear() {
		mItems.clear();
	}
}
