package com.jiagu.mobile.tourguide.adapter;

import java.util.ArrayList;

import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.bean.PhotoAlbum;
import com.jiagu.mobile.tourguide.utils.MyImageLoader;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.tourguide.utils.PhotoView;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
public class SamplePagerAdapter extends PagerAdapter {
	private LayoutInflater mInflater;
	private Context context;
	private ArrayList<PhotoAlbum> data;

	public SamplePagerAdapter(Context context, ArrayList<PhotoAlbum> data) {
		super();
		this.context = context;
		this.data = data;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	// 解析数据
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = mInflater.inflate(R.layout.item_dctiviets, null);
		PhotoView tvPic = (PhotoView) view.findViewById(R.id.icon);
		TextView tv_name = (TextView) view.findViewById(R.id.tv_name);

		PhotoAlbum photoAlbum = data.get(position);
		String url = photoAlbum.getUrl();
		String imgaerurl = photoAlbum.getImgaeurl();
		String text = photoAlbum.getText();
		String title = photoAlbum.getTitle();
		if (!(url == null)) {
			MyImageLoader.loadImage(Path.IMAGER_ADRESS + url, tvPic);
		}
		if (!(imgaerurl == null)) {
			MyImageLoader.loadImage(Path.IMAGER_ADRESS + imgaerurl, tvPic);
		}
		if (!(text == null)) {
			tv_name.setText(text);
		}
		if (!(title == null)) {
			tv_name.setText(title);
		}
		container.addView(view);
		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}
}
