package com.ezeeworld.b4s.android.sdk.sample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DeepLinkReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Required as no Activity context is available
		context.startActivity(intent);
	}

}
