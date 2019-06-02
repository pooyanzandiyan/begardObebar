package com.jarvissoft.begardobebar;

import android.app.Application;
import android.view.View;

import com.jarvissoft.begardobebar.comunication.sms.models.QuestionModel;
import com.jarvissoft.begardobebar.utils.pref.SystemPrefs;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class G extends Application {
public static boolean InMarkar=false;
	private static final G instance = new G();
	public static QuestionModel question ;
	public static String imgId ;
	public static boolean isFirst=true ;
	public static View view;
	
	public static G getInstance() {
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
