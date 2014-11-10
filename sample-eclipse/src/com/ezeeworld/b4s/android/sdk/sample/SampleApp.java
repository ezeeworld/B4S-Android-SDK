package com.ezeeworld.b4s.android.sdk.sample;

import com.ezeeworld.b4s.android.sdk.B4SSettings;
import com.ezeeworld.b4s.android.sdk.monitor.MonitoringManager;

import android.app.Application;

public class SampleApp extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		// Initialize the B4S SDK with our app-specific registration ID
		B4SSettings.init(this, "MY-APP-ID", "1");

		// Adjust the settings to our needs
		B4SSettings settings = B4SSettings.from(getApplicationContext());
		settings.setMonitorScanDuration(500); // 500ms scan bursts
		settings.setMonitorHandleInterval(2 * 1000); // Scan for 2 seconds
		settings.setMonitorRangingInterval(5 * 1000); // 5 second interval
		settings.setShouldEnforceBluetooth(true); // Turn on Bluetooth when required for background scanning
		settings.setShouldLogDebug(true);
		settings.setShouldLogVerbose(true);

		// Start the monitoring service, if needed
		MonitoringManager.ensureMonitoringService(this);

	}

}
