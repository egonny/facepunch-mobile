package com.egonny.facepunch.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.egonny.facepunch.R;

public abstract class MediaView extends FrameLayout {

	protected ImageButton mButton;
	protected TextView mTitle;
	protected TextView mAuthor;
	protected ImageView mBackground;

	protected MediaView(Context context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_media, this);

		mButton = (ImageButton) findViewById(R.id.media_button);
		mTitle = (TextView) findViewById(R.id.media_title);
		mAuthor = (TextView) findViewById(R.id.media_author);
		mBackground = (ImageView) findViewById(R.id.media_background);

		setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
	}

	protected void showErrorScreen() {

	}

	protected void showLoadingScreen() {

	}
}
