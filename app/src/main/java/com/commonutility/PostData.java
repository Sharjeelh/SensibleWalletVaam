package com.commonutility;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.parse.entity.mime.HttpMultipartMode;
import com.parse.entity.mime.MultipartEntity;
import com.parse.entity.mime.content.FileBody;
import com.parse.entity.mime.content.StringBody;
import com.cashmobi.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostData {
	public static String postData(final Context context, String Url, HashMap<String, String> keyPaire) {
		String url		=	Url+"?";
		for (Map.Entry<String, String> entry : keyPaire.entrySet()) {
			url		= url+entry.getKey() + "=" + entry.getValue()+"&";
		}

		url			= url.substring(0, url.length()-1);
		url.replaceAll(" ", "%20");
		Log.v("url  ", url);
		// Create a new HttpClient and Post Header
		if(checknetwork(context)) {
			HttpClient 		httpclient 	= new DefaultHttpClient();
			HttpPost 		httppost 	= new HttpPost(url.replaceAll(" ", "%20"));
			String			urlResponce	=	"";

			try {
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				for (Map.Entry<String, String> entry : keyPaire.entrySet())	{
					nameValuePairs.add(new BasicNameValuePair(entry.getKey() , entry.getValue()));
				}
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));

				//				['X-Oc-Merchant-Id']   =   aF5tGHO90OlNjYhhg
				httppost.setHeader("X-Oc-Merchant-Id", "aF5tGHO90OlNjYhhg");
				//Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity   entity   = response.getEntity();
				//If the response does not enclose an entity, there is no need
				//to worry about connection release
				if (entity != null) {
					// A Simple JSON Response Read
					InputStream instream = entity.getContent();
					urlResponce= convertStreamToString(instream);
					// now you have the string representation of the HTML request
					instream.close();
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return urlResponce;
		}else {
			((Activity)context).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(context, context.getResources().getString(R.string.error_no_network), Toast.LENGTH_LONG).show();
				}
			});
			return context.getResources().getString(R.string.error_no_network);
		}
	} 

	public static String postDataWithImage(final Context context, String Url, HashMap<String, String> keyPaire, String path) {
		String jsonText = "";
		try	{ 
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(Url.replaceAll(" ", "%20"));

			MultipartEntity entitys = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			for (Map.Entry<String, String> entry : keyPaire.entrySet())	{
				entitys.addPart(entry.getKey(), new StringBody(entry.getValue()));
			}

			if (! path.equals("")) {
				entitys.addPart("profile_img", new FileBody(new File(path), "image/jpg"));
			}

			HttpContext localContext = new BasicHttpContext();
			//			httppost.setHeader("Content-Lenght", "" + entitys.getContentLength());
			httppost.setHeader("X-Oc-Merchant-Id", "aF5tGHO90OlNjYhhg");
			httppost.setEntity(entitys);
			HttpResponse response = httpClient.execute(httppost, localContext); 
			HttpEntity entity = response.getEntity();

			InputStream instream = null;
			if (entity != null) {
				instream = entity.getContent();
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}

			jsonText = sb.toString();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return jsonText;
	}

	public static String putData(final Context context,String Url, HashMap<String, String> keyPaire) {
		// Create a new HttpClient and Post Header
		if(checknetwork(context)) {
			HttpClient 		httpclient = new DefaultHttpClient();
			HttpPut 		httpPut = new HttpPut(Url);
			String			urlResponce	=	"";
			try {
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				for (Map.Entry<String, String> entry : keyPaire.entrySet()) {
					nameValuePairs.add(new BasicNameValuePair(entry.getKey() , entry.getValue()));
				}

				httpPut.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				//Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httpPut);
				HttpEntity entity = response.getEntity();
				//If the response does not enclose an entity, there is no need
				//to worry about connection release
				if (entity != null) {

					// A Simple JSON Response Read
					InputStream instream = entity.getContent();
					urlResponce= convertStreamToString(instream);
					// now you have the string representation of the HTML request
					instream.close();
				}

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Log.v("responce"," msg  "+urlResponce);
			return urlResponce;
		}else {
			return context.getResources().getString(R.string.error_no_network);
		}
	} 

	public static String getData(Context context, String url, HashMap<String, String> keyPaire) {
		if(checknetwork(context)) {
			// Add your data
			url		=	url+"?";

			for (Map.Entry<String, String> entry : keyPaire.entrySet()) {
				url			=url+entry.getKey() +"="+entry.getValue()+"&";
			}

			url			=	url.substring(0, url.length()-1);
			url.replaceAll(" ", "%20");
			Log.v("url  ", url);
			// Create a new HttpClient and Post Header
			HttpClient 		client 			= new DefaultHttpClient();  
			HttpGet 		httpGet 		= new HttpGet(url.replaceAll(" ", "%20"));
			String			urlResponce		=	"";
			httpGet.setHeader("X-Oc-Merchant-Id", "aF5tGHO90OlNjYhhg");

			try {
				HttpResponse response = client.execute(httpGet);  
				//StatusLine statusLine = response.getStatusLine();  
				//int statusCode = statusLine.getStatusCode();  
				HttpEntity entity = response.getEntity();  
				if (entity != null) {
					// A Simple JSON Response Read
					InputStream instream = entity.getContent();
					urlResponce= convertStreamToString(instream);
					// now you have the string representation of the HTML request
					instream.close();
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Log.v("responce"," msg  "+urlResponce);
			return urlResponce;
		}else {
			return context.getResources().getString(R.string.error_no_network);
		}
	} 

	private static String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the BufferedReader.readLine()
		 * method. We iterate until the BufferedReader return null which means
		 * there's no more data to read. Each line will appended to a StringBuilder
		 * and returned as String.
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public static boolean checknetwork(Context context)	{
		ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] networkInfo = connMgr.getAllNetworkInfo();
		if (networkInfo != null) {
			for (int i = 0; i < networkInfo.length; i++) {
				if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isOnline(Context context) {
		ConnectivityManager cm =  (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		Toast.makeText(context, context.getResources().getString(R.string.error_no_network), Toast.LENGTH_LONG).show();
		return false;
	}
}
