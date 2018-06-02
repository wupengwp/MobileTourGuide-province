package com.jiagu.mobile.zhifu.adapter;

import java.util.ArrayList;

import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.utils.MyImageLoader;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.zhifu.bean.Indents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class IndentListViewAdapte extends BaseAdapter {
	private Context context;
	private ArrayList<Indents> data;
	private LayoutInflater inflater;

	public IndentListViewAdapte(Context context, ArrayList<Indents> data) {
		super();
		this.context = context;
		this.data = data;
		inflater = LayoutInflater.from(context);
	}

	public ArrayList<Indents> getData() {
		return data;
	}

	public void setData(ArrayList<Indents> data2) {
		this.data = data2;
	}

	public void addData(ArrayList<Indents> data) {
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
			convertView = inflater
					.inflate(R.layout.listview_dingdan_item, null);
			holder.name = (TextView) convertView
					.findViewById(R.id.liatview_item_dingdan_tv_01);
			holder.count = (TextView) convertView
					.findViewById(R.id.liatview_item_dingdan_tv_05);
			holder.type = (TextView) convertView
					.findViewById(R.id.liatview_item_dingdan_tv_03);
			holder.money = (TextView) convertView
					.findViewById(R.id.liatview_item_dingdan_tv_04);
			holder.image = (ImageView) convertView
					.findViewById(R.id.liatview_item_dingdan_iv_01);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Indents indent = data.get(position);
		if (indent.getAdmission()!=null) {
			if (indent.getAdmission().getTitle()!=null&&!indent.getAdmission().getTitle().equals("")) {
				holder.name.setText(indent.getAdmission().getTitle());
			}
			if (indent.getAdmission().getSoldNumber()!=null&&!indent.getAdmission().getSoldNumber().equals("")) {
				holder.count.setText("已售"+indent.getAdmission().getSoldNumber());
			}
			if (indent.getAdmission().getImageurl() != null) {
				if ("2".equals(indent.getAdmission().getCargoclassify().trim())) {
					MyImageLoader.loadImage(indent.getAdmission().getImageurl(), holder.image);
				}else {
					MyImageLoader.loadImage(Path.IMAGER_ADRESS
							+ indent.getAdmission().getImageurl(), holder.image);
				}
				
			}
		}
		if (indent.getStatus().equals("0")) {
			holder.type.setText("未支付");
		} else if (indent.getStatus().equals("1")) {
			if (indent.getIsenable().equals("0")) {
				holder.type.setText("未消费");
			} else {
				holder.type.setText("已消费");
			}
		} else if (indent.getStatus().equals("2")) {
			holder.type.setText("退款中");
			}
//		} else if (indent.getStatus().equals("3")) {
////			holder.type.setText("退款成功");
//		} else {
//			holder.type.setText("退款失败");
//		}
		holder.money.setText(indent.getCargoPayment()+"元");
		return convertView;
	}

	class ViewHolder {
		public TextView name, type, count, money;
		private ImageView image;
	}
}
