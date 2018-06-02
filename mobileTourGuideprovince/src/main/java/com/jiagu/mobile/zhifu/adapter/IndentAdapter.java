package com.jiagu.mobile.zhifu.adapter;

import java.util.ArrayList;

import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.utils.MyImageLoader;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.zhifu.bean.Collect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class IndentAdapter extends BaseAdapter{
	private Context context;
	private ArrayList<Collect> data;
	private LayoutInflater inflater;
	
	public IndentAdapter(Context context, ArrayList<Collect> data) {
		super();
		this.context = context;
		this.data = data;
		inflater = LayoutInflater.from(context);
	}
	
	public ArrayList<Collect> getData() {
		return data;
	}

	public void setData(ArrayList<Collect> data2) {
		this.data = data2;
	}

	public void addData(ArrayList<Collect> data) {
		this.data.addAll(data);
		notifyDataSetChanged();
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
			convertView = inflater.inflate(R.layout.listview_dingdan_item, null);
			holder.name = (TextView) convertView
					.findViewById(R.id.liatview_item_dingdan_tv_01);
			holder.money = (TextView) convertView
					.findViewById(R.id.liatview_item_dingdan_tv_04);
			holder.type = (TextView) convertView
					.findViewById(R.id.liatview_item_dingdan_tv_03);
			holder.count = (TextView) convertView
					.findViewById(R.id.liatview_item_dingdan_tv_05);
			holder.image = (ImageView) convertView
					.findViewById(R.id.liatview_item_dingdan_iv_01);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Collect collect = data.get(position);
		holder.name.setText(collect.getTitle());
		holder.type.setText("");
		holder.count.setText("已售: "+collect.getSoldNumber());
		holder.money.setText("￥ "+collect.getPrice());
		if(collect.getUrl()!=null){
			switch (collect.getCargoclassify().trim()) {
			case "2":
				MyImageLoader.loadImage(collect.getUrl(), holder.image);
				
				break;
				
			default:
				MyImageLoader.loadImage(Path.IMAGER_ADRESS+collect.getUrl(), holder.image);
				break;
			}		
			
		}
		return convertView;
	}
	class ViewHolder {
		public TextView name,type,count,money;
		private ImageView image;
	}
}
