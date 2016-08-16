package com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.commonutility.GlobalData;
import com.model.EarnCreditModel;
import com.cashmobi.FragEarnCredits;
import com.cashmobi.R;

import java.util.List;

public class AdapterEarnCredits extends BaseAdapter {
	private List<EarnCreditModel> mainData;
	private final Context _context;
	private static LayoutInflater _inflater = null;
	FragEarnCredits frag;
	GlobalData gd;

	public AdapterEarnCredits(FragEarnCredits frag, Context context, List<EarnCreditModel> lst) {
		this.frag 	  = frag;
		this._context = context;
		this.mainData = lst;
		gd			  = new GlobalData(_context);
		_inflater 	  = (LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return mainData.size();
	}

	public EarnCreditModel getItem(int position) {
		return mainData.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public void removeItem(int position) {
		mainData.remove(position);
		notifyDataSetChanged();
	}

	public void removeItem(EarnCreditModel item) {
		mainData.remove(item);
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (convertView == null) {
			view = _inflater.inflate(R.layout.earn_credits_list_item, parent, false);
		}

		EarnCreditModel offerItem 	= mainData.get(position);

		TextView  tvName 			= (TextView)view.findViewById(R.id.txtname);
		TextView  tvDeatil	 		= (TextView)view.findViewById(R.id.txtdesc);
		TextView  tvCredits			= (TextView)view.findViewById(R.id.txtcredits);
		ImageView imgItem 			= (ImageView)view.findViewById(R.id.imgcreditcompany);

		ImageView badgeImage = (ImageView)view.findViewById(R.id.list_offer_badge);
		TextView badgeText = (TextView)view.findViewById(R.id.list_offer_badge_text);

		if(offerItem.isBadgeVisible()) {
			badgeImage.setVisibility(View.VISIBLE);
			badgeText.setVisibility(View.VISIBLE);
		} else {
			badgeImage.setVisibility(View.GONE);
			badgeText.setVisibility(View.GONE);
		}

		tvName.setText(offerItem.getOfferName());
		tvDeatil.setText(offerItem.getOfferDetail());
		tvCredits.setText(offerItem.getOfferCredit());
		imgItem.setImageResource(offerItem.getImage());

		if (offerItem.getOfferCredit().equals("")) {
			tvCredits.setText(_context.getResources().getString(R.string.screen_earn_credits_offer_prompt));
		}

		if (offerItem.getOfferDetail().equals("")) {
			tvDeatil.setVisibility(View.GONE);
		} else {
			tvDeatil.setVisibility(View.VISIBLE);
		}
		return view;
	}
}
