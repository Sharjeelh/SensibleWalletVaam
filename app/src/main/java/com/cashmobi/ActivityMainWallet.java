package com.cashmobi;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.adapter.AdapterCountry;
import com.applovin.sdk.AppLovinSdk;
import com.chartboost.sdk.CBLocation;
import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.ChartboostDelegate;
import com.chartboost.sdk.Libraries.CBLogging;
import com.chartboost.sdk.Model.CBError;
import com.commonutility.FragmentTAG;
import com.commonutility.GlobalData;
import com.commonutility.GlobalVariables;
import com.commonutility.PreferenceConnector;
import com.commonutility.WebService;
import com.commonutility.WebServiceListener;
import com.facebook.login.LoginManager;
import com.google.android.gcm.GCMRegistrar;
import com.helper.MyUtils;
import com.heyzap.sdk.ads.HeyzapAds;
import com.heyzap.sdk.ads.VideoAd;
import com.material.BaseActivity;
import com.model.CountryListModel;
import com.squareup.picasso.Picasso;

import net.adxmi.android.os.OffersManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.cashmobi.ActivityMainWallet.countryIconUrl;

@SuppressWarnings("deprecation")
public class ActivityMainWallet extends BaseActivity implements WebServiceListener, NavigationView.OnNavigationItemSelectedListener{
	private Context aiContext;
	private Fragment mContent;
	private TextView textError;
	private GlobalData gd;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private Animation slideUp, slideDown;
	private RelativeLayout layConnection, progressbarInternet;
	NavigationView mDrawerLay;
	public static final String TAG_MESSAGE="Message";
	public static final String TAG_DATA="Data";
	public static final String TAG_COUNTRY_PRICE_SIGN="country_price_sign";
	public static final String TAG_COUNTRY_NAME="country_name";
	public static final String TAG_COUNTRY_STATUS="country_status";
	public static final String TAG_COUNTRY_ID="country_id";
	public static final String TAG_COUNTRY_LOGO="country_logo";
	public static final String TAG_RESULT="RESULT";
//	ViewPager pager;
	FragmentPagerAdapter adapterTab;
    private ImageView countryFlagImageView;

    private Toolbar drawerToolbar;
    private TabLayout tabLayout;

	boolean isOutSidePage=false;

    boolean isShowFlagDialog = false;
	
	 /**
     * Tag used on log messages.
     */
    static final String TAG = "ActivityMainWallet";
    static final String DISPLAY_MESSAGE_ACTION =
            "com.androidhive.pushnotifications.DISPLAY_MESSAGE";
 
    static final String EXTRA_MESSAGE = "message";

	FragmentTabHost mTabHost;
	static Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		AppLovinSdk.initializeSdk(this);
		HeyzapAds.start(getString(R.string.heyzap_app_id), this);
		//HeyzapAds.startTestActivity(this);

		VideoAd.fetch();

		Chartboost.startWithAppId(this, getString(R.string.api_chartboost_app_id), getString(R.string.api_chartboost_app_signature));

		Chartboost.setLoggingLevel(CBLogging.Level.ALL);
		Chartboost.setDelegate(new ChartboostDelegate() {
			@Override
			public void didFailToLoadInterstitial(String location, CBError.CBImpressionError error) {
				super.didFailToLoadInterstitial(location, error);

			}

			@Override
			public void didCloseInterstitial(String location) {
				super.didCloseInterstitial(location);
				upDatePoints(GlobalVariables.VIDEO_POINTS_NEW,"Chartboost");

			}

			@Override
			public boolean shouldDisplayInterstitial(String location) {
				//Chartboost.showInterstitial(CBLocation.LOCATION_DEFAULT);

				return super.shouldDisplayInterstitial(location);

			}

			@Override
			public void didCacheInterstitial(String location) {
				super.didCacheInterstitial(location);
				//Toast.makeText(ActivityMainWallet.this, "interstitial cached", Toast.LENGTH_SHORT).show();
				//Chartboost.showInterstitial(CBLocation.LOCATION_DEFAULT);
				//Chartboost.showInterstitial(CBLocation.LOCATION_DEFAULT);

			}

		});

		Chartboost.onCreate(this);
		Chartboost.cacheInterstitial(CBLocation.LOCATION_DEFAULT);
		//Chartboost.showInterstitial(CBLocation.LOCATION_DEFAULT);

		/*ViewGroup viewGroup = (ViewGroup)((ViewGroup)this.findViewById(R.id.mainLayout));
		MyUtils.setFontAllView(viewGroup);*/

