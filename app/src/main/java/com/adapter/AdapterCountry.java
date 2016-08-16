package com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.archiveinfotech.crashreport.Utils;
import com.commonutility.GlobalData;
import com.model.CountryListModel;
import com.cashmobi.R;
import com.squareup.picasso.Callback.EmptyCallback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterCountry extends BaseAdapter {
	private List<CountryListModel> mainData;
	private final Context _context;
	private static LayoutInflater _inflater = null;
	GlobalData gd;

	public AdapterCountry(Context context, List<CountryListModel> lst) {
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (convertView == null) {
			view = _inflater.inflate(R.layout.select_country_list_item, parent, false);
		}
		Utils.setFontAllView((ViewGroup)view);
		
		view.setTag(position+"");

		CountryListModel offerItem 	= mainData.get(position);
		ImageView  imgCountry 	= (ImageView)view.findViewById(R.id.imgcountry);
		if (offerItem.getStr_country_logo().equals("")) {
			imgCountry.setVisibility(View.GONE);
			imgCountry.setImageResource(R.drawable.app_icon);
		}else {
			Picasso.with(_context)
			.load(offerItem.getStr_country_logo())
			.error(R.drawable.ic_launcher)
			.into(imgCountry, new EmptyCallback() {
				@Override 
				public void onSuccess() {}

				@Override
				public void onError() {}			
			});
		}
		
		TextView  tvCredits	 	= (TextView)view.findViewById(R.id.txtcountry);
		tvCredits.setText(offerItem.getStr_country_name());

		return view;
	}
}
