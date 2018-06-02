package com.jiagu.mobile.tourguide.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.bean.Exercise;
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
 * @author Administrator Adapate主要景观的适配器 	高磊 	2015-01-04
 */
public class ExerciseAdapter extends BaseAdapter{
	private Context context;
	private ArrayList<Exercise> data;
	private LayoutInflater inflater;
	
	public ExerciseAdapter(Context context, ArrayList<Exercise> data) {
		super();
		this.context = context;
		this.data = data;
		inflater = LayoutInflater.from(context);
	}
	public List<Exercise> getData() {
		return data;
	}

	public void setData(ArrayList<Exercise> data2) {
		this.data = data2;
	}

	public void addData(ArrayList<Exercise> data) {
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
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.exercise_listview_item, null);
			holder.name = (TextView) convertView
					.findViewById(R.id.exercise_listview_item_tv01);
			holder.time = (TextView) convertView
					.findViewById(R.id.exercise_listview_item_tv02);
			holder.image = (ImageView) convertView
					.findViewById(R.id.exercise_listview_item_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Exercise exercise = data.get(position);
		holder.name.setText(exercise.getTitle());
		try {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-mm-dd");
			holder.time.setText(sdf.format(sdf.parse(exercise.getTimer())));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!(exercise.getImagurl1()==null)){
			MyImageLoader.loadImage(Path.IMAGER_ADRESS+exercise.getImagurl1(), holder.image);
		}
		return convertView;
	}
	class ViewHolder {
		public TextView name,time;
		public ImageView image;
	}
}
