package com.jiagu.mobile.tourguide.fragments;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;

import android.R.string;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.navisdk.BaiduNaviManager;
import com.baidu.nplatform.comapi.basestruct.GeoPoint;
import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.activities.ExerciseActivity;
import com.jiagu.mobile.tourguide.activities.FeatureActivity;
import com.jiagu.mobile.tourguide.activities.FirstTouristActivity;
import com.jiagu.mobile.tourguide.activities.MainActivity;
import com.jiagu.mobile.tourguide.activities.SceneActivity;
import com.jiagu.mobile.tourguide.activities.SceneDetialActivity;
import com.jiagu.mobile.tourguide.activities.StrategyActivity;
import com.jiagu.mobile.tourguide.activities.bases.TitleDrawerActivity;
import com.jiagu.mobile.tourguide.activities.bases.TitleDrawerActivity.OnTitleDrawerListener;
import com.jiagu.mobile.tourguide.adapter.MainAdapter;
import com.jiagu.mobile.tourguide.bean.Area;
import com.jiagu.mobile.tourguide.bean.HomePagerBean;
import com.jiagu.mobile.tourguide.utils.HttpUtil;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.tourguide.utils.UesrInfo;
import com.jiagu.mobile.zhifu.GroupPurchaseActivity;
import com.jiagu.mobile.zhifu.PurchaseActivity;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

//首页fragment
public class HomeFragment extends Fragment implements OnTitleDrawerListener,
		OnClickListener {
	private int windowWidth;
	private TextView mAttractions;
	private HorizontalScrollView mMenu;
	private ImageButton iamgea, iamgeb, iamgec, iamged, iamgee, iamgef;
	private TextView text;
//	 private BroadcastReceiver br;
//	 IntentFilter mFilter = null;// 系统监听消息过滤
	private ArrayList<HomePagerBean> list;

	private ViewPager mViewPager;
	private Context context;
	private ArrayList<Area> arealist;
	public static HomeFragment homeFragment;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		windowWidth = dm.widthPixels;
		context = this.getActivity();
		homeFragment = this;
		return inflater.inflate(R.layout.fragment_home, container, false);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mAttractions = (TextView) getView().findViewById(
				R.id.home_of_late_attractions);
		mMenu = (HorizontalScrollView) getView().findViewById(
				R.id.home_view_pager);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
				windowWidth / 5);
		mMenu.setLayoutParams(lp);
		mViewPager = (ViewPager) getView().findViewById(
				R.id.home_fragment_viewpager);

		mViewPager.setVisibility(View.VISIBLE);
		// int[] data =
		// {R.drawable.bg_zhuye,R.drawable.baojiditu,R.drawable.cuihuashan};
		// HelpAdapter adapter = new HelpAdapter(getActivity(), data);
		// mViewPager.setAdapter(adapter);

		// int[] data =
		// {R.drawable.bg_zhuye,R.drawable.baojiditu,R.drawable.cuihuashan};
		mViewPager.setAdapter(null);

		iamgea = (ImageButton) getView().findViewById(R.id.home_image_but_01);
		iamgeb = (ImageButton) getView().findViewById(R.id.home_image_but_02);
		iamgec = (ImageButton) getView().findViewById(R.id.home_image_but_03);
		iamged = (ImageButton) getView().findViewById(R.id.home_image_but_04);
		iamgee = (ImageButton) getView().findViewById(R.id.home_image_but_05);
		iamgef = (ImageButton) getView().findViewById(R.id.home_image_but_06);
		text = (TextView) getView().findViewById(R.id.home_of_late_attractions);
		iamgef.setVisibility(View.VISIBLE);
		iamgea.setOnClickListener(this);
		iamgeb.setOnClickListener(this);
		iamgec.setOnClickListener(this);
		iamged.setOnClickListener(this);
		iamgee.setOnClickListener(this);
		iamgef.setOnClickListener(this);
		myLBSCity(context, text);
		getCity();

		final FrameLayout mRelativeLayout = (FrameLayout) getView()
				.findViewById(R.id.fragment_home_layout);

		getPhotoId(UesrInfo.photoId);