		aiContext 			= this;
		gd					= new GlobalData(aiContext);
		gd.setStatusBarColor();
		slideUp 			= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slideup);
		slideDown 			= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slidedown);

		layConnection 		= (RelativeLayout)findViewById(R.id.lay_connection);
		progressbarInternet = (RelativeLayout)findViewById(R.id.lay_dialog);
		textError 			= (TextView)findViewById(R.id.text_error);
		textError	 		= (TextView)findViewById(R.id.text_error);

		isShowFlagDialog = PreferenceConnector.readString(aiContext, PreferenceConnector.COUNTRYSELECTED, "").equals("");

        if(!PreferenceConnector.readBoolean(aiContext, PreferenceConnector.WELCOME_DIALOG_SHOWN, false)) {
			if(getString(R.string.show_disclaimer).equals("true")) {
				showWelcomeDialog();
			}
				PreferenceConnector.writeBoolean(aiContext, PreferenceConnector.WELCOME_DIALOG_SHOWN, true);

        }

        if (gd.isConnectingToInternet()) {
            callWebService(GlobalVariables.COUNTRYLIST, new HashMap<String, String>());
        }else {
            GlobalData.showToast(getResources().getString(R.string.error_no_internet), aiContext);
        }

		textError.setVisibility(View.VISIBLE);
		progressbarInternet.setVisibility(View.INVISIBLE);

		if (gd.isConnectingToInternet()) {
			hideNoConnectionError();
		}else {
			showErrorMessage(aiContext, GlobalVariables.ERRORIDINTERNETNOTAVAILABLE, "");
		}

		mDrawerLayout 	= (DrawerLayout)findViewById(R.id.drawer_layout);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.END);
		mDrawerLay		= (NavigationView)findViewById(R.id.navigation_view);
        mDrawerLay.setNavigationItemSelectedListener(this);
        TextView navigationTotalCreditTextView = (TextView)mDrawerLay.findViewById(R.id.nav_drawer_total_credits);
        navigationTotalCreditTextView.setText("Total Credits: " + PreferenceConnector.readInteger(aiContext,
                PreferenceConnector.WALLETPOINTS, 0));
        TextView navigationWalletIDText = (TextView)mDrawerLay.findViewById(R.id.nav_drawer_wallet_id);
        navigationWalletIDText.setText("Wallet ID: " + PreferenceConnector.readString(aiContext,
                PreferenceConnector.WALLETID, ""));
        TextView navigationUsernameText = (TextView)mDrawerLay.findViewById(R.id.nav_drawer_username);
        navigationUsernameText.setText(PreferenceConnector.readString(aiContext,
                PreferenceConnector.USERNAME, ""));
//		MyUtils.setFontAllView((RelativeLayout)findViewById(R.id.laymenu));
//		completedoffers			= (Button)mDrawerLay.findViewById(R.id.completedoffers);
//		MyRewards				= (Button)mDrawerLay.findViewById(R.id.MyRewards);
//		RateusonGooglePlay		= (Button)mDrawerLay.findViewById(R.id.RateusonGooglePlay);
//		CheckforUpdates			= (Button)mDrawerLay.findViewById(R.id.CheckforUpdates);
//		Assistance				= (Button)mDrawerLay.findViewById(R.id.Assistance);
//		FAQ						= (Button)mDrawerLay.findViewById(R.id.FAQ);
//		TermsOfServices			= (Button)mDrawerLay.findViewById(R.id.TermsOfServices);
//		Logout					= (Button)mDrawerLay.findViewById(R.id.Logout);
//
//		userIdEditText			= (EditText)mDrawerLay.findViewById(R.id.userIdEditText);
//		userEmailEditText		= (EditText)mDrawerLay.findViewById(R.id.userEmailEditText);
//		userPaypalEditText		= (EditText)mDrawerLay.findViewById(R.id.userPaypalEditText);
//		userFirstNameEditText	= (EditText)mDrawerLay.findViewById(R.id.userFirstNameEditText);
//		userLastEditText		= (EditText)mDrawerLay.findViewById(R.id.userLastEditText);
//
//		userIdEditText.setText(PreferenceConnector.readString(aiContext, PreferenceConnector.WALLETID,""));
//		userEmailEditText.setText(PreferenceConnector.readString(aiContext, PreferenceConnector.USERNAME,""));
//		userPaypalEditText.setText(PreferenceConnector.readString(aiContext, PreferenceConnector.PAYPAL_EMAIL,""));
//		userFirstNameEditText.setText(PreferenceConnector.readString(aiContext, PreferenceConnector.FIRST_NAME,""));
//		userLastEditText.setText(PreferenceConnector.readString(aiContext, PreferenceConnector.LAST_NAME,""));
//
//		tv_submit				= (TextView)mDrawerLay.findViewById(R.id.tv_submit);
//		tv_cancel				= (TextView)mDrawerLay.findViewById(R.id.tv_cancel);
//
//		appFirstName= (TextView)mDrawerLay.findViewById(R.id.firstname);
//		appLastName= (TextView)mDrawerLay.findViewById(R.id.lastname);
//		MyUtils.firstText(appFirstName);
//		MyUtils.lastName(appLastName);
//		scrollView = (ScrollView)mDrawerLay.findViewById(R.id.scrollbar_invitepage);

        drawerToolbar = (Toolbar)findViewById(R.id.drawer_toolbar);
        drawerToolbar.setTitle(getString(R.string.app_name));
        drawerToolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        drawerToolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openAndCloseDrawer();
            }
        });

