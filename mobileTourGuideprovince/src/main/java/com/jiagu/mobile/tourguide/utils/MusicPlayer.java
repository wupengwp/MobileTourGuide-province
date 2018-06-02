package com.jiagu.mobile.tourguide.utils;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.jiagu.mobile.tourguide.R;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * @ClassName: MusicPlayer
 * @Description: 音乐播放
 * @author GaoLei
 * @date 2015年1月15日 下午12:20:42
 * @version 1
 * @company 西安甲骨企业文化传播有限公司
 */
public class MusicPlayer implements OnBufferingUpdateListener,
		OnCompletionListener, OnPreparedListener, OnSeekBarChangeListener {

	public MediaPlayer mediaPlayer; // 媒体播放器
	private SeekBar seekBar; // 拖动条
	private TextView start; // 当前位置
	private TextView max; // 播放长度
	private ImageButton control; // 播放按钮
	private View progerss;
	private boolean play = false;

	public void setProgerss(View progerss) {
		this.progerss = progerss;
	}

	private Timer mTimer = new Timer(); // 计时器

	// 初始化播放器
	public MusicPlayer(SeekBar seekBar) {
		super();
		if (seekBar != null) {
			this.seekBar = seekBar;
			this.seekBar.setOnSeekBarChangeListener(this);
		}
		try {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置媒体流类型
			mediaPlayer.setOnBufferingUpdateListener(this);
			mediaPlayer.setOnPreparedListener(this);
			mediaPlayer.setOnCompletionListener(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 每一秒触发一次
		mTimer.schedule(timerTask, 0, 1000);
	}

	// 初始化播放器
	public MusicPlayer(SeekBar seekBar, TextView start, TextView max,
			ImageButton control) {
		this(seekBar);
		this.start = start;
		this.max = max;
		this.control = control;
	}

	public void resetView(SeekBar seekBar, TextView start, TextView max,
			ImageButton control) {
		this.start = start;
		this.max = max;
		this.control = control;
		this.seekBar = seekBar;
		this.seekBar.setOnSeekBarChangeListener(this);
	};

	// 计时器
	TimerTask timerTask = new TimerTask() {

		@Override
		public void run() {
			if (mediaPlayer == null)
				return;
			// if (mediaPlayer.isPlaying() && seekBar.isPressed() == false) {
			handler.sendEmptyMessage(0); // 发送消息

			// }
		}
	};

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (mediaPlayer == null) {
				return;
			}
			int position = mediaPlayer.getCurrentPosition();
			int duration = mediaPlayer.getDuration();
			if (duration > 0) {
				// 计算进度（获取进度条最大刻度*当前音乐播放位置 / 当前音乐时长）
				long pos = seekBar.getMax() * position / duration;
				seekBar.setProgress((int) pos);
				if (start != null && max != null) {
					start.setText(ShowTime(position));
					max.setText(ShowTime(duration));
				}
				if (seekBar.getMax() == pos) {
					// pos = (long) 0.1;
					// seekBar.setProgress((int) pos);
					mediaPlayer.seekTo(1);
					play();
					pause();
				}
			}
		};
	};

	public boolean isPlaying() {
		return mediaPlayer.isPlaying();
	}

	public void play() {
		play = false;
		if (control != null)
			control.setImageResource(R.drawable.btn_play);
		mediaPlayer.start();
	}

	/**
	 * 
	 * @param url
	 *            url地址
	 */
	public void playUrl(String url) {
		if (control != null)
			control.setImageResource(R.drawable.btn_play);
		try {
			mediaPlayer.reset();
			mediaPlayer.setDataSource(url); // 设置数据源
			mediaPlayer.prepare(); // prepare自动播放
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 暂停
	public void pause() {
		play = true;
		if (control != null)
			control.setImageResource(R.drawable.btn_pause);
		if (mediaPlayer != null) {
			// if (mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
			// } else {
			// mediaPlayer.stop();
			// }
		}
	}

	// 停止
	public void stop() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
		}
	}

	// 销毁
	public void destroy() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		if (play) {
		} else {
			if (control != null)
				control.setImageResource(R.drawable.btn_play);
			if (progerss != null)
				progerss.setVisibility(View.GONE);
			mp.start();
		}
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		if (control != null)
			control.setImageResource(R.drawable.btn_pause);
	}

	/**
	 * 缓冲更新
	 */
	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		seekBar.setSecondaryProgress(percent);
		if (mediaPlayer.getDuration() != 0) {
			int currentProgress = seekBar.getMax()
					* mediaPlayer.getCurrentPosition()
					/ mediaPlayer.getDuration();
			Log.e(currentProgress + "% play", percent + " buffer");
		} else {

		}
	}

	public String ShowTime(int time) {
		// 将ms转换为s
		time /= 1000;
		// 求分
		int minute = time / 60;
		// 求秒
		int second = time % 60;
		minute %= 60;
		return String.format("%02d:%02d", minute, second);
	}

	int progress;

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// 原本是(progress/seekBar.getMax())*player.mediaPlayer.getDuration()
		this.progress = progress * mediaPlayer.getDuration() / seekBar.getMax();
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// seekTo()的参数是相对与影片时间的数字，而不是与seekBar.getMax()相对的数字
		mediaPlayer.seekTo(progress);
		String time = ShowTime(mediaPlayer.getCurrentPosition());
		start.setText(time);
	}
}
