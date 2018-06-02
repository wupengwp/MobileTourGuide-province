package com.jiagu.mobile.tourguide.activities;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.activities.bases.TitleDrawerActivity;
import com.jiagu.mobile.tourguide.adapter.StrategyAdapter;
import com.jiagu.mobile.tourguide.bean.Strategy;
import com.jiagu.mobile.tourguide.utils.HttpUtil;
import com.jiagu.mobile.tourguide.utils.MyImageLoader;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.tourguide.utils.UesrInfo;
import com.jiagu.mobile.tourguide.widget.CircleImageView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 旅游攻略 ==MyStrategy高磊 2015-01-04
 */
public class StrategyActivity extends TitleDrawerActivity implements
		OnClickListener, OnItemClickListener, OnLastItemVisibleListener,
		OnRefreshListener<ListView>, OnItemLongClickListener {
	private PullToRefreshListView mListView;
	private int a = 2;
	private ArrayList<Strategy> data;
	private StrategyAdapter adapter;
	private String path;
	private String judge;
	private CircleImageView mCircleImageView;
	private TextView name;
	private int count = 0;

	@Override
	@SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stategy);
		ImageView imagerF = (ImageView) findViewById(R.id.stategy_image_but_01);
		ImageView image = (ImageView) findViewById(R.id.stategy_imager_but_02);
		mListView = (PullToRefreshListView) findViewById(R.id.strategy_pullrefesh);
		LinearLayout layout = (LinearLayout) findViewById(R.id.stategy_image_layout);
		LinearLayout layout1 = (LinearLayout) findViewById(R.id.stategy_image_layout1);
		mCircleImageView = (CircleImageView) findViewById(R.id.login_user_logo_iv);
		ImageView fanhui = (ImageView) findViewById(R.id.activity_stategy_image_return);
		name = (TextView) findViewById(R.id.text_personal_name);
		name.setText(UesrInfo.getUsername());
		String userIcon = UesrInfo.getUserIcon();
		if (!userIcon.equals("") && userIcon != null) {
			MyImageLoader.loadImage(Path.IMAGER_ADRESS + userIcon,
					mCircleImageView);
		}
		Intent i = getIntent();
		judge = i.getStringExtra("judge");
		if (judge.equals("my")) {
			path = Path.SERVER_ADRESS + "personalCenter/myTouristStrategy.htm";
			layout.setVisibility(View.GONE);
			layout1.setVisibility(View.VISIBLE);
			mListView.getRefreshableView().setOnItemLongClickListener(this);
		} else {
			path = Path.SERVER_ADRESS
					+ "personalCenter/touristStrategyList.htm";
		}
		getData(path);
		fanhui.setOnClickListener(this);
		imagerF.setOnClickListener(this);
		image.setOnClickListener(this);
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
		case R.id.activity_stategy_image_return:
			onBackPressed();
			break;
		case R.id.stategy_image_but_01:
			onBackPressed();
			break;
		case R.id.stategy_imager_but_02:
			if (UesrInfo.getTourIDid() == null
					|| UesrInfo.getTourIDid().equals("")) {
				Toast.makeText(StrategyActivity.this, "请在个人中心处登录",
						Toast.LENGTH_SHORT).show();
			} else {
				Intent i = new Intent();
				i.putExtra("a", "b");
				i.putExtra("id", "");
				i.putExtra("title", "");
				i.setClass(StrategyActivity.this,
						NewIntroductionsdActivity.class);
				startActivity(i);
			}
			break;
		}
	}

	// Item点击事件处理
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent i = new Intent();
		String name = data.get(arg2 - 1).getTourname();
		String title = data.get(arg2 - 1).getTitle();
		String id = data.get(arg2 - 1).getStrategyid();
		i.putExtra("name", name);
		i.putExtra("title", title);
		i.putExtra("id", id);
		i.setClass(StrategyActivity.this, TravelStrategyActivity.class);
		startActivity(i);
	}

	// 下拉刷新
	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		getData(path);
	}

	// 上拉加载
	@Override
	public void onLastItemVisible() {
		showProgress();
		mListView.setSelected(true);
		RequestParams params = new RequestParams(); // 绑定参数
		params.put("pages", "" + a);
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
				if (response == null || response.equals("")) {
					Toast.makeText(StrategyActivity.this, "服务器在打盹",
							Toast.LENGTH_LONG).show();
				} else {
					JSONObject object = JSONObject.parseObject(response);
					JSONArray top = object.getJSONArray("records");
					String result = object.getString("result");
					if (top == null || top.equals("") || result.equals("4")) {
						Toast.makeText(StrategyActivity.this, "服务器在打盹",
								Toast.LENGTH_LONG).show();
					} else if (result.equals("30")) {
						Toast.makeText(StrategyActivity.this, "已是最后一页",
								Toast.LENGTH_LONG).show();
					} else {
						ArrayList<Strategy> list = new ArrayList<Strategy>();
						list = (ArrayList<Strategy>) JSONArray.parseArray(
								top.toJSONString(), Strategy.class);
						for (Strategy strategy : list) {
							data.add(strategy);
						}
						adapter.setData(data);
						adapter.notifyDataSetChanged();
						a++;
					}
				}
				hideProgress();
			}
		});
	}

	// 首次数据加载
	private void getData(final String path) {
		showProgress();
		RequestParams params = new RequestParams(); // 绑定参数
		params.put("pages", "" + 1);
		if (judge.equals("my")) {
			params.put("tourId", UesrInfo.getTourIDid());
		}
		HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				super.onFailure(arg0, arg1);
				count++;
				if (count < 3) {
					getData(path);
				} else {
					count = 0;
					hideProgress();
					mListView.onRefreshComplete();
					mListView.setSelected(true);
					Toast.makeText(StrategyActivity.this, "请检查您的网络",
							Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				if (response == null | response.equals(null)) {
					Toast.makeText(StrategyActivity.this, "服务器在打盹",
							Toast.LENGTH_LONG).show();
				} else {
					JSONObject object = JSONObject.parseObject(response);
					JSONArray top = object.getJSONArray("records");
					String result = object.getString("result");
					if (top == null | top.equals(null) || result.equals("4")) {
						Toast.makeText(StrategyActivity.this, "服务器在打盹",
								Toast.LENGTH_LONG).show();
					} else {
						data = (ArrayList<Strategy>) JSONArray.parseArray(
								top.toJSONString(), Strategy.class);
						if (data.size() == 0) {
							Toast.makeText(StrategyActivity.this, "赶紧去发表攻略吧,亲",
									Toast.LENGTH_LONG).show();
						} else {
							adapter = new StrategyAdapter(data,
									StrategyActivity.this);
							mListView.setAdapter(adapter);
							a = 2;
							mListView.onRefreshComplete();
							mListView.setSelected(true);
						}
					}
				}
				hideProgress();
			}
		});
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
			final int arg2, long arg3) {
		AlertDialog.Builder builder = new Builder(StrategyActivity.this);
		builder.setMessage("您确定要删除该攻略吗？");
		builder.setTitle("攻略删除");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				RequestParams params = new RequestParams();
				params.put("id", data.get(arg2 - 1).getId());
				params.put("type", "1");
				HttpUtil.post(Path.SERVER_ADRESS
						+ "personalCenter/deleteStrategy.htm", params,
						new AsyncHttpResponseHandler() {

							@Override
							public void onFailure(Throwable arg0, String arg1) {
								Toast.makeText(StrategyActivity.this,
										"请检查您的网络", Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onSuccess(String response) {
								super.onSuccess(response);
								JSONObject object = JSONObject
										.parseObject(response);
								String result = object.getString("result");
								if (result.equals("0")) {
									Toast.makeText(StrategyActivity.this,
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
		return true;
	}
}
