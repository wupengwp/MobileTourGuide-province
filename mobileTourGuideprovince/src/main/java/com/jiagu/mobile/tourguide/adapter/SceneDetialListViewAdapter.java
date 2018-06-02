package com.jiagu.mobile.tourguide.adapter;

import java.util.ArrayList;
import java.util.List;

import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.bean.Appraise;
import com.jiagu.mobile.tourguide.utils.MyImageLoader;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.tourguide.widget.CircleImageView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SceneDetialListViewAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Appraise> data;
	private LayoutInflater inflater;

	public SceneDetialListViewAdapter(Context context, ArrayList<Appraise> data) {
		super();
		this.context = context;
		this.data = data;
		inflater = LayoutInflater.from(context);
	}

	public List<Appraise> getData() {
		return data;
	}

	public void setData(ArrayList<Appraise> data2) {
		this.data = data2;
	}

	public void addData(ArrayList<Appraise> data) {
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
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(
					R.layout.activity_scenedetial_listview_item, null);
			holder.image = (CircleImageView) convertView
					.findViewById(R.id.scenedetial_listview_iv_01);
			holder.name = (TextView) convertView
					.findViewById(R.id.scenedetial_listview_tv_01);
			holder.shenfen = (TextView) convertView
					.findViewById(R.id.scenedetial_listview_tv_02);
			holder.time = (TextView) convertView
					.findViewById(R.id.scenedetial_listview_tv_03);
			holder.text = (TextView) convertView
					.findViewById(R.id.scenedetial_listview_tv_04);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Appraise appraise = data.get(position);
		holder.name.setText(appraise.getMyname());
		holder.shenfen.setText("游客");
		holder.time.setText(appraise.getTimer());
		holder.text.setText(appraise.getNote());
		if (appraise.getTourIcon() != null&&!appraise.getTourIcon().equals("")) {
			MyImageLoader.loadImage(
					Path.IMAGER_ADRESS + appraise.getTourIcon(), holder.image);
		}
		return convertView;
	}

	class ViewHolder {
		TextView name, shenfen, time, text;
		CircleImageView image;
	}
}
