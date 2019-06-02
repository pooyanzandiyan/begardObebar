package com.f22labs.instalikefragmenttransaction.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.f22labs.instalikefragmenttransaction.R;
import com.f22labs.instalikefragmenttransaction.activities.MainActivity;
import com.f22labs.instalikefragmenttransaction.adapter.NewsAdapter;
import com.f22labs.instalikefragmenttransaction.comunication.sms.AppService;
import com.f22labs.instalikefragmenttransaction.comunication.sms.models.NewsModel;
import com.f22labs.instalikefragmenttransaction.comunication.sms.models.ServiceCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeFragment extends BaseFragment {
	
	
	@BindView(R.id.lstNews)
	ListView lst;
	
	int fragCount;
	
	
	public static HomeFragment newInstance(int instance) {
		Bundle args = new Bundle();
		args.putInt(ARGS_INSTANCE, instance);
		HomeFragment fragment = new HomeFragment();
		fragment.setArguments(args);
		return fragment;
		
	}
	
	public HomeFragment() {
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
		// Inflate the layout for this fragment
		
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		ButterKnife.bind(this, view);
		checkToken();
		showLoading();
		AppService.getInstance().getNews(new ServiceCallback<List<NewsModel>>() {
			@Override
			public void callback(List<NewsModel> result) {
				cancelLoading();
				if (result != null) {
					if (result.size() >= 1) {
						NewsAdapter adapter = new NewsAdapter(getActivity(), result);
						lst.setAdapter(adapter);
					} else {
						ToastMessage("در حال حاضر خبری موجود نیست");
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
		
		((MainActivity) getActivity()).updateToolbarTitle((fragCount == 0) ? "خانه" : "Sub Home " + fragCount);
		
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
}
