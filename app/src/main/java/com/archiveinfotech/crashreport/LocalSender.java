package com.archiveinfotech.crashreport;

import android.content.Context;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import org.acra.ACRA;
import org.acra.ACRAConstants;
import org.acra.ReportField;
import org.acra.collector.CrashReportData;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

public class LocalSender implements ReportSender {  
	private final Map<ReportField, String> mMapping = new HashMap<ReportField, String>() ;  
	FileOutputStream crashReport = null;  
	Context ctx;  
	public LocalSender(Context ct) {  
		ctx = ct;  
	}  

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void send(CrashReportData report) throws ReportSenderException {  
		final Map<String, String> finalReport = remap(report);  
		ByteArrayOutputStream buf = new ByteArrayOutputStream();  
		try {  
			Set set = finalReport.entrySet();  
			Iterator i = set.iterator();  
			String tmp;  
			while (i.hasNext()) {  
				Map.Entry<String,String> me = (Map.Entry) i.next();  
				tmp = "[" + me.getKey() + "]=" + me.getValue();  
				buf.write(tmp.getBytes());  
			}  

			ParseFile myFile = new ParseFile("akhbarona_crash.txt", buf.toByteArray());  

			myFile.save();  
			ParseObject jobApplication = new ParseObject("Akhbarona_AppCrash");  
			jobApplication.put("Date_Time", getcurrentDate() + " " + getcurrentTime());  
			jobApplication.put("applicantResumeFile", myFile);  

			try {  
				jobApplication.save();  
			} catch (ParseException e) {  
				e.printStackTrace();  
			}  
		}catch (FileNotFoundException e) {  
			e.printStackTrace();
		}catch (IOException e) {  
			e.printStackTrace();
		}catch (ParseException e) {  
			e.printStackTrace();  
		}  
	}  

	private Map<String, String> remap(Map<ReportField, String> report) { 
		ReportField[] fields = ACRA.getConfig().customReportContent();  
		if (fields.length == 0) {  
			fields = ACRAConstants.DEFAULT_REPORT_FIELDS;  
		}  
		final Map<String, String> finalReport = new HashMap<String, String>(report.size());  
		for (ReportField field : fields) {  
			if (mMapping == null || mMapping.get(field) == null) {  
				finalReport.put(field.toString(), report.get(field));  
			}else {  
				finalReport.put(mMapping.get(field), report.get(field));  
			}  
		}  
		return finalReport;  
	}  

	public static String getcurrentDate() {
		Calendar today = Calendar.getInstance();
		int date = today.get(Calendar.DATE);
		int month = today.get(Calendar.MONTH);
		int year = today.get(Calendar.YEAR);
		TimeZone tz = TimeZone.getDefault();
		String zone = "TimeZone ="+tz.getDisplayName(false, TimeZone.SHORT)+" Timezon id =" +tz.getID();
		return year + "-" + (month+1) + "-" + date + "  " + zone;
	}

	public static String getcurrentTime() {
		Calendar today = Calendar.getInstance();
		int hour = today.get(Calendar.HOUR_OF_DAY);
		int minute = today.get(Calendar.MINUTE);
		return hour + ":" + minute;
	}

}  