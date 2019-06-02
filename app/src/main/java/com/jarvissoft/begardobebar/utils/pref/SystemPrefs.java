package com.jarvissoft.begardobebar.utils.pref;

import android.content.Context;




public class SystemPrefs extends BasePreferences {
	private static final SystemPrefs instance = new SystemPrefs();
	public static SystemPrefs getInstance(){ return instance; }

	public static final String PrefName = "SystemPrefs";




    // region Authorization ( OTP with SMS verification)
    private static final String PREF_MOBILE_NUMBER= "mobile_number";
    private static final String PREF_TOKEN= "token";
    private static final String PREF_LOGIN= "login";
    private static final String PREF_IS_WAITING_FOR_SMS= "IsWaitingForSms";
   
    // endregion




	public void init(Context context){
		super.init(context, PrefName);
	}

	public void setIsWaitingForSms(boolean isWaiting) {
		setBoolean(PREF_IS_WAITING_FOR_SMS, isWaiting);
	}

	public boolean isWaitingForSms() {
		return getBoolean(PREF_IS_WAITING_FOR_SMS, false);
	}


	public void setMobileNumber(String mobileNumber) {
		setString(PREF_MOBILE_NUMBER, mobileNumber);
	}

	public String getMobileNumber() {
		return getString(PREF_MOBILE_NUMBER, null);
	}

	public void setLogedIn(boolean userName) {
		setBoolean(PREF_LOGIN, userName);
	}

	public boolean getLogedIn() {
		return getBoolean(PREF_LOGIN, false);
	}
	public void setToken(String userName) {
		setString(PREF_TOKEN, userName);
	}
	
	public String getToken() {
		return getString(PREF_TOKEN, null);
	}
}