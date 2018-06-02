package com.jiagu.mobile.zhifu.adapter;

import java.util.ArrayList;

import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.utils.MyImageLoader;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.zhifu.bean.GroupPurchase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GroupPurchaseAdapter extends BaseAdapter{
	private ArrayList<GroupPurchase> data;
	private Context context;
	private LayoutInflater inflater;
	public GroupPurchaseAdapter(ArrayList<GroupPurchase> data, Context context) {
		super();
		this.data = data;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}
	public ArrayList<GroupPurchase> getData() {
		return data;
	}

	public void setData(ArrayList<GroupPurchase> data2) {
		this.data = data2;
	}

	public void addData(ArrayList<GroupPurchase> data) {
		this.data.addAll(data);
		notifyDataSetChanged();
	}
	@Override
	public int getCount(){
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
			convertView = inflater.inflate(R.layout.gridview_item_grouppurchase, null);
			holder.name = (TextView) convertView
					.findViewById(R.id.activity_grouppurchase_gridview_item_textview_02);
			holder.money = (TextView) convertView
					.findViewById(R.id.activity_grouppurchase_gridview_item_textview_01);
			holder.image = (ImageView) convertView
					.findViewById(R.id.activity_grouppurchase_gridview_item_imageview);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		GroupPurchase groupPurchase = data.get(position);
		holder.name.setText(groupPurchase.getTitle());
		holder.money.setText("¥"+groupPurchase.getPrice());
		String imgurl =	groupPurchase.getImageurl();

		if(imgurl!=null){
			switch (groupPurchase.getCargoclassify().trim()) {
			case "2":
				MyImageLoader.loadImage(imgurl, holder.image);
				
				break;
				
			default:
				MyImageLoader.loadImage(Path.IMAGER_ADRESS+imgurl, holder.image);
				break;
			}					
		}
		return convertView;
	}
	class ViewHolder {
		public TextView name,money;
		public ImageView image;
	}
}
