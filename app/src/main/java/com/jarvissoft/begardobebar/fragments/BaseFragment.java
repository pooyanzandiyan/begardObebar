package com.jarvissoft.begardobebar.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jarvissoft.begardobebar.R;
import com.jarvissoft.begardobebar.activities.AuthenticationActivity;
import com.jarvissoft.begardobebar.comunication.app.AppService;
import com.jarvissoft.begardobebar.utils.pref.SystemPrefs;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

/**
 * Created by jarvissoft on 07/03/17.
 */

public class BaseFragment extends Fragment {
	
	public static final String ARGS_INSTANCE = "com.jarvissoft.begardobebar";
	ProgressDialog progressDialog;
	
	FragmentNavigation mFragmentNavigation;
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	void showLoading() {
		progressDialog = ProgressDialog.show(getActivity(), "",
				"در حال بارگذاری...", true);
		progressDialog.setCancelable(false);
		progressDialog.show();
	}
	
	void cancelLoading() {
		if (progressDialog != null)
			progressDialog.dismiss();
	}
	Typeface getFont() {
		String cmpltFontName = "iranYekanBoldFaNum.ttf";
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(), cmpltFontName);
		return font;
	}
	MaterialTapTargetPrompt materialTapTargetPrompt;
	public <T> void showFirstRuntimeHelp(T iconID, String titleID,  String msgID, int prefID){
		View tmpIconId;
		if( materialTapTargetPrompt !=null )
			return;
		if( iconID instanceof View ){
			tmpIconId  = (View) iconID;
		}else{
			tmpIconId  =  getActivity().findViewById((Integer) iconID);
		}
		
		if (tmpIconId instanceof TextView) {
			((TextView) tmpIconId).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
		}
		View finalTmpIconId = tmpIconId;
		materialTapTargetPrompt = new MaterialTapTargetPrompt.Builder(this)
				.setTarget(tmpIconId)
				.setPrimaryTextTypeface(getFont())
				.setSecondaryTextTypeface(getFont())
				.setPrimaryTextGravity(1)
				.setSecondaryTextGravity(0)
				.setPrimaryTextSize(R.dimen.helpFontSize)
				.setSecondaryTextSize(R.dimen.helpFontSize)
				.setBackgroundColour(getResources().getColor(R.color.secondary_text))
				.setPrimaryText(titleID)
				.setSecondaryText(msgID)
				.setBackButtonDismissEnabled(true)
				.setFocalColour(getResources().getColor(R.color.primary_text))
				.setPromptStateChangeListener((prompt, state) -> {
					if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED
							|| state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED){
						if (finalTmpIconId instanceof TextView) {
							((TextView) finalTmpIconId).setTextColor(getResources().getColor(R.color.textColorPrimary));
						}
						materialTapTargetPrompt.finish();
						materialTapTargetPrompt = null;
						SystemPrefs.getInstance().setNotShownAgain(true, prefID);
					}
				})
				.show();
	}
	void checkToken() {
		AppService.getInstance().checkToken(result -> {
			if (result != null) {
				if (result.equals("nok")) {
					SystemPrefs.getInstance().setLogedIn(false);
					startActivity(new Intent(getActivity(), AuthenticationActivity.class));
					ToastMessage("توکن شما منقضی شده است. لطفا دوباره وارد شوید");
					getActivity().finish();
				}
			}
		});
	}
	
	protected void ToastMessage(String text) {
		Snackbar.make(getActivity().findViewById(android.R.id.content),text,Snackbar.LENGTH_LONG).show();
		//Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
	}
	
	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof FragmentNavigation) {
			mFragmentNavigation = (FragmentNavigation) context;
		}
	}
	
	public interface FragmentNavigation {
		void pushFragment(Fragment fragment);
	}
	
	
}
