package com.cashmobi;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.archiveinfotech.crashreport.Utils;
import com.commonutility.GlobalData;
import com.commonutility.GlobalVariables;
import com.commonutility.PreferenceConnector;
import com.commonutility.WebService;
import com.commonutility.WebServiceListener;
import com.helper.MyUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

public class DailyRewardFragment extends Fragment implements OnClickListener, WebServiceListener {
	private static Context aiContext;
	private View aiView = null;
	private boolean mAlreadyLoaded=false;
	private GlobalData gd;
	private Button btnCheckIn;
	public static final String TAG_MESSAGE	= "Message";
	public static final String TAG_RESULT	= "RESULT";
	public static final String TAG_DATA		= "Data";
	public static final String TAG_USER_POINTS		= "user_points";
	public static final String TAG_REWARDS_CHECKIN 	= "rewards_checkin";
	private TextView txtDailyRewardTop, txtDailyRewardBottom;
	
	/**
	 * Default notification counter 
	 * 1 for reset counter change on first phase timer time 
	 * 2 for reset counter change on second phase timer time 
	 * 3 for reset counter change on both phase timer time
	 * 4 for reset counter change on any one phase timer time
	 */
	public   static final int 	  	  DEFAULT_RESET_COUNTER_CHANGE		=	1;
	/**
	 *Set  millisecond per minute
	 */
	private  static long   			 Milli_SECOND_PER_SECOND			=	1000;	
	
	/**
	 * Default timer initial value
	 */
	private   static final String 	  DEFAULT_TIMER_VALUE				=	"1";
	/**
	 * Default notification counter 
	 */
	public   static final int 	  	  DEFAULT_NOTIFICATION_COUNTER		=	0;
	/**
	 * Phase first notification id 
	 */
	public   static final int 	  	  PHASE_FIRST_NOTIFICATION_ID		=	1;
	/**
	 * Phase second notification id 
	 */
	public   static final int 	  	  PHASE_SECOND_NOTIFICATION_ID		=	2;
	/**
	 * Increment decrement per time value 
	 */
	private  static final int			INCREMENT_DECREMENT_PER_TIME	=	1;
	Button			start,reset;
	TextView		timerFirstTime;
	static TextView notificationCounter,lastSuccessfullyFirstPhase,lastSuccessfullySecondPhase;
	ImageView      	timerFirstIncrement,timerFirstDecrement,timerSecondIncrement,timerSecondDcrement;
	Runnable 		runnable;
	Handler 		handler		= new Handler();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (aiView == null) {
			aiView = inflater.inflate(R.layout.fragment_daily_reward, container, false);
		}
		Utils.setFontAllView((ViewGroup)aiView);
		MyUtils.sendScreenToGoogleAnalytics(getActivity().getApplication(),"Daily Reward");
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
			
			((ActivityMainWallet)aiContext).customizeActionBarWithBack(getResources().getString(R.string.title_screen_daily_reward));
			
			btnCheckIn				= (Button)aiView.findViewById(R.id.btncheckin);
			btnCheckIn.setOnClickListener(this);

			txtDailyRewardTop		= (TextView)aiView.findViewById(R.id.text_dailyrewardtexttop);
			txtDailyRewardBottom	= (TextView)aiView.findViewById(R.id.text_dailyrewardtextbottom);

