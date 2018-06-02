package com.jiagu.mobile.zhifu.adapter;

import java.util.ArrayList;

import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.utils.MyImageLoader;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.zhifu.bean.Admission;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MerchatAdapter extends BaseAdapter{
	private Context context;
	private ArrayList<Admission> data;
	private LayoutInflater inflater;
	public MerchatAdapter(Context context, ArrayList<Admission> data) {
		super();
		this.context = context;
		this.data = data;
		inflater = LayoutInflater.from(context);
	}
	
	public ArrayList<Admission> getData() {
		return data;
	}

	public void setData(ArrayList<Admission> data) {
		this.data = data;
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
			convertView = inflater.inflate(R.layout.activity_merchant_listview_item, null);
			holder.name = (TextView) convertView
					.findViewById(R.id.activity_merchant_listview_item_tv01);
			holder.money = (TextView) convertView
					.findViewById(R.id.activity_merchant_listview_item_tv02);
			holder.moneyed = (TextView) convertView
					.findViewById(R.id.activity_merchant_listview_item_tv03);
			holder.count = (TextView) convertView
					.findViewById(R.id.activity_merchant_listview_item_tv04);
			holder.image = (ImageView) convertView
					.findViewById(R.id.activity_merchant_listview_item_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Admission admission = data.get(position);
		holder.name.setText(admission.getTitle());
		//显示第三方原价
		if ("2".equals(admission.getCargoclassify().trim())) {
			if ("".equals(admission.getZwyoldprice())||admission.getZwyoldprice()==null) {
				holder.moneyed.setText("¥0");
			}else {
			holder.moneyed.setText("¥"+admission.getZwyoldprice());
			holder.moneyed.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);// 加横线
			}
		}else {
			holder.moneyed.setText("¥"+admission.getOldPrice());
			holder.moneyed.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);// 加横线
		}
		
		holder.count.setText("已售: "+admission.getSoldNumber());
		holder.money.setText("¥ "+admission.getPrice());
		if(admission.getImageurl()!=null){
			switch (admission.getCargoclassify().trim()) {
			case "2":
				MyImageLoader.loadImage(admission.getImageurl(), holder.image);
				break;
				
			default:
				MyImageLoader.loadImage(Path.IMAGER_ADRESS+admission.getImageurl(), holder.image);
				break;
			}	
			
		}
		return convertView;
	}
	class ViewHolder {
		public TextView name,moneyed,count,money;
		private ImageView image;
	}
}
