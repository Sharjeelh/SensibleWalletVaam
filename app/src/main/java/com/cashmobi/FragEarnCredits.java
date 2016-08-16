package com.cashmobi;

import android.app.TabActivity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.adapter.AdapterEarnCredits;
import com.adscendmedia.sdk.ui.OffersActivity;
import com.applovin.adview.AppLovinInterstitialAd;
import com.applovin.adview.AppLovinInterstitialAdDialog;
import com.applovin.sdk.AppLovinAd;
import com.applovin.sdk.AppLovinAdDisplayListener;
import com.applovin.sdk.AppLovinSdk;
import com.archiveinfotech.crashreport.Utils;
import com.chartboost.sdk.CBLocation;
import com.chartboost.sdk.Chartboost;
import com.commonutility.FragmentTAG;
import com.commonutility.GlobalData;
import com.commonutility.GlobalVariables;
import com.commonutility.PreferenceConnector;
import com.commonutility.WebService;
import com.commonutility.WebServiceListener;
import com.commonutility.WebServiceWithoutDialog;
import com.helper.MyUtils;
import com.heyzap.sdk.ads.HeyzapAds;
import com.heyzap.sdk.ads.VideoAd;
import com.jirbo.adcolony.AdColony;
import com.jirbo.adcolony.AdColonyAd;
import com.jirbo.adcolony.AdColonyAdAvailabilityListener;
import com.jirbo.adcolony.AdColonyAdListener;
import com.jirbo.adcolony.AdColonyV4VCAd;
import com.model.EarnCreditModel;
import com.nativex.monetization.MonetizationManager;
import com.nativex.monetization.business.reward.Reward;
import com.nativex.monetization.communication.RedeemRewardData;
import com.nativex.monetization.enums.AdEvent;
import com.nativex.monetization.enums.NativeXAdPlacement;
import com.nativex.monetization.listeners.OnAdEventV2;
import com.nativex.monetization.listeners.RewardListener;
import com.nativex.monetization.listeners.SessionListener;
import com.nativex.monetization.mraid.AdInfo;
import com.playerize.superrewards.SuperRewards;
import com.revmob.RevMob;
import com.revmob.RevMobAdsListener;
import com.revmob.ads.interstitial.RevMobFullscreen;
import com.squareup.picasso.Picasso;
import com.supersonic.adapters.supersonicads.SupersonicConfig;
import com.supersonic.mediationsdk.sdk.Supersonic;
import com.supersonic.mediationsdk.sdk.SupersonicFactory;
import com.tapjoy.TJActionRequest;
import com.tapjoy.TJConnectListener;
import com.tapjoy.TJEarnedCurrencyListener;
import com.tapjoy.TJError;
import com.tapjoy.TJGetCurrencyBalanceListener;
import com.tapjoy.TJPlacement;
import com.tapjoy.TJPlacementListener;
import com.tapjoy.TJViewListener;
import com.tapjoy.Tapjoy;
import com.tapjoy.TapjoyConnectFlag;
import com.tapjoy.TapjoyLog;
import com.tapjoy.TapjoyViewType;
import com.trialpay.android.Trialpay;
import com.trialpay.android.TrialpayEvent;
import com.unity3d.ads.android.IUnityAdsListener;
import com.unity3d.ads.android.UnityAds;
import com.vungle.publisher.AdConfig;
import com.vungle.publisher.EventListener;
import com.vungle.publisher.VunglePub;

import net.adxmi.android.interstitial.Interstitial;
import net.adxmi.android.os.EarnPointsOrderInfo;
import net.adxmi.android.os.EarnPointsOrderList;
import net.adxmi.android.os.OffersManager;
import net.adxmi.android.os.PointsEarnNotify;
import net.adxmi.android.os.PointsManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import ly.persona.sdk.PersonaSdk;

import static com.cashmobi.ActivityMainWallet.countryIconUrl;

