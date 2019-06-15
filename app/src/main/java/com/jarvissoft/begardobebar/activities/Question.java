package com.jarvissoft.begardobebar.activities;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jarvissoft.begardobebar.G;
import com.jarvissoft.begardobebar.R;
import com.jarvissoft.begardobebar.comunication.sms.app.AppService;
import com.jarvissoft.begardobebar.comunication.models.QuestionModel;

public class Question extends MyBaseActivity {
	CountDownTimer countDownTimer;
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question);
		TextView question=findViewById(R.id.txtQuestion);
		Button answer1=findViewById(R.id.answer1);
		Button answer2=findViewById(R.id.answer2);
		Button answer3=findViewById(R.id.answer3);
		Button answer4=findViewById(R.id.answer4);
		final ProgressBar prg=findViewById(R.id.prgTime);
		if(G.question!=null){
			QuestionModel q =G.question;
			question.setText(q.getQuestion());
			answer1.setText(q.getAnswer1());
			answer2.setText(q.getAnswer2());
			answer3.setText(q.getAnswer3());
			answer4.setText(q.getAnswer4());
			prg.setMax(30);
			
		}
		countDownTimer=new CountDownTimer(30000,1000) {
			@Override
			public void onTick(long millisUntilFinished) {
				prg.setProgress((int)millisUntilFinished/1000);
			}
			
			@Override
			public void onFinish() {
				setAnswer(G.question.getId(),"5");
				finish();
			}
		};
		countDownTimer.start();
	}
	public void CheckAnswer(View v){
		countDownTimer.cancel();
		String answer="";
	switch (v.getId())
	{
		case R.id.answer1:
			answer="1";
			break;
		case R.id.answer2:
			answer="2";
			break;
		case R.id.answer3:
			answer="3";
			break;
		case R.id.answer4:
			answer="4";
			break;
	}
		setAnswer(G.question.getId(),answer);
	finish();
	}
	void setAnswer(String id,String answer){
		AppService.getInstance().setAnswer(id, answer, result -> {
			if(result==null){
				shortToastMessage("خطا در برقراری ارتباط با سرور");
			}
		});
		
	}
	
	@Override
	public void onBackPressed() {
		return;
	}
}