//		completedoffers.setOnClickListener(this);
//		MyRewards.setOnClickListener(this);
//		RateusonGooglePlay.setOnClickListener(this);
//		CheckforUpdates.setOnClickListener(this);
//		Assistance.setOnClickListener(this);
//		FAQ.setOnClickListener(this);
//		TermsOfServices.setOnClickListener(this);
//		Logout.setOnClickListener(this);
//
//		tv_submit.setOnClickListener(this);
//		tv_cancel.setOnClickListener(this);
		layConnection.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				textError.setVisibility(View.INVISIBLE);
				progressbarInternet.setVisibility(View.VISIBLE);

				if (gd.isConnectingToInternet()) {
					hideNoConnectionError();
					mContent = new FragEarnCredits();
					getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, mContent).commit();
				}else {
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							textError.setVisibility(View.VISIBLE);
							progressbarInternet.setVisibility(View.INVISIBLE);
						}
					}, 1000);
					showErrorMessage(aiContext, GlobalVariables.ERRORIDINTERNETNOTAVAILABLE, "");
				}
			}
		});
		adapterTab = new GoogleMusicAdapter(getSupportFragmentManager());
		/*adapterTab = new GoogleMusicAdapter(getSupportFragmentManager());
		pager = (ViewPager)findViewById(R.id.pager);
		pager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
				MyUtils.sendScreenToGoogleAnalytics(getApplication(),"Screen : " + CONTENT[position]);
                String countryIconUrl = PreferenceConnector.readString(ActivityMainWallet.this, PreferenceConnector.COUNTRYLOGO, "");
                System.out.println("My Pager " + position);
                Fragment frag = ((GoogleMusicAdapter)pager.getAdapter()).getItem(position);
                View aiView = frag.getView();
                if(aiView == null) return;
                ImageView image = (ImageView) aiView.findViewById(R.id.image_view_country_flag);
                if (!countryIconUrl.isEmpty()) {
                    Picasso.with(aiContext)
                            .load(countryIconUrl)
                            .error(R.drawable.ic_launcher)
                            .into(image);
                }
				*//* if(position==0)
	                    ((FirstFragment)pager.getChildAt(position)).onUpdateView(); //onUpdateView is public function at 'FirstFragment', insert your code here

				 *//*
				*//*if(position==0)
					FragEarnCredits.onUpdateView(aiContext);
				if(position==1)
					// ((ViewRewardsFragment)adapterTab.getItem(position)).onUpdateView();
					ViewRewardsFragment.onUpdateView(aiContext);*//*
                //adapterTab.notifyDataSetChanged(); //this line will force all pages to be loaded fresh when changing between fragments
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });
		pager.setAdapter(adapterTab);

        tabLayout = (TabLayout)findViewById(R.id.sliding_tabs);
        for(int i = 0 ;i <pager.getAdapter().getCount(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(pager.getAdapter().getPageTitle(i)));
            LinearLayout layout = ((LinearLayout)((LinearLayout)tabLayout.getChildAt(0)).getChildAt(i));
            layout.setPadding(10, 0, 10 ,0);
        }
        tabLayout.setTabTextColors(getResources().getColor(R.color.md_grey_400), getResources().getColor(R.color.md_white_1000));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.md_white_1000));
        tabLayout.setBackgroundColor(getResources().getColor(R.color.material_color_primary));
        tabLayout.setSelectedTabIndicatorHeight(8);
        tabLayout.setupWithViewPager(pager);*/

//		Button copyrightButton = (Button)findViewById(R.id.navigation_button_footer);
//		copyrightButton.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent viewIntent = new Intent(Intent.ACTION_VIEW);
//				viewIntent.setData(Uri.parse("https://github.com/mikimn/"));
//				startActivity(viewIntent);
//			}
//		});

        customizeActionBar();

        registerReceiver(mHandleMessageReceiver, new IntentFilter(
				DISPLAY_MESSAGE_ACTION));


		initTabs();


	}

    public void initCountryFlagIcon(View aiView) {
        ImageView countryFlagImageView = (ImageView)aiView.findViewById(R.id.image_view_country_flag);
        countryFlagImageView.setClickable(true);
        countryFlagImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gd.isConnectingToInternet()) {
                    callWebService(GlobalVariables.COUNTRYLIST, new HashMap<String, String>());
                } else {
                    GlobalData.showToast(getResources().getString(R.string.error_no_internet), aiContext);
                }
            }
        });
    }

    @Override
	protected void initViews() {

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



	public static final String[] CONTENT = new String[] {"EARN CREDITS", "REWARDS", "INVITE", "CONNECT"};
	class GoogleMusicAdapter extends FragmentPagerAdapter {

        private Fragment[] frags;

		public GoogleMusicAdapter(FragmentManager fm) {
			super(fm);
            frags = new Fragment[] {new FragEarnCredits(),
                                    new ViewRewardsFragment(),
                                    new InviteFriendsFragment(),
                                    new ConnectSocialFragment()};
		}

		@Override
		public Fragment getItem(int position) {
//			Fragment frag = null;
			return frags[position];
		}

		@Override
		public CharSequence getPageTitle(int position) {
			String title = null;
			switch (position) {
			case 0:
				title = CONTENT[0];
				break;
			case 1:
				title = CONTENT[1];
				break;
			case 2:
				title = CONTENT[2];
				break;
			case 3:
				title = CONTENT[3];
				break;

			default:
				break;
			}
			return title;
		}

		@Override
		public int getCount() {
			return CONTENT.length;
		}
		@Override
		public int getItemPosition(Object object) {
		    return POSITION_UNCHANGED;
		}  
	}

	@SuppressLint("InflateParams")
	public void customizeActionBar() {


        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override public void onDrawerClosed(View drawerView) {
                hideKeyboard();
                super.onDrawerClosed(drawerView);
            }

            @Override public void onDrawerOpened(View drawerView) {
                hideKeyboard();
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);

		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
//        pager.setVisibility(View.VISIBLE);
//		tabLayout.setVisibility(View.VISIBLE);

		getToolbar().setTitle(R.string.app_name);
		mDrawerToggle.setDrawerIndicatorEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
		mDrawerToggle.syncState();

		Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarInner);

		toolbar.setNavigationOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openAndCloseDrawer();
			}
		});
		toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
					case android.R.id.home:
						openAndCloseDrawer();
						return true;
				}
				return false;
			}
		});
	}

	public void customizeActionBarWithBack(String string) {

		Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarInner);
        toolbar.setTitleTextColor(getResources().getColor(R.color.md_white_1000));
		toolbar.setTitle(string);

		setDisplayHomeAsUpEnabled(true, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switchBack();
				customizeActionBar();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch(item.getItemId()) {
					case android.R.id.home:
						switchBack();
						customizeActionBar();
						return true;
				}


				return false;
			}
		});
	}

	//	public static void showErrorMessage(Context aContext, int errorId, String strValue) {
	//		textError.setVisibility(View.VISIBLE);
	//		if (errorId == 0) {
	//			textError.setText(strValue);
	//		}else {
	//			if(errorId == GlobalData.ERRORIDNOTGETTINGDATAFROMSERVER) {
	//				textError.setText(GlobalData.getStringRes(aContext, R.string.errorgettingdatafromserver));
	//			}else if (errorId == GlobalData.ERRORIDINTERNETNOTAVAILABLE) {
	//				textError.setText(GlobalData.getStringRes(aContext, R.string.errorsplashinternetnotavailable));
	//			}
	//		}
	//	}
	//
	//	public static void hideErrorMessage() {
	//		textError.setVisibility(View.GONE);
	//	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                openAndCloseDrawer();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showErrorMessage(Context aiContext, int errorId, String strValue) {
		layConnection.setVisibility(View.VISIBLE);

		textError.setVisibility(View.VISIBLE);
		progressbarInternet.setVisibility(View.INVISIBLE);
		if (errorId == GlobalVariables.CUSTOMIZEERROR) {
			textError.setText(strValue);
		}else {
			if(errorId == GlobalVariables.ERRORIDNOTGETTINGDATAFROMSERVER) {
				textError.setText(GlobalData.getStringRes(aiContext, R.string.error_data_from_server));
			}else if (errorId == GlobalVariables.ERRORIDINTERNETNOTAVAILABLE) {
				textError.setText(GlobalData.getStringRes(aiContext, R.string.error_splash_no_internet));
			}
		}

		if (errorId == 1) {
		}
	}

	public void hideNoConnectionError() {
		layConnection.setVisibility(View.GONE);
		textError.setVisibility(View.INVISIBLE);
		progressbarInternet.setVisibility(View.VISIBLE);
	}

	public void switchContent(Fragment fragment, String tag) {
		hideNoConnectionError();
		hideKeyboard();

//        pager.setVisibility(View.GONE);
//        tabLayout.setVisibility(View.GONE);
		mTabHost.setVisibility(View.GONE);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.content_frame, fragment).addToBackStack(tag).commit();
    }
	
	

	public void switchBack() {
		isOutSidePage=false;
		hideKeyboard();
		FragmentManager fragmentManager = getSupportFragmentManager();
		if(fragmentManager.getBackStackEntryCount() > 0){
			fragmentManager.popBackStack();
		}
		mTabHost.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
		super.onPostCreate(savedInstanceState);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	private void hideKeyboard() {
		InputMethodManager inputManager = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
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

	private void callWebService(String postUrl, HashMap<String, String> hash) {
		WebService webService	=	new WebService(aiContext, "", postUrl, hash, this, WebService.POST);
		webService.execute();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (!(adapterTab == null)) {

			adapterTab.notifyDataSetChanged();


	    }
		Chartboost.onResume(this);
		/*GlobalData.showToast("resume", this);*/
		
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		FragEarnCredits.onUpdateView(aiContext);
		ViewRewardsFragment.onUpdateView(aiContext);
		InviteFriendsFragment.onUpdateView(aiContext);
		ConnectSocialFragment.onUpdateView(aiContext);
		customizeActionBar();
        switchBack();
Chartboost.onBackPressed();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Chartboost.onPause(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Chartboost.onStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		Chartboost.onStop(this);
	}

	public static String countryIconUrl = "";
	@Override
	public void onWebServiceActionComplete(String result, String url) {
		System.out.println("..pretext.." + result + ".........jsonresponse....." + url);
		countryList = new ArrayList<CountryListModel>();
		if (url.contains(GlobalVariables.COUNTRYLIST)) {



			try {
				JSONObject json = new JSONObject(result);
				String str_RESULT = json.getString(TAG_RESULT);
				String str_Message = json.getString(TAG_MESSAGE);
				JSONArray Data = json.getJSONArray(TAG_DATA);
				for(int Data_i = 0; Data_i < Data.length(); Data_i++){
					JSONObject Data_obj=Data.getJSONObject(Data_i);
					String str_country_price_sign 	= Data_obj.getString(TAG_COUNTRY_PRICE_SIGN);
					String str_country_name 		= Data_obj.getString(TAG_COUNTRY_NAME);
					String str_country_status 		= Data_obj.getString(TAG_COUNTRY_STATUS);
					String str_country_id 			= Data_obj.getString(TAG_COUNTRY_ID);
					String str_country_logo 		= Data_obj.getString(TAG_COUNTRY_LOGO);

                    if(str_country_id.equals(PreferenceConnector.readString(aiContext, PreferenceConnector.COUNTRYID, "2"))) {
                        countryIconUrl = str_country_logo;
						FragEarnCredits.onUpdateView(aiContext);
					}

					countryList.add(new CountryListModel(str_country_id, str_country_status, 
							str_country_price_sign, str_country_name, str_country_logo));


				}
			}catch (JSONException e){
				e.printStackTrace();
			}

            if(isShowFlagDialog) {
                showCountryDialog(aiContext);
            }
            isShowFlagDialog = true;
            /*for(int i = 0; i < adapterTab.getCount(); i++) {
                android.app.Fragment frag = getFragmentManager().findFragmentByTag("Rewards");
                View aiView = frag.getView();
                if(aiView == null) continue;
                ImageView image = (ImageView) aiView.findViewById(R.id.image_view_country_flag);
                if (!countryIconUrl.isEmpty()) {
                    Picasso.with(aiContext)
                            .load(countryIconUrl)
                            .error(R.drawable.ic_launcher)
                            .into(image);
                }
            }*/
		}
		if (url.contains(GlobalVariables.UPDATE_PROFILE)) {
			try {
				JSONObject json 	= new JSONObject(result);
				String str_RESULT 	= json.getString(TAG_RESULT);
				String str_Message 	= json.getString(TAG_MESSAGE);
				GlobalData.showToast(str_Message, aiContext);



				if (str_RESULT.equals("YES")) {
					openAndCloseDrawer();
					JSONObject Data_obj 		= json.getJSONObject(TAG_DATA);
					String str_first_name 			= Data_obj.getString(ActivityLogin.TAG_FIRST_NAME);
					String str_last_name 			= Data_obj.getString(ActivityLogin.TAG_LAST_NAME);
					String str_email 				= Data_obj.getString(ActivityLogin.TAG_PAYPAY_EMAIL);

					PreferenceConnector.writeString(aiContext, PreferenceConnector.FIRST_NAME, str_first_name);
					PreferenceConnector.writeString(aiContext, PreferenceConnector.LAST_NAME, str_last_name);
					PreferenceConnector.writeString(aiContext, PreferenceConnector.PAYPAL_EMAIL, str_email);

//					userPaypalEditText.setText(PreferenceConnector.readString(aiContext, PreferenceConnector.PAYPAL_EMAIL,""));
//					userFirstNameEditText.setText(PreferenceConnector.readString(aiContext, PreferenceConnector.FIRST_NAME,""));
//					userLastEditText.setText(PreferenceConnector.readString(aiContext, PreferenceConnector.LAST_NAME,""));


				}
			} catch (JSONException e){
				e.printStackTrace();
			}
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

				PreferenceConnector.writeString(aiContext, PreferenceConnector.COUNTRYLOGO,
						countryList.get(aiPos).getStr_country_logo());

				String countryIconUrl = countryList.get(aiPos).getStr_country_logo();
				ActivityMainWallet.countryIconUrl = countryIconUrl;
				dialog.dismiss();
				ViewRewardsFragment.onUpdateView(aiContext);
				ConnectSocialFragment.onUpdateView(aiContext);
				FragEarnCredits.onUpdateView(aiContext);
				InviteFriendsFragment.onUpdateView(aiContext);
				/*ViewRewardsFragment viewRewardsFragment = new ViewRewardsFragment();
				viewRewardsFragment.LoadCountryDetail();*/
				/*for (int i = 0; i < pager.getAdapter().getCount(); i++) {
					Fragment frag = ((GoogleMusicAdapter) pager.getAdapter()).getItem(i);
					if (frag instanceof ViewRewardsFragment) {
						((ViewRewardsFragment)frag).LoadCountryDetail();
					}
					View aiView = frag.getView();
					if (aiView == null) continue;
					ImageView image = (ImageView) aiView.findViewById(R.id.image_view_country_flag);
					if (!countryIconUrl.isEmpty()) {
						Picasso.with(aiContext)
								.load(countryIconUrl)
								.error(R.drawable.ic_launcher)
								.into(image);
					}
				}*/
			}
		});

		dialog.show();
	}

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
			case R.id.drawer_home:
				openAndCloseDrawer();
				switchBack();
				break;
			case R.id.drawer_input_code:
				isOutSidePage = true;
                openAndCloseDrawer();
				switchContent(new InputInvitationCodeFragment(), FragmentTAG.FragInputInvitationCode);
				break;
            case R.id.drawer_offer_history:
                isOutSidePage=true;
                openAndCloseDrawer();
                switchContent(new CompletedOfferListFragment(), FragmentTAG.FragCompletedOffers);
                break;
            case R.id.drawer_reward_history:
                isOutSidePage=true;
                openAndCloseDrawer();
                switchContent(new MyRewardsListFragment(), FragmentTAG.FragMyRewardsOffer);
                break;
            case R.id.drawer_rate:
                openAndCloseDrawer();
                goToGooglePlay();
                break;
            case R.id.drawer_check_updates:
                openAndCloseDrawer();

                PackageManager manager = this.getPackageManager();
                PackageInfo info;
                try {
                    info = manager.getPackageInfo(this.getPackageName(), 0);
                    if(info.versionCode<Float.parseFloat(PreferenceConnector.readString(aiContext, PreferenceConnector.APPVERSION, "1"))){
                        showCheckUpdateDialog(aiContext, getResources().getString(R.string.error_old_version), 20, "UPDATE NOW", "");
                    }
                    else{
                        showCheckUpdateDialog(aiContext, getResources().getString(R.string.message_updated_version), 20, "OK", "");
                    }

                } catch (NameNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case R.id.drawer_assistance:
                isOutSidePage=true;
                openAndCloseDrawer();
                switchContent(new AssistanceFragment(), FragmentTAG.FragAssistance);
                break;
            case R.id.drawer_faq:
                isOutSidePage=true;
                openAndCloseDrawer();
                switchContent(new FAQ(), FragmentTAG.FragFAQ);
                break;
            case R.id.drawer_terms:
                isOutSidePage=true;
                openAndCloseDrawer();
                switchContent(new TermsOfServiceFragemnt(), FragmentTAG.FragTermsOf);
                break;
            case R.id.drawer_logout:
                //PreferenceConnector.cleanPrefrences(aiContext);
                PreferenceConnector.writeString(aiContext, PreferenceConnector.USERNAME, "");

                PreferenceConnector.writeString(aiContext, PreferenceConnector.FIRST_NAME, "");
                PreferenceConnector.writeString(aiContext, PreferenceConnector.LAST_NAME, "");
                PreferenceConnector.writeString(aiContext, PreferenceConnector.PAYPAL_EMAIL, "");

                PreferenceConnector.writeString(aiContext, PreferenceConnector.PASSWORD, "");

                PreferenceConnector.writeString(aiContext, PreferenceConnector.USERID, "");
                PreferenceConnector.writeString(aiContext, PreferenceConnector.WALLETID, "");
                PreferenceConnector.writeInteger(aiContext, PreferenceConnector.WALLETPOINTS,
                        Integer.parseInt("0"));
                PreferenceConnector.writeBoolean(aiContext, PreferenceConnector.ISREMEMBER, false);
                PreferenceConnector.writeBoolean(aiContext, PreferenceConnector.ISFBLOGIN, false);
                PreferenceConnector.writeBoolean(aiContext, PreferenceConnector.ISGPLOGIN, false);
                Intent aiIntent = new Intent(aiContext, ActivityLogin.class);
                startActivity(aiIntent);
				LoginManager.getInstance().logOut();
                finish();
                break;
            case R.id.drawer_edit_profile:

                if (gd.isConnectingToInternet()) {
                    isOutSidePage = true;
                    openAndCloseDrawer();
                    switchContent(new FragEditProfile(), FragmentTAG.FragEditProfile);
                    break;
//                    onUpdateProfile();
                }else {
                    GlobalData.showToast(getResources().getString(R.string.error_no_internet), aiContext);
                }
                break;
			case R.id.drawer_view_invited_friends:
				isOutSidePage=true;
				openAndCloseDrawer();
				switchContent(new InvitedFriendListFragment(), FragmentTAG.FragInvitedFriends);
				break;
           /* case R.id.drawer_design:
                showDesignedByDialog();*/
            default:
                break;
        }
        return false;
    }
