package com.jiagu.mobile.tourguide.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.activities.NewIntroductionsdActivity;
import com.jiagu.mobile.tourguide.activities.PhotoAlbumActivity;
import com.jiagu.mobile.tourguide.activities.TravelStrategyActivity;
import com.jiagu.mobile.tourguide.bean.TravelStrategy;
import com.jiagu.mobile.tourguide.utils.MyImageLoader;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.tourguide.utils.UesrInfo;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
public class TravelStrategyAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<TravelStrategy> data;
	private LayoutInflater inflater;
	private HashMap<Integer, View> map;

	public TravelStrategyAdapter(Context context, ArrayList<TravelStrategy> data) {
		super();
		this.context = context;
		this.data = data;
		inflater = LayoutInflater.from(context);
		map = new HashMap<Integer,View>();
	}
	public ArrayList<TravelStrategy> getData() {
		return data;
	}

	public void setData(ArrayList<TravelStrategy> data) {
		this.data = data;
	}

	public void addData(ArrayList<TravelStrategy> data) {
		this.data.addAll(data);
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return data.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view;
		final ViewHolder mViewHolder;
		if (map.get(position) == null) {
			mViewHolder = new ViewHolder();
			view = inflater
					.inflate(R.layout.travelstrategy_listview_item, null);
			mViewHolder.time = (TextView) view
					.findViewById(R.id.travelstrategy_listview_item_tv_01);
			mViewHolder.timeint = (TextView) view
					.findViewById(R.id.travelstrategy_listview_item_tv_02);
			mViewHolder.name = (TextView) view
					.findViewById(R.id.travelstrategy_listview_item_tv_03);
			mViewHolder.text = (TextView) view
					.findViewById(R.id.travelstrategy_listview_item_tv_04);
			mViewHolder.imageview = (ImageView) view
					.findViewById(R.id.travelstrategy_image);
			view.setTag(mViewHolder);
			map.put(position, view);
		} else {
			view = map.get(position);
			mViewHolder = (ViewHolder) view.getTag();
		}
		TravelStrategy travelStrategy = data.get(position);
		mViewHolder.time.setText(travelStrategy.getNdays());
		mViewHolder.timeint.setText(travelStrategy.getTimer());
		mViewHolder.name.setText(travelStrategy.getTitle());
		mViewHolder.text.setText(travelStrategy.getText());
		if (travelStrategy.getUrl() == null
				|| travelStrategy.getUrl().equals("")) {

		} else {
			MyImageLoader.loadImage(
					Path.IMAGER_ADRESS + travelStrategy.getUrl(),
					mViewHolder.imageview);
			mViewHolder.imageview.setVisibility(View.VISIBLE);
		}
		//点击照片进入相册
//		mViewHolder.imageview.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				
//				AlertDialog.Builder builder = new Builder(context);
//				builder.setPositiveButton("修改", new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//						if (UesrInfo.getTourIDid().equals(TravelStrategyActivity.travelStrategyActivity.tourid)) {
//							Intent i = new Intent();					
//							i.putExtra("index", position);
//							i.putExtra("photoId", data.get(position).getPhotoid());
//							i.putExtra("typeA", "xiugai");
//							i.putExtra("pathA", Path.SERVER_ADRESS
//									+ "characteristic/photoList.htm");
//							i.putExtra("a", "");
//							i.putExtra("id", data.get(position).getId());
//							i.putExtra("title", "" + data.get(position).getTitle());
//							i.putExtra("text", "" + data.get(position).getText());
//							i.putExtra("timer", "" + data.get(position).getNdays());
//							i.setClass(context, NewIntroductionsdActivity.class);
//							context.startActivity(i);							
//						}else{
//							Toast.makeText(context, "非本人发布不可修改", Toast.LENGTH_SHORT).show();
//						}
//						
//					}
//				});
//				builder.setNegativeButton("查看", new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//						Intent i = new Intent();
//						i.putExtra("photoId", data.get(position).getPhotoid());
//						i.putExtra("path", Path.SERVER_ADRESS
//								+ "characteristic/photoList.htm");
//						i.setClass(context, PhotoAlbumActivity.class);
//						context.startActivity(i);
//					
//					}
//				});
//				builder.create().show();
//			}
//
//		});
		return view;
	}
	private class ViewHolder {
		TextView name, time, text, timeint;
		ImageView imageview;
	}
}