public class FragEarnCredits extends Fragment implements  WebServiceListener,  AdColonyAdAvailabilityListener,
OnAdEventV2,SessionListener,RewardListener, PointsEarnNotify,AppLovinAdDisplayListener, IUnityAdsListener {
	private Context aiContext;
	private View aiView = null;
	private GlobalData gd;
	public boolean mAlreadyLoaded=false;
	int totalRewardAmount = 0;
	private static final String CATEGORY_OFFERWALL = "Offerwall";

	private String[] strCreditOfferName;

	private Integer[] intCreditOfferImage = {
            R.drawable.ic_input_code,
            R.drawable.ic_invite_friends,
            R.drawable.ic_rate_five,
            R.drawable.ic_daily_reward,
			R.drawable.ic_offerwall_logo,
            R.drawable.ic_offerwall_logo,
            R.drawable.ic_offerwall_logo,
            R.drawable.ic_offerwall_logo,
            R.drawable.ic_offerwall_logo,
            R.drawable.ic_offerwall_logo,
            R.drawable.ic_offerwall_logo,
            R.drawable.ic_offerwall_logo,
            R.drawable.ic_offerwall_logo,
            R.drawable.ic_offerwall_logo,
            R.drawable.ic_offerwall_logo,
            R.drawable.ic_offerwall_logo,
            R.drawable.ic_offerwall_logo,
            R.drawable.ic_offerwall_logo,
            //R.drawable.ic_offerwall_logo,
            //R.drawable.ic_offerwall_logo,
            //R.drawable.ic_offerwall_logo,
	};

	private String[] strCreditOfferDetail = {
			"Input Your Friend’s Invite ID to Earn Credits.\nYou Can Only Do This Once.",
			"Invite an Unlimited Number of Friends to\nSensible Wallet to Earn Credits",
            "Rate us Five Stars and Earn 50 credits!",
			"Get Your Daily Login Bonus!\nCollect Credits Every Day\n",
//            "Try Apps for at least 3 minutes",
//            "Try Apps for at least 3 minutes",
//			"Try Apps for at least 3 minutes",
			"Try Apps for at Least 3 Minutes",
			"Try Apps for at Least 3 Minutes",
			"Try Apps for at Least 3 Minutes",
			"Try Apps for at Least 3 Minutes",
			"Try Apps for at Least 3 Minutes",
			"Try Apps for at Least 3 Minutes",
            "Try Apps for at Least 3 Minutes ",
//            "Watch video To Get "+GlobalVariables.VIDEO_POINTS_NEW+" credits",
//            "Watch video To Get "+GlobalVariables.VIDEO_POINTS_NEW+" credits",
//            "Watch video To Get "+GlobalVariables.VIDEO_POINTS_NEW+" credits",
            "Watch video To Get "+GlobalVariables.VIDEO_POINTS_NEW+" credits",
            "Watch video To Get "+GlobalVariables.VIDEO_POINTS_NEW+" credits",
            //"Watch video To Get "+GlobalVariables.VIDEO_POINTS_NEW+" credits",
            //"Watch video To Get "+GlobalVariables.VIDEO_POINTS_NEW+" credits"
	};

	private String[] strCreditOfferCredits = new String[strCreditOfferDetail.length];

	private List<EarnCreditModel> listEarnCredits = new ArrayList<>();
	private ListView listEarnCredit;
	private static TextView creditWallet;

	private Supersonic mMediationAgent;
	private String mUserId;
	private String mAppKey;


	TrialpayEvent tpButtonClickEvent;

	public static final String TAG = "TapjoyEasyApp";
	private TJPlacement offerwallPlacement;

	// variable to control if app is ready to show ads
	private Boolean _canShowAds = false;

	// adcolony
	String mAdColonyAppId;
	String mAdColonyZoneId;

	Handler button_text_handler;
	Runnable button_text_runnable;
	AppLovinInterstitialAdDialog adDialog;
	// get the VunglePub instance
	final VunglePub vunglePub = VunglePub.getInstance();
	boolean AdColonyAdSLoaded=false;
	private RevMob revmob;
	private RevMobFullscreen video;

	static ImageView image;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (aiView == null) {
			aiView = inflater.inflate(R.layout.fragment_earn_credits, container, false);
		}
		image = (ImageView) aiView.findViewById(R.id.image_view_country_flag);
//Chartboost.startWithAppId(getActivity(), getString(R.string.api_chartboost_app_id), getString(R.string.api_chartboost_app_signature));

	UnityAds.init(getActivity(),getString(R.string.unity_ads_id),this);
	UnityAds.setDebugMode(Boolean.parseBoolean(getString(R.string.unity_ads_debug_mode)));
	UnityAds.setTestMode(Boolean.parseBoolean(getString(R.string.unity_ads_test_mode)));
		/*adDialog = AppLovinInterstitialAd.create(AppLovinSdk.getInstance(getActivity()), getActivity());
		adDialog.setAdDisplayListener(this);*/


		/*revmob = RevMob.startWithListener(getActivity(), new RevMobAdsListener(){
			@Override
			public void onRevMobSessionIsStarted() {
				createVideo();

			}

			public void onRevMobVideoLoaded() {
				// You can already show it here if you prefer
			}

			@Override
			public void onRevMobSessionNotStarted(String s) {
				super.onRevMobSessionNotStarted(s);
			}

			@Override
			public void onRevMobAdNotReceived(String s) {
				super.onRevMobAdNotReceived(s);
			}
		});*/


		Utils.setFontAllView((ViewGroup) aiView);
		/*VideoAd.setOnStatusListener(new HeyzapAds.OnStatusListener() {
			@Override
			public void onShow(String s) {
				VideoAd.fetch();
				upDatePoints(GlobalVariables.VIDEO_POINTS_NEW, "Heyzap");
			}

			@Override
			public void onClick(String s) {

			}

			@Override
			public void onHide(String s) {

			}

			@Override
			public void onFailedToShow(String s) {

			}

			@Override
			public void onAvailable(String s) {

			}

			@Override
			public void onFailedToFetch(String s) {

			}

			@Override
			public void onAudioStarted() {

			}

			@Override
			public void onAudioFinished() {

			}

		});*/



		return aiView;
	}

	/*private void createVideo() {
		video = revmob.createVideo(getActivity(), new RevMobAdsListener(){

			@Override
			public void onRevMobVideoFinished() {
				super.onRevMobVideoFinished();
				//createVideo();
				upDatePoints(GlobalVariables.VIDEO_POINTS_NEW, "Revmob");

			}

			@Override
			public void onRevMobAdDismissed() {
			}

			@Override
			public void onRevMobAdNotReceived(String s) {
				super.onRevMobAdNotReceived(s);
			}

			@Override
			public void onRevMobEulaWasRejected() {
				super.onRevMobEulaWasRejected();
			}
		});

	}*/

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		//	GlobalData.showToast(mAlreadyLoaded+"Credit"+savedInstanceState, getActivity());
		if (!mAlreadyLoaded) 
		{
			mAlreadyLoaded 		= true;
			aiContext 			= getActivity();

			/*appLovin.preload(new AppLovinAdLoadListener() {
				@Override
				public void adReceived(AppLovinAd appLovinAd) {
					Toast.makeText(getActivity(),"received applovin",Toast.LENGTH_SHORT).show();

				}

				@Override
				public void failedToReceiveAd(int i) {
					Toast.makeText(getActivity(),"Failed to receive applovin",Toast.LENGTH_SHORT).show();
				}
			});*/
			aiView 				= getView();
			gd					= new GlobalData(aiContext);

            mAdColonyAppId = getResources().getString(R.string.api_ad_colony_app_id);
            mAdColonyZoneId = getResources().getString(R.string.api_ad_colony_zone_id);

			strCreditOfferName = new String[] {
					getResources().getString(R.string.offer_input_invitation_code)	,
					getResources().getString(R.string.offer_invite_friends)	,
                    getResources().getString(R.string.offer_rate_app)	,
					getResources().getString(R.string.offer_daily_reward)	,
//					getResources().getString(R.string.offer_persona)	,
//					getResources().getString(R.string.offer_trialpay)	,
					getResources().getString(R.string.offer_super_sonic)	,
                    getResources().getString(R.string.offer_adxmi)	,
//					getResources().getString(R.string.offer_tapjoy)	,
                    getResources().getString(R.string.offer_adgate_rewards)	,
					getResources().getString(R.string.offer_nativex)	,
					getResources().getString(R.string.offer_ascend_media)	,
					getResources().getString(R.string.offer_cpalead)	,
					getResources().getString(R.string.offer_super_rewards)	,
//					getResources().getString(R.string.offer_unity),
//					getResources().getString(R.string.offer_chartboost),
//					getResources().getString(R.string.offer_applovin),
					getResources().getString(R.string.offer_adcolony),
					//getResources().getString(R.string.offer_heyzap),
					//getResources().getString(R.string.offer_revmob),
					getResources().getString(R.string.offer_vungle)	,
			};

			for (int i = 0; i < strCreditOfferName.length; i++) {
				if (i == 0) {
					strCreditOfferCredits[i] = PreferenceConnector.readString(getActivity(), 
							PreferenceConnector.INVITEFRIENDREWARD, "") + " Credits"; 
				}else if (i == 1) {
					strCreditOfferCredits[i] = PreferenceConnector.readString(getActivity(), 
							PreferenceConnector.INVITEFRIENDREWARD, "") + " Credits";
				}else if (i == 2) {
                    strCreditOfferCredits[i] = "50 Credits";
                } else if(i == 3) {
                    strCreditOfferCredits[i] = PreferenceConnector.readString(getActivity(),
                            PreferenceConnector.DAILYREWARDPOINTS, "") + " Credits";
				}else {
					strCreditOfferCredits[i] = "";
				}
			}

			listEarnCredit		= (ListView)aiView.findViewById(R.id.listearncredit);
			creditWallet		= (TextView)aiView.findViewById(R.id.credit_wallet);

            setupAXDMI();
			loadListView();
//			setUpPersonaSdk();
//			trailPay();
			setUpSuperSonic();
//			connectToTapjoy();
			setUpVungle();
//			setupSuperRewards();
			setUpAdColony();

        }

		((ActivityMainWallet)getActivity()).initCountryFlagIcon(aiView);

		if(aiContext!=null)
			creditWallet.setText(PreferenceConnector.readInteger(aiContext, PreferenceConnector.WALLETPOINTS,0)+"");

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}
	public void loadListView() {
		String shouldLoadRateOffer = getString(R.string.show_rate_offer);
		for (int i = 0; i < strCreditOfferName.length; i++) {
			if(i==2 && shouldLoadRateOffer.equals("false")){
				continue;
			}
            EarnCreditModel model = new EarnCreditModel(strCreditOfferName[i], strCreditOfferDetail[i], strCreditOfferCredits[i],
                    intCreditOfferImage[i], false);
            model.setTag(offerTags[i]);

            if(model.getImage() == R.drawable.ic_input_code) {
                if(PreferenceConnector.readBoolean(aiContext, PreferenceConnector.INPUT_INTIVTE_CODE_COMPLETED, false)) {
                    continue;
                }
            } else if(model.getImage() == R.drawable.ic_rate_five) {
                if(PreferenceConnector.readBoolean(aiContext, PreferenceConnector.RATE_APP_COMPLETED, false)) {
                    continue;
                }
            }

            listEarnCredits.add(model);
		}

		FragEarnCredits frag = this;
		AdapterEarnCredits offerListAdapter = new AdapterEarnCredits(frag, aiContext, listEarnCredits);
		listEarnCredit.setAdapter(offerListAdapter);

		listEarnCredit.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int aiPos, long arg3) {

                AdapterEarnCredits adapter = (AdapterEarnCredits)arg0.getAdapter();

                EarnCreditModel model = adapter.getItem(aiPos);

                switch(model.getTag()) {
                    case "InputCode":
                        switchFragment(new InputInvitationCodeFragment(), FragmentTAG.FragInputInvitationCode);
                        break;
                    case "InviteFriends":
						FragmentTabHost tabhost = (FragmentTabHost) getActivity().findViewById(android.R.id.tabhost);
						tabhost.setCurrentTab(2);
                        break;
                    case "RateFive":
                        Uri uri = Uri.parse("market://details?id=" + aiContext.getPackageName());
                        Intent marketIntent = new Intent(Intent.ACTION_VIEW, uri);
                        marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|
                                Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET|
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        try {
                            startActivity(marketIntent);
                            PreferenceConnector.writeBoolean(aiContext, PreferenceConnector.RATE_APP_COMPLETED, true);
                            adapter.removeItem(model);
                        } catch (ActivityNotFoundException ex) {
                            GlobalData.showToast(getResources().getString(R.string.error_open_google_play), aiContext);
                        }
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								upDatePoints("50", getResources().getString(R.string.app_name));
							}
						}, 30000);
                        break;
                    case "DailyReward":
                        switchFragment(new DailyRewardFragment(), FragmentTAG.FragDailyReward);
                        break;
                    default:
                        initOffer(model.getTag());
                        break;
                }
			}
		});
	}

    private String[] offerTags = {
            "InputCode",
            "InviteFriends",
            "RateFive",
            "DailyReward",
//			"PS",
//            "TP",
			"SS",
            "ADXMI",
//            "TJ",
            "AR",
			"NX",
            "AM",
            "CL",
			"SR",
//            "UT",
//            "CB",
//            "AL",
            "AC",
            //"HZ",
            //"RM",
			"VG",

    };

    private void initOffer(String tag) {
        switch(tag) {
            case "ADXMI":
                OffersManager.getInstance(aiContext).showOffersWall();
				MyUtils.sendEventToGoogleAnalytics(getActivity().getApplication(),CATEGORY_OFFERWALL,"ADXMI");
                break;
            case "SS":
                if (mMediationAgent.isOfferwallAvailable()) {
					mMediationAgent.showOfferwall();
					MyUtils.sendEventToGoogleAnalytics(getActivity().getApplication(), CATEGORY_OFFERWALL, "SuperSonic");

				}

                break;
            case "AM":
                String publisherId = getResources().getString(R.string.api_ascendmedia_publisher_id);
                String adwallId = getResources().getString(R.string.api_ascendmedia_adwall_id);
                String subId1=PreferenceConnector.readString(aiContext, PreferenceConnector.WALLETID, "");
                Intent intent = OffersActivity.getIntentForOfferWall(getActivity(), publisherId, adwallId, subId1);
                startActivity(intent);
				MyUtils.sendEventToGoogleAnalytics(getActivity().getApplication(), CATEGORY_OFFERWALL, "AdscendMedia");

				break;
            case "NX":
                OnButtonMultiofferClick(); //NativeX API
				MyUtils.sendEventToGoogleAnalytics(getActivity().getApplication(), CATEGORY_OFFERWALL, "nativeX");

				break;
            case "AR":
                Bundle bundle=new Bundle();
                bundle.putString("url", "http://wall.adgaterewards.com/oK2c/"+PreferenceConnector.readString(aiContext, PreferenceConnector.WALLETID, ""));
                Fragment fragment=new CPLeadCreditScreen();
                fragment.setArguments(bundle);
                switchFragment(fragment , FragmentTAG.FragEARNCREDIT);

						/*Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://wall.adgaterewards.com/naiY/"+PreferenceConnector.readString(aiContext, PreferenceConnector.WALLETID, "")));
						startActivity(browserIntent);*/
				MyUtils.sendEventToGoogleAnalytics(getActivity().getApplication(),CATEGORY_OFFERWALL,"AdGateRewards");

                break;
            case "TP":
                tpButtonClickEvent.isAvailable(new TrialpayEvent.IsAvailableCallback() {
					@Override
					public void isAvailableCallback(boolean isAvailable) {
						System.out.println("*********************************" + isAvailable);
						//Update UI
						if (isAvailable) {
							tpButtonClickEvent.fire();
							MyUtils.sendEventToGoogleAnalytics(getActivity().getApplication(), CATEGORY_OFFERWALL, "TrialPay");

						} else {
							GlobalData.showToast("Please wait", aiContext);
						}
					}
				});

                break;
            case "PS":
                PersonaSdk.getInstance().setUserId(PreferenceConnector.readString(aiContext, PreferenceConnector.WALLETID, ""));
                PersonaSdk.showOffers();
				MyUtils.sendEventToGoogleAnalytics(getActivity().getApplication(), CATEGORY_OFFERWALL, "Personaly");

				break;
            case "CL":
                bundle=new Bundle();
                bundle.putString("url",
                        "https://cpalead.com/mobile/locker/?pub=" + getResources().getString(R.string.api_cpalead_publisher_id) +
                        "&gateid=" + getResources().getString(R.string.api_cpalead_gateway_id) +
                        "&subid="+PreferenceConnector.readString(aiContext, PreferenceConnector.WALLETID, ""));
                fragment=new CPLeadCreditScreen();
                fragment.setArguments(bundle);
                switchFragment(fragment, FragmentTAG.FragEARNCREDIT);
				MyUtils.sendEventToGoogleAnalytics(getActivity().getApplication(), CATEGORY_OFFERWALL, "CPALead");

				break;
            case "VG":
                final AdConfig overrideConfig = new AdConfig();
                overrideConfig.setIncentivized(true);
                overrideConfig.setSoundEnabled(true);
                vunglePub.playAd(overrideConfig);
				MyUtils.sendEventToGoogleAnalytics(getActivity().getApplication(), CATEGORY_OFFERWALL, "Vungle");

				break;
			case "TJ":
				callShowOffers();
				MyUtils.sendEventToGoogleAnalytics(getActivity().getApplication(), CATEGORY_OFFERWALL, "ADXMI");

				break;
			case "SR":
				String userId = PreferenceConnector.readString(aiContext, PreferenceConnector.WALLETID, "TapJoy");
				String appHash = getResources().getString(R.string.api_super_rewards_hash);
				SuperRewards sr = new SuperRewards(getResources(), getResources().getString(R.string.api_super_rewards_app_package_name));
				sr.showOffers(getActivity(), appHash, userId);
				break;
			case "AC":
				startAdColony();
				MyUtils.sendEventToGoogleAnalytics(getActivity().getApplication(), CATEGORY_OFFERWALL, "AdColony");

				break;

			case "CB":
				//Chartboost.cacheInterstitial(CBLocation.LOCATION_DEFAULT);
				Chartboost.showInterstitial(CBLocation.LOCATION_DEFAULT);
				MyUtils.sendEventToGoogleAnalytics(getActivity().getApplication(), CATEGORY_OFFERWALL, "ChartBoost");

				break;
			case "AL":
				if(adDialog.isAdReadyToDisplay()){
					adDialog.show();
					MyUtils.sendEventToGoogleAnalytics(getActivity().getApplication(), CATEGORY_OFFERWALL, "AppLovin");

				}else{
					Toast.makeText(getActivity(),"Ad not ready",Toast.LENGTH_SHORT).show();
				}
				break;
			case "HZ":
				if(VideoAd.isAvailable()){
					VideoAd.display(getActivity());
					MyUtils.sendEventToGoogleAnalytics(getActivity().getApplication(), CATEGORY_OFFERWALL, "HeyZap");

				}else{
					Toast.makeText(getActivity(),"Ad not ready",Toast.LENGTH_SHORT).show();

				}
				break;
			case "RM":
				if(revmob!=null && revmob.isVideoLoaded() && video!=null){
					MyUtils.sendEventToGoogleAnalytics(getActivity().getApplication(),CATEGORY_OFFERWALL,"RevMob");
					video.show();
				}else{
					Toast.makeText(getActivity(),"Revmob video not ready not ready",Toast.LENGTH_SHORT).show();

				}
				break;
			case "UT":
				if(UnityAds.canShow()){
					UnityAds.show();
				}
            default:
                break;
        }
    }

    @Override
    public void onPointEarn(Context context, EarnPointsOrderList earnPointsOrderList) {
        for(int i = 0; i < earnPointsOrderList.size(); i++) {
            EarnPointsOrderInfo info = earnPointsOrderList.get(i);
            upDatePoints(String.valueOf(info.getPoints()), "ADXMI");
        }
    }

    private void startAdColony() {
		if(AdColonyAdSLoaded)
		{
			AdColonyV4VCAd ad = new AdColonyV4VCAd(mAdColonyZoneId).withListener(new AdColonyAdListener() {
				public void onAdColonyAdStarted(AdColonyAd ad) { }

				public void onAdColonyAdAttemptFinished( AdColonyAd ad ) {
					if(ad.shown()){
						upDatePoints(GlobalVariables.VIDEO_POINTS_NEW,"AdColony");
					}
				}
			});
			ad.show();
		} else {
			GlobalData.showToast("Please wait", getActivity());
		}
	}

	@Override
	public void onRedeem(RedeemRewardData rewardData) {
		//Take possession of the balances returned here. 
		totalRewardAmount = 0;
		for (final Reward reward : rewardData.getRewards()) {
			Log.d("SampleApp", "Reward: rewardName:" + reward.getRewardName()
					+ " rewardId:" + reward.getRewardId()
					+ " amount:" + Double.toString(reward.getAmount()));
			// add the reward amount to the total
			totalRewardAmount += reward.getAmount();


		}
		/* getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), totalRewardAmount+"..", 100).show();
			}
		});*/
		upDatePoints(totalRewardAmount+"","NativeX");
		//rewardData.showAlert(getActivity());
	}

	public void upDatePoints(String points,String sdkName) {
		// TODO Auto-generated method stub
		String[] keys 		= {"wallet_id", "points","sdkName"};
		String[] value 		= {PreferenceConnector.readString(aiContext, PreferenceConnector.WALLETID, ""),points,sdkName};

		HashMap<String, String> hash	=	new HashMap<String, String>();
		for (int i = 0; i < keys.length; i++) {
			System.out.println(keys[i]+ "......." + value[i]);
			hash.put(keys[i], value[i]);
		}

		if (gd.isConnectingToInternet()) {
			callWebService(GlobalVariables.UPDATE_POINTS, hash);
		}else {
			GlobalData.showToast(getResources().getString(R.string.error_no_internet), aiContext);
		}
	}


	private void callWebService(String postUrl, HashMap<String, String> hash) {
		WebServiceWithoutDialog webService	=	new WebServiceWithoutDialog(aiContext, "", postUrl, hash, this, WebService.POST);
		webService.execute();
	}

	@Override
	public void onWebServiceActionComplete(String result, String url) {
		System.out.println(result+".........jsonresponse....."+url);
		try {
			JSONObject json = new JSONObject(result);
			String str_RESULT = json.getString(DailyRewardFragment.TAG_RESULT);
			String str_Message = json.getString(DailyRewardFragment.TAG_MESSAGE);
			//			GlobalData.showToast(str_Message, aiContext);GlobalData.showToast(str_Message, aiContext);
			if (str_RESULT.equals("YES")) {
				JSONObject Data_obj 		= json.getJSONObject(DailyRewardFragment.TAG_DATA);
				String str_user_points 		= Data_obj.getString(DailyRewardFragment.TAG_USER_POINTS);
				//String str_rewards_checkin 	= Data_obj.getString(DailyRewardFragment.TAG_REWARDS_CHECKIN);

				PreferenceConnector.writeInteger(aiContext, PreferenceConnector.WALLETPOINTS, 
						Integer.parseInt(str_user_points));
				onUpdateView(aiContext);
			}/*else {
				GlobalData.showToast(str_Message, aiContext);
			}*/
		} catch (JSONException e){
			e.printStackTrace();
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


	//---------------------------------------------------------------- CREDIT OFFER CODE 

	/*// Appgrade
	private void showInterstitial() {
		if (Interstitial.isReady(true)) {
			Interstitial.show(getActivity(), true);
		}
		else {
			Log.v(TAG, "Interstitial is not yet ready");
		}
	}
	 */
	// SuperSonic ***********************************************************
	private void setUpSuperSonic(){
		// this should a unique id per user when calling this init methods
		// note that init methods should be only called once per session
		//mUserId  = "123%40abc.com";
		mUserId  = PreferenceConnector.readString(aiContext, PreferenceConnector.WALLETID, "");
		mAppKey = getResources().getString(R.string.api_super_sonic_app_key);

		// get the mediation publisher instance
		mMediationAgent = SupersonicFactory.getInstance();

		/*TextView sdkVersion = (TextView) findViewById(R.id.textViewSDKVersion);
			sdkVersion.setText(SupersonicUtils.getSDKVersion());*/


		/*// register the interstitial listener and initialize the interstitial
			mMediationAgent.setInterstitialListener(mInterstitialListener);
			mMediationAgent.initInterstitial(this,mAppKey,mUserId);*/


		// register for offerwall callbacks and initialise the offerwall
		//mMediationAgent.setOfferwallListener(mOfferwallListener);

		// Change config at runtime example #1 - set client side callbacks for the offerwall product
		SupersonicConfig.getConfigObj().setClientSideCallbacks(false);
		mMediationAgent.initOfferwall(getActivity(),mAppKey, mUserId);

		// register the rewarded video listener and initalize the rewarded video
		/*mMediationAgent.setRewardedVideoListener(mRewardedVideoListener);
			//Please refer to assets/supersonic.config for initial configurations
			mMediationAgent.initRewardedVideo(this,mAppKey, mUserId);*/
	}

	private void trailPay(){
		Trialpay.setVerbosity(true);
		Trialpay.initApp(getActivity(), getResources().getString(R.string.api_trialpay_app_key));
		
		// Set the user id. Not required: if not called then will be generated by the SDK
		Trialpay.setSid(PreferenceConnector.readString(aiContext, PreferenceConnector.WALLETID, ""));

		System.out.println(Trialpay.getSid()+":::::::::::::::::::tpButtonClickEvent.getFullName()");
		// Create reference to "button1" event of "button_clicked" type
		tpButtonClickEvent = Trialpay.event.buttonClicked("cashmobi");
		

	}

    private void setupAXDMI() {
		Interstitial.getInstance(getActivity()).loadAds();
		Interstitial.getInstance(getActivity()).setAnimationType(Interstitial.ANIM_ADVANCE);
    }

	// Persona
	private void setUpPersonaSdk() {
		PersonaSdk.init (getActivity(), getResources().getString(R.string.api_persona_app_id), PreferenceConnector.readString(aiContext, PreferenceConnector.WALLETID, ""));
	}
	/**
	 * Attempts to connect to Tapjoy
	 */
	private void connectToTapjoy() {
		// OPTIONAL: For custom startup flags.
		Hashtable<String, Object> connectFlags = new Hashtable<String, Object>();
		connectFlags.put(TapjoyConnectFlag.ENABLE_LOGGING, "true");
		
		// If you are not using Tapjoy Managed currency, you would set your own user ID here.
		//	connectFlags.put(TapjoyConnectFlag.USER_ID, "A_UNIQUE_USER_ID");

		// Connect with the Tapjoy server.  Call this when the application first starts.
		// REPLACE THE SDK KEY WITH YOUR TAPJOY SDK Key.
		//String tapjoySDKKey = "u6SfEbh_TA-WMiGqgQ3W8QECyiQIURFEeKm0zbOggubusy-o5ZfXp33sTXaD";
		String tapjoySDKKey = getResources().getString(R.string.api_tapjoy_sdk_key);
//		String tapjoySDKKey = "u6SfEbh_TA-WMiGqgQ3W8QECyiQIURFEeKm0zbOggubusy-o5ZfXp33sTXaD";


		//Tapjoy.setGcmSender(PreferenceConnector.readString(aiContext, PreferenceConnector.GSMREGID, ""));

		// NOTE: This is the only step required if you're an advertiser.
		Tapjoy.connect(getActivity(), tapjoySDKKey, connectFlags, new TJConnectListener() {
			@Override
			public void onConnectSuccess() {
				FragEarnCredits.this.onConnectSuccess();
			}

			@Override
			public void onConnectFailure() {
				FragEarnCredits.this.onConnectFail();
			}
		});
	}
	/**
	 * Handles a successful connect to Tapjoy. Pre-loads direct play placement
	 * and sets up Tapjoy listeners
	 */
	public void onConnectSuccess() {
		Tapjoy.setUserID(PreferenceConnector.readString(aiContext, PreferenceConnector.WALLETID, ""));
		Tapjoy.setEarnedCurrencyListener(new TJEarnedCurrencyListener() {
			@Override
			public void onEarnedCurrency(final String currencyName, final int amount) {
			}
		});
		Tapjoy.setTapjoyViewListener(new TJViewListener() {
			@Override
			public void onViewWillOpen(int viewType) {
				TapjoyLog.i(TAG, getViewName(viewType) + " is about to open");
			}

			@Override
			public void onViewWillClose(int viewType) {
				TapjoyLog.i(TAG, getViewName(viewType) + " is about to close");
			}

			@Override
			public void onViewDidOpen(int viewType) {
				TapjoyLog.i(TAG, getViewName(viewType) + " did open");
			}

			@Override
			public void onViewDidClose(int viewType) {
				TapjoyLog.i(TAG, getViewName(viewType) + " did close");

				// Best Practice: We recommend calling getCurrencyBalance as often as possible so the user�s balance is always up-to-date.
				Tapjoy.getCurrencyBalance(new TJGetCurrencyBalanceListener() {

					@Override
					public void onGetCurrencyBalanceResponseFailure(String arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onGetCurrencyBalanceResponse(final String currencyName, final int balance) {
						Log.i(TAG, "currencyName: " + currencyName);
						Log.i(TAG, "balance: " + balance);
					}
				});
			}
		});
	}

	/**
	 * Handles a failed connect to Tapjoy
	 */
	public void onConnectFail() {
		Log.e(TAG, "Tapjoy connect call failed");
		//updateTextInUI("Tapjoy connect failed!");
	}

	@Override
	public void onStart() {
		super.onStart();
		Tapjoy.onActivityStart(getActivity());
	}

	@Override
	public void onStop() {
		super.onStop();
		Tapjoy.onActivityStop(getActivity());
		PointsManager.getInstance(getContext()).unRegisterPointsEarnNotify(this);
	}

	private void callShowOffers() {
		// Construct TJPlacement to show Offers web view from where users can download the latest offers for virtual currency.
		offerwallPlacement = new TJPlacement(aiContext, "CashMobi Rewards", new TJPlacementListener() {
		//offerwallPlacement = new TJPlacement(aiContext, "offerwall_unit", new TJPlacementListener() {
			@Override
			public void onRequestSuccess(final TJPlacement placement) {
				/* updateTextInUI("onRequestSuccess for placement " + placement.getName());

	             if (!placement.isContentAvailable()) {
	               updateTextInUI("No Offerwall content available");
	             }*/
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if (!placement.isContentAvailable()) {
							// TODO Auto-generated method stub
							GlobalData.showToast("No Offerwall content available", getActivity());
						}
					}
				});

			}

			@Override
			public void onRequestFailure(TJPlacement placement, TJError error) {
				// updateTextInUI("Offerwall error: " + error.message);


			}

			@Override
			public void onContentReady(TJPlacement placement) {
				TapjoyLog.i(TAG, "onContentReady for placement " + placement.getName());
				placement.showContent();
			}

			@Override
			public void onContentShow(TJPlacement placement) {
				TapjoyLog.i(TAG, "onContentShow for placement " + placement.getName());
			}

			@Override
			public void onContentDismiss(TJPlacement placement) {
				TapjoyLog.i(TAG, "onContentDismiss for placement " + placement.getName());
			}

			@Override
			public void onPurchaseRequest(TJPlacement placement, TJActionRequest request, String productId) {
			}

			@Override
			public void onRewardRequest(TJPlacement placement, TJActionRequest request, String itemId, int quantity) {
			}
		});
		offerwallPlacement.requestContent();
	}

	/**
	 * Helper method to get the name of each view type.
	 *
	 * @param type
	 *            Tapjoy view type from the view notification callbacks.
	 * @return Name of the view.
	 */
	public String getViewName(int type) {
		String name = "";
		switch (type) {
		case TapjoyViewType.OFFER_WALL_AD:
			name = "offer wall ad";
			break;
		case TapjoyViewType.OTHER_AD:
			name = "other ad";
			break;
		case TapjoyViewType.PLACEMENT:
			name = "placement";
			break;
		default:
			name = "undefined type: " + type;
			break;
		}
		return name;
	}


	// NativeXAd Start *************************************************************
	//Listener callback for createSession
	public void createSessionCompleted(boolean success, boolean isOfferWallEnabled, String message) {
		if (success) {	
			// A session with our servers was established successfully.
			// The app is ready to show ads.
			System.out.println("Wahoo! We are now ready to show an ad!");
			_canShowAds = true;

			//We are fetching all the ads here as this is a one scene app. 
			//However, in a typical integration you will want to spread these calls out in your game. 
			//It is recommended that you add these calls in an area that would allow around 5 seconds before attempting to show
			MonetizationManager.fetchAd(getActivity(), NativeXAdPlacement.Main_Menu_Screen, this);
		} else {
			// Establishing a session with our servers failed.
			// The app will be unable to show ads until a session is established.
			System.out.println("Oh no! Something isn't set up correctly - re-read the documentation or ask customer support for help https://selfservice.nativex.com/Help");
			_canShowAds = false;
		}

	}

	public void OnButtonMultiofferClick() {
		android.util.Log.d("Click button", "OnButtonMultiofferClick");

		if(_canShowAds == true)
			//shows an ad that is already fetched and ready to show instantly
			//NOTE: if the ad has not been fetched yet this method will not do anything
			MonetizationManager.showReadyAd(getActivity(), NativeXAdPlacement.Main_Menu_Screen, this);
		else
			android.util.Log.d("ShowAd","Can't show ads but still button clicked");
	}

	// NativeXAd End ************************************************************* 


	// AdColony Start ************************************************************* 
	private void setUpAdColony(){
		AdColony.configure( getActivity(), "version:1.0,store:google", mAdColonyAppId, mAdColonyZoneId);
		// version - arbitrary application version
		// store   - google or amazon

		// Add ad availability listener
		AdColony.addAdAvailabilityListener(this);

		// Disable rotation if not on a tablet-sized device (note: not
		// necessary to use AdColony).
		if ( !AdColony.isTablet() )
		{
			getActivity().setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
		}

		// Handler and Runnable for updating button text based on ad availability listener
		button_text_handler = new Handler();
		button_text_runnable = new Runnable()
		{
			public void run()
			{
				AdColonyAdSLoaded=true;
				//AdColonyButton.setText("Show Video");
				//button10.setOnClickListener(
				/*new View.OnClickListener()
			    	{
			          public void onClick( View v )
			    	  {
			    		AdColonyVideoAd ad = new AdColonyVideoAd( mAdColonyZoneId ).withListener( MainActivity.this );
			    		ad.show();
			    	  }
			    	});*/
			}
		};
	}

	//Ad Availability Change Callback - update button text
	public void onAdColonyAdAvailabilityChange(boolean available, String zone_id) 
	{
		if (available) button_text_handler.post(button_text_runnable);
	}
	// AdColony End ************************************************************* 

	// Vungle Start ************************************************************* 
	private void setUpVungle(){
		final String app_id = getResources().getString(R.string.api_vungle_app_id);
		vunglePub.init(getActivity(), app_id);
		vunglePub.setEventListeners(vungleListener);
		final AdConfig overrideConfig = new AdConfig();

		// set any configuration options you like.
		overrideConfig.setIncentivized(true);
		overrideConfig.setSoundEnabled(true);

		
	}

	private final EventListener vungleListener = new EventListener() {
		@Override
		public void onVideoView(boolean isCompletedView, int watchedMillis, int videoDurationMillis) {
			if(isCompletedView) {
				upDatePoints(GlobalVariables.VIDEO_POINTS,"Vungle");
			}
		}
		@Override
		public void onAdStart() { }
		@Override
		public void onAdUnavailable(String reason) { }
		@Override
		public void onAdEnd(boolean wasCallToActionClicked) { }
		@Override
		public void onAdPlayableChanged(boolean isAdPlayable) { }
	};

	@Override
	public void onPause() {
		super.onPause();
		AdColony.pause();
		vunglePub.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		try {
			setUpAdColony();
		} catch (Exception e) {
			// TODO: handle exception
		}
		MonetizationManager.createSession(getActivity(), getResources().getString(R.string.api_nativex_app_id), this);
		MonetizationManager.setRewardListener(this);
		AdColony.resume(getActivity());
		vunglePub.onResume();
		UnityAds.changeActivity(getActivity());

	}
	
	@Override
	public void onEvent(AdEvent arg0, AdInfo arg1, String arg2) {
		// TODO Auto-generated method stub

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
	public void adDisplayed(AppLovinAd appLovinAd) {
		upDatePoints(GlobalVariables.VIDEO_POINTS_NEW,"AppLovin");
	}

	@Override
	public void adHidden(AppLovinAd appLovinAd) {

	}

	@Override
	public void onHide() {

	}

	@Override
	public void onShow() {

	}

	@Override
	public void onVideoStarted() {

	}

	@Override
	public void onVideoCompleted(String s, boolean b) {
		upDatePoints(GlobalVariables.VIDEO_POINTS_NEW,"Unity");
	}

	@Override
	public void onFetchCompleted() {

	}

	@Override
	public void onFetchFailed() {

	}
}