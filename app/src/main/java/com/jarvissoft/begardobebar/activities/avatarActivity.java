package com.jarvissoft.begardobebar.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.GridView;

import com.jarvissoft.begardobebar.G;
import com.jarvissoft.begardobebar.R;
import com.jarvissoft.begardobebar.adapter.AvatarAdapter;
import com.jarvissoft.begardobebar.comunication.sms.AppService;
import com.jarvissoft.begardobebar.comunication.sms.models.ServiceCallback;
import com.jarvissoft.begardobebar.utils.pref.SystemPrefs;

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
					AppService.getInstance().setAvatar(SystemPrefs.getInstance().getMobileNumber(), G.imgId, result -> {
						if(result!=null)
							if(!result.equals("")){
								finish();
								setResult(Activity.RESULT_OK);
								shortToastMessage("با موفقیت ثبت شد");
							}
					});
				}
				finish();
				break;
			
			
		}
		
	}
}
