package com.jarvissoft.begardobebar.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.jarvissoft.begardobebar.R;
import com.jarvissoft.begardobebar.comunication.app.AppService;
import com.jarvissoft.begardobebar.comunication.sms.SmsService;
import com.jarvissoft.begardobebar.utils.NetworkUtils;
import com.jarvissoft.begardobebar.utils.pref.SystemPrefs;

public class ConfirmAuthenticationActivity extends MyBaseActivity {
	EditText edtPass;
	TextInputLayout textInputLayout;
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sms);
		textInputLayout=findViewById(R.id.inputLayout);
		edtPass=findViewById(R.id.input_pass);
		edtPass.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			
			}
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s.length()>=4)
					login();
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			
			}
		});
		findViewById(R.id.btn_save).setOnClickListener(v -> login());
	}
	void login(){

		hideKeyboard(ConfirmAuthenticationActivity.this);
		findViewById(R.id.btn_save).setEnabled(false);
		String pass=edtPass.getText().toString().trim();
		if(pass.length()>1){
			if(isValidPass(pass)){
				if (NetworkUtils.isConnected(ConfirmAuthenticationActivity.this)) {
					findViewById(R.id.lytLoading).setVisibility(View.VISIBLE);
					SmsService.getInstance().ConfirmVerification(SystemPrefs.getInstance().getMobileNumber(), pass, result -> {
						findViewById(R.id.btn_save).setEnabled(true);
						findViewById(R.id.lytLoading).setVisibility(View.GONE);
						if (result != null)
							if(result.getStatus()){
								SystemPrefs.getInstance().setToken(result.getToken());
								SystemPrefs.getInstance().setLogedIn(true);
								
								AppService.getInstance().getProfile(SystemPrefs.getInstance().getMobileNumber(), result1 -> {
									if (result1 != null) {
										if(result1.getFullName().length()<1){
											startActivity(new Intent(ConfirmAuthenticationActivity.this, ChangeProfile.class));
										}else{
											startActivity(new Intent(ConfirmAuthenticationActivity.this, MainActivity.class));
										}
										finish();
									}
								});
								
							}
							else
								textInputLayout.setError(result.getMsg());
						
					});
				}else{
					textInputLayout.setError("لطفا ابتدا به اینترنت متصل شوید");
				}
			}else{
				textInputLayout.setError("کد وارد شده باید به صورت اعداد لاتین و 4 رقمی باشد");
			}
		}
		else {
			textInputLayout.setError("لطفا رمز عبور یکبار مصرف خود را وارد کنید");
		}
	}
	private boolean isValidPass(String mobileNumber) {
		String regEx = "^[0-9]{4}$";
		return mobileNumber.matches(regEx);
	}
	public static void hideKeyboard(AppCompatActivity activity) {
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
		//Find the currently focused view, so we can grab the correct window token from it.
		View view = activity.getCurrentFocus();
		//If no view currently has focus, create a new one, just so we can grab a window token from it
		if (view == null) {
			view = new View(activity);
		}
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
}
