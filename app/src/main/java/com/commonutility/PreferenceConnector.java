package com.commonutility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferenceConnector{
	public static final String 	PREF_NAME 				= "app_prefrences";
	public static final int 	MODE 					= Context.MODE_PRIVATE;

	public static final String  GSMREGID 				= "gcmid";
	public static final String  SEARCHTEXT 				= "searchtext";
	public static final String  USERNAME 				= "username";
	public static final String  FIRST_NAME 				= "first_name";
	public static final String  LAST_NAME 				= "last_name";
	public static final String  PAYPAL_EMAIL			= "paypal_account";
	public static final String  PASSWORD 				= "password";
	public static final String  USERID 					= "userid";
	public static final String  WALLETPOINTS 			= "walletpoin";
	public static final String  WALLETID 				= "walletid";
	public static final String  COUNTRYSELECTED 		= "countryselected";
	public static final String  COUNTRYSYMBOL 			= "countrysymbol";
	public static final String  COUNTRYID 				= "countryid";
	public static final String  COUNTRYLOGO 			= "countryimage";
	public static final String  INVITEDISABLEPOINT 		= "invitedisablepoint";
	public static final String  REWARDCHECKINDATE		= "rewardcheckin";
	public static final String  INVITEFRIENDREWARD 		= "invitefriendreward";
	public static final String 	RATEREWARDPOINTS = "raterewardpoints";
	public static final String  DAILYREWARDPOINTS 		= "dailyrewardpoints";
	public static final String  DAILYREWARDLIMIT 		= "dailyrewardlimit";
	public static final String  ASSISTANCECONTENT 		= "assistancecontent";
	public static final String  INVITEUSERREWARD 		= "inviteuserreward";
	public static final String  INVITETEXT 				= "invitetext";
	public static final String  SHARETEXT 				= "sharetext";
	public static final String  FAQCONTEXT 				= "faqtext";
	public static final String  TERMCONTENT 			= "termcontent";
	public static final String  ISREMEMBER 				= "isremember";

	public static final String  ISFBLOGIN 				= "isfacebooklogin";
	public static final String  ISGPLOGIN 				= "isgooglepluslogin";

	public static final String  FBFNAME 				= "fbfname";
	public static final String  FBLNAME 				= "fblname";
	public static final String  FBEMAIL 				= "fbemail";

	public static final String  GPFNAME 				= "gpfname";
	public static final String  GPLNAME 				= "gplname";
	public static final String  GPEMAIL 				= "gpemail";
	
	public static final String  APPVERSION 				= "app_version";

    //Version 2.1
    public static final String INPUT_INTIVTE_CODE_COMPLETED = "inputinvitecodecompleted";
    public static final String RATE_APP_COMPLETED = "rateappcompleted";


    public static final String NOTIFICATOIN_COUNTER	 							= "notification_counter";
	public static final String TIMER_FIRST_TIME 								= "timer_first_time";
	public static final String TIMER_SECOND_TIME 								= "timer_second_time";
	public static final String LAST_NOTIFICATION_CREATE_TIMER_FIRST_PHASE_TIME 	= "last_notification_create_timer_first_time";
	public static final String LAST_NOTIFICATION_CREATE_TIMER_SECOND_PHASE_TIME = "last_notification_create_timer_second_time";
	public static final String LAST_NOTIFICATION_TIMER_FIRST_PHASE_TIME 		= "last_notification_timer_first_time";
	public static final String LAST_NOTIFICATION_TIMER_SECOND_PHASE_TIME 		= "last_notification_timer_second_time";
	public static final String NOTICATION_FIRST_PHASE_TIME 						= "notification_first_phase_time";
	public static final String NOTICATION_SECOND_PHASE_TIME 					= "notification_second_phase_time";
	public static final String NOTIFICATION_COUNTER_UPDATED		 				= "notification_counter_update";

	public static final String WELCOME_DIALOG_SHOWN = "welomeDialogShownPreference";

	public static void writeBoolean(Context context, String key, boolean value) {
		getEditor(context).putBoolean(key, value).commit();
	}

	public static boolean readBoolean(Context context, String key, boolean defValue) {
		return getPreferences(context).getBoolean(key, defValue);
	}

	public static void writeInteger(Context context, String key, int value) {
		getEditor(context).putInt(key, value).commit();
	}

	public static int readInteger(Context context, String key, int defValue) {
		return getPreferences(context).getInt(key, defValue);
	}

	public static void writeString(Context context, String key, String value) {
		getEditor(context).putString(key, value).commit();
		System.out.println(key+"......prefvalue........"+value);
	}

	public static String readString(Context context, String key, String defValue) {
		return getPreferences(context).getString(key, defValue);
	}

	public static void writeFloat(Context context, String key, float value) {
		getEditor(context).putFloat(key, value).commit();
	}

	public static float readFloat(Context context, String key, float defValue) {
		return getPreferences(context).getFloat(key, defValue);
	}

	public static void writeLong(Context context, String key, long value) {
		getEditor(context).putLong(key, value).commit();
	}

	public static long readLong(Context context, String key, long defValue) {
		return getPreferences(context).getLong(key, defValue);
	}

	public static SharedPreferences getPreferences(Context context) {
		return context.getSharedPreferences(PREF_NAME, MODE);
	}

	public static Editor getEditor(Context context) {
		return getPreferences(context).edit();
	}

	public static void cleanPrefrences(Context context){
		getPreferences(context).edit().clear().commit();
	}
}
