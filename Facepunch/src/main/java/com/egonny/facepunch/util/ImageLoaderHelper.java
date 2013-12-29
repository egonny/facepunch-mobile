package com.egonny.facepunch.util;

import android.graphics.Bitmap;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.egonny.facepunch.FPApplication;

/**
 * Courtesy of Jesse Farebrother
 * http://stackoverflow.com/questions/17336434/instantiating-core-volley-objects
 */
public class ImageLoaderHelper {
	private static ImageLoaderHelper sInstance;

	private final ImageLoader mImageLoader;
	private final ImageLoader.ImageCache mImageCache;

	public static ImageLoaderHelper getInstance() {
		if (sInstance == null)
			sInstance = new ImageLoaderHelper();
		return sInstance;
	}

	public ImageLoaderHelper() {
		mImageCache = new BitmapLruCache();
		mImageLoader = new ImageLoader(FPApplication.getInstance().getRequestQueue(),mImageCache);
	}

	public ImageLoader getImageLoader() {
		return mImageLoader;
	}

	public interface ImageCallback {
		void onResult(boolean success, Bitmap bitmap);
	}

	public void getBitmap(String url, final ImageCallback callback) {
		mImageLoader.get(url, new ImageLoader.ImageListener() {
			@Override
			public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
					callback.onResult(true, imageContainer.getBitmap());
			}

			@Override
			public void onErrorResponse(VolleyError volleyError) {
				callback.onResult(false, null);
			}
		});
	}
}
