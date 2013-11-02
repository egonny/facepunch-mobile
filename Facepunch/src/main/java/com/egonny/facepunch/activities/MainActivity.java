package com.egonny.facepunch.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.View;
import com.egonny.facepunch.R;
import com.egonny.facepunch.fragments.MenuFragment;

public class MainActivity extends Activity {

	private MenuFragment mMenuFragment;
	private Fragment mMainFragment;

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
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		return super.onPrepareOptionsMenu(menu);
	}
    
}
