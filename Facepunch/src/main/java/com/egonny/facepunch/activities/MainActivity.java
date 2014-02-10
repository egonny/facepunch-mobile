package com.egonny.facepunch.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.egonny.facepunch.R;
import com.egonny.facepunch.fragments.MenuFragment;
import com.egonny.facepunch.fragments.SubforumFragment;
import com.egonny.facepunch.model.facepunch.FPThread;
import com.egonny.facepunch.model.facepunch.Subforum;
import com.egonny.facepunch.model.menu.MenuSubforum;

public class MainActivity extends Activity implements MenuFragment.onItemClickListener, SubforumFragment.onItemClickListener {

	private MenuFragment mMenuFragment;
	private SubforumFragment mSubforumFragment;

	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

	    // Configure drawer
	    mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
	    mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
			    R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

		    /** Called when a drawer has settled in a completely closed state. */
		    public void onDrawerClosed(View view) {
			    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
		    }

		    /** Called when a drawer has settled in a completely open state. */
		    public void onDrawerOpened(View drawerView) {
			    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
		    }
	    };

	    // Set the drawer toggle as the DrawerListener
	    mDrawerLayout.setDrawerListener(mDrawerToggle);
	    getActionBar().setDisplayHomeAsUpEnabled(true);
	    getActionBar().setHomeButtonEnabled(true);

	    // Prepare fragments
	    FragmentManager manager = getFragmentManager();
	    mMenuFragment = (MenuFragment) manager.findFragmentByTag("menu");
	    if (mMenuFragment == null) {
		    mMenuFragment = new MenuFragment();
		    mMenuFragment.setRetainInstance(true);
		    mMenuFragment.setItemClickListener(this);
		    manager.beginTransaction().add(R.id.menu_fragment_frame, mMenuFragment, "menu").commit();
	    }

	    mSubforumFragment = (SubforumFragment) manager.findFragmentByTag("subforum");
	    if (mSubforumFragment == null) {
		    mSubforumFragment = new SubforumFragment();
		    mSubforumFragment.setRetainInstance(true);
		    mSubforumFragment.setItemClickListener(this);
		    manager.beginTransaction().add(R.id.subforum_fragment_frame, mSubforumFragment, "subforum").commit();
	    }
    }

	@Override
	public void onSubforumClick(MenuSubforum subforum) {
		mSubforumFragment.load(subforum);
	}

	@Override
	public void onThreadClick(FPThread thread) {
		Intent intent = new Intent(this, ThreadActivity.class);
		intent.putExtra("thread", thread);
		startActivity(intent);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(findViewById(R.id.menu_fragment_frame));
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Pass the event to ActionBarDrawerToggle, if it returns
		// true, then it has handled the app icon touch event
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle your other action bar items...

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
}
