package com.archiveinfotech.crashreport;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.InputStream;
import java.io.OutputStream;

public class Utils {
	public static final String BOLD_FONT_PATH 		= "fonts/champabold.ttf";
	public static final String REGULAR_FONT_PATH 	= "fonts/champa.ttf";
	public static Typeface regularFont, boldFont;

	public static void loadFonts() {
		regularFont = Typeface.createFromAsset(ParseCrash.getContext().getAssets(),
				Utils.REGULAR_FONT_PATH);
		boldFont = Typeface.createFromAsset(ParseCrash.getContext().getAssets(),
				Utils.BOLD_FONT_PATH);

	}

	@SuppressLint("DefaultLocale")
	public static void setFontAllView(ViewGroup vg) {
		for (int i = 0; i < vg.getChildCount(); ++i) {
			View child 	= vg.getChildAt(i);

			if (child instanceof ViewGroup) {
				setFontAllView((ViewGroup) child);
			} else if (child != null) {
				Typeface face;
				if (child instanceof TextView) {
					if(((TextView) child).getTypeface() != null){
						if(((TextView) child).getTypeface().getStyle() == Typeface.BOLD){
							face = boldFont;
						}else {
							face = regularFont;
						}
					}else {
						face = regularFont;
					}

					TextView textView = (TextView) child;
					textView.setTypeface(face);
				} else if (child instanceof EditText) {
					if(((EditText) child).getTypeface() != null){
						if(((EditText) child).getTypeface().getStyle() == Typeface.BOLD){
							face = boldFont;
						}else {
							face = regularFont;
						}
					}else {
						face = regularFont;
					}

					EditText editView = (EditText) child;
					editView.setTypeface(face);
				} else if (child instanceof RadioButton) {
					RadioButton radioView = (RadioButton) child;
					radioView.setTypeface(regularFont);
				} else if (child instanceof CheckBox) {
					CheckBox checkboxView = (CheckBox) child;
					checkboxView.setTypeface(regularFont);
				}
				else if (child instanceof Button) {
					Button buttonView = (Button) child;
					buttonView.setTypeface(regularFont);
				}
			}
		}
	}
	public static void firstText(TextView textView)
	{
		textView.setTypeface(boldFont,Typeface.BOLD);
	}
	public static void lastName(TextView textView)
	{
		textView.setTypeface(regularFont);
	}
	

	//	public static void setFontToTextView(TextView textView) {
	//		Typeface face;;
	//		if(textView.getTypeface().getStyle() == Typeface.BOLD){
	//			face = boldFont;
	//		}else {
	//			face = regularFont;
	//		}
	//		textView.setTypeface(face);
	//	}
	//	
	//	public static void setFontToEditText(EditText editView) {
	//		Typeface face;;
	//		if(editView.getTypeface().getStyle() == Typeface.BOLD){
	//			face = boldFont;
	//		}else {
	//			face = regularFont;
	//		}
	//		editView.setTypeface(face);
	//	}

	//	public static void setFontToButton(View view) {
	//		Typeface face;;
	//		if(((Button) view).getTypeface().getStyle() == Typeface.BOLD){
	//			face = boldFont;
	//		}else {
	//			face = regularFont;
	//		}
	//		Button btnView = (Button) view;
	//		btnView.setTypeface(face);
	//	}
	//	
	//	public static void setFontToRadioButton(View view) {
	//		RadioButton radioView = (RadioButton) view;
	//		radioView.setTypeface(regularFont);
	//	}
	//	
	//	public static void setFontToCheckBox(View view) {
	//		CheckBox checkboxView = (CheckBox) view;
	//		checkboxView.setTypeface(regularFont);
	//	}

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
			ex.printStackTrace();
		}
	}

	public static Bitmap GetImageFromAssets(Context context,String imagePath){
		Bitmap bmp = null;
		try {
			InputStream bitmap= context.getAssets().open(imagePath);
			bmp = BitmapFactory.decodeStream(bitmap);

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return bmp;
	}

}
