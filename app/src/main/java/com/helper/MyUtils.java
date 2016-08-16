package com.helper;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.cashmobi.MyApplication;

/**
 * Created by nikhil on 3/4/16.
 */
public class MyUtils {

    private static final String MY_PREFF = "my_preff";

    public static void sendScreenToGoogleAnalytics(Application myApp, String screenName){
        Tracker t = ((MyApplication)myApp).getDefaultTracker();
        t.setScreenName(screenName);
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public static void sendEventToGoogleAnalytics(Application myApp, String category,String action){
        Tracker t = ((MyApplication) myApp).getDefaultTracker();
        t.send(new HitBuilders.EventBuilder().setAction(action).setCategory(category).build());
    }

    public static void setPref(Context context,String prefDeviceId, String deviceId) {
        context.getSharedPreferences(MY_PREFF,Context.MODE_PRIVATE).edit().putString(prefDeviceId,deviceId).apply();
    }

    public static String getStringPref(Context context,String prefDeviceId) {
        return context.getSharedPreferences(MY_PREFF,Context.MODE_PRIVATE).getString(prefDeviceId,"");
    }
}
