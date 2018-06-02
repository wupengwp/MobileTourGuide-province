package com.jiagu.mobile.tourguide.adapter;

import java.util.ArrayList;

import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.bean.ScenicSpots;
import com.jiagu.mobile.tourguide.utils.MyImageLoader;
import com.jiagu.mobile.tourguide.utils.Path;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ScenicSpotsAdapter extends BaseAdapter{
	private Context context;
	private ArrayList<ScenicSpots> data;
	private LayoutInflater inflater;
	public ScenicSpotsAdapter(Context context, ArrayList<ScenicSpots> data) {
		super();
		this.context = context;
		this.data = data;
		inflater = LayoutInflater.from(context);
	}
	public ArrayList<ScenicSpots> getData() {
		return data;
	}
	public void setData(ArrayList<ScenicSpots> data) {
		this.data = data;
	}
	public void addData() {
		this.data.addAll(data);
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.gridview_item_scenicspots, null);
			holder.name = (TextView) convertView
					.findViewById(R.id.scenespots_item_text_01);
			holder.image = (ImageView) convertView.findViewById(R.id.scenespots_item_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ScenicSpots scenicSpots = data.get(position);
		holder.name.setText(scenicSpots.getTitle());
		if(!(scenicSpots.getImageurl()==null)){
			MyImageLoader.loadImage(Path.IMAGER_ADRESS+scenicSpots.getImageurl(), holder.image);
		}
		return convertView;
	}
	class ViewHolder{
		TextView name;
		ImageView image;
	}
}
