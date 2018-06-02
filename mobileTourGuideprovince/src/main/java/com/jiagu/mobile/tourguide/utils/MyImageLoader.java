package com.jiagu.mobile.tourguide.utils;

import android.widget.ImageView;

import com.jiagu.mobile.tourguide.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyImageLoader {

	public static void loadImage(String url, ImageView imageView) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_empty)
				.showImageForEmptyUri(R.drawable.ic_error)
				.showImageOnFail(R.drawable.ic_noml).cacheInMemory(true)
				.cacheOnDisk(true)
				.build();
		ImageLoader.getInstance().displayImage(url, imageView, options);
	}

}
