/**
 *@项目名称: 手机导游2.0
 *@Copyright: ${year} www.jiagu.com Inc. All rights reserved.
 *注意：本内容仅限于西安甲骨企业文化传播有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
package com.jiagu.mobile.tourguide.utils;

import java.util.Timer;
import java.util.TimerTask;

import com.jiagu.mobile.daohang.RouteGuideDemo;
import com.jiagu.mobile.tourguide.R;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
/**
 * @Project: Maplocation
 * @Author: 徐进涛
 * @Date: 2014年12月30日
 */
public class AudioPlayer implements OnClickListener {
	private View mLoadProgress = null;
	private AudioPlayerCallback mCallback = null;
	private View mView = null;
	private PopupWindow mPlayWnd = null;
	private ImageButton mControlBtn = null;
	private SeekBar mPlaybackProgressSb = null;
	private TextView mPlaybackProgressTv = null;
	private TextView mAudioDurationTv = null;
	private TextView mTitleTv = null;
	private String mTitle; 
	private MediaPlayer mMediaPlayer = null;
	private boolean mIsShow = false;
	
	private boolean mInitPlayPause = false;
	
	int mWidth = 0;
	int mHeight = 0;

	private String FormatTime(int time) {
		int sum = time / 1000; // 算出总计秒数
		int minute = sum / 60; // 算出分钟
		int second = sum % 60; // 算出秒数
		minute %= 60; // 超过60分钟，只显示1小时以内的时间
		return String.format("%02d:%02d", minute, second);
	}

	public void play(String strAudioPath, boolean initPlayPause) {
		mInitPlayPause = initPlayPause;
		// 播放指定位置的音频文件
		try {
			mMediaPlayer.reset();
			mMediaPlayer.setDataSource(strAudioPath);
			mMediaPlayer.prepare();// prepare之后自动播放
		} catch (Exception e) {
//			mParent.SendMessage(RealTimeTouristActivity.MSG_ERROR_TIPS, "加载语音失败，请检查网络！");
//			mParent.SendMessage(RealTimeTouristActivity.MSG_PROGRESS_GONE);
			
			mCallback.sendErrorTips("加载语音失败，请检查网络！");
			mCallback.hideLoading();
		}
	}

	// 继续播放
	public void play() {
		mMediaPlayer.start();		
	}

	// 暂停播放
	public void pause() {
		mControlBtn.setTag("pause");// 设置为暂停状态
		mControlBtn.setImageResource(R.drawable.btn_pause);// 按钮可播放
		
		if (mMediaPlayer.isPlaying() == true){
			//FileTools.writeLog("realtime.txt", "excute pause");
			mMediaPlayer.pause();
		}
		else{
			//FileTools.writeLog("realtime.txt", "excute stop");
			mMediaPlayer.stop();
		}	
	}

	// 在指定位置显示播放窗口
	public void show(int x, int y) {
		mIsShow = true;
		//View view = mParent.getWindow().getDecorView();
		View view = mCallback.getView();
		
		if (mPlayWnd.getContentView().getWidth()>0 && mWidth==0){
			mWidth = mPlayWnd.getContentView().getWidth();
		}
		if (mPlayWnd.getContentView().getHeight()>0 && mHeight==0){
			mHeight = mPlayWnd.getContentView().getHeight();
		}

		mPlayWnd.showAtLocation(view, Gravity.TOP | Gravity.LEFT, x, y);
		
	}
	
	public void move(int x, int y){
		//View view = mParent.getWindow().getDecorView();
		
		View view = mCallback.getView();
		
		if (mPlayWnd.getContentView().getWidth()>0 && mWidth==0){
			mWidth = mPlayWnd.getContentView().getWidth();
		}
		
		if (mPlayWnd.getContentView().getHeight()>0 && mHeight==0){
			mHeight = mPlayWnd.getContentView().getHeight();
		}
		
		mPlayWnd.update(x, y, mPlayWnd.getWidth(), mPlayWnd.getHeight());
	}

	// 隐藏播放窗口
	public void hide() {
		mPlayWnd.dismiss();
		mIsShow = false;
	}

	// 播放按钮事件处理
	class PlayerButtonListener implements OnClickListener {
		public void onClick(View v) {
			ImageButton btn = (ImageButton) v;
			String tag = (String) btn.getTag();
			// 设置播放器状态
			if (tag == "play") {
				btn.setTag("pause");// 设置为暂停状态
				mControlBtn.setImageResource(R.drawable.btn_pause);// 按钮可播放
				mMediaPlayer.pause();
			} else {
				btn.setTag("play");// 设置为播放状态状态
				mControlBtn.setImageResource(R.drawable.btn_play);// 按钮可暂停
				mMediaPlayer.start();
			}
		}
	}

	// 手动设置了播放器的滑块位置
	class PlayerSeekbarListener implements OnSeekBarChangeListener {
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			int pos = seekBar.getProgress();
			
			mMediaPlayer.seekTo(pos * mMediaPlayer.getDuration() / 100);// 设置播放位置
			
			if (mMediaPlayer.isPlaying() == false){
				mMediaPlayer.start();
			}
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {

		}
	}

	class AudioPlayerListener implements OnBufferingUpdateListener,
			OnCompletionListener, OnPreparedListener {
		@Override
		/**  
		 * 通过onPrepared播放  
		 */
		public void onPrepared(MediaPlayer mp) {			
			//mParent.SendMessage(RealTimeTouristActivity.MSG_PROGRESS_GONE);
			mCallback.hideLoading();

			mp.start();
			
			//增加对道路和区域的播报支持
			if (mInitPlayPause == true){
				mControlBtn.setTag("pause");// 设置为暂停状态
				mControlBtn.setImageResource(R.drawable.btn_pause);// 按钮可播放
				mp.pause();
				
				mInitPlayPause = false;
			}
			if (mControlBtn.getTag().equals("pause")){
				mp.pause();
			}

			mPlaybackProgressSb.setProgress(0); // 设置播放进度
			mPlaybackProgressTv.setText(FormatTime(0)); // 设置播放时间
			mAudioDurationTv.setText(FormatTime(mp.getDuration())); // 设置音频总播放时长

			Log.e("mediaPlayer", "onPrepared");
		}

		@Override
		public void onCompletion(MediaPlayer mp) {
			// 播放结束
			mPlaybackProgressSb.setProgress(100);// 设置播放进度
			
			mControlBtn.setImageResource(R.drawable.btn_pause);//
			mControlBtn.setTag("pause");
			
			//FileTools.writeLog("realtime.txt", "invoke onCompletion");
			Log.e("mediaPlayer", "onCompletion");
		}

		@Override
		public void onBufferingUpdate(MediaPlayer mp, int progress) {
			// 下载播放音频的进度
			//Toast.makeText(mParentView.getContext(), "onBufferingUpdate progress:"+progress, Toast.LENGTH_LONG).show();
			mPlaybackProgressSb.setSecondaryProgress(progress);
			Log.e("mediaPlayer", "onBufferingUpdate");
		}
	}

