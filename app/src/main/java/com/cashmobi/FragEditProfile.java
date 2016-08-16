package com.cashmobi;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.archiveinfotech.crashreport.Utils;
import com.commonutility.GlobalData;
import com.commonutility.GlobalVariables;
import com.commonutility.PreferenceConnector;
import com.commonutility.WebService;
import com.commonutility.WebServiceListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class FragEditProfile extends Fragment implements WebServiceListener{

    public static final String TAG_MESSAGE="Message";
    public static final String TAG_DATA="Data";
    public static final String TAG_RESULT="RESULT";

    private GlobalData gd;
    private Context aiContext;
    private View aiView = null;
    private boolean mAlreadyLoaded=false;

    private EditText invitationId, email, paypalEmail, firstName, lastName;
    private Button submitBtn, cancelBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (aiView == null) {
            aiView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        }

        aiContext = getContext();

        invitationId = (EditText)aiView.findViewById(R.id.edit_profile_invite_id);
        email = (EditText)aiView.findViewById(R.id.edit_profile_email);
        paypalEmail = (EditText)aiView.findViewById(R.id.edit_profile_paypal_email);
        firstName = (EditText)aiView.findViewById(R.id.edit_profile_first_name);
        lastName = (EditText)aiView.findViewById(R.id.edit_profile_last_name);

        submitBtn = (Button)aiView.findViewById(R.id.edit_profile_btn_submit);
        cancelBtn = (Button)aiView.findViewById(R.id.edit_profile_btn_cancel);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUpdateProfile();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


        invitationId.setText(PreferenceConnector.readString(aiContext, PreferenceConnector.WALLETID, ""));
        email.setText(PreferenceConnector.readString(aiContext, PreferenceConnector.GPEMAIL,
                PreferenceConnector.readString(aiContext, PreferenceConnector.FBEMAIL, "")));
        paypalEmail.setText(PreferenceConnector.readString(aiContext, PreferenceConnector.PAYPAL_EMAIL, ""));
        firstName.setText(PreferenceConnector.readString(aiContext, PreferenceConnector.FIRST_NAME, ""));
        lastName.setText(PreferenceConnector.readString(aiContext, PreferenceConnector.LAST_NAME, ""));

        InputFilter[] filters = new InputFilter[] { new InputFilter() {
            public CharSequence filter(CharSequence src, int start, int end, Spanned dst, int dstart, int dend) {
                return src.length() < 1 ? dst.subSequence(dstart, dend) : "";
            }}};

        invitationId.setFocusable(false);
        invitationId.setFilters(filters);
        if(firstName.getText().length() > 0) {
            firstName.setFocusable(false);
            firstName.setFilters(filters);
        }
        if(lastName.getText().length() > 0) {
            lastName.setFocusable(false);
            lastName.setFilters(filters);
        }

        Utils.setFontAllView((ViewGroup) aiView);
        return aiView;
    }

    private void callWebService(String postUrl, HashMap<String, String> hash) {
        WebService webService	=	new WebService(aiContext, "", postUrl, hash, this, WebService.POST);
        webService.execute();
    }

    @Override
    public void onWebServiceActionComplete(String result, String url) {
        if (url.contains(GlobalVariables.UPDATE_PROFILE)) {
            try {
                JSONObject json 	= new JSONObject(result);
                String str_RESULT 	= json.getString(TAG_RESULT);
                String str_Message 	= json.getString(TAG_MESSAGE);
                GlobalData.showToast(str_Message, aiContext);

                if (str_RESULT.equals("YES")) {
                    JSONObject Data_obj 		= json.getJSONObject(TAG_DATA);
                    String str_first_name 			= Data_obj.getString(ActivityLogin.TAG_FIRST_NAME);
                    String str_last_name 			= Data_obj.getString(ActivityLogin.TAG_LAST_NAME);
                    String str_email 				= Data_obj.getString(ActivityLogin.TAG_PAYPAY_EMAIL);

                    PreferenceConnector.writeString(aiContext, PreferenceConnector.FIRST_NAME, str_first_name);
                    PreferenceConnector.writeString(aiContext, PreferenceConnector.LAST_NAME, str_last_name);
                    PreferenceConnector.writeString(aiContext, PreferenceConnector.PAYPAL_EMAIL, str_email);

                }
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null && !mAlreadyLoaded) {
            mAlreadyLoaded 		= true;
//            aiContext 			= getActivity();
            aiView 				= getView();
            ((ActivityMainWallet)aiContext).customizeActionBarWithBack(getResources().getString(R.string.title_screen_edit_profile));

            gd = new GlobalData(aiContext);
        }
    }

    	private void onUpdateProfile() {
		int response = 0;
		response = gd.emptyEditTextError(
				new EditText[] { firstName, lastName },
				new String[] { getResources().getString(R.string.error_register_empty_first_name),
                        getResources().getString(R.string.error_register_empty_last_name) });

		if (! GlobalData.isEmailValid(paypalEmail.getText().toString().trim())) {
			response++;
			paypalEmail.setError(getResources().getString(R.string.error_login_invalid_email));
		}
		if (response == 0) {
			String strFname 			= firstName.getText().toString().trim();
			String strLname 			= lastName.getText().toString().trim();
			String strEmailID 			= paypalEmail.getText().toString().trim();

			String[] keys = {"user_id","first_name", "last_name", "paypal_account", "password" };
			String[] value = {
					PreferenceConnector.readString(aiContext, PreferenceConnector.USERID, ""),
					strFname,
					strLname,
					strEmailID,
					"",};

			HashMap<String, String> hash = new HashMap<String, String>();
			for (int i = 0; i < keys.length; i++) {
				System.out.println(keys[i] + "......." + value[i]);
				hash.put(keys[i], value[i]);
			}
			callWebService(GlobalVariables.UPDATE_PROFILE, hash);
		}
	}
}