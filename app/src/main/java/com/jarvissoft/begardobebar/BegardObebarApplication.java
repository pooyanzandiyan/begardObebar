package com.jarvissoft.begardobebar;

import android.app.Application;
import android.view.View;
import android.widget.RadioButton;

import com.jarvissoft.begardobebar.comunication.models.QuestionModel;
import com.jarvissoft.begardobebar.utils.pref.SystemPrefs;

import java.util.ArrayList;
import java.util.List;

import co.ronash.pushe.Pushe;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class BegardObebarApplication extends Application {
public static boolean InMarkar=false;
	private static final BegardObebarApplication instance = new BegardObebarApplication();
	public static QuestionModel question ;
	public static String imgId="" ;
	public static boolean isFirst=true ;
	public static View view;
	public static List<RadioButton> radioButtons=new ArrayList<>();
	public static String LoggerName="BegardObebarDriod";
	
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
		Pushe.initialize(this,false);
	}
	
}
