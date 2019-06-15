package com.jarvissoft.begardobebar.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jarvissoft.begardobebar.R;
import com.jarvissoft.begardobebar.activities.MainActivity;
import com.jarvissoft.begardobebar.adapter.ScoreAdapter;
import com.jarvissoft.begardobebar.comunication.app.AppService;

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
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_rank, container, false);
		ButterKnife.bind(this, view);
		checkToken();
		
		getData();
		
		
		return view;
	}
	
	private void getData() {
		showLoading();
		AppService.getInstance().getScore(result -> {
			cancelLoading();
			if (result != null) {
				if (result.size() > 0) {
					ScoreAdapter adapter = new ScoreAdapter(getActivity(), result);
					lst.setAdapter(adapter);
				}else
					ToastMessage("تا کنون رتبه ای ثبت نشده است");
			} else {
				ToastMessage("خطا در برقراری ارتباط با سرور");
			}
		});
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
