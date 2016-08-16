package com.cashmobi;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.archiveinfotech.crashreport.Utils;
import com.commonutility.GlobalData;
import com.commonutility.GlobalVariables;
import com.commonutility.PreferenceConnector;
import com.commonutility.WebService;
import com.commonutility.WebServiceListener;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.helper.MyUtils;
import com.material.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

/*import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.UserInfoChangedCallback;*/

public class ActivityLogin extends BaseActivity implements OnClickListener, WebServiceListener,ConnectionCallbacks, OnConnectionFailedListener {
	private Context aiContext;
	private EditText editUserNameLogIn, editPasswordLogIn;
	private Button btnLogin;
	private GlobalData gd;
	private TextView txtForgotPwd, txtSignUp;
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


	//private UiLifecycleHelper uiHelper;
	private CallbackManager mCallbackManager;
	//private LoginButton loginBtn;

	private LoginButton loginBtn;
//	private CheckBox radioRemember;

	private static final int RC_SIGN_IN = 0;
	// Logcat tag
	private static final String TAG = "ActivityLogin";

	// Profile pic image size in pixels
	private static final int PROFILE_PIC_SIZE = 400;

	// Google client to interact with Google API
	private GoogleApiClient mGoogleApiClient;

	/**
	 * A flag indicating that a PendingIntent is in progress and prevents us
	 * from starting further intents.
	 */
	private boolean mIntentInProgress;

	private boolean mSignInClicked;

	private ConnectionResult mConnectionResult;

	private SignInButton btnSignIn;

//	TextView appFirstName,appLastName;
	private String strFname = "", strLname = "", strEmailID = "", strConfirmPassword = "", strRegisterType="";
	private String mGooglePlusButtonText;
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FacebookSdk.sdkInitialize(this);

		if(getString(R.string.enable_email).equals("true")) {
			setContentView(R.layout.login_activity);
			mGooglePlusButtonText = getString(R.string.screen_login_button_google_login);
		}else{
			setContentView(R.layout.login_activity_no_email);
			mGooglePlusButtonText = getString(R.string.screen_login_button_google_login_long);
		}

		aiContext	    	= this;
		gd					= new GlobalData(aiContext);
		gd.setStatusBarColor();
		/*ViewGroup viewGroup = (ViewGroup)((ViewGroup)this.findViewById(android.R.id.content)).getChildAt(0);
		MyUtils.setFontAllView(viewGroup);*/
		ViewGroup viewGroup = (ViewGroup)this.findViewById(R.id.mainView);
		Utils.setFontAllView(viewGroup);

		//uiHelper 			= new UiLifecycleHelper(this, callback);
		mCallbackManager = CallbackManager.Factory.create();
		//uiHelper.onCreate(savedInstanceState);

		editUserNameLogIn	= (EditText)findViewById(R.id.et_usernamelogin);
		editPasswordLogIn	= (EditText)findViewById(R.id.et_passwordlogin);
		txtForgotPwd 		= (TextView)findViewById(R.id.tv_forgotpwd);
		txtSignUp 			= (TextView)findViewById(R.id.txt_signup);
		btnLogin 			= (Button)findViewById(R.id.btn_login);

		txtForgotPwd.setOnClickListener(this);
		txtSignUp.setOnClickListener(this);
		btnLogin.setOnClickListener(this);
//		radioRemember		= (CheckBox)findViewById(R.id.radio_remember);

//		appFirstName= (TextView)findViewById(R.id.app_name);
//		appLastName= (TextView)findViewById(R.id.tv_app_name_sub);
//		MyUtils.firstText(appFirstName);
//		MyUtils.lastName(appLastName);

		btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
		btnSignIn.setOnClickListener(this);
		btnSignIn.setStyle(SignInButton.SIZE_STANDARD, SignInButton.COLOR_DARK);

		mGoogleApiClient = new GoogleApiClient.Builder(this)
		.addConnectionCallbacks(this)
		.addOnConnectionFailedListener(this).addApi(Plus.API)
		.addScope(Plus.SCOPE_PLUS_LOGIN).build();
		setGooglePlusButtonText(btnSignIn, mGooglePlusButtonText);

