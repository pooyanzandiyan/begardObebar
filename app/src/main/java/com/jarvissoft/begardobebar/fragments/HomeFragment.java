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
import com.jarvissoft.begardobebar.adapter.NewsAdapter;
import com.jarvissoft.begardobebar.comunication.sms.app.AppService;

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
		
		getData();
		
		
		return view;
	}
	
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		((MainActivity) getActivity()).updateToolbarTitle( "اخبار" );
		
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
		AppService.getInstance().getNews(result -> {
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
		});
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
		inflater.inflate(R.menu.menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
}
