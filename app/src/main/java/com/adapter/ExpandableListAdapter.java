package com.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.archiveinfotech.crashreport.Utils;
import com.commonutility.PreferenceConnector;
import com.model.ExpandableModel;
import com.cashmobi.R;
import com.squareup.picasso.Callback.EmptyCallback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
	private Context _context;
	private List<String> _listDataHeader; // header titles
	// child data in format of header title, child title
	private List<ExpandableModel> _listDataChild;

	public ExpandableListAdapter(Context context, List<String> listDataHeader, List<ExpandableModel> listChildData) {
		this._context = context;
		this._listDataHeader 	= listDataHeader;
		this._listDataChild 	= listChildData;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return ((this._listDataChild.get(groupPosition).getListDataGroupChild()).get(childPosititon)).getStr_offers_redeem_amount();
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		final String childText = (String)getChild(groupPosition, childPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater)this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_item, parent, false);
		}
		Utils.setFontAllView((ViewGroup)convertView);

		TextView txtListChild 	= (TextView)convertView.findViewById(R.id.txtcardname);
		TextView txtListCredits = (TextView)convertView.findViewById(R.id.txtcardcredits);
		ImageView imgList		= (ImageView)convertView.findViewById(R.id.imgcreditcompany);

		String offerPrice = (_listDataChild.get(groupPosition).getListDataGroupChild()).
				get(childPosition).getStr_offer_price();

		String offerAmt	  = (_listDataChild.get(groupPosition).getListDataGroupChild()).
				get(childPosition).getStr_offers_redeem_amount();

		txtListChild.setText(PreferenceConnector.readString(_context, 
				PreferenceConnector.COUNTRYSYMBOL, "$") + 
				offerAmt /*+ " " + this._listDataHeader.get(groupPosition)*/);

		txtListCredits.setText("-" + offerPrice + " Credits");

		String imagePath = (_listDataChild.get(groupPosition).getListDataGroupChild()).
				get(childPosition).getStrOfferImage();
		
		if (imagePath.equals("")) {
			imgList.setVisibility(View.GONE);
			imgList.setImageResource(R.drawable.app_icon);
		}else {
			Picasso.with(_context)
			.load(imagePath)
			.error(R.drawable.ic_launcher)
			.into(imgList, new EmptyCallback() {
				@Override 
				public void onSuccess() {}

				@Override
				public void onError() {}			
			});
		}
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return (this._listDataChild.get(groupPosition).getListDataGroupChild()).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		if (_listDataHeader.size() != 0 && groupPosition <= _listDataHeader.size()) {
			return this._listDataHeader.get(groupPosition);
		}
		return "";
	}

	@Override
	public int getGroupCount() {
		return this._listDataHeader.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		String headerTitle = (String) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater)this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.reward_list_header_item, parent, false);
		}
		Utils.setFontAllView((ViewGroup)convertView);

		TextView lblListHeader = (TextView)convertView.findViewById(R.id.lblListHeader);
		lblListHeader.setTypeface(null, Typeface.BOLD);
		lblListHeader.setText(headerTitle);

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
