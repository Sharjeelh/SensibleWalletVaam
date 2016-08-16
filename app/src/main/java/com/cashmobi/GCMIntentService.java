package com.cashmobi;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;

import com.commonutility.GlobalVariables;
import com.commonutility.PreferenceConnector;
import com.google.android.gcm.GCMBaseIntentService;

import java.util.concurrent.atomic.AtomicInteger;

public class GCMIntentService extends GCMBaseIntentService {
	private static final String TAG 	= "GCMIntentService";
	private final static AtomicInteger c = new AtomicInteger(0);

	public GCMIntentService() {
		super(GlobalVariables.SENDER_ID);
	}

	/**
	 * Method called on device registered
	 **/
	@Override
	protected void onRegistered(Context context, String registrationId) {
		Log.i(TAG, "Device registered: regId = " + registrationId);
		PreferenceConnector.writeString(context, PreferenceConnector.GSMREGID, registrationId);
		if(!PreferenceConnector.readString(context, PreferenceConnector.USERNAME, "").equals("") && !PreferenceConnector.readString(context, PreferenceConnector.PASSWORD, "").equals(""))
		ActivitySplashScreen.updateGCMID(context);
	}

	/**
	 * Method called on device un registred
	 * */
	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Log.i(TAG, "Device unregistered");
	}

	/**
	 * Method called on Receiving a new message
	 * */
	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.i(TAG, "Received message");
		String message 		= intent.getExtras().getString("message");
		 String type 		= intent.getExtras().getString("type");
 		String userpoints 	= intent.getExtras().getString("userpoints");
		System.out.println(userpoints + "........message.,............" + type);
		if (message != null) {
			//if(type!=null && type.equals("confirm"))
			{
				ActivityMainWallet.displayMessage(context, message,userpoints);
			}
			sendSimpleNotification(context, message);
		}
	}

	/**
	 * Method called on Error
	 * */
	@Override
	public void onError(Context context, String errorId) {
		Log.i(TAG, "Received error: " + errorId);
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		// log message
		Log.i(TAG, "Received recoverable error: " + errorId);
		return super.onRecoverableError(context, errorId);
	}

	@SuppressLint("NewApi")
	public void sendSimpleNotification(Context context, String message) {
		Intent intent = new Intent(context, ActivityMainWallet.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);
		// Build notification
		// Actions are just fake
		Notification noti = new Notification.Builder(context)
		.setContentTitle(context.getString(R.string.app_name))
		.setContentText(Html.fromHtml(message))
		.setSmallIcon(getNotificationIcon())
		.setContentIntent(pIntent)
		.build();

		noti.defaults |= Notification.DEFAULT_SOUND;
		noti.defaults |= Notification.DEFAULT_VIBRATE;

		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		// Hide the notification after its selected
		noti.flags |= Notification.FLAG_AUTO_CANCEL;
		
		notificationManager.notify(c.incrementAndGet(), noti);
	}

	private int getNotificationIcon() {
		boolean whiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
		return whiteIcon ? R.drawable.ic_notif_lollipop : R.drawable.ic_launcher;
	}
}
