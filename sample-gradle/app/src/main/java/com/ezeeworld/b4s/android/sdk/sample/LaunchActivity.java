package com.ezeeworld.b4s.android.sdk.sample;

import android.app.Activity;
import android.os.Bundle;

import com.ezeeworld.b4s.android.sample.R;
import com.ezeeworld.b4s.android.sdk.notifications.NotificationService;

public class LaunchActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch);

		if (getIntent().getExtras() != null && getIntent().hasExtra(NotificationService.INTENT_ACTIONID)) {
			String actionId = getIntent().getStringExtra(NotificationService.INTENT_ACTIONID);
			// TODO Do something with the action ID, such as start an activity for a specific product
		}
	}

}
