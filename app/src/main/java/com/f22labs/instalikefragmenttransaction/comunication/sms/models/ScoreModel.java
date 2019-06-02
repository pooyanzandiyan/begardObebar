package com.f22labs.instalikefragmenttransaction.comunication.sms.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ScoreModel implements Serializable {
	@SerializedName("clientId")
	String clientId;
	@SerializedName("fullName")
	String fullName;
	@SerializedName("allScore")
	String allScore;
	
	public String getClientId() {
		return clientId;
	}
	
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
	public String getFullName() {
		return fullName;
	}
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public String getAllScore() {
		return allScore;
	}
	
	public void setAllScore(String allScore) {
		this.allScore = allScore;
	}
}
