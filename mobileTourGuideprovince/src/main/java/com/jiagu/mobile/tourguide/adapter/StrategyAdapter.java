package com.jiagu.mobile.tourguide.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.bean.Strategy;
import com.jiagu.mobile.tourguide.utils.MyImageLoader;
import com.jiagu.mobile.tourguide.utils.Path;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @author Administrator Adapate旅游攻略的适配器 	高磊 	2015-01-04
 */
public class StrategyAdapter extends BaseAdapter {
	private ArrayList<Strategy> data;
	private Context context;
	private LayoutInflater inflater;
	private HashMap<Integer, View> map;

	public StrategyAdapter(ArrayList<Strategy> data, Context context) {
		super();
		this.data = data;
		this.context = context;
		inflater = LayoutInflater.from(context);
		map = new HashMap<Integer, View>();
	}

	public List<Strategy> getData() {
		return data;
	}

	public void setData(ArrayList<Strategy> data2) {
		this.data = data2;
	}

	public void addData(ArrayList<Strategy> data) {
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
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		MyViewHolder holder = null;
		if (map.get(position) == null) {
			holder = new MyViewHolder();
			view = inflater.inflate(R.layout.strategy_listview_item, null);
			holder.name = (TextView) view
					.findViewById(R.id.strategy_listview_iten_tv_01);
			holder.time = (TextView) view
					.findViewById(R.id.strategy_listview_iten_tv_02);
			holder.text = (TextView) view
					.findViewById(R.id.strategy_listview_iten_tv_03);
			holder.iv01 = (ImageView) view
					.findViewById(R.id.strategy_listview_iten_iv_01);
			view.setTag(holder);
			map.put(position, view);
		} else {
			view = map.get(position);
			holder = (MyViewHolder) view.getTag();
		}
		Strategy strategy = data.get(position);
		holder.name.setText(strategy.getTitle());
		holder.time.setText(strategy.getTimer());
		holder.text.setText(strategy.getText());
		if(data.get(position).getUrl()==null||data.get(position).getUrl().equals("")){
			holder.iv01.setVisibility(View.GONE);
		}else{
			MyImageLoader.loadImage(Path.IMAGER_ADRESS+ data.get(position).getUrl(),holder.iv01);
		}
		return view;
	}

	class MyViewHolder {
		TextView name, time, text;
		ImageView iv01;
	}

}