package com.ezeeworld.b4s.android.sdk.sample;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.ezeeworld.b4s.android.sdk.AsyncExecutor;
import com.ezeeworld.b4s.android.sdk.B4SSettings;
import com.ezeeworld.b4s.android.sdk.B4SUserProperty;
import com.ezeeworld.b4s.android.sdk.monitor.LocationAwareThread;
import com.ezeeworld.b4s.android.sdk.notifications.NotificationService;

/**
 * An example application for the B4S SDK that sets up some non-default settings and ensures the monitoring service is properly set up.
 * We implements NotificationModifier to allow dynamic notification update through :
 *   public String modifyNotificationTitle(Bundle extras)
 *  AND
 *   public String modifyNotificationMessage(Bundle extras)
 */
public class SampleApp extends Application implements NotificationService.NotificationModifier {

	private static final String TAG = "B4S";
    public static LocationAwareThread lat = null;

	@Override
	public void onCreate() {
		super.onCreate();

        AsyncExecutor.get().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                try {

                    // Initialize the B4S SDK. The Neerby identifier APP-ID is set in the Manifest file
                    B4SSettings settings = B4SSettings.init(SampleApp.this);

                    // Enable remote push notifications
                    // settings.setPushMessagingSenderId("MY-GOOGLE-SENDER-ID");

                    // Prevent the SDK to be fooled with fake locations sent by a simulator.
                    settings.setDiscardMockLocations(true);

                    settings.setShouldVibrateOnNotification(true);
                    settings.setVibrationPattern(new long[]{100, 300, 100, 300});
                    settings.setShouldRingOnNotification(true);
                    settings.enableNotificationSound();

                    // Send deep links to our broadcast receiver (instead of the default launcher activity delivery)
                    NotificationService.registerDeepLinkStyle(NotificationService.DeepLinkStyle.BroadcastReceiver);

                    B4SUserProperty.get().store(B4SUserProperty.USER_FIRST_NAME, "Jean-Michel");
                    B4SUserProperty.get().store(B4SUserProperty.USER_LAST_NAME, "Bécatresse");
                    B4SUserProperty.get().store(B4SUserProperty.USER_GENDER, B4SUserProperty.Gender.Male);

                    // Start SDK
                    settings.go();

                    lat = LocationAwareThread.get();
                    if (lat == null) {
                        lat = new LocationAwareThread(getApplicationContext());
                    }
                } catch (Exception e) {
                    Log.e("ERROR", "onCreate: Failed to get package manager " + e.getLocalizedMessage() );
                }
            }
        });
	}

    /**
     * This callback is called at notification generation time. It gives opportunity to modify
     * the notification title just before it will be displayed.
     * The extras params can be used to monitor notification activity.
     * You can even change notification icon accordingly to the data values associated to the notification.
     * @param extras
     * @return
     */
    public String modifyNotificationTitle(Bundle extras) {
        Log.d(TAG," >> "+extras.getString(NotificationService.INTENT_TITLE));
        Log.d(TAG," > "+extras.getString(NotificationService.INTENT_MESSAGE));
        Log.d(TAG," > "+extras.getDouble(NotificationService.INTENT_SHOPLATITUDE, 0));
        Log.d(TAG," > "+extras.getDouble(NotificationService.INTENT_SHOPLONGITUDE, 0));
        Log.d(TAG," > "+extras.getInt(NotificationService.INTENT_INTERACTIONRADIUS, 0));
        Log.d(TAG," > "+extras.getString(NotificationService.INTENT_SHOPCITY));
        Log.d(TAG," > "+extras.getString(NotificationService.INTENT_SHOPZIPCODE));
        Log.d(TAG," > "+extras.getString(NotificationService.INTENT_SHOPNAME));
        Log.d(TAG," > "+extras.getString(NotificationService.INTENT_SHOPCLIENTREF));
        Log.d(TAG," > "+extras.getString(NotificationService.INTENT_BEACONID));
        Log.d(TAG," > "+extras.getString(NotificationService.INTENT_BEACONNAME));
        Log.d(TAG," > "+extras.getString(NotificationService.INTENT_BEACONCLIENTREF));
        Log.d(TAG," > "+extras.getString(NotificationService.INTENT_CAMPAIGNNAME));
        Log.d(TAG," > "+extras.getString(NotificationService.INTENT_INTERACTIONNAME));

        /*
          The sample code below show how to customize notifications behaviours just before
          triggering them.
        */
        /*if (extras.getString(NotificationService.INTENT_CAMPAIGNNAME).indexOf("APP") == 0) {
            B4SSettings.get().setShouldVibrateOnNotification(false);
            B4SSettings.get().setCustomNotificationSmallIcon(R.drawable.ic_notifsmall);
            B4SSettings.get().setCustomNotificationLargeIcon(this.getApplicationInfo().icon);

            return "[ALT] "+extras.getString(NotificationService.INTENT_TITLE);
        }
        B4SSettings.get().setShouldVibrateOnNotification(true);
        B4SSettings.get().setNotificationBackgroundColor(0xff111111);
        B4SSettings.get().setCustomNotificationSmallIcon(R.drawable.ic_notifsmall);
        B4SSettings.get().setCustomNotificationLargeIcon(R.drawable.ic_notiflarge);*/

        return extras.getString(NotificationService.INTENT_TITLE);
    }

    /**
     * This callback is called at notification generation time. It gives opportunity to modify
     * the notification message just before it will be displayed.
     * @param extras
     * @return
     */
    public String modifyNotificationMessage(Bundle extras) {
        Log.d("B4S"," >> "+extras.getString(NotificationService.INTENT_MESSAGE));
        return extras.getString(NotificationService.INTENT_MESSAGE);
    }
}
