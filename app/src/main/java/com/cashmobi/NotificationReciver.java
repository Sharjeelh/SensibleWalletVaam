package com.cashmobi;


import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.commonutility.PreferenceConnector;

import java.util.List;


public class NotificationReciver extends BroadcastReceiver
{
	/**
	 * Reset notification counter on first phase timer
	 */
	public   static final int 	  	  RESET_ON_FIRST_PHASE_CHANGE		=	1;
	/**
	 * Reset notification counter on second phase timer
	 */
	public   static final int 	  	  RESET_ON_SECOND_PHASE_CHANGE		=	2;
	/**
	 * Reset notification counter on both phase timer
	 */
	public   static final int 	  	  RESET_ON_BOTH_PHASE_CHANGE		=	3;
	/**
	 * Reset notification counter on any one phase timer
	 */
	public   static final int 	  	  RESET_ON_ANY_ONE_PHASE_CHANGE		=	4;
	public static final String 				NOTIFICATION_ID			=	"notificationId";
	public static final String 				NOTIFICATION_MASSEGE	=	"notificationmessage";
	public static final String 				NOTIFICATION_TIMER_TIME	=	"notification_timer_time";
	Context 								context;
//	RemoteViews 							contentView;
    NotificationCompat.Builder 				mBuilder;
    private NotificationManager 			mgr 			= 		null;
    public void onReceive(Context con, Intent arg1) 
	{
		context				=		con;
		mgr 				= 		(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//		contentView 		= 		new RemoteViews(context.getPackageName(), R.layout.custom_notification);
//		Notification counter is increment when occurred second phase notification
		if(arg1.getIntExtra(NOTIFICATION_ID, -1)== DailyRewardFragment.PHASE_SECOND_NOTIFICATION_ID)
		{
			switch (DailyRewardFragment.DEFAULT_RESET_COUNTER_CHANGE)
			{
			case RESET_ON_FIRST_PHASE_CHANGE:
				if(PreferenceConnector.readBoolean(context, PreferenceConnector.NOTIFICATION_COUNTER_UPDATED, true))
					DailyRewardFragment.resetNoticationCounter(PreferenceConnector.NOTIFICATOIN_COUNTER, context);
				else
					DailyRewardFragment.saveNoticationCounter(PreferenceConnector.NOTIFICATOIN_COUNTER, context);
				break;
			case RESET_ON_SECOND_PHASE_CHANGE:
				if(arg1.getIntExtra(NOTIFICATION_TIMER_TIME, -1)==PreferenceConnector.readInteger(context, PreferenceConnector.LAST_NOTIFICATION_TIMER_SECOND_PHASE_TIME, 0))
					DailyRewardFragment.saveNoticationCounter(PreferenceConnector.NOTIFICATOIN_COUNTER, context);
				else
					DailyRewardFragment.resetNoticationCounter(PreferenceConnector.NOTIFICATOIN_COUNTER, context);
				break;
			case RESET_ON_BOTH_PHASE_CHANGE:
				if(PreferenceConnector.readBoolean(context, PreferenceConnector.NOTIFICATION_COUNTER_UPDATED, true))
				{
					if(arg1.getIntExtra(NOTIFICATION_TIMER_TIME, -1)==PreferenceConnector.readInteger(context, PreferenceConnector.LAST_NOTIFICATION_TIMER_SECOND_PHASE_TIME, 0))
						DailyRewardFragment.saveNoticationCounter(PreferenceConnector.NOTIFICATOIN_COUNTER, context);
					else
						DailyRewardFragment.resetNoticationCounter(PreferenceConnector.NOTIFICATOIN_COUNTER, context);
				}
				else
					DailyRewardFragment.saveNoticationCounter(PreferenceConnector.NOTIFICATOIN_COUNTER, context);
				break;
			case RESET_ON_ANY_ONE_PHASE_CHANGE:
				if(PreferenceConnector.readBoolean(context, PreferenceConnector.NOTIFICATION_COUNTER_UPDATED, true))
					DailyRewardFragment.saveNoticationCounter(PreferenceConnector.NOTIFICATOIN_COUNTER, context);
				else
				{
					if(arg1.getIntExtra(NOTIFICATION_TIMER_TIME, -1)==PreferenceConnector.readInteger(context, PreferenceConnector.LAST_NOTIFICATION_TIMER_SECOND_PHASE_TIME, 0))
						DailyRewardFragment.saveNoticationCounter(PreferenceConnector.NOTIFICATOIN_COUNTER, context);
					else
						DailyRewardFragment.resetNoticationCounter(PreferenceConnector.NOTIFICATOIN_COUNTER, context);
				}
				break;
			}
			DailyRewardFragment.saveTimeOnNotificationSave(PreferenceConnector.LAST_NOTIFICATION_TIMER_FIRST_PHASE_TIME, PreferenceConnector.LAST_NOTIFICATION_CREATE_TIMER_FIRST_PHASE_TIME);
			DailyRewardFragment.saveTimeOnNotificationSave(PreferenceConnector.LAST_NOTIFICATION_TIMER_SECOND_PHASE_TIME, PreferenceConnector.LAST_NOTIFICATION_CREATE_TIMER_SECOND_PHASE_TIME);
			if(isRunning(context))
			{
				// set counter value
				DailyRewardFragment.setNoticationCounterDefault();
				// set last completion  timer value
				DailyRewardFragment.setLastSuccessfullyTimerTime();
			}
		
		}
		else
		{
			switch (DailyRewardFragment.DEFAULT_RESET_COUNTER_CHANGE)
			{
			case RESET_ON_FIRST_PHASE_CHANGE:
				if(arg1.getIntExtra(NOTIFICATION_TIMER_TIME, -1)==PreferenceConnector.readInteger(context, PreferenceConnector.LAST_NOTIFICATION_TIMER_FIRST_PHASE_TIME, 0))
					PreferenceConnector.writeBoolean(con, PreferenceConnector.NOTIFICATION_COUNTER_UPDATED, false);
				else
					PreferenceConnector.writeBoolean(con, PreferenceConnector.NOTIFICATION_COUNTER_UPDATED, true);
				break;
			case RESET_ON_SECOND_PHASE_CHANGE:
					PreferenceConnector.writeBoolean(con, PreferenceConnector.NOTIFICATION_COUNTER_UPDATED, true);
				break;
			case RESET_ON_BOTH_PHASE_CHANGE:
				if(arg1.getIntExtra(NOTIFICATION_TIMER_TIME, -1)==PreferenceConnector.readInteger(context, PreferenceConnector.LAST_NOTIFICATION_TIMER_FIRST_PHASE_TIME, 0))
					PreferenceConnector.writeBoolean(con, PreferenceConnector.NOTIFICATION_COUNTER_UPDATED, false);
				else
					PreferenceConnector.writeBoolean(con, PreferenceConnector.NOTIFICATION_COUNTER_UPDATED, true);
				break;
			case RESET_ON_ANY_ONE_PHASE_CHANGE:
				if(arg1.getIntExtra(NOTIFICATION_TIMER_TIME, -1)==PreferenceConnector.readInteger(context, PreferenceConnector.LAST_NOTIFICATION_TIMER_FIRST_PHASE_TIME, 0))
					PreferenceConnector.writeBoolean(con, PreferenceConnector.NOTIFICATION_COUNTER_UPDATED, false);
				else
					PreferenceConnector.writeBoolean(con, PreferenceConnector.NOTIFICATION_COUNTER_UPDATED, true);
				break;
			}
			
		}
		/*Intent 	notificationIntent 					= 	new Intent(context, MainActivity.class);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent contentIntent 				= 	PendingIntent.getActivity(context, arg1.getIntExtra(NOTIFICATION_ID, -1), notificationIntent, 0);
		mBuilder = new NotificationCompat.Builder( context).setSmallIcon(R.drawable.ic_launcher).setContent(contentView);
		contentView.setImageViewResource(R.id.image, R.drawable.ic_launcher);
		contentView.setTextViewText(R.id.Tital, "Phase");
		contentView.setTextViewText(R.id.message, arg1.getStringExtra(NOTIFICATION_MASSEGE));
		mBuilder.setSound(Uri.parse("android.resource://" + PACKAGE + "/raw/"+"timeout"));
//		mBuilder.setContent(contentView);
		mBuilder.setContentTitle("Phase");
		mBuilder.setContentText(arg1.getStringExtra(NOTIFICATION_MASSEGE));
		mBuilder.setContentIntent(contentIntent);
		mBuilder.setAutoCancel(true);
		mBuilder.setWhen(System.currentTimeMillis());
		mBuilder.setLights(Color.BLUE, 500, 500);
		mgr.notify(arg1.getIntExtra(NOTIFICATION_ID, -1), mBuilder.build()); */
		
	}
	 public boolean isRunning(Context ctx) 
	 {
	        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
	        List<RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);
	       
	        for (RunningTaskInfo task : tasks)
	        {
	            if ((ctx.getPackageName()+".MainActivity").equalsIgnoreCase(task.baseActivity.getClassName())) 
	                return true;                                  
	        }

	        return false;
	    }

}

