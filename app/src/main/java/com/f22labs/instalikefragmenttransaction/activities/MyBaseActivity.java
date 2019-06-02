package com.f22labs.instalikefragmenttransaction.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.f22labs.instalikefragmenttransaction.G;
import com.f22labs.instalikefragmenttransaction.R;
import com.f22labs.instalikefragmenttransaction.utils.NetworkUtils;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

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
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}
	
	protected void longToastMessage(String text) {
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}
	
	Typeface getFont() {
		String cmpltFontName = "iranYekanBoldFaNum.ttf";
		Typeface font = Typeface.createFromAsset(this.getAssets(), cmpltFontName);
		return font;
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
		Toast toast = Toast.makeText(getApplicationContext(), String.format(" %s ", message), duration);
		LinearLayout toastLayout = (LinearLayout) toast.getView();
		TextView toastMessage = toastLayout.findViewById(android.R.id.message);
		toastMessage.setTextSize(11);
		toastMessage.setTextColor(Color.WHITE);
		toastMessage.setTypeface(getFont());
		toastMessage.setGravity(Gravity.CENTER);
		toastLayout.setBackgroundResource(R.drawable.bg_toast);
		toast.show();
	}
}
