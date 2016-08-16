package com.cashmobi;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class InvitedFriendListFragment extends Fragment implements WebServiceListener {
	private Context aiContext;
	private View aiView = null;
	private boolean mAlreadyLoaded=false;

	public  ArrayList<InvitedFriendsModel> invitedFriendListModel;
	ListView mInvitedFriendsListView;

	public static final String TAG_MESSAGE="Message";
	public static final String TAG_DATA="Data";
	public static final String TAG_LAST_NAME="last_name";
	public static final String TAG_FIRST_NAME="first_name";
	public static final String TAG_EMAIL="email";
	public static final String TAG_RESULT="RESULT";
	TextView errorTextView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (aiView == null) {
			aiView = inflater.inflate(R.layout.fragment_invited_friends, container, false);
		}
		Utils.setFontAllView((ViewGroup)aiView);
		MyUtils.sendScreenToGoogleAnalytics(getActivity().getApplication(), "Screen : Invite Friends List");

		return aiView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState == null && !mAlreadyLoaded) {
			mAlreadyLoaded 		= true;
			aiContext 			= getActivity();
			aiView 				= getView();
			((ActivityMainWallet)aiContext).customizeActionBarWithBack(getResources().getString(R.string.title_screen_invited_friends));
			mInvitedFriendsListView =(ListView)getActivity().findViewById(R.id.list);
			errorTextView	=(TextView)getActivity().findViewById(R.id.errorMsg);

			HashMap<String, String> hash	=	new HashMap<String, String>();
			hash.put("user_id", PreferenceConnector.readString(aiContext, PreferenceConnector.USERID, ""));

			callWebService(GlobalVariables.INVITED_FRIEND_LIST, hash);


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

			invitedFriendListModel = new ArrayList<InvitedFriendsModel>();
			if(!str_RESULT.equals("NO")){
				JSONArray Data = json.getJSONArray(TAG_DATA);
				for(int Data_i = 0; Data_i < Data.length(); Data_i++){
					JSONObject Data_obj=Data.getJSONObject(Data_i);
					String str_first_name = Data_obj.getString(TAG_FIRST_NAME);
					String str_last_name = Data_obj.getString(TAG_LAST_NAME);

					String str_email = Data_obj.getString(TAG_EMAIL);

					InvitedFriendsModel model=new InvitedFriendsModel();
					model.setfName(str_first_name);
					model.setlName(str_last_name);
					model.setEmail(str_email);
					invitedFriendListModel.add(model);
				}
			}
			if(invitedFriendListModel.size()==0){
				errorTextView.setVisibility(View.VISIBLE);
				mInvitedFriendsListView.setVisibility(View.GONE);
			}
			else{
				errorTextView.setVisibility(View.GONE);
				mInvitedFriendsListView.setVisibility(View.VISIBLE);
			}
			InvitedFriendAdapter adapter = new	InvitedFriendAdapter(getActivity(), invitedFriendListModel);
			mInvitedFriendsListView.setAdapter(adapter);
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

			txtTitle.setText(modelsData.get(position).getfName()+" "+modelsData.get(position).getlName());
			TextView distance = (TextView) rowView.findViewById(R.id.txtDistanse);
			distance.setVisibility(View.GONE);
			return rowView;
		}

		@Override
		public int getCount() { return modelsData.size(); }
		@Override
		public Object getItem(int position) { return position; }
		@Override
		public long getItemId(int position) { return position; }
	}

}