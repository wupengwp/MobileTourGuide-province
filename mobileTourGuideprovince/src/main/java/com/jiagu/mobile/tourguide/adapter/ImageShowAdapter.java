package com.jiagu.mobile.tourguide.adapter;

/*
 * Copyright (C) 2013 www.418log.org
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.util.List;

import com.ab.bitmap.AbImageCache;
import com.ab.bitmap.AbImageDownloader;
import com.ab.global.AbConstant;
import com.ab.util.AbFileUtil;
import com.ab.util.AbStrUtil;
import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.utils.MyImageLoader;
import com.jiagu.mobile.tourguide.utils.Path;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

// TODO: Auto-generated Javadoc
/**
 * 适配器 网络URL的图片.
 */
public class ImageShowAdapter extends BaseAdapter {

	/** The m context. */
	private Context mContext;

	/** The m image paths. */
	private List<String> mImagePaths = null;

	/** The m width. */
	private int mWidth;

	/** The m height. */
	private int mHeight;
	
	private String mytype;

	// 图片下载器
	private AbImageDownloader mAbImageDownloader = null;

	/**
	 * Instantiates a new ab image show adapter.
	 * 
	 * @param context
	 *            the context
	 * @param imagePaths
	 *            the image paths
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 */
	public ImageShowAdapter(Context context, List<String> imagePaths,
			int width, int height,String mytype) {
		mContext = context;
		this.mImagePaths = imagePaths;
		this.mWidth = width;
		this.mHeight = height;
		this.mytype = mytype;
		// 图片下载器
		mAbImageDownloader = new AbImageDownloader(mContext);
		mAbImageDownloader.setWidth(this.mWidth);
		mAbImageDownloader.setHeight(this.mHeight);
		mAbImageDownloader.setLoadingImage(R.drawable.ic_noml);
		mAbImageDownloader.setErrorImage(R.drawable.ic_error);
		mAbImageDownloader.setNoImage(R.drawable.ic_noml);
	}
	
	public List<String> getmImagePaths() {
		return mImagePaths;
	}

	public void setmImagePaths(List<String> mImagePaths) {
		this.mImagePaths = mImagePaths;
	}
	public void addmImagePaths(List<String> mImagePaths) {
		// TODO Auto-generated method stub
		this.mImagePaths.addAll(mImagePaths);
		notifyDataSetChanged();
	}
	/**
	 * 描述：获取数量.
	 *
	 * @return the count
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount() {
		return mImagePaths.size();
	}

	/**
	 * 描述：获取索引位置的路径.
	 *
	 * @param position
	 *            the position
	 * @return the item
	 * @see android.widget.Adapter#getItem(int)
	 */
	public Object getItem(int position) {
		return mImagePaths.get(position);
	}

	/**
	 * 描述：获取位置.
	 *
	 * @param position
	 *            the position
	 * @return the item id
	 * @see android.widget.Adapter#getItemId(int)
	 */
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 描述：显示View.
	 *
	 * @param position
	 *            the position
	 * @param convertView
	 *            the convert view
	 * @param parent
	 *            the parent
	 * @return the view
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 *      android.view.ViewGroup)
	 */
	public View getView(int position, View convertView, ViewGroup parent) {

		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			LinearLayout mLinearLayout = new LinearLayout(mContext);
			RelativeLayout mRelativeLayout = new RelativeLayout(mContext);
			ImageView mImageView1 = new ImageView(mContext);
			mImageView1.setScaleType(ScaleType.FIT_CENTER);
			ImageView mImageView2 = new ImageView(mContext);
			mImageView2.setScaleType(ScaleType.FIT_CENTER);
			holder.mImageView1 = mImageView1;
			holder.mImageView2 = mImageView2;
			LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.FILL_PARENT);
			lp1.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
			RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
					mWidth, mHeight);
			lp2.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
			lp2.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
			mRelativeLayout.addView(mImageView1, lp2);
			mRelativeLayout.addView(mImageView2, lp2);
			mLinearLayout.addView(mRelativeLayout, lp1);

			convertView = mLinearLayout;
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.mImageView1.setImageBitmap(null);
		holder.mImageView2.setBackgroundDrawable(null);

		String imagePath = mImagePaths.get(position);

//		if ("xiugai".equals(mytype)&& position<(mImagePaths.size()-1)) {
//			MyImageLoader.loadImage(Path.IMAGER_ADRESS + imagePath, holder.mImageView1);
//		}else{
			if (!AbStrUtil.isEmpty(imagePath)) {
			// 从缓存中获取图片，很重要否则会导致页面闪动
			Bitmap bitmap = AbImageCache.getBitmapFromCache(imagePath);
			// 缓存中没有则从网络和SD卡获取
			if (bitmap == null) {
				holder.mImageView1.setImageResource(R.drawable.ic_noml);
				if (imagePath.indexOf("http://") != -1) {
					// 图片的下载
					mAbImageDownloader.setType(AbConstant.ORIGINALIMG);
					mAbImageDownloader.display(holder.mImageView1, imagePath);

				} else if (imagePath.indexOf("/") == -1) {
					// 索引图片
					try {
						int res = Integer.parseInt(imagePath);
						holder.mImageView1.setImageDrawable(mContext
								.getResources().getDrawable(res));
					} catch (Exception e) {
						holder.mImageView1
								.setImageResource(R.drawable.ic_error);
					}
				} else {
					Bitmap mBitmap = AbFileUtil.getBitmapFromSD(new File(
							imagePath), AbConstant.SCALEIMG, mWidth, mHeight);
					if (mBitmap != null) {
						holder.mImageView1.setImageBitmap(mBitmap);
					} else {
						// 无图片时显示
						holder.mImageView1
								.setImageResource(R.drawable.ic_noml);
					}
				}
			} else {
				// 直接显示
				holder.mImageView1.setImageBitmap(bitmap);
			}
		} else {
			// 无图片时显示
			holder.mImageView1.setImageResource(R.drawable.ic_noml);
		}
			holder.mImageView1.setAdjustViewBounds(true);
//	}
		
		return convertView;
	}

	//刷新适配器
	public void notifyAdapter(List<String> imagePaths) {
		mImagePaths = imagePaths;
		notifyDataSetChanged();
	}
	/**
	 * 增加并改变视图.
	 * 
	 * @param position
	 *            the position
	 * @param imagePaths
	 *            the image paths
	 */
	public void addItem(int position, String imagePaths) {
		mImagePaths.add(position, imagePaths);
		notifyDataSetChanged();
	}

	/**
	 * 增加多条并改变视图.
	 * 
	 * @param imagePaths
	 *            the image paths
	 */
	public void addItems(List<String> imagePaths) {
		mImagePaths.addAll(imagePaths);
		notifyDataSetChanged();
	}

	/**
	 * 增加多条并改变视图.
	 */
	public void clearItems() {
		mImagePaths.clear();
		notifyDataSetChanged();
	}

	/**
	 * View元素.
	 */
	public static class ViewHolder {

		/** The m image view1. */
		public ImageView mImageView1;

		/** The m image view2. */
		public ImageView mImageView2;
	}

}
