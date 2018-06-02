/**
 *@项目名称: 手机导游2.0
 *@Copyright: ${year} www.jiagu.com Inc. All rights reserved.
 *注意：本内容仅限于西安甲骨企业文化传播有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
package com.jiagu.mobile.tourguide.activities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;


import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.activities.bases.TitleDrawerActivity;
import com.jiagu.mobile.tourguide.bean.Attract;
import com.jiagu.mobile.tourguide.utils.AudioPlayer;
import com.jiagu.mobile.tourguide.utils.AudioPlayerCallback;
import com.jiagu.mobile.tourguide.utils.FileTools;
import com.jiagu.mobile.tourguide.utils.HttpUtil;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.tourguide.utils.UesrInfo;
import com.jiagu.mobile.tourguide.widget.ImgMapView;
import com.jiagu.mobile.tourguide.widget.ImgMapView.Coordinate;
import com.jiagu.mobile.tourguide.widget.ImgMapView.OnMarkaClickListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
/**
 * @Project: Maplocation
 * @Author: 徐进涛
 * @Date: 2014年12月29日
 */
public class PreTouristActivity extends TitleDrawerActivity implements OnMarkaClickListener, AudioPlayerCallback{
	//static final String scenicid = "S86b67f5ed14d4fadbdee6c419516d51b";
									
	private ImgMapView mMapView;
	private Bitmap bitmap;
	private Coordinate mCurCoordinate=null;
	
	TextView mFirstTourist = null;
	TextView mRealTourist = null;
	TextView mPreTourist = null;
	
	private View mLoadProgress;//加载进度
	
	private AudioPlayer mPlayer = null;
	
