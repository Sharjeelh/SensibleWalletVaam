package com.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.archiveinfotech.crashreport.Utils;
import com.commonutility.GlobalData;
import com.model.ConnectModel;
import com.cashmobi.ConnectSocialFragment;
import com.cashmobi.R;
import com.squareup.picasso.Callback.EmptyCallback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterConnect extends BaseAdapter {
	private List<ConnectModel> mainData;
	private final Context _context;
	private static LayoutInflater _inflater = null;
	ConnectSocialFragment frag;
	GlobalData gd;

	public AdapterConnect(ConnectSocialFragment frag, Context context, List<ConnectModel> lst) {
		this.frag 	  = frag;
		this._context = context;
		this.mainData = lst;
		gd			  = new GlobalData(_context);
		_inflater 	  = (LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return mainData.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public static class ViewHolder {}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (convertView == null) {
			view = _inflater.inflate(R.layout.social_connect_list_item, parent, false);
		}
		Utils.setFontAllView((ViewGroup)view);
		
		view.setTag(position+"");

		ConnectModel offerItem 	= mainData.get(position);

		TextView  tvCredits	 	= (TextView)view.findViewById(R.id.txtcredits);
		ImageView imgItem 		= (ImageView)view.findViewById(R.id.imgcreditcompany);

		String connectName = offerItem.getConnectName();
		tvCredits.setText(connectName == null || connectName.isEmpty() ? "Earn Credits" : connectName);
		//		imgItem.setImageResource(offerItem.getImage());

		if (offerItem.getConnectLogo().equals("")) {
			imgItem.setVisibility(View.GONE);
			imgItem.setImageResource(R.drawable.ic_launcher);
		}else {
			Picasso.with(_context)
			.load(offerItem.getConnectLogo())
			.error(R.drawable.ic_launcher)
			.into(imgItem, new EmptyCallback() {
				@Override 
				public void onSuccess() {
					//	progress.setVisibility(View.GONE);
				}

				@Override
				public void onError() {
					//	progress.setVisibility(View.GONE);
				}			
			});
		}

		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View vi) {
				int pos = Integer.parseInt(vi.getTag().toString());
				String url = mainData.get(pos).getConnectLink();
				if (!url.startsWith("http://") && !url.startsWith("https://")) {
					url = "http://" + url;
				}

				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				_context.startActivity(browserIntent);
			}
		});
		
		return view;
	}

	public boolean checkAppExist(String appPackageName) {
		try{
			_context.getPackageManager().getApplicationInfo(appPackageName, 0 );
			return true;
		}catch (PackageManager.NameNotFoundException e){
			return false;
		}
	}
}
