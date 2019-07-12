package com.jarvissoft.begardobebar.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.jarvissoft.begardobebar.R;
import com.jarvissoft.begardobebar.comunication.models.ScoreModel;
import com.jarvissoft.begardobebar.utils.AvatarManager;
import com.jarvissoft.begardobebar.utils.pref.SystemPrefs;
import com.squareup.picasso.Picasso;

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
			if (position % 2 == 0)
				setOddBackground(convertView);
			else
				setEvenBackground(convertView);
			TextView number = convertView.findViewById(R.id.txtNumber);
			TextView fullname = convertView.findViewById(R.id.txtFullName);
			TextView score = convertView.findViewById(R.id.txtScore);
			ImageView src = convertView.findViewById(R.id.profileImg);
			convertView.setMinimumHeight(150);
			if (scoreModel.getClientId().equals(SystemPrefs.getInstance().getMobileNumber())) {
				convertView.setBackgroundColor(Color.parseColor("#fba30b"));
				fullname.setTextColor(Color.parseColor("#27272e"));
				number.setTextColor(Color.parseColor("#27272e"));
				score.setTextColor(Color.parseColor("#27272e"));
			}
			number.setText(String.valueOf(position + 1));
			fullname.setText(scoreModel.getFullName().equals("") ? "بدون نام" : scoreModel.getFullName());
			score.setText("امتیاز:" + scoreModel.getAllScore());
			src.setImageBitmap(AvatarManager.getInstance().getAvatar(context, scoreModel.getProfileImageId()));
			
		}
		return convertView;
	}
	
	void setOddBackground(View convertView) {
		convertView.setBackgroundColor(Color.parseColor("#40000000"));
		TextView number = convertView.findViewById(R.id.txtNumber);
		TextView fullname = convertView.findViewById(R.id.txtFullName);
		TextView score = convertView.findViewById(R.id.txtScore);
		fullname.setTextColor(Color.parseColor("#dc8014"));
		number.setTextColor(Color.parseColor("#dc8014"));
		score.setTextColor(Color.parseColor("#dc8014"));
	}
	
	void setEvenBackground(View convertView) {
		convertView.setBackgroundColor(Color.parseColor("#16000000"));
		TextView number = convertView.findViewById(R.id.txtNumber);
		TextView fullname = convertView.findViewById(R.id.txtFullName);
		TextView score = convertView.findViewById(R.id.txtScore);
		fullname.setTextColor(Color.parseColor("#dc8014"));
		number.setTextColor(Color.parseColor("#dc8014"));
		score.setTextColor(Color.parseColor("#dc8014"));
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