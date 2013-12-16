package com.egonny.facepunch.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.egonny.facepunch.R;
import com.egonny.facepunch.model.facepunch.FPPost;

public class ThreadAdapter extends ArrayAdapter<FPPost> {
	public ThreadAdapter(Context context) {
		super(context, 0);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.thread_item_layout, null);
		}

		FPPost post = getItem(position);
		TextView mainTextView = (TextView) convertView.findViewById(R.id.post_message);
		mainTextView.setText(post.getMessage());

		return convertView;
	}
}
