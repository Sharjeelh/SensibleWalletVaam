package com.archiveinfotech.crashreport;

import android.app.Application;
import android.content.Context;

import com.parse.Parse;

import org.acra.ACRA;
import org.acra.ErrorReporter;
import org.acra.annotation.ReportsCrashes;

@ReportsCrashes( formKey="")  
public class ParseCrash extends Application {  
	private static ParseCrash instance;

	@SuppressWarnings("deprecation")
	@Override  
	public void onCreate() {  
		super.onCreate();  
		Parse.initialize(this, "sELZ2JSUco981lrgprrb2JdkEsLISqRVViE4Kjpx", "9AP8hpwgthFncg73OJ5V1lsLsGKy8iFOK3wjawAV");
		ACRA.init(this);  
		ErrorReporter.getInstance().setReportSender(new LocalSender(this));  
		Utils.loadFonts();
	}   

	public ParseCrash() {
		super();
		instance = this;
	}

	public static ParseCrash getApp() {
		return instance;
	}

	public static Context getContext() {
		return instance;
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}


	public enum TrackerName {
		APP_TRACKER, GLOBAL_TRACKER, ECOMMERCE_TRACKER,
	}
}  
