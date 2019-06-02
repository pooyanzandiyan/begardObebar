package com.jarvissoft.begardobebar.comunication.sms.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProfileModel implements Serializable {
	@SerializedName("clientId")
	String clientId;
	@SerializedName("fullName")
	String fullName;
	@SerializedName("Rank")
	String rank;
	@SerializedName("profileImageId")
	String profileImageId;
	@SerializedName("allTrueQuestion")
	String allTrueQuestion;
	@SerializedName("allFalseQuestion")
	String allFalseQuestion;
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
	
	public String getRank() {
		return rank;
	}
	
	public void setRank(String rank) {
		this.rank = rank;
	}
	
	public String getProfileImageId() {
		return profileImageId;
	}
	
	public void setProfileImageId(String profileImageId) {
		this.profileImageId = profileImageId;
	}
	
	public String getAllTrueQuestion() {
		return allTrueQuestion;
	}
	
	public void setAllTrueQuestion(String allTrueQuestion) {
		this.allTrueQuestion = allTrueQuestion;
	}
	
	public String getAllFalseQuestion() {
		return allFalseQuestion;
	}
	
	public void setAllFalseQuestion(String allFalseQuestion) {
		this.allFalseQuestion = allFalseQuestion;
	}
	
	public String getAllScore() {
		return allScore;
	}
	
	public void setAllScore(String allScore) {
		this.allScore = allScore;
	}
}
