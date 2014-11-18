package com.ezeeworld.b4s.android.sdk.sample;

import com.ezeeworld.b4s.android.sdk.B4SSettings;
import com.ezeeworld.b4s.android.sdk.monitor.MonitoringManager;

import android.app.Application;

/**
 * An example application for the B4S SDK that sets up some non-default settings and ensures the monitoring service is properly set up.
 */
public class SampleApp extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		// Initialize the B4S SDK with our app-specific registration ID
		B4SSettings.init(this, "MY-APP-ID", "1");

		// Adjust the settings to our needs
		B4SSettings settings = B4SSettings.from(getApplicationContext());
		settings.setShouldEnforceBluetooth(true); // Turn on Bluetooth when required for background scanning (true by default)
		settings.setShouldLogDebug(true);
		settings.setShouldLogVerbose(true);

		// Start the monitoring service, if needed
		MonitoringManager.ensureMonitoringService(this);

	}

}
