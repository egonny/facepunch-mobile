package com.egonny.facepunch.activities;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.NumberPicker;
import com.egonny.facepunch.R;
import com.egonny.facepunch.fragments.ThreadFragment;
import com.egonny.facepunch.model.facepunch.FPThread;

public class ThreadActivity extends Activity {

	ThreadFragment mFragment;
	FPThread mThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);

        if (savedInstanceState == null) {
	        mFragment = new ThreadFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.container, mFragment)
                    .commit();
	        getFragmentManager().executePendingTransactions();
	        mThread = (FPThread) getIntent().getExtras().get("thread");
        }

//	    getActionBar().setDisplayHomeAsUpEnabled(true);
	    getActionBar().setTitle(mThread.getTitle());
    }

	@Override
	protected void onStart() {
		super.onStart();
		if (mFragment.getThread() == null) {
			mFragment.load(mThread);
		}
	}

	private void switchPage() {
		// Show login dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// Get the layout inflater
		builder.setTitle(R.string.jump_to_page);

		final NumberPicker picker = new NumberPicker(this);
		picker.setMinValue(1);
		picker.setMaxValue(mFragment.getThread().getPages());
		picker.setValue(mFragment.getCurrentPage());
		picker.setWrapSelectorWheel(true);
		builder.setView(picker)
				// Add action buttons
				.setPositiveButton(R.string.jump_to_page_OK, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(final DialogInterface dialog, int id) {
						mFragment.reload(picker.getValue());
					}
				})
				.setNegativeButton(R.string.jump_to_page_cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.thread, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
	    switch (item.getItemId()) {
		    case R.id.action_settings:
			    return true;
		    case R.id.jump_to_page:
			    switchPage();
	    }
        return super.onOptionsItemSelected(item);
    }
}
