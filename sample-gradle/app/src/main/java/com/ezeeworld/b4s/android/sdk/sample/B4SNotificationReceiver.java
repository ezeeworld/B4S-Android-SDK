package com.ezeeworld.b4s.android.sdk.sample;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ezeeworld.b4s.android.sample.R;
import com.ezeeworld.b4s.android.sdk.monitor.MonitoringManager;

public class B4SNotificationReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent == null || intent.getAction() == null || intent.getExtras() == null)
			return;

		// Message that a sticky notification should be removed?
		if (intent.hasExtra(MonitoringManager.INTENT_REMOVE)) {
			cancelNotification(context, intent.getIntExtra(MonitoringManager.INTENT_REMOVE, -1));
		}

		// Message that a new notification should be displayed?
		if (intent.hasExtra(MonitoringManager.INTENT_SHOW)) {
			showNotification(context, intent.getIntExtra(MonitoringManager.INTENT_SHOW, -1),
					intent.getStringExtra(MonitoringManager.INTENT_TITLE),
					intent.getStringExtra(MonitoringManager.INTENT_MESSAGE),
					intent.getStringExtra(MonitoringManager.INTENT_DATA));
		}

	}

	private void cancelNotification(Context context, int id) {

		((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(id);

	}

	private void showNotification(Context context, int id, String title, String message, String data) {

		Log.i("B4S", "Showing notification " + id + ": " + message);
		NotificationManager notifications = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		PendingIntent action = PendingIntent.getActivity(context, id, new Intent(context, LaunchActivity.class),
				PendingIntent.FLAG_UPDATE_CURRENT);

		Notification.Builder builder = new Notification.Builder(context).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(title).setContentIntent(action).setContentText(message)
				.setStyle(new Notification.BigTextStyle().bigText(message)).setAutoCancel(true)
				.setVibrate(new long[] { 0, 500 });
		notifications.notify(id, builder.build());

	}
}
