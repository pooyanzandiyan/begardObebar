package com.jarvissoft.begardobebar.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

public class AvatarManager {
	private static final AvatarManager instance = new AvatarManager();
	public static AvatarManager getInstance() {
		return instance;
	}


	public Bitmap getAvatar (Context context,Integer id){
		try {
			InputStream stream=context.getResources().getAssets().open(String.format("avatar/%s.png",String.valueOf(id)));
			return BitmapFactory.decodeStream(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public Bitmap getAvatar (Context context,String id){
		try {
			InputStream stream=context.getResources().getAssets().open(String.format("avatar/%s.png",id));
			return BitmapFactory.decodeStream(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


}
