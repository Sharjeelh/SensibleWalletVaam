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

public class TermsOfServiceFragemnt extends Fragment {
	private Context aiContext;
	private View aiView = null;
	private boolean mAlreadyLoaded=false;
	private WebView webView;
	public static final String TAG_MESSAGE	= "Message";
	public static final String TAG_RESULT	= "RESULT";
	public static final String TAG_DATA		= "Data";
	public static final String TAG_term_content	= "term_content";

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
			((ActivityMainWallet)aiContext).customizeActionBarWithBack(getResources().getString(R.string.title_screen_terms));

//			Toolbar toolbar = (Toolbar)aiView.findViewById(R.id.toolbarInner);
//			toolbar.setTitle("Terms Of Service");

			webView	= (WebView)aiView.findViewById(R.id.webView);
			webView.loadData(PreferenceConnector.readString(aiContext, PreferenceConnector.TERMCONTENT,""), "text/html", "UTF-8");
		}
	}
}