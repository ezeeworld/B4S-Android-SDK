package com.ezeeworld.b4s.android.sdk.sample;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.ezeeworld.b4s.android.sample.R;
import com.ezeeworld.b4s.android.sdk.B4SSettings;
import com.ezeeworld.b4s.android.sdk.notifications.NotificationService;

public class LaunchActivity extends Activity {
    public static final int PERMISSIONS_REQUEST_LOCATION = 1799;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch);

		if (getIntent().getExtras() != null && getIntent().hasExtra(NotificationService.INTENT_ACTIONID)) {
			String actionId = getIntent().getStringExtra(NotificationService.INTENT_ACTIONID);
			// TODO Do something with the action ID, such as start an activity for a specific product
		}
	}

	@Override
    protected void onResume() {
        super.onResume();

        Log.d("B4S", "Main::OnResume");

        checkPermissions();

        if (B4SSettings.isInitialized()) {
	        Location home = B4SSettings.get().userHomeCoordinates();
	        if (home != null)
	        {
	            Log.d("B4S", "Main::OnResume home="+home.toString());
	        }
	        Location work = B4SSettings.get().userWorkplaceCoordinates();
	        if (work != null)
	        {
	            Log.d("B4S", "Main::OnResume work="+work.toString());
	        }
    	}
    }

    @TargetApi(23)
    protected void checkPermissions() {
        checkLocationPermission();
    }

	private boolean checkLocationPermission() {
        Log.d("B4S", "Main::checkLocationPermission");

        int res = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);

        if (res != PackageManager.PERMISSION_GRANTED) {

            Log.d("B4S", "Main::checkLocationPermission step 1-1 res="+res);
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_LOCATION);

            Log.d("B4S", "Main::checkLocationPermission step 1-2");
            return false;
        } else {
            Log.d("B4S", "Main::checkLocationPermission step 2-1 res="+res);
            return true;
        }
    }
}
