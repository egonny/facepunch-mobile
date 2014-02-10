package com.egonny.facepunch.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.egonny.facepunch.R;
import com.egonny.facepunch.model.facepunch.FPPost;
import com.egonny.facepunch.util.HTMLPostParser;
import com.egonny.facepunch.util.ImageLoaderHelper;
import org.xml.sax.XMLReader;

import java.util.ArrayList;
import java.util.List;

public class ThreadAdapter extends ArrayAdapter<FPPost> {
	public ThreadAdapter(Context context) {
		super(context, 0);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.thread_item_layout, null);
		}
		FPPost post = getItem(position);

		final ImageView avatar = (ImageView) convertView.findViewById(R.id.post_author_image);
		avatar.setTag(position);
		final int mPosition = position;
		ImageLoaderHelper.getInstance().getBitmap("http://facepunch.com/image.php?u=" + post.getAuthor().getId(), new ImageLoaderHelper.ImageCallback() {
			@Override
			public void onResult(boolean success, Bitmap bitmap) {
				if (success && mPosition == avatar.getTag()) avatar.setImageBitmap(bitmap);
			}
		});
		TextView authorName = (TextView) convertView.findViewById(R.id.post_author_name);
		authorName.setText(post.getAuthor().getName());
		if (post.getAuthor().getUserGroup().getColor() != -1) {
			authorName.setTextColor(getContext().getResources().getColor(post.getAuthor().getUserGroup().getColor()));
		} else {
			authorName.setTextColor(getContext().getResources().getColor(R.color.thread_primary));
		}
		TextView authorNbPosts = (TextView) convertView.findViewById(R.id.post_author_nbposts);
		authorNbPosts.setText(Integer.toString(post.getAuthor().getPostcount()) + " Posts");
		TextView postDate = (TextView) convertView.findViewById(R.id.post_date);
		postDate.setText(post.getPostTime());

		LinearLayout message = (LinearLayout) convertView.findViewById(R.id.post_message);
		message.removeAllViews();
		List<View> messageViews = HTMLPostParser.parse(inflater, post.getMessage());
		for (View view: messageViews) message.addView(view);

		return convertView;
	}
}
