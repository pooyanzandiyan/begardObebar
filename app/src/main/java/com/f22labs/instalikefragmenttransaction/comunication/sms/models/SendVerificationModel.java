package com.f22labs.instalikefragmenttransaction.comunication.sms.models;

import android.text.BoringLayout;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SendVerificationModel implements Serializable {
	@SerializedName("status")
	private Boolean status;
	@SerializedName("client_id")
	private String mobile;
	
	public Boolean getStatus() {
		return status;
	}
	
	public void setStatus(Boolean status) {
		this.status = status;
	}
	
	public String getMobile() {
		return mobile;
	}
	
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
}