//
//    @Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		switch (v.getId()) {
//		case R.id.completedoffers:
//			isOutSidePage=true;
//			openAndCloseDrawer();
//			switchContent(new CompletedOfferListFragment(), FragmentTAG.FragCompletedOffers);
//			break;
//		case R.id.MyRewards:
//			isOutSidePage=true;
//			openAndCloseDrawer();
//			switchContent(new MyRewardsListFragment(), FragmentTAG.FragMyRewardsOffer);
//			break;
//		case R.id.RateusonGooglePlay:
//			openAndCloseDrawer();
//			goToGooglePlay();
//			break;
//		case R.id.CheckforUpdates:
//			openAndCloseDrawer();
//
//			PackageManager manager = this.getPackageManager();
//			PackageInfo info;
//			try {
//				info = manager.getPackageInfo(this.getPackageName(), 0);
//				if(info.versionCode<Float.parseFloat(PreferenceConnector.readString(aiContext, PreferenceConnector.APPVERSION, "1"))){
//					showCheckUpdateDialog(aiContext, "You're using the old version!", 20, "UPDATE NOW", "");
//				}
//				else{
//					showCheckUpdateDialog(aiContext, "You're using the latest version!", 20, "OK", "");
//				}
//
//			} catch (NameNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			break;
//		case R.id.Assistance:
//			isOutSidePage=true;
//			openAndCloseDrawer();
//			switchContent(new AssistanceFragment(), FragmentTAG.FragAssistance);
//			break;
//		case R.id.FAQ:
//			isOutSidePage=true;
//			openAndCloseDrawer();
//			switchContent(new FAQ(), FragmentTAG.FragFAQ);
//			break;
//		case R.id.TermsOfServices:
//			isOutSidePage=true;
//			openAndCloseDrawer();
//			switchContent(new TermsOfServiceFragemnt(), FragmentTAG.FragTermsOf);
//			break;
//		case R.id.Logout:
//			//PreferenceConnector.cleanPrefrences(aiContext);
//			PreferenceConnector.writeString(aiContext, PreferenceConnector.USERNAME, "");
//
//			PreferenceConnector.writeString(aiContext, PreferenceConnector.FIRST_NAME, "");
//			PreferenceConnector.writeString(aiContext, PreferenceConnector.LAST_NAME, "");
//			PreferenceConnector.writeString(aiContext, PreferenceConnector.PAYPAL_EMAIL, "");
//
//			PreferenceConnector.writeString(aiContext, PreferenceConnector.PASSWORD, "");
//
//			PreferenceConnector.writeString(aiContext, PreferenceConnector.USERID, "");
//			PreferenceConnector.writeString(aiContext, PreferenceConnector.WALLETID, "");
//			PreferenceConnector.writeInteger(aiContext, PreferenceConnector.WALLETPOINTS,
//					Integer.parseInt("0"));
//			PreferenceConnector.writeBoolean(aiContext, PreferenceConnector.ISREMEMBER, false);
//			PreferenceConnector.writeBoolean(aiContext, PreferenceConnector.ISFBLOGIN, false);
//			PreferenceConnector.writeBoolean(aiContext, PreferenceConnector.ISGPLOGIN, false);
//			Intent aiIntent = new Intent(aiContext, ActivityLogin.class);
//			startActivity(aiIntent);
//			finish();
//			break;
//		case R.id.tv_submit:
//
//			if (gd.isConnectingToInternet()) {
////				onUpdateProfile();
//			}else {
//				GlobalData.showToast(getResources().getString(R.string.error_internet), aiContext);
//			}
//			break;
//		case R.id.tv_cancel:
//			openAndCloseDrawer();
//			break;
//		default:
//			break;
//		}
//
//	}

	private  void openAndCloseDrawer(){
		if (mDrawerLayout.isDrawerOpen(mDrawerLay)) {
			mDrawerLayout.closeDrawer(mDrawerLay);
			//imgSlideMenu.setImageResource(R.drawable.ico_left);
		}else {
			mDrawerLayout.openDrawer(mDrawerLay);
			//imgSlideMenu.setImageResource(R.drawable.menuopener);
		}
	}
	private void goToGooglePlay(){
		Uri uri = Uri.parse("market://details?id=" + aiContext.getPackageName());
		Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
		// To count with Play market backstack, After pressing back button, 
		// to taken back to our application, we need to add following flags to intent. 
		goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
				Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
				Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
		try {
			startActivity(goToMarket);
		} catch (ActivityNotFoundException e) {
			startActivity(new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://play.google.com/store/apps/details?id=" + aiContext.getPackageName())));
		}
	}

	private void showCheckUpdateDialog(final Context aiContext, final String text, int textSize, 
			final String btnOneText, String btnTwoText) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Check for updates");
        builder.setMessage(text);
        builder.setPositiveButton(btnOneText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                if (btnOneText.equals("UPDATE NOW")) {
                    if (gd.isConnectingToInternet()) {
                        goToGooglePlay();
                    } else {
                        GlobalData.showDialog(aiContext, getResources().getString(R.string.error_no_internet), 16, "OK", "");
                    }
                }
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
        Button buttonOk = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonOk.setBackgroundColor(getResources().getColor(R.color.material_color_accent));
        buttonOk.setTextColor(getResources().getColor(R.color.md_white_1000));
	}

    private void showWelcomeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.welcome_disclaimer_title));
        builder.setMessage(getString(R.string.welcome_disclaimer_message)
				);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
		builder.setNegativeButton("More Questions", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				isOutSidePage=true;
				openAndCloseDrawer();
				switchContent(new FAQ(), FragmentTAG.FragFAQ);
			}
		});

        AlertDialog alert = builder.create();
        alert.show();
        Button buttonOk = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonOk.setBackgroundColor(getResources().getColor(R.color.material_color_accent));
        buttonOk.setTextColor(getResources().getColor(R.color.md_white_1000));

		Button buttonFAQ = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
		buttonFAQ.setBackgroundColor(getResources().getColor(R.color.material_color_accent));
		buttonFAQ.setTextColor(getResources().getColor(R.color.md_white_1000));
    }

	private void showDesignedByDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Design & Copyright");
		builder.setMessage("Copyright Sensible Mobile LLC 2016©\nDesigned with ♥ by Miki Mints\ngithub.com/mikimn/");
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("Visit Me!", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Intent viewIntent = new Intent(Intent.ACTION_VIEW);
				viewIntent.setData(Uri.parse("https://github.com/mikimn/"));
				startActivity(viewIntent);
			}
		});

		AlertDialog alert = builder.create();
		alert.show();
		Button buttonOk = alert.getButton(DialogInterface.BUTTON_POSITIVE);
		buttonOk.setBackgroundColor(getResources().getColor(R.color.material_color_accent));
		buttonOk.setTextColor(getResources().getColor(R.color.md_white_1000));

		Button buttonFAQ = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
		buttonFAQ.setBackgroundColor(getResources().getColor(R.color.material_color_accent));
		buttonFAQ.setTextColor(getResources().getColor(R.color.md_white_1000));
	}

