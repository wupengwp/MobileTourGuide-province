package com.jiagu.mobile.zhifu.adapter;

import java.util.ArrayList;

import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.zhifu.bean.Pop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PopAdapter extends BaseAdapter{
	private ArrayList<Pop> data;
	private Context context;
	private LayoutInflater inflater;
	
	public PopAdapter(ArrayList<Pop> data, Context context) {
		super();
		this.data = data;
		this.context = context;
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
			convertView = inflater.inflate(R.layout.popview_item, null);
			holder.name = (TextView) convertView
					.findViewById(R.id.pop_listview_item_tv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Pop pop = data.get(position);
		holder.name.setText(pop.getCaption());
		return convertView;
	}
	class ViewHolder{
		public TextView name;
	}
}
