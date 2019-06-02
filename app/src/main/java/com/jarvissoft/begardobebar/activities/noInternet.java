package com.jarvissoft.begardobebar.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.jarvissoft.begardobebar.G;
import com.jarvissoft.begardobebar.R;
import com.jarvissoft.begardobebar.utils.NetworkUtils;

public class noInternet extends MyBaseActivity {
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.no_internet);
		
		final Handler handler = new Handler();
		final Runnable r = new Runnable() {
			public void run() {
				if (NetworkUtils.isConnected(noInternet.this)) {
						G.isFirst = true;
						finish();
				}
				handler.postDelayed(this, 1000);
				
			}
		};
		handler.postDelayed(r, 1000);
	}
	
	@Override
	public void onBackPressed() {
		return;
	}
}
