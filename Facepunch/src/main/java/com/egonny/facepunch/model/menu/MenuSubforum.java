package com.egonny.facepunch.model.menu;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.widget.ImageView;
import com.egonny.facepunch.util.ImageLoaderHelper;

public class MenuSubforum extends MenuListItem {

	private int mId;

	public MenuSubforum(String title, int id) {
		super(title, -1);
		mId = id;
	}

	public int getId() {
		return mId;
	}

	@Override
	protected void setIcon(Resources resources, final ImageView view) {
		ImageLoaderHelper.getInstance().getBitmap(getImageUrl(), new ImageLoaderHelper.ImageCallback() {
			@Override
			public void onResult(boolean success, Bitmap bitmap) {
				if (success) {
					view.setImageBitmap(bitmap);
				}
			}
		});
	}

	protected String getImageUrl() {
		return "http://www.facepunch.com/fp/forums/" + mId + ".png";
	}
}
