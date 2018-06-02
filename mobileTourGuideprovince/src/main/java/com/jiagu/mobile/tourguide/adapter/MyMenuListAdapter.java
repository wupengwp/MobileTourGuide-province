package com.jiagu.mobile.tourguide.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.jiagu.mobile.tourguide.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyMenuListAdapter extends BaseAdapter {
	
	private ArrayList<HashMap<String, Object>> data;
	private LayoutInflater inflater;
	private int r;
	public MyMenuListAdapter(Context context,ArrayList<HashMap<String, Object>> data,int r) {
		super();
		this.data = data;
		this.r = r;
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

	@SuppressLint("InflateParams") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(r, null);
			holder.text = (TextView) convertView
					.findViewById(R.id.menu_item_text);
			holder.iamger = (ImageView) convertView
					.findViewById(R.id.menu_item_imager);
			if((data.get(position).get("text")).equals("版本查询")){
				TextView text = (TextView) convertView.findViewById(R.id.menu_item_text_02);
				text.setText(""+data.get(position).get("versionnumber"));
				text.setVisibility(View.VISIBLE);
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		HashMap<String,Object> hashMap = data.get(position);
		Object object = hashMap.get("text");
		holder.text.setText((String)object);
		Object imager = hashMap.get("imager");
		int imagerId = (Integer)imager;
		holder.iamger.setImageResource(imagerId);
		
		return convertView;
	}

	class ViewHolder {
		public TextView text;
		public ImageView iamger;
	}

}
