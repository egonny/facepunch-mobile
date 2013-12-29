package com.egonny.facepunch.views;

import android.content.Context;
import android.graphics.Bitmap;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.egonny.facepunch.FPApplication;
import com.egonny.facepunch.R;
import com.egonny.facepunch.util.ImageLoaderHelper;
import com.egonny.facepunch.util.Keys;
import org.json.JSONException;
import org.json.JSONObject;

public class SoundcloudView extends MediaView {

	public SoundcloudView(Context context) {
		super(context);

		mButton.setImageResource(R.drawable.soundcloud_icon);
	}

	public void load(String url) {
		FPApplication.getInstance().addToRequestQueue(new JsonObjectRequest("http://api.soundcloud.com/resolve.json?url="+ url + "&client_id=" + Keys.soundcloudClientId,
				null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonObject) {
						parse(jsonObject);
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						showErrorScreen();
					}
				}));
	}

	private void parse(JSONObject object) {
		try {
			mTitle.setText(object.getString("title"));
			mAuthor.setText(object.getJSONObject("user").getString("username"));
			String artwork = object.getString("artwork_url");
			if (!artwork.equals("null")) {
				artwork = artwork.replace("large", "t500x500");
				ImageLoaderHelper.getInstance().getBitmap(artwork, new ImageLoaderHelper.ImageCallback() {
					@Override
					public void onResult(boolean success, Bitmap bitmap) {
						if (success && bitmap != null) mBackground.setImageBitmap(bitmap);
					}
				});
			}
		} catch (JSONException e) {
			showErrorScreen();
		}

	}
}
