package com.egonny.facepunch.model.menu;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.egonny.facepunch.R;
import com.egonny.facepunch.util.ImageLoaderHelper;

public abstract class NetworkMenuListItem extends MenuListItem {

	public NetworkMenuListItem(String title) {
		super(title);
	}

	protected abstract String getImageUrl();

	@Override
	protected void loadIcon(final Resources resources) {
		ImageLoaderHelper.getInstance().getBitmap(getImageUrl(), new ImageLoaderHelper.ImageCallback() {
			@Override
			public void onResult(boolean success, Bitmap bitmap) {
				if (success) {
					Drawable img = new BitmapDrawable(resources, bitmap);
					setCompoundDrawable(img, (int) resources.getDimension(R.dimen.menu_item_icon_size));
				}
			}
		});

	}
}
