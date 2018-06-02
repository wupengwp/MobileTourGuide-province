package com.jiagu.mobile.tourguide.adapter;

import java.util.ArrayList;

import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.activities.SceneDetialActivity;
import com.jiagu.mobile.tourguide.bean.Scene;
import com.jiagu.mobile.tourguide.utils.MyImageLoader;
import com.jiagu.mobile.tourguide.utils.Path;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
// 高磊
public class SceneAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<Scene> data;
	private LayoutInflater inflater;
	public SceneAdapter(Context context, ArrayList<Scene> data) {
		super();
		this.context = context;
		this.data = data;
		inflater = LayoutInflater.from(context);
	}
	public ArrayList<Scene> getData() {
		return data;
	}
	public void setData(ArrayList<Scene> data) {
		this.data = data;
	}
	public void addData() {
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.scene_gridview_item, null);
			holder.name = (TextView) convertView
					.findViewById(R.id.scene_item_text_01);
			holder.jiage = (TextView) convertView
					.findViewById(R.id.scene_item_text_02);
			holder.jibie = (TextView) convertView
					.findViewById(R.id.scene_item_text_03);
			holder.leixing = (TextView) convertView
					.findViewById(R.id.scene_item_text_04);
			holder.image = (ImageView) convertView.findViewById(R.id.scene_item_image_01);
			holder.imagebtn = (ImageView) convertView.findViewById(R.id.scene_item_image_02);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.imagebtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.putExtra("id", data.get(position).getScenicid());
				i.putExtra("a", "a");
				i.setClass(context, SceneDetialActivity.class);
				context.startActivity(i);
			}
		});
		Scene scene = data.get(position);
		holder.name.setText(scene.getScenicabbr());
		holder.jiage.setText("门票价格:"+scene.getPrice());
		holder.jibie.setText("景区级别:"+scene.getScenicLevel());
		holder.leixing.setText("景区类型:"+scene.getScenicType());
		if(!(scene.getScenicphoto()==null)){
			MyImageLoader.loadImage(Path.IMAGER_ADRESS+scene.getScenicphoto(), holder.image);
		}
		return convertView;
	}
	class ViewHolder{
		TextView name,jibie,leixing,jiage;
		ImageView image,imagebtn;
	}
}
