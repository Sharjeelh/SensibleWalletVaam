package com.cashmobi;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.TextView;

import com.commonutility.GlobalData;
import com.commonutility.GlobalVariables;
import com.commonutility.PreferenceConnector;
import com.commonutility.WebService;
import com.commonutility.WebServiceListener;
import com.commonutility.WebServiceWithoutDialog;
import com.google.android.gcm.GCMRegistrar;
import com.helper.MyUtils;
import com.heyzap.internal.Utils;
import com.material.BaseActivity;

import net.adxmi.android.AdManager;
import net.adxmi.android.os.OffersManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ActivitySplashScreen extends BaseActivity implements WebServiceListener{
	public static final String PREF_DEVICE_ID = "pref_device_id" ;
	Context aiContext;
	GlobalData gd;
	TextView tvLoadPerc;
	int count = 0;
	private Handler customHandler = new Handler();
//	TextView appFirstName,appLastName;
private static final int MY_PERMISSIONS_PHONE_STATE = 101;

	public static final String TAG_RESULT				= "RESULT";
	public static final String TAG_MESSAGE				= "Message";
	public static final String TAG_DATA					= "Data";
	public static final String TAG_GCM_ID				= "gcm_id";
	public static final String TAG_INVATION_CODE		= "invation_code";
	public static final String TAG_IS_ACTIVE			= "is_active";
	public static final String TAG_LAST_LOGIN_DATE		= "last_login_date";
	public static final String TAG_DEVICE_ID			= "device_id";
	public static final String TAG_WALLET_ID			= "wallet_id";
	public static final String TAG_PASSWORD				= "password";
	public static final String TAG_FACBOOK_USER_ID		= "facbook_user_id";
	public static final String TAG_ACTIVATION_CODE		= "activation_code";
	public static final String TAG_IS_USECODE			= "is_usecode";
	public static final String TAG_ID					= "id";
	public static final String TAG_FIRST_NAME			= "first_name";
	public static final String TAG_GOOGLE_USER_ID		= "google_user_id";
	public static final String TAG_PAYPAY_EMAIL			= "paypal_account";
	public static final String TAG_LAST_MODIFIED_TIME	= "last_modified_time";
	public static final String TAG_ENCRYPT				= "encrypt";
	public static final String TAG_REGISTRATION_DATE	= "registration_date";
	public static final String TAG_LAST_NAME			= "last_name";
	public static final String TAG_USER_ID				= "user_id";
	public static final String TAG_POINTS				= "points";
	public static final String TAG_PARENT_ID			= "parent_id";
	public static final String TAG_INVITE_CODE_DISABLE_POINTS	= "invite_code_disable_points";
	public static final String TAG_REWARDS_CHECKIN				= "rewards_checkin";
	public static final String TAG_INVITE_FRIEND_REWARD			= "invite_friend_reward";
	public static final String TAG_INVITE_USER_REWARD			= "invite_user_reward";
	public static final String TAG_DAILY_REWARD_LIMIT			= "daily_reward_limit";
	public static final String TAG_SHARE_TEXT					= "share_text";
	public static final String TAG_DAILY_REWARD_POINTS			= "daily_reward_points";
	public static final String TAG_ASSISTANCE_CONTENT			= "assistance_content";
	public static final String TAG_TERM_CONTENT					= "term_content";
	public static final String TAG_INVITE_TEXT					= "invite_text";
	public static final String TAG_FAQ_CONTENT					= "faq_content";

	public static final String TAG_APP_VERSION					= "app_version";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity);
		checkForPhonePermission(this);






	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}




	public void continueWithApp(){
		MyUtils.setPref(this,PREF_DEVICE_ID,Utils.getDeviceId(this));

		aiContext 	= this;
		gd			= new GlobalData(aiContext);
		gd.setStatusBarColor();
		getGCMRegistrationId();
		tvLoadPerc = (TextView)findViewById(R.id.tv_loadperc);
		customHandler.postDelayed(updateTimerThread, 0);

		final String ADXMI_APPID = getResources().getString(R.string.api_adxmi_app_id);
		final String ADXMI_APPSECRET = getResources().getString(R.string.api_adxmi_app_secret);

		AdManager.getInstance(aiContext).init(ADXMI_APPID, ADXMI_APPSECRET);
		AdManager.getInstance(aiContext).setEnableDebugLog(false);
		OffersManager.getInstance(aiContext).setCustomUserId(PreferenceConnector.readString(aiContext, PreferenceConnector.WALLETID, ""));
		OffersManager.setUsingServerCallBack(true);
		OffersManager.getInstance(aiContext).onAppLaunch();

	}


	private void checkForPhonePermission(Activity context) {
		if (ContextCompat.checkSelfPermission(context,
				android.Manifest.permission.READ_PHONE_STATE)
				!= PackageManager.PERMISSION_GRANTED) {

			// Should we show an explanation?
		/*	if (ActivityCompat.shouldShowRequestPermissionRationale(context,
					android.Manifest.permission.READ_PHONE_STATE)) {

				// Show an expanation to the user *asynchronously* -- don't block
				// this thread waiting for the user's response! After the user
				// sees the explanation, try again to request the permission.

			} else {
*/
				// No explanation needed, we can request the permission.

				ActivityCompat.requestPermissions(context,
						new String[]{android.Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_PHONE_STATE
				);

				// MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
				// app-defined int constant. The callback method gets the
				// result of the request.
			}else{
			continueWithApp();
		}

	}

	@Override
	protected void initViews() {

	}

	boolean counter = true;
	private Runnable updateTimerThread = new Runnable() {
		public void run() {
			counter = !counter;

			if (counter) {
				count++;
				tvLoadPerc.setText((String.format("%02d", count)+"%"));
			}

			customHandler.postDelayed(this, 0);

			if (Integer.parseInt(String.format("%03d", count)) >= 100) {
				customHandler.removeCallbacks(updateTimerThread);

				boolean isRemember = PreferenceConnector.readBoolean(aiContext, PreferenceConnector.ISREMEMBER, false);
				if (isRemember) {
					if (gd.isConnectingToInternet()) {
						String gcmId = PreferenceConnector.readString(aiContext, PreferenceConnector.GSMREGID, "");
						//if (! gcmId.equals(""))
						{
							onLoginAttempt();
						}
					}else {
						GlobalData.showDialog(aiContext, getResources().getString(R.string.error_no_internet), 16, "OK", "");
					}
				}
				else{
					Intent i = new Intent(aiContext, ActivityLogin.class);
					startActivity(i);
					finish();
				}
			}
		}
	};

	public String removeLastCharacter(String str) {
		return str.substring(0,str.length()-1);
	}

	public String getGCMRegistrationId() {
		String regId = "";
		String prefRegIg = PreferenceConnector.readString(aiContext, PreferenceConnector.GSMREGID, "");
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		{
			{
				GCMRegistrar.register(aiContext, GlobalVariables.SENDER_ID);
				//PreferenceConnector.writeString(aiContext, PreferenceConnector.GSMREGID, GCMRegistrar.getRegistrationId(aiContext));
			} 
		}
		return PreferenceConnector.readString(aiContext, PreferenceConnector.GSMREGID, "");
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		customHandler.removeCallbacks(updateTimerThread);
		finish();
	}
	String strUserName, strPassword;
	private void onLoginAttempt() {
		if(TextUtils.isEmpty(MyUtils.getStringPref(this,PREF_DEVICE_ID))) {
			finish();
		}

		strUserName			= PreferenceConnector.readString(aiContext, PreferenceConnector.USERNAME, "");
		strPassword 		= PreferenceConnector.readString(aiContext, PreferenceConnector.PASSWORD, "");


		String[] keys 		= {"email", "password", "device_id", "gcm_id"};

			String devId = MyUtils.getStringPref(this,PREF_DEVICE_ID);
			String[] value = {strUserName, strPassword, devId,
					PreferenceConnector.readString(aiContext, PreferenceConnector.GSMREGID, "")};


		HashMap<String, String> hash	=	new HashMap<String, String>();
		for (int i = 0; i < keys.length; i++) {
			System.out.println(keys[i]+ "......." + value[i]);
			hash.put(keys[i], value[i]);
		}

		if (gd.isConnectingToInternet()) {
			callWebService(GlobalVariables.LOGIN, hash);
		}else {
			GlobalData.showToast(getResources().getString(R.string.error_no_internet), aiContext);
		}
	}

	private void callWebService(String postUrl, HashMap<String, String> hash) {
		WebService webService	=	new WebService(aiContext, "", postUrl, hash, this, WebService.POST);
		webService.execute();
	}


	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if(requestCode==MY_PERMISSIONS_PHONE_STATE){
			if (grantResults.length > 0
					&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				// permission was granted, yay! Do the
				// contacts-related task you need to do.
				continueWithApp();
			} else {

				finish();
				// permission denied, boo! Disable the
				// functionality that depends on this permission.
			}
		}

	}

	@Override
	public void onWebServiceActionComplete(String result, String url) {
		System.out.println(result+".........jsonresponse....."+url);
		if (url.contains(GlobalVariables.LOGIN)) {
			try {
				JSONObject json = new JSONObject(result);
				String str_RESULT = json.getString(TAG_RESULT);
				String str_Message = json.getString(TAG_MESSAGE);
				if (str_RESULT.equals("NO")) {
					GlobalData.showToast(str_Message, aiContext);
					Intent i = new Intent(aiContext, ActivityLogin.class);
					startActivity(i);
					finish();
				}else {
					JSONObject Data_obj 		= json.getJSONObject(TAG_DATA);
					String str_user_id 			= Data_obj.getString(TAG_USER_ID);
					String str_wallet_id 		= Data_obj.getString(TAG_WALLET_ID);
					String str_points 			= Data_obj.getString(TAG_POINTS);
					//	String str_invation_code 		= Data_obj.getString(TAG_INVATION_CODE);
					String str_first_name 			= Data_obj.getString(TAG_FIRST_NAME);
					String str_last_name 			= Data_obj.getString(TAG_LAST_NAME);
					String str_email 				= Data_obj.getString(TAG_PAYPAY_EMAIL);
					String str_invite_code_disable_points 	= Data_obj.getString(TAG_INVITE_CODE_DISABLE_POINTS);
					String str_rewards_checkin 				= Data_obj.getString(TAG_REWARDS_CHECKIN);
					String str_invite_friend_reward 		= Data_obj.getString(TAG_INVITE_FRIEND_REWARD);
					String str_invite_user_reward 			= Data_obj.getString(TAG_INVITE_USER_REWARD);
					String str_daily_reward_limit 			= Data_obj.getString(TAG_DAILY_REWARD_LIMIT);
					String str_daily_reward_points 			= Data_obj.getString(TAG_DAILY_REWARD_POINTS);
					String str_assistance_content 			= Data_obj.getString(TAG_ASSISTANCE_CONTENT);
					String str_invite_text 					= Data_obj.getString(TAG_INVITE_TEXT);
					String str_share_text 					= Data_obj.getString(TAG_SHARE_TEXT);
					String str_term_content 				= Data_obj.getString(TAG_TERM_CONTENT);
					String str_faq_content 					= Data_obj.getString(TAG_FAQ_CONTENT);

					String str_app_version 					= Data_obj.getString(TAG_APP_VERSION);



					PreferenceConnector.writeString(aiContext, PreferenceConnector.USERNAME, strUserName);

					PreferenceConnector.writeString(aiContext, PreferenceConnector.FIRST_NAME, str_first_name);
					PreferenceConnector.writeString(aiContext, PreferenceConnector.LAST_NAME, str_last_name);
					PreferenceConnector.writeString(aiContext, PreferenceConnector.PAYPAL_EMAIL, str_email);

					PreferenceConnector.writeString(aiContext, PreferenceConnector.PASSWORD, strPassword);

					PreferenceConnector.writeString(aiContext, PreferenceConnector.USERID, str_user_id);
					PreferenceConnector.writeString(aiContext, PreferenceConnector.WALLETID, str_wallet_id);
					PreferenceConnector.writeInteger(aiContext, PreferenceConnector.WALLETPOINTS, 
							Integer.parseInt(str_points));
					PreferenceConnector.writeInteger(aiContext, PreferenceConnector.INVITEDISABLEPOINT, 
							Integer.parseInt(str_invite_code_disable_points));
					PreferenceConnector.writeString(aiContext, PreferenceConnector.REWARDCHECKINDATE, str_rewards_checkin);
					PreferenceConnector.writeString(aiContext, PreferenceConnector.INVITEFRIENDREWARD, str_invite_friend_reward);
					PreferenceConnector.writeString(aiContext, PreferenceConnector.INVITEUSERREWARD, str_invite_user_reward);
					PreferenceConnector.writeString(aiContext, PreferenceConnector.DAILYREWARDLIMIT, str_daily_reward_limit);
					PreferenceConnector.writeString(aiContext, PreferenceConnector.DAILYREWARDPOINTS, str_daily_reward_points);
					PreferenceConnector.writeString(aiContext, PreferenceConnector.ASSISTANCECONTENT, str_assistance_content);
					PreferenceConnector.writeString(aiContext, PreferenceConnector.INVITETEXT, str_invite_text);
					PreferenceConnector.writeString(aiContext, PreferenceConnector.SHARETEXT, str_share_text);
					PreferenceConnector.writeString(aiContext, PreferenceConnector.TERMCONTENT, str_term_content);
					PreferenceConnector.writeString(aiContext, PreferenceConnector.FAQCONTEXT, str_faq_content);


					PreferenceConnector.writeString(aiContext, PreferenceConnector.APPVERSION, str_app_version);

					Intent aiIntent = new Intent(aiContext, ActivityMainWallet.class);
					startActivity(aiIntent);
					finish();
				}
			}catch (JSONException e){
				e.printStackTrace();
			}
		}
		
	}
	public static void updateGCMID(Context context){
		String[] keys 		= {"email", "password", "device_id", "gcm_id"};
		String[] value 		= {PreferenceConnector.readString(context, PreferenceConnector.USERNAME, ""), 
				PreferenceConnector.readString(context, PreferenceConnector.PASSWORD, ""), MyUtils.getStringPref(context,PREF_DEVICE_ID),
				PreferenceConnector.readString(context, PreferenceConnector.GSMREGID, "")};

		HashMap<String, String> hash	=	new HashMap<String, String>();
		for (int i = 0; i < keys.length; i++) {
			System.out.println(keys[i]+ "...GCM..." + value[i]);
			hash.put(keys[i], value[i]);
		}
		WebServiceWithoutDialog webService	=	new WebServiceWithoutDialog(context, "", GlobalVariables.LOGIN_GCM_ID, hash, new WebServiceListener() {
			
			@Override
			public void onWebServiceActionComplete(String result, String url) {
				// TODO Auto-generated method stub
				
			}
		}, WebService.POST);
		webService.execute();
	}
}