package com.jarvissoft.begardobebar.utils.pref;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Set;

public abstract class BasePreferences {
	protected SharedPreferences sharedPrefs = null;

	protected void init(Context context, String prefName){
		sharedPrefs = context.getApplicationContext().getSharedPreferences(prefName, Activity.MODE_PRIVATE);
	}

	public void setString(String key, String value){
		SharedPreferences.Editor editor = sharedPrefs.edit();
		editor.putString(key, value);
		editor.apply();
	}
	public void setInteger(String key, int value){
		SharedPreferences.Editor editor = sharedPrefs.edit();
		editor.putInt(key, value);
		boolean ok= editor.commit();
		int i=10;
	}
	public void setFloat(String key, float value){
		SharedPreferences.Editor editor = sharedPrefs.edit();
		editor.putFloat(key, value);
		editor.apply();
	}
	public void setBoolean(String key, boolean value){
		SharedPreferences.Editor editor = sharedPrefs.edit();
		editor.putBoolean(key, value);
		editor.apply();
	}
	public void setLong(String key, long value){
		SharedPreferences.Editor editor = sharedPrefs.edit();
		editor.putLong(key, value);
		editor.apply();
	}
	public void setStringSet(String key, Set<String> value) {
		SharedPreferences.Editor editor = sharedPrefs.edit();
		editor.putStringSet(key, value);
		editor.apply();
	}

	<T> void setList(String key, List<T> valueList){
		SharedPreferences.Editor editor = sharedPrefs.edit();
		editor.putString(key, new Gson().toJson(valueList));
		editor.apply();
	}

	<T> void setObject(String key, T object){
		SharedPreferences.Editor editor = sharedPrefs.edit();
		editor.putString(key, new Gson().toJson(object));
		editor.apply();
	}

	public String getString(String key, String def){
		return sharedPrefs.getString(key, def);
	}
	public int getInteger(String key, int def){
		return sharedPrefs.getInt(key, def);
	}
	public float getFloat(String key, float def){
		return sharedPrefs.getFloat(key, def);
	}
	public boolean getBoolean(String key, boolean def){
		return sharedPrefs.getBoolean(key, def);
	}
	public long getLong(String key, long def){
		return sharedPrefs.getLong(key, def);
	}
	

	<V extends List<T>, T> List<T> getList(String key , TypeToken<V> token){
		String json = sharedPrefs.getString(key, "");
		return new Gson().fromJson(json, token.getType());
	}

	<T> T getObject(String key , TypeToken<T> token){
		String json = sharedPrefs.getString(key, "");
		return new Gson().fromJson(json, token.getType());
	}

	void clearItem(String key){
		if(sharedPrefs.contains(key))
			sharedPrefs.edit().remove(key).apply();
	}
}