		loginBtn = (LoginButton)findViewById(R.id.authButton);
		//loginBtn.setOnClickListener(this);
        loginBtn.setTextSize(17);
//        loginBtn.setTextAppearance(R.style.TextAppearance_AppCompat_Widget_Button);
		/*loginBtn.setReadPermissions(Arrays.asList("public_profile", "email", "user_friends", "user_hometown",
				"user_location", "user_work_history", "user_website"));*/
		loginBtn.setReadPermissions(Arrays.asList("public_profile", "email"));
		loginBtn.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult loginResult) {
				GraphRequest request = GraphRequest.newMeRequest(
						loginResult.getAccessToken(),
						new GraphRequest.GraphJSONObjectCallback() {
							@Override
							public void onCompleted(JSONObject object, GraphResponse response) {
								Log.v("LoginActivity", response.toString());

								// Application code
								try {
									PreferenceConnector.writeString(aiContext, PreferenceConnector.FBEMAIL, object.getString("email"));
									PreferenceConnector.writeString(aiContext, PreferenceConnector.FBFNAME,object.getString("first_name"));
									PreferenceConnector.writeString(aiContext, PreferenceConnector.FBLNAME, object.getString("last_name"));

									/*String email = object.getString("email");
									String birthday = object.getString("birthday"); // 01/31/1980 format
*/
								} catch (JSONException e) {
									e.printStackTrace();
									GlobalData.showToast(aiContext.getResources().getString(R.string.error_facebook_email_unverified), aiContext);

								}
								PreferenceConnector.writeBoolean(aiContext, PreferenceConnector.ISFBLOGIN, true);
								onRegister();
							}
						});
				Bundle parameters = new Bundle();
				parameters.putString("fields", "id,first_name,last_name,email");
				request.setParameters(parameters);
				request.executeAsync();
			}

			@Override
			public void onCancel() {

			}


			@Override
			public void onError(FacebookException error) {

			}
		});
	/*	loginBtn.setUserInfoChangedCallback(new UserInfoChangedCallback() {
			@Override
			public void onUserInfoFetched(GraphUser user) {

				if (user != null) {


//					System.out.println(user + "::::user");
					System.out.println(Session.getActiveSession().getPermissions());

					if (user.getProperty("email") != null) {
						PreferenceConnector.writeString(aiContext, PreferenceConnector.FBFNAME, user.getFirstName());
						PreferenceConnector.writeString(aiContext, PreferenceConnector.FBLNAME, user.getLastName());
						PreferenceConnector.writeString(aiContext, PreferenceConnector.FBEMAIL, user.getProperty("email").toString());
					} else {
						GlobalData.showToast(aiContext.getResources().getString(R.string.error_facebook_email_unverified), aiContext);
					}

					Session.getActiveSession().closeAndClearTokenInformation();

					PreferenceConnector.writeBoolean(aiContext, PreferenceConnector.ISFBLOGIN, true);
					onRegister();
				} else {
					//	GlobalData.showToast("You are not logged in.", mContext);
				}
			}
		});*/

		PreferenceConnector.writeBoolean(aiContext, PreferenceConnector.WELCOME_DIALOG_SHOWN, false);
	}

	@Override
	protected void initViews() {

	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			hideKeyboard();
			if (gd.isConnectingToInternet()) {
				String gcmId = PreferenceConnector.readString(aiContext, PreferenceConnector.GSMREGID, "");
				if (! gcmId.equals(""))
				{
					onLoginAttempt();
				}
			}else {
				GlobalData.showDialog(aiContext, getResources().getString(R.string.error_no_internet), 16, "OK", "");
			}
			break;
		case R.id.txt_signup:
			hideKeyboard();
			Intent aiIntent = new Intent(aiContext, ActivityRegister.class);
			startActivity(aiIntent);
			finish();
			break;
		case R.id.tv_forgotpwd:
			hideKeyboard();

			showForgetPasswordDialog(aiContext, getResources().getString(R.string.prompt_forgot_password), 20, "Reset Password", "");
			break;
		case R.id.btn_sign_in:
			signInWithGplus();
			break;
		/*	case R.id.authButton:
				Session.getActiveSession().openActiveSession(this, true, Arrays.asList("public_profile", "email"), callback);*/
				//break;
		default:
			break;
		}
	}

	String strForgePasswordEmail;
	EditText editRememberPwd;
	private void showForgetPasswordDialog(final Context aiContext, String text, int textSize, 
			String btnOneText, String btnTwoText) {
		final Dialog dialog;
		dialog = new Dialog(aiContext);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_forgot_password);
		Utils.setFontAllView((ViewGroup)dialog.findViewById(R.id.mainView));

		editRememberPwd			= (EditText)dialog.findViewById(R.id.et_usernameforget);
		TextView textHead 		= (TextView)dialog.findViewById(R.id.text);
		Button btnfirst 		= (Button)dialog.findViewById(R.id.btnfirst);

		textHead.setTextSize(textSize);
		textHead.setText(text);

		btnfirst.setText(btnOneText);
		btnfirst.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (gd.isConnectingToInternet()) {
					onResetAttempt(dialog);
				} else {
					GlobalData.showDialog(aiContext, getResources().getString(R.string.error_no_internet), 16, "OK", "");
				}
			}
		});

		dialog.show();
	}

	private void onResetAttempt(Dialog dialog) {
		int response 	= 0;
		response		= gd.emptyEditTextError(
				new EditText[]{editRememberPwd},
				new String[]{ getResources().getString(R.string.error_login_empty_email) });

		if (! GlobalData.isEmailValid(editRememberPwd.getText().toString().trim())) {
			response++;
			editRememberPwd.setError(getResources().getString(R.string.error_login_invalid_email));
		}

		if(response == 0) {
			dialog.dismiss();

			strForgePasswordEmail = editRememberPwd.getText().toString().trim();

			String[] keys 		= {"email"};
			String[] value 		= {strForgePasswordEmail};

			HashMap<String, String> hash	=	new HashMap<String, String>();
			for (int i = 0; i < keys.length; i++) {
				System.out.println(keys[i]+ "......." + value[i]);
				hash.put(keys[i], value[i]);
			}

			if (gd.isConnectingToInternet()) {
				callWebService(GlobalVariables.FORGOTPASSWORD, hash);
			}else {
				GlobalData.showToast(getResources().getString(R.string.error_no_internet), aiContext);
			}
		}
	}

	String strUserName, strPassword;
	private void onLoginAttempt() {
		int response 	= 0;
		response		= gd.emptyEditTextError(
				new EditText[]{editUserNameLogIn, editPasswordLogIn},
				new String[]{getResources().getString(R.string.error_login_empty_username),
                        getResources().getString(R.string.error_login_empty_password)});

		if (! GlobalData.isEmailValid(editUserNameLogIn.getText().toString().trim())) {
			response++;
			editUserNameLogIn.setError(getResources().getString(R.string.error_login_invalid_email));
		}

		if(response == 0) {
			strUserName			= editUserNameLogIn.getText().toString().trim();
			strPassword 		= editPasswordLogIn.getText().toString().trim();

			String[] keys 		= {"email", "password", "device_id", "gcm_id"};
			String[] value 		= {strUserName, strPassword, MyUtils.getStringPref(aiContext,ActivitySplashScreen.PREF_DEVICE_ID),
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
	}

	private void callWebService(String postUrl, HashMap<String, String> hash) {
		WebService webService	=	new WebService(aiContext, "", postUrl, hash, this, WebService.POST);
		webService.execute();
	}

	@Override
	public void onWebServiceActionComplete(String result, String url) {
		System.out.println(result + ".........jsonresponse....." + url);
		if (url.contains(GlobalVariables.LOGIN)) {
			try {
				JSONObject json = new JSONObject(result);
				String str_RESULT = json.getString(TAG_RESULT);
				String str_Message = json.getString(TAG_MESSAGE);
				if (str_RESULT.equals("NO")) {
					GlobalData.showToast(str_Message, aiContext);
				}else {
					JSONObject Data_obj 		= json.getJSONObject(TAG_DATA);
					String str_user_id 			= Data_obj.getString(TAG_USER_ID);
					String str_wallet_id 		= Data_obj.getString(TAG_WALLET_ID);
					String str_points 			= Data_obj.getString(TAG_POINTS);
					//	String str_invation_code 		= Data_obj.getString(TAG_INVATION_CODE);
					String str_first_name 			= Data_obj.getString(TAG_FIRST_NAME);
					String str_last_name 			= Data_obj.getString(TAG_LAST_NAME);
					String str_email 				= Data_obj.getString(TAG_PAYPAY_EMAIL);
					//	String str_parent_id 			= Data_obj.getString(TAG_PARENT_ID);
					//	String str_gcm_id 				= Data_obj.getString(TAG_GCM_ID);
					//	String str_is_active 			= Data_obj.getString(TAG_IS_ACTIVE);
					//	String str_last_login_date 		= Data_obj.getString(TAG_LAST_LOGIN_DATE);
					//	String str_device_id 			= Data_obj.getString(TAG_DEVICE_ID);
					//	String str_password 			= Data_obj.getString(TAG_PASSWORD);
					//	String str_id 					= Data_obj.getString(TAG_ID);
					//	String str_facbook_user_id 		= Data_obj.getString(TAG_FACBOOK_USER_ID);
					//	String str_activation_code 		= Data_obj.getString(TAG_ACTIVATION_CODE);
					//	String str_is_usecode 			= Data_obj.getString(TAG_IS_USECODE);
					//	String str_google_user_id 		= Data_obj.getString(TAG_GOOGLE_USER_ID);
					//	String str_last_modified_time 	= Data_obj.getString(TAG_LAST_MODIFIED_TIME);
					//	String str_encrypt 				= Data_obj.getString(TAG_ENCRYPT);
					//	String str_registration_date 	= Data_obj.getString(TAG_REGISTRATION_DATE);
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
					PreferenceConnector.writeBoolean(aiContext, PreferenceConnector.ISREMEMBER, true);

					/*if (radioRemember.isChecked()) {
						PreferenceConnector.writeBoolean(aiContext, PreferenceConnector.ISREMEMBER, true);
					}else {
						PreferenceConnector.writeBoolean(aiContext, PreferenceConnector.ISREMEMBER, false);
					}*/

					Intent aiIntent = new Intent(aiContext, ActivityMainWallet.class);
					startActivity(aiIntent);
					finish();
				}
			}catch (JSONException e){
				e.printStackTrace();
			}
		}else if (url.contains(GlobalVariables.FORGOTPASSWORD)) {
			try {
				JSONObject json = new JSONObject(result);

				String str_RESULT = json.getString(TAG_RESULT);
				String str_Message = json.getString(TAG_MESSAGE);
				GlobalData.showToast(str_Message, aiContext);
			}catch (JSONException e){
				e.printStackTrace();
			}
		}
		else if (url.contains(GlobalVariables.REGISTER)) {
			try {
				JSONObject json 	= new JSONObject(result);
				String str_RESULT 	= json.getString(TAG_RESULT);
				String str_Message 	= json.getString(TAG_MESSAGE);
				//GlobalData.showToast(str_Message, aiContext);
					
					
					//onLoginAttempt();
						if (str_RESULT.equals("NO")) {
							GlobalData.showToast(str_Message, aiContext);
						}else {
							GlobalData.showToast(getResources().getString(R.string.message_login_success), aiContext);
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




							PreferenceConnector.writeString(aiContext, PreferenceConnector.FIRST_NAME, str_first_name);
							PreferenceConnector.writeString(aiContext, PreferenceConnector.LAST_NAME, str_last_name);
							PreferenceConnector.writeString(aiContext, PreferenceConnector.PAYPAL_EMAIL, str_email);

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
							PreferenceConnector.writeBoolean(aiContext, PreferenceConnector.ISREMEMBER, true);

							/*if (radioRemember.isChecked()) {
								PreferenceConnector.writeBoolean(aiContext, PreferenceConnector.ISREMEMBER, true);
							}else {
								PreferenceConnector.writeBoolean(aiContext, PreferenceConnector.ISREMEMBER, false);
							}*/
							/*if (PreferenceConnector.readBoolean(aiContext, PreferenceConnector.ISFBLOGIN, false)) {
								strFname 			= PreferenceConnector.readString(aiContext, PreferenceConnector.FBFNAME, "");
								strLname 			= PreferenceConnector.readString(aiContext, PreferenceConnector.FBLNAME, "");
								strEmailID 			= PreferenceConnector.readString(aiContext, PreferenceConnector.FBEMAIL, "");
								strPassword 		= GlobalData.getDeviceId(aiContext);
								strConfirmPassword 	= GlobalData.getDeviceId(aiContext);
								strRegisterType		= "fb";
								
							}else if (PreferenceConnector.readBoolean(aiContext, PreferenceConnector.ISGPLOGIN, false)) {
								strFname 			= PreferenceConnector.readString(aiContext, PreferenceConnector.GPFNAME, "");
								strLname 			= PreferenceConnector.readString(aiContext, PreferenceConnector.GPLNAME, "");
								strEmailID 			= PreferenceConnector.readString(aiContext, PreferenceConnector.GPEMAIL, "");
								strPassword 		= GlobalData.getDeviceId(aiContext);
								strConfirmPassword 	= GlobalData.getDeviceId(aiContext);
								strRegisterType		= "googleplus";
								
								
							}*/
							PreferenceConnector.writeString(aiContext, PreferenceConnector.USERNAME, strEmailID);
							PreferenceConnector.writeString(aiContext, PreferenceConnector.PASSWORD, strPassword);
							/*editUserNameLogIn.setText(PreferenceConnector.readString(aiContext, PreferenceConnector.USERNAME, ""));
							editPasswordLogIn.setText(PreferenceConnector.readString(aiContext, PreferenceConnector.PASSWORD, ""));*/
							
							Intent aiIntent = new Intent(aiContext, ActivityMainWallet.class);
							startActivity(aiIntent);
							finish();
						
						}
			} catch (JSONException e){
				e.printStackTrace();
			}
		}
	}

	private void hideKeyboard() {
		InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
		// check if no view has focus:
		View view = this.getCurrentFocus();
		if (view != null) {
			inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	public static void hideFragmentkeyboard(Context meraContext, View meraView) {
		final InputMethodManager imm = (InputMethodManager) meraContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(meraView.getWindowToken(), 0);
	}

	/*private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		if (state.isOpened()) {
		} else if (state.isClosed()) {
		}
	}*/

	/*private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
			if (state.isOpened()) {
				Log.d("MainActivity", "Facebook session opened.");
			} else if (state.isClosed()) {
				Log.d("MainActivity", "Facebook session closed.");
			}
		}
	};*/

	@Override
	public void onResume() {
		super.onResume();
		Profile profile = Profile.getCurrentProfile().getCurrentProfile();
		if(profile!=null){

		}
		//Session session = Session.getActiveSession();
	/*	if (session != null &&
				(session.isOpened() || session.isClosed()) ) {
			onSessionStateChange(session, session.getState(), null);
		}*/
		//uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RC_SIGN_IN) {
			if (resultCode != RESULT_OK) {
				mSignInClicked = false;
			}

			mIntentInProgress = false;

			if (!mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();
			}
		}
		//uiHelper.onActivityResult(requestCode, resultCode, data);
		mCallbackManager.onActivityResult(requestCode,resultCode,data);
	}

	@Override
	public void onPause() {
		super.onPause();
		//uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//uiHelper.onSaveInstanceState(outState);
	}
	protected void onStart() {
		super.onStart();
		mGoogleApiClient.connect();
	}

	protected void onStop() {
		super.onStop();
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	/**
	 * Method to resolve any signin errors
	 * */
	private void resolveSignInError() {
		if(mConnectionResult != null) {
            if (mConnectionResult.hasResolution()) {
                try {
                    mIntentInProgress = true;
                    mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
                } catch (SendIntentException e) {
                    mIntentInProgress = false;
                    mGoogleApiClient.connect();
                }
            }
        }
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (!result.hasResolution()) {
			GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
					0).show();
			return;
		}

		if (!mIntentInProgress) {
			// Store the ConnectionResult for later usage
			mConnectionResult = result;

			if (mSignInClicked) {
				// The user has already clicked 'sign-in' so we attempt to
				// resolve all
				// errors until the user is signed in, or they cancel.
				resolveSignInError();
			}
		}

	}


	@Override
	public void onConnected(Bundle arg0) {
		mSignInClicked = false;

		//Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();

		// Get user's information
		getProfileInformation();

		// Update the UI after signin
		updateUI(true);

	}

	/**
	 * Updating the UI, showing/hiding buttons and profile layout
	 * */
	private void updateUI(boolean isSignedIn) {
		/*if (isSignedIn) {
			btnSignIn.setVisibility(View.GONE);
		} else {
			btnSignIn.setVisibility(View.VISIBLE);
		}*/
	}

	/**
	 * Fetching user's information name, email, profile pic
	 * */
	private void getProfileInformation() {
		try {
			if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
				Person currentPerson = Plus.PeopleApi
						.getCurrentPerson(mGoogleApiClient);
				String personName = currentPerson.getDisplayName();
				String personPhotoUrl = currentPerson.getImage().getUrl();
				String personGooglePlusProfile = currentPerson.getUrl();
				String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

				Log.e(TAG, "Name: " + personName + ", plusProfile: "
						+ personGooglePlusProfile + ", email: " + email
						+ ", Name: " + currentPerson.getName()+ ", NICK Name: " + currentPerson.getNickname());


				PreferenceConnector.writeString(aiContext, PreferenceConnector.GPFNAME, currentPerson.getName().getGivenName());
				PreferenceConnector.writeString(aiContext, PreferenceConnector.GPLNAME, currentPerson.getName().getFamilyName());
				PreferenceConnector.writeString(aiContext, PreferenceConnector.GPEMAIL, email);
				PreferenceConnector.writeBoolean(aiContext, PreferenceConnector.ISGPLOGIN, true);
				//txtSignUp.performClick();
				signOutFromGplus();
				onRegister();
				// by default the profile url gives 50x50 px image only
				// we can replace the value with whatever dimension we want by
				// replacing sz=X
				personPhotoUrl = personPhotoUrl.substring(0,
						personPhotoUrl.length() - 2)
						+ PROFILE_PIC_SIZE;


			} else {
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.error_google_plus_no_person_information), Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		mGoogleApiClient.connect();
		updateUI(false);
	}


	/**
	 * Sign-in into google
	 * */
	private void signInWithGplus() {
		if (!mGoogleApiClient.isConnecting()) {
			mSignInClicked = true;
			resolveSignInError();
		}
	}

	/**
	 * Sign-out from google
	 * */
	private void signOutFromGplus() {
		if (mGoogleApiClient.isConnected()) {
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			mGoogleApiClient.disconnect();
			mGoogleApiClient.connect();
			updateUI(false);
		}
	}
	protected void setGooglePlusButtonText(SignInButton signInButton,String buttonText)
	{
		for(int i =0; i < signInButton.getChildCount(); i++)
		{View v = signInButton.getChildAt(i);
		if(v instanceof TextView){TextView mTextView =(TextView) v;
		mTextView.setText(buttonText);return;}}}  
	
	private void onRegister() {
		int response = 0;
		if (response == 0) {
			if (PreferenceConnector.readBoolean(aiContext, PreferenceConnector.ISFBLOGIN, false)) {
				strFname 			= PreferenceConnector.readString(aiContext, PreferenceConnector.FBFNAME, "");
				strLname 			= PreferenceConnector.readString(aiContext, PreferenceConnector.FBLNAME, "");
				strEmailID 			= PreferenceConnector.readString(aiContext, PreferenceConnector.FBEMAIL, "");
				strPassword 		= strEmailID.substring(1, 2).toUpperCase()+strEmailID.substring(1, strEmailID.length()-2)+"4873";
				strConfirmPassword 	= strPassword;
				strRegisterType		= "fb";
			}else if (PreferenceConnector.readBoolean(aiContext, PreferenceConnector.ISGPLOGIN, false)) {
				strFname 			= PreferenceConnector.readString(aiContext, PreferenceConnector.GPFNAME, "");
				strLname 			= PreferenceConnector.readString(aiContext, PreferenceConnector.GPLNAME, "");
				strEmailID 			= PreferenceConnector.readString(aiContext, PreferenceConnector.GPEMAIL, "");
				strPassword 		= strEmailID.substring(1,2).toUpperCase()+strEmailID.substring(1, strEmailID.length()-2)+"4873";
				strConfirmPassword 	= strPassword;
				strRegisterType		= "googleplus";
			}

			String[] keys = {"first_name", "last_name", "email", "password", "cnfpassword", "device_id",
			"gcm_id","type" };
			String[] value = {
					strFname,
					strLname,
					strEmailID,
					strPassword,
					strConfirmPassword,
					MyUtils.getStringPref(aiContext,ActivitySplashScreen.PREF_DEVICE_ID),
					PreferenceConnector.readString(aiContext, PreferenceConnector.GSMREGID, ""),strRegisterType };

			HashMap<String, String> hash = new HashMap<String, String>();
			for (int i = 0; i < keys.length; i++) {
				System.out.println(keys[i] + "......." + value[i]);
				hash.put(keys[i], value[i]);
			}
			callWebService(GlobalVariables.REGISTER, hash);
		}
	}

}

