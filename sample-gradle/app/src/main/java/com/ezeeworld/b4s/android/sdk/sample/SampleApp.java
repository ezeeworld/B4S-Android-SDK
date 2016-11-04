package com.ezeeworld.b4s.android.sdk.sample;

import android.app.Application;

import com.ezeeworld.b4s.android.sdk.B4SSettings;
import com.ezeeworld.b4s.android.sdk.B4SUserProperty;
import com.ezeeworld.b4s.android.sdk.BuildConfig;
import com.ezeeworld.b4s.android.sdk.monitor.MonitoringManager;
import com.ezeeworld.b4s.android.sdk.notifications.NotificationService;

/**
 * An example application for the B4S SDK that sets up some non-default settings and ensures the monitoring service is properly set up.
 */
public class SampleApp extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		// Initialize the B4S SDK with our app-specific registration ID
		B4SSettings settings = B4SSettings.init(this, "MY-APP-ID");

		// Enable remote push notifications
//		settings.setPushMessagingSenderId("MY-GOOGLE-SENDER-ID");

		// Send deep links to our broadcast receiver (instead of the default launcher activity delivery)
		NotificationService.registerDeepLinkStyle(NotificationService.DeepLinkStyle.BroadcastReceiver);

		B4SUserProperty.get().store(B4SUserProperty.USER_FIRST_NAME, "Jean-Michel");
		B4SUserProperty.get().store(B4SUserProperty.USER_LAST_NAME, "Bécatresse");
		B4SUserProperty.get().store(B4SUserProperty.USER_GENDER, B4SUserProperty.Gender.Male);


		// Start the monitoring service, if needed
		MonitoringManager.ensureMonitoringService(this);

	}

}
