package com.jarvissoft.begardobebar.comunication.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AdsModel implements Serializable {
	@SerializedName("imgUrl")
	String imgUrl;
	@SerializedName("siteUrl")
	String siteUrl;
	
	public String getImgUrl() {
		return imgUrl;
	}
	
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	
	public String getSiteUrl() {
		return siteUrl;
	}
	
	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}
}
