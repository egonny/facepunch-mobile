import android.app.Application;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class FPApplication extends Application {
	private static RequestQueue mRequestQueue;

	@Override
	public void onCreate() {
		super.onCreate();
		mRequestQueue = Volley.newRequestQueue(getApplicationContext());
	}

	public static RequestQueue getRequestQueue() {
		return mRequestQueue;
	}
}