	long mPauseTime = 0;//暂停播放时间
	long mPressHomeKyeTime = 0;//按下home键时间

	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// 初始化窗口
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pretour);
		
		mMapView = (ImgMapView) findViewById(R.id.my_map_view);
		
		mLoadProgress = findViewById(android.R.id.progress);//初始化语音文件进度条资源
		
		mMapView.setMarkaClickable(true);
		
		// 初始化播放器
		mPlayer = new AudioPlayer();
		if (mMapView != null){
			mPlayer.create((AudioPlayerCallback)PreTouristActivity.this, mLoadProgress);
		}
		
		mFirstTourist = (TextView)findViewById(R.id.tourist_state_first);
		mRealTourist = (TextView)findViewById(R.id.tourist_state_realtour);
		mPreTourist = (TextView)findViewById(R.id.tourist_state_pretour);
		
		mPreTourist.setTextColor(Color.RED);
		
		mFirstTourist.setOnClickListener(new OnClickListener(){
			@Override   
			public void onClick(View v) {
				mPreTourist.setTextColor(Color.WHITE);
				
				Intent i = new Intent();
				i.setClass(getActivity(), FirstTouristActivity.class);
				startActivity(i);
				
				finish();
			}
		});
		
		mRealTourist.setOnClickListener(new OnClickListener(){
			@Override   
			public void onClick(View v) {
				mPreTourist.setTextColor(Color.WHITE);
				Intent i = new Intent();
				i.setClass(getActivity(), RealTimeTouristActivity.class);
				startActivity(i);	
				
				finish();
			}
		});
		
		mPreTourist.setOnClickListener(new OnClickListener(){
			@Override   
			public void onClick(View v) {
				
			}
		});
		
		sendRequest();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()){
		case R.id.my_map_view:
			if (mPlayer.isPlaying()){
				Point pt = getPlayerPosition(new Point((int)mCurCoordinate.x, (int)mCurCoordinate.y));
				
				if (mPlayer.isShow()){
					mPlayer.hide();
				}
				else{
					mPlayer.show(pt.x, pt.y);
				}
			}
			
			break;
		}
	}
	
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onPause() {
		PowerManager manager = (PowerManager)getSystemService(Activity.POWER_SERVICE);
		if (manager.isScreenOn()){
			//没有关屏			
			mPauseTime = System.currentTimeMillis();//记录处理时间
			SendMessage(MSG_PAUSE_PLAYER);
		}
		
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
//		setAppName(UesrInfo.area);
		appName.setText(UesrInfo.area + "手机导游");
	}
	
	//被ImageView回调函数，设置当前被选中的标点
	@Override
	public void onMarkaClick(Coordinate coordinate) {			
		//播放语音
		if (mCurCoordinate != null){
			mCurCoordinate.isClick = false;
		}
		
		mCurCoordinate = coordinate;
		mCurCoordinate.isClick = true;
				
		//是景点
		UesrInfo.HttpListen(this, null, UesrInfo
				.getTourIDid(), null, null, coordinate.id, "1");

		playMarker(mCurCoordinate.name, mCurCoordinate.voiceURL);
		
		Point pt = getPlayerPosition(new Point((int)mCurCoordinate.x, (int)mCurCoordinate.y));
		if (mPlayer.isShow()){	
			mPlayer.move(pt.x, pt.y);
		}
		else
		{
			mPlayer.show(pt.x, pt.y);
		}
		
		mMapView.invalidate();
		
		
		//FileTools.writeLog("realtime.txt", "mCurCoordinate.x:"+mCurCoordinate.x+" "+"mCurCoordinate.y:"+mCurCoordinate.y);
	}
	
	public void onPlayer(){
		//if (mPlayer.isPlaying()){
		
			if (mCurCoordinate == null){
				return;
			}
			
			
			Point pt = getPlayerPosition(new Point((int)mCurCoordinate.x, (int)mCurCoordinate.y));
			
			if (mPlayer.isPlaying()){
				if (mPlayer.isShow()){
					mPlayer.hide();
				}
				else{
					mPlayer.show(pt.x, pt.y);
				}
			}
			else{
				if (mPlayer.isShow()){
					mPlayer.hide();
				} 
			}
			
		//}
	}
	
	//播放语音
	private void playMarker(String name, final String voiceurl){
		//显示等待进度
		showProgress();
		mPlayer.setTitle(name);
		mPlayer.setControlPlay();

		//加载录音、播放
		new Thread(new Runnable() {
			@Override
			public void run() {	
				mPlayer.play(voiceurl, false);
			}
		}).start();
		
//		if (spot.id != null){
//			//是景区
//			
//			UesrInfo.HttpListen(this, spot.id, UesrInfo
//					.getTourIDid(), spot.scenicID, null, null, "0");
//		}
//		else{
//			
//			//是景点
//			
//			UesrInfo.HttpListen(this, null, UesrInfo
//					.getTourIDid(), null, null, spot.attracID, "1");
//		}
		
		
	}
	
	public Point getPlayerPosition(Point point){	
		int[] location = new  int[2] ;
		int top = 0;
		int bottom = 0;
		int left = 0;
		int right = 0;
		int x = 0;
		int y = 0;
		Point pt = new Point();
		if (point != null){
			pt.x = point.x;
			pt.y = point.y;
		}

		if (mMapView != null){
			//地图窗口的区域
			mMapView.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
			
			Rect frame = new Rect();
			getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
			@SuppressWarnings("unused")
			int statusBarHeight = frame.top;
			
			top = location[1];
			bottom = top+mMapView.getHeight();
			left = 0;
			right = left+mMapView.getWidth();
		}
		
		if (mPlayer.getWidth() > 0 && mPlayer.getHeight() > 0){
			x = point.x - mPlayer.getWidth()/2;
			y = point.y - 2*mPlayer.getHeight()/3;	
		}
		else {
			x = point.x - 100;
			y = point.y - 40;
		}
		
		if (x<left){
			x = left+1;
		}
		else if (x>right-mPlayer.getWidth()/2){
			x = right-mPlayer.getWidth()/2-1;
		}
		
		if (y<top){
			y = top+1;
		}
		else if (y>bottom-mPlayer.getHeight()){
			y = bottom-mPlayer.getHeight()-1;
		}
		
		pt.x = x;
		pt.y = y;

		return pt;
	}
	
	@Override
	public void onDeleteAttract(String id){
		
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
		
	// 获取景区地图
		private void sendRequest() {
			showProgress();
			
			FileTools.writeLog("realtime.txt", "UesrInfo.area:"+UesrInfo.area+" scenicid = S86b67f5ed14d4fadbdee6c419516d51b");
			
			String url = Path.SEVER_PATH+ "scenicUser/getScenicMap.htm";
			RequestParams params = new RequestParams();
			//params.put("scenicID", UesrInfo.area);
			params.put("scenicID", UesrInfo.sceneId);
			onPost(url, params, new AsyncHttpResponseHandler() {
				@Override
				public void onFailure(Throwable arg0, String result) {
					showToast("网络异常，请检查您的网络！");
					hideProgress();
				}

				@Override
				public void onSuccess(int code, String result) {
					hideProgress();
					if (200 == code) {
						System.out.println(result);
						try {
							JSONObject root = new JSONObject(result);
							if (root.optString("result").equals("0")) {
								JSONObject data = root.optJSONArray("records").optJSONObject(0);
								final String mapUrl = Path.SEVER_RES_PATH + data.optString("mapurl");
								if (mapUrl.length() > 0) {
									String path = "/mnt/sdcard/" + "map/";
									File folder = new File(path);
									if (!folder.exists()) {
										folder.mkdirs();
									}
									final String filePath = path + UesrInfo.area + ".jpg";
									File imageFile = new File(filePath);
									if (imageFile.exists()) {
										imageFile.delete();
									}
									
									new Thread(new Runnable() {
										@Override
										public void run() {
											writeFile(filePath, downFile(mapUrl));
											runOnUiThread(new Runnable() {
												@Override
												public void run() {
													setMapImage(filePath);
													hideProgress();
												}
											});
										}
									}).start();
								} 
								else {	
								}
								sendGetInfoRequest();
							} 
						} 
						catch (Exception e) {
						}
					} 
				}
			});
		}
		
		//向服务器请求获取景区和景点标注信息
		private void sendGetInfoRequest() {
			showProgress();
			String url = Path.SEVER_PATH + "scenicUser/getSignScenicInfoEx.htm";
			//String url = Path.SEVER_PATH + "scenicUser/getSignScenicInfo.htm";
			RequestParams params = new RequestParams();
			params.put("scenicID", UesrInfo.sceneId);
			
			FileTools.writeLog("realtime.txt", "UesrInfo.area:"+UesrInfo.area+" scenicid = S86b67f5ed14d4fadbdee6c419516d51b");
			
			//HttpUtil.getClient().setTimeout(30000);
			
			HttpUtil.post(url, params, new AsyncHttpResponseHandler(){
			//onPost(url, params, new AsyncHttpResponseHandler() {
				@Override
				public void onFailure(Throwable arg0, String result) {
					showToast("onFailure 网络异常，请检查您的网络！");	
					hideProgress();
				}

				@Override
				public void onSuccess(int code, String result) {
					hideProgress();
					
					if (200 == code) {
						try {
							JSONObject root = new JSONObject(result);
							if (root.optString("result").equals("0")) {
								JSONArray data = root.optJSONArray("records");
								
								ArrayList<Attract> mDataList = new ArrayList<Attract>();
								ArrayList<Coordinate> coordinates = new ArrayList<ImgMapView.Coordinate>();
								for (int i = 0; i < data.length(); i++) {
									Attract attract = new Attract(data.optJSONObject(i));
									
									//没有景区景点已在景区地图上标注
									if (attract.x!=-99999 && attract.y!=-99999){
										attract.isBind = true;
									}
									
									mDataList.add(attract);
									
									if (attract.x != -99999) {
										float x = (float) (attract.x * 1);
										float y = (float) (attract.y * 1);
										Coordinate coo = new Coordinate(x, y);
										coo.id = attract.id;
										coo.imgX = (float)attract.x;
										coo.imgY = (float)attract.y;
										coo.voiceURL = attract.voiceURL;
										coo.imageURL = attract.imageURL;
										coo.name = attract.name;
										
										//coo.isBind = true;
										coordinates.add(coo);
									}
								}

								mMapView.setCoordinate(coordinates);
							} 
						} catch (JSONException e) {
							showToast(R.string.system_error_prompt);
						} finally {
							hideProgress();
						}
					} else {
						showToast(R.string.network_error_try_again);
					}
				}
			});
		}
		
		public void setMapImage(String path) {
			bitmap = BitmapFactory.decodeFile(path);
			if (bitmap == null) {
				return;
			}
			mMapView.setImageBitmap(bitmap);
			mMapView.setOnMarkaClickListener(this);
		}
		
		//实现AudioPlayerCallback接口
		public void sendErrorTips(String tips){
			SendMessage(MSG_ERROR_TIPS, tips);
		}
		
		public void hideLoading(){
			SendMessage(MSG_PROGRESS_GONE);
		}
		
		public void showLoading(){
			
		}
		
		public View getView(){
			return getWindow().getDecorView();
		}
		
		public Activity getActivity(){
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
		
		public InputStream downFile(String url) {
			try {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response = client.execute(get);
				HttpEntity entity = response.getEntity();
				return entity.getContent();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		//将景区地图写入文件
		public void writeFile(String filePath, InputStream is) {
			if (is != null){
				try {
					File file = new File(filePath);
					if (file.exists()) {
						final File to = new File(file.getAbsolutePath() + System.currentTimeMillis());
						file.renameTo(to);
						file.delete();
					}
					file.createNewFile();
					FileOutputStream fos = null;
					fos = new FileOutputStream(file);
					byte[] buffer = new byte[8192];
					int temp = 0;
					while ((temp = is.read(buffer)) > 0) {
						fos.write(buffer, 0, temp);
					}
					fos.close();
					is.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
		
		public final static int MSG_PLAY_AUDIO = 3;//播放导游语音
		public final static int MSG_PAUSE_PLAYER = 5;//onPause消息
		public final static int MSG_ERROR_TIPS = 6;//错误提示
		public final static int MSG_PROGRESS_GONE = 7;//关闭加载进度窗口
		
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
		
		// 业务之间消息处理
		Handler handleProgress = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.arg1) {
				case MSG_ERROR_TIPS:
					String errmsg = msg.getData().getString("msgCode");
					showToast(errmsg);
					break;
					
				case MSG_PROGRESS_GONE:
					mLoadProgress.setVisibility(View.GONE);
					break;
					
				case MSG_PAUSE_PLAYER:
					try{
						int count = 0;
						
						//等待150毫秒
						while (mPressHomeKyeTime==0&&count<3){
							try {
								Thread.sleep(50);
								count++;
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					
						if (mPressHomeKyeTime == 0 || Math.abs(mPressHomeKyeTime-mPauseTime)>150){
							//没有按Homekey或者相隔时间太久
							if (mPlayer != null) {
								mPlayer.pause();
							}
						}
						
						//重置时间记录
						mPauseTime = 0;
						mPressHomeKyeTime = 0;
					}
					catch (Exception e){
						e.printStackTrace();
					}
					
					break;
				}
			};
		};
		
		
		
}