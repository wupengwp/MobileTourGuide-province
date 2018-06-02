package com.jiagu.mobile.zhifu;

import java.util.ArrayList;
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
import com.jiagu.mobile.tourguide.utils.HttpUtil;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.tourguide.utils.UesrInfo;
import com.jiagu.mobile.zhifu.adapter.GroupPurchaseAdapter;
import com.jiagu.mobile.zhifu.adapter.PopAdapter;
import com.jiagu.mobile.zhifu.bean.GroupPurchase;
import com.jiagu.mobile.zhifu.bean.Pop;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;

/**
 * @ClassName: MainActivity
 * @Description: 团购列表界面
 * @author 高磊
 * @date 2015年2月01日 12:21
 * @company 西安甲骨企业文化传播有限公司
 */
public class GroupPurchaseActivity extends TitleDrawerActivity implements
		OnLastItemVisibleListener, OnItemClickListener,
		OnRefreshListener<GridView>, OnClickListener {

	private PullToRefreshGridView mPullToRefreshGridView;
	private ArrayList<GroupPurchase> data;
	private GroupPurchaseAdapter adapter;
	private int pages = 2;
	private PopupWindow popupwindow;
	private PopupWindow searchpopupwindow;
	private ListView popListView;
	private int type;
	private String goodsType = "-1";
	private int sortRule = 1;
	private TextView tv02, tv03;
	private ArrayList<Pop> list, listA;
	private double latitude, longitude;
	private boolean is = true;
	private int count = 0;
	private Button btnSearch;
	private EditText etSearch;
	private ImageView ivDeleteText;
	private PullToRefreshGridView searchpopupwindow_gridview;
	private GroupPurchaseAdapter searchadapter;
	private ArrayList<GroupPurchase> searchdata;
	private int searchpages = 2;
	private boolean searchis = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_purchase);

		ImageView iamge = (ImageView) findViewById(R.id.grouppurchase_iv);
		ImageView search_btn = (ImageView) findViewById(R.id.search_btn);

		tv02 = (TextView) findViewById(R.id.grouppurchase_tv_02);
		tv03 = (TextView) findViewById(R.id.grouppurchase_tv_03);
		mPullToRefreshGridView = (PullToRefreshGridView) findViewById(R.id.grouppurchase_gridview);
		myLBS();
		getData();
		iamge.setOnClickListener(this);
		search_btn.setOnClickListener(this);
		tv02.setOnClickListener(this);
		tv03.setOnClickListener(this);
		mPullToRefreshGridView.setOnItemClickListener(this);
		mPullToRefreshGridView.setOnRefreshListener(this);
		mPullToRefreshGridView.setOnLastItemVisibleListener(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// setAppName(UesrInfo.area);
		appName.setText(UesrInfo.area + "手机导游");
	}

	private void getData() {
		showProgress();
		RequestParams params = new RequestParams();
		params.put("areaType", UesrInfo.areaType);
		params.put("area", UesrInfo.area.substring(0, 3));
		params.put("type", "" + goodsType);
		params.put("order", "" + sortRule);
		params.put("latitude", "" + latitude + "," + longitude);
		params.put("pages", "1");
		String path = Path.SERVER_ADRESS + "admission/findAdmissionList.htm";
		HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				super.onFailure(arg0, arg1);
				count++;
				if (count < 3) {
					getData();
				} else {
					count = 0;
					Toast.makeText(getApplicationContext(), "请检查您的网络",
							Toast.LENGTH_LONG).show();
				}
				hideProgress();
			}

			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				if (response.equals("") || response == null) {
					Toast.makeText(GroupPurchaseActivity.this, "服务器在打盹",
							Toast.LENGTH_SHORT).show();
				} else {
					JSONObject object = JSONObject.parseObject(response);
					JSONArray top = object.getJSONArray("records");
					String result = object.getString("result");
					if (top.equals("") || top == null || result.equals("4")) {
						Toast.makeText(GroupPurchaseActivity.this, "服务器在打盹",
								Toast.LENGTH_SHORT).show();
					} else if (result.equals("30")) {
						mPullToRefreshGridView.setAdapter(null);
						Toast.makeText(GroupPurchaseActivity.this, "暂无该类商品",
								Toast.LENGTH_SHORT).show();
					} else if (result.equals("0")) {
						data = (ArrayList<GroupPurchase>) JSONArray.parseArray(
								top.toJSONString(), GroupPurchase.class);
						if (data.size() == 0) {
							mPullToRefreshGridView.setAdapter(null);
						} else {
							adapter = new GroupPurchaseAdapter(data,
									GroupPurchaseActivity.this);
							mPullToRefreshGridView.setAdapter(adapter);
							mPullToRefreshGridView.onRefreshComplete();
							pages = 2;
						}

					} else {
						mPullToRefreshGridView.setAdapter(null);
						if (adapter != null) {
							adapter.setData(null);
						}
						Toast.makeText(GroupPurchaseActivity.this, "服务器在打盹",
								Toast.LENGTH_SHORT).show();
					}
				}
				hideProgress();
			}
		});
	}

	@Override
	public void onRefresh(PullToRefreshBase<GridView> refreshView) {
		// TODO Auto-generated method stub

		if (searchpopupwindow != null && searchpopupwindow.isShowing()) {
			getSearchData();
		} else {
			getData();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if (searchpopupwindow != null && searchpopupwindow.isShowing()) {
			Intent i = new Intent(GroupPurchaseActivity.this,
					PurchaseActivity.class);
			i.putExtra("id", searchdata.get(position).getId());
			startActivity(i);
		} else {
			Intent i = new Intent(GroupPurchaseActivity.this,
					PurchaseActivity.class);
			i.putExtra("id", data.get(position).getId());
			startActivity(i);
		}
	}

	@Override
	public void onLastItemVisible() {
		// TODO Auto-generated method stub
		if (searchpopupwindow != null && searchpopupwindow.isShowing()) {
			refreData();
		} else {

			showProgress();
			RequestParams params = new RequestParams();
			params.put("type", "" + goodsType);
			params.put("order", "" + sortRule);
			params.put("areaType", UesrInfo.areaType);
			params.put("area", UesrInfo.area.substring(0, 3));
			params.put("latitude", "" + latitude + "," + longitude);
			params.put("pages", "" + pages);
			String path = Path.SERVER_ADRESS
					+ "admission/findAdmissionList.htm";
			if (is) {
				is = false;
				HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(Throwable arg0, String arg1) {
						Toast.makeText(getApplicationContext(), "请检查您的网络",
								Toast.LENGTH_LONG).show();
						super.onFailure(arg0, arg1);
						hideProgress();
						is = true;
					}

					@Override
					public void onSuccess(String response) {
						super.onSuccess(response);
						if (response.equals("") || response == null) {
							Toast.makeText(GroupPurchaseActivity.this,
									"服务器在打盹", Toast.LENGTH_SHORT).show();
						} else {
							JSONObject object = JSONObject
									.parseObject(response);
							JSONArray top = object.getJSONArray("records");
							String result = object.getString("result");
							if (top.equals("") || top == null
									|| result.equals("4")) {
								Toast.makeText(GroupPurchaseActivity.this,
										"服务器在打盹", Toast.LENGTH_SHORT).show();
							} else if (result.equals("30")) {
								Toast.makeText(GroupPurchaseActivity.this,
										"已是最后一页", Toast.LENGTH_SHORT).show();
							} else if (result.equals("0")) {
								ArrayList<GroupPurchase> list = (ArrayList<GroupPurchase>) JSONArray
										.parseArray(top.toJSONString(),
												GroupPurchase.class);
								for (GroupPurchase groupPurchase : list) {
									data.add(groupPurchase);
								}
								adapter.setData(data);
								adapter.notifyDataSetChanged();
								pages++;
							} else {
								Toast.makeText(GroupPurchaseActivity.this,
										"服务器在打盹", Toast.LENGTH_SHORT).show();
							}
						}
						is = true;
						hideProgress();
					}

				});
			}
		}
	}

	public void initmPopupWindowView() {
		// 获取自定义布局文件pop.xml的视图
		View customView = getLayoutInflater().inflate(R.layout.pop_listview,
				null, false);
		WindowManager wm = (WindowManager) GroupPurchaseActivity.this
				.getSystemService(Context.WINDOW_SERVICE);

		int width = wm.getDefaultDisplay().getWidth();// 获取屏幕的宽度

		// 创建PopupWindow实例,200,150分别是宽度和高度
		popupwindow = new PopupWindow(customView, width / 2,
				LayoutParams.WRAP_CONTENT);
		// 自定义view添加触摸事件
		popupwindow.setFocusable(true);
		customView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (popupwindow != null && popupwindow.isShowing()) {
					popupwindow.dismiss();
					popupwindow = null;
				}
				return false;
			}
		});
		/** 在这里可以实现自定义视图的功能 */
		popListView = (ListView) customView.findViewById(R.id.pop_listview);
		popListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (type == 1) {
					goodsType = list.get(position).getIdkey();
					tv02.setText(list.get(position).getCaption());
					getData();
					popupwindow.dismiss();
				} else {
					sortRule = position + 1;
					tv03.setText(listA.get(position).getCaption());
					getData();
					popupwindow.dismiss();
				}
			}
		});
		if (type == 1) {
			String path = Path.SERVER_ADRESS + "admission/typeList.htm";
			HttpUtil.post(path, null, new AsyncHttpResponseHandler() {

				@Override
				public void onFailure(Throwable arg0, String arg1) {
					Toast.makeText(getApplicationContext(), "请检查您的网络",
							Toast.LENGTH_LONG).show();
					super.onFailure(arg0, arg1);
				}

				@Override
				public void onSuccess(String response) {
					super.onSuccess(response);
					if (response.equals("") || response == null) {
						Toast.makeText(GroupPurchaseActivity.this, "服务器在打盹",
								Toast.LENGTH_SHORT).show();
					} else {
						JSONObject object = JSONObject.parseObject(response);
						JSONArray top = object.getJSONArray("records");
						String result = object.getString("result");
						if (top.equals("") || top == null || result.equals("4")) {
							Toast.makeText(GroupPurchaseActivity.this,
									"服务器在打盹", Toast.LENGTH_SHORT).show();
						} else if (result.equals("30")) {
							Toast.makeText(GroupPurchaseActivity.this,
									"已是最后一页", Toast.LENGTH_SHORT).show();
						} else {
							list = (ArrayList<Pop>) JSONArray.parseArray(
									top.toJSONString(), Pop.class);
							PopAdapter popAdapter = new PopAdapter(list,
									GroupPurchaseActivity.this);
							popListView.setAdapter(popAdapter);
						}
					}
				}
			});
		} else {
			Pop pop1 = new Pop();
			pop1.setCaption("更新时间");
			Pop pop2 = new Pop();
			pop2.setCaption("离我最近");
			Pop pop3 = new Pop();
			pop3.setCaption("价格最低");
			Pop pop4 = new Pop();
			pop4.setCaption("销量最好");
			listA = new ArrayList<Pop>();
			listA.add(pop1);
			listA.add(pop2);
			listA.add(pop3);
			listA.add(pop4);
			PopAdapter popAdapter = new PopAdapter(listA,
					GroupPurchaseActivity.this);
			popListView.setAdapter(popAdapter);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.grouppurchase_iv:
			onBackPressed();
			break;
		case R.id.grouppurchase_tv_02:
			if (popupwindow != null && popupwindow.isShowing()) {
				popupwindow.dismiss();
				return;
			} else {
				type = 1;
				initmPopupWindowView();
				popupwindow.showAsDropDown(v, 0, 0);
			}
			break;
		case R.id.grouppurchase_tv_03:
			if (popupwindow != null && popupwindow.isShowing()) {
				popupwindow.dismiss();
				return;
			} else {
				type = 2;
				initmPopupWindowView();
				popupwindow.showAsDropDown(v, 0, 0);
			}
			break;
		case R.id.search_btn:
			if (searchpopupwindow != null && searchpopupwindow.isShowing()) {
				searchpopupwindow.dismiss();
				return;
			} else {
				initmSearchPopupWindowView();
				searchpopupwindow.showAsDropDown(v, 0, 0);
			}
			break;
		}
	}

	// 定位
	public void myLBS() {
		LocationClient client = new LocationClient(GroupPurchaseActivity.this);

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

	public void initmSearchPopupWindowView() {
		// 获取自定义布局文件pop.xml的视图
		View customView = getLayoutInflater().inflate(
				R.layout.searchpopuwindowlayout, null, false);
		WindowManager wm = (WindowManager) GroupPurchaseActivity.this
				.getSystemService(Context.WINDOW_SERVICE);

		int width = wm.getDefaultDisplay().getWidth();// 获取屏幕的宽度

		// 创建PopupWindow实例,200,150分别是宽度和高度
		searchpopupwindow = new PopupWindow(customView, width,
				LayoutParams.MATCH_PARENT);
		// 自定义view添加触摸事件
		searchpopupwindow.setFocusable(true);
		searchpopupwindow.setBackgroundDrawable(new PaintDrawable());  
		customView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (searchpopupwindow != null && searchpopupwindow.isShowing()) {
					searchpopupwindow.dismiss();
					searchpopupwindow = null;
				}
				return false;
			}
		});

		btnSearch = (Button) customView.findViewById(R.id.btnSearch);
		etSearch = (EditText) customView.findViewById(R.id.etSearch);
		ivDeleteText = (ImageView) customView.findViewById(R.id.ivDeleteText);
		searchpopupwindow_gridview = (PullToRefreshGridView) customView
				.findViewById(R.id.searchpopupwindow_gridview);
		searchpopupwindow_gridview.setOnItemClickListener(this);
		searchpopupwindow_gridview.setOnRefreshListener(this);
		searchpopupwindow_gridview.setOnLastItemVisibleListener(this);
		etSearch.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			public void afterTextChanged(Editable s) {
				if (s.length() == 0) {
					ivDeleteText.setVisibility(View.GONE);
				} else {
					ivDeleteText.setVisibility(View.VISIBLE);
				}
			}
		});
		ivDeleteText.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				etSearch.setText("");
			}
		});
		btnSearch.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				getSearchData();
			}
		});
	}

	// 获取搜索数据
	private void getSearchData() {

		showProgress();
		RequestParams params = new RequestParams();
		params.put("title", etSearch.getText().toString().trim());
		params.put("areaType", UesrInfo.areaType);
		params.put("area", UesrInfo.area.substring(0, 3));
		params.put("type", "" + goodsType);
		params.put("order", "" + sortRule);
		params.put("latitude", "" + latitude + "," + longitude);
		params.put("pages", "1");
		String path = Path.SERVER_ADRESS + "admission/findAdmissionList.htm";
		HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				super.onFailure(arg0, arg1);
				count++;
				if (count < 3) {
					getData();
				} else {
					count = 0;
					Toast.makeText(getApplicationContext(), "请检查您的网络",
							Toast.LENGTH_LONG).show();
				}
				hideProgress();
			}

			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				if (response.equals("") || response == null) {
					Toast.makeText(GroupPurchaseActivity.this, "服务器在打盹",
							Toast.LENGTH_SHORT).show();
				} else {
					JSONObject object = JSONObject.parseObject(response);
					JSONArray top = object.getJSONArray("records");
					String result = object.getString("result");
					if (top.equals("") || top == null || result.equals("4")) {
						Toast.makeText(GroupPurchaseActivity.this, "服务器在打盹",
								Toast.LENGTH_SHORT).show();
					} else if (result.equals("30")) {
						searchpopupwindow_gridview.setAdapter(null);
						Toast.makeText(GroupPurchaseActivity.this, "暂无该类商品",
								Toast.LENGTH_SHORT).show();
					} else if (result.equals("0")) {
						searchdata = (ArrayList<GroupPurchase>) JSONArray
								.parseArray(top.toJSONString(),
										GroupPurchase.class);
						if (searchdata.size() == 0) {
							searchpopupwindow_gridview.setAdapter(null);
						} else {
							searchadapter = new GroupPurchaseAdapter(
									searchdata, GroupPurchaseActivity.this);
							searchpopupwindow_gridview
									.setAdapter(searchadapter);
							searchpopupwindow_gridview.onRefreshComplete();
							searchpages = 2;
						}

					} else {
						searchpopupwindow_gridview.setAdapter(null);
						if (searchadapter != null) {
							searchadapter.setData(null);
						}
						Toast.makeText(GroupPurchaseActivity.this, "服务器在打盹",
								Toast.LENGTH_SHORT).show();
					}
				}
				hideProgress();
			}
		});
	}

	// 刷新搜索列表
	public void refreData() {
		showProgress();
		RequestParams params = new RequestParams();
		params.put("title", etSearch.getText().toString().trim());
		params.put("type", "" + goodsType);
		params.put("order", "" + sortRule);
		params.put("areaType", UesrInfo.areaType);
		params.put("area", UesrInfo.area.substring(0, 3));
		params.put("latitude", "" + latitude + "," + longitude);
		params.put("pages", "" + searchpages);
		String path = Path.SERVER_ADRESS + "admission/findAdmissionList.htm";
		if (searchis) {
			searchis = false;
			HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

				@Override
				public void onFailure(Throwable arg0, String arg1) {
					Toast.makeText(getApplicationContext(), "请检查您的网络",
							Toast.LENGTH_LONG).show();
					super.onFailure(arg0, arg1);
					hideProgress();
					searchis = true;
				}

				@Override
				public void onSuccess(String response) {
					super.onSuccess(response);
					if (response.equals("") || response == null) {
						Toast.makeText(GroupPurchaseActivity.this, "服务器在打盹",
								Toast.LENGTH_SHORT).show();
					} else {
						JSONObject object = JSONObject.parseObject(response);
						JSONArray top = object.getJSONArray("records");
						String result = object.getString("result");
						if (top.equals("") || top == null || result.equals("4")) {
							Toast.makeText(GroupPurchaseActivity.this,
									"服务器在打盹", Toast.LENGTH_SHORT).show();
						} else if (result.equals("30")) {
							Toast.makeText(GroupPurchaseActivity.this,
									"已是最后一页", Toast.LENGTH_SHORT).show();
						} else if (result.equals("0")) {
							ArrayList<GroupPurchase> list = (ArrayList<GroupPurchase>) JSONArray
									.parseArray(top.toJSONString(),
											GroupPurchase.class);
							for (GroupPurchase groupPurchase : list) {
								searchdata.add(groupPurchase);
							}
							searchadapter.setData(searchdata);
							searchadapter.notifyDataSetChanged();
							searchpages++;
						} else {
							Toast.makeText(GroupPurchaseActivity.this,
									"服务器在打盹", Toast.LENGTH_SHORT).show();
						}
					}
					searchis = true;
					hideProgress();
				}

			});
		}
	}

}
