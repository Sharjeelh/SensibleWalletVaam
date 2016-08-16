package com.cashmobi;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adapter.AdapterCountry;
import com.adapter.ExpandableListAdapter;
import com.archiveinfotech.crashreport.Utils;
import com.commonutility.FragmentTAG;
import com.commonutility.GlobalData;
import com.commonutility.GlobalVariables;
import com.commonutility.PreferenceConnector;
import com.commonutility.RoundImageView;
import com.commonutility.WebService;
import com.commonutility.WebServiceListener;
import com.model.CountryListModel;
import com.model.ExpandableModel;
import com.model.GroupItemModel;
import com.model.GroupModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.cashmobi.ActivityMainWallet.countryIconUrl;

public class ViewRewardsFragment extends Fragment implements WebServiceListener {
	private Context aiContext;
	private View aiView 	= null;
	private GlobalData 				gd;
	private RelativeLayout 			mDrawerLay;
	public boolean mAlreadyLoaded=false;
	private ExpandableListAdapter 	listAdapter;
	private ExpandableListView 		expListView;
	private List<String> listDataHeader;
	private List<ExpandableModel> listDataMainData = new ArrayList<ExpandableModel>();
	//	HashMap<List<GroupModel>, List<GroupItemModel>> listDataMainData;
	public static final String TAG_DATA = "Data";
	public static final String TAG_GIFTCARD_ID 			= "giftcard_id";
	public static final String TAG_COUNTRY_ID			= "country_id";
	public static final String TAG_OFFERS_DETAILS		= "offers_details";
	public static final String TAG_OFFER_ID				= "offer_id";
	public static final String TAG_OFFERS_REDEEM_AMOUNT	= "offers_redeem_amount";
	public static final String TAG_OFFER_PRICE			= "offer_price";
	public static final String TAG_GIFTCARD_LOGO		= "giftcard_logo";
	public static final String TAG_GIFTCARD_DESCRIPTION	= "giftcard_description";
	public static final String TAG_GIFTCARD_TITLE		= "giftcard_title";
	public static final String TAG_RESULT				= "RESULT";
	public static final String TAG_MESSAGE				= "Message";
	public static final String TAG_COUNTRY_PRICE_SIGN	= "country_price_sign";
	public static final String TAG_COUNTRY_NAME			= "country_name";
	public static final String TAG_COUNTRY_STATUS		= "country_status";
	public static final String TAG_COUNTRY_LOGO			= "country_logo";
	private static TextView txtCountry;
	private static TextView creditWallet;
    private static RoundImageView countryFlagImageView;
	static ImageView image;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (aiView == null) {
			aiView = inflater.inflate(R.layout.fragment_rewards, container, false);
		}
		image = (ImageView) aiView.findViewById(R.id.image_view_country_flag);
		Utils.setFontAllView((ViewGroup) aiView);
		return aiView;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if ( !mAlreadyLoaded) 
		{
			mAlreadyLoaded 		= true;
			aiContext 			= getActivity();
			aiView 				= getView();
			gd					= new GlobalData(aiContext);

			expListView 		= (ExpandableListView)aiView.findViewById(R.id.lvExp);
			txtCountry 			= (TextView)aiView.findViewById(R.id.txtcountry);
			creditWallet		= (TextView)aiView.findViewById(R.id.credit_wallet);
			countryFlagImageView= (RoundImageView)aiView.findViewById(R.id.image_view_country_flag);

			txtCountry.setText(PreferenceConnector.readString(aiContext,
					PreferenceConnector.COUNTRYSELECTED, ""));
			txtCountry.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (gd.isConnectingToInternet()) {
						callWebService(GlobalVariables.COUNTRYLIST, new HashMap<String, String>());
					}else {
						GlobalData.showToast(getResources().getString(R.string.error_no_internet), aiContext);
					}
				}
			});

			LoadCountryDetail();

			expListView.setOnGroupClickListener(new OnGroupClickListener() {
				@Override
				public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
					return true;
				}
			});

						// Listview on child click listener
						expListView.setOnChildClickListener(new OnChildClickListener() {
							@Override
							public boolean onChildClick(ExpandableListView parent, View v,
									int groupPosition, int childPosition, long id) {
								
								
								/*GlobalData.showToast(listDataHeader.get(groupPosition)
										+ " : "
										+ listDataMainData.get(groupPosition).getListDataGroupChild().get(childPosition).getStr_offer_price(), aiContext);*/
								Bundle bundle=new Bundle();
								bundle.putString("headerName",PreferenceConnector.readString(aiContext, 
										PreferenceConnector.COUNTRYSYMBOL, "$")+""+listDataMainData.get(groupPosition).getListDataGroupChild().get(childPosition).getStr_offers_redeem_amount()+" "+ listDataHeader.get(groupPosition));
								bundle.putString("amount",listDataMainData.get(groupPosition).getListDataGroupChild().get(childPosition).getStr_offers_redeem_amount());
								bundle.putString("offerid", listDataMainData.get(groupPosition).getListDataGroupChild().get(childPosition).getStr_offer_id());
								bundle.putString("offerImage", listDataMainData.get(groupPosition).getListDataGroupChild().get(childPosition).getStrOfferImage());
								bundle.putString("points", listDataMainData.get(groupPosition).getListDataGroupChild().get(childPosition).getStr_offer_price());
								Fragment fragment=new RedeemRewardFragment();
								fragment.setArguments(bundle);
								switchFragment(fragment , FragmentTAG.FragRedeemOffers);
								return false;
							}
						});

		}
		if(aiContext!=null)
			creditWallet.setText(PreferenceConnector.readInteger(aiContext, PreferenceConnector.WALLETPOINTS,
					0)+"");

		((ActivityMainWallet)getActivity()).initCountryFlagIcon(aiView);


	}

	public void LoadCountryDetail() {
		try {
			String[] keys 		= {"country_id"};
			String[] value = {PreferenceConnector.readString(aiContext, PreferenceConnector.COUNTRYID,
					"1")};

			if (gd.isConnectingToInternet()) {
				HashMap<String, String> hash = new HashMap<String, String>();
				for (int i = 0; i < keys.length; i++) {
					System.out.println(keys[i] + "......." + value[i]);
					hash.put(keys[i], value[i]);
				}

				callWebService(GlobalVariables.COUNTRYDETAIL, hash);
			} else {
				GlobalData.showToast(getResources().getString(R.string.error_no_internet), aiContext);
			}
		} catch (Exception ex) {
            //Context is probably null
            ex.printStackTrace();
        }
	}

	private void callWebService(String postUrl, HashMap<String, String> hash) {
		WebService webService	=	new WebService(aiContext, "", postUrl, hash, this, WebService.POST);
		webService.execute();
	}

	@Override
	public void onWebServiceActionComplete(String result, String url) {
		System.out.println(".....jsonresponse......"+result+"........."+url);
		List<GroupItemModel> listDataGroupChild;
		if (url.contains(GlobalVariables.COUNTRYDETAIL)) {
			listDataHeader 	= new ArrayList<String>();
			List<GroupModel> listDataGroup 				= new ArrayList<GroupModel>();
			try {
				JSONObject json = new JSONObject(result);
				String str_RESULT = json.getString(TAG_RESULT);
				String str_Message = json.getString(TAG_MESSAGE);

				listDataGroupChild 	= new ArrayList<GroupItemModel>();
				listDataGroup 		= new ArrayList<GroupModel>();
				listDataMainData 	= new ArrayList<ExpandableModel>();
				if (str_RESULT.equals("NO")) {
					listDataGroupChild 	= new ArrayList<GroupItemModel>();
					listDataGroup 		= new ArrayList<GroupModel>();
					listDataMainData 	= new ArrayList<ExpandableModel>();
					if (! (PreferenceConnector.readString(aiContext, PreferenceConnector.COUNTRYSELECTED, "")).equals("")) {
						GlobalData.showToast(getResources().getString(R.string.error_no_giftcard_for_country), aiContext);
					}
				}else {
					JSONArray Data = json.getJSONArray(TAG_DATA);
					for(int Data_i = 0; Data_i < Data.length(); Data_i++) {
						listDataGroupChild 	= new ArrayList<GroupItemModel>();

						JSONObject Data_obj=Data.getJSONObject(Data_i);
						String str_giftcard_id = Data_obj.getString(TAG_GIFTCARD_ID);
						String str_country_id = Data_obj.getString(TAG_COUNTRY_ID);
						String str_giftcard_logo = Data_obj.getString(TAG_GIFTCARD_LOGO);
						String str_giftcard_description = Data_obj.getString(TAG_GIFTCARD_DESCRIPTION);
						String str_giftcard_title = Data_obj.getString(TAG_GIFTCARD_TITLE);

						listDataHeader.add(str_giftcard_title);

						listDataGroup.add(new GroupModel(str_giftcard_id, str_country_id, 
								str_giftcard_logo, str_giftcard_description, str_giftcard_title));

						if (Data_obj.getString(TAG_OFFERS_DETAILS) != null || !Data_obj.getString(TAG_OFFERS_DETAILS).equals("null")) {
							JSONArray offers_details = Data_obj.getJSONArray(TAG_OFFERS_DETAILS);
							for(int offers_details_i = 0; offers_details_i < offers_details.length(); offers_details_i++) {
								JSONObject offers_details_obj=offers_details.getJSONObject(offers_details_i);
								String str_offer_id = offers_details_obj.getString(TAG_OFFER_ID);
								String str_offers_redeem_amount = offers_details_obj.getString(TAG_OFFERS_REDEEM_AMOUNT);
								String str_offer_price = offers_details_obj.getString(TAG_OFFER_PRICE);

								listDataGroupChild.add(new GroupItemModel(str_offer_id, str_offers_redeem_amount, str_offer_price,
										str_giftcard_logo));
							}
						}
						listDataMainData.add(new ExpandableModel(listDataGroup, listDataGroupChild));
					}
				}
			}catch (JSONException e){
				e.printStackTrace();
			}

			listAdapter = new ExpandableListAdapter(aiContext, listDataHeader, listDataMainData);
			expListView.setAdapter(listAdapter);

			for (int i = 0; i < listDataMainData.size(); i++) {
				expListView.expandGroup(i);
			}

		}else if (url.contains(GlobalVariables.COUNTRYLIST)) {
			countryList = new ArrayList<CountryListModel>();
			try {
				JSONObject json 	= new JSONObject(result);
				String str_RESULT 	= json.getString(TAG_RESULT);
				String str_Message 	= json.getString(TAG_MESSAGE);
				JSONArray Data = json.getJSONArray(TAG_DATA);
				for(int Data_i = 0; Data_i < Data.length(); Data_i++){
					JSONObject Data_obj	= Data.getJSONObject(Data_i);
					String str_country_price_sign 	= Data_obj.getString(TAG_COUNTRY_PRICE_SIGN);
					String str_country_name 		= Data_obj.getString(TAG_COUNTRY_NAME);
					String str_country_status 		= Data_obj.getString(TAG_COUNTRY_STATUS);
					String str_country_id 			= Data_obj.getString(TAG_COUNTRY_ID);
					String str_country_logo 		= Data_obj.getString(TAG_COUNTRY_LOGO);

					countryList.add(new CountryListModel(str_country_id, str_country_status, 
							str_country_price_sign, str_country_name, str_country_logo));
				}
			}catch (JSONException e){
				e.printStackTrace();
			}

			showCountryDialog(aiContext);
		}
	}

	List<CountryListModel> countryList = new ArrayList<CountryListModel>();
	private void showCountryDialog(final Context aiContext) {
		final Dialog dialog;
		dialog = new Dialog(aiContext);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.select_country_dialog);

		ListView list			= (ListView)dialog.findViewById(R.id.listView1);

		AdapterCountry offerListAdapter = new AdapterCountry(aiContext, countryList);
		list.setAdapter(offerListAdapter);

		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int aiPos, long arg3) {
				PreferenceConnector.writeString(aiContext, PreferenceConnector.COUNTRYSELECTED,
						countryList.get(aiPos).getStr_country_name());

				PreferenceConnector.writeString(aiContext, PreferenceConnector.COUNTRYSYMBOL,
						countryList.get(aiPos).getStr_country_price_sign());

				PreferenceConnector.writeString(aiContext, PreferenceConnector.COUNTRYID,
						countryList.get(aiPos).getStr_country_id());

				txtCountry.setText(PreferenceConnector.readString(aiContext, 
						PreferenceConnector.COUNTRYSELECTED, ""));

				LoadCountryDetail();

				dialog.dismiss();
				onUpdateView(aiContext);
			}
		});

		dialog.show();
	}
	public static void onUpdateView(Context aiContext) {
		// TODO Auto-generated method stub
		if(aiContext!=null && creditWallet!=null){
			creditWallet.setText(PreferenceConnector.readInteger(aiContext, PreferenceConnector.WALLETPOINTS,
					0)+"");
			txtCountry.setText(PreferenceConnector.readString(aiContext, 
					PreferenceConnector.COUNTRYSELECTED, ""));

//            ((ActivityMainWallet)aiContext).setCountryFlagIcon(countryFlagImageView);

		}
		if (!countryIconUrl.isEmpty()) {
			if (image!=null)
			Picasso.with(aiContext)
					.load(countryIconUrl)
					.error(R.drawable.ic_launcher)
					.into(image);

		}
	}
	private void switchFragment(Fragment fragment, String tag) {
		if (getActivity() == null)
			return;
		if (getActivity() instanceof ActivityMainWallet) {
			ActivityMainWallet mActivity = (ActivityMainWallet) getActivity();
			mActivity.switchContent(fragment, tag);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		LoadCountryDetail();
	}
}