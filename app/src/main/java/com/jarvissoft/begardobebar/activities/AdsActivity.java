package com.jarvissoft.begardobebar.activities;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.jarvissoft.begardobebar.R;

import java.util.concurrent.TimeUnit;

public class AdsActivity extends BaseActivity {
	private double currentTime = 0;
	private double duration = 0;
	private ProgressBar prgCurrentTime;
	private Handler mHandler = new Handler();
	MediaPlayer mediaPlayer;
	TextView percent;
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ads);
		VideoView vidView = findViewById(R.id.adsVideo);
		prgCurrentTime=findViewById(R.id.prgCurrentTime);
		percent =findViewById(R.id.txtPercent);
		String vidAddress = "https://pzandian.ir/ads_video/test.mp4";
		Uri vidUri = Uri.parse(vidAddress);
		vidView.setVideoURI(vidUri);
		vidView.start();
		showLoading();
		
		vidView.setOnPreparedListener(mp -> {
			mediaPlayer=mp;
			cancelLoading();
			prgCurrentTime.setMax(mediaPlayer.getDuration() /1000);
			updateProgressBar();
		});
		vidView.setOnCompletionListener(mp -> {
			mp.stop();
			mHandler.removeCallbacks(UpdateProgress);
			finish();
		});
	}
	
	@Override
	public void onBackPressed() {
		mHandler.removeCallbacks(UpdateProgress);
		super.onBackPressed();
	}
	
	public void updateProgressBar() {
		if (mHandler != null) {
			mHandler.postDelayed(UpdateProgress,100);
		}
	}
	private int getPercent(int duration,int second){
		return (second*100)/duration;
	}
	private Runnable UpdateProgress = new Runnable() {
		@SuppressLint("SetTextI18n")
		public void run() {
			currentTime = mediaPlayer.getCurrentPosition();
			duration = mediaPlayer.getDuration();
			
			long CurrentSec=TimeUnit.MILLISECONDS.toSeconds((long) currentTime) -
					TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
							toMinutes((long) currentTime));
			long Duration=TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) duration));
			prgCurrentTime.setProgress((int) CurrentSec);
			percent.setText(getPercent((int)Duration,(int)CurrentSec)+" %" );
			if(CurrentSec>Duration-15){
				findViewById(R.id.adsVideo).setOnClickListener(v -> {
				
				});
			}
			mHandler.postDelayed(this, 500);
		}
	};
}
