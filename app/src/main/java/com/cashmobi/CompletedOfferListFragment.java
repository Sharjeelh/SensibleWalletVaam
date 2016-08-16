package com.cashmobi;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.archiveinfotech.crashreport.Utils;
import com.commonutility.GlobalVariables;
import com.commonutility.PreferenceConnector;
import com.commonutility.WebService;
import com.commonutility.WebServiceListener;
import com.helper.MyUtils;
import com.model.InvitedFriendsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CompletedOfferListFragment extends Fragment implements WebServiceListener {
	private Context aiContext;
	private View aiView = null;
	private boolean mAlreadyLoaded=false;
	public static final String TAG_faq_content	= "faq_content";

	public  ArrayList<InvitedFriendsModel> invitedFriendListModel ;

	ListView   list;

	public static final String TAG_MESSAGE="Message";
	public static final String TAG_DATA="Data";
	public static final String TAG_GCM_ID="gcm_id";
	public static final String TAG_IS_ACTIVE="is_active";
	public static final String TAG_LAST_MODIFIED_TIME="last_modified_time";
	public static final String TAG_ACTIVATION_CODE="activation_code";
	public static final String TAG_DEVICE_ID="device_id";
	public static final String TAG_FACBOOK_USER_ID="facbook_user_id";
	public static final String TAG_INVATION_CODE="invation_code";
	public static final String TAG_LAST_NAME="last_name";
	public static final String TAG_PAYPAL_ACCOUNT="paypal_account";
	public static final String TAG_LAST_LOGIN_DATE="last_login_date";
	public static final String TAG_POINTS="points";
	public static final String TAG_WALLET_ID="wallet_id";
	public static final String TAG_PASSWORD="password";
	public static final String TAG_REWARDS_CHECKIN="rewards_checkin";
	public static final String TAG_REGISTRATION_DATE="registration_date";
	public static final String TAG_GOOGLE_USER_ID="google_user_id";
	public static final String TAG_PARENT_ID="parent_id";
	public static final String TAG_ENCRYPT="encrypt";
	public static final String TAG_ID="id";
	public static final String TAG_FIRST_NAME="first_name";
	public static final String TAG_EMAIL="email";
	public static final String TAG_IS_USECODE="is_usecode";
	public static final String TAG_RESULT="RESULT";

	public static final String TAG_GIFTCARD_DESCRIPTION	= "user_wallet";
	public static final String TAG_GIFTCARD_TITLE		= "sdk_name";
	public static final String TAG_OFFERS_TITLE			= "created";
	public static final String TAG_OFFER_PRICE			= "credits";
//	public static final String TAG_OFFER_PRICE_SIGN		= "country_price_sign";
//	public static final String TAG_OFFER_ACTIVE			= "is_active";


	TextView errorTextView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (aiView == null) {
			aiView = inflater.inflate(R.layout.fragment_completed_offers, container, false);
		}
		Utils.setFontAllView((ViewGroup)aiView);
		MyUtils.sendScreenToGoogleAnalytics(getActivity().getApplication(),"Completed Offer List");
		return aiView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState == null && !mAlreadyLoaded) {
			mAlreadyLoaded 		= true;
			aiContext 			= getActivity();
			aiView 				= getView();
			((ActivityMainWallet)aiContext).customizeActionBarWithBack(aiContext.getResources().getString(R.string.title_screen_completed_offer_list));
			list			=(ListView)getActivity().findViewById(R.id.list);
			errorTextView	=(TextView)getActivity().findViewById(R.id.errorMsg);
			/*errorTextView.setVisibility(View.GONE);
			mInvitedFriendsListView.setVisibility(View.VISIBLE);*/


			HashMap<String, String> hash	=	new HashMap<String, String>();
			//hash.put("user_id", "2");
			hash.put("user_id", PreferenceConnector.readString(aiContext, PreferenceConnector.WALLETID, ""));

			callWebService(GlobalVariables.CREDITS_COMPLETED_LIST, hash);


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

			//JSONObject jsonData = new JSONObject(json.getString(TAG_DATA));

			invitedFriendListModel = new ArrayList<InvitedFriendsModel>();
			if(!str_RESULT.equals("NO")){
				JSONArray Data = json.getJSONArray(TAG_DATA);
				for(int Data_i = 0; Data_i < Data.length(); Data_i++){
					JSONObject Data_obj=Data.getJSONObject(Data_i);
					{

						String str_title = Data_obj.getString(TAG_OFFERS_TITLE);

						String str_price = Data_obj.getString(TAG_OFFER_PRICE);

						String str_first_name = Data_obj.getString(TAG_GIFTCARD_TITLE);
						String str_last_name = Data_obj.getString(TAG_LAST_NAME);

						String str_email = Data_obj.getString(TAG_GIFTCARD_DESCRIPTION);

						InvitedFriendsModel model=new InvitedFriendsModel();
						model.setfName(str_title);
						model.setlName(str_first_name);
						model.setEmail(str_price+" credits");
						invitedFriendListModel.add(model);
					}
				}
			}
			if(invitedFriendListModel.size()==0){
				errorTextView.setVisibility(View.VISIBLE);
				list.setVisibility(View.GONE);
			}
			else{
				errorTextView.setVisibility(View.GONE);
				list.setVisibility(View.VISIBLE);
			}
			InvitedFriendAdapter adapter = new	InvitedFriendAdapter(getActivity(), invitedFriendListModel);
			list.setAdapter(adapter);

			list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {



				}
			});
			//GlobalData.showToast(jsonData.getString(TAG_faq_content), aiContext);
		} catch (JSONException e){
			e.printStackTrace();
		}
	}
	public class InvitedFriendAdapter extends BaseAdapter{
		private final Activity context;
		private ArrayList<InvitedFriendsModel> modelsData;
		public InvitedFriendAdapter(Activity context,ArrayList<InvitedFriendsModel> modelsData) {
			this.context = context;
			this.modelsData = modelsData;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {


			LayoutInflater inflater = context.getLayoutInflater();
			View rowView= inflater.inflate(R.layout.invited_friends_list_item, null, true);
			TextView dateTextView = (TextView) rowView.findViewById(R.id.txtEmail);
			dateTextView.setText(modelsData.get(position).getEmail());
			TextView txtTitle = (TextView) rowView.findViewById(R.id.txtName);

			txtTitle.setText(modelsData.get(position).getlName());
			TextView distance = (TextView) rowView.findViewById(R.id.txtDistanse);
			distance.setText(modelsData.get(position).getfName());
			/*String noHTMLString = modelsData.get(position).getText();
			distance.setText(Html.fromHtml(noHTMLString));*/

			return rowView;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return modelsData.size();
		}
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
	}

}