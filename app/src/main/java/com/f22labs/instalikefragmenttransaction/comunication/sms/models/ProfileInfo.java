package com.f22labs.instalikefragmenttransaction.comunication.sms.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProfileInfo implements Serializable {
	@SerializedName("day")
	String day;
	@SerializedName("trueQuestion")
	String trueQuestion;
	@SerializedName("falseQuestion")
	String falseQuestion;
	@SerializedName("score")
	String score;
	
	public String getDay() {
		return day;
	}
	
	public void setDay(String day) {
		this.day = day;
	}
	
	public String getTrueQuestion() {
		return trueQuestion;
	}
	
	public void setTrueQuestion(String trueQuestion) {
		this.trueQuestion = trueQuestion;
	}
	
	public String getFalseQuestion() {
		return falseQuestion;
	}
	
	public void setFalseQuestion(String falseQuestion) {
		this.falseQuestion = falseQuestion;
	}
	
	public String getScore() {
		return score;
	}
	
	public void setScore(String score) {
		this.score = score;
	}
}
