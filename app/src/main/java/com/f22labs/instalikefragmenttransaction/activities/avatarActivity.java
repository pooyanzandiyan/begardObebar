package com.f22labs.instalikefragmenttransaction.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.GridView;

import com.f22labs.instalikefragmenttransaction.G;
import com.f22labs.instalikefragmenttransaction.R;
import com.f22labs.instalikefragmenttransaction.adapter.AvatarAdapter;
import com.f22labs.instalikefragmenttransaction.comunication.sms.AppService;
import com.f22labs.instalikefragmenttransaction.comunication.sms.models.ServiceCallback;
import com.f22labs.instalikefragmenttransaction.utils.pref.SystemPrefs;

public class avatarActivity extends MyBaseActivity {
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.avatar_activity);
		AvatarAdapter avatarAdapter = new AvatarAdapter(this,G.view);
		GridView gridView = findViewById(R.id.grdAvatar);
		gridView.setAdapter(avatarAdapter);
		
	}
	
	public void checkBtns(View v) {
		switch (v.getId()) {
			case R.id.btnAvatarCancel:
				finish();
				break;
			case R.id.btnAvatarSave:
				if (!G.imgId.equals("")) {
					AppService.getInstance().setAvatar(SystemPrefs.getInstance().getMobileNumber(), G.imgId, new ServiceCallback<String>() {
						@Override
						public void callback(String result) {
							if(result!=null)
								if(!result.equals("")){
									finish();
									shortToastMessage("با موفقیت ثبت شد");
								}
						}
					});
				}
				finish();
				break;
			
			
		}
		
	}
}
