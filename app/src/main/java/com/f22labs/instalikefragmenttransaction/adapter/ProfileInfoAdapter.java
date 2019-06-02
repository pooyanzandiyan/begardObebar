package com.f22labs.instalikefragmenttransaction.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.f22labs.instalikefragmenttransaction.R;
import com.f22labs.instalikefragmenttransaction.comunication.sms.models.NewsModel;
import com.f22labs.instalikefragmenttransaction.comunication.sms.models.ProfileInfo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProfileInfoAdapter implements ListAdapter {
	List<ProfileInfo> arrayList;
	Context context;
	
	public ProfileInfoAdapter(Context context, List<ProfileInfo> arrayList) {
		this.arrayList = arrayList;
		this.context = context;
	}
	
	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}
	
	@Override
	public boolean isEnabled(int position) {
		return true;
	}
	
	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
	}
	
	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
	}
	
	@Override
	public int getCount() {
		return arrayList.size();
	}
	
	@Override
	public Object getItem(int position) {
		return position;
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public boolean hasStableIds() {
		return false;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ProfileInfo profileInfo = arrayList.get(position);
		if (convertView == null) {
			LayoutInflater layoutInflater = LayoutInflater.from(context);
			convertView = layoutInflater.inflate(R.layout.item_profile_info, null);
			convertView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
				}
			});
			TextView day = convertView.findViewById(R.id.profInfoDay);
			TextView trueQuestion = convertView.findViewById(R.id.profInfoTrueQuestion);
			TextView falseQuestion = convertView.findViewById(R.id.profInfoFalseQuestion);
			TextView score = convertView.findViewById(R.id.profInfoScore);
			day.setText(getDayName(profileInfo.getDay()));
			trueQuestion.setText("تعداد جواب درست:"+profileInfo.getTrueQuestion());
			falseQuestion.setText("تعداد جواب غلط:"+profileInfo.getFalseQuestion());
			score.setText("امتیاز:"+profileInfo.getScore());
		}
		return convertView;
	}
	String getDayName(String day){
		switch (day){
			case "1":
				return "روز اول";
			case "2":
				return "روز دوم";
			case "3":
				return "روز سوم";
			case "4":
				return "روز چهارم";
			case "5":
				return "روز پنجم";
				default:
					return "";
		}
	}
	
	@Override
	public int getItemViewType(int position) {
		return position;
	}
	
	@Override
	public int getViewTypeCount() {
		return arrayList.size();
	}
	
	@Override
	public boolean isEmpty() {
		return false;
	}
}