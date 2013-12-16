package com.egonny.facepunch.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;
import com.egonny.facepunch.model.facepunch.FPPost;

public class ThreadAdapter extends ArrayAdapter<FPPost> {
	public ThreadAdapter(Context context) {
		super(context, 0);
	}
}
