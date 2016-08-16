package com.cashmobi;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.archiveinfotech.crashreport.Utils;
import com.commonutility.GlobalData;
import com.commonutility.GlobalVariables;
import com.commonutility.PreferenceConnector;
import com.commonutility.WebService;
import com.commonutility.WebServiceListener;
import com.squareup.picasso.Callback.EmptyCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RedeemRewardFragment extends Fragment implements WebServiceListener {
	private Context aiContext;
	private View aiView = null;
	private boolean mAlreadyLoaded=false;
	public static final String TAG_MESSAGE	= "Message";
	public static final String TAG_RESULT	= "RESULT";
	public static final String TAG_DATA		= "Data";
	public static final String TAG_term_content	= "term_content";
	
	private TextView txtInvite,txtInvitationCodeText;
	private Button btnEnter;
	private GlobalData gd;
	ImageView logoImageView;
	private EditText last_nameEdit,first_nameEdit,email_editText;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (aiView == null) {
			aiView = inflater.inflate(R.layout.fragment_redeem_reward, container, false);
		}
		Utils.setFontAllView((ViewGroup)aiView);
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
			String points=getArguments().getString("points");
			String headerName=getArguments().getString("headerName");
			((ActivityMainWallet)aiContext).customizeActionBarWithBack(headerName);

			txtInvite				= (TextView)aiView.findViewById(R.id.text_invitetext);
			txtInvitationCodeText	= (TextView)aiView.findViewById(R.id.txt_invitationbelowcode);
			logoImageView			= (ImageView)aiView.findViewById(R.id.imageView1);
			btnEnter				= (Button)aiView.findViewById(R.id.bt_enter);
			
			last_nameEdit	= (EditText)aiView.findViewById(R.id.last_nameEdit);
			first_nameEdit	= (EditText)aiView.findViewById(R.id.first_nameEdit);
			email_editText	= (EditText)aiView.findViewById(R.id.email_editText);

            first_nameEdit.setText(PreferenceConnector.readString(aiContext, PreferenceConnector.FIRST_NAME,""));
            last_nameEdit.setText(PreferenceConnector.readString(aiContext, PreferenceConnector.LAST_NAME,""));

            InputFilter[] filters = new InputFilter[] { new InputFilter() {
                public CharSequence filter(CharSequence src, int start, int end, Spanned dst, int dstart, int dend) {
                    return src.length() < 1 ? dst.subSequence(dstart, dend) : "";
                }}};

            first_nameEdit.setFocusable(false);
            first_nameEdit.setFilters(filters);
            last_nameEdit.setFocusable(false);
            last_nameEdit.setFilters(filters);
			
			Picasso.with(aiContext)
			.load(getArguments().getString("offerImage"))
			.error(R.drawable.ic_launcher)
			.into(logoImageView, new EmptyCallback() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onError() {
                }
            });
			
			
			
			if(headerName.toLowerCase().contains("paypal")){
				txtInvite.setText(Html.fromHtml("It will cost <font color='#f7835c'>" + points + "</font> Credits to redeem this reward."));
				txtInvitationCodeText.setText(getString(R.string.disclaimer_paypal_redeem));
				email_editText.setText(PreferenceConnector.readString(aiContext, PreferenceConnector.PAYPAL_EMAIL, ""));
			}
			else{
				txtInvite.setText(Html.fromHtml("It will cost <font color='#f7835c'>" + points + "</font> Credits to redeem this gift card.<br> This gift card is valid only on <font color='#f7835c'>" + PreferenceConnector.readString(aiContext, PreferenceConnector.COUNTRYSELECTED, "US") + "</font> store."));
				txtInvitationCodeText.setText(getString(R.string.disclaimer_redeem));
				email_editText.setText(PreferenceConnector.readString(aiContext, PreferenceConnector.USERNAME, ""));
			}
			System.out.println(points);
			System.out.println((PreferenceConnector.readInteger(aiContext, PreferenceConnector.WALLETPOINTS, 0)));
			System.out.println(Integer.parseInt(points) >
                    (PreferenceConnector.readInteger(aiContext, PreferenceConnector.WALLETPOINTS, 0)));
			if(Integer.parseInt(points) <
					(PreferenceConnector.readInteger(aiContext,PreferenceConnector.WALLETPOINTS, 0))) {
				btnEnter.setEnabled(true);
				btnEnter.setTextColor(Color.WHITE);
				btnEnter.setTypeface(null, Typeface.BOLD);
			}else {
				GlobalData.showSnackbar("Not enough credits to redeem this reward.", aiContext, aiView);
//				GlobalData.showToast("Not enough credits to redeem this reward.", aiContext);
				btnEnter.setEnabled(false);
				btnEnter.setTextColor(getResources().getColor(R.color.md_grey_200));
			}
			btnEnter.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					onLoginAttempt();
				}
			});
			/*txtInvite.setText(Html.fromHtml(PreferenceConnector.readString(aiContext, 
					PreferenceConnector.INVITETEXT, "")));*/
			
			HashMap<String, String> hash	=	new HashMap<String, String>();
			hash.put("user_id", PreferenceConnector.readString(aiContext, PreferenceConnector.USERID,""));
			hash.put("first_name", PreferenceConnector.readString(aiContext, PreferenceConnector.FIRST_NAME,""));
			hash.put("last_name", PreferenceConnector.readString(aiContext, PreferenceConnector.LAST_NAME,""));
			hash.put("email",PreferenceConnector.readString(aiContext, PreferenceConnector.PAYPAL_EMAIL,""));
			hash.put("offer_id",getArguments().getString("offerid"));
			


			//callWebService(GlobalVariables.REDEEM_OFFER, hash);
			
		}
	}

	String strUserName, strPassword,strEmail;
	private void onLoginAttempt() {
		int response 	= 0;
		response		= gd.emptyEditTextError(
				new EditText[]{first_nameEdit,last_nameEdit,email_editText},
				new String[]{ getResources().getString(R.string.error_register_empty_first_name),
						getResources().getString(R.string.error_register_empty_last_name),
						getResources().getString(R.string.error_register_empty_email)
				});

		if (! GlobalData.isEmailValid(email_editText.getText().toString().trim())) {
			response++;
			email_editText.setError(getResources().getString(R.string.error_login_invalid_email));
		}

		if(response == 0) {
			strUserName			= first_nameEdit.getText().toString().trim();
			strPassword 		= last_nameEdit.getText().toString().trim();
			strEmail			= email_editText.getText().toString().trim();

			
			/*HashMap<String, String> hash	=	new HashMap<String, String>();
			hash.put("user_id", PreferenceConnector.readString(aiContext, PreferenceConnector.USERID,""));
			hash.put("first_name", PreferenceConnector.readString(aiContext, PreferenceConnector.FIRST_NAME,""));
			hash.put("last_name", PreferenceConnector.readString(aiContext, PreferenceConnector.LAST_NAME,""));
			hash.put("email",PreferenceConnector.readString(aiContext, PreferenceConnector.PAYPAL_EMAIL,""));
			hash.put("offer_id",getArguments().getString("offerid"));*/
			


			//callWebService(GlobalVariables.REDEEM_OFFER, hash);
			
			String[] keys 		= {"user_id","first_name", "last_name", "email", "offer_id"};
			String[] value 		= {PreferenceConnector.readString(aiContext, PreferenceConnector.USERID,""),strUserName, strPassword, strEmail,getArguments().getString("offerid")};

			HashMap<String, String> hash	=	new HashMap<String, String>();
			for (int i = 0; i < keys.length; i++) {
				System.out.println(keys[i]+ "......." + value[i]);
				hash.put(keys[i], value[i]);
			}

			if (gd.isConnectingToInternet()) {
				callWebService(GlobalVariables.REDEEM_OFFER, hash);
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
		System.out.println(result+".........jsonresponse....."+url);

		try {
			JSONObject json = new JSONObject(result);
			String str_RESULT 	= json.getString(TAG_RESULT);
			String str_Message 	= json.getString(TAG_MESSAGE);

			if (str_RESULT.equals("YES")) {
				GlobalData.showToast(getResources().getString(R.string.message_redeem_success), aiContext);
				JSONObject Data_obj 		= json.getJSONObject(TAG_DATA);
				String str_user_points 		= Data_obj.getString(DailyRewardFragment.TAG_USER_POINTS);

				PreferenceConnector.writeInteger(aiContext, PreferenceConnector.WALLETPOINTS, 
						Integer.parseInt(str_user_points));
				FragEarnCredits.onUpdateView(aiContext);
				ViewRewardsFragment.onUpdateView(aiContext);
				InviteFriendsFragment.onUpdateView(aiContext);
				ConnectSocialFragment.onUpdateView(aiContext);
				
				switchBack();
			} else {
				GlobalData.showToast(str_Message, aiContext);
			}
		} catch (JSONException e){
			e.printStackTrace();
		}
		

	}
	private void switchBack() {
		if (getActivity() == null)
			return;
		if (getActivity() instanceof ActivityMainWallet) {
			ActivityMainWallet mActivity = (ActivityMainWallet) getActivity();
			mActivity.customizeActionBar();
			mActivity.switchBack();
		}
	}
}