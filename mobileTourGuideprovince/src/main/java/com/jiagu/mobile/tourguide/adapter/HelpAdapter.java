package com.jiagu.mobile.tourguide.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jiagu.mobile.tourguide.R;

public class HelpAdapter extends PagerAdapter {
	private Context context;
	private int[] data;
	private LayoutInflater inflater;

	public HelpAdapter(Context context, int[] data) {
		super();
		this.context = context;
		this.data = data;
		inflater = LayoutInflater.from(context);
	}

	public int[] getData() {
		return data;
	}

	public void setData(int[] data) {
		this.data = data;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return data.length;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
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
		image.setImageResource(data[position]);
		container.addView(view);
		return view;
	}
}
