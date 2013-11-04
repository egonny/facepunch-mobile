package com.egonny.facepunch.model.menu;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.egonny.facepunch.R;
import com.egonny.facepunch.util.headeradapter.HeaderListHeader;
import com.egonny.facepunch.util.headeradapter.HeaderListItem;

public abstract class MenuListItem implements HeaderListItem {

	private String mTitle;
	protected TextView textView;

	public MenuListItem(String title) {
		this.mTitle = title;
	}

	@Override
	public abstract HeaderListHeader getHeader();

	//TODO: not really ideal, look for a better way to asynchronously load compound drawables.
	//!! Current implementation also doesn't keep recycling in mind, use tags if necessary to check.
	protected abstract void loadIcon(Resources resources);

	protected void setCompoundDrawable(Drawable drawable, int iconSize) {
		if (textView != null) {
			drawable.setBounds(0, 0, iconSize, iconSize);
			textView.setCompoundDrawables(drawable, null, null, null);
		}
	}

	@Override
	public View getView(LayoutInflater inflater, View convertView) {
		return loadView(inflater, inflater.inflate(R.layout.menu_item_layout, null));
	}

	public View getSelectedView(LayoutInflater inflater, View convertView) {
		return loadView(inflater, inflater.inflate(R.layout.menu_item_layout_selected, null));
	}

	private View loadView(LayoutInflater inflater,View convertView) {
		textView = (TextView) convertView.findViewById(R.id.menu_item_text);
		textView.setText(mTitle);

		Resources res = inflater.getContext().getResources();
		if (res == null) throw new IllegalStateException("Could not get Resources.");
		loadIcon(res);

		return convertView;
	}
}
