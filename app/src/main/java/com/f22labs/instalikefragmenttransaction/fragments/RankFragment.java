package com.f22labs.instalikefragmenttransaction.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.f22labs.instalikefragmenttransaction.R;
import com.f22labs.instalikefragmenttransaction.activities.MainActivity;
import com.f22labs.instalikefragmenttransaction.adapter.ScoreAdapter;
import com.f22labs.instalikefragmenttransaction.comunication.sms.AppService;
import com.f22labs.instalikefragmenttransaction.comunication.sms.models.ScoreModel;
import com.f22labs.instalikefragmenttransaction.comunication.sms.models.ServiceCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RankFragment extends BaseFragment {
	
	
	@BindView(R.id.lstNews)
	ListView lst;
	
	int fragCount;
	
	
	public static RankFragment newInstance(int instance) {
		Bundle args = new Bundle();
		args.putInt(ARGS_INSTANCE, instance);
		RankFragment fragment = new RankFragment();
		fragment.setArguments(args);
		return fragment;
		
	}
	
	public RankFragment() {
		// Required empty public constructor
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_rank, container, false);
		ButterKnife.bind(this, view);
		checkToken();
		showLoading();
		AppService.getInstance().getScore(new ServiceCallback<List<ScoreModel>>() {
			@Override
			public void callback(List<ScoreModel> result) {
				cancelLoading();
				if (result != null) {
					if (result.size() > 0) {
						ScoreAdapter adapter = new ScoreAdapter(getActivity(), result);
						lst.setAdapter(adapter);
					}
				} else {
					ToastMessage("خطا در برقراری ارتباط با سرور");
				}
			}
		});
		
		
		return view;
	}
	
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		((MainActivity) getActivity()).updateToolbarTitle("رتبه");
		
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
}