	private Timer mTimer = new Timer(); // 及时器用于更新播放进行信息
	TimerTask mTimerTask = new TimerTask() {
		@Override
		public void run() {
			if (mMediaPlayer == null) {
				return;
			}
			try {
				if (mMediaPlayer.isPlaying()) {
					handleProgress.sendEmptyMessage(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	Handler handleProgress = new Handler() {
		public void handleMessage(Message msg) {
			try{
				if (mMediaPlayer.isPlaying()){
					int position = mMediaPlayer.getCurrentPosition();
					int duration = mMediaPlayer.getDuration();

					if (duration > 0) {
						// 设置播放进度和时间
						long pos = mPlaybackProgressSb.getMax() * position / duration;
						mPlaybackProgressSb.setProgress((int) pos);// 设置播放进度					
						mPlaybackProgressTv.setText(FormatTime((int) position)); // 设置播放时间
					}
				}
			}
			catch (Exception e){
				e.printStackTrace();
			}
			
		};
	};
	private ImageButton DH;

	public boolean create(AudioPlayerCallback callback, View loadProgress) {
		mCallback = callback;
		mLoadProgress = loadProgress;

		// 创建播放窗口窗口
		mView = LayoutInflater.from(mCallback.getActivity()).inflate(
				R.layout.audio_player, null);
		
		mPlayWnd = new PopupWindow(mView, ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		
		mControlBtn = (ImageButton) mView
				.findViewById(R.id.audio_player_control);
		mPlaybackProgressSb = (SeekBar) mView
				.findViewById(R.id.music_player_seekbar);
		mAudioDurationTv = (TextView) mView.findViewById(R.id.tv_duration);
		mPlaybackProgressTv = (TextView) mView
				.findViewById(R.id.tv_playback_progress);
		
		mTitleTv = (TextView) mView
				.findViewById(R.id.scenic_name);
		DH = (ImageButton) mView.findViewById(R.id.pop_imagebutton_daohang);

		// 状态为播放
		mControlBtn.setOnClickListener(new PlayerButtonListener());
		mControlBtn.setImageResource(R.drawable.btn_play);//
		mControlBtn.setTag("play");

		mPlaybackProgressSb
				.setOnSeekBarChangeListener(new PlayerSeekbarListener());
		
		
		DH.setOnClickListener(this);
		
		if (mCallback.isNeedGuide() == false){
			DH.setVisibility(View.GONE);
		}
		else{
			DH.setVisibility(View.VISIBLE);
		}
		// 设置播放器
		try {
			mMediaPlayer = new MediaPlayer();
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mMediaPlayer
					.setOnBufferingUpdateListener(new AudioPlayerListener());
			mMediaPlayer.setOnPreparedListener(new AudioPlayerListener());
			mMediaPlayer.setOnCompletionListener(new AudioPlayerListener());
		} catch (Exception e) {
			Log.e("MediaPlayer create", "error", e);
			Toast.makeText(mCallback.getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
			
			return false;
		}

		// 创建计时器，每秒更新一次时间进度显示
		mTimer.schedule(mTimerTask, 0, 1000);

		return true;
	}

	public void destory() {
		mTimer.cancel(); // 关闭及时器
		mMediaPlayer.release();// 释放播放器资源
		mPlayWnd.dismiss();// 释放弹出的播放器窗口资源
	}

	public AudioPlayer() {

	}

	// 返回弹出窗口，在地图上能显示
	public View getPlayView() {
		return mPlayWnd.getContentView();
	}

	public boolean isPlaying() {
		return mMediaPlayer.isPlaying();
	}
	
	public void setTitle(String title){
		mTitleTv.setText(title);
		mTitle = title;
	}
	
	public String getTitle(){
		return mTitle;
	}
	
	public String getViewTitle(){
		return (String)mTitleTv.getText();
	}
	
	public boolean isShow(){
		return mIsShow;
	}
	
	public int getWidth(){
		return mWidth;
	}
	
	public int getHeight(){
		return mHeight;
	}
	
	public void setControlPlay(){
		mControlBtn.setTag("play");// 设置为播放状态
		mControlBtn.setImageResource(R.drawable.btn_play);// 按钮可播放
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent i = new Intent();
		switch (v.getId()) {
		case R.id.pop_imagebutton_daohang:
			//Toast.makeText(mCallback.getActivity(), "daohang"+" "+mCallback.getName()+" "+mCallback.getX()+" "+mCallback.getY(), Toast.LENGTH_SHORT).show();
			i.setClass(mCallback.getActivity(), RouteGuideDemo.class);
			i.putExtra("name", mCallback.getName());
			i.putExtra("x", mCallback.getX());
			i.putExtra("y", mCallback.getY());
			mCallback.getActivity().startActivity(i);
			
			break;

		default:
			break;
		}
	}

	private void startActivity(Intent i) {
		// TODO Auto-generated method stub
		
	}
}
