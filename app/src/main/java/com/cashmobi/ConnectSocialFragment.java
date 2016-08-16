package com.cashmobi;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.adapter.AdapterConnect;
import com.archiveinfotech.crashreport.Utils;
import com.commonutility.GlobalData;
import com.commonutility.GlobalVariables;
import com.commonutility.PreferenceConnector;
import com.commonutility.WebService;
import com.commonutility.WebServiceListener;
import com.helper.MyUtils;
import com.model.ConnectModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.cashmobi.ActivityMainWallet.countryIconUrl;

public class ConnectSocialFragment extends Fragment implements WebServiceListener {
	private Context aiContext;
	private View aiView = null;
	private boolean mAlreadyLoaded=false;
	private GlobalData gd;
	private List<ConnectModel> listConnect = new ArrayList<ConnectModel>();
	private ListView lvConnect;
	public static final String TAG_DATA			= "Data";
	public static final String TAG_SOCIAL_LOGO	= "social_logo";
	public static final String TAG_SOCIAL_STATUS= "social_status";
	public static final String TAG_SOCIAL_LINK	= "social_link";
	public static final String TAG_SOCIAL_ID	= "social_id";
	public static final String TAG_SOCIAL_TITLE	= "social_title";
	public static final String TAG_RESULT		= "RESULT";
	public static final String TAG_MESSAGE		= "Message";
	private static TextView creditWallet;
	static ImageView image;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (aiView == null) {
			aiView = inflater.inflate(R.layout.fragment_social, container, false);
		}
		image = (ImageView) aiView.findViewById(R.id.image_view_country_flag);
		Utils.setFontAllView((ViewGroup)aiView);
		MyUtils.sendScreenToGoogleAnalytics(getActivity().getApplication(),"Social Connect");
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

			lvConnect			= (ListView)aiView.findViewById(R.id.listconnect);
			creditWallet		= (TextView)aiView.findViewById(R.id.credit_wallet);

			if (gd.isConnectingToInternet()) {
				callWebService(GlobalVariables.SOCIALLIST, new HashMap<String, String>());
			}else {
				GlobalData.showToast(getResources().getString(R.string.error_no_internet), aiContext);
			}
			creditWallet.setText(PreferenceConnector.readInteger(aiContext, PreferenceConnector.WALLETPOINTS,
					0) + "");

			((ActivityMainWallet)getActivity()).initCountryFlagIcon(aiView);
		}


		
	}

	private void callWebService(String postUrl, HashMap<String, String> hash) {
		WebService webService	=	new WebService(aiContext, "", postUrl, hash, this, WebService.POST);
		webService.execute();
	}

	@Override
	public void onWebServiceActionComplete(String result, String url) {
		System.out.println(result+".........jsonresponse....."+url);
		if(url.contains(GlobalVariables.COUNTRYLIST)) {

		} else {
			listConnect = new ArrayList<ConnectModel>();
			try {
				JSONObject json = new JSONObject(result);
				JSONArray Data = json.getJSONArray(TAG_DATA);
				String str_RESULT = json.getString(TAG_RESULT);
				String str_Message = json.getString(TAG_MESSAGE);
				if (str_RESULT.equals("YES")) {
					for (int Data_i = 0; Data_i < Data.length(); Data_i++) {
						JSONObject Data_obj = Data.getJSONObject(Data_i);
						String str_social_logo = Data_obj.getString(TAG_SOCIAL_LOGO);
						String str_social_status = Data_obj.getString(TAG_SOCIAL_STATUS);
						String str_social_link = Data_obj.getString(TAG_SOCIAL_LINK);
						String str_social_id = Data_obj.getString(TAG_SOCIAL_ID);
						String str_social_title = Data_obj.getString(TAG_SOCIAL_TITLE);

						if (str_social_status.equalsIgnoreCase("Y")) {
							listConnect.add(new ConnectModel(str_social_id, str_social_title, str_social_link, str_social_logo));
						}
					}
				} else {
					GlobalData.showToast(str_Message, aiContext);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			loadListView();
		}
	}

	public void loadListView() {
		ConnectSocialFragment frag = this;
		AdapterConnect offerListAdapter = new AdapterConnect(frag, aiContext, listConnect);
		lvConnect.setAdapter(offerListAdapter);

		lvConnect.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int aiPos, long arg3) {

			}
		});
	}
	public static void onUpdateView(Context aiContext) {
		// TODO Auto-generated method stub
		if(aiContext!=null && creditWallet!=null)
			creditWallet.setText(PreferenceConnector.readInteger(aiContext, PreferenceConnector.WALLETPOINTS,0)+"");
		if (!countryIconUrl.isEmpty()) {
			if (image!=null)
				Picasso.with(aiContext)
					.load(countryIconUrl)
					.error(R.drawable.ic_launcher)
					.into(image);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!countryIconUrl.isEmpty()) {
			Picasso.with(aiContext)
					.load(countryIconUrl)
					.error(R.drawable.ic_launcher)
					.into(image);
		}
	}
}