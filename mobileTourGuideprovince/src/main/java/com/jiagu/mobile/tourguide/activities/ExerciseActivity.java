package com.jiagu.mobile.tourguide.activities;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.activities.bases.TitleDrawerActivity;
import com.jiagu.mobile.tourguide.adapter.ExerciseAdapter;
import com.jiagu.mobile.tourguide.bean.Exercise;
import com.jiagu.mobile.tourguide.utils.FileTools;
import com.jiagu.mobile.tourguide.utils.HttpUtil;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.tourguide.utils.UesrInfo;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

//近期活动
public class ExerciseActivity extends TitleDrawerActivity implements OnClickListener, OnItemClickListener, OnRefreshListener<ListView>,OnLastItemVisibleListener {
	private ArrayList<Exercise> data;
	private PullToRefreshListView mListView;
	private String path = Path.SERVER_ADRESS + "scenicPlanning/scenicPlanningList.htm";
	private int a = 2;
	private ExerciseAdapter adapter;
	private static int count = 0;
	private boolean is = true;

	@Override
	@SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exercise);
		ImageView image = (ImageView) findViewById(R.id.activity_exercise_image_return);
		mListView = (PullToRefreshListView) findViewById(R.id.list_indent_activity);
		image.setOnClickListener(this);
		getData();
		mListView.setOnItemClickListener(this);
		mListView.setOnRefreshListener(this);
		mListView.setOnLastItemVisibleListener(this);
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
		case R.id.activity_exercise_image_return:
			onBackPressed();
			break;
		}
	}

	// 下载数据
	private void getData() {
		showProgress();
		RequestParams params = new RequestParams();
		params.put("pages", "" + 1);
		if (UesrInfo.areaType.equals("2")) {
			params.put("area", UesrInfo.area);
		} else {
			params.put("area", UesrInfo.sceneId);
		}
		params.put("areaType", UesrInfo.areaType);
		HttpUtil.post(path, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onFailure(Throwable arg0, String arg1) {
				super.onFailure(arg0, arg1);
				hideProgress();
				count++;
				if (count < 3) {
					FileTools.writeLog("exercise.txt", "活动列表第1页错误----count:"
							+ count + "---Throwable:" + arg0 + "------" + arg1);
					getData();
				} else {
					count = 0;
					Toast.makeText(getApplicationContext(), "请检查您的网络",
							Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				if (response == null || response.equals("")) {
					Toast.makeText(ExerciseActivity.this, "服务器在打盹",
							Toast.LENGTH_SHORT).show();
				} else {
					
					JSONObject object = JSONObject.parseObject(response);
					JSONArray top = object.getJSONArray("records");
					String result = object.getString("result");
					if (top == null || top.equals("") || result.equals("4")) {
						Toast.makeText(ExerciseActivity.this, "服务器在打盹", Toast.LENGTH_SHORT).show();
					} else if (result.equals("0")) {
						data = (ArrayList<Exercise>) JSONArray.parseArray(
								top.toJSONString(), Exercise.class);
						adapter = new ExerciseAdapter(ExerciseActivity.this,
								data);
						mListView.setAdapter(adapter);
						mListView.onRefreshComplete();
						a = 2;
					} else if (result.equals("30")) {
						Toast.makeText(ExerciseActivity.this, "该景区暂无活动信息",
								Toast.LENGTH_SHORT).show();
					}
				}
				hideProgress();
			}

		});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent i = new Intent();
		i.putExtra("id", data.get(arg2 - 1).getId());
		i.putExtra("photoId", data.get(arg2 - 1).getPhotoid());
		i.setClass(ExerciseActivity.this, ExerciseDetailActivity.class);
		startActivity(i);
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		getData();
	}

	@Override
	public void onLastItemVisible() {
		showProgress();
		mListView.setSelected(true);
		RequestParams params = new RequestParams(); // 绑定参数
		params.put("pages", "" + a);
		if (UesrInfo.areaType.equals("2")) {
			params.put("area", UesrInfo.area);
		} else {
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
					Toast.makeText(getApplicationContext(), "请检查您的网络",
							Toast.LENGTH_LONG).show();
					is = true;
					FileTools.writeLog("exercise.txt", "活动列表第" + a
							+ "页错误-------Throwable:" + arg0 + "------" + arg1);
				}

				@Override
				public void onSuccess(String response) {
					super.onSuccess(response);
					if (response == null || response.equals("")) {
						Toast.makeText(ExerciseActivity.this, "服务器在打盹",
								Toast.LENGTH_LONG).show();
					} else {
						JSONObject object = JSONObject.parseObject(response);
						String result = object.getString("result");
						JSONArray top = object.getJSONArray("records");
						if (top == null || top.equals("") || result.equals("4")) {
							Toast.makeText(ExerciseActivity.this, "服务器在打盹",
									Toast.LENGTH_LONG).show();
						} else if (result.equals("30")) {
							Toast.makeText(ExerciseActivity.this, "已是最后一页",
									Toast.LENGTH_LONG).show();
						} else {
							ArrayList<Exercise> list = new ArrayList<Exercise>();
							list = (ArrayList<Exercise>) JSONArray.parseArray(
									top.toJSONString(), Exercise.class);
							for (Exercise exercise : list) {
								data.add(exercise);
							}
							adapter.setData(data);
							adapter.notifyDataSetChanged();
							a++;
						}
					}
					hideProgress();
					is = true;
				}
			});
		}
	}
}
