package com.cashmobi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.archiveinfotech.crashreport.Utils;
import com.commonutility.GlobalData;
import com.commonutility.GlobalVariables;
import com.commonutility.PreferenceConnector;
import com.commonutility.WebService;
import com.commonutility.WebServiceListener;
import com.google.android.gcm.GCMRegistrar;
import com.helper.MyUtils;
import com.material.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ActivityRegister extends BaseActivity implements OnClickListener, WebServiceListener {
	private Context aiContext;
	private GlobalData gd;
	private EditText edtFname, edtLname, edtEmailId, edtPassword, edtConfirmPassword;
	private String strFname = "", strLname = "", strEmailID = "", strPassword = "", strConfirmPassword = "";
	private Button btnStartEarning;

	public static final String TAG_RESULT		= "RESULT";
	public static final String TAG_MESSAGE		= "Message";

//	TextView appFirstName,appLastName;
	@Override
	public void onCreate(Bundle inState) {
		super.onCreate(inState);
//		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.register_activity);
		aiContext = this;
		gd 		  = new GlobalData(aiContext);
		gd.setStatusBarColor();
		getGCMRegistrationId();

		/*ViewGroup viewGroup = (ViewGroup)((ViewGroup)this.findViewById(android.R.id.content)).getChildAt(0);
		MyUtils.setFontAllView(viewGroup);*/
		ViewGroup viewGroup = (ViewGroup)this.findViewById(R.id.scrollbar);
		Utils.setFontAllView(viewGroup);

		edtFname 			= (EditText) findViewById(R.id.et_fname);
		edtLname 			= (EditText) findViewById(R.id.et_lname);
		edtEmailId 			= (EditText) findViewById(R.id.et_email);
		edtPassword 		= (EditText) findViewById(R.id.et_password);
		edtConfirmPassword 	= (EditText) findViewById(R.id.et_repassword);
		btnStartEarning 	= (Button) findViewById(R.id.btn_startearning);

//		appFirstName= (TextView)findViewById(R.id.app_name);
//		appLastName= (TextView)findViewById(R.id.tv_app_name_sub);
//		MyUtils.firstText(appFirstName);
//		MyUtils.lastName(appLastName);
		
		btnStartEarning.setOnClickListener(this);


		if (PreferenceConnector.readBoolean(aiContext, PreferenceConnector.ISFBLOGIN, false)) {
			edtFname.setText(PreferenceConnector.readString(aiContext, PreferenceConnector.FBFNAME, ""));
			edtLname.setText(PreferenceConnector.readString(aiContext, PreferenceConnector.FBLNAME, ""));
			edtEmailId.setText(PreferenceConnector.readString(aiContext, PreferenceConnector.FBEMAIL, ""));
		}else if (PreferenceConnector.readBoolean(aiContext, PreferenceConnector.ISGPLOGIN, false)) {
			edtFname.setText(PreferenceConnector.readString(aiContext, PreferenceConnector.GPFNAME, ""));
			edtLname.setText(PreferenceConnector.readString(aiContext, PreferenceConnector.GPLNAME, ""));
			edtEmailId.setText(PreferenceConnector.readString(aiContext, PreferenceConnector.GPEMAIL, ""));
		}

		setDisplayHomeAsUpEnabled(true, R.drawable.abc_ic_ab_back_mtrl_am_alpha);

		hideKeyboard();
	}

	@Override
	protected void initViews() {
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}

	@SuppressLint("InlinedApi")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_startearning:
			hideKeyboard();
			if (gd.isConnectingToInternet()) {
				String gcmId = PreferenceConnector.readString(aiContext, PreferenceConnector.GSMREGID, "");
				if (!gcmId.equals("")) {
					onRegister();
				}
			}else {
				GlobalData.showToast(getResources().getString(R.string.error_no_internet), aiContext);
			}
		default:
			break;
		}
	}

	private void onRegister() {
		int response = 0;
		response = gd.emptyEditTextError(
				new EditText[] { edtFname, edtLname, edtEmailId, edtPassword, edtConfirmPassword},
				new String[] { getResources().getString(R.string.error_register_empty_first_name),
						getResources().getString(R.string.error_register_empty_last_name),
						getResources().getString(R.string.error_register_empty_email),
						getResources().getString(R.string.error_register_empty_password),
						getResources().getString(R.string.error_register_empty_confirm_password) });

		if (! GlobalData.isEmailValid(edtEmailId.getText().toString().trim())) {
			response++;
			edtEmailId.setError(getResources().getString(R.string.error_login_invalid_email));
		}

		if (! ((edtPassword.getText().toString().trim()).equals(edtConfirmPassword.getText().toString().trim()))) {
			response++;
			edtConfirmPassword.setError(getResources().getString(R.string.error_register_password_no_match));
		}

		if (response == 0) {
			strFname 			= edtFname.getText().toString().trim();
			strLname 			= edtLname.getText().toString().trim();
			strEmailID 			= edtEmailId.getText().toString().trim();
			strPassword 		= edtPassword.getText().toString().trim();
			strConfirmPassword 	= edtConfirmPassword.getText().toString().trim();

			String[] keys = {"first_name", "last_name", "email", "password", "cnfpassword", "device_id",
					"gcm_id","type" };
			String[] value = {
					strFname,
					strLname,
					strEmailID,
					strPassword,
					strConfirmPassword,
					MyUtils.getStringPref(aiContext,MyUtils.getStringPref(aiContext,ActivitySplashScreen.PREF_DEVICE_ID)),
					PreferenceConnector.readString(aiContext, PreferenceConnector.GSMREGID, ""),"email" };

			HashMap<String, String> hash = new HashMap<String, String>();
			for (int i = 0; i < keys.length; i++) {
				System.out.println(keys[i] + "......." + value[i]);
				hash.put(keys[i], value[i]);
			}
			callWebService(GlobalVariables.REGISTER, hash);
		}
	}

	public String getGCMRegistrationId() {
		String regId = "";
		String prefRegIg = PreferenceConnector.readString(aiContext, PreferenceConnector.GSMREGID, "");
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		if (prefRegIg.equals("")) {
			regId = GCMRegistrar.getRegistrationId(aiContext);
			PreferenceConnector.writeString(aiContext, PreferenceConnector.GSMREGID, regId);
			if (regId.equals("")) {
				GCMRegistrar.register(aiContext, GlobalVariables.SENDER_ID);
				PreferenceConnector.writeString(aiContext, PreferenceConnector.GSMREGID, GCMRegistrar.getRegistrationId(aiContext));
			}
		}
		return PreferenceConnector.readString(aiContext, PreferenceConnector.GSMREGID, "");
	}

	private void hideKeyboard() {
		InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
		// check if no view has focus:
		View view = this.getCurrentFocus();
		if (view != null) {
			inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	private void callWebService(String postUrl, HashMap<String, String> hash) {
		WebService webService = new WebService(aiContext, "", postUrl, hash, this, WebService.POST);
		webService.execute();
	}

	@Override
	public void onWebServiceActionComplete(String result, String url) {
		System.out.println(result + ".........jsonresponse....." + url);
		if (url.contains(GlobalVariables.REGISTER)) {
			try {
				JSONObject json 	= new JSONObject(result);
				String str_RESULT 	= json.getString(TAG_RESULT);
				String str_Message 	= json.getString(TAG_MESSAGE);
				GlobalData.showToast(str_Message, aiContext);

				

				if (str_RESULT.equals("YES")) {
					PreferenceConnector.writeString(aiContext, PreferenceConnector.USERNAME, strEmailID);
					PreferenceConnector.writeString(aiContext, PreferenceConnector.PASSWORD, strPassword);
					Intent aiIntent = new Intent(aiContext, ActivityLogin.class);
					startActivity(aiIntent);
					finish();
				}
			} catch (JSONException e){
				e.printStackTrace();
			}
		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		PreferenceConnector.writeString(aiContext, PreferenceConnector.GPFNAME, "");
		PreferenceConnector.writeString(aiContext, PreferenceConnector.GPLNAME, "");
		PreferenceConnector.writeString(aiContext, PreferenceConnector.GPEMAIL, "");
		PreferenceConnector.writeBoolean(aiContext, PreferenceConnector.ISGPLOGIN, false);
		
		PreferenceConnector.writeString(aiContext, PreferenceConnector.FBFNAME, "");
		PreferenceConnector.writeString(aiContext, PreferenceConnector.FBLNAME, "");
		PreferenceConnector.writeString(aiContext, PreferenceConnector.FBEMAIL, "");
		PreferenceConnector.writeBoolean(aiContext, PreferenceConnector.ISFBLOGIN, false);
		
		Intent aiIntent = new Intent(aiContext, ActivityLogin.class);
		startActivity(aiIntent);
		finish();
	}
}
