package com.commonutility;

import android.content.Context;
import android.os.AsyncTask;

import com.cashmobi.R;

import java.util.HashMap;

public class WebService extends AsyncTask<String, Void, String> {
	public static final int POST 			= 1;
	public static final int GET  			= 2;
	public static final int POSTWITHIMAGE 	= 3;

	Context context;
	String  url, message;
	int post;
	HashMap<String, String> keyValue;
	String path;
	WebServiceListener listener;
	private CustomProgressDialog customeProgressDialog;

	// For Get and Post
	public WebService(Context context, String message, String url, HashMap<String, String> keyValue, 
			WebServiceListener listener, int post) {
		this.context	=	context;
		this.url		=	url;
		this.keyValue	=	keyValue;
		this.listener	=	listener;
		this.message	=	message;
		this.post       =   post;
//		System.out.println(url+".....................url,,,,,,,,..............");
	}

	public WebService(Context context, String message, String url, HashMap<String, String> keyValue, 
			WebServiceListener listener, int post, String path) {
		this.context	=	context;
		this.url		=	url;
		this.keyValue	=	keyValue;
		this.listener	=	listener;
		this.message	=	message;
		this.post       =   post;
		this.path   	=   path;
//		System.out.println(url+".....................url,,,,,,,,..............");
	}

	@Override
	protected void onPreExecute() {
		customeProgressDialog	=	new CustomProgressDialog(context, R.layout.dialog_progress);
		customeProgressDialog.setCancelable(false);
		customeProgressDialog.show();
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... params) {
		String result = "";
		if (post == POST) {
			result	=	PostData.postData(context, GlobalVariables.PRE_URL + url, keyValue);
		}else if (post == GET) {
			result	=	PostData.getData(context, GlobalVariables.PRE_URL + url, keyValue);
		}else {
			result	=	PostData.postDataWithImage(context, GlobalVariables.PRE_URL + url, keyValue, path);
		}
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (null != customeProgressDialog && customeProgressDialog.isShowing()) {
			customeProgressDialog.dismiss();
		}
		listener.onWebServiceActionComplete(result, url);
	}
}
