package com.jarvissoft.begardobebar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jarvissoft.begardobebar.R;
import com.jarvissoft.begardobebar.utils.pref.SystemPrefs;

public class SplashScreenActivity extends MyBaseActivity {
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		new Thread() {
			public void run() {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.getStackTrace();
				} finally {
					if (SystemPrefs.getInstance().getLogedIn())
						startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
					else
						startActivity(new Intent(SplashScreenActivity.this, AuthenticationActivity.class));
					finish();
				}
			}
		}.start();
	}
	
	// exiting the app if the back key is pressed
	@Override
	public void onBackPressed() {
		return;
	}
}