//		 br = new BroadcastReceiver() {
//		
//		 @Override
//		 public void onReceive(Context context, Intent intent) {
//		 String name = intent.getExtras().getString("area");
//		 String id = intent.getExtras().getString("id");
//		 RequestParams params = new RequestParams(); // 绑定参数
//		 params.put("photoId", id);
//		 HttpUtil.post(Path.SERVER_ADRESS
//		 + "characteristic/photoList.htm", params,
//		 new AsyncHttpResponseHandler() {
//		 @Override
//		 public void onFailure(Throwable arg0, String arg1) {
//		 // TODO Auto-generated method stub
//		 super.onFailure(arg0, arg1);
//		 mViewPager.setVisibility(View.GONE);
//		 }
//		
//		 @Override
//		 public void onSuccess(String response) {
//		 // TODO Auto-generated method stub
//		 super.onSuccess(response);
//		 if (response.equals("") || response == null) {
//		 Toast.makeText(getActivity(), "服务器在打盹",
//		 Toast.LENGTH_SHORT).show();
//		 } else {
//		 JSONObject object = JSONObject
//		 .parseObject(response);
//		 JSONArray top = object
//		 .getJSONArray("records");
//		 String result = object.getString("result");
//		 if (result.equals("0")) {
//		 list = (ArrayList<HomePagerBean>) JSONArray
//		 .parseArray(top.toJSONString(),
//		 HomePagerBean.class);
//		 MainAdapter adapter = new MainAdapter(
//		 getActivity(), list);
//		 mViewPager.setAdapter(adapter);
//		 } else {
//		
//		 }
//		 }
//		 }
//		 });
//		 }
//		
//		 };
//		 mFilter = new IntentFilter();
//		 mFilter.addAction("gaolei");
//		
//		 getActivity().registerReceiver(br, mFilter);
	}

	//切换城市刷新主页图片
	public void getPhotoToo(String id){
	 
		 RequestParams params = new RequestParams(); // 绑定参数
		 params.put("photoId", id);
		 HttpUtil.post(Path.SERVER_ADRESS
		 + "characteristic/photoList.htm", params,
		 new AsyncHttpResponseHandler() {
		 @Override
		 public void onFailure(Throwable arg0, String arg1) {
		 // TODO Auto-generated method stub
		 super.onFailure(arg0, arg1);
		 mViewPager.setVisibility(View.GONE);
		 }
		
		 @Override
		 public void onSuccess(String response) {
		 // TODO Auto-generated method stub
		 super.onSuccess(response);
		 if (response.equals("") || response == null) {
		 Toast.makeText(getActivity(), "服务器在打盹",
		 Toast.LENGTH_SHORT).show();
		 } else {
		 JSONObject object = JSONObject
		 .parseObject(response);
		 JSONArray top = object
		 .getJSONArray("records");
		 String result = object.getString("result");
		 if (result.equals("0")) {
		 list = (ArrayList<HomePagerBean>) JSONArray
		 .parseArray(top.toJSONString(),
		 HomePagerBean.class);
		 MainAdapter adapter = new MainAdapter(
		 getActivity(), list);
		 mViewPager.setAdapter(adapter);
		 } else {
		
		 }
		 }
		 }
		 });
		 
	}
	private void getPhotoId(String id) {
		RequestParams params = new RequestParams(); // 绑定参数
		params.put("photoId", id);
		HttpUtil.post(Path.SERVER_ADRESS + "characteristic/photoList.htm",
				params, new AsyncHttpResponseHandler() {
					@Override
					public void onFailure(Throwable arg0, String arg1) {
						// TODO Auto-generated method stub
						super.onFailure(arg0, arg1);
					}

					@Override
					public void onSuccess(String response) {
						// TODO Auto-generated method stub
						super.onSuccess(response);
						if (response.equals("") || response == null) {
							Toast.makeText(getActivity(), "服务器在打盹",
									Toast.LENGTH_SHORT).show();
						} else {
							JSONObject object = JSONObject
									.parseObject(response);
							JSONArray top = object.getJSONArray("records");
							String result = object.getString("result");
							if (result.equals("0")) {
								list = (ArrayList<HomePagerBean>) JSONArray
										.parseArray(top.toJSONString(),
												HomePagerBean.class);
								MainAdapter adapter = new MainAdapter(
										getActivity(), list);
								mViewPager.setAdapter(adapter);
								mViewPager.setVisibility(View.VISIBLE);
							} else {

							}
						}
					}
				});
	}

	@Override
	public void onTitleDrawerClosed(View arg0) {
		mAttractions.setVisibility(View.VISIBLE);
		mMenu.setVisibility(View.VISIBLE);
	}

	@Override
	public void onTitleDrawerOpened(View arg0) {
		mAttractions.setVisibility(View.INVISIBLE);
		mMenu.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onTitleDrawerSlide(View drawer, float arg1) {
		mMenu.setLeft(drawer.getRight());
	}

	@Override
	public void onTitleDrawerStateChanged(int state) {
	}

	@Override
	public void onClick(View v) {
		Intent i = new Intent();
		switch (v.getId()) {
		case R.id.home_image_but_01:
			i.setClass(getActivity(), FirstTouristActivity.class);
			startActivity(i);
			break;
		case R.id.home_image_but_02:
			if (UesrInfo.areaType.equals("2")) {
				i.setClass(getActivity(), SceneActivity.class);
			} else {
				i.putExtra("id", UesrInfo.sceneId);
				i.putExtra("a", "b");
				i.setClass(getActivity(), SceneDetialActivity.class);
			}
			startActivity(i);
			break;
		case R.id.home_image_but_03:
			i.setClass(getActivity(), FeatureActivity.class);
			startActivity(i);
			break;
		case R.id.home_image_but_04:
			i.setClass(getActivity(), ExerciseActivity.class);
			startActivity(i);
			break;
		case R.id.home_image_but_05:
			i.putExtra("judge", "noMy");
			i.setClass(getActivity(), StrategyActivity.class);
			startActivity(i);
			break;
		case R.id.home_image_but_06:
			i.setClass(getActivity(), GroupPurchaseActivity.class);
			startActivity(i);
			break;

		}
	}

	public void myLBS(Context context, final TextView text) {
		LocationClient client = new LocationClient(
				context.getApplicationContext());
		LocationClientOption options = new LocationClientOption();
		options.setLocationMode(LocationMode.Hight_Accuracy);
		// options.setCoorType("bd09ll");
		options.setCoorType("gcj02");
		options.setIsNeedAddress(true);
		options.setNeedDeviceDirect(true);
		options.setOpenGps(true);
		options.setScanSpan(2000);

		client.setLocOption(options);
		client.registerLocationListener(new BDLocationListener() {
			String addessA = "";
			String addessB = "";
			String currentCity = "";
			String tempcurrentCity = "";

			@Override
			public void onReceiveLocation(BDLocation location) {
				currentCity = location.getCity();
				addessA = location.getStreet();
				UesrInfo.myX = location.getLongitude();// 当前x
				UesrInfo.myY = location.getLatitude();// 当前y
				UesrInfo.at = addessA;

				/*
				 * 定位距离
				 */
				// LatLng latLng1 = new LatLng(location.getLongitude(), location
				// .getLatitude());
				//
				// for (int i = 0; i < arealist.size(); i++) {
				// LatLng latLng2 = new LatLng(Double.parseDouble(arealist
				// .get(i).getX()), Double.parseDouble(arealist.get(i)
				// .getY()));
				// if (getDistance(latLng1, latLng2) <= 2500.0) {
				// System.out.println("111111111");
				// if (!(TitleDrawerActivity.titleDrawerActivity.appName
				// .getText().toString().trim().equals((arealist
				// .get(i).getTitle() + "手机导游").trim()))) {
				// System.out.println("222222222");
				// UesrInfo.area = arealist.get(i).getTitle();
				// TitleDrawerActivity.titleDrawerActivity.appName
				// .setText(arealist.get(i).getTitle()
				// + "手机导游");
				// getPhoto(arealist.get(i).getId());
				// }
				// }
				// }

				if (addessA != null) {
					if (!(addessA.equals(addessB))) {
						addessB = addessA;
						text.setText("您当前位置是:" + currentCity + addessB);
					}
				}
				if (currentCity != null) {
					if (!(currentCity.equals(tempcurrentCity))) {
						tempcurrentCity = currentCity;
						for (int i = 0; i < arealist.size(); i++) {
							if (arealist.get(i).getTitle()
									.equals(tempcurrentCity)) {

								UesrInfo.areaType = arealist.get(i)
										.getAreaType();
								if (arealist.get(i).getAreaType().equals("4")) {
									UesrInfo.area = arealist.get(i).getTitle();
									UesrInfo.sceneId = arealist.get(i)
											.getArea();
								} else {
									UesrInfo.area = arealist.get(i).getTitle();
								}
								UesrInfo.x = arealist.get(i).getX();
								UesrInfo.y = arealist.get(i).getY();

								UesrInfo.abc = "0.0";

								MainActivity.mainActivity.appName
										.setText(tempcurrentCity + "手机导游");

								TitleDrawerActivity.titleDrawerActivity.appName
										.setText(tempcurrentCity + "手机导游");
								getPhoto(arealist.get(i).getId());
							}
						}
					}
				}

			}
		});
		client.start();
		client.requestLocation();
	}

	/**
	 * 计算两点之间距离
	 * 
	 * @param start
	 * @param end
	 * @return 米
	 */
	public double getDistance(LatLng start, LatLng end) {
		double lat1 = (Math.PI / 180) * start.latitude;
		double lat2 = (Math.PI / 180) * end.latitude;

		double lon1 = (Math.PI / 180) * start.longitude;
		double lon2 = (Math.PI / 180) * end.longitude;

		// 地球半径
		double R = 6371;

		// 两点间距离 km，如果想要米的话，结果*1000就可以了
		double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1)
				* Math.cos(lat2) * Math.cos(lon2 - lon1))
				* R;

		return d * 1000;
	}

	// 获取城市列表
	public void getCity() {
		HttpUtil.post(Path.SERVER_ADRESS + "area/areaList.htm", null,
				new AsyncHttpResponseHandler() {
					@Override
					public void onFailure(Throwable arg0, String arg1) {
						// TODO Auto-generated method stub
						super.onFailure(arg0, arg1);
					}

					@Override
					public void onSuccess(String response) {
						// TODO Auto-generated method stub
						super.onSuccess(response);
						if (response.equals("") || response == null) {
							Toast.makeText(context, "服务器在打盹",
									Toast.LENGTH_SHORT).show();
						} else {
							JSONObject object = JSONObject
									.parseObject(response);
							JSONArray top = object.getJSONArray("records");
							String result = object.getString("result");
							if (result.equals("0")) {
								arealist = (ArrayList<Area>) JSONArray
										.parseArray(top.toJSONString(),
												Area.class);
								myLBS(context, text);
							} else {

							}
						}
					}
				});
	}

	// 刷新主页图片
	public void getPhoto(String str) {
		RequestParams params = new RequestParams(); // 绑定参数
		params.put("photoId", str);
		HttpUtil.post(Path.SERVER_ADRESS + "characteristic/photoList.htm",
				params, new AsyncHttpResponseHandler() {
					@Override
					public void onFailure(Throwable arg0, String arg1) {
						// TODO Auto-generated method stub
						super.onFailure(arg0, arg1);
						mViewPager.setVisibility(View.GONE);
					}

					@Override
					public void onSuccess(String response) {
						// TODO Auto-generated method stub
						super.onSuccess(response);
						if (response.equals("") || response == null) {
							Toast.makeText(getActivity(), "服务器在打盹",
									Toast.LENGTH_SHORT).show();
						} else {
							JSONObject object = JSONObject
									.parseObject(response);
							JSONArray top = object.getJSONArray("records");
							String result = object.getString("result");
							if (result.equals("0")) {
								list = (ArrayList<HomePagerBean>) JSONArray
										.parseArray(top.toJSONString(),
												HomePagerBean.class);
								MainAdapter adapter = new MainAdapter(
										getActivity(), list);
								mViewPager.setAdapter(adapter);
							} else {

							}
						}
					}
				});
	}

	/*
	 * 定位城市
	 */
	public void myLBSCity(Context context, final TextView text) {
		LocationClient client = new LocationClient(
				context.getApplicationContext());
		LocationClientOption options = new LocationClientOption();
		options.setLocationMode(LocationMode.Hight_Accuracy);
		// options.setCoorType("bd09ll");
		options.setCoorType("gcj02");
		options.setIsNeedAddress(true);
		options.setNeedDeviceDirect(true);
		options.setOpenGps(true);
		options.setScanSpan(2000);

		client.setLocOption(options);
		client.registerLocationListener(new BDLocationListener() {
			String currentCity = "";
			String currentStreet = "";
			String addessA = "";

			@Override
			public void onReceiveLocation(BDLocation location) {
				currentCity = location.getCity();
				currentStreet = location.getStreet();
				UesrInfo.myX = location.getLongitude();// 当前x
				UesrInfo.myY = location.getLatitude();// 当前y
				UesrInfo.at = currentStreet;

				if (currentStreet != null) {
					if (!(currentStreet.equals(addessA))) {
						addessA = currentStreet;
						text.setText("您当前位置是:" + currentCity + addessA);
					}
				}
			}
		});
		client.start();
		client.requestLocation();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		 getActivity().unregisterReceiver(br);
	}

}
