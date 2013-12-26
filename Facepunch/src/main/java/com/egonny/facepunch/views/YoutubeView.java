package com.egonny.facepunch.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.egonny.facepunch.FPApplication;
import com.egonny.facepunch.R;
import com.egonny.facepunch.util.ImageLoaderHelper;
import com.egonny.facepunch.util.Keys;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class YoutubeView extends MediaView {

	public YoutubeView(Context context) {
		super(context);
		ImageButton button = (ImageButton) findViewById(R.id.media_button);
		button.setImageResource(R.drawable.youtube_icon);
	}

	public void load(String id) {
		FPApplication.getInstance().addToRequestQueue(
				new JsonObjectRequest("https://www.googleapis.com/youtube/v3/videos?part=snippet&id=" + id + "&key=" + Keys.youtubeWebKey,
						null,
						new Response.Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject jsonObject) {
								parse(jsonObject);
							}},
						new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError volleyError) {
								showErrorScreen();
							}}
		));
	}

	private void parse(JSONObject jsonObject) {
		try {
			JSONArray items = jsonObject.getJSONArray("items");
			JSONObject video = items.getJSONObject(0).getJSONObject("snippet");
			if (video == null) {
				showErrorScreen();
				return;
			}
			TextView title = (TextView) findViewById(R.id.media_title);
			title.setText(video.getString("title"));
			final ImageView background = (ImageView) findViewById(R.id.media_background);
			ImageLoaderHelper.getInstance().getBitmap(video.getJSONObject("thumbnails").getJSONObject("maxres").getString("url"),
					new ImageLoaderHelper.ImageCallback() {
						@Override
						public void onResult(boolean success, Bitmap bitmap) {
							if (success) {
								background.setImageBitmap(bitmap);
							}
						}
					});
		} catch (JSONException e) {
			showErrorScreen();
		}

	}
}
