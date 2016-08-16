package com.cashmobi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

// Referenced classes of package com.hespress.android.gcm:
//            IntentService

public class BroadcastReceiver extends WakefulBroadcastReceiver {
	public BroadcastReceiver(){}

	public void onReceive(Context context, Intent intent) {
		Intent gcmIntent = new Intent (context, GCMIntentService.class);
		gcmIntent.putExtras (intent.getExtras());
		startWakefulService(context, gcmIntent);
		setResultCode(Activity.RESULT_OK);
	}
}
