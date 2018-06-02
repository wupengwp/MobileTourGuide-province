package com.jiagu.mobile.tourguide.adapter;

import java.util.ArrayList;

import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.bean.HomePagerBean;
import com.jiagu.mobile.tourguide.utils.MyImageLoader;
import com.jiagu.mobile.tourguide.utils.Path;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MainAdapter extends PagerAdapter {
	private Context context;
	private ArrayList<HomePagerBean> data;
	private LayoutInflater inflater;

	public MainAdapter(Context context, ArrayList<HomePagerBean> data) {
		super();
		this.context = context;
		this.data = data;
		inflater = LayoutInflater.from(context);
	}

	public ArrayList<HomePagerBean> getData() {
		return data;
	}

	public void setData(ArrayList<HomePagerBean> data) {
		this.data = data;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg1 == arg0;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = inflater.inflate(R.layout.help_viewpager, null);
		ImageView image = (ImageView) view
				.findViewById(R.id.help_viewpager_item_imager);
		MyImageLoader.loadImage(Path.IMAGER_ADRESS
				+ data.get(position).getUrl(), image);
		container.addView(view);
		return view;
	}

}
