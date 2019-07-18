package com.jarvissoft.begardobebar.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jarvissoft.begardobebar.R;
import com.jarvissoft.begardobebar.utils.pref.SystemPrefs;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

public class MyBaseActivity extends AppCompatActivity {
	ProgressDialog progressDialog;
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}
	
	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
	
	protected void shortToastMessage(String text) {
		//Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
		Snackbar.make(findViewById(android.R.id.content),text,Snackbar.LENGTH_SHORT).show();
	}
	
	protected void longToastMessage(String text) {
		Snackbar.make(findViewById(android.R.id.content),text,Snackbar.LENGTH_LONG).show();
		//Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}
	
	Typeface getFont() {
		String cmpltFontName = "iranYekanBoldFaNum.ttf";
		Typeface font = Typeface.createFromAsset(this.getAssets(), cmpltFontName);
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
			tmpIconId  =  findViewById((Integer) iconID);
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
	void showLoading() {
		progressDialog = ProgressDialog.show(this, "",
				"در حال بارگذاری...", true);
		progressDialog.setCancelable(false);
		progressDialog.show();
	}
	
	void cancelLoading() {
		if (progressDialog != null)
			progressDialog.dismiss();
	}

	public void showToastMessage(String message, int duration) {
		
//		Toast toast = Toast.makeText(getApplicationContext(), String.format(" %s ", message), duration);
//		LinearLayout toastLayout = (LinearLayout) toast.getView();
//		TextView toastMessage = toastLayout.findViewById(android.R.id.message);
//		toastMessage.setTextSize(11);
//		toastMessage.setTextColor(Color.WHITE);
//		toastMessage.setTypeface(getFont());
//		toastMessage.setGravity(Gravity.CENTER);
//		toastLayout.setBackgroundResource(R.drawable.bg_toast);
//		toast.show();
		Snackbar.make(findViewById(android.R.id.content),message,duration).show();
	}
}
