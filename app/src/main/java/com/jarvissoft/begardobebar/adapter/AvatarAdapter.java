package com.jarvissoft.begardobebar.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RadioButton;

import com.jarvissoft.begardobebar.BegardObebarApplication;
import com.jarvissoft.begardobebar.R;
import com.jarvissoft.begardobebar.utils.AvatarManager;

import java.util.ArrayList;
import java.util.List;

public class AvatarAdapter implements ListAdapter {
	List<Integer> arrayList;
	Context context;
	View view;
	
	public AvatarAdapter(Context context, View view) {
		this.arrayList = new ArrayList<>();
		for (int i = 1; i < 16; i++)
			arrayList.add(i);
		this.context = context;
		this.view = view;
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
		View ConvertView=convertView;
		final Integer imgId = arrayList.get(position);
		if (convertView == null) {
			LayoutInflater layoutInflater = LayoutInflater.from(context);
			ConvertView = layoutInflater.inflate(R.layout.avatar_item, null);
			RadioButton radioButton= ConvertView.findViewById(R.id.rdoItem);
			BegardObebarApplication.radioButtons.add(radioButton);
			ConvertView.setOnClickListener(v -> {
				BegardObebarApplication.imgId = String.valueOf(imgId);
				if (view!=null) {
					ImageView avatar = view.findViewById(R.id.profileImg);
					avatar.setImageBitmap(AvatarManager.getInstance().getAvatar(context, imgId));
					resetRadioButton();
					radioButton.setChecked(true);
				}
				
			});
			ImageView avatarImg = ConvertView.findViewById(R.id.imgAvatar);
			avatarImg.setImageBitmap(AvatarManager.getInstance().getAvatar(context, imgId));
			
		}
		return ConvertView;
	}
	
	void resetRadioButton(){
		for (RadioButton item:BegardObebarApplication.radioButtons
		     ) {
			item.setChecked(false);
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