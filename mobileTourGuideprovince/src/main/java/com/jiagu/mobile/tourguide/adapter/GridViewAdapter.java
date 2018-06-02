package com.jiagu.mobile.tourguide.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.jiagu.mobile.tourguide.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridViewAdapter extends BaseAdapter{

	private ArrayList<HashMap<String, Object>> data;
	private LayoutInflater inflater;
	public GridViewAdapter(Context context,ArrayList<HashMap<String, Object>> data) {
		super();
		this.data = data;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
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
			convertView = inflater.inflate(R.layout.buy_grindview_item, null);
			holder.name = (TextView) convertView
					.findViewById(R.id.buy_item_name);
			holder.image = (ImageView) convertView
					.findViewById(R.id.buy_item_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		HashMap<String,Object> hashMap = data.get(position);
		Object object = hashMap.get("name");
		holder.name.setText((String)object);
		Object imager = hashMap.get("imager");
		int imagerId = (Integer)imager;
		holder.image.setImageResource(imagerId);
		return convertView;
	}
	class ViewHolder {
		public TextView name,money;
		public ImageView image;
	}
}
