package com.jiagu.mobile.tourguide.activities;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.activities.bases.TitleDrawerActivity;
import com.jiagu.mobile.tourguide.adapter.SceneAdapter;
import com.jiagu.mobile.tourguide.bean.Scene;
import com.jiagu.mobile.tourguide.utils.FileTools;
import com.jiagu.mobile.tourguide.utils.HttpUtil;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.tourguide.utils.UesrInfo;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

//主要景观		高磊
public class SceneActivity extends TitleDrawerActivity implements
		OnClickListener, OnItemClickListener, OnRefreshListener<GridView>,
		OnLastItemVisibleListener {
	private PopupWindow popupwindow;
	private RadioButton classify, sort;
	private ImageView image;
	private PullToRefreshGridView mGridView;
	int a, b, c, d;
	private List<String> dataA, dataB;
	private ArrayList<Scene> data;
	private TextView textA, textB, textC, textD;
	private double latitude;
	private double longitude;
	private SceneAdapter adapter;
	static int count = 0;
	String path = Path.SERVER_ADRESS + "scenic/scenicByArea.htm";
	String pathIP = Path.SERVER_ADRESS + "scenic/scenicByArea.htm";
	private boolean is = true;
	
	@Override
	@SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scene);
		classify = (RadioButton) findViewById(R.id.scene_1_but);
		sort = (RadioButton) findViewById(R.id.scene_2_but);
		image = (ImageView) findViewById(R.id.activity_scene_image_return);
		mGridView = (PullToRefreshGridView) findViewById(R.id.scene_gridview);
		dataA = new ArrayList<String>();
		dataB = new ArrayList<String>();
		dataA.add("全部景观");
		dataA.add("历史景观");
		dataA.add("城市景观");
		dataA.add("自然景观");
		dataB.add("人气最高");
		dataB.add("最新发布");
		dataB.add("距离最近");
		myLBS();
		classify.setOnClickListener(this);
		sort.setOnClickListener(this);
		image.setOnClickListener(this);
		getData(path,b, c);
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
		case R.id.scene_1_but:
			a = 1;
			if (popupwindow != null && popupwindow.isShowing()) {
				popupwindow.dismiss();
				return;
			} else {
				initmPopupWindowViews(dataA);
				popupwindow.showAsDropDown(v, 0, 0);
			}
			break;
		case R.id.scene_2_but:
			a = 2;
			if (popupwindow != null && popupwindow.isShowing()) {
				popupwindow.dismiss();
				return;
			} else {
				initmPopupWindowViews(dataB);
				popupwindow.showAsDropDown(v, 0, 0);
			}
			break;
		case R.id.activity_scene_image_return:
			onBackPressed();
			break;
		}
	}

	public void initmPopupWindowViews(List<String> data) {
		// 获取自定义布局文件pop.xml的视图
		View customView = getLayoutInflater().inflate(
				R.layout.popupwindow_view, null, false);
		WindowManager wm = (WindowManager) SceneActivity.this
				.getSystemService(Context.WINDOW_SERVICE);

		@SuppressWarnings("deprecation")
		int width = wm.getDefaultDisplay().getWidth();// 获取屏幕的宽度

		// 创建PopupWindow实例
		popupwindow = new PopupWindow(customView, (width / 2) - 20,
				LayoutParams.WRAP_CONTENT);
		// 自定义view添加触摸事件
		customView.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (popupwindow != null && popupwindow.isShowing()) {
					popupwindow.dismiss();
					popupwindow = null;
				}
				return false;
			}
		});
		textA = (TextView) customView.findViewById(R.id.pop_textView1);
		textB = (TextView) customView.findViewById(R.id.pop_textView2);
		textC = (TextView) customView.findViewById(R.id.pop_textView3);
		textD = (TextView) customView.findViewById(R.id.pop_textView4);
		switch (a) {
		case 1:
			textA.setText(dataA.get(0));
			textB.setText(dataA.get(1));
			textC.setText(dataA.get(2));
			textD.setText(dataA.get(3));
			break;
		case 2:
			textA.setText(dataB.get(0));
			textB.setText(dataB.get(1));
			textC.setText(dataB.get(2));
			textD.setVisibility(View.GONE);
			break;

		}
		textA.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (a) {
				case 1:
					b = 0;
					classify.setText(dataA.get(0));
					getData(path,b, c);
					break;
				case 2:
					c = 1;
					sort.setText(dataB.get(0));
					getData(path,b, c);
					break;
				}
				popupwindow.dismiss();
			}
		});
		textB.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (a) {
				case 1:
					b = 1;
					classify.setText(dataA.get(1));
					getData(path,b, c);
					break;
				case 2:
					c = 2;
					sort.setText(dataB.get(1));
					getData(path,b, c);
					break;
				}
				popupwindow.dismiss();
			}
		});
		textC.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (a) {
				case 1:
					b = 2;
					classify.setText(dataA.get(2));
					getData(path,b, c);
					break;
				case 2:
					c = 3;
					sort.setText(dataB.get(2));
					getData(path,b, c);
					break;
				}
				popupwindow.dismiss();
			}
		});
		textD.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (a) {
				case 1:
					b = 3;
					classify.setText(dataA.get(3));
					getData(path,b, c);
					break;
				}
				popupwindow.dismiss();
			}
		});
	}

	// 获取数据
	private void getData(String path,final int b, final int c) {
		showProgress();
		RequestParams params = new RequestParams();
		if (UesrInfo.areaType.equals("4")) {
			Intent intent = getIntent();
			String areaName = intent.getStringExtra("areaname");
			String areatype = intent.getStringExtra("areatype");
			params.put("areaType", areatype);
			params.put("area", areaName);
		} else {
			params.put("areaType", UesrInfo.areaType);
			params.put("area", UesrInfo.area);
		}
		params.put("scenicType", "" + b);
		params.put("orderby", "" + c);
		if (c == 3) {
			params.put("latitude", latitude + "," + longitude);
		}
		params.put("pages", "" + 1);
		HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				super.onFailure(arg0, arg1);
				hideProgress();
				if (count < 3){
					getData(pathIP,b, c);
					FileTools.writeLog("scene.txt", "======>>>>>>主要景观列表数据------count:1-------Throwable:"+arg0+"--------"+arg1);
				}else{
					count= 0;
					Toast.makeText(getApplicationContext(), "请检查您的网络",
							Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				if (response == null || response.equals("")) {
					Toast.makeText(SceneActivity.this, "服务器在打盹",
							Toast.LENGTH_SHORT).show();
				} else {
					JSONObject object = JSONObject.parseObject(response);
					JSONArray top = object.getJSONArray("records");
					String result = object.getString("result");
					if (top.equals("") || top == null || result.equals("4")) {
						Toast.makeText(SceneActivity.this, "服务器在打盹",
								Toast.LENGTH_SHORT).show();
					} else if (result.equals("30")) {
						Toast.makeText(SceneActivity.this, "已是最后一页",
								Toast.LENGTH_SHORT).show();
					} else if (result.equals("0")) {
						data = (ArrayList<Scene>) JSONArray.parseArray(
								top.toJSONString(), Scene.class);
						adapter = new SceneAdapter(SceneActivity.this, data);
						mGridView.setAdapter(adapter);
						mGridView.onRefreshComplete();
						d = 2;
					} else {
						Toast.makeText(SceneActivity.this, "服务器在打盹",
								Toast.LENGTH_SHORT).show();
					}
				}
				hideProgress();
			}

		});
	}

	// 跳转
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent i = new Intent();
		i.putExtra("id", data.get(arg2).getScenicid());
		i.putExtra("a", "b");
		i.setClass(SceneActivity.this, SceneDetialActivity.class);
		startActivity(i);
	}

	// 定位
	public void myLBS() {
		LocationClient client = new LocationClient(SceneActivity.this);

		LocationClientOption options = new LocationClientOption();
		options.setLocationMode(LocationMode.Hight_Accuracy);
		options.setCoorType("bd09ll");
		options.setIsNeedAddress(true);
		options.setNeedDeviceDirect(true);
		options.setOpenGps(true);
		options.setScanSpan(2000);

		client.setLocOption(options);
		client.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation location) {
				// 维度
				latitude = location.getLatitude();
				// 经度
				longitude = location.getLongitude();
			}
		});
		client.start();
		client.requestLocation();
	}

	// 刷新
	@Override
	public void onRefresh(PullToRefreshBase<GridView> refreshView) {
		getData(path,b, c);
	}

	// 加载
	@Override
	public void onLastItemVisible() {
		showProgress();
		RequestParams params = new RequestParams();
		if (UesrInfo.areaType.equals("4")) {
			Intent intent = getIntent();
			String areaName = intent.getStringExtra("areaname");
			String areatype = intent.getStringExtra("areatype");
			params.put("areaType", areatype);
			params.put("area", areaName);
		} else {
			params.put("areaType", UesrInfo.areaType);
			params.put("area", UesrInfo.area);
		}
		params.put("scenicType", "" + b);
		params.put("orderby", "" + c);
		if (c == 3) {
			params.put("latitude", latitude + "," + longitude);
		}
		params.put("pages", "" + d);
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
					FileTools.writeLog("scene.txt", "======>>>>>>主要景观列表数据------count:"+d+"-------Throwable:"+arg0+"--------"+arg1);
				}

				@Override
				public void onSuccess(String response) {
					super.onSuccess(response);
					if (response == null || response.equals("")) {
						Toast.makeText(SceneActivity.this, "服务器在打盹",
								Toast.LENGTH_SHORT).show();
					} else {
						JSONObject object = JSONObject.parseObject(response);
						JSONArray top = object.getJSONArray("records");
						String result = object.getString("result");
						if (top.equals("") || top == null || result.equals("4")) {
							Toast.makeText(SceneActivity.this, "服务器在打盹",
									Toast.LENGTH_SHORT).show();
						} else if (result.equals("30")) {
							Toast.makeText(SceneActivity.this, "已是最后一页",
									Toast.LENGTH_SHORT).show();
						} else if (result.equals("0")) {
							ArrayList<Scene> list = (ArrayList<Scene>) JSONArray
									.parseArray(top.toJSONString(), Scene.class);
							for (Scene scene : list) {
								data.add(scene);
							}
							adapter.setData(data);
							adapter.notifyDataSetChanged();
							d++;
						} else {
							Toast.makeText(SceneActivity.this, "服务器在打盹",
									Toast.LENGTH_SHORT).show();
						}
					}
					is = true;
					hideProgress();
				}

			});
		}
	}
}
