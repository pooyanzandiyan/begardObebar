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
import android.widget.ImageView;

import com.jarvissoft.begardobebar.R;
import com.jarvissoft.begardobebar.comunication.sms.AppService;
import com.jarvissoft.begardobebar.comunication.sms.models.ServiceCallback;
import com.jarvissoft.begardobebar.utils.AvatarManager;
import com.jarvissoft.begardobebar.utils.NetworkUtils;
import com.jarvissoft.begardobebar.utils.pref.SystemPrefs;

import static com.jarvissoft.begardobebar.G.imgId;

public class ChangeProfile extends MyBaseActivity {
	TextInputLayout txtInputLayout;
	public static int ChangeImageRequestCode=100;
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_profile);
		final EditText editText = findViewById(R.id.input_name);
		txtInputLayout = findViewById(R.id.mobileInputLayout);
		findViewById(R.id.setProfileImage).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				startActivityForResult(new Intent(ChangeProfile.this, avatarActivity.class), ChangeImageRequestCode);
			}
		});
		findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				hideKeyboard(ChangeProfile.this);
				findViewById(R.id.btn_save).setEnabled(false);
				if (editText.getText().toString().trim().length() > 1) {
					txtInputLayout.setError(null);
					if (NetworkUtils.isConnected(ChangeProfile.this)) {
						findViewById(R.id.lytLoading).setVisibility(View.VISIBLE);
						AppService.getInstance().ChangeProfile(SystemPrefs.getInstance().getMobileNumber(), editText.getText().toString(), new ServiceCallback<String>() {
							@Override
							public void callback(String result) {
								if (result != null)
									if (result.equals("ok")) {
										startActivity(new Intent(ChangeProfile.this, MainActivity.class));
										finish();
									} else
										txtInputLayout.setError("خطا در برقراری ارتباط با سرور");
							}
						});
					} else {
						txtInputLayout.setError("لطفا ابتدا به اینترنت متصل شوید");
					}
					
				} else {
					txtInputLayout.setError("لطفا نام و نام خانوادگی خود را وارد کنید");
				}
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
			if (requestCode == ChangeImageRequestCode) {
				ImageView imageView = findViewById(R.id.setProfileImage);
				imageView.setImageBitmap(AvatarManager.getInstance().getAvatar(this, imgId));
			}
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
