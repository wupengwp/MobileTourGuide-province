package com.jiagu.mobile.zhifu.adapter;

import java.util.ArrayList;

import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.zhifu.bean.Site;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SiteAdapter extends BaseAdapter{
	private Context context;
	private ArrayList<Site> data;
	private LayoutInflater inflater;
	
	public SiteAdapter(Context context, ArrayList<Site> data) {
		super();
		this.context = context;
		this.data = data;
		inflater = LayoutInflater.from(context);
	}
	
	public void setData(ArrayList<Site> data) {
		this.data = data;
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
			convertView = inflater.inflate(R.layout.activity_mysite_listview_item, null);
			holder.name = (TextView) convertView
					.findViewById(R.id.orderfrom_tv_name);
			holder.mobilephone = (TextView) convertView
					.findViewById(R.id.orderfrom_tv_dianhua);
			holder.site = (TextView) convertView
					.findViewById(R.id.orderfrom_tv_dizhi);
			holder.radioButton = (ImageView) convertView
					.findViewById(R.id.radioButton1);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Site site = data.get(position);
		holder.name.setText(site.getUsername());
		holder.site.setText(site.getAddress());
		holder.mobilephone.setText(site.getMobilephone());
		if (site.getIsDefault().equals("1")) {
			holder.radioButton.setImageResource(R.drawable.shoucang);
		}else{
			holder.radioButton.setImageResource(R.drawable.shoucang1);
		}
		return convertView;
	}
	class ViewHolder {
		public TextView name,site,mobilephone;
		public ImageView radioButton;
	}
}
