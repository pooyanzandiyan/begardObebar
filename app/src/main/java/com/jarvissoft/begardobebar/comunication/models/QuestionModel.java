package com.jarvissoft.begardobebar.comunication.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class QuestionModel implements Serializable {
	@SerializedName("question")
	String question;
	@SerializedName("answer1")
	String answer1;
	@SerializedName("answer2")
	String answer2;
	@SerializedName("answer3")
	String answer3;
	@SerializedName("answer4")
	String answer4;
	@SerializedName("id")
	String id;
	@SerializedName("status")
	boolean status;
	@SerializedName("type")
	int type;
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public boolean isStatus() {
		return status;
	}
	
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getQuestion() {
		return question;
	}
	
	public void setQuestion(String question) {
		this.question = question;
	}
	
	public String getAnswer1() {
		return answer1;
	}
	
	public void setAnswer1(String answer1) {
		this.answer1 = answer1;
	}
	
	public String getAnswer2() {
		return answer2;
	}
	
	public void setAnswer2(String answer2) {
		this.answer2 = answer2;
	}
	
	public String getAnswer3() {
		return answer3;
	}
	
	public void setAnswer3(String answer3) {
		this.answer3 = answer3;
	}
	
	public String getAnswer4() {
		return answer4;
	}
	
	public void setAnswer4(String answer4) {
		this.answer4 = answer4;
	}
}
