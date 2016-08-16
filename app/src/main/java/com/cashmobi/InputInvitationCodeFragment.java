package com.cashmobi;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.archiveinfotech.crashreport.Utils;
import com.commonutility.GlobalData;
import com.commonutility.GlobalVariables;
import com.commonutility.PreferenceConnector;
import com.commonutility.WebService;
import com.commonutility.WebServiceListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class InputInvitationCodeFragment extends Fragment implements OnClickListener, WebServiceListener {
	private Context aiContext;
	private View aiView = null;
	private boolean mAlreadyLoaded=false;
	private GlobalData gd;
	private EditText edtInputInvitationCode;
	private Button btnEnter;
	public static final String TAG_MESSAGE		= "Message";
	public static final String TAG_RESULT		= "RESULT";
	public static final String TAG_DATA			= "Data";
	public static final String TAG_user_points	= "user_points";
	public static final String TAG_USER_POINTS		= "user_points";
	public static final String TAG_REWARDS_CHECKIN 	= "rewards_checkin";

	private TextView txtInvite, txtInvitationCodeText;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (aiView == null) {
			aiView = inflater.inflate(R.layout.fragment_input_friend_code, container, false);
		}

		return aiView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState == null && !mAlreadyLoaded) {
			mAlreadyLoaded 		= true;
			aiContext 			= getActivity();
			aiView 				= getView();
			gd					= new GlobalData(aiContext);

			((ActivityMainWallet)aiContext).customizeActionBarWithBack(getResources().getString(R.string.title_screen_input_invitation_code));

			edtInputInvitationCode	= (EditText)aiView.findViewById(R.id.friend_invitationcode);
			btnEnter				= (Button)aiView.findViewById(R.id.bt_enter);
			txtInvite				= (TextView)aiView.findViewById(R.id.text_invitetext);
			txtInvitationCodeText	= (TextView)aiView.findViewById(R.id.txt_invitationbelowcode);

			btnEnter.setOnClickListener(this);

			//	edtInputInvitationCode.addTextChangedListener(textWatcher);

			txtInvite.setText(Html.fromHtml(PreferenceConnector.readString(aiContext, 
					PreferenceConnector.INVITETEXT, "")));

			if (PreferenceConnector.readInteger(aiContext, 
					PreferenceConnector.INVITEDISABLEPOINT, 0) < 
					PreferenceConnector.readInteger(aiContext, 
							PreferenceConnector.WALLETPOINTS, 0)) {
				btnEnter.setEnabled(true);
				btnEnter.setTextColor(Color.parseColor("#ffffff"));
				btnEnter.setTypeface(null, Typeface.BOLD);
				txtInvitationCodeText.setEnabled(true);
				txtInvitationCodeText.setText("You Must Acquire "
						+ PreferenceConnector.readInteger(aiContext, PreferenceConnector.INVITEDISABLEPOINT, 
								0) + " Credits to Input Your Friend's Invitation Code!");
			}else {
				btnEnter.setEnabled(false);
				txtInvitationCodeText.setEnabled(false);
				btnEnter.setTextColor(getResources().getColor(R.color.md_grey_200));
				txtInvitationCodeText.setText("Collect " 
						+ PreferenceConnector.readInteger(aiContext, PreferenceConnector.INVITEDISABLEPOINT, 0)
						+ " Credit first");
			}
		}
		Utils.setFontAllView((ViewGroup)aiView);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_enter:
			if (edtInputInvitationCode.getText().toString().length() >= 0) {
				if (gd.isConnectingToInternet()) {
					String[] keys 		= {"user_id", "invite_wallet_id"};
					String[] value 		= {PreferenceConnector.readString(aiContext, PreferenceConnector.USERID,
							""), edtInputInvitationCode.getText().toString().trim()};

					HashMap<String, String> hash	=	new HashMap<String, String>();
					for (int i = 0; i < keys.length; i++) {
						System.out.println(keys[i]+ "......." + value[i]);
						hash.put(keys[i], value[i]);
					}

					callWebService(GlobalVariables.INVITAIONCODE, hash);
				}else {
					GlobalData.showToast(getResources().getString(R.string.error_no_internet), aiContext);
				}
			}else {
				edtInputInvitationCode.setError(getResources().getString(R.string.error_input_invitation_code_empty_code));
			}
			break;
		default:
			break;
		}
	}

	private void callWebService(String postUrl, HashMap<String, String> hash) {
		WebService webService	=	new WebService(aiContext, "", postUrl, hash, this, WebService.POST);
		webService.execute();
	}

	@Override
	public void onWebServiceActionComplete(String result, String url) {
		System.out.println(result+".........jsonresponse....."+url);

		try {
			JSONObject json = new JSONObject(result);
			String str_RESULT 	= json.getString(TAG_RESULT);
			String str_Message 	= json.getString(TAG_MESSAGE);

			GlobalData.showToast(str_Message, aiContext);


			//GlobalData.showToast(jsonData.getString(TAG_faq_content), aiContext);
			//				
			if (str_RESULT.equals("YES")) {
				JSONObject Data_obj 		= json.getJSONObject(TAG_DATA);
				String str_user_points 		= Data_obj.getString(TAG_USER_POINTS);
				String str_rewards_checkin 	= Data_obj.getString(TAG_REWARDS_CHECKIN);

				PreferenceConnector.writeInteger(aiContext, PreferenceConnector.WALLETPOINTS, 
						Integer.parseInt(str_user_points));
				PreferenceConnector.writeBoolean(aiContext, PreferenceConnector.INPUT_INTIVTE_CODE_COMPLETED, true);
				FragEarnCredits.onUpdateView(aiContext);
				ViewRewardsFragment.onUpdateView(aiContext);
				InviteFriendsFragment.onUpdateView(aiContext);
				ConnectSocialFragment.onUpdateView(aiContext);
			}
		} catch (JSONException e){
			e.printStackTrace();
		}

	}
}