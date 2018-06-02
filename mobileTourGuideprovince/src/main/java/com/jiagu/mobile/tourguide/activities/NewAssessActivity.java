package com.jiagu.mobile.tourguide.activities;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
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
import com.jiagu.mobile.tourguide.adapter.SceneDetialListViewAdapter;
import com.jiagu.mobile.tourguide.bean.Appraise;
import com.jiagu.mobile.tourguide.utils.HttpUtil;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.tourguide.utils.UesrInfo;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

//我的评价
public class NewAssessActivity extends TitleDrawerActivity implements
		OnClickListener, OnLastItemVisibleListener,
		OnRefreshListener<ListView>, OnItemLongClickListener {
	private PullToRefreshListView mListView;
	private int a = 2;
	private ArrayList<Appraise> data;
	private SceneDetialListViewAdapter adapter;
	private int number = 0;

	@Override
	@SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newassess);
		ImageButton image = (ImageButton) findViewById(R.id.activity_newassess_image_return);
		mListView = (PullToRefreshListView) findViewById(R.id.activity_newassess_list);
		getData();
		image.setOnClickListener(this);
		mListView.setOnLastItemVisibleListener(this);
		mListView.setOnRefreshListener(this);
		mListView.getRefreshableView().setOnItemLongClickListener(this);
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
		// 返回键方法
		case R.id.activity_newassess_image_return:
			onBackPressed();
			break;
		}
	}

	private void getData() {
		RequestParams params = new RequestParams();
		params.put("pages", "1");
		params.put("tourId", UesrInfo.getTourIDid());
		String path = Path.SERVER_ADRESS
				+ "personalCenter/myTouristAppraise.htm";
		HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				super.onFailure(arg0, arg1);
				number++;
				if (number < 3) {
					getData();
				} else {
					number++;
					hideProgress();
					Toast.makeText(getApplicationContext(), "请检查您的网络",
							Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				if (!(response == null)) {
					JSONObject object = JSONObject.parseObject(response);
					JSONArray top = object.getJSONArray("records");
					String result = object.getString("result");
					if (result.equals("0")) {
						data = (ArrayList<Appraise>) JSONArray.parseArray(
								top.toJSONString(), Appraise.class);
						if (data.size() == 0) {
							Toast.makeText(NewAssessActivity.this,
									"赶紧去评价吧!!!!!", Toast.LENGTH_SHORT).show();
						} else {
							adapter = new SceneDetialListViewAdapter(
									NewAssessActivity.this, data);
							mListView.setAdapter(adapter);
							mListView.onRefreshComplete();
							a = 2;
						}
					}
				} else {
					Toast.makeText(NewAssessActivity.this, "服务器在打盹",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		getData();
	}

	@Override
	public void onLastItemVisible() {
		RequestParams params = new RequestParams();
		params.put("pages", "" + a);
		params.put("tourId", UesrInfo.getTourIDid());
		String path = Path.SERVER_ADRESS
				+ "personalCenter/myTouristAppraise.htm";
		HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				super.onFailure(arg0, arg1);
				hideProgress();
				Toast.makeText(getApplicationContext(), "请检查您的网络",
						Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				if (!(response == null)) {
					JSONObject object = JSONObject.parseObject(response);
					JSONArray top = object.getJSONArray("records");
					String result = object.getString("result");
					if (!(top == null) && result.equals("0")) {
						ArrayList<Appraise> list = (ArrayList<Appraise>) JSONArray
								.parseArray(top.toJSONString(), Appraise.class);
						for (Appraise appraise : list) {
							data.add(appraise);
						}
						adapter.setData(data);
						adapter.notifyDataSetChanged();
						a++;
					} else if (top.equals("") && result.equals("0")) {
						Toast.makeText(NewAssessActivity.this, "暂无数据",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(NewAssessActivity.this, "已是最后一页",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(NewAssessActivity.this, "服务器在打盹",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
			final int arg2, long arg3) {
		AlertDialog.Builder builder = new Builder(NewAssessActivity.this);
		builder.setMessage("您确定要删除该评价吗？");
		builder.setTitle("评价删除");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				RequestParams params = new RequestParams();
				params.put("id", data.get(arg2 - 1).getId());
				HttpUtil.post(Path.SERVER_ADRESS
						+ "personalCenter/deleteAppraise.htm", params,
						new AsyncHttpResponseHandler() {

							@Override
							public void onFailure(Throwable arg0, String arg1) {
								Toast.makeText(NewAssessActivity.this,
										"请检查您的网络", Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onSuccess(String response) {
								super.onSuccess(response);
								JSONObject object = JSONObject
										.parseObject(response);
								String result = object.getString("result");
								if (result.equals("0")) {
									Toast.makeText(NewAssessActivity.this,
											"删除成功", Toast.LENGTH_SHORT).show();
									data.remove(arg2 - 1);
									adapter.setData(data);
									adapter.notifyDataSetChanged();
								} else {

								}
							}
						});
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
		return false;
	}
}
