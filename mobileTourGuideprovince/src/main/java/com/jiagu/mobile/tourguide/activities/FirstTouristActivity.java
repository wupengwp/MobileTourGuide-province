/**
 *@项目名称: 手机导游2.0
 *@Copyright: ${year} www.jiagu.com Inc. All rights reserved.
 *注意：本内容仅限于西安甲骨企业文化传播有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
package com.jiagu.mobile.tourguide.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

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
import android.graphics.Paint.Align;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
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
public class FirstTouristActivity extends TitleDrawerActivity implements
		OnMapStatusChangeListener, OnMarkerClickListener, OnMapClickListener,
		AudioPlayerCallback {
	private static final String SCENIC_SPOTS_URL = "realGuide/distance.htm";// 获取景点信息的请求地址
	private static final float INIT_LEVEL = 14.0f; // 初始化百度地图缩放级别

	private static final int[] SCALES = { 2000000, 2000000, 2000000, 1000000,
			500000, 200000, 100000, 50000, 25000, 20000, 10000, 5000, 2000,
			1000, 500, 200, 100, 50, 20 }; // 各级比例尺分母值数组

	// 地图和定位相关成员变量
	private MapView mMapView = null;
	private BaiduMap mBaiduMap = null;
	private ArrayList<Marker> mMarkers = null;
	private ArrayList<ScenicSpot> mScenicSpots = null;

	private float mCurLevel = 0f;
	private int mCount = 0;
	private boolean mIsGetting = false;

	// 播放器相关成员变量
	private View mLoadProgress;// 加载进度
	private AudioPlayer mPlayer = null;

	// 触发onMapStatusChange的消息
	private static final int MAPCHANGE_CENTER_MARK = 1; // 地图mark居中
	private static final int MAPCHANGE_MOVE_MAP = 2; // 地图移动
	private static final int MAPCHANGE_NULL_MAP = 0; // 其他原因

	Marker mActiveMarker = null;// 当前正在播放的景点
	LatLng mLatLng = null;// 当前地图中心位置的经纬度
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

	TextView mFirstTourist = null;
	TextView mRealTourist = null;
	TextView mPreTourist = null;
	
	static int requestCount = 0;

	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// 初始化窗口
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_real_time_tourist);

		initBaiduMap();// 初始化地图

		mLoadProgress = findViewById(android.R.id.progress);// 初始化语音文件进度条资源

		// 初始化播放器
		mPlayer = new AudioPlayer();
		if (mMapView != null) {
			mPlayer.create((AudioPlayerCallback) FirstTouristActivity.this,
					mLoadProgress);
		}

		mMarkIcon = BitmapDescriptorFactory.fromResource(R.drawable.mark);// 加载景点图标
		mActiveIcon = BitmapDescriptorFactory
				.fromResource(R.drawable.activemark);// 加载景点图标

		mFirstTourist = (TextView) findViewById(R.id.tourist_state_first);
		mFirstTourist.setTextColor(Color.RED);

		mRealTourist = (TextView) findViewById(R.id.tourist_state_realtour);
		mPreTourist = (TextView) findViewById(R.id.tourist_state_pretour);

		View line = (View) findViewById(R.id.separate_line);
		if (!UesrInfo.areaType.equals("4")) {
			mPreTourist.setVisibility(View.GONE);
			line.setVisibility(View.GONE);
		}

		mFirstTourist.setOnClickListener(this);
		mRealTourist.setOnClickListener(this);
		mPreTourist.setOnClickListener(this);

		// 初始化Markers相关变量
		mMarkers = new ArrayList<Marker>();
		mScenicSpots = new ArrayList<ScenicSpot>();

		// 监控消息
		mFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
		mFilter.addAction(Intent.ACTION_SCREEN_ON);
		mFilter.addAction(Intent.ACTION_SCREEN_OFF);
		mReceiver = new SDKReceiver();
		registerReceiver(mReceiver, mFilter);

		mEventList = new ArrayList<MotionEvent>();

		// 通知获取景点
		SendMessage(MSG_GET_SCENIC_SPOTS);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 登录
		case R.id.tourist_state_first:
			break;

		case R.id.tourist_state_realtour:
			switchToRealTimeActivity();
			break;
		case R.id.tourist_state_pretour:
			switchToPreTouristActivity();
			break;
		}

	}

	// 切换到实时导游窗口
	public void switchToRealTimeActivity() {
		mFirstTourist.setTextColor(Color.WHITE);
		Intent i = new Intent();
		i.setClass(getActivity(), RealTimeTouristActivity.class);
		startActivity(i);

		finish();
	}

	// 切换到预游览窗口
	public void switchToPreTouristActivity() {
		mFirstTourist.setTextColor(Color.WHITE);

		Intent i = new Intent();
		i.setClass(getActivity(), PreTouristActivity.class);
		startActivity(i);

				
		finish();
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
		super.onStop();
	}

	@Override
	protected void onResume() {
		super.onResume();
//		setAppName(UesrInfo.area);
		appName.setText(UesrInfo.area + "手机导游");
		mMapView.onResume();
		mIsNetOK = true;
		mIsHome = false;
		mIsScreenOn = true;
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onDestroy() {
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
		// 设置mMarkers
		try {
			if (mBaiduMap != null && mBaiduMap.getMapStatus() != null) {
				clearMarkers();

				float zoom = mBaiduMap.getMapStatus().zoom;
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
							// MarkerOptions().position(latLng).icon(mMarkIcon).title(scenicSpots.get(i).name).perspective(true);

							Bitmap bmp = createDrawable(
									scenicSpots.get(i).name,
									R.drawable.baidumark);
							// Bitmap bmp =
							// createDrawable(scenicSpots.get(i).name,
							// R.drawable.biao);
							BitmapDescriptor markicon = BitmapDescriptorFactory
									.fromBitmap(bmp);
							OverlayOptions option = new MarkerOptions()
									.position(latLng).icon(markicon)
									.visible(false);
							Marker marker = (Marker) (mBaiduMap
									.addOverlay(option));
							// marker.setVisible(false);

							// setTextInfoWindow(latLng,
							// scenicSpots.get(i).name);

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

							Bundle bundle = new Bundle();
							bundle.putSerializable("scenicSpot",
									scenicSpots.get(i));
							marker.setExtraInfo(bundle);
							// marker.setVisible(true);
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

		int[] location = new int[2];
		mMapView.getLocationOnScreen(location);
		int width = mMapView.getWidth();
		int height = mMapView.getHeight();

		for (int i = 0; i < mMarkers.size(); i++) {
			Marker marker = mMarkers.get(i);

			Point point = mBaiduMap.getProjection().toScreenLocation(
					marker.getPosition());

			if (point.x < location[0] || point.x > location[0] + width
					|| point.y < location[1] || point.y > location[1] + height) {
				marker.setVisible(false);
			} else {
				marker.setVisible(true);
			}
		}
	}

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
		boolean isShowPlayer = true;// 是否需要显示播放器标准

		int zoom = (int) mBaiduMap.getMapStatus().zoom > 18 ? 18
				: (int) mBaiduMap.getMapStatus().zoom;// 当前的比例尺系数

		int scale = 50 * SCALES[zoom - 2];// 需要的景点范围
		
		//FileTools.writeLog("realtime.txt", "scale="+scale);
		if (scale > 2000000) {
			scale = 2000000;
		} else if (scale < 50000) {
			scale = 50000;
		}

		double distance = DistanceUtil.getDistance(mLastCenter, status.target);
		
		//FileTools.writeLog("realtime.txt", "distance="+distance+" scale="+scale);
		
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

		mLatLng = status.target;// 地图中心位置经纬度值

		int[] location = new int[2];
		mMapView.getLocationOnScreen(location);
		int width = mMapView.getWidth();
		int height = mMapView.getHeight();

		for (int i = 0; i < mMarkers.size(); i++) {
			Marker marker = mMarkers.get(i);

			Point point = mBaiduMap.getProjection().toScreenLocation(
					marker.getPosition());
			
//			 ScenicSpot spot =
//			 (ScenicSpot)marker.getExtraInfo().get("scenicSpot");
			
			
//			FileTools.writeLog("realtime.txt", "name:"+spot.name
//					+" point.x="+point.x+" point.y="+point.y
//					+" location[0]="+location[0]+" location[1]="+location[1]
//							+" width="+width+" height="+height);

			if (point.x < location[0]-100 || point.x > location[0] + width+100
					|| point.y < location[1]-100 || point.y > location[1] + height+100) {
				marker.setVisible(false);
			} else {
				marker.setVisible(true);
			}
		}

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
				// mTmpIcon = arg0.getIcon();

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

		mLastCenter = new LatLng(ll.latitude, ll.longitude);

		try {

			mLastLevel = mBaiduMap.getMapStatus().zoom;

			int zoom = (int) (mBaiduMap.getMapStatus().zoom);
			// int upperLevel = zoom-2>3?zoom-2:3;
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
			
			//FileTools.writeLog("realtime.txt", "scale="+scale+ "lowerLevel="+lowerLevel);
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
					
					FileTools.writeLog("onfailure.txt", "FirstTouristActivity::getScenicSpots requestCount="+requestCount+" arg0:"+arg0.toString()+" arg1:"+arg1);
					
					if (requestCount < 3){
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						//再请求一次
						getScenicSpots(mLatLng);
						
						
					}
					else{
						requestCount = 0;
						mIsGetting = false;
						hideProgress();
						mIsNetOK = false;
						showToast("网络异常，请检查您的网络！");
						super.onFailure(arg0, arg1);// 获取景点列表失败，显示失败信息，通常为网络异常，服务器故障
					}
				}

				@Override
				public void onSuccess(String response) {
					mIsGetting = false;
					hideProgress();
					super.onSuccess(response);

					//FileTools.writeLog("realtime.txt", "导游首页：" + response);

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

	// ////////////////////////////////////////////////////////////////////////////////
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
					if (mPlayer.isPlaying() || mPlayer.isShow()) {
						String name = mPlayer.getTitle();
						for (int i = 0; i < mMarkers.size(); i++) {
							if (name.equals(((ScenicSpot) mMarkers.get(i)
									.getExtraInfo().get("scenicSpot")).name)) {

								mTmpIcon = mMarkers.get(i).getIcon();
								setActiveMarker(mMarkers.get(i));// 设置激活的景点

								break;
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case MSG_GET_SCENIC_SPOTS:
				try {
					getScenicSpots(mLatLng);
				} catch (Exception e) {
					e.printStackTrace();
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

					if (mPressHomeKyeTime == 0
							|| Math.abs(mPressHomeKyeTime - mPauseTime) > 150) {
						// 没有按Homekey或者相隔时间太久
						if (mPlayer != null) {
							mPlayer.pause();
						}
					}

					// 重置时间记录
					mPauseTime = 0;
					mPressHomeKyeTime = 0;
				} catch (Exception e) {
					e.printStackTrace();
				}

				break;
			}
		};
	};

	private void parseScenicSpots(String response) {
		mScenicSpots.clear();
		
		String name = "";
		
		if (response != null) {
			try {
				JSONArray jsonObjs = new JSONObject(response)
						.getJSONArray("records");
				if (jsonObjs != null) {
					ArrayList<ScenicSpot> parseArray = new ArrayList<ScenicSpot>();
					for (int i = 0; i < jsonObjs.length(); i++) {
						ScenicSpot spot = new ScenicSpot();
						
						spot.name = jsonObjs.getJSONObject(i)
								.getString("title");
						
						name = spot.name;

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

						// 解析经纬度和景点层级
						spot.latitude = jsonObjs.getJSONObject(i)
								.getJSONObject("attracGps").getDouble("y");
						spot.longitude = jsonObjs.getJSONObject(i)
								.getJSONObject("attracGps").getDouble("x");
						spot.level = (float) jsonObjs.getJSONObject(i)
								.getJSONObject("attracGps").getDouble("offset");

						spot.broadcastDistance = (float) (jsonObjs
								.getJSONObject(i).getJSONObject("attracGps")
								.getDouble("broadcastDistance"));
						if (spot.broadcastDistance < 0.01
								&& spot.broadcastDistance > -0.01f) {
							spot.broadcastDistance = 100.0f;
						}

						spot.audio = Path.VOICE_ADRESS
								+ jsonObjs.getJSONObject(i).getString(
										"voiceurl");

						mScenicSpots.add(spot);

					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				FileTools.writeLog("realtime.txt", "name:"+name+
						" Parse Exception:" + e.getMessage());
			}
		}
	}

	private void initBaiduMap() {
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMyLocationEnabled(true);
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(INIT_LEVEL));

		mCurLevel = mBaiduMap.getMapStatus().zoom;
		
		

		if (UesrInfo.y != null && !UesrInfo.y.equals("") && UesrInfo.x != null && !UesrInfo.x.equals("")) {
			
			System.out.println("优质y:"+UesrInfo.y+" x:"+UesrInfo.x);
			
			double lat = Double.valueOf(UesrInfo.y);
			double lng = Double.valueOf(UesrInfo.x);
			mLatLng = new LatLng(lat, lng);
		} else {
			System.out.println("空置y:"+UesrInfo.y+" x:"+UesrInfo.x);
			
			if (UesrInfo.area.equals("宝鸡市") == true) {
				mLatLng = new LatLng(34.368707, 107.245043);// 宝鸡市政府经纬度
			} else if (UesrInfo.area.equals("翠华山景区") == true) {
				mLatLng = new LatLng(33.986906, 109.022683); // 翠华山天池经纬度
			} else if (UesrInfo.area.equals("丰庆公园") == true) {
				mLatLng = new LatLng(34.254021, 108.905653);// 丰庆公园经纬度
			} else {
				mLatLng = new LatLng(34.265672, 108.953439);// 缺省位置是钟楼经纬度
			}
		}

		setCenter(mLatLng);// 将当前位置设置到地图中心

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
		mTmpIcon = mark.getIcon();
		mActiveMarker = mark;

		ScenicSpot spot = (ScenicSpot) mActiveMarker.getExtraInfo().get(
				"scenicSpot");
		Bitmap bmp = createDrawable(spot.name, R.drawable.baiduactivemark);
		BitmapDescriptor activemarkicon = BitmapDescriptorFactory
				.fromBitmap(bmp);
		mActiveMarker.setIcon(activemarkicon);

		// mActiveMarker.setIcon(mActiveIcon);

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
				mPlayer.play(spot.audio, false);
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
		ScenicSpot spot = (ScenicSpot)mActiveMarker.getExtraInfo().get("scenicSpot");
		if (spot == null){
			return null;
		}
		return String.valueOf(spot.longitude);
		
	}
	public String getY(){
		ScenicSpot spot = (ScenicSpot)mActiveMarker.getExtraInfo().get("scenicSpot");
		if (spot == null){
			return null;
		}
		
		return String.valueOf(spot.latitude);
	}
	public String getName(){
		ScenicSpot spot = (ScenicSpot)mActiveMarker.getExtraInfo().get("scenicSpot");
		if (spot == null){
			return null;
		}
		
		return spot.name;
	}
	
	
	public boolean isNeedGuide(){
		return true;
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
}