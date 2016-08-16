package com.cashmobi;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.archiveinfotech.crashreport.Utils;
import com.commonutility.GlobalData;
import com.commonutility.PreferenceConnector;
import com.commonutility.WebService;
import com.commonutility.WebServiceListener;
import com.helper.MyUtils;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import static com.cashmobi.ActivityMainWallet.countryIconUrl;

public class InviteFriendsFragment extends Fragment implements OnClickListener, WebServiceListener {
	private Context aiContext;
	private View aiView = null;
	private boolean mAlreadyLoaded=false;
	private GlobalData gd;
	private TextView txtInvitationCode;
	private Button btnShareCopy, btnShare;
	private static TextView creditWallet;
	static ImageView image;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (aiView == null) {
			aiView = inflater.inflate(R.layout.fragment_invite, container, false);
		}
		Utils.setFontAllView((ViewGroup)aiView);
		MyUtils.sendScreenToGoogleAnalytics(getActivity().getApplication(), "Screen : Invite Friends");
		image = (ImageView) aiView.findViewById(R.id.image_view_country_flag);

		return aiView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState == null && !mAlreadyLoaded) {
			mAlreadyLoaded = true;
			aiContext = getActivity();
			aiView = getView();
			gd = new GlobalData(aiContext);

			txtInvitationCode = (TextView) aiView.findViewById(R.id.invite_id_textview);
			btnShareCopy = (Button) aiView.findViewById(R.id.btnsharecopy);
			btnShare = (Button) aiView.findViewById(R.id.btnshare);
			creditWallet = (TextView) aiView.findViewById(R.id.credit_wallet);

			btnShareCopy.setOnClickListener(this);
			btnShare.setOnClickListener(this);

			txtInvitationCode.setText(PreferenceConnector.readString(aiContext,
					PreferenceConnector.WALLETID, ""));
			creditWallet.setText(PreferenceConnector.readInteger(aiContext, PreferenceConnector.WALLETPOINTS,
					0) + "");

			((ActivityMainWallet) getActivity()).initCountryFlagIcon(aiView);
		}

		ImageView image = (ImageView) aiView.findViewById(R.id.image_view_country_flag);
		if (!countryIconUrl.isEmpty()) {
			Picasso.with(aiContext)
					.load(countryIconUrl)
					.error(R.drawable.ic_launcher)
					.into(image);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnsharecopy:
			ClipboardManager clipboard 	= (ClipboardManager)aiContext.getSystemService(Context.CLIPBOARD_SERVICE);
			ClipData clip 				= ClipData.newPlainText("Sensible Wallet", 
					PreferenceConnector.readString(aiContext, PreferenceConnector.WALLETID, ""));
			clipboard.setPrimaryClip(clip);

			GlobalData.showToast(getResources().getString(R.string.message_invite_id_copied), aiContext);
			break;
		case R.id.btnshare:
			GlobalData.ShareText(aiContext, PreferenceConnector.readString(aiContext, 
					PreferenceConnector.SHARETEXT, ""));
			break;
		default:
			break;
		}
	}

	private void callWebService(String postUrl, HashMap<String, String> hash) {
		WebService webService	=	new WebService(aiContext, "", postUrl, hash, this, WebService.POST);
		webService.execute();
	}

	@Override
	public void onWebServiceActionComplete(String result, String url) {
		System.out.println(result+".........jsonresponse....."+url);
	}
	private void switchFragment(Fragment fragment, String tag) {
		if (getActivity() == null)
			return;
		if (getActivity() instanceof ActivityMainWallet) {
			ActivityMainWallet mActivity = (ActivityMainWallet) getActivity();
			mActivity.switchContent(fragment, tag);
		}
	}
	public static void onUpdateView(Context aiContext) {
		// TODO Auto-generated method stub
		if(aiContext!=null && creditWallet!=null)
			creditWallet.setText(PreferenceConnector.readInteger(aiContext, PreferenceConnector.WALLETPOINTS,0)+"");
		if (!countryIconUrl.isEmpty()) {
			if (image!=null)
				Picasso.with(aiContext)
					.load(countryIconUrl)
					.error(R.drawable.ic_launcher)
					.into(image);
		}
	}
}