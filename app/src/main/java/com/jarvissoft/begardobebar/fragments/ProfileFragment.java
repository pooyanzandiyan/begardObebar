package com.jarvissoft.begardobebar.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jarvissoft.begardobebar.G;
import com.jarvissoft.begardobebar.R;
import com.jarvissoft.begardobebar.activities.MainActivity;
import com.jarvissoft.begardobebar.activities.avatarActivity;
import com.jarvissoft.begardobebar.adapter.ProfileInfoAdapter;
import com.jarvissoft.begardobebar.comunication.sms.AppService;
import com.jarvissoft.begardobebar.comunication.sms.models.ProfileInfo;
import com.jarvissoft.begardobebar.comunication.sms.models.ProfileModel;
import com.jarvissoft.begardobebar.comunication.sms.models.ScoreModel;
import com.jarvissoft.begardobebar.comunication.sms.models.ServiceCallback;
import com.jarvissoft.begardobebar.utils.AvatarManager;
import com.jarvissoft.begardobebar.utils.pref.SystemPrefs;

import java.util.List;

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
		profileImg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				G.view=view;
				startActivity(new Intent(getActivity(), avatarActivity.class));
			}
		});
		return view; }
	
	String checkNullOrEmpty(String text) {
		if(text==null)
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
		if(item.getItemId()==R.id.refresh){
			getData();
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void getData() {
		showLoading();
		AppService.getInstance().getScore(new ServiceCallback<List<ScoreModel>>() {
			@Override
			public void callback(List<ScoreModel> result) {
				AppService.getInstance().getProfile(SystemPrefs.getInstance().getMobileNumber(), new ServiceCallback<ProfileModel>() {
					@Override
					public void callback(ProfileModel result) {
						if (result != null) {
							TextView fullName = view.findViewById(R.id.profFullName);
							TextView allTrue = view.findViewById(R.id.profAllTrue);
							TextView allFalse = view.findViewById(R.id.profAllFalse);
							TextView rank = view.findViewById(R.id.profRank);
							fullName.setText(result.getFullName());
							allTrue.setText(checkNullOrEmpty(result.getAllTrueQuestion()));
							allFalse.setText(checkNullOrEmpty(result.getAllFalseQuestion()));
							rank.setText(checkNullOrEmpty(result.getRank()).equals("0")?"نامعلوم":result.getRank());
							ImageView avatarImg = view.findViewById(R.id.profileImg);
							avatarImg.setImageBitmap(AvatarManager.getInstance().getAvatar(getActivity(),result.getProfileImageId()));
							
						}
					}
				});
			}
		});
		
		
		AppService.getInstance().getProfileInfo(SystemPrefs.getInstance().getMobileNumber(), new ServiceCallback<List<ProfileInfo>>() {
			@Override
			public void callback(List<ProfileInfo> result) {
				cancelLoading();
				if (result != null) {
					if (result.size() > 0) {
						ProfileInfoAdapter profileInfoAdapter = new ProfileInfoAdapter(getActivity(), result);
						lst.setAdapter(profileInfoAdapter);
					}
				} else {
					ToastMessage("خطا در برقراری ارتباط با سرور");
				}
			}
		});
	}
}
