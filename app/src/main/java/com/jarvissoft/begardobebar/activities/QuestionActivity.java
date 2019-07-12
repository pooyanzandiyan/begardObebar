package com.jarvissoft.begardobebar.activities;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jarvissoft.begardobebar.BegardObebarApplication;
import com.jarvissoft.begardobebar.R;
import com.jarvissoft.begardobebar.comunication.app.AppService;
import com.jarvissoft.begardobebar.comunication.models.QuestionModel;

public class QuestionActivity extends MyBaseActivity {
	CountDownTimer countDownTimer;
	int time;
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question);
		TextView question = findViewById(R.id.txtQuestion);
		Button answer1 = findViewById(R.id.answer1);
		Button answer2 = findViewById(R.id.answer2);
		Button answer3 = findViewById(R.id.answer3);
		Button answer4 = findViewById(R.id.answer4);
		final ProgressBar prg = findViewById(R.id.prgTime);
		if (BegardObebarApplication.question != null) {
			QuestionModel q = BegardObebarApplication.question;
			question.setText(q.getQuestion());
			answer1.setText(q.getAnswer1());
			answer2.setText(q.getAnswer2());
			answer3.setText(q.getAnswer3());
			answer4.setText(q.getAnswer4());
			if (q.getType() == 0)
				time = 15;
			else
				time = 120;
			
		}
		prg.setMax(time);
		countDownTimer = new CountDownTimer(time*1000, 1000) {
			@Override
			public void onTick(long millisUntilFinished) {
				prg.setProgress((int) millisUntilFinished / 1000);
			}
			
			@Override
			public void onFinish() {
				setAnswer(BegardObebarApplication.question.getId(), "5");
				finish();
			}
		};
		countDownTimer.start();
	}
	
	public void CheckAnswer(View v) {
		countDownTimer.cancel();
		findViewById(R.id.answer1).setEnabled(false);
		findViewById(R.id.answer2).setEnabled(false);
		findViewById(R.id.answer3).setEnabled(false);
		findViewById(R.id.answer4).setEnabled(false);
		String answer = "";
		switch (v.getId()) {
			case R.id.answer1:
				answer = "1";
				break;
			case R.id.answer2:
				answer = "2";
				break;
			case R.id.answer3:
				answer = "3";
				break;
			case R.id.answer4:
				answer = "4";
				break;
		}
		setAnswer(BegardObebarApplication.question.getId(), answer);
		
	}
	
	void setAnswer(String id, String answer) {
		AppService.getInstance().setAnswer(id, answer, result -> {
			if (result == null) {
				shortToastMessage("خطا در برقراری ارتباط با سرور");
			} else {
				shortToastMessage("جواب شما با موفقیت ثبت شد!");
				new Handler().postDelayed(this::finish, 2000);
			}
		});
		
	}
	
	@Override
	public void onBackPressed() {
		return;
	}
}
