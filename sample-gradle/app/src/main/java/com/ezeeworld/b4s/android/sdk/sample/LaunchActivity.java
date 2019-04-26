package com.ezeeworld.b4s.android.sdk.sample;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.HashMap;

import com.ezeeworld.b4s.android.sample.R;
import com.ezeeworld.b4s.android.sdk.B4SLog;
import com.ezeeworld.b4s.android.sdk.B4SSettings;
import com.ezeeworld.b4s.android.sdk.Device;
import com.ezeeworld.b4s.android.sdk.notifications.NotificationService;
import com.ezeeworld.b4s.android.sdk.monitor.NeerbyTag;

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

		NeerByFirebaseMessagingService.initFirebase(this);
	}

	@Override
    protected void onResume() {
        super.onResume();

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

	        int counter=1;
	        HashMap<String, Object> hmap = new HashMap();
            hmap.put("Key1", "LAST");
            hmap.put("Counter", counter++);
            try {
                NeerbyTag.newEvent(getApplicationContext(), "albedo", hmap,  true);
            } catch(Exception e) {
                B4SLog.e("B4S", "Exception e:"+e.toString());
                e.printStackTrace();
            }
    	}
    }

    @TargetApi(23)
    protected void checkPermissions() {
        checkLocationPermission();
    }

	private boolean checkLocationPermission() {

        int res = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);

        if (res != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_LOCATION);

            return false;
        } else {
            return true;
        }
    }
}
