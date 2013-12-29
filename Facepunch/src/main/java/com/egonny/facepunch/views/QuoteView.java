package com.egonny.facepunch.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.egonny.facepunch.R;
import com.egonny.facepunch.model.facepunch.User;

import java.util.List;

public class QuoteView extends LinearLayout {

	protected TextView mTitle;
	protected LinearLayout mMessage;

	public QuoteView(Context context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_quote, this);

		mTitle = (TextView) findViewById(R.id.quote_author);
		mMessage = (LinearLayout) findViewById(R.id.quote_message);

		setBackgroundResource(R.color.lighter_gray);
		setOrientation(VERTICAL);
		LinearLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		int margin = (int) context.getResources().getDimension(R.dimen.thread_item_message_view_spacer);
		params.setMargins(0, margin, 0, margin);
		setLayoutParams(params);
	}

	public void setQuote(String author, List<View> message) {
		if (author != null) {
			mTitle.setText(author + " posted");
			mTitle.setVisibility(VISIBLE);
			mMessage.setPadding(mMessage.getPaddingLeft(), 0, mMessage.getPaddingRight(), mMessage.getPaddingBottom());
		} else {
			mTitle.setVisibility(GONE);
			mMessage.setPadding(mMessage.getPaddingLeft(), mTitle.getPaddingTop(), mMessage.getPaddingRight(), mMessage.getPaddingBottom());
		}
		mMessage.removeAllViews();
		for (View view: message) mMessage.addView(view);
	}
}
