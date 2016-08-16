package com.commonutility;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;

import com.cashmobi.R;

public class CustomProgressDialog extends Dialog {
	public CustomProgressDialog(Context context, int layoutResID) {
		super(context, R.style.TransparentProgressDialog);
		WindowManager.LayoutParams wlmp = getWindow().getAttributes();
		wlmp.gravity = Gravity.CENTER_HORIZONTAL;
		getWindow().setAttributes(wlmp);
		setTitle(null);
		setCancelable(true);
		setOnCancelListener(null);
		setContentView(layoutResID);
	}

	@Override
	public void show() {
		super.show();
	}
}
