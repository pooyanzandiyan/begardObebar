package com.jarvissoft.begardobebar.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jarvissoft.begardobebar.BegardObebarApplication;
import com.jarvissoft.begardobebar.R;
import com.jarvissoft.begardobebar.activities.ChangeProfile;
import com.jarvissoft.begardobebar.activities.MainActivity;
import com.jarvissoft.begardobebar.activities.AvatarActivity;
import com.jarvissoft.begardobebar.adapter.ProfileInfoAdapter;
import com.jarvissoft.begardobebar.comunication.app.AppService;
import com.jarvissoft.begardobebar.utils.AvatarManager;
import com.jarvissoft.begardobebar.utils.pref.SystemPrefs;
import com.jarvissoft.qrcodescanner.QrCodeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProfileFragment extends BaseFragment {
	View view;
	@BindView(R.id.lstProfileInfo)
	ListView lst;
	@BindView(R.id.profileImg)
	ImageView profileImg;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_profile, container, false);
		ButterKnife.bind(this, view);
		((MainActivity) getActivity()).updateToolbarTitle("پروفایل");
		checkToken();
		
		getData();
		
		profileImg.setOnClickListener(v -> {
			BegardObebarApplication.view = view;
			startActivityForResult(new Intent(getActivity(), AvatarActivity.class), 0);
			
			
		});
		view.findViewById(R.id.profFullName).setOnClickListener(v -> startActivity(new Intent(getActivity(), ChangeProfile.class)));
		return view;
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (!SystemPrefs.getInstance().isShownOnce(145))
			showFirstRuntimeHelp(profileImg, "عکس پروفایلتو عوض کن",
					"روی این تصویر بزن و یه اواتاری انتخاب کن تا در قسمت برترین ها اواتارتو دوستات ببینن",
					145);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK)
			if (requestCode == 0)
				new Handler().postDelayed(() -> ToastMessage("با موفقیت ثبت شد"), 500);
		
		
	}
	
	String checkNullOrEmpty(String text) {
		if (text == null)
			return "نامعلوم";
		
		if (text.length() < 1)
			return "نامعلوم";
		
		return text;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.refresh) {
			getData();
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void getData() {
		showLoading();
		AppService.getInstance().getScore(result -> AppService.getInstance().getProfile(SystemPrefs.getInstance().getMobileNumber(), result1 -> {
			if (result1 != null) {
				TextView fullName = view.findViewById(R.id.profFullName);
				TextView allTrue = view.findViewById(R.id.profAllTrue);
				TextView allFalse = view.findViewById(R.id.profAllFalse);
				TextView rank = view.findViewById(R.id.profRank);
				fullName.setText(result1.getFullName());
				allTrue.setText(checkNullOrEmpty(result1.getAllTrueQuestion()));
				allFalse.setText(checkNullOrEmpty(result1.getAllFalseQuestion()));
				rank.setText(checkNullOrEmpty(result1.getRank()).equals("0") ? "نامعلوم" : result1.getRank());
				ImageView avatarImg = view.findViewById(R.id.profileImg);
				avatarImg.setImageBitmap(AvatarManager.getInstance().getAvatar(getActivity(), result1.getProfileImageId()));
				
			}
			AppService.getInstance().getProfileInfo(SystemPrefs.getInstance().getMobileNumber(), r -> {
				cancelLoading();
				if (r != null) {
					if (r.size() > 0) {
						ProfileInfoAdapter profileInfoAdapter = new ProfileInfoAdapter(getActivity(), r);
						lst.setAdapter(profileInfoAdapter);
					}
				} else {
					ToastMessage("خطا در برقراری ارتباط با سرور");
				}
			});
		}));
	}
}
