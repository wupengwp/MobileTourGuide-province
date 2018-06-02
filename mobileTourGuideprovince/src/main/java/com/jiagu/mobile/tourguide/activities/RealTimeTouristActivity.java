/**
 *@项目名称: 手机导游2.0
 *@Copyright: ${year} www.jiagu.com Inc. All rights reserved.
 *注意：本内容仅限于西安甲骨企业文化传播有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
package com.jiagu.mobile.tourguide.activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.activities.bases.TitleDrawerActivity;
import com.jiagu.mobile.tourguide.bean.ScenicSpot;
import com.jiagu.mobile.tourguide.utils.AudioPlayer;
import com.jiagu.mobile.tourguide.utils.AudioPlayerCallback;
import com.jiagu.mobile.tourguide.utils.FileTools;
import com.jiagu.mobile.tourguide.utils.HttpUtil;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.tourguide.utils.UesrInfo;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
/**
 * @Project: Maplocation
 * @Author: 徐进涛
 * @Date: 2014年12月29日
 */
public class RealTimeTouristActivity extends TitleDrawerActivity implements
		OnMapStatusChangeListener, OnMarkerClickListener, OnMapClickListener,
		AudioPlayerCallback {
	private static final String SCENIC_SPOTS_URL = "realGuide/distance.htm";// 获取景点信息的请求地址
	private static final float MAX_LEVEL = 19.0f; // 百度地图最大缩放级别
	private static final float INIT_LEVEL = 14.0f; // 初始化百度地图缩放级别

	private static final int[] SCALES = { 2000000, 2000000, 2000000, 1000000,
			500000, 200000, 100000, 50000, 25000, 20000, 10000, 5000, 2000,
			1000, 500, 200, 100, 50, 20 }; // 各级比例尺分母值数组
	private static final String[] SCALE_DESCS = { "2000公里", "2000公里", "2000公里",
			"1000公里", "500公里", "200公里", "100公里", "50公里", "25公里", "20公里",
			"10公里", "5公里", "2公里", "1公里", "500米", "200米", "100米", "50米", "20米" }; // 各级比例尺上面的文字数组

	TextView mFirstTourist = null;
	TextView mRealTourist = null;
	TextView mPreTourist = null;

	// 地图和定位相关成员变量
	private MapView mMapView = null;
	private BaiduMap mBaiduMap = null;
	private ArrayList<Marker> mMarkers = null;
	private ArrayList<ScenicSpot> mScenicSpots = null;
	private LocationClient mLocClient = null;
	private RealTimeLocationListenner mRealTimeLocationListenner = null;
	private float mCurLevel = 0f;
	private int mCount = 0;
	private boolean mIsGetting = false;

	// 播放器相关成员变量
	private View mLoadProgress;// 加载进度
	private AudioPlayer mPlayer = null;
	private boolean mIsFirstLoc = true;

	// 触发onMapStatusChange的消息
	private static final int MAPCHANGE_CENTER_MARK = 1; // 地图mark居中
	private static final int MAPCHANGE_MOVE_MAP = 2; // 地图移动
	private static final int MAPCHANGE_NULL_MAP = 0; // 其他原因

	Marker mActiveMarker = null;// 当前正在播放的景点
//	LatLng mLatLng = null;// 当前地图中心位置的经纬度
	LatLng mMyLatLng = null;// 用户位置经纬度
	LatLng mLastCenter = null;// 上次请求获取景点信息时的地图中心经纬度
	double mLastLevel = 0;// 上次请求获取景点信息时的地图比例尺
	boolean mIsMarkerClick = false;

	BitmapDescriptor mActiveIcon = null;
	BitmapDescriptor mMarkIcon = null;
	BitmapDescriptor mTmpIcon = null;

	boolean mIsNetOK = true;// 网络是否正常
	boolean mIsScreenOn = true;// 是否开屏
	boolean mIsHome = false;// 是否按了home键

	IntentFilter mFilter = null;// 系统监听消息过滤
	SDKReceiver mReceiver = null;// 系统消息receiver
	long mPauseTime = 0;// 暂停播放时间
	long mPressHomeKyeTime = 0;// 按下home键时间

	ArrayList<MotionEvent> mEventList = null;// 触屏事件记录列表
	
	static int requestCount = 0;

	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// FileTools.writeLog("tourist.txt", "onCreate");
		// 初始化窗口
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_real_time_tourist);

		mLoadProgress = findViewById(android.R.id.progress);// 初始化语音文件进度条资源

		initBaiduMap();// 初始化地图
		mPlayer = new AudioPlayer();
		if (mMapView != null) {
			mPlayer.create((AudioPlayerCallback) RealTimeTouristActivity.this,
					mLoadProgress);
		}

		createLocationClient();// 创建位置定位

		mMarkIcon = BitmapDescriptorFactory.fromResource(R.drawable.mark);// 加载景点图标
		mActiveIcon = BitmapDescriptorFactory
				.fromResource(R.drawable.activemark);// 加载景点图标

		// 初始化实时导游状态提示
		mFirstTourist = (TextView) findViewById(R.id.tourist_state_first);

		mRealTourist = (TextView) findViewById(R.id.tourist_state_realtour);
		mRealTourist.setTextColor(Color.RED);

		mPreTourist = (TextView) findViewById(R.id.tourist_state_pretour);
		View line = (View) findViewById(R.id.separate_line);

		if (!UesrInfo.areaType.equals("4")) {
			mPreTourist.setVisibility(View.GONE);
			line.setVisibility(View.GONE);
		}

		mFirstTourist.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mRealTourist.setTextColor(Color.WHITE);
				Intent i = new Intent();
				i.setClass(getActivity(), FirstTouristActivity.class);
				startActivity(i);

				finish();
			}
		});

		mRealTourist.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});

		mPreTourist.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mRealTourist.setTextColor(Color.WHITE);

				Intent i = new Intent();
				i.setClass(getActivity(), PreTouristActivity.class);
				startActivity(i);				
				finish();
			}
		});

		// 初始化Markers相关变量
		mMarkers = new ArrayList<Marker>();
		mScenicSpots = new ArrayList<ScenicSpot>();
		//

		mFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
		mFilter.addAction(Intent.ACTION_SCREEN_ON);
		mFilter.addAction(Intent.ACTION_SCREEN_OFF);
		// filter.addAction(Intent.ACTION_USER_PRESENT);
		mReceiver = new SDKReceiver();
		registerReceiver(mReceiver, mFilter);

		mEventList = new ArrayList<MotionEvent>();

		mBaiduMap
				.setMyLocationConfigeration(new MyLocationConfiguration(
						com.baidu.mapapi.map.MyLocationConfiguration.LocationMode.FOLLOWING,
						true, null));
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(MAX_LEVEL));
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(mMyLatLng);
		mBaiduMap.animateMapStatus(u);

	}

	private void createLocationClient() {
		LocationClient mLocClient = new LocationClient(getApplicationContext());
		LocationClientOption options = new LocationClientOption();

		options.setLocationMode(LocationMode.Hight_Accuracy);
		options.setCoorType("bd09ll");
		options.setIsNeedAddress(true);
		options.setNeedDeviceDirect(true);
		options.setOpenGps(true);
		options.setScanSpan(1000);// 每10秒扫描一次
		mLocClient.setLocOption(options);
		mLocClient.start();
		mLocClient.requestLocation();
		mRealTimeLocationListenner = new RealTimeLocationListenner();
		mLocClient.registerLocationListener(mRealTimeLocationListenner);
	}

	private void destoryLocationClient() {
		if (mLocClient != null) {
			if (mLocClient.isStarted()) {
				mLocClient
						.unRegisterLocationListener(mRealTimeLocationListenner);
				mRealTimeLocationListenner = null;
			}
			mLocClient.stop();// 关闭定位
			mLocClient = null;
		}
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	protected void onPause() {
		PowerManager manager = (PowerManager) getSystemService(Activity.POWER_SERVICE);
		if (manager.isScreenOn()) {
			// 没有关屏
			mPauseTime = System.currentTimeMillis();// 记录处理时间
			SendMessage(MSG_PAUSE_PLAYER);
		}

		super.onPause();
		mMapView.onPause();
	}

	@Override
	protected void onStop() {
		// FileTools.writeLog("realtime.txt", "onStop");
		super.onStop();
	}

	@Override
	protected void onResume() {
		super.onResume();
//		setAppName(UesrInfo.area);
		appName.setText(UesrInfo.area + "手机导游");

		mMapView.onResume();

		boolean mIsNetOK = true;

		mIsHome = false;
		mIsScreenOn = true;
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onDestroy() {
		destoryLocationClient();
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();// 销毁百度地图

		// 销毁播放器
		if (mPlayer != null) {
			mPlayer.destory();
		}

		if (mEventList.size() > 0) {
			mEventList.clear();
		}
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		// 处理点击上面的界面视图
		int[] location = new int[2];

		try {
			mMapView.getLocationOnScreen(location);// 获取在整个屏幕内的绝对坐标
			if (location[1] > event.getY()) {
				if (mPlayer.isShow() == true) {
					// 播放器已显示了
					hidePlayer();
				}
			}

			// 处理从左侧滑动
			MotionEvent ev = event.obtain(event);
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				if (mEventList.size() > 0) {
					mEventList.clear();
				}

				mEventList.add(ev);
			} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
				mEventList.add(ev);
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				mEventList.add(ev);

				if (mEventList.size() > 4 && mEventList.get(0).getX() == 0.0
						&& mEventList.get(mEventList.size() - 1).getX() > 1.0f) {
					if (mPlayer.isShow() == true) {
						// 播放器已显示了
						hidePlayer();
					}
				}

				mEventList.clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return super.dispatchTouchEvent(event);
	}

	// 标记景点
	private void markScenicSpots(ArrayList<ScenicSpot> scenicSpots) {
		// FileTools.writeLog("map.txt", "markScenicSpots");
		// 设置mMarkers
		try {
			if (mBaiduMap != null && mBaiduMap.getMapStatus() != null) {
				float zoom = mBaiduMap.getMapStatus().zoom;

				clearMarkers();

				// FileTools.writeLog("realtime.txt",
				// "spots size:"+scenicSpots.size());

				for (int i = 0; i < scenicSpots.size(); i++) {

					if (scenicSpots.get(i).level - 0.5f <= zoom) {
						// 判断是否已经加入到mark中
						boolean isMarked = false;
						for (int j = 0; j < mMarkers.size(); j++) {
							if (scenicSpots.get(i) == mMarkers.get(j)
									.getExtraInfo().get("scenicSpot")) {
								isMarked = true;
								break;
							}
						}
						if (isMarked == false) {
							// 已经加过就不再加了
							LatLng latLng = new LatLng(
									scenicSpots.get(i).latitude,
									scenicSpots.get(i).longitude);
							// OverlayOptions option = new
							// MarkerOptions().position(latLng).icon(mMarkIcon);

							Bitmap bmp = createDrawable(
									scenicSpots.get(i).name,
									R.drawable.baidumark);
							BitmapDescriptor markicon = BitmapDescriptorFactory
									.fromBitmap(bmp);
							OverlayOptions option = new MarkerOptions()
									.position(latLng).icon(markicon);
							Marker marker = (Marker) (mBaiduMap
									.addOverlay(option));

							// setTextMarker(latLng, scenicSpots.get(i).name);

							// FileTools.writeLog("realtime.txt",
							// "marker景区:"+scenicSpots.get(i).name);

							// //区域或道路景点处理
							// ArrayList<LatLng> pts = new ArrayList<LatLng>();
							// if (scenicSpots.get(i).points != null){
							// for (LatLng pt:scenicSpots.get(i).points){
							// pts.add(new LatLng(pt.latitude, pt.longitude));
							// }
							// }
							//
							// //景点类型为区域
							// if (scenicSpots.get(i).attrctType == 2){
							// if (pts.size()>0){
							// OverlayOptions polygonOption = new
							// PolygonOptions().points(pts).stroke(new Stroke(5,
							// 0xAA00FF00)).fillColor(0xAAFFFF00);
							// scenicSpots.get(i).ol=mBaiduMap.addOverlay(polygonOption);
							// //增加区域图层
							// }
							// }
							//
							// //景点类型为道路
							// else if (scenicSpots.get(i).attrctType == 3){
							// if (pts.size()>0){
							// OverlayOptions polylineOption = new
							// PolylineOptions().points(pts).color(0xAAFF0033).width(10);
							// scenicSpots.get(i).ol=mBaiduMap.addOverlay(polylineOption);//增加道路图层
							// }
							// }
							//
							Bundle bundle = new Bundle();
							bundle.putSerializable("scenicSpot",
									scenicSpots.get(i));
							marker.setExtraInfo(bundle);
							marker.setVisible(true);
							mMarkers.add(marker);

						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			FileTools.writeLog("realtime.txt",
					"markScenicSpots：" + e.toString());
		}
	}

	// void setTextMarker(LatLng ll, String text){
	// // OverlayOptions ooText = new TextOptions().bgColor(0x00FFFFFF)
	// // .fontSize(24).fontColor(0xFFFF00FF).text(text).rotate(0)
	// // .position(ll);
	//
	// OverlayOptions ooText = new TextOptions().bgColor(0x00FFFFFF)
	// .fontSize(24).fontColor(0xf0161110).text(text).rotate(0)
	// .position(ll);
	// mBaiduMap.addOverlay(ooText);
	// }

	private Bitmap createDrawable(String title, int resourceid) {
		DisplayMetrics dm = this.getResources().getDisplayMetrics();
		float defaultfontsize = 12;
		float fontsize = defaultfontsize * dm.density;

		Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG
				| Paint.DEV_KERN_TEXT_FLAG);
		textPaint.setTextSize(fontsize);
		textPaint.setTypeface(Typeface.DEFAULT_BOLD); // 采用默认的宽度
		textPaint.setColor(Color.WHITE);
		textPaint.setTextAlign(Align.LEFT);

		float titlewidth = textPaint.measureText(title);

		Bitmap bmp = BitmapFactory.decodeResource(getResources(), resourceid);
		Matrix matrix = new Matrix(); // 矩阵，用于图片比例缩放
		matrix.postScale((float) (titlewidth * 2) / bmp.getWidth(), (float) 1); // 设置高宽比例（三维矩阵）
		Bitmap newBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
				bmp.getHeight(), matrix, true);

		Canvas canvas = new Canvas(newBmp);

		Paint paint = new Paint(); // 建立画笔
		paint.setDither(true);
		paint.setFilterBitmap(true);

		Rect src = new Rect(0, 0, newBmp.getWidth(), newBmp.getHeight());
		Rect dst = new Rect(0, 0, newBmp.getWidth(), newBmp.getHeight());

		canvas.drawBitmap(newBmp, src, dst, paint);

		canvas.drawText(title, newBmp.getWidth() / 2 - titlewidth / 2,
				newBmp.getHeight() / 2, textPaint);

		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();

		return newBmp;
	}

	// 清除景点标识
	private void clearMarkers() {
		if (mActiveMarker != null) {
			mActiveMarker = null;
		}

		if (mBaiduMap != null && mMarkers != null && mMarkers.size() > 0) {
			mBaiduMap.clear();// 清除地图上的mark
			mMarkers.clear();
		}
	}

	@Override
	public void onMapStatusChange(MapStatus status) {

	}

	@Override
	public void onMapStatusChangeFinish(MapStatus status) {
		// FileTools.writeLog("realtime.txt",
		// "zoom:"+status.zoom+" mBaiduMap.getMapStatus().zoom"+mBaiduMap.getMapStatus().zoom);

		boolean isShowPlayer = true;// 是否需要显示播放器标准

		int zoom = (int) mBaiduMap.getMapStatus().zoom > 18 ? 18
				: (int) mBaiduMap.getMapStatus().zoom;// 当前的比例尺系数
		// FileTools.writeLog("realtime.txt",
		// "zoom:"+zoom+" mBaiduMap.getMapStatus().zoom:"+mBaiduMap.getMapStatus().zoom);

		int scale = 50 * SCALES[zoom - 2];// 需要的景点范围
		if (scale > 2000000) {
			scale = 2000000;
		} else if (scale < 50000) {
			scale = 50000;
		}

		double distance = DistanceUtil.getDistance(mLastCenter, status.target);
		if ((distance > scale / 2)
				|| Math.abs(mBaiduMap.getMapStatus().zoom - mLastLevel) >= 1.0f) {
			SendMessage(MSG_GET_SCENIC_SPOTS);

			if (mPlayer != null && mPlayer.isShow() == true) {
				hidePlayer();
			}

			mCurLevel = mBaiduMap.getMapStatus().zoom;// 保存最新的地图比例尺
		} else {
			if (mPlayer != null) {
				// 处理播放器位置和显示
				if (mActiveMarker != null) {
					Point point = mBaiduMap.getProjection().toScreenLocation(
							mActiveMarker.getPosition());
					if (mIsMarkerClick == true) {
						if (mPlayer.isShow() == true) {
							// 播放器已显示了
							movePlayer(point);
						} else {
							// 否则显示播放器
							showPlayer(point);
						}
						mIsMarkerClick = false;
					} else {
						if (mPlayer.isShow() == true) {
							// 播放器已显示了
							movePlayer(point);
						}
					}
				}
			}
		}

		mMyLatLng = status.target;// 地图中心位置经纬度值
	}

	@Override
	public void onMapStatusChangeStart(MapStatus status) {
		mIsNetOK = true;
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		mIsMarkerClick = true;
		// 正在播自己,就返回
		if (mActiveMarker == arg0) {
			if (mPlayer.isShow() == false) {
				Point point = mBaiduMap.getProjection().toScreenLocation(
						mActiveMarker.getPosition());
				showPlayer(point);
			}
		} else {
			// 设置激活的图标
			if (mActiveMarker != null) {
				// mActiveMarker.setIcon(mMarkIcon);
				mActiveMarker.setIcon(mTmpIcon);

				// ScenicSpot spot =
				// (ScenicSpot)mActiveMarker.getExtraInfo().get("scenicSpot");
				// if (spot.attrctType==2){
				// //为区域景点
				// if (spot.points.size() != 0){
				// Polygon polygon = (Polygon)spot.ol;
				// polygon.setFillColor(0xAAFFFF00);
				// }
				// }
				// else if (spot.attrctType==3){
				// //为道路
				// if (spot.points.size() != 0){
				// Polyline line = (Polyline)spot.ol;
				// line.setColor(0xAAFF0033);
				// }
				// }
			}

			setActiveMarker(arg0);// 激活mark
			centerActiveMarker();// mark居中，语音播报处理
			playActiveMarker();// 播放语音
		}
		return true;
	}

	// 从服务器获得景点描述信息，向服务器请求需要展示的景点
	private void getScenicSpots(LatLng ll) {
		mIsGetting = true;
		showProgress();

		try {
			mLastCenter = new LatLng(ll.latitude, ll.longitude);
			mLastLevel = mBaiduMap.getMapStatus().zoom;

			int zoom = (int) (mBaiduMap.getMapStatus().zoom);
			int upperLevel = zoom - 2 > 3 ? zoom - 2 : 3;
			int lowerLevel = zoom + 2 < 19 ? zoom + 2 : 19;
			int scale = 25 * SCALES[zoom - 2];
			if (scale > 2000000) {
				scale = 2000000;
			} else if (scale < 50000) {
				scale = 50000;
			}

			RequestParams params = new RequestParams();

			// 设置请求参数，获取景点列表
			// params.put("latitude", String.valueOf(ll.latitude));
			// params.put("longitude", String.valueOf(ll.longitude));
			// params.put("area", "宝鸡市");// 区域名称
			// params.put("areaType", "2");// 区域类型
			params.put(
					"latitude",
					String.valueOf(ll.latitude) + ","
							+ String.valueOf(ll.longitude));// 设置经纬度
			params.put("scale", String.valueOf(scale));// 设置搜索范围
			params.put("lower", String.valueOf(lowerLevel));//
			// params.put("upper", String.valueOf(upperLevel));//
			String serverUrl = Path.SERVER_ADRESS + SCENIC_SPOTS_URL;// 请求地址

			// FileTools.writeLog("realtime.txt",
			// "经纬度:"+ll.longitude+","+ll.latitude+" 景点范围:"+scale+"地图层级下限"+lowerLevel);

			// FileTools.writeLog("realtime.txt", "params:"+params.toString());

			HttpUtil.post(serverUrl, params, new AsyncHttpResponseHandler() {

				@Override
				public void onFailure(Throwable arg0, String arg1) {
					requestCount++;
					
//					FileTools.writeLog("onfailure.txt", "RealTimeTouristActivity::getScenicSpots requestCount="+requestCount+" arg0:"+arg0.toString()+" arg1:"+arg1);
					
					if (requestCount < 3){
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//再请求一次
						getScenicSpots(mMyLatLng);
					}
					else{
						requestCount = 0;
						mIsGetting = false;
						hideProgress();
						mIsNetOK = false;
						showToast("网络异常，请检查您的网络！");
						super.onFailure(arg0, arg1);// 获取景点列表失败，显示失败信息，通常为网络异常，服务器故障
					}
					
//					mIsGetting = false;
//					hideProgress();
//					mIsNetOK = false;
//					showToast("网络异常，请检查您的网络！");
//					super.onFailure(arg0, arg1);// 获取景点列表失败，显示失败信息，通常为网络异常，服务器故障
				}

				@Override
				public void onSuccess(String response) {
					mIsGetting = false;
					hideProgress();
					super.onSuccess(response);

					// FileTools.writeLog("realtime.txt", "实时导游:" + response);

					// 服务器反馈景点列表
					if (response != null && response.length() > 0) {

						// 对返回的景点列表信息解码
						parseScenicSpots(response);

						// 设置景点标志
						if (mScenicSpots.size() > 0 && mBaiduMap != null) {
							SendMessage(MSG_MARK_SCENIC_SPOTS);
						}
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public final static int MSG_MARK_SCENIC_SPOTS = 1;// 景点设置标识消息
	public final static int MSG_GET_SCENIC_SPOTS = 2;// 获取景点信息
	public final static int MSG_PLAY_AUDIO = 3;// 播放导游语音
	public final static int MSG_REAL_TIME_TOURIST = 4;// 实时导游
	public final static int MSG_PAUSE_PLAYER = 5;// onPause消息
	public final static int MSG_ERROR_TIPS = 6;// 错误提示
	public final static int MSG_PROGRESS_GONE = 7;// 关闭加载进度窗口

	public void SendMessage(int msg) {
		Message message = new Message();
		message.arg1 = msg;
		handleProgress.sendMessage(message);
	}

	public void SendMessage(int msgCode, String msg) {
		Bundle data = new Bundle();
		data.putString("msgCode", msg);
		Message message = new Message();
		message.arg1 = msgCode;
		message.setData(data);
		handleProgress.sendMessage(message);
	}

	private void SendMessage(int msg, LatLng ll) {
		Message message = new Message();
		message.arg1 = msg;
		message.obj = ll;
		handleProgress.sendMessage(message);
	}

	// 业务之间消息处理
	Handler handleProgress = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.arg1) {
			case MSG_MARK_SCENIC_SPOTS:
				try {
					markScenicSpots(mScenicSpots);
					// FileTools.writeLog("realtime.txt", "markScenicSpots");
					if (mPlayer.isPlaying() || mPlayer.isShow()) {
						// FileTools.writeLog("realtime.txt", "isPlaying");

						String name = mPlayer.getTitle();
						// FileTools.writeLog("realtime.txt",
						// "Player Title:"+name);

						for (int i = 0; i < mMarkers.size(); i++) {
							// FileTools.writeLog("realtime.txt",
							// "marker:"+((ScenicSpot)mMarkers.get(i).getExtraInfo().get("scenicSpot")).name);
							if (name.equals(((ScenicSpot) mMarkers.get(i)
									.getExtraInfo().get("scenicSpot")).name)) {
								setActiveMarker(mMarkers.get(i));// 设置激活的景点
								// FileTools.writeLog("realtime.txt",
								// "active marker:"+((ScenicSpot)mMarkers.get(i).getExtraInfo().get("scenicSpot")).name);
								break;
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					FileTools.writeLog(
							"realtime.txt",
							"handleMessage MSG_MARK_SCENIC_SPOTS:"
									+ e.toString());
				}
				break;
			case MSG_GET_SCENIC_SPOTS:
				try {
					getScenicSpots(mMyLatLng);
				} catch (Exception e) {
					e.printStackTrace();
					FileTools.writeLog(
							"realtime.txt",
							"handleMessage+MSG_MARK_SCENIC_SPOTS:"
									+ e.toString());
				}

				break;

			case MSG_ERROR_TIPS:
				String errmsg = msg.getData().getString("msgCode");
				showToast(errmsg);
				break;

			case MSG_PROGRESS_GONE:
				mLoadProgress.setVisibility(View.GONE);
				break;

			case MSG_PAUSE_PLAYER:
				try {
					int count = 0;

					// 等待150毫秒
					while (mPressHomeKyeTime == 0 && count < 3) {
						try {
							Thread.sleep(50);
							count++;
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					// FileTools.writeLog("realtime.txt",
					// "mPressHomeKyeTime="+mPressHomeKyeTime+" mPauseTime="+mPauseTime);

					if (mPressHomeKyeTime == 0
							|| Math.abs(mPressHomeKyeTime - mPauseTime) > 150) {
						// 没有按Homekey或者相隔时间太久
						if (mPlayer != null) {
							// FileTools.writeLog("realtime.txt",
							// "invoke pause");
							mPlayer.pause();
						}
					}

					// 重置时间记录
					mPauseTime = 0;
					mPressHomeKyeTime = 0;
				} catch (Exception e) {
					e.printStackTrace();
					FileTools.writeLog("realtime.txt", e.toString());
				}

				break;
			}
		};
	};

	/**
	 * 定位SDK监听函数
	 */
	private class RealTimeLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			mCount++;

			// map view 销毁后不再处理新接收的位置
			if (location == null || mMapView == null || mBaiduMap == null) {
				return;
			}

			if (location.getLocType() == BDLocation.TypeGpsLocation
					|| mCount % 30 == 0 || mIsFirstLoc == true) {

				//FileTools.writeLog("realtime.txt",
				//		"Radius:" + location.getRadius());

				if (location.getLocType() == BDLocation.TypeGpsLocation) {
					//FileTools.writeLog("realtime.txt", "TypeGpsLocation");
				} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
					//FileTools.writeLog("realtime.txt", "TypeNetWorkLocation");
				} else if (location.getLocType() == BDLocation.TypeNetWorkException) {
					//FileTools.writeLog("realtime.txt", "TypeNetWorkException");
				} else if (location.getLocType() == BDLocation.TypeNone) {
					//FileTools.writeLog("realtime.txt", "TypeNone");
				}

				else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
					//FileTools.writeLog("realtime.txt", "TypeOffLineLocation");
				}

				else if (location.getLocType() == BDLocation.TypeOffLineLocationFail) {
					//FileTools.writeLog("realtime.txt",
					//		"TypeOffLineLocationFail");
				}

				else if (location.getLocType() == BDLocation.TypeOffLineLocationNetworkFail) {
					//FileTools.writeLog("realtime.txt",
					//		"TypeOffLineLocationNetworkFail");
				}

				else if (location.getLocType() == BDLocation.TypeServerError) {
					//FileTools.writeLog("realtime.txt", "TypeServerError");
				}
				if (mIsFirstLoc == true || location.getRadius()<100) {
					mIsFirstLoc = false;
					setMyLocation(location);
					
					// 探测附近是否有景点
					double myLat = location.getLatitude();
					double myLon = location.getLongitude();
					mMyLatLng = new LatLng(myLat, myLon);
					if (mScenicSpots.size() == 0 && mIsNetOK == true
							&& mIsGetting == false) {
						SendMessage(MSG_GET_SCENIC_SPOTS);
					}
				}

				if (location.getLocType() == BDLocation.TypeGpsLocation || mCount>90) {
					mCount = 0;
				}
			}
			if (mIsGetting == false && mMyLatLng != null) {
				Marker activeMarker = detectScenicSpot(mMyLatLng.latitude,
						mMyLatLng.longitude);
				if (activeMarker != null) {
					setActiveMarker(activeMarker);
					playActiveMarker();
				}
			}
		}

		private void setMyLocation(BDLocation location) {
			MyLocationData locData = null;
			locData = new MyLocationData.Builder().accuracy(0).direction(100)
					.latitude(location.getLatitude())// 此处设置开发者获取到的方向信息，顺时针0-360
					.longitude(location.getLongitude()).build();

			mBaiduMap.setMyLocationData(locData);
		}

		private Marker detectScenicSpot(double lat, double lon) {
			Marker marker = null;

			List<Integer> listIndex = new ArrayList<Integer>();

			if (mMarkers.size() > 0) {
				for (int i = 0; i < mMarkers.size(); i++) {
					ScenicSpot spot = (ScenicSpot) mMarkers.get(i)
							.getExtraInfo().get("scenicSpot");
					double distance = DistanceUtil.getDistance(new LatLng(lat,
							lon), new LatLng(spot.latitude, spot.longitude));
//					FileTools.writeLog("realtime.txt", "当前位置经纬度:"+lon+","+lat+" 景点名称:"+spot.name+" 景点与当前位置的距离:"+distance+" 景点设置的播报距离:"+spot.broadcastDistance);

					// 处理景区和景点
					if (spot.attrctType == 2 || spot.attrctType == 3) {
						for (LatLng pt : spot.points) {
							distance = DistanceUtil
									.getDistance(new LatLng(lat, lon),
											new LatLng(pt.latitude,
													pt.longitude));
							if (spot.broadcastDistance > 0
									&& distance <= spot.broadcastDistance) {
								listIndex.add(i);
								break;
							}
						}
					} else if (spot.broadcastDistance > 0
							&& distance <= spot.broadcastDistance) {
						listIndex.add(i);
					}
				}
				if (listIndex.size() > 0) {
					int index = (int) listIndex.get(0);
					ScenicSpot spot = (ScenicSpot) mMarkers.get(index)
							.getExtraInfo().get("scenicSpot");
					double minDistance = spot.broadcastDistance;
					int minDistanceIndex = index;

					for (int i = 1; i < listIndex.size(); i++) {
						ScenicSpot tmpSpot = (ScenicSpot) mMarkers
								.get(listIndex.get(i)).getExtraInfo()
								.get("scenicSpot");

						if (minDistance > tmpSpot.broadcastDistance) {
							minDistance = tmpSpot.broadcastDistance;

							// 增加对区域和道路的处理
							if ((tmpSpot.attrctType == 2 || tmpSpot.attrctType == 3)
									&& tmpSpot.lastBroadcastTime != 0) {
								if (listIndex.size() == 1) {
									minDistanceIndex = listIndex.get(i);
								}
							} else {
								minDistanceIndex = listIndex.get(i);
							}
						}
					}

					// 距离小于100米，实时导游
					if (mActiveMarker != mMarkers.get(minDistanceIndex)) {
						// 与景点距离小于100米，存在mark的景区，当前播报的不是最近的景点时，进行语音播报
						try{
							if (mActiveMarker != null) {
								mActiveMarker.setIcon(mTmpIcon);
							}
							marker = mMarkers.get(minDistanceIndex);
						}
						catch(Exception e){
							e.printStackTrace();
						}
					}
				}
			}
			
			if (marker != null){
				ScenicSpot spot = (ScenicSpot) marker.getExtraInfo().get("scenicSpot");
				FileTools.writeLog("realtime.txt", "检查的播报景点:"+spot.name+" 设置播报距离:"+spot.broadcastDistance);
			}

			return marker;
		}

		public void onReceivePoi(BDLocation poiLocation) {

		}
	}

	private void parseScenicSpots(String response) {
		mScenicSpots.clear();

		//FileTools.writeLog("realtime.txt", response);

		if (response != null) {
			try {
				JSONArray jsonObjs = new JSONObject(response)
						.getJSONArray("records");
				if (jsonObjs != null) {
					ArrayList<ScenicSpot> parseArray = new ArrayList<ScenicSpot>();
					for (int i = 0; i < jsonObjs.length(); i++) {
						ScenicSpot spot = new ScenicSpot();

						// spot.scenicID =
						// jsonObjs.getJSONObject(i).getString("attracID");
						// 增加解析支持区域和道路
						String type = jsonObjs.getJSONObject(i).getString(
								"attractType");
						if (type != null && type.length() > 0) {
							spot.attrctType = Integer.valueOf(type);
						}
						if (spot.attrctType == 2 || spot.attrctType == 3) {
							// 2为区域，3为道路
							spot.points = new ArrayList<LatLng>();
							JSONArray points = jsonObjs.getJSONObject(i)
									.getJSONArray("attractLocation");
							for (int j = 0; j < points.length(); j++) {
								double lng = points.getJSONObject(j).getDouble(
										"x");
								double lat = points.getJSONObject(j).getDouble(
										"y");
								spot.points.add(new LatLng(lat, lng));
							}
						}

						if (type == null || type.length() == 0) {
							// 是景区
							spot.id = jsonObjs.getJSONObject(i).getString("id");
							spot.scenicID = jsonObjs.getJSONObject(i)
									.getString("scenicId");
						} else {
							// 是景点
							spot.attracID = jsonObjs.getJSONObject(i)
									.getString("attracID");
						}

						spot.name = jsonObjs.getJSONObject(i)
								.getString("title");

						// 解析经纬度和景点层级
						spot.latitude = jsonObjs.getJSONObject(i)
								.getJSONObject("attracGps").getDouble("y");
						spot.longitude = jsonObjs.getJSONObject(i)
								.getJSONObject("attracGps").getDouble("x");

						spot.level = (float) jsonObjs.getJSONObject(i)
								.getJSONObject("attracGps").getDouble("offset");

						spot.broadcastDistance = (float) jsonObjs
								.getJSONObject(i).getJSONObject("attracGps")
								.getDouble("broadcastDistance");
						if (spot.broadcastDistance < 0.01
								&& spot.broadcastDistance > -0.01f) {
							spot.broadcastDistance = 100.0f;
						}

						spot.audio = Path.VOICE_ADRESS
								+ jsonObjs.getJSONObject(i).getString(
										"voiceurl");
						//
						// // 增加解析支持区域和道路
						// String type = jsonObjs.getJSONObject(i).getString(
						// "attractType");
						// if (type != null && type.length() > 0) {
						// spot.attrctType = Integer.valueOf(type);
						// }
						// if (spot.attrctType == 2 || spot.attrctType == 3) {
						// // 2为区域，3为道路
						// spot.points = new ArrayList<LatLng>();
						// JSONArray points = jsonObjs.getJSONObject(i)
						// .getJSONArray("attractLocation");
						// for (int j = 0; j < points.length(); j++) {
						// double lng = points.getJSONObject(j).getDouble(
						// "x");
						// double lat = points.getJSONObject(j).getDouble(
						// "y");
						// spot.points.add(new LatLng(lat, lng));
						// }
						// }

						mScenicSpots.add(spot);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void initBaiduMap() {
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMyLocationEnabled(true);
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(INIT_LEVEL));

		mCurLevel = mBaiduMap.getMapStatus().zoom;

		setCenter(mMyLatLng);// 将当前位置设置到地图中心

		mBaiduMap.setOnMapClickListener(this);
		mBaiduMap.setOnMarkerClickListener(this);
		mBaiduMap.setOnMapStatusChangeListener(this);
	}

	@Override
	public void onMapClick(LatLng arg0) {

		if (mPlayer != null) {
			if (mActiveMarker != null) {
				if (mPlayer.isShow() == true) {
					hidePlayer();
				} else {
					Point point = mBaiduMap.getProjection().toScreenLocation(
							mActiveMarker.getPosition());
					showPlayer(point);
				}
			} else {
				if (mPlayer.isShow() == true) {
					hidePlayer();
				} else {
					if (true == mPlayer.isPlaying()) {
						Point point = mBaiduMap.getProjection()
								.toScreenLocation(
										mBaiduMap.getMapStatus().target);
						showPlayer(point);
					}
				}
			}
		}
	}

	@Override
	public boolean onMapPoiClick(MapPoi status) {
		return false;
	}

	void setCenter(LatLng ll) {
		// 将景点设置到地图中间位置
		MapStatus status = new MapStatus.Builder().target(ll)
				.zoom((mBaiduMap.getMapStatus()).zoom).build();
		MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(status);
		mBaiduMap.animateMapStatus(u);
	}

	// 设置当前的marker
	private void setActiveMarker(Marker mark) {
		try {
			mTmpIcon = mark.getIcon();

			mActiveMarker = mark;

			ScenicSpot spot = (ScenicSpot) mActiveMarker.getExtraInfo().get(
					"scenicSpot");
			Bitmap bmp = createDrawable(spot.name, R.drawable.baiduactivemark);
			BitmapDescriptor activemarkicon = BitmapDescriptorFactory
					.fromBitmap(bmp);
			mActiveMarker.setIcon(activemarkicon);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// if (mActiveMarker != null){
		// mActiveMarker.setIcon(mActiveIcon);

		// ScenicSpot spot =
		// (ScenicSpot)mActiveMarker.getExtraInfo().get("scenicSpot");
		// if (spot.attrctType==2){
		// //为区域景点
		// if (spot.points.size() != 0){
		// Polygon polygon = (Polygon)spot.ol;
		// polygon.setFillColor(0xAAFF0000);
		// }
		// }
		// else if (spot.attrctType==3){
		// //为道路
		// if (spot.points.size() != 0){
		// Polyline line = (Polyline)spot.ol;
		// line.setColor(0xAA00FF00);
		// }
		// }
		// }
	}

	// 使marker点居中
	void centerActiveMarker() {
		if (mActiveMarker != null) {
			// 将景点设置到地图中间位置
			MapStatus status = new MapStatus.Builder()
					.target(mActiveMarker.getPosition())
					.zoom((mBaiduMap.getMapStatus()).zoom).build();
			MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(status);
			mBaiduMap.animateMapStatus(u); // 触发onMapStatusChangeFished,使marker点居中
		}
	}

	// 播放语音
	private void playActiveMarker() {
		// 显示等待进度
		showProgress();
		ScenicSpot spot = (ScenicSpot) mActiveMarker.getExtraInfo().get(
				"scenicSpot");
		mPlayer.setTitle(spot.name);
		mPlayer.setControlPlay();

		// 加载录音、播放
		new Thread(new Runnable() {
			@Override
			public void run() {
				ScenicSpot spot = (ScenicSpot) mActiveMarker.getExtraInfo()
						.get("scenicSpot");
				boolean initPlayPause = false;
				if (spot.attrctType == 2 || spot.attrctType == 3) {
					if (spot.lastBroadcastTime != 0) {
						initPlayPause = true;
					}
					spot.lastBroadcastTime = System.currentTimeMillis();
				}
				mPlayer.play(spot.audio, initPlayPause);
			}
		}).start();

		if (spot.id != null) {
			// 是景区

			UesrInfo.HttpListen(this, spot.id, UesrInfo.getTourIDid(),
					spot.scenicID, null, null, "0");
		} else {

			// 是景点

			UesrInfo.HttpListen(this, null, UesrInfo.getTourIDid(), null, null,
					spot.attracID, "1");
		}

	}

	// 暂停播放
	private void pausePlayer() {
		if (mPlayer != null) {
			if (mPlayer.isPlaying()) {
				mPlayer.pause();
			}
		}
	}

	// 移动播放器窗口
	public void movePlayer(Point point) {
		Point pt = getPlayerPosition(point);
		// Toast.makeText(getApplicationContext(), "x:"+pt.x+" y:"+pt.y,
		// Toast.LENGTH_SHORT).show();
		mPlayer.move(pt.x, pt.y);// 显示播放器
	}

	// 隐藏播放器
	public void hidePlayer() {
		if (mPlayer != null) {
			mPlayer.hide();
		}
	}

	// 显示播放器
	public void showPlayer(Point point) {
		Point pt = getPlayerPosition(point);
		mPlayer.show(pt.x, pt.y);// 显示播放器
	}

	public Point getPlayerPosition(Point point) {
		int[] location = new int[2];
		int top = 0;
		int bottom = 0;
		int left = 0;
		int right = 0;
		int x = 0;
		int y = 0;
		Point pt = new Point();
		if (point != null) {
			pt.x = point.x;
			pt.y = point.y;
		}

		if (mMapView != null) {
			// 地图窗口的区域
			mMapView.getLocationOnScreen(location);// 获取在整个屏幕内的绝对坐标

			Rect frame = new Rect();
			getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
			int statusBarHeight = frame.top;

			top = location[1];
			bottom = top + mMapView.getHeight();
			left = 0;
			right = left + mMapView.getWidth();
		}

		if (mPlayer.getWidth() > 0 && mPlayer.getHeight() > 0) {
			x = point.x - mPlayer.getWidth() / 2;
			y = point.y - mPlayer.getHeight() / 4;
		} else {
			x = point.x - 100;
			y = point.y - 15;
		}

		if (x < left) {
			x = left + 1;
		} else if (x > right - mPlayer.getWidth() / 2) {
			x = right - mPlayer.getWidth() / 2 - 1;
		}

		if (y < top) {
			y = top + 1;
		} else if (y > bottom - mPlayer.getHeight()) {
			y = bottom - mPlayer.getHeight() - 1;
		}

		pt.x = x;
		pt.y = y;

		return pt;
	}

	//
	public void showProgress() {
		if (mLoadProgress != null) {
			mLoadProgress.setVisibility(View.VISIBLE);
		}
	}

	public void hideProgress() {
		if (mLoadProgress != null) {
			mLoadProgress.setVisibility(View.GONE);
		}
	}

	// 实现AudioPlayerCallback接口
	public void sendErrorTips(String tips) {
		SendMessage(MSG_ERROR_TIPS, tips);
	}

	public void hideLoading() {
		SendMessage(MSG_PROGRESS_GONE);
	}

	public void showLoading() {

	}

	public View getView() {
		return getWindow().getDecorView();
	}

	public Activity getActivity() {
		return this;
	}
	
	public String getX(){
		return null;
	}
	public String getY(){
		return null;
	}
	public String getName(){
		return null;
	}
	
	public boolean isNeedGuide(){
		return false;
	}

	/**
	 * @author Administrator 网络异常
	 * 
	 */
	public class SDKReceiver extends BroadcastReceiver {
		final String SYSTEM_DIALOG_REASON_KEY = "reason";
		final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
		final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action
					.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
				mIsNetOK = false;
				showToast("网络异常，请检查您的网络！");
			}
			// 开关屏的处理
			else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
				mIsScreenOn = false;
			} else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
				mIsScreenOn = true;
			} else if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
				String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
				if (reason != null) {
					if (SYSTEM_DIALOG_REASON_HOME_KEY.equals(reason)) {
						// 短按Home键
						mPressHomeKyeTime = System.currentTimeMillis();// 记录按键时间
						mIsHome = true;
					}
				}
			}
		}
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		destoryLocationClient();
		RealTimeTouristActivity.this.finish();
	}
}