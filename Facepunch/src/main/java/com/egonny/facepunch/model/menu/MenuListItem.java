package com.egonny.facepunch.model.menu;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.egonny.facepunch.R;
import com.egonny.facepunch.util.headeradapter.HeaderListHeader;
import com.egonny.facepunch.util.headeradapter.HeaderListItem;

public class MenuListItem implements HeaderListItem {

	private HeaderListHeader mHeader;
	private String mTitle;

	public MenuListItem(String title) {
		this.mTitle = title;
	}

	@Override
	public HeaderListHeader getHeader() {
		return mHeader;
	}

	@Override
	public View getView(LayoutInflater inflater, View convertView) {
		convertView = inflater.inflate(R.layout.menu_item_layout, null);
		TextView textView = (TextView) convertView.findViewById(R.id.menu_item_text);
		textView.setText(mTitle);
		return convertView;
	}
}
