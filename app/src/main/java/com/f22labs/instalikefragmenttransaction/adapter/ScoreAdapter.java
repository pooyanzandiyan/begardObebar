package com.f22labs.instalikefragmenttransaction.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.f22labs.instalikefragmenttransaction.R;
import com.f22labs.instalikefragmenttransaction.comunication.sms.models.ProfileInfo;
import com.f22labs.instalikefragmenttransaction.comunication.sms.models.ScoreModel;
import com.f22labs.instalikefragmenttransaction.utils.pref.SystemPrefs;

import java.util.List;

public class ScoreAdapter implements ListAdapter {
	List<ScoreModel> arrayList;
	Context context;
	
	public ScoreAdapter(Context context, List<ScoreModel> arrayList) {
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
		ScoreModel scoreModel = arrayList.get(position);
		if (convertView == null) {
			LayoutInflater layoutInflater = LayoutInflater.from(context);
			convertView = layoutInflater.inflate(R.layout.item_rank, null);
			convertView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
				}
			});
			TextView number = convertView.findViewById(R.id.txtNumber);
			TextView fullname = convertView.findViewById(R.id.txtFullName);
			TextView score = convertView.findViewById(R.id.txtScore);
			convertView.setMinimumHeight(150);
			if(scoreModel.getClientId().equals(SystemPrefs.getInstance().getMobileNumber())){
				convertView.setBackgroundColor(Color.parseColor("#ffB0BEF7"));
			}
			number.setText(String.valueOf(position+1));
			fullname.setText(scoreModel.getFullName().equals("")?"بدون نام":scoreModel.getFullName());
			score.setText("امتیاز:"+scoreModel.getAllScore());

		}
		return convertView;
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