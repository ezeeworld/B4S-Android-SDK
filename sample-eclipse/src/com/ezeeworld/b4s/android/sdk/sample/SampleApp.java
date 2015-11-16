package com.ezeeworld.b4s.android.sdk.sample;

import android.app.Application;

import com.ezeeworld.b4s.android.sdk.B4SAlertBehaviours;
import com.ezeeworld.b4s.android.sdk.B4SSettings;
import com.ezeeworld.b4s.android.sdk.monitor.MonitoringManager;
import com.ezeeworld.b4s.android.sdk.server.InteractionsApi;

/**
 * An example application for the B4S SDK that sets up some non-default settings and ensures the monitoring service is properly set up.
 */
public class SampleApp extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		// Initialize the B4S SDK with our app-specific registration ID
		B4SSettings settings = B4SSettings.init(this, "MY-APP-ID");

		// Enable push messages
		settings.setPushMessagingSenderId("MY-GOOGLE-SENDER-ID");

		// Adjust the settings to our needs
		settings.setShouldEnforceBluetooth(true); // Turn on Bluetooth when required for background scanning (true by default)
		settings.setShouldLogDebug(true);
		settings.setShouldLogVerbose(true);

		// Have the SDK manage warnings for Bluetooth, Play Services installation and Location Services
		B4SAlertBehaviours.get().warnForBluetooth(true, 1, 2, 0); // Skip first time, then every other app launch
		B4SAlertBehaviours.get().warnForGeolocation(true, 0, 1, 2); // Max 2 times
		B4SAlertBehaviours.get().warnForPlayServices(true, 0, 0, 0); // One time only warning

		if (settings.getCustomerFirstName() == null) {
			// Still need to set the customer details
			settings.storeCustomerFields("SampleApp", "", InteractionsApi.B4SGENDER_UNDEFINED, "SampleApp", "Demo", "", "", "", "", "", "", 0);
		}

		// Start the monitoring service, if needed
		MonitoringManager.ensureMonitoringService(this);

	}

}
