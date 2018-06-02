package com.jiagu.mobile.tourguide.activities;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.activities.bases.TitleDrawerActivity;
import com.jiagu.mobile.tourguide.adapter.SceneDetialListViewAdapter;
import com.jiagu.mobile.tourguide.bean.Appraise;
import com.jiagu.mobile.tourguide.bean.Records;
import com.jiagu.mobile.tourguide.utils.FileTools;
import com.jiagu.mobile.tourguide.utils.HttpUtil;
import com.jiagu.mobile.tourguide.utils.MusicPlayer;
import com.jiagu.mobile.tourguide.utils.MyImageLoader;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.tourguide.utils.UesrInfo;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

//特色西安详情页面
public class FeatureDetailsActivity extends TitleDrawerActivity implements
		OnClickListener {
	private ImageView yuyin;
	private TextView mTextView, mName;
	private String path;
	private ImageView tupian;
	private String photoid;
	private String id;
	private String type;
	private String voiceurl;
	private PopupWindow pop;
	private MusicPlayer player;
	private EditText et;
	private TextView text;
	private List<Records> data;
	private int plays = 1;
	private ListView listView;
	private ArrayList<Appraise> list;
	private String aprClass;
	private SceneDetialListViewAdapter adapter;
	private int Pages = 2;
	static int count = 0;

	@Override
	@SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feature_details);
		Intent i = getIntent();
		id = i.getStringExtra("id");
		path = i.getStringExtra("url");
		type = i.getStringExtra("type");
		aprClass = i.getStringExtra("aprClass");
		listView = (ListView) findViewById(R.id.featuredetails_listview);
		mName = (TextView) findViewById(R.id.feature_name);
		ImageButton image = (ImageButton) findViewById(R.id.activity_feature_image);
		yuyin = (ImageView) findViewById(R.id.xian_image_01);
		// 添加头部试图
		View topView = LayoutInflater.from(FeatureDetailsActivity.this)
				.inflate(R.layout.activity_featuredetails_top, null);
		LinearLayout layout = (LinearLayout) topView.findViewById(R.id.layout);
		tupian = (ImageView) topView
				.findViewById(R.id.activity_feature_details_image);
		mTextView = (TextView) topView.findViewById(R.id.feature_details_text);
		et = (EditText) topView.findViewById(R.id.feature_details_et);
		text = (TextView) topView.findViewById(R.id.qinglun);
		et.setVisibility(View.GONE);
		text.setVisibility(View.GONE);
		listView.addHeaderView(topView);

		// 添加底部试
		View bottom = LayoutInflater.from(FeatureDetailsActivity.this).inflate(
				R.layout.bottom_button, null);
		TextView more = (TextView) bottom.findViewById(R.id.bottom_textview);
		listView.addFooterView(bottom);

		getData(id, path);
		getPingLunData(id);

		more.setOnClickListener(this);
		image.setOnClickListener(this);
		tupian.setOnClickListener(this);
		yuyin.setOnClickListener(this);
		layout.setOnClickListener(this);
		text.setOnClickListener(this);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		setAppName(UesrInfo.area);
		appName.setText(UesrInfo.area + "手机导游");
		setMap();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.activity_feature_image:
			onBackPressed();
			break;
		case R.id.bottom_textview:
			// 评论查看更多
			RequestParams params = new RequestParams();
			params.put("appraiseId", id);
			params.put("pages", "" + Pages);
			params.put("type", aprClass);
			String path = Path.SERVER_ADRESS + "scenic/scenicAppraise.htm";
			HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

				@Override
				public void onFailure(Throwable arg0, String arg1) {
					super.onFailure(arg0, arg1);
					hideProgress();
					Toast.makeText(getApplicationContext(), "请检查您的网络",Toast.LENGTH_LONG).show();
				}

				@Override
				public void onSuccess(String response) {
					super.onSuccess(response);
					if (response != null) {
						JSONObject object = JSONObject.parseObject(response);
						JSONArray top = object.getJSONArray("records");
						String result = object.getString("result");
						if (top != null) {
							ArrayList<Appraise> lists = (ArrayList<Appraise>) JSONArray
									.parseArray(top.toJSONString(),
											Appraise.class);
							for (Appraise appraise : lists) {
								list.add(appraise);
							}
							adapter.setData(list);
							adapter.notifyDataSetChanged();
							Pages++;
						} else if (result.equals("30")) {
							Toast.makeText(FeatureDetailsActivity.this,
									"已是最后一页", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(FeatureDetailsActivity.this,
									"服务器在打盹", Toast.LENGTH_SHORT).show();
						}
					} else {

					}
				}
			});
			break;
		case R.id.qinglun:
			if (et.getText().toString().trim().equals("")|| et.getText().toString().trim() == null) {
				Toast.makeText(FeatureDetailsActivity.this, "请输入评价内容",Toast.LENGTH_SHORT).show();
			} else {
				if (data.get(0).getName() != null&& !data.get(0).getName().equals("")) {
					UesrInfo.getText(FeatureDetailsActivity.this,UesrInfo.getTourIDid(), UesrInfo.getUsername(), id,
							aprClass, et.getText().toString().trim(),data.get(0).getName(), et);
				} else {
					UesrInfo.getText(FeatureDetailsActivity.this,UesrInfo.getTourIDid(), UesrInfo.getUsername(), id,
							aprClass, et.getText().toString().trim(),data.get(0).getTitle(), et);
				}
				getPingLunData(id);
			}
			break;
		case R.id.xian_image_01:
			if (pop.isShowing()) {
				pop.dismiss();
			} else {
				if (plays == 1) {
					play(Path.VOICE_ADRESS + voiceurl);
				}
				pop.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM,0, 0);
			}
			break;
		case R.id.layout:
			if (pop.isShowing()) {
				pop.dismiss();
			} else {
				if (plays == 1) {
					play(Path.VOICE_ADRESS + voiceurl);
				}
				pop.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM,0, 0);
			}
			break;
		case R.id.activity_feature_details_image:
			Intent i = new Intent();
			i.putExtra("photoId", photoid);
			i.putExtra("path", Path.SERVER_ADRESS
					+ "characteristic/photoList.htm");
			i.setClass(FeatureDetailsActivity.this, PhotoAlbumActivity.class);
			startActivity(i);
			break;
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onPause() {
		super.onPause();
		PowerManager manager = (PowerManager)getSystemService(Activity.POWER_SERVICE);
		if (manager.isScreenOn()){
			//没有关屏
			if (player!=null&&player.isPlaying()) {
				player.pause();
				isPlay = false;
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

	private void getData(String Id, final String path) {
		showProgress();
		RequestParams params = new RequestParams(); // 绑定参数
		params.put("id", Id);
		HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				super.onFailure(arg0, arg1);
				hideProgress();
				Toast.makeText(getApplicationContext(), arg1, Toast.LENGTH_LONG).show();
				count++;
				if (count < 3){
					FileTools.writeLog("featuredetails.txt", "======>>>>>>特色详情数据------Throwable:"+arg0+"--------"+arg1);
					getData(id, path);
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
					Toast.makeText(FeatureDetailsActivity.this, "服务器在打盹",Toast.LENGTH_SHORT).show();
				} else {
					JSONObject object = JSONObject.parseObject(response);
					JSONArray top = object.getJSONArray("records");
					String result = object.getString("result");
					if (top.equals("") || top == null || result.equals("4")) {
						Toast.makeText(FeatureDetailsActivity.this, "服务器在打盹",Toast.LENGTH_SHORT).show();
					} else if (result.equals("0") && !top.equals("")&& top != null) {
						data = JSONArray.parseArray(top.toJSONString(),Records.class);
						if (data.size() <= 0) {
							Toast.makeText(FeatureDetailsActivity.this,"服务器在打盹", Toast.LENGTH_SHORT).show();
						} else {
							FileTools.writeLog("text.txt", response);
							photoid = data.get(0).getPhotoid();
							if (data.get(0).getTitle() == null|| data.get(0).getTitle().equals("")) {
								mName.setText(data.get(0).getName());
								initMusicPlayerPop(data.get(0).getName()+"(收听:"+data.get(0).getListenCount()+"次)");
							} else {
								mName.setText(data.get(0).getTitle());
								initMusicPlayerPop(data.get(0).getTitle()+"(收听:"+data.get(0).getListenCount()+"次)");
							}
							if (!(data.get(0).getImageurl() == null || data.get(0).getImageurl().equals(""))) {
								MyImageLoader.loadImage(Path.IMAGER_ADRESS+ data.get(0).getImageurl(), tupian);
							}
							if (!(data.get(0).getUrl() == null || data.get(0).getUrl().equals(""))) {
								MyImageLoader.loadImage(Path.IMAGER_ADRESS+ data.get(0).getUrl(), tupian);
							}
							mTextView.setText(data.get(0).getText());
							voiceurl = data.get(0).getVoiceurl();
							et.setVisibility(View.VISIBLE);
							text.setVisibility(View.VISIBLE);
						}
					} else {
						Toast.makeText(FeatureDetailsActivity.this, "服务器在打盹",
								Toast.LENGTH_SHORT).show();
					}
				}
				hideProgress();
			}
		});
	}

	private void getPingLunData(String id) {
		RequestParams params = new RequestParams();
		params.put("appraiseId", id);
		params.put("pages", "1");
		params.put("type", aprClass);
		String path = Path.SERVER_ADRESS + "scenic/scenicAppraise.htm";
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
					if (!(top == null)) {
						list = (ArrayList<Appraise>) JSONArray.parseArray(
								top.toJSONString(), Appraise.class);
						adapter = new SceneDetialListViewAdapter(
								FeatureDetailsActivity.this, list);
						listView.setAdapter(adapter);
					} else {
						Toast.makeText(FeatureDetailsActivity.this, "服务器在打盹",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(FeatureDetailsActivity.this, "服务器在打盹",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private void play(final String url) {

		if (url == null || url.equals("")) {
			showToast("服务器在打盹");
			return;
		}
		if (url.contains("http://") || url.contains("https://")) {
			if (!pop.isShowing()) {
				if (pop.isShowing()) {
					pop.showAtLocation(getWindow().getDecorView(),
							Gravity.BOTTOM, 0, 0);
				}
				plays++;
				UesrInfo.HttpListen(FeatureDetailsActivity.this, null,
						UesrInfo.getTourIDid(), null, id, null, "" + type);
				new Thread(new Runnable() {
					@Override
					public void run() {
						player.playUrl(url);
					}
				}).start();
			}
		}
	}

	boolean isPlay = true;

	@SuppressWarnings("deprecation")
	private void initMusicPlayerPop(String title) {
		View view = LayoutInflater.from(this).inflate(
				R.layout.pop_music_player, null);
		pop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		pop.setAnimationStyle(R.style.pop_anim_style);
		pop.setFocusable(true);
		pop.setTouchable(true);
		pop.setOutsideTouchable(true);
		pop.setBackgroundDrawable(new BitmapDrawable());
		view.setFocusableInTouchMode(true);
		ImageButton mControl = (ImageButton) view
				.findViewById(R.id.music_player_control);
		OnClickListener listener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.music_player_control:
					if (isPlay) {
						player.pause();
						isPlay = false;
					} else {
						player.play();
						isPlay = true;
					}
					break;
				case R.id.music_player_hidden:
					pop.dismiss();
					break;
				}
			}
		};
		view.findViewById(R.id.music_player_hidden)
				.setOnClickListener(listener);
		mControl.setOnClickListener(listener);
		SeekBar mMusicSeekBar = (SeekBar) view
				.findViewById(R.id.music_player_seekbar);
		TextView mThisTime = (TextView) view.findViewById(R.id.current_time);
		TextView mMaxTime = (TextView) view.findViewById(R.id.max_time);
		TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_title.setText(title);
		player = new MusicPlayer(mMusicSeekBar, mThisTime, mMaxTime, mControl);
	}
}
