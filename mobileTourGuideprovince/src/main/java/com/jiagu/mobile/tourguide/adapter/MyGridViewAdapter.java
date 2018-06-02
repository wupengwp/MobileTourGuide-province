package com.jiagu.mobile.tourguide.adapter;

import java.util.ArrayList;
import java.util.List;

import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.bean.Records;
import com.jiagu.mobile.tourguide.utils.MyImageLoader;
import com.jiagu.mobile.tourguide.utils.Path;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyGridViewAdapter extends BaseAdapter{
	private ArrayList<Records> data;
	private LayoutInflater inflater;
	private Context context;
	
	public MyGridViewAdapter(ArrayList<Records> data, Context context) {
		super();
		this.data = data;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}
	public List<Records> getData() {
		return data;
	}

	public void setData(ArrayList<Records> data2) {
		this.data = data2;
	}

	public void addData(ArrayList<Records> data) {
		this.data.addAll(data);
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.gridvoid_feature, null);
			holder.name = (TextView) convertView
					.findViewById(R.id.feature_gridview_text_name);
			holder.data = (TextView) convertView
					.findViewById(R.id.feature_gridview_text_data);
			holder.image = (ImageView) convertView.findViewById(R.id.feature_gridview_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Records records = data.get(position);
		if(!(records.getTitle()==null||records.getTitle().equals(""))){
			holder.name.setText(records.getTitle());
		}else{
			holder.name.setText(records.getName());
		}
		holder.data.setText(records.getText());
		if(!(records.getImageurl()==null)){
			MyImageLoader.loadImage(Path.IMAGER_ADRESS+records.getImageurl(), holder.image);
		}
		if (!(records.getUrl()==null)){
			MyImageLoader.loadImage(Path.IMAGER_ADRESS+records.getUrl(), holder.image);
		}
		return convertView;
	}
	class ViewHolder {
		public TextView name,data;
		public ImageView image;
	}
}
