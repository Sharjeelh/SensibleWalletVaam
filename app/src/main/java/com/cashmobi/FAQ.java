package com.cashmobi;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.archiveinfotech.crashreport.Utils;
import com.commonutility.PreferenceConnector;
import com.commonutility.WebService;
import com.commonutility.WebServiceListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class FAQ extends Fragment implements WebServiceListener {
	private Context aiContext;
	private View aiView = null;
	private boolean mAlreadyLoaded=false;
	private WebView webView;
	public static final String TAG_MESSAGE	= "Message";
	public static final String TAG_RESULT	= "RESULT";
	public static final String TAG_DATA		= "Data";
	public static final String TAG_faq_content	= "faq_content";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (aiView == null) {
			aiView = inflater.inflate(R.layout.fragment_faq, container, false);
		}
		Utils.setFontAllView((ViewGroup)aiView);
		return aiView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState == null && !mAlreadyLoaded) {
			mAlreadyLoaded 		= true;
			aiContext 			= getActivity();
			aiView 				= getView();
			((ActivityMainWallet)aiContext).customizeActionBarWithBack(getResources().getString(R.string.title_screen_faq));

//			Toolbar toolbar = (Toolbar)aiView.findViewById(R.id.toolbarInner);
//			toolbar.setTitle("FAQ");

			webView	= (WebView)aiView.findViewById(R.id.webView);
			webView.loadData(PreferenceConnector.readString(aiContext, PreferenceConnector.FAQCONTEXT,""), "text/html", "UTF-8");
			/*HashMap<String, String> hash	=	new HashMap<String, String>();
			callWebService(GlobalVariables.MENUCONTENT, hash);*/

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

			JSONObject jsonData = new JSONObject(json.getString(TAG_DATA));

			//GlobalData.showToast(jsonData.getString(TAG_faq_content), aiContext);
			webView.loadData(jsonData.getString(TAG_faq_content), "text/html", "UTF-8");
		} catch (JSONException e){
			e.printStackTrace();
		}


	}
}