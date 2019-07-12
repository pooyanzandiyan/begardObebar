package com.jarvissoft.begardobebar.utils;

import android.util.Log;

import static com.jarvissoft.begardobebar.BegardObebarApplication.LoggerName;

public class Logger {
	private static final Logger instance = new Logger();
	public static Logger getInstance(){ return instance; }
	public void log(String meesage){
		Log.v(LoggerName,meesage);
		
	}
}
