package com.ezeeworld.b4s.android.sdk.sample;

import android.os.Bundle;

import com.ezeeworld.b4s.android.sample.R;
import com.ezeeworld.b4s.android.sdk.notifications.B4SNotificationActivity;

public class LaunchActivity extends B4SNotificationActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch);
	}

}
