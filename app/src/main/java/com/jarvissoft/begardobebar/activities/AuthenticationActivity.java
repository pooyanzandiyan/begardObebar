package com.jarvissoft.begardobebar.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.jarvissoft.begardobebar.R;
import com.jarvissoft.begardobebar.comunication.sms.SmsService;
import com.jarvissoft.begardobebar.comunication.sms.models.SendVerificationModel;
import com.jarvissoft.begardobebar.comunication.sms.models.ServiceCallback;
import com.jarvissoft.begardobebar.utils.NetworkUtils;
import com.jarvissoft.begardobebar.utils.pref.SystemPrefs;

public class AuthenticationActivity extends MyBaseActivity {
	TextInputLayout txtInputLayout;
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		final EditText editText = findViewById(R.id.input_name);
		txtInputLayout = findViewById(R.id.mobileInputLayout);
		findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				hideKeyboard(AuthenticationActivity.this);
				findViewById(R.id.btn_save).setEnabled(false);
				
				if (editText.getText().toString().trim().length() > 1) {
					if (isValidPhoneNumber(editText.getText().toString())) {
						txtInputLayout.setError(null);
						if (NetworkUtils.isConnected(AuthenticationActivity.this)) {
							findViewById(R.id.lytLoading).setVisibility(View.VISIBLE);
							SmsService.getInstance().SendVerification(editText.getText().toString(), new ServiceCallback<SendVerificationModel>() {
								@Override
								public void callback(SendVerificationModel result) {
									
									findViewById(R.id.btn_save).setEnabled(true);
									findViewById(R.id.lytLoading).setVisibility(View.GONE);
									if (result != null)
										if(result.getStatus()){
											SystemPrefs.getInstance().setMobileNumber(editText.getText().toString().trim());
											startActivity(new Intent(AuthenticationActivity.this, ConfirmAuthenticationActivity.class));
											finish();
										}
									 else
										txtInputLayout.setError("خطا در برقراری ارتباط با سرور");
								}
							});
						} else {
							txtInputLayout.setError("لطفا ابتدا به اینترنت متصل شوید");
						}
					} else {
						txtInputLayout.setError("لطفا شماره تلفن خود را به درستی وارد کنید");
					}
				} else {
					txtInputLayout.setError("لطفا شماره تلفن خود را وارد کنید");
				}
			}
		});
	}
	
	private boolean isValidPhoneNumber(String mobileNumber) {
		String regEx = "^09[0-9]{9}$";
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
