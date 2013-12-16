package com.egonny.facepunch.activities;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
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
    }

	@Override
	protected void onStart() {
		super.onStart();
		if (mFragment.getThread() == null) {
			mFragment.load(mThread);
		}
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
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
