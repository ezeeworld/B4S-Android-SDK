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

/**
 * An example receiver of interaction matches that are not handled by the SDK. This gives full flexibility on the performed action, but no
 * automatic generation of popups and notifications is provided. This example just shows a notification with the supplied data.
 */
public class B4SNotificationReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent == null || intent.getAction() == null || intent.getExtras() == null)
			return;

		// Message that a new notification should be displayed?
		if (intent.hasExtra(MonitoringManager.INTENT_SHOW)) {
			// See GitHub for all data that is provided in the Intent extras
			showNotification(context, intent.getIntExtra(MonitoringManager.INTENT_SHOW, -1),
					intent.getStringExtra(MonitoringManager.INTENT_TITLE),
					intent.getStringExtra(MonitoringManager.INTENT_MESSAGE),
					intent.getStringExtra(MonitoringManager.INTENT_DATA));
		}

	}

	private void showNotification(Context context, int id, String title, String message, String data) {

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
