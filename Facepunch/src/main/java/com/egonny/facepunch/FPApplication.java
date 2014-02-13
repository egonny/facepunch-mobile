package com.egonny.facepunch;

import android.app.Application;
import android.text.TextUtils;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;

import java.net.CookieHandler;
import java.net.CookieManager;

/**
 * Credit to Arnab Chakraborty for most of this code.
 * See http://arnab.ch/blog/2013/08/asynchronous-http-requests-in-android-using-volley/
 */
public class FPApplication extends Application {

	/**
	 * Log or request TAG
	 */
	public static final String TAG = "VolleyPatterns";

	/**
	 * Global request queue for Volley
	 */
	private RequestQueue mRequestQueue;

	/**
	 * Global cookie manager for HTTP requests
	 */
	private CookieManager mManager;

	/**
	 * A singleton instance of the application class for easy access in other places
	 */
	private static FPApplication sInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		sInstance = this;
		mManager = new CookieManager();
		CookieHandler.setDefault(mManager);
	}

	/**
	 * @return FPApplication singleton instance
	 */
	public static synchronized FPApplication getInstance() {
		return sInstance;
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	/**
	 * @return Global cookie manager
	 */
	public CookieManager getManager() {
		return mManager;
	}

	/**
	 * Adds the specified request to the global queue, if tag is specified
	 * then it is used else Default TAG is used.
	 *
	 * @param req
	 * @param tag
	 */
	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

		VolleyLog.d("Adding request to queue: %s", req.getUrl());

		getRequestQueue().add(req);
	}

	/**
	 * Adds the specified request to the global queue using the Default TAG.
	 *
	 * @param req
	 */
	public <T> void addToRequestQueue(Request<T> req) {
		// set the default tag if tag is empty
		req.setTag(TAG);

		getRequestQueue().add(req);
	}

	/**
	 * Cancels all pending requests by the specified TAG, it is important
	 * to specify a TAG so that the pending/ongoing requests can be cancelled.
	 *
	 * @param tag
	 */
	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}
}
