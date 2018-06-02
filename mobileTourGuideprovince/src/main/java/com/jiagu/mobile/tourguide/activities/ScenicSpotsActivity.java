package com.jiagu.mobile.tourguide.activities;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.activities.bases.TitleDrawerActivity;
import com.jiagu.mobile.tourguide.adapter.ScenicSpotsAdapter;
import com.jiagu.mobile.tourguide.bean.ScenicSpots;
import com.jiagu.mobile.tourguide.utils.HttpUtil;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.tourguide.utils.UesrInfo;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ScenicSpotsActivity extends TitleDrawerActivity implements
		OnClickListener, OnItemClickListener, OnRefreshListener<GridView>,
		OnLastItemVisibleListener {
	private TextView name;
	private PullToRefreshGridView mGridView;
	private String id;
	private ArrayList<ScenicSpots> data;
	private ScenicSpotsAdapter adapter;
	private int a = 2;
	private boolean is = true;
	private int number = 0;

	@Override
	@SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scenicspots);
		Intent i = getIntent();
		id = i.getStringExtra("id");
		ImageButton image = (ImageButton) findViewById(R.id.activity_scenicspots_image_return);
		name = (TextView) findViewById(R.id.activity_scenicspots_image_name);
		name.setText(i.getStringExtra("name"));
		mGridView = (PullToRefreshGridView) findViewById(R.id.scenicspots_listview);
		image.setOnClickListener(this);
		getData();
		mGridView.setOnItemClickListener(this);
		mGridView.setOnRefreshListener(this);
		mGridView.setOnLastItemVisibleListener(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		setAppName(UesrInfo.area);
		appName.setText(UesrInfo.area + "手机导游");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.activity_scenicspots_image_return:
			onBackPressed();
			break;
		}
	}

	private void getData() {
		showProgress();
		RequestParams params = new RequestParams();
		params.put("scenicId", id);
		params.put("pages", "1");
		String path = Path.SERVER_ADRESS + "scenic/scenicAttrac.htm";
		HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				super.onFailure(arg0, arg1);
				number++;
				if (number < 3) {
					getData();
				} else {
					hideProgress();
					Toast.makeText(getApplicationContext(), "请检查您的网络",
							Toast.LENGTH_LONG).show();
					number = 0;
				}
			}

			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				JSONObject object = JSONObject.parseObject(response);
				JSONArray top = object.getJSONArray("records");
				String result = object.getString("result");
				if (result.equals("0") && (!top.equals("")) && top != null) {
					data = (ArrayList<ScenicSpots>) JSONArray.parseArray(
							top.toJSONString(), ScenicSpots.class);
					adapter = new ScenicSpotsAdapter(ScenicSpotsActivity.this,
							data);
					mGridView.setAdapter(adapter);
					mGridView.onRefreshComplete();
					a = 2;
					hideProgress();
				} else {
					Toast.makeText(ScenicSpotsActivity.this, "该景区暂无景点信息",
							Toast.LENGTH_SHORT).show();
					hideProgress();
				}
			}
		});
	}

	@Override
	public void onLastItemVisible() {
		showProgress();
		RequestParams params = new RequestParams();
		params.put("scenicId", id);
		params.put("pages", "" + a);
		String path = Path.SERVER_ADRESS + "scenic/scenicAttrac.htm";
		if (is) {
			is = false;
			HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

				@Override
				public void onFailure(Throwable arg0, String arg1) {
					super.onFailure(arg0, arg1);
					hideProgress();
					Toast.makeText(getApplicationContext(), "请检查您的网络",
							Toast.LENGTH_LONG).show();
					is = true;
				}

				@Override
				public void onSuccess(String response) {
					super.onSuccess(response);
					JSONObject object = JSONObject.parseObject(response);
					JSONArray top = object.getJSONArray("records");
					String result = object.getString("result");
					if (result.equals("0") && (!top.equals("")) && top != null) {
						ArrayList<ScenicSpots> list = (ArrayList<ScenicSpots>) JSONArray
								.parseArray(top.toJSONString(),
										ScenicSpots.class);
						for (ScenicSpots scenicSpots : list) {
							data.add(scenicSpots);
						}
						adapter.setData(data);
						adapter.notifyDataSetChanged();
						a++;
						hideProgress();
					} else {
						if (result.equals("30")) {
							Toast.makeText(ScenicSpotsActivity.this, "已是最后一页",
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(ScenicSpotsActivity.this, "服务器在打盹",
									Toast.LENGTH_SHORT).show();
						}
						hideProgress();
					}
					is = true;
				}
			});
		}
	}

	@Override
	public void onRefresh(PullToRefreshBase<GridView> refreshView) {
		getData();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent i = new Intent(ScenicSpotsActivity.this,
				ScenicSpotsDetailsActivity.class);
		i.putExtra("id", data.get(arg2).getAttracid());
		startActivity(i);
	}
}