//	private void onUpdateProfile() {
//		int response = 0;
//		response = gd.emptyEditTextError(
//				new EditText[] { userFirstNameEditText,userLastEditText},
//				new String[] { "Enter first name", "Enter last name" });
//
//		if (! GlobalData.isEmailValid(userPaypalEditText.getText().toString().trim())) {
//			response++;
//			userPaypalEditText.setError("Not an valid email");
//		}
//		if (response == 0) {
//			String strFname 			= userFirstNameEditText.getText().toString().trim();
//			String strLname 			= userLastEditText.getText().toString().trim();
//			String strEmailID 			= userPaypalEditText.getText().toString().trim();
//
//			String[] keys = {"user_id","first_name", "last_name", "paypal_account", "password" };
//			String[] value = {
//					PreferenceConnector.readString(aiContext, PreferenceConnector.USERID, ""),
//					strFname,
//					strLname,
//					strEmailID,
//					"",};
//
//			HashMap<String, String> hash = new HashMap<String, String>();
//			for (int i = 0; i < keys.length; i++) {
//				System.out.println(keys[i] + "......." + value[i]);
//				hash.put(keys[i], value[i]);
//			}
//			callWebService(GlobalVariables.UPDATE_PROFILE, hash);
//		}
//	}

	/**
     * Receiving push messages
     * */
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	
        	
            String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
            
           // String type 		= intent.getExtras().getString("type");
    		
    		String userpoints 	= intent.getExtras().getString("userpoints");
    		if(!userpoints.equals("0"))
    		{
            //Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();
           PreferenceConnector.writeInteger(aiContext, PreferenceConnector.WALLETPOINTS, 
					Integer.parseInt(userpoints));
            FragEarnCredits.onUpdateView(aiContext);
    		ViewRewardsFragment.onUpdateView(aiContext);
    		InviteFriendsFragment.onUpdateView(aiContext);
			ConnectSocialFragment.onUpdateView(aiContext);
    		}
        }
    };
     
    @Override
    protected void onDestroy() {
        
        try {
            unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(this);
        } catch (Exception e) {
            Log.e("UnRegister Error", "> " + e.getMessage());
        }

		OffersManager.getInstance(this).onAppExit();
        super.onDestroy();
    }
    static void displayMessage(Context context, String message,String userpoints) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra("userpoints", userpoints);
        context.sendBroadcast(intent);
        
    }


	private void initTabs() {
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			@Override
			public void onTabChanged(final String tabId) {
				Fragment fg = getSupportFragmentManager().findFragmentByTag(tabId);
				Log.d(TAG, "onTabChanged(): " + tabId + ", fragment " + fg);

				if (fg == null) {
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							Fragment fg = getSupportFragmentManager().findFragmentByTag(tabId);
							Log.d(TAG, "onTabChanged() delay 50ms: " + tabId + ", fragment " + fg);
							View aiView = fg.getView();
							ImageView image = (ImageView) aiView.findViewById(R.id.image_view_country_flag);
							if (!countryIconUrl.isEmpty()) {
								Picasso.with(aiContext)
										.load(countryIconUrl)
										.error(R.drawable.ic_launcher)
										.into(image);
							}
						}
					}, 50);
				}
			}
		});
		mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

		View view1 = LayoutInflater.from(ActivityMainWallet.this).inflate(R.layout.tab_indicator_earn_credits, null);
		View view2 = LayoutInflater.from(ActivityMainWallet.this).inflate(R.layout.tab_indicator_rewards, null);
		View view3 = LayoutInflater.from(ActivityMainWallet.this).inflate(R.layout.tab_indicator_invite, null);
		View view4 = LayoutInflater.from(ActivityMainWallet.this).inflate(R.layout.tab_indicator_connect, null);
		View view5 = LayoutInflater.from(ActivityMainWallet.this).inflate(R.layout.tab_indicator_earn_credits, null);

		mTabHost.addTab(mTabHost.newTabSpec(getString(R.string.earn_cred)).setIndicator(view1), FragEarnCredits.class, null);
		mTabHost.addTab(mTabHost.newTabSpec(getString(R.string.rewards)).setIndicator(view2), ViewRewardsFragment.class, null);
		mTabHost.addTab(mTabHost.newTabSpec(getString(R.string.invite)).setIndicator(view3), InviteFriendsFragment.class, null);
		mTabHost.addTab(mTabHost.newTabSpec(getString(R.string.connect)).setIndicator(view4), ConnectSocialFragment.class, null);

	}

}
