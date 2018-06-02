package com.jiagu.mobile.tourguide.adapter;

import java.util.ArrayList;

import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.bean.Area;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TitlePOPAdapter extends BaseAdapter{
	private Context context; 
	private ArrayList<Area> data;
	private LayoutInflater inflater;
	
	public TitlePOPAdapter(Context context, ArrayList<Area> data) {
		super();
		this.context = context;
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
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.titlepop, null);
			holder.name = (TextView) convertView.findViewById(R.id.titlepop_textview);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Area area = data.get(position);
		if (area.getTitle()!=null&&!area.getTitle().equals("")) {
			holder.name.setText(area.getTitle().toString());
		}
		return convertView;
	}
	class ViewHolder {
		public TextView name;
	}
}
