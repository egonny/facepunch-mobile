package com.egonny.facepunch.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import com.egonny.facepunch.R;

public abstract class MediaView extends FrameLayout {

	protected MediaView(Context context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_media, this);
	}

	protected void showErrorScreen() {

	}
}
