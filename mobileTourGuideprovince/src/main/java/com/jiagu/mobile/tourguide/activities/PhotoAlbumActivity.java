package com.jiagu.mobile.tourguide.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.bean.PhotoAlbum;
import com.jiagu.mobile.tourguide.utils.HackyViewPager;
import com.jiagu.mobile.tourguide.utils.HttpUtil;
import com.jiagu.mobile.tourguide.utils.MyImageLoader;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.tourguide.utils.PhotoView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * @author Administrator 相册 高磊
 */
public class PhotoAlbumActivity extends Activity implements
		OnClickListener {

	SamplePagerAdapter mSamplePagerAdapter;
	private String scenicId;
	private String path;
	private ArrayList<PhotoAlbum> data;
	private HackyViewPager viewPager;
	private String photoId;
	private TextView no_photos;
	private String type_type = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photoalbum);
		Intent i = getIntent();
		scenicId = i.getStringExtra("id");
		photoId = i.getStringExtra("photoId");
		path = i.getStringExtra("path");
		if(i.hasExtra("type_type")){
			type_type  = i.getStringExtra("type_type");
		}
		initView();
	}

	private void initView() {
		// 初始化控件
		no_photos= (TextView) findViewById(R.id.no_photos);
		viewPager = (HackyViewPager) findViewById(R.id.pager);
		ImageButton image = (ImageButton) findViewById(R.id.activity_photoalbum_image_return);
		image.setImageResource(R.drawable.img_return_selector);
		image.setOnClickListener(this);
		mSamplePagerAdapter = new SamplePagerAdapter();
		getPhotoAlbum(path, scenicId, photoId);
	}

	private void getPhotoAlbum(String path, String scenicId, String photoId) {
		RequestParams params = new RequestParams(); // 绑定参数
		if (scenicId != null&&!scenicId.equals("")) {
			params.put("scenicId", scenicId);
		}
		if (photoId != null&&!photoId.equals("")) {
			params.put("photoId", photoId);
		}
		HttpUtil.post(path, params, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(Throwable arg0, String arg1) {
				super.onFailure(arg0, arg1);
			}

			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				if (response.equals("") || response == null) {
					Toast.makeText(PhotoAlbumActivity.this, "服务器在打盹11",
							Toast.LENGTH_SHORT).show();
				} else {
					JSONObject object = JSONObject.parseObject(response);
					JSONArray top = object.getJSONArray("records");
					String result = object.getString("result");
					if (result.equals("0")) {
						data = (ArrayList<PhotoAlbum>) JSONArray.parseArray(
								top.toJSONString(), PhotoAlbum.class);
						if (data.size()>0) {
							mSamplePagerAdapter.notifyDataSetChanged();
							viewPager.setAdapter(mSamplePagerAdapter);
						}else {
							no_photos.setVisibility(View.VISIBLE);
						}
						
					} else {
						Toast.makeText(PhotoAlbumActivity.this, "服务器在打盹",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.activity_photoalbum_image_return:
			onBackPressed();
			break;
		}
	}

	class SamplePagerAdapter extends PagerAdapter {
		private LayoutInflater mInflater;

		public SamplePagerAdapter() {
			super();
			mInflater = LayoutInflater.from(PhotoAlbumActivity.this);
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
				if ("2".equals(type_type.trim())) {
				MyImageLoader.loadImage(url, tvPic);
				}else {
					MyImageLoader.loadImage(Path.IMAGER_ADRESS + url, tvPic);
				}
				
			}
			if (!(imgaerurl == null)) {
				if ("2".equals(type_type.trim())) {
					MyImageLoader.loadImage(imgaerurl, tvPic);
					}else {
				MyImageLoader.loadImage(Path.IMAGER_ADRESS + imgaerurl, tvPic);
					}
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
}
