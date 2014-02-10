package com.egonny.facepunch.model.menu;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.egonny.facepunch.R;
import com.egonny.facepunch.util.headeradapter.HeaderListHeader;
import com.egonny.facepunch.util.headeradapter.HeaderListItem;
import com.egonny.facepunch.util.headeradapter.HeaderListSubItem;

import java.util.ArrayList;
import java.util.List;

public class MenuListItem implements HeaderListItem {

	private String mTitle;
	private HeaderListHeader mHeader;
	private List<MenuListSubItem> mSubItems;

	private int mCounter;
	private boolean mSelected;
	private int mIconId;

	public MenuListItem(String title, int iconId) {
		mTitle = title;
		mIconId = iconId;

		mSubItems = new ArrayList<MenuListSubItem>();
	}

	@Override
	public HeaderListHeader getHeader() {
		return mHeader;
	}

	public void setHeader(MenuListHeader header) {
		mHeader = header;
	}

	public int getCounter() {
		return mCounter;
	}

	public void setCounter(int counter) {
		mCounter = counter;
	}

	public void addSubItem(MenuListSubItem subItem) {
		mSubItems.add(subItem);
		subItem.setItem(this);
	}

	public void removeSubItem(MenuListSubItem subItem) {
		subItem.setItem(null);
		mSubItems.remove(subItem);
	}

	@Override
	public List<HeaderListSubItem> getSubItems() {
		return new ArrayList<HeaderListSubItem>(mSubItems);
	}

	@Override
	public int getSubItemCount() {
		return mSubItems.size();
	}

	public boolean isSelected() {
		return mSelected;
	}

	public void setSelected(boolean mSelected) {
		this.mSelected = mSelected;
	}

	protected void setIcon(Resources resources, ImageView view) {
		view.setImageResource(mIconId);
	}

	@Override
	public View getView(LayoutInflater inflater, View convertView) {
		convertView = inflater.inflate(R.layout.menu_item_layout, null);
		TextView title = (TextView) convertView.findViewById(R.id.menu_item_text);
		title.setText(mTitle);

		Resources res = inflater.getContext().getResources();
		if (res == null) throw new IllegalStateException("Could not get Resources.");
		ImageView imageView = (ImageView) convertView.findViewById(R.id.menu_item_icon);
		setIcon(res, imageView);

		TextView counter = (TextView) convertView.findViewById(R.id.menu_item_counter);
		if (mCounter > 0) {
			counter.setText("" + mCounter);
			counter.setVisibility(View.VISIBLE);
		} else {
			counter.setText("0"); //for spacing
			counter.setVisibility(View.INVISIBLE);
		}

		//TODO use selector instead of color
		if (mSelected) convertView.setBackgroundResource(R.color.menu_item_background_selected);
		else convertView.setBackgroundResource(android.R.color.transparent);

		return convertView;
	}
}
