package com.jarvissoft.begardobebar.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.jarvissoft.begardobebar.R;
import com.jarvissoft.begardobebar.comunication.models.NewsModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter implements ListAdapter {
	List<NewsModel> arrayList;
	Context context;
	
	public NewsAdapter(Context context, List<NewsModel> arrayList) {
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
		NewsModel newsData = arrayList.get(position);
		if (convertView == null) {
			LayoutInflater layoutInflater = LayoutInflater.from(context);
			convertView = layoutInflater.inflate(R.layout.item_news, null);
			TextView tittle = convertView.findViewById(R.id.txtTitle);
			TextView text = convertView.findViewById(R.id.txtNewsText);
			ImageView imag = convertView.findViewById(R.id.imgNews);
			tittle.setText(newsData.getTitle());
			text.setText(newsData.getText());
			if (newsData.getImgUrl().length() > 1)
				Picasso.get()
						.load(newsData.getImgUrl())
						.into(imag);
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