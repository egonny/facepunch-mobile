package com.egonny.facepunch.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import com.egonny.facepunch.R;
import com.egonny.facepunch.fragments.SubforumFragment;
import com.egonny.facepunch.model.facepunch.FPThread;

public class SubforumAdapter extends ArrayAdapter<FPThread> {

	private OnDotsClickListener mListener;

	public SubforumAdapter(Context context) {
		super(context, 0);
	}

	public void setOnDotsClickListener(OnDotsClickListener listener) {
		mListener = listener;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.subforum_item_layout, null);
		}

		final FPThread thread = getItem(position);
		TextView titleTextView = (TextView) convertView.findViewById(R.id.subforum_item_title);
		titleTextView.setText(thread.getTitle());

		TextView authorTextView = (TextView) convertView.findViewById(R.id.subforum_item_author);
		authorTextView.setText(" " + thread.getAuthor().getName());

		TextView readingTextView = (TextView) convertView.findViewById(R.id.subforum_item_reading);
		if (thread.getReading() > 0) {
			readingTextView.setVisibility(View.VISIBLE);
			readingTextView.setText(" " + thread.getReading());
		}
		else readingTextView.setVisibility(View.GONE);

		TextView recentTextView = (TextView) convertView.findViewById(R.id.subforum_item_recent);
		recentTextView.setText(" " + thread.getLastPostAuthor().getName() + ", " + thread.getLastPostDate());

		ImageButton dots = (ImageButton) convertView.findViewById(R.id.subforum_item_dots);
		dots.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mListener != null) {
					mListener.onClick(view, thread);
				}
			}
		});
		dots.setFocusable(false);

		return convertView;
	}

	public interface OnDotsClickListener {
		void onClick(View view, FPThread thread);
	}

}
