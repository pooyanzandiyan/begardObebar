package com.jarvissoft.begardobebar;

import android.app.Application;
import android.view.View;

import com.jarvissoft.begardobebar.comunication.models.QuestionModel;
import com.jarvissoft.begardobebar.utils.pref.SystemPrefs;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class BegardObebarApplication extends Application {
public static boolean InMarkar=false;
	private static final BegardObebarApplication instance = new BegardObebarApplication();
	public static QuestionModel question ;
	public static String imgId ;
	public static boolean isFirst=true ;
	public static View view;
	
	public static BegardObebarApplication getInstance() {
		return instance;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
				.setDefaultFontPath("iranYekanBoldFaNum.ttf")
				.setFontAttrId(R.attr.fontPath)
				.build()
		);
		SystemPrefs.getInstance().init(this);
	}
}
