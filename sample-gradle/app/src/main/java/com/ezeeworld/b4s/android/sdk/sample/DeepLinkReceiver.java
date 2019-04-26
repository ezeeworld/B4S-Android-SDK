package com.ezeeworld.b4s.android.sdk.sample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ezeeworld.b4s.android.sdk.notifications.NotificationService;

public class DeepLinkReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
        if (bundle != null) {
        	String campaignName    = (String)bundle.get(NotificationService.INTENT_CAMPAIGNNAME);
            String campaignId      = (String)bundle.get(NotificationService.INTENT_CAMPAIGN);
            String interactionName = (String)bundle.get(NotificationService.INTENT_INTERACTIONNAME);
            String interactionId   = (String)bundle.get(NotificationService.INTENT_INTERACTION);
            String beaconId        = (String)bundle.get(NotificationService.INTENT_BEACONID);
            String beaconName      = (String)bundle.get(NotificationService.INTENT_BEACONNAME);
            String beaconRef       = (String)bundle.get(NotificationService.INTENT_BEACONCLIENTREF);
            String shopId          = (String)bundle.get(NotificationService.INTENT_SHOPID);
            String shopName        = (String)bundle.get(NotificationService.INTENT_SHOPNAME);
            String shopRef         = (String)bundle.get(NotificationService.INTENT_SHOPCLIENTREF);
            String shopCity        = (String)bundle.get(NotificationService.INTENT_SHOPCITY);
            String shopZipCode     = (String)bundle.get(NotificationService.INTENT_SHOPZIPCODE);
            String title           = (String)bundle.get(NotificationService.INTENT_TITLE);
            String message         = (String)bundle.get(NotificationService.INTENT_MESSAGE);
            Long   timeStamp       = (Long)  bundle.get(NotificationService.INTENT_TIMESTAMP);
        }

		Intent start = new Intent(context, LaunchActivity.class);
		start.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		start.putExtra("extraEventId", intent.getStringExtra("b4s_intent_session"));
		context.startActivity(start);
	}

}
