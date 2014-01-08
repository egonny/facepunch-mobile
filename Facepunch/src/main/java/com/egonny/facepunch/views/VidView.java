package com.egonny.facepunch.views;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class VidView extends MediaView {

	String mUrl;

	public VidView(Context context) {
		super(context);
	}

	public void load(String url) {
		mUrl = url;
	}

	@Override
	protected void onClick() {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl));
		intent.setDataAndType(Uri.parse(mUrl), "video/");
		getContext().startActivity(intent);
	}
}
