package com.jiagu.mobile.tourguide.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.activities.bases.TitleDrawerActivity;
import com.jiagu.mobile.tourguide.adapter.MyGridViewAdapter;
import com.jiagu.mobile.tourguide.bean.Records;
import com.jiagu.mobile.tourguide.utils.FileTools;
import com.jiagu.mobile.tourguide.utils.HttpUtil;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.tourguide.utils.UesrInfo;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
//特色西安页面
public class FeatureActivity extends TitleDrawerActivity implements
		OnItemClickListener, OnClickListener, OnCheckedChangeListener,
		OnRefreshListener<GridView>, OnLastItemVisibleListener {
	private String path = Path.SERVER_ADRESS + "characteristic/folkways.htm";
	private MyGridViewAdapter adapter;
	private PullToRefreshGridView gridView;
	private ArrayList<Records> parseArray;
	private String url = Path.SERVER_ADRESS
			+ "characteristic/folkwaysDetial.htm";
	private int a = 2;
	private int b = 2;
	private int c = 4;
	static int count = 0;
	private boolean is = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feature);
		ImageButton imageReturn = (ImageButton) findViewById(R.id.activity_feature_image_return);
		imageReturn.setOnClickListener(this);
		RadioGroup radioGroup = (RadioGroup) findViewById(R.id.but_feature_selector);
		radioGroup.setOnCheckedChangeListener(this);
		gridView = (PullToRefreshGridView) findViewById(R.id.activity_feature_gridview);
		getData(path);
		gridView.setOnItemClickListener(this);
		gridView.setOnRefreshListener(this);
		gridView.setOnLastItemVisibleListener(this);
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
		case R.id.activity_feature_image_return:
			onBackPressed();
			break;
		}
	}
	
	private void getData(final String path) {
		showProgress();
		RequestParams params = new RequestParams();
		params.put("pages", "1");
		if (UesrInfo.areaType.equals("2")) {
			params.put("area", UesrInfo.area);
		}else{
			params.put("area", UesrInfo.sceneId);
		}
		params.put("areaType", UesrInfo.areaType);
		HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				super.onFailure(arg0, arg1);
				hideProgress();
				count++;
				if (count < 3){
					FileTools.writeLog("feature.txt", "======>>>>>>特色详情数据------count:"+count+"Throwable:"+arg0+"--------"+arg1);
					getData(path);
				}else{
					count= 0;
					Toast.makeText(getApplicationContext(), "请检查您的网络",
							Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				if (response == null||response.equals("")) {
					Toast.makeText(FeatureActivity.this, "服务器在打盹",
							Toast.LENGTH_SHORT).show();
				} else {
					JSONObject object = JSONObject.parseObject(response);
					JSONArray top = object.getJSONArray("records");
					String result = object.getString("result");
					if (top == null || top.equals("") || result.equals("4")) {
						Toast.makeText(FeatureActivity.this, "服务器在打盹",
								Toast.LENGTH_SHORT).show();
					} else if (result.equals("30")) {
						gridView.setAdapter(null);
						Toast.makeText(FeatureActivity.this, "暂无数据",
								Toast.LENGTH_SHORT).show();
					} else if(result.equals("0")){
						parseArray = (ArrayList<Records>) JSONArray.parseArray(
								top.toJSONString(), Records.class);
						if (parseArray.size() <= 0) {
							Toast.makeText(FeatureActivity.this, "服务器在打盹",
									Toast.LENGTH_SHORT).show();
						} else {
							adapter = new MyGridViewAdapter(parseArray,
									FeatureActivity.this);
							gridView.setAdapter(adapter);
							gridView.onRefreshComplete();
							a = 2;
						}
					}
				}
				hideProgress();
			}

		});

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent i = new Intent();
		i.putExtra("id", parseArray.get(arg2).getId());
		i.putExtra("url", url);
		i.putExtra("type", "" + b);
		i.putExtra("aprClass", "" + c);
		i.setClass(FeatureActivity.this, FeatureDetailsActivity.class);
		startActivity(i);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.but_feature_selector_1:
			getData(Path.SERVER_ADRESS + "characteristic/folkways.htm");
			url = Path.SERVER_ADRESS + "characteristic/folkwaysDetial.htm";
			path = Path.SERVER_ADRESS + "characteristic/folkways.htm";
			b = 2;
			c = 4;
			break;
		case R.id.but_feature_selector_2:
			getData(Path.SERVER_ADRESS + "characteristic/history.htm");
			url = Path.SERVER_ADRESS + "characteristic/historyDetial.htm";
			path = Path.SERVER_ADRESS + "characteristic/history.htm";
			b = 3;
			c = 5;
			break;
		case R.id.but_feature_selector_3:
			getData(Path.SERVER_ADRESS + "characteristic/famousPeople.htm");
			url = Path.SERVER_ADRESS + "characteristic/famousPeopleDetial.htm";
			path = Path.SERVER_ADRESS + "characteristic/famousPeople.htm";
			b = 4;
			c = 6;
			break;
		case R.id.but_feature_selector_4:
			getData(Path.SERVER_ADRESS + "characteristic/specialty.htm");
			url = Path.SERVER_ADRESS + "characteristic/specialtyDetial.htm";
			path = Path.SERVER_ADRESS + "characteristic/specialty.htm";
			b = 5;
			c = 7;
			break;
		}
	}

	// 下拉刷新
	@Override
	public void onRefresh(PullToRefreshBase<GridView> refreshView) {
		getData(path);
	}

	// 上拉加载
	@Override
	public void onLastItemVisible() {
		showProgress();
		RequestParams params = new RequestParams();
		params.put("pages", "" + a);
		if (UesrInfo.areaType.equals("2")) {
			params.put("area", UesrInfo.area);
		}else{
			params.put("area", UesrInfo.sceneId);
		}
		params.put("areaType", UesrInfo.areaType);
		if (is) {
			is = false;
			HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

				@Override
				public void onFailure(Throwable arg0, String arg1) {
					super.onFailure(arg0, arg1);
					hideProgress();
					Toast.makeText(getApplicationContext(), "请检查您的网络",Toast.LENGTH_LONG).show();
					is = true;
					FileTools.writeLog("feature.txt", "======>>>>>>特色列表第"+a+"页数据------count:"+count+"Throwable:"+arg0+"--------"+arg1);
				}

				@Override
				public void onSuccess(String response) {
					super.onSuccess(response);
					if (response.equals("") || response == null) {
						Toast.makeText(FeatureActivity.this, "服务器在打盹",Toast.LENGTH_SHORT).show();
					} else {
						JSONObject object = JSONObject.parseObject(response);
						String result = object.getString("result");
						JSONArray top = object.getJSONArray("records");
						if (top == null || top.equals("") ||result.equals("4")) {
							Toast.makeText(FeatureActivity.this, "服务器在打盹",Toast.LENGTH_SHORT).show();
						} else if (result.equals("30")) {
							Toast.makeText(FeatureActivity.this, "已是最后一页",Toast.LENGTH_SHORT).show();
						} else if(result.equals("0")){
							List<Records> list = JSONArray.parseArray(top.toJSONString(), Records.class);
							if (list.size() <= 0) {
								Toast.makeText(FeatureActivity.this, "服务器在打盹",Toast.LENGTH_SHORT).show();
							} else {
								for (Records records : list) {
									parseArray.add(records);
								}
								adapter.setData(parseArray);
								adapter.notifyDataSetChanged();
								a++;
							}
						}else{
							Toast.makeText(FeatureActivity.this, "服务器在打盹",Toast.LENGTH_SHORT).show();
						}
					}
					is = true;
					hideProgress();
				}

			});
		}
	}
}
