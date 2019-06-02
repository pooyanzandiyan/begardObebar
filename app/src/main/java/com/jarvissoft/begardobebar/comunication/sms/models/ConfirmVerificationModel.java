package com.jarvissoft.begardobebar.comunication.sms.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ConfirmVerificationModel implements Serializable {
	@SerializedName("status")
	private Boolean status;
	@SerializedName("client_id")
	private String mobile;
	@SerializedName("token")
	private String token;
	@SerializedName("msg")
	private String msg;
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
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
