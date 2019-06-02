package com.f22labs.instalikefragmenttransaction.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.f22labs.instalikefragmenttransaction.G;
import com.f22labs.instalikefragmenttransaction.R;
import com.f22labs.instalikefragmenttransaction.utils.NetworkUtils;

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
