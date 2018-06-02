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
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jiagu.mobile.daohang.RouteGuideDemo;
import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.activities.bases.TitleDrawerActivity;
import com.jiagu.mobile.tourguide.adapter.SceneDetialListViewAdapter;
import com.jiagu.mobile.tourguide.bean.Appraise;
import com.jiagu.mobile.tourguide.bean.SceneDetial;
import com.jiagu.mobile.tourguide.utils.FileTools;
import com.jiagu.mobile.tourguide.utils.HttpUtil;
import com.jiagu.mobile.tourguide.utils.MenuRightAnimations;
import com.jiagu.mobile.tourguide.utils.MusicPlayer;
import com.jiagu.mobile.tourguide.utils.MyImageLoader;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.tourguide.utils.UesrInfo;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

//主要景观详情页面		高磊
public class SceneDetialActivity extends TitleDrawerActivity implements
		OnCheckedChangeListener, OnClickListener, OnRefreshListener<ListView>,
		OnLastItemVisibleListener {
	private RadioGroup mRG;
	private ImageButton mReturn, mControl;
	private PullToRefreshListView mListView;
	private TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, mThisTime, mMaxTime,
			tv_title;
	private ImageView image;
	private LinearLayout layout;
	private PopupWindow pop;
	private com.jiagu.mobile.tourguide.utils.MusicPlayer player;
	private SeekBar mMusicSeekBar;
	private String url;
	private String id;
	private ArrayList<Appraise> data;
	private RelativeLayout mComposerButtonsWrapper,
			mComposerButtonsShowHideButton;
	private ImageView mComposerButtonsShowHideButtonIcon;
	private boolean areButtonsShowing;
	private List<SceneDetial> list;
	private String a;
	private String name;
	private EditText et;
	private TextView text;
	private int plays = 1;
	private SceneDetialListViewAdapter adapter;

	static int count = 0;

	@Override
	@SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scenedetial);
		Intent i = getIntent();
		id = i.getStringExtra("id");
		a = i.getStringExtra("a");
		initMusicPlayerPop();
		getTextData();
		initView();
	}

	private void initView() {
		mRG = (RadioGroup) findViewById(R.id.scenedetial_but);
		layout = (LinearLayout) findViewById(R.id.scenedetial_layout);
		mListView = (PullToRefreshListView) findViewById(R.id.scenedetial_listview);
		mReturn = (ImageButton) findViewById(R.id.activity_scene_image_return);
		ImageButton photo = (ImageButton) findViewById(R.id.composer_button_photo);
		ImageButton video = (ImageButton) findViewById(R.id.composer_button_video);
		ImageButton listen = (ImageButton) findViewById(R.id.composer_button_listen);
		tv1 = (TextView) findViewById(R.id.scenedetial_text_01);
		tv2 = (TextView) findViewById(R.id.scenedetial_text_02);
		tv3 = (TextView) findViewById(R.id.scenedetial_text_03);
		tv4 = (TextView) findViewById(R.id.scenedetial_text_04);
		tv5 = (TextView) findViewById(R.id.scenedetial_text_05);
		tv6 = (TextView) findViewById(R.id.scenedetial_text_06);
		tv7 = (TextView) findViewById(R.id.scenedetial_text_07);
		image = (ImageView) findViewById(R.id.scenedetial_image);
		et = (EditText) findViewById(R.id.feature_details_et);
		text = (TextView) findViewById(R.id.qinglun);
		et.setVisibility(View.GONE);
		text.setVisibility(View.GONE);
		mListView.setVisibility(View.GONE);
		layout.setVisibility(View.VISIBLE);
		layout.setOnClickListener(this);
		mRG.setOnCheckedChangeListener(this);
		mReturn.setOnClickListener(this);
		image.setOnClickListener(this);
		photo.setOnClickListener(this);
		video.setOnClickListener(this);
		listen.setOnClickListener(this);
		text.setOnClickListener(this);
		mListView.setOnLastItemVisibleListener(this);
		mListView.setOnRefreshListener(this);
		MenuRightAnimations.initOffset(this);
		mComposerButtonsWrapper = (RelativeLayout) findViewById(R.id.composer_buttons_wrapper);
		mComposerButtonsShowHideButton = (RelativeLayout) findViewById(R.id.composer_buttons_show_hide_button);
		mComposerButtonsShowHideButtonIcon = (ImageView) findViewById(R.id.composer_buttons_show_hide_button_icon);
		mComposerButtonsShowHideButton.setOnClickListener(this);
		for (int j = 0; j < mComposerButtonsWrapper.getChildCount(); j++) {
			mComposerButtonsWrapper.getChildAt(j).setOnClickListener(this);
		}
		mComposerButtonsShowHideButton.startAnimation(MenuRightAnimations
				.getRotateAnimation(0, 360, 200));
		mComposerButtonsShowHideButtonIcon.setOnClickListener(this);
		tv6.setOnClickListener(this);
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
	protected void onPause() {
		super.onPause();
		PowerManager manager = (PowerManager) getSystemService(Activity.POWER_SERVICE);
		if (manager.isScreenOn()) {
			// 没有关屏
			if (player.isPlaying()) {
				player.pause();
				isPlay = false;
			}
		} else {

		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		player.destroy();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.scenedetial_1_but:
			mListView.setVisibility(View.GONE);
			layout.setVisibility(View.VISIBLE);
			break;
		case R.id.scenedetial_2_but:
			layout.setVisibility(View.GONE);
			mListView.setVisibility(View.VISIBLE);
			getData(id);
			break;
		}
	}

	private void hidebtn() {
		MenuRightAnimations.startAnimationsOut(mComposerButtonsWrapper, 300);
		mComposerButtonsShowHideButtonIcon.startAnimation(MenuRightAnimations
				.getRotateAnimation(-315, 0, 300));
		areButtonsShowing = false;
	}

	@Override
	public void onClick(View v) {
		Intent i = new Intent();
		switch (v.getId()) {
		case R.id.activity_scene_image_return:
			onBackPressed();
			break;
		case R.id.scenedetial_text_06:
			if (list.get(0).getScenicGps() == null
					|| list.get(0).getScenicGps().getX() == null
					|| list.get(0).getScenicGps().getX().equals("")
					|| list.get(0).getScenicGps().getY() == null
					|| list.get(0).getScenicGps().getY().equals("")) {

			} else {
				i.putExtra("x", list.get(0).getScenicGps().getX());
				i.putExtra("y", list.get(0).getScenicGps().getY());
				i.putExtra("name", list.get(0).getSteet());
				i.setClass(SceneDetialActivity.this, RouteGuideDemo.class);
				startActivity(i);
			}
			break;
		case R.id.qinglun:
			if (et.getText().toString().trim() == null
					|| et.getText().toString().trim().equals("")) {
				Toast.makeText(SceneDetialActivity.this, "请输入评价内容",
						Toast.LENGTH_SHORT).show();
			} else {
				UesrInfo.getText(SceneDetialActivity.this,
						UesrInfo.getTourIDid(), UesrInfo.getUsername(), id,
						"0", et.getText().toString().trim(), name, et);
			}
			break;
		case R.id.composer_button_photo:
			hidebtn();
			i.putExtra("id", id);
			i.putExtra("path", Path.SERVER_ADRESS + "scenic/scenicPhoto.htm");
			i.setClass(SceneDetialActivity.this, PhotoAlbumActivity.class);
			startActivity(i);
			break;
		case R.id.composer_button_video:
			i.putExtra("id", id);
			i.putExtra("name", name);
			i.setClass(SceneDetialActivity.this, ScenicSpotsActivity.class);
			startActivity(i);
			break;
		case R.id.composer_button_listen:
			if (pop.isShowing()) {
				pop.dismiss();
			} else {
				if (plays == 1) {
					play(url, list.get(0).getScenicname() + "(收听:"
							+ list.get(0).getListenCount() + "次)");
				}
				pop.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM,
						0, 0);
			}
			break;
		case R.id.scenedetial_layout:
			if (pop.isShowing()) {
				pop.dismiss();
			} else {
				if (plays == 1) {
					play(url, list.get(0).getScenicname() + "(收听:"
							+ list.get(0).getListenCount() + "次)");
				}
				pop.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM,
						0, 0);
			}
			break;
		case R.id.scenedetial_image:// 跳到相册
			i.putExtra("id", id);
			i.putExtra("path", Path.SERVER_ADRESS + "scenic/scenicPhoto.htm");
			i.setClass(SceneDetialActivity.this, PhotoAlbumActivity.class);
			startActivity(i);
			break;
		case R.id.composer_buttons_show_hide_button_icon:
			if (!areButtonsShowing) {
				MenuRightAnimations.startAnimationsIn(mComposerButtonsWrapper,
						300);
				mComposerButtonsShowHideButtonIcon
						.startAnimation(MenuRightAnimations.getRotateAnimation(
								0, -315, 300));
			} else {
				MenuRightAnimations.startAnimationsOut(mComposerButtonsWrapper,
						300);
				mComposerButtonsShowHideButtonIcon
						.startAnimation(MenuRightAnimations.getRotateAnimation(
								-315, 0, 300));
			}
			areButtonsShowing = !areButtonsShowing;
			break;
		}
	}

	private void getTextData() {
		showProgress();
		RequestParams params = new RequestParams();
		params.put("id", id);
		String path = Path.SERVER_ADRESS + "scenic/scenicDetial.htm";
		HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				super.onFailure(arg0, arg1);
				hideProgress();
				count++;
				if (count < 3) {
					getTextData();
					FileTools.writeLog("Scene.txt", "======>>>>>>pages:" + 1
							+ ":area" + UesrInfo.area + "或" + UesrInfo.sceneId
							+ "conut:" + count + "areaType" + UesrInfo.areaType
							+ "主要景观详情数据:" + "连接失败:Throwable:" + arg0 + "arg1:"
							+ arg1);
				} else {

					count = 0;
					Toast.makeText(getApplicationContext(), "请检查您的网络",
							Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				FileTools.writeLog("Scene.txt", "======>>>>>>pages:" + 1
						+ ":area" + UesrInfo.area + "或" + UesrInfo.sceneId
						+ "conut:" + count + "areaType" + UesrInfo.areaType
						+ "主要景观详情数据:" + response);
				if (response != null && !response.equals("")) {
					JSONObject object = JSONObject.parseObject(response);
					JSONArray top = object.getJSONArray("records");
					String result = object.getString("result");
					if (result.equals("0") && top != null && !top.equals("")) {
						list = JSONArray.parseArray(top.toJSONString(),
								SceneDetial.class);
						
						tv1.setText(list.get(0).getScenicabbr());
						tv2.setText("景区级别:" + list.get(0).getScenicLevel());
						tv3.setText("景区类型:" + list.get(0).getScenicType());
						tv4.setText("最佳旅游时间:" + list.get(0).getGoodTimer());
						tv5.setText("景区电话:" + list.get(0).getPhonenumber());
						tv6.setText("景区地址:" + list.get(0).getSteet());
						if (list.get(0).getScenicCn()!=null&&list.get(0).getScenicCn().getVoiceurl()!=null&&!list.get(0).getScenicCn().getVoiceurl().equals("")) {
							url = Path.VOICE_ADRESS+ list.get(0).getScenicCn().getVoiceurl();
						}
						tv7.setText(list.get(0).getText());
						name = list.get(0).getScenicabbr();
						if (!(list.get(0).getScenicphoto().equals(""))) {
							MyImageLoader.loadImage(Path.IMAGER_ADRESS
									+ list.get(0).getScenicphoto(), image);
						}
						if (a.equals("a")) {
							play(url, list.get(0).getScenicname() + "(收听:"
									+ list.get(0).getListenCount() + "次)");
						}
						et.setVisibility(View.VISIBLE);
						text.setVisibility(View.VISIBLE);
					} else {
						Toast.makeText(SceneDetialActivity.this, "服务器在打盹",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(SceneDetialActivity.this, "服务器在打盹",
							Toast.LENGTH_SHORT).show();
				}
				hideProgress();
			}

		});
	}

	private void getData(String id) {
		Pages = 2;
		RequestParams params = new RequestParams();
		params.put("appraiseId", id);
		params.put("pages", "1");
		params.put("type", "0");
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
				if (response != null && !response.equals("")) {
					JSONObject object = JSONObject.parseObject(response);
					JSONArray top = object.getJSONArray("records");
					if (top != null && !top.equals("")) {
						data = (ArrayList<Appraise>) JSONArray.parseArray(
								top.toJSONString(), Appraise.class);
						adapter = new SceneDetialListViewAdapter(
								SceneDetialActivity.this, data);
						mListView.setAdapter(adapter);
						mListView.onRefreshComplete();
					} else {
						Toast.makeText(SceneDetialActivity.this, "服务器在打盹",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(SceneDetialActivity.this, "服务器在打盹",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private void play(final String url, String title) {

		if (url == null || url.equals("")) {
			showToast("暂无该语音文件");
			return;
		}
		if (url.contains("http://") || url.contains("https://")) {
			try {
				if (!pop.isShowing()) {
					tv_title.setText(title.trim());
					plays++;
					if (getWindow().getDecorView() != null) {
						pop.showAtLocation(getWindow().getDecorView(),
								Gravity.BOTTOM, 0, 0);
						UesrInfo.HttpListen(SceneDetialActivity.this,
								list.get(0).getId(), UesrInfo.getTourIDid(),
								list.get(0).getScenicid(), null, null, "0");
						new Thread(new Runnable() {
							@Override
							public void run() {
								player.playUrl(url);
							}
						}).start();
					}
				}
			} catch (Exception e) {

			}
		}
	}

	boolean isPlay = true;

	private void initMusicPlayerPop() {
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
		mControl = (ImageButton) view.findViewById(R.id.music_player_control);
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
		mMusicSeekBar = (SeekBar) view.findViewById(R.id.music_player_seekbar);
		mThisTime = (TextView) view.findViewById(R.id.current_time);
		mMaxTime = (TextView) view.findViewById(R.id.max_time);
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		player = new MusicPlayer(mMusicSeekBar, mThisTime, mMaxTime, mControl);
	}

	int Pages = 2;

	@Override
	public void onLastItemVisible() {
		RequestParams params = new RequestParams();
		params.put("appraiseId", id);
		params.put("pages", "" + Pages);
		params.put("type", "0");
		String path = Path.SERVER_ADRESS + "scenic/scenicAppraise.htm";
		HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				Toast.makeText(getApplicationContext(), "请检查您的网络",
						Toast.LENGTH_LONG).show();
				super.onFailure(arg0, arg1);
				hideProgress();
			}

			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				if (!(response == null)) {
					JSONObject object = JSONObject.parseObject(response);
					JSONArray top = object.getJSONArray("records");
					String result = object.getString("result");
					if (result.equals("0") && top != null && !top.equals("")) {
						ArrayList<Appraise> list = (ArrayList<Appraise>) JSONArray
								.parseArray(top.toJSONString(), Appraise.class);
						for (Appraise appraise : list) {
							data.add(appraise);
						}
						adapter.setData(data);
						adapter.notifyDataSetChanged();
						Pages++;
					} else if (result.equals("30")) {
						Toast.makeText(SceneDetialActivity.this, "已是最后一页",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(SceneDetialActivity.this, "服务器在打盹",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(SceneDetialActivity.this, "服务器在打盹",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		getData(id);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		this.finish();
		if (UesrInfo.areaType.equals("4")) {
			Intent i = new Intent();
			i.putExtra("areaname", list.get(0).getAreaName());
			i.putExtra("areatype", "2");
			i.setClass(SceneDetialActivity.this, SceneActivity.class);
			startActivity(i);
		}
	}
}
