package com.egonny.facepunch.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.egonny.facepunch.FPApplication;
import com.egonny.facepunch.R;
import com.egonny.facepunch.fragments.MenuFragment;
import com.egonny.facepunch.fragments.SubforumFragment;
import com.egonny.facepunch.model.facepunch.FPThread;
import com.egonny.facepunch.model.menu.ActionItem;
import com.egonny.facepunch.model.menu.MenuSubforum;
import com.egonny.facepunch.util.FPParser;

import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity implements MenuFragment.onItemClickListener, SubforumFragment.onItemClickListener {

	private static final String PREFS_NAME = "MainPrefs";
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

	    // Add session cookie to CookieStore
	    try {
		    HttpCookie cookie = new HttpCookie("bb_sessionhash", getSessionHash());
		    FPApplication.getInstance().getManager().getCookieStore()
				         .add(new URI("http://www.facepunch.com/"), cookie);
	    } catch (URISyntaxException e) {

	    }

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
	public void onActionClick(ActionItem.Action action) {
		switch (action) {
			case LOG_IN:
				showLoginDialog();
				break;
			case PM:
				break;
			case POPULAR:
				break;
			case SUBSCRIBED:
				break;
		}
	}

	private void showLoginDialog() {
		// Show login dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// Get the layout inflater
		LayoutInflater inflater = getLayoutInflater();

		builder.setTitle(R.string.login_title);

		final View v = inflater.inflate(R.layout.dialog_login, null);
		builder.setView(v)
				// Add action buttons
				.setPositiveButton(R.string.login_OK, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(final DialogInterface dialog, int id) {
						EditText name = (EditText) v.findViewById(R.id.login_username);
						EditText password = (EditText) v.findViewById(R.id.login_password);

						login(name.getText().toString(), md5(password.getText().toString()));
					}
				})
				.setNegativeButton(R.string.login_cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	private String md5(String s) throws IllegalStateException {
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			byte[] array = md.digest(s.getBytes());
			StringBuffer sb = new StringBuffer();
			for (byte anArray : array) {
				sb.append(Integer.toHexString((anArray & 0xFF) | 0x100).substring(1, 3));
			}
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
			Log.e("NoSuchAlgorithmException", e.getMessage());
		}
		return null;
	}

	private void login(String username, String password) {
		checkLogin(username, password, new LoginCallback() {
			@Override
			public void onResult(boolean success, FPParser.LoginResponse response) {
				if (success) {
					setUsername(response.username);
					Toast.makeText(MainActivity.this, "Successfully logged in!", Toast.LENGTH_SHORT).show();
					mMenuFragment.refreshAccountCategory();
				} else if (response.error == FPParser.Error.INCORRECT_USERNAME) {
					Toast.makeText(MainActivity.this, "Failed to log in, try again later. Retry " + response.retry +" of 5", Toast.LENGTH_SHORT).show();
				} else if (response.error == FPParser.Error.RETRIES_LIMIT_REACHED) {
					Toast.makeText(MainActivity.this, "Failed to log in, max limit of retries reached. Retry in 15 minutes.", Toast.LENGTH_SHORT).show();
				}
			}
		});
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

	public String getSessionHash() {
		return getStringPref("sessionHash");
	}

	public void setSessionHash(String hash) {
		setStringPref("sessionHash", hash);
	}

	public String getUsername() {
		return getStringPref("username");
	}

	public void setUsername(String username) {
		setStringPref("username", username);
	}

	public String getStringPref(String key) {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		return settings.getString(key, "");
	}

	public void setStringPref(String key, String value) {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(key, value);
		editor.commit();
	}

	//FIXME OMG this is ugly what am I doing
	private void checkLogin(final String username, final String password, final LoginCallback callback) {
		StringRequest request = new StringRequest(Request.Method.POST,
				"http://facepunch.com/login.php?do=login",
				new Response.Listener<String>() {
					@Override
					public void onResponse(String s) {
						FPParser.LoginResponse response = FPParser.parseLogin(s);
						try {
							List<HttpCookie> cookies = FPApplication.getInstance().getManager().getCookieStore()
									.get(new URI("http://www.facepunch.com/"));
							for (HttpCookie cookie: cookies) {
								if (cookie.getName().equals("bb_sessionhash")) {
									setSessionHash(cookie.getValue());
									break;
								}
							}
						} catch (URISyntaxException e) {
							Log.e("URISyntaxException", "Could not create URI \"http://www.facepunch.com\"", e);
						}
						callback.onResult(response.error == null, response);
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						callback.onResult(false, null);
					}
				}){

			protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("do", "login");
				params.put("vb_login_username", username);
				params.put("vb_login_md5password", password);
				return params;
			}
		};
		FPApplication.getInstance().addToRequestQueue(request);
	}

	private interface LoginCallback {
		//TODO use onSuccess, onFailure instead
		void onResult(boolean success, FPParser.LoginResponse response);
	}
}
