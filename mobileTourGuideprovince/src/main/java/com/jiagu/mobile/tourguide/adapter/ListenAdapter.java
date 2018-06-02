package com.jiagu.mobile.tourguide.adapter;

import java.util.ArrayList;
import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.bean.Listen;
import com.jiagu.mobile.tourguide.utils.MusicPlayer;
import com.jiagu.mobile.tourguide.utils.MyImageLoader;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.tourguide.utils.UesrInfo;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class ListenAdapter extends BaseAdapter {
	private Context context;
	private static ArrayList<Listen> data;
	private LayoutInflater inflater;
	private PopupWindow pop;
	private static MusicPlayer player;
	private int a = 1000000000;
	private String type = "a";

	public ListenAdapter(Context context, ArrayList<Listen> data, String type) {
		super();
		this.context = context;
		this.data = data;
		this.type = type;
		inflater = LayoutInflater.from(context);
		initMusicPlayerPop();
	}

	public ArrayList<Listen> getData() {
		return data;
	}

	public void setData(ArrayList<Listen> data) {
		this.data = data;
	}

	public void addData(ArrayList<Listen> data) {
		this.data.addAll(data);
		notifyDataSetChanged();
	}

	public int getA() {
		return a;
	}

	public void setA() {
		this.a = 1000000000;
		notifyDataSetChanged();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(
					R.layout.activity_listen_listview_item, null);
			holder.name = (TextView) convertView
					.findViewById(R.id.activity_listen_listview_item_tv_01);
			holder.image = (ImageView) convertView
					.findViewById(R.id.activity_listen_listview_item_iv_01);
			holder.imager = (ImageView) convertView
					.findViewById(R.id.activity_listen_listview_item_iv_02);
			holder.mCheckBox = (ImageView) convertView
					.findViewById(R.id.checkBox1);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final Listen listen = data.get(position);
		if (!(listen.getScenictitle().equals("") || listen.getScenictitle() == null)) {
			holder.name.setText(listen.getScenictitle());
		}
		if (!(listen.getCharacteristicTitle().equals("") || listen
				.getCharacteristicTitle() == null)) {
			holder.name.setText(listen.getCharacteristicTitle());
		}
		if (!(listen.getAttractitle().equals("") || listen.getAttractitle() == null)) {
			holder.name.setText(listen.getAttractitle());
		}
		if (!(listen.getUrl().equals("") || listen.getUrl() == null)) {
			MyImageLoader.loadImage(Path.IMAGER_ADRESS + listen.getUrl(),
					holder.image);
		}
		if (type.equals("a")) {
			holder.mCheckBox.setVisibility(View.VISIBLE);
		} else {
			holder.mCheckBox.setVisibility(View.GONE);
		}

		final int positions = position;
		final ImageView box = holder.mCheckBox;
		if (listen.getChecked()) {
			box.setImageResource(R.drawable.shoucang);
			data.get(positions).setChecked(true);
		} else {
			box.setImageResource(R.drawable.shoucang1);
			data.get(positions).setChecked(false);
		}
		holder.mCheckBox.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (listen.getChecked()) {
					box.setImageResource(R.drawable.shoucang1);
					data.get(positions).setChecked(false);
				} else {
					box.setImageResource(R.drawable.shoucang);
					data.get(positions).setChecked(true);
				}
			}
		});
		holder.imager.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (a == positions) {
					if (pop.isShowing()) {
						pop.dismiss();
					} else {
						pop.showAtLocation(((Activity) context).getWindow()
								.getDecorView(), Gravity.BOTTOM, 0, 0);
						if (isPlay) {
							isPlay = false;
						} else {
							isPlay = true;
						}
					}
				} else {
					if (!(data.get(positions).getScenictitle() == null || data
							.get(positions).getScenictitle().equals(""))) {
						tv_title.setText(data.get(positions).getScenictitle()
								+ "(收听" + data.get(positions).getListenCount()
								+ "次)");
					}
					if (!(data.get(positions).getCharacteristicTitle() == null || data
							.get(positions).getCharacteristicTitle().equals(""))) {
						tv_title.setText(data.get(positions)
								.getCharacteristicTitle()
								+ "(收听"
								+ data.get(positions).getListenCount() + "次)");
					}
					if (!(data.get(positions).getAttractitle() == null || data
							.get(positions).getAttractitle().equals(""))) {
						tv_title.setText(data.get(positions).getAttractitle()
								+ "(收听" + data.get(positions).getListenCount()
								+ "次)");
					}
					player.pause();
					a = positions;
					play(Path.VOICE_ADRESS + data.get(positions).getVoiceUrl(),
							data.get(positions).getType(), data.get(positions)
									.getScenicPriId(),
							data.get(positions).getScenicid(),
							data.get(positions).getCharacteristicId(), data
									.get(positions).getAttracid());
					player.play();
					isPlay = false;
				}
			}
		});
		return convertView;
	}

	class ViewHolder {
		public TextView name, time;
		public ImageView image, imager, mCheckBox;
	}

	private void play(final String url, String type, String id,
			String scenicid, String characteristicid, String attracid) {

		if (url == null || url.equals("")) {
			Toast.makeText(context, "服务器在打盹", Toast.LENGTH_SHORT).show();
			return;
		}
		if (url.contains("http://") || url.contains("https://")) {
			if (!pop.isShowing()) {
				UesrInfo.HttpListen(context, id, UesrInfo.getTourIDid(),
						scenicid, characteristicid, attracid, type);
				pop.showAtLocation(((Activity) context).getWindow()
						.getDecorView(), Gravity.BOTTOM, 0, 0);
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
	private TextView tv_title;

	private void initMusicPlayerPop() {
		View view = LayoutInflater.from(context).inflate(
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
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		player = new MusicPlayer(mMusicSeekBar, mThisTime, mMaxTime, mControl);
	}

	public static MusicPlayer stop() {
		return player;
	}

	public static ArrayList<Listen> get() {
		return data;
	}
}