			String strTimeLeft = "23:59:00";
			/*txtDailyRewardTop.setText("You will receive "
					+ PreferenceConnector.readString(aiContext, PreferenceConnector.DAILYREWARDPOINTS, "") 
					+ " points when  checking in.");*/
			txtDailyRewardBottom.setText("You will get 20 credits each day.");
			startTimer();
			// Set last successfully timer time
			//setLastSuccessfullyTimerTime();
		}
	}

	/**
	 * Set notification 
	 * @param calendar
	 * @param id
	 * @param msg
	 */
	public void createNotification(Calendar calendar,int id,String msg,int timerTime) 
	{
		Intent intentAlarm = new Intent(aiContext, NotificationReciver.class);
		intentAlarm.putExtra(NotificationReciver.NOTIFICATION_ID, id);
		intentAlarm.putExtra(NotificationReciver.NOTIFICATION_MASSEGE, msg);
		intentAlarm.putExtra(NotificationReciver.NOTIFICATION_TIMER_TIME, timerTime);
		AlarmManager alarmManager = (AlarmManager) aiContext.getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), PendingIntent.getBroadcast(aiContext,id,  intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
	}
	/**
	 * cancel notification
	 * @param id
	 */
	public void cancelNotification(int id) 
	{
		Intent intentAlarm = new Intent(aiContext, NotificationReciver.class);
		AlarmManager alarmManager = (AlarmManager) aiContext.getSystemService(Context.ALARM_SERVICE);
		PendingIntent   pendingIntent	=PendingIntent.getBroadcast(aiContext,id,  intentAlarm, 0);
		pendingIntent.cancel();
		alarmManager.cancel( pendingIntent);
	}
	/**
	 * Append 0 when timer time is less than 10 
	 * @param a
	 * @return
	 */
	public static String append(int a)
	{
		if(a<10)
			return "0"+a;
		else
			return ""+a;
	}
	/**
	 * Save timer value on  change
	 * @param field
	 * @param values
	 */
	public void saveTimeOnTimerChange(String field,String values)
	{
		PreferenceConnector.writeString(aiContext, field,values);
	}
	/**
	 * Save timer value on  notification save 
	 * @param field
	 */
	public static  void saveTimeOnNotificationSave(String field,String retiveField)
	{
		PreferenceConnector.writeInteger(aiContext, field,Integer.parseInt(PreferenceConnector.readString(aiContext, retiveField, DEFAULT_TIMER_VALUE)));
	}
	/**
	 * Save notification time on set
	 * @param calendar
	 * @param field
	 */
	public void saveNotificationPhaseTime(Calendar calendar,String field)
	{
		PreferenceConnector.writeLong(aiContext, field,calendar.getTimeInMillis());
	}
	/**
	 * Show remaining time of notification 
	 */
	public void startTimer()
	{
		
		runnable = new Runnable() 
		{
			
			@Override
			public void run() 
			{
				Calendar   calendar 	=	Calendar.getInstance();
				System.out.println("handler**************************");
				long timeTimeDeffrence   =	PreferenceConnector.readLong(aiContext, PreferenceConnector.NOTICATION_FIRST_PHASE_TIME,0 )-calendar.getTimeInMillis();
				System.out.println(timeTimeDeffrence+"timeTimeDeffrence**************************"+PreferenceConnector.readLong(aiContext, PreferenceConnector.NOTICATION_FIRST_PHASE_TIME,0 ));
				// When both phase is not complete 
				if(timeTimeDeffrence>0)
				{
					//TextView   			textView		=	(TextView)aiView.findViewById(R.id.remaining_time);
					TextView   			textViewTimer	=	(TextView)aiView.findViewById(R.id.remaining_time_text);
					long				second			=	timeTimeDeffrence/Milli_SECOND_PER_SECOND;
					long				minute			=	second/60;
					long				hour			=	minute/60;		
					//textView.setText(String.format(getResources().getString(R.string.time_formate),append((int)hour),append((int)(minute%60)),append((int)(second%60))));
					txtDailyRewardTop.setText(String.format("After "+getResources().getString(R.string.time_format)+", You will receive "
							+ PreferenceConnector.readString(aiContext, PreferenceConnector.DAILYREWARDPOINTS, "") 
							+ " credits when  checking in.",append((int)hour),append((int)(minute%60)),append((int)(second%60))));
					textViewTimer.setText("Phase First Time Remaining");
					handler.postDelayed(runnable, Milli_SECOND_PER_SECOND);
				}
				else{
					txtDailyRewardTop.setText("You will receive "
							+ PreferenceConnector.readString(aiContext, PreferenceConnector.DAILYREWARDPOINTS, "") 
							+ " credits when  checking in.");
				}
				
			}
		};
		handler.removeCallbacks(runnable);
		Intent i = new Intent(aiContext, NotificationReciver.class);
		if(alarmIsSet(PHASE_FIRST_NOTIFICATION_ID,i)||alarmIsSet(PHASE_SECOND_NOTIFICATION_ID,i))
		{
			handler.post(runnable);
		}
		else{
			txtDailyRewardTop.setText("You will receive "
					+ PreferenceConnector.readString(aiContext, PreferenceConnector.DAILYREWARDPOINTS, "") 
					+ " credits when  checking in.");
		}
		
		
		
		
	}
	/**
	 * Set timer text  font style
	 */
	public void setFont()
	{
		/*TextView   			textView		=	(TextView)findViewById(R.id.remaining_time);
		 Typeface tf = Typeface.createFromAsset(aiContext.getAssets(), "fonts/Quartz Regular.ttf");
		 textView.setTypeface(tf);*/
	}
	/**
	 * Increment notification timer 
	 * @param field
	 * @param context
	 */
	public static  void saveNoticationCounter(String field,Context context)
	{
		PreferenceConnector.writeInteger(context, field,PreferenceConnector.readInteger(context, field, DEFAULT_NOTIFICATION_COUNTER)+1);
	}
	/**
	 * Reset notification timer
	 * @param value
	 */
	public static void resetNoticationCounter(String field,Context context)
	{
		
		PreferenceConnector.writeInteger(context.getApplicationContext(), field,DEFAULT_NOTIFICATION_COUNTER+1);
	}
	/**
	 * Get notification counter 
	 * @param field
	 * @return
	 */
	public int getNoticationCounter(String field)
	{
		 return PreferenceConnector.readInteger(aiContext, field,DEFAULT_NOTIFICATION_COUNTER);
	}
	
	/**
	 * Set notification counter by Alarm
	 */
	public static void setNoticationCounterDefault()
	{
		
		//notificationCounter.setText((String.format(aiContext.getResources().getString(R.string.timer_counter), append(PreferenceConnector.readInteger(context.getApplicationContext(), PreferenceConnector.NOTIFICATOIN_COUNTER, DEFAULT_NOTIFICATION_COUNTER)))));
	}
	/**
	 * Handler in stop when application is destroy
	 */
	@Override
	public void onDestroy() 
	{
		super.onDestroy();
		handler.removeCallbacks(runnable);
	}
	/**
	 * Alarm is set or not from id
	 * @param alarmID
	 * @param intent
	 * @return
	 */
	private boolean alarmIsSet(int alarmID,Intent intent) 
	{
        return PendingIntent.getBroadcast(aiContext, alarmID, intent, PendingIntent.FLAG_NO_CREATE) != null;
    }
	/**
	 * Set last successfully timer time set
	 */
	public static void setLastSuccessfullyTimerTime()
	{
		if(PreferenceConnector.readInteger(aiContext, PreferenceConnector.LAST_NOTIFICATION_TIMER_FIRST_PHASE_TIME, -1)!=-1)
		{
			/*lastSuccessfullyFirstPhase.setVisibility(View.VISIBLE);
			lastSuccessfullySecondPhase.setVisibility(View.VISIBLE);
			lastSuccessfullyFirstPhase.setText((String.format(aiContext.getResources().getString(R.string.lastsuccessfullytime),append( PreferenceConnector.readInteger(context.getApplicationContext(), PreferenceConnector.LAST_NOTIFICATION_TIMER_FIRST_PHASE_TIME, -1)))));
			lastSuccessfullySecondPhase.setText((String.format(aiContext.getResources().getString(R.string.lastsuccessfullytime),append(PreferenceConnector.readInteger(context.getApplicationContext(), PreferenceConnector.LAST_NOTIFICATION_TIMER_SECOND_PHASE_TIME, -1)))));*/
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btncheckin:
			
			
			
			if (gd.isConnectingToInternet()) {
				String[] keys 		= {"user_id"};
				String[] value 		= {PreferenceConnector.readString(aiContext, 
						PreferenceConnector.USERID, "")};

				HashMap<String, String> hash	=	new HashMap<String, String>();
				for (int i = 0; i < keys.length; i++) {
					System.out.println(keys[i]+ "......." + value[i]);
					hash.put(keys[i], value[i]);
				}
				callWebService(GlobalVariables.CHECKIN, hash);
			}else {
				GlobalData.showToast(getResources().getString(R.string.error_no_internet), aiContext);
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
			String str_RESULT = json.getString(TAG_RESULT);
			String str_Message = json.getString(TAG_MESSAGE);
			GlobalData.showToast(str_Message, aiContext);
			//GlobalData.showToast(str_Message, aiContext);
			if (str_RESULT.equals("YES")) {
				JSONObject Data_obj 		= json.getJSONObject(TAG_DATA);
				String str_user_points 		= Data_obj.getString(TAG_USER_POINTS);
				String str_rewards_checkin 	= Data_obj.getString(TAG_REWARDS_CHECKIN);

				PreferenceConnector.writeInteger(aiContext, PreferenceConnector.WALLETPOINTS, 
						Integer.parseInt(str_user_points));
				FragEarnCredits.onUpdateView(aiContext);
				ViewRewardsFragment.onUpdateView(aiContext);
				
				InviteFriendsFragment.onUpdateView(aiContext);
				ConnectSocialFragment.onUpdateView(aiContext);
				
				Calendar  calendar			=	Calendar.getInstance();
				//calendar.add(Calendar.MINUTE, Integer.parseInt(("1440")));
				calendar.add(Calendar.MINUTE, Integer.parseInt(("1440")));
				createNotification(calendar,PHASE_FIRST_NOTIFICATION_ID,"", Integer.parseInt(("1440")));
				saveNotificationPhaseTime(calendar, PreferenceConnector.NOTICATION_FIRST_PHASE_TIME);
				saveTimeOnTimerChange(PreferenceConnector.TIMER_FIRST_TIME, "1440");
				saveTimeOnTimerChange(PreferenceConnector.LAST_NOTIFICATION_CREATE_TIMER_FIRST_PHASE_TIME, "1440");
				// Start remaining timer 
				startTimer();
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

	private void switchBack() {
		if (getActivity() == null)
			return;
		if (getActivity() instanceof ActivityMainWallet) {
			ActivityMainWallet mActivity = (ActivityMainWallet) getActivity();
			mActivity.switchBack();
		}
	}
	
	
}