package com.commonutility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cashmobi.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint("NewApi")
public class GlobalData {
	private Context _context;
	public GlobalData(Context context) {
		this._context = context;
	}
	public void setStatusBarColor(){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window =  ((Activity)_context).getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(_context.getResources().getColor(R.color.material_color_primary_dark));
//			getResources().getColor(R.color.lightbluecolor)
		}
	}

	public boolean isConnectingToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager)_context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) 
				for (int i = 0; i < info.length; i++) 
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
		}
		return false;
	}

	public static String getStringRes(Context aContext, int strId) {
		String str = aContext.getResources().getString(strId);
		return str;
	}

	/*public static void startTimer(final int startSecond, int endSecond, final TextView txtViewTimer) {
		new CountDownTimer(startSecond*1000, endSecond*1000) {
			public void onTick(long millisUntilFinished) {
				txtViewTimer.setText(returnTime(millisUntilFinished/1000 - 1));
			}

			public void onFinish() {
				txtViewTimer.setText("00:00");
			}
		}.start();		
	}
	*/
	public static void ShareText(Context aiContext, String shareText) {
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Hey");
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareText);
		aiContext.startActivity(Intent.createChooser(sharingIntent, "Share "));
	}

	/*public static String returnTime(long secondsUntilFinish) {
		String strSpanned = "00:";
		if(secondsUntilFinish <= 9) {
			strSpanned = "00:0" + secondsUntilFinish;
			return strSpanned;
		}else {
			strSpanned = "00:" + secondsUntilFinish;
			return strSpanned;
		}
	}*/

	public static String removeFirstCountChar(String word, int count){
		return word.substring(count);
	}

	//	public static JSONArray getJsonArray(String postUrl, String[] keys, String[] value) {
	//		JSONArray json = null;
	//		try	{ 
	//			URL url = new URL(PRE_URL + postUrl);
	//			DefaultHttpClient httpClient = new DefaultHttpClient();
	//			HttpPost httppost = new HttpPost(url.toString());
	//			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(keys.length);
	//
	//			for (int i = 0; i < keys.length; i++) {
	//				nameValuePairs.add(new BasicNameValuePair(keys[i], value[i]));
	//				System.out.println(keys[i]+".....key....value....."+value[i]);
	//			}
	//
	//			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	//			HttpResponse response = httpClient.execute(httppost); 
	//			HttpEntity entity = response.getEntity();
	//
	//			InputStream instream = null;
	//			if (entity != null) {
	//				instream = entity.getContent();
	//			}
	//
	//			BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
	//			StringBuilder sb = new StringBuilder();
	//			String line = null;
	//			while((line = reader.readLine()) != null) {
	//				sb.append(line + "\n");
	//			}
	//
	//			String jsonText = sb.toString();
	//			System.out.println(jsonText+"......jsontext...("+postUrl+")");
	//			json = new JSONArray(jsonText);
	//		} catch(Exception e) {
	//			e.printStackTrace();
	//		}
	//		return json;
	//	}

	//	public static JSONObject getJsonObject(String postUrl, String[] keys, String[] value) {
	//		JSONObject json = null;
	//		try	{ 
	//			URL url = new URL(PRE_URL + postUrl);
	//			DefaultHttpClient httpClient = new DefaultHttpClient();
	//			HttpPost httppost = new HttpPost(url.toString());
	//			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(keys.length);
	//
	//			for (int i = 0; i < keys.length; i++) {
	//				nameValuePairs.add(new BasicNameValuePair(keys[i], value[i]));
	//				System.out.println(keys[i]+".....key....value....."+value[i]);
	//			}
	//
	//			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	//			HttpResponse response = httpClient.execute(httppost); 
	//			HttpEntity entity = response.getEntity();
	//
	//			InputStream instream = null;
	//
	//			if (entity != null) {
	//				instream = entity.getContent();
	//			}
	//			BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
	//			StringBuilder sb = new StringBuilder();
	//			String line = null;
	//			while((line = reader.readLine()) != null) {
	//				sb.append(line + "\n");
	//			}
	//
	//			String jsonText = sb.toString();
	//			System.out.println(jsonText+"......jsontext...("+postUrl+")");
	//			json = new JSONObject(jsonText);
	//		} catch(Exception e) {
	//			e.printStackTrace();
	//		}
	//		return json;
	//	}

	public static boolean isEmailValid(String email) {
		String regExpn =
				"^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
						+"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
						+"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
						+"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
						+"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
						+"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,7})$";

		CharSequence inputStr = email;
		Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		return matcher.matches();
	}

	/*public static void displayAlert(String msg,Context context){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		// set title
		alertDialogBuilder.setTitle("Alert");
		// set dialog message
		alertDialogBuilder.setMessage(msg).setCancelable(false)
		.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// if this button is clicked, close
				dialog.dismiss();
				// current activity
			}
		});

	 * .setNegativeButton("No",new DialogInterface.OnClickListener() {
	 * public void onClick(DialogInterface dialog,int id) { // if this
	 * button is clicked, just close // the dialog box and do nothing
	 * dialog.cancel(); } });


		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}*/

	public static void showDialog(final Context meraContext, String text, int textSize, String btnOneText, String btnTwoText) {
		final Dialog dialog;
		dialog = new Dialog(meraContext, R.style.AlertDialog_AppCompat);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_two_button_dialog);

		TextView textHead = (TextView) dialog.findViewById(R.id.text);
		textHead.setTextSize(textSize);
		textHead.setText(text);

		Button btnfirst = (Button)dialog.findViewById(R.id.btnfirst);
		btnfirst.setText(btnOneText);
		btnfirst.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});

		Button btnCancel = (Button)dialog.findViewById(R.id.btnsecond);
		if (btnTwoText.equals("")) {
			btnCancel.setVisibility(View.GONE);
		}else {
			btnCancel.setText(btnTwoText);
			btnCancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					dialog.dismiss();
				}
			});
		}
		dialog.show();
	}

	public static String getFormatedcurrentDate() {
		Calendar today = Calendar.getInstance();
		int date = today.get(Calendar.DATE);
		int month = today.get(Calendar.MONTH);
		int year = today.get(Calendar.YEAR);

		String mon = "";
		String[] monthdayArray = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
		String[] monthArray = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
		for (int i = 0; i < monthdayArray.length; i++) {
			if ((""+(month+1)).equals(monthdayArray[i])) {
				mon = monthArray[i];
				break;
			}
		}
		return (mon) + " " + date + ", " +  year + ", ";
	}

	public static String returnMonthAlphabet(int month) {
		String mon = "";
		String[] monthdayArray = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
		String[] monthArray = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
		for (int i = 0; i < monthdayArray.length; i++) {
			if ((""+(month+1)).equals(monthdayArray[i])) {
				mon = monthArray[i];
				break;
			}
		}
		return mon;
	}

	public static String getFormatedcurrentTime() {
		Calendar today = Calendar.getInstance();
		int hour 	= today.get(Calendar.HOUR);
		int minute 	= today.get(Calendar.MINUTE);
		int second 	= today.get(Calendar.SECOND);
		int amorpm	= today.get(Calendar.AM_PM);

		String strAMORPM = "";
		if (amorpm == 0) {
			strAMORPM = "AM";
		} else {
			strAMORPM = "PM";
		}
		return hour + ":" + minute + ":" + second + " "  + strAMORPM;
	}

	public static String getcurrentDate() {
		Calendar today = Calendar.getInstance();
		int date = today.get(Calendar.DATE);
		int month = today.get(Calendar.MONTH);
		int year = today.get(Calendar.YEAR);

		return date + "" + month + "" +  year;
	}

	public static String getcurrentTime() {
		Calendar today = Calendar.getInstance();
		int hour 	= today.get(Calendar.HOUR);
		int minute 	= today.get(Calendar.MINUTE);
		int second 	= today.get(Calendar.SECOND);

		return hour + "" + minute + "" + second ;
	}

	public static String replaceMonthString(String dateTime) {
		String month 	= dateTime.substring(0, 2);
		dateTime 		= dateTime.substring(2); 

		String mon = "";
		String[] monthdayArray = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
		String[] monthArray = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
		for (int i = 0; i < monthdayArray.length; i++) {
			if ((""+(month)).equals(monthdayArray[i])) {
				mon = monthArray[i];
				break;
			}
		}

		return mon + dateTime;
	}

	/*public String getDeviceName() {
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;
		if (model.startsWith(manufacturer)) {
			return capitalize(model);
		} else {
			return capitalize(manufacturer) + "_" + model;
		}
	}
*/
	/*private String capitalize(String s) {
		if (s == null || s.length() == 0) {
			return "";
		}
		char first = s.charAt(0);
		if (Character.isUpperCase(first)) {
			return s;
		} else {
			return Character.toUpperCase(first) + s.substring(1);
		}
	}

	public String getDeviceVersion() {
		String myVersion = android.os.Build.VERSION.RELEASE; // e.g. myVersion := "1.6"
		//		int sdkVersion = android.os.Build.VERSION.SDK_INT; // e.g. sdkVersion := 8; 
		return myVersion;
	}*/

	/*public String getDeviceVersionName() {
		// Names taken from android.os.build.VERSION_CODES
		String[] mapper = new String[] {
				"ANDROID_BASE", "ANDROID_BASE1.1", "CUPCAKE", "DONUT",
				"ECLAIR", "ECLAIR_0_1", "ECLAIR_MR1", "FROYO", "GINGERBREAD",
				"GINGERBREAD_MR1", "HONEYCOMB", "HONEYCOMB_MR1", "HONEYCOMB_MR2",
				"ICE_CREAM_SANDWICH", "ICE_CREAM_SANDWICH_MR1", "JELLY_BEAN","JELLY_BEAN_MR1", "JELLY_BEAN_MR2", "KITKAT","LOLLYPOP"
		};
		int index = Build.VERSION.SDK_INT - 1;
		String versionName = index < mapper.length? mapper[index] : "UNKNOWN_VERSION"; // > KITKAT)
		return versionName;
	}

	public static double roundUp(double value, int roundAfterDecimal) {
		BigDecimal totaalAmt 		= new BigDecimal(value);
		BigDecimal strtotaalAmt 	= totaalAmt.setScale(roundAfterDecimal, RoundingMode.HALF_UP);	
		double roundedValue = Double.parseDouble(String.valueOf(strtotaalAmt));
		return roundedValue;
	}*/

	public static String getDeviceId(Activity mContext) {
			final TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
			final String tmDevice, tmSerial, androidId;
			tmDevice = "" + tm.getDeviceId();
			tmSerial = "" + tm.getSimSerialNumber();
			androidId = "" + android.provider.Settings.Secure.getString(mContext.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

			UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
			String deviceId = deviceUuid.toString();
		
		return deviceId;
	}



	public int emptyEditTextError(EditText[] edtTexts, String[] errorMsg) {
		int count = 0;
		for (int i = 0; i < edtTexts.length; i++) {
			edtTexts[i].setError(null);
			if (edtTexts[i].getText().toString().trim().length() == 0) {
				edtTexts[i].setError(errorMsg[i]);
				count++;
			}
		}
		return count;
	}

	public static int emptyEditTextError(EditText[] edtTexts, String[] errorMsg, int[] minCount, String[] minError) {
		int count = 0;
		for (int i = 0; i < edtTexts.length; i++) {
			edtTexts[i].setError(null);
			if (edtTexts[i].getText().toString().trim().length() == 0) {
				edtTexts[i].setError(errorMsg[i]);
				count++;
			}else {
				if (minCount[i] != 0) {
					if (edtTexts[i].getText().length() <= minCount[i]) {
						if (minError[i].equals("")) {
							String strError = "Please enter minimum " + minCount[i] + " character";
							System.out.println(strError+"..........strerror..............");
							edtTexts[i].setError(strError);
						}else {
							edtTexts[i].setError(minError[i]);
						}
						count++;
					}
				}
			}
		}
		return count;
	}

	public static String getIMEINumber(Context mContext) {
		TelephonyManager  tm = (TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}

	public static void showSnackbar(final String string, final Context con, final View view) {
		((Activity)con).runOnUiThread(new Runnable() {
			public void run() {
				Snackbar.make(view, string, Snackbar.LENGTH_LONG).show();
			}
		});
	}

	public static void showToast(final String string, final Context con) {
		((Activity)con).runOnUiThread(new Runnable() {
			public void run() {
				View v = ((Activity)con).findViewById(android.R.id.content);
				if(v == null) {
					Toast.makeText(con, string, Toast.LENGTH_LONG).show();
				} else {
					showSnackbar(string, con, v);
				}
				//System.out.println(string + "..........toast print......");
			}
		});
	}

	public static Double[] getLatLon(Context mContext) {
		Double[] latLon = new Double[2];
		GPSTracker gps = new GPSTracker(mContext);
		if (gps.canGetLocation()) {
			latLon[0] = gps.getLatitude();
			latLon[1] = gps.getLongitude();
		}
		return latLon;
	}

	public static String getMinuteInDayHourMin(String strparkingTime) {
		int parkingTime = Integer.parseInt(strparkingTime);
		if (parkingTime < 60) {
			return parkingTime + " minute";
		}else {
			if (parkingTime/60 > 24) {
				return (parkingTime/60)/24 + " day " + (parkingTime%60) + " hour " + parkingTime%60 + " min";
			} else {
				return parkingTime/60 + " hour " + parkingTime%60 + " min";
			}
		}
	}

	public static int getApiVesion() {
		return android.os.Build.VERSION.SDK_INT;
	}

	public static boolean createDirIfNotExists(String path) {
		boolean ret = true;
		File file = new File(Environment.getExternalStorageDirectory(), path);
		if (!file.exists()) {
			if (!file.mkdirs()) {
				//System.out.println("Problem creating Image folder");
				ret = false;
			}
		}
		return ret;
	}

	public static void setImageFromDatabase(byte[] imgByte, ImageView imgView) {
		Bitmap bitmap = BitmapFactory.decodeByteArray(imgByte, 0,
				imgByte.length);

		imgView.setImageBitmap(bitmap);
	}

	public static boolean createDirIfNotExist(File file) {
		boolean ret = true;
		if (!file.exists()) {
			if (!file.mkdirs()) {
				//System.out.println("Problem creating Image folder");
				ret = false;
			}
		}
		return ret;
	}

	public static String getRGBtoHEX(String RGB) {
		String[] rgb = RGB.split(",");
		int r = Integer.parseInt(rgb[0]);
		int g = Integer.parseInt(rgb[1]);
		int b = Integer.parseInt(rgb[2]);
		String hexColor = String.format("#%02x%02x%02x", r, g, b);
		return hexColor;
	}


	public static Drawable byteToDrawable(byte[] byteArray) {
		if (byteArray != null && byteArray.length > 0) {
			ByteArrayInputStream ins = new ByteArrayInputStream(byteArray);
			return Drawable.createFromStream(ins, null);
		} else {
			return null;
		}
	}

	/*public static void LoadPicassoImage(final String url,
			final ImageView imgView, final Context mContext) {
		((Activity) mContext).runOnUiThread(new Runnable() {
			public void run() {
				Picasso.with(mContext).load(url).fit().into(imgView);
			}
		});
	}*/

	public static int getSpinnerPosByValue(List<String> spinnerItem, String myString) {
		int index = 0;
		for (int i=0;i<spinnerItem.size();i++){
			System.out.println(spinnerItem.get(i)+"........."+myString);
			// For compare with id write [0] and for value write [1]
			if (spinnerItem.get(i).trim().split("#:#")[0].equalsIgnoreCase(myString.trim())){
				index = i;
				break;
			}
		}
		return index;
	}

	public static int getSpinnerPosByValueWithoutSplit(List<String> spinnerItem, String myString) {
		int index = 0;
		for (int i=0;i<spinnerItem.size();i++){
			System.out.println(spinnerItem.get(i)+"........."+myString);
			// For compare with id write [0] and for value write [1]
			if (spinnerItem.get(i).trim().equalsIgnoreCase(myString.trim())){
				index = i;
				break;
			}
		}
		return index;
	}

	public static boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			if (files == null) {
				return true;
			}
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}

	public static byte[] getImageByte(Resources res, String imgUrl)
			throws IOException {
		ByteArrayOutputStream bais = new ByteArrayOutputStream();
		InputStream is = null;
		URL url = null;
		try {

			url = new URL(imgUrl);
			is = url.openStream();
			byte[] byteChunk = new byte[4096]; // Or whatever size you want to
			// read in at a time.
			int n;

			while ((n = is.read(byteChunk)) > 0) {
				bais.write(byteChunk, 0, n);
			}
		} catch (IOException e) {
			System.err.printf("Failed while reading bytes from %s: %s",
					url.toExternalForm(), e.getMessage());
			e.printStackTrace();
		} finally {
			if (is != null) {
				is.close();
			}
		}

		/*
		 * byte[] byteReturn = bais.toByteArray(); Bitmap bitmap =
		 * BitmapFactory.decodeByteArray(byteReturn, 0, byteReturn.length);
		 * bitmap.compress(CompressFormat.JPEG, 20, bais);
		 */

		return bais.toByteArray();
	}

	public static byte[] getImageByteFromDrawable(Resources res, int imgdrawable) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		Bitmap bitmap = BitmapFactory.decodeResource(res, imgdrawable);
		bitmap.compress(CompressFormat.PNG, 70, stream);
		byte[] byteArray = stream.toByteArray();
		return byteArray;
	}

	@SuppressLint("DefaultLocale")
	public static int countImgs(File file, int number) {
		File[] dirs = file.listFiles();
		String name = "";
		if (dirs != null) { // Sanity check
			for (File dir : dirs) {
				if (dir.isFile()) { // Check file or directory
					name = dir.getName().toLowerCase();
					// Add or delete extensions as needed
					if (name.endsWith(".jpg")) {
						number++;
					}
				} else
					number = countImgs(dir, number);
			}
		}
		return number;
	}

	public static void mDownloadAndSave(File file, String directory,
			String[] url, String[] imgName) {
		// Setting up file to write the image to.
		File f = file;
		// Open InputStream to download the image.
		InputStream is;
		try {
			for (int i = 0; i < url.length; i++) {
				is = new URL(url[i]).openStream();
				OutputStream os = new FileOutputStream(new File(f, directory
						+ imgName[i]));
				// Set up OutputStream to write data into image file.
				CopyStream(is, os);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {

		}
	}

	public static String getDeviceType(Context context) {
		//		System.out.println("Quick Mobi Indfo "
		//				+ android.os.Build.VERSION.RELEASE + "  "
		//				+ android.os.Build.MODEL);
		return android.os.Build.MODEL;
	}

	public static String getOsVersion(Context context) {
		return android.os.Build.VERSION.RELEASE;
	}

	public static void showDialog(String msg, Context context) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.AlertDialog_AppCompat);
		// Setting Dialog Title
		alertDialog.setTitle("Alert");
		// Setting Dialog Message
		alertDialog.setMessage(msg);
		alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int which) {

					// Write your code here to invoke YES event
				dialog.dismiss();
			}
		});
		// Showing Alert Message
		alertDialog.show();
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @param selection (Optional) Filter used in the query.
	 * @param selectionArgs (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri, String selection,
			String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = {
				column
		};

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
					null);
			if (cursor != null && cursor.moveToFirst()) {
				final int column_index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(column_index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}


	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

}