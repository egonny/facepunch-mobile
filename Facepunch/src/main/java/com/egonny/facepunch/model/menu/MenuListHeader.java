package com.egonny.facepunch.model.menu;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.egonny.facepunch.R;
import com.egonny.facepunch.util.headeradapter.HeaderListHeader;

public abstract class MenuListHeader implements HeaderListHeader {

	private String mTitle;

	public MenuListHeader(String title) {
		this.mTitle = title;
	}

	@Override
	public String getTitle() {
		return mTitle;
	}

	@Override
	public View getView(LayoutInflater inflater, View convertView) {
		convertView = inflater.inflate(R.layout.menu_header_layout, null);
		TextView textView = (TextView) convertView.findViewById(R.id.menu_header_title);
		if (textView != null) textView.setText(getTitle());
		return convertView;
	}
}
