package com.jiagu.mobile.tourguide.activities;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.jiagu.mobile.tourguide.adapter.ListenAdapter;
import com.jiagu.mobile.tourguide.bean.Listen;
import com.jiagu.mobile.tourguide.fragments.HomeFragment;
import com.jiagu.mobile.tourguide.utils.HttpUtil;
import com.jiagu.mobile.tourguide.utils.MusicPlayer;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.tourguide.utils.UesrInfo;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

//我的收听
public class ListenActivity extends TitleDrawerActivity implements
		OnClickListener, OnItemClickListener, OnRefreshListener<ListView>,
		OnLastItemVisibleListener, OnItemLongClickListener {
	private PullToRefreshListView mPullToRefreshListView;
	private int a = 2;
	private String path = Path.SERVER_ADRESS
			+ "personalCenter/myListenList.htm";
	private ArrayList<Listen> data;
	private ListenAdapter adapter;
	private String tourIDid = UesrInfo.getTourIDid();
	private MusicPlayer player;
	private Button button;
	private String stringId;
	private int count = 1;
	private boolean is = true;
	private int number = 0;

	@Override
	@SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listen);
		ImageButton image = (ImageButton) findViewById(R.id.activity_listen_image_return);
		TextView text = (TextView) findViewById(R.id.activity_listen_text);
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.list_indent_activity);
		button = (Button) findViewById(R.id.textshanchu);
		ImageButton shanchu = (ImageButton) findViewById(R.id.activity_listen_image_shanchu);

		getData(path);

		image.setOnClickListener(this);
		shanchu.setOnClickListener(this);
		button.setOnClickListener(this);
		mPullToRefreshListView.setOnItemClickListener(this);
		mPullToRefreshListView.setOnRefreshListener(this);
		mPullToRefreshListView.setOnLastItemVisibleListener(this);
		mPullToRefreshListView.getRefreshableView().setOnItemLongClickListener(
				this);
		HomeFragment.homeFragment.myLBS(ListenActivity.this, text);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		setAppName(UesrInfo.area);
		appName.setText(UesrInfo.area + "手机导游");
	}

	@Override
	protected void onPause() {
		super.onPause();
		PowerManager manager = (PowerManager) getSystemService(Activity.POWER_SERVICE);
		if (manager.isScreenOn()) {
			// 没有关屏
			if (player != null && player.isPlaying()) {
				player.pause();
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (player != null) {
			player.destroy();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 返回键方法
		case R.id.activity_listen_image_return:

			onBackPressed();
			break;
		case R.id.activity_listen_image_shanchu:
			if (data == null || data.toString().equals("")) {

			} else {
				adapter.setType("a");
				button.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.textshanchu:
			AlertDialog.Builder builder = new Builder(ListenActivity.this);
			builder.setMessage("您确定要批量删除吗？");
			builder.setTitle("确认删除");
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							data = adapter.getData();
							// get = 0;
							System.out.println("response:" + data.size());
							for (int i = 0; i < data.size(); i++) {
								if (data.get(i).getChecked()) {
									if (count == 1) {
										stringId = data.get(i).getId();
									} else {
										stringId = stringId + ","
												+ data.get(i).getId();
									}
									count++;
								}
							}
							RequestParams params = new RequestParams();
							params.put("id", stringId);
							HttpUtil.post(Path.SERVER_ADRESS
									+ "personalCenter/deleteMyListenList.htm",
									params, new AsyncHttpResponseHandler() {
										@Override
										public void onFailure(Throwable arg0,
												String arg1) {
											super.onFailure(arg0, arg1);
											Toast.makeText(ListenActivity.this,
													"请检查您的网络",
													Toast.LENGTH_SHORT).show();
										}

										@Override
										public void onSuccess(String response) {
											super.onSuccess(response);
											mPullToRefreshListView
													.setAdapter(null);
											getData(path);
											button.setVisibility(View.GONE);
											player.pause();
										}
									});
						}
					});
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder.create().show();
			break;
		}
	}

	// 加载
	@Override
	public void onLastItemVisible() {
		// button.setVisibility(View.GONE);
		showProgress();
		RequestParams params = new RequestParams();
		params.put("pages", "" + a);
		params.put("userId", tourIDid);
		if (is) {
			is = false;
			HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

				@Override
				public void onFailure(Throwable arg0, String arg1) {
					Toast.makeText(getApplicationContext(), "请检查您的网络",
							Toast.LENGTH_LONG).show();
					super.onFailure(arg0, arg1);
					is = true;
					hideProgress();
				}

				@Override
				public void onSuccess(String response) {
					super.onSuccess(response);
					if (response == null || response.equals("")) {
						Toast.makeText(ListenActivity.this, "服务器在打盹",
								Toast.LENGTH_SHORT).show();
					} else {
						JSONObject object = JSONObject.parseObject(response);
						JSONArray top = object.getJSONArray("records");
						String result = object.getString("result");
						if (result.equals("0")) {
							ArrayList<Listen> list = new ArrayList<Listen>();
							list = (ArrayList<Listen>) JSONArray.parseArray(
									top.toJSONString(), Listen.class);
							for (Listen listen : list) {
								data.add(listen);
							}
							adapter.setData(data);
							adapter.setType("b");
							adapter.notifyDataSetChanged();
							a++;
						} else if (result.equals("30")) {
							Toast.makeText(ListenActivity.this, "已是最后一页",
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(ListenActivity.this, "服务器在打盹",
									Toast.LENGTH_SHORT).show();
						}
					}
					is = true;
					hideProgress();
				}
			});
		}
	}

	// 更新
	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		if (player != null) {
			player.destroy();
		}
		button.setVisibility(View.GONE);
		getData(path);
	}

	// item跳转
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent i;
		String type = data.get(arg2 - 1).getType();
		if (type.equals("0")) {
			i = new Intent();
			i.putExtra("url", data.get(arg2 - 1).getVoiceUrl());
			i.putExtra("id", data.get(arg2 - 1).getScenicid());
			i.putExtra("a", "b");
			i.setClass(ListenActivity.this, SceneDetialActivity.class);
			startActivity(i);
		} else if (type.equals("1")) {
			i = new Intent();
			i.putExtra("id", data.get(arg2 - 1).getAttracid());
			i.setClass(ListenActivity.this, ScenicSpotsDetailsActivity.class);
			startActivity(i);
		} else if (type.equals("2")) {
			i = new Intent();
			i.putExtra("id", data.get(arg2 - 1).getCharacteristicId());
			i.putExtra("url", Path.SERVER_ADRESS
					+ "characteristic/folkwaysDetial.htm");
			i.putExtra("type", "2");
			i.setClass(ListenActivity.this, FeatureDetailsActivity.class);
			startActivity(i);
		} else if (type.equals("3")) {
			i = new Intent();
			i.putExtra("id", data.get(arg2 - 1).getCharacteristicId());
			i.putExtra("url", Path.SERVER_ADRESS
					+ "characteristic/historyDetial.htm");
			i.putExtra("type", "3");
			i.setClass(ListenActivity.this, FeatureDetailsActivity.class);
			startActivity(i);
		} else if (type.equals("4")) {
			i = new Intent();
			i.putExtra("id", data.get(arg2 - 1).getCharacteristicId());
			i.putExtra("url", Path.SERVER_ADRESS
					+ "characteristic/famousPeopleDetial.htm");
			i.putExtra("type", "4");
			i.setClass(ListenActivity.this, FeatureDetailsActivity.class);
			startActivity(i);
		} else {
			i = new Intent();
			i.putExtra("id", data.get(arg2 - 1).getCharacteristicId());
			i.putExtra("url", Path.SERVER_ADRESS
					+ "characteristic/specialtyDetial.htm");
			i.putExtra("type", "5");
			i.setClass(ListenActivity.this, FeatureDetailsActivity.class);
			startActivity(i);
		}
	}

	private void getData(final String path) {
		showProgress();
		RequestParams params = new RequestParams();
		params.put("pages", "" + 1);
		params.put("userId", tourIDid);
		HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				super.onFailure(arg0, arg1);
				number++;
				if (number < 3) {
					getData(path);
				} else {
					Toast.makeText(getApplicationContext(), "请检查您的网络",
							Toast.LENGTH_LONG).show();
					hideProgress();
					number = 0;
				}
			}

			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				if (response == null || response.equals("")) {
					Toast.makeText(ListenActivity.this, "服务器在打盹",
							Toast.LENGTH_SHORT).show();
				} else {
					JSONObject object = JSONObject.parseObject(response);
					JSONArray top = object.getJSONArray("records");
					String result = object.getString("result");
					if (result.equals("0")) {
						data = (ArrayList<Listen>) JSONArray.parseArray(
								top.toJSONString(), Listen.class);
						if (data.size() == 0) {
							Toast.makeText(ListenActivity.this, "赶紧去收听吧!!!!!",
									Toast.LENGTH_SHORT).show();
						} else {
							adapter = new ListenAdapter(ListenActivity.this,
									data, "b");
							mPullToRefreshListView.setAdapter(adapter);
							mPullToRefreshListView.onRefreshComplete();
							player = ListenAdapter.stop();
							a = 2;
						}
					} else if (result.equals("30")) {
						Toast.makeText(ListenActivity.this, "赶紧去收听吧!!!!!",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(ListenActivity.this, "服务器在打盹",
								Toast.LENGTH_SHORT).show();
					}
				}
				hideProgress();
			}
		});
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
			final int arg2, long arg3) {
		// adapter.setType("a");
		// button.setVisibility(View.VISIBLE);
		AlertDialog.Builder builder = new Builder(ListenActivity.this);
		builder.setMessage("您确定要删除该收听记录吗？");
		builder.setTitle("收听删除");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				RequestParams params = new RequestParams();
				params.put("id", data.get(arg2 - 1).getId());
				HttpUtil.post(Path.SERVER_ADRESS
						+ "personalCenter/deleteMyListenList.htm", params,
						new AsyncHttpResponseHandler() {

							@Override
							public void onFailure(Throwable arg0, String arg1) {
								Toast.makeText(ListenActivity.this, "请检查您的网络",
										Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onSuccess(String response) {
								super.onSuccess(response);
								JSONObject object = JSONObject
										.parseObject(response);
								String result = object.getString("result");
								if (result.equals("0")) {
									Toast.makeText(ListenActivity.this, "删除成功",
											Toast.LENGTH_SHORT).show();
									data.remove(arg2 - 1);
									adapter.setData(data);
									adapter.setA();
									adapter.notifyDataSetChanged();
									player.pause();
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
