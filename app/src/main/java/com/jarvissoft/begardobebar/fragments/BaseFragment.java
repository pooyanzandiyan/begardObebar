package com.jarvissoft.begardobebar.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.jarvissoft.begardobebar.activities.AuthenticationActivity;
import com.jarvissoft.begardobebar.comunication.sms.AppService;
import com.jarvissoft.begardobebar.comunication.sms.models.ServiceCallback;
import com.jarvissoft.begardobebar.utils.pref.SystemPrefs;

/**
 * Created by jarvissoft on 07/03/17.
 */

public class BaseFragment extends Fragment {
	
	public static final String ARGS_INSTANCE = "com.jarvissoft.begardobebar";
	ProgressDialog progressDialog;
	
	FragmentNavigation mFragmentNavigation;
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	void showLoading() {
		progressDialog = ProgressDialog.show(getActivity(), "",
				"در حال بارگذاری...", true);
		progressDialog.setCancelable(false);
		progressDialog.show();
	}
	
	void cancelLoading() {
		if (progressDialog != null)
			progressDialog.dismiss();
	}
	
	void checkToken() {
		AppService.getInstance().checkToken(result -> {
			if (result != null) {
				if (result.equals("nok")) {
					SystemPrefs.getInstance().setLogedIn(false);
					startActivity(new Intent(getActivity(), AuthenticationActivity.class));
					ToastMessage("توکن شما منقضی شده است. لطفا دوباره وارد شوید");
					getActivity().finish();
				}
			}
		});
	}
	
	protected void ToastMessage(String text) {
		Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
	}
	
	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof FragmentNavigation) {
			mFragmentNavigation = (FragmentNavigation) context;
		}
	}
	
	public interface FragmentNavigation {
		void pushFragment(Fragment fragment);
	}
	
	
}
