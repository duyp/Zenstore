package com.zenstore.order;

import java.util.ArrayList;

import com.zenstore.order.R;
import com.zenstore.order.object.ZenOrder;
import com.zenstore.order.ui.ZenActivity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

public class ZenNotification {

	public static void notifyNewOrders(Context c, ArrayList<ZenOrder> orders) {

		String content = "" + orders.size() + " new orders: \n";
		// for (Order o : orders) {
		// content += o.product + " - " + o.address + " | ";
		// }

		Uri soundUri = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
		Intent intent = new Intent(c, ZenActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pIntent = PendingIntent.getActivity(c, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(c)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("ZENCASE - new orders")
				.setContentText(content)
				.setSound(soundUri)
				.setVibrate(
						new long[] { 1000, 1000, 1000, 1000, 1000, 1000, 1000,
								1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000 })
				.setLights(Color.RED, 1000, 1000).setAutoCancel(true);

		mBuilder.setContentIntent(pIntent);

		NotificationManager mNotifyMgr = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotifyMgr.notify(001, mBuilder.build());
	}
}
