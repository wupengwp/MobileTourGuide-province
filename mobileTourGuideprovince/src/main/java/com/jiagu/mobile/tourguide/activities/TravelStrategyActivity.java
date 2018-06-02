package com.jiagu.mobile.tourguide.activities;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.activities.bases.TitleDrawerActivity;
import com.jiagu.mobile.tourguide.adapter.TravelStrategyAdapter;
import com.jiagu.mobile.tourguide.bean.TravelStrategy;
import com.jiagu.mobile.tourguide.utils.FileTools;
import com.jiagu.mobile.tourguide.utils.HttpUtil;
import com.jiagu.mobile.tourguide.utils.MyImageLoader;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.tourguide.utils.UesrInfo;
import com.jiagu.mobile.tourguide.widget.CircleImageView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * @author Administrator 攻略详情 高磊 2015-01-04
 * 
 */
public class TravelStrategyActivity extends TitleDrawerActivity implements
		OnClickListener ,OnItemClickListener{
	private ListView listview;
	private String id;
	public String tourid;
	private ImageView image;
	private String title;
	private CircleImageView mCircleImageView;
	private EditText et;
	private String text;
	private TextView qinglun;
	private TextView more;
	private ArrayList<TravelStrategy> data;
	private TravelStrategyAdapter adapter;
	public static TravelStrategyActivity travelStrategyActivity;
	@Override
	@SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_travelstrategy);
		travelStrategyActivity= this;
		ImageButton mImageReturn = (ImageButton) findViewById(R.id.activity_travelstategy_image_return);
		listview = (ListView) findViewById(R.id.travelstrategy_listview);
		// 添加头部试图
		View topView = LayoutInflater.from(TravelStrategyActivity.this)
				.inflate(R.layout.travelstrategy_listview_top, null);

		TextView name = (TextView) topView
				.findViewById(R.id.travelstrategy_text_02);
		TextView title = (TextView) topView
				.findViewById(R.id.travelstrategy_text_03);
		image = (ImageView) topView
				.findViewById(R.id.activity_travelstategy_top_image);
		image.setVisibility(View.GONE);
		mCircleImageView = (CircleImageView) topView
				.findViewById(R.id.login_user_logo_iv);
		Intent i = getIntent();
		if (i.getStringExtra("name") == null
				|| i.getStringExtra("name").equals("")) {
			name.setText("匿名游客");
		} else {
			name.setText(i.getStringExtra("name"));
		}
		text = i.getStringExtra("title");
		if (i.getStringExtra("title") != null) {
			title.setText(i.getStringExtra("title"));
		} else {
			name.setText("未知地域");
		}
		id = i.getStringExtra("id");
		listview.addHeaderView(topView,null,false);//防止头部文件可点击
		// 添加底部试图
		View bottomView = LayoutInflater.from(TravelStrategyActivity.this)
				.inflate(R.layout.travelstrategy_bottom, null);
		et = (EditText) bottomView.findViewById(R.id.feature_details_et);
		qinglun = (TextView) bottomView.findViewById(R.id.pinglun);
		more = (TextView) bottomView.findViewById(R.id.bottom_text);
		listview.addFooterView(bottomView,null,false);

		getData();

		// listview.setOnItemLongClickListener(this);
		 listview.setOnItemClickListener(this);
		more.setOnClickListener(this);
		mImageReturn.setOnClickListener(this);
		image.setOnClickListener(this);
		qinglun.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		setAppName(UesrInfo.area);
		appName.setText(UesrInfo.area + "手机导游");
		getData();
	}

	@Override
	public void onClick(View v) {
		Intent i = new Intent();
		switch (v.getId()) {
		case R.id.activity_travelstategy_image_return:
			onBackPressed();
			break;
		case R.id.bottom_text:
			i.putExtra("id", id);
			i.setClass(TravelStrategyActivity.this, CommentActivity.class);
			startActivity(i);
			break;
		case R.id.pinglun:
			if (et.getText().toString().trim().equals("")
					|| et.getText().toString().trim() == null) {
				Toast.makeText(TravelStrategyActivity.this, "请输入评价内容",
						Toast.LENGTH_SHORT).show();
			} else {
				UesrInfo.getText(TravelStrategyActivity.this,
						UesrInfo.getTourIDid(), UesrInfo.getUsername(), id,
						"8", et.getText().toString().trim(), text, et);
			}
			break;
		case R.id.activity_travelstategy_top_image:
			// 添加攻略
			i.putExtra("a", "a");
			i.putExtra("id", id);
			i.putExtra("title", title);
			i.setClass(TravelStrategyActivity.this,
					NewIntroductionsdActivity.class);
			startActivity(i);
			break;
		}
	}

	private void getData() {
		RequestParams params = new RequestParams(); // 绑定参数
		params.put("strategyId", id);
		String path = Path.SERVER_ADRESS + "personalCenter/touristNotes.htm";
		HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				super.onFailure(arg0, arg1);
				hideProgress();
				Toast.makeText(getApplicationContext(), "请检查您的网络",Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(String response) {
				
				FileTools.writeLog("st.txt", response);
				
				super.onSuccess(response);
				if (response == null || response.equals("")) {
					Toast.makeText(TravelStrategyActivity.this, "服务器在打盹",
							Toast.LENGTH_LONG).show();
				} else {
					JSONObject object = JSONObject.parseObject(response);
					JSONArray top = object.getJSONArray("records");
					String result = object.getString("result");
					if (top == null || top.equals("") || result.equals("4")) {
						Toast.makeText(TravelStrategyActivity.this, "服务器在打盹",
								Toast.LENGTH_LONG).show();
					} else {
						data = (ArrayList<TravelStrategy>) JSONArray
								.parseArray(top.toJSONString(),
										TravelStrategy.class);
						adapter = new TravelStrategyAdapter(
								TravelStrategyActivity.this, data);
						if (!data.get(0).getTourIcon().equals("")
								&& data.get(0).getTourIcon() != null) {
							MyImageLoader.loadImage(Path.IMAGER_ADRESS
									+ data.get(0).getTourIcon(),
									mCircleImageView);
						}
						tourid = data.get(0).getTourid();
						if (tourid.equals(UesrInfo.getTourIDid())) {
							image.setVisibility(View.VISIBLE);
						}
						title = data.get(0).getTitle();
						listview.setAdapter(adapter);
					}
				}
			}
		});
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
		// TODO Auto-generated method stub
		//这里position-1是因为listview添加了header,以至于listview从header开始
		AlertDialog.Builder builder = new Builder(TravelStrategyActivity.this);
		builder.setPositiveButton("修改", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (UesrInfo.getTourIDid().equals(TravelStrategyActivity.travelStrategyActivity.tourid)) {
					Intent i = new Intent();					
					i.putExtra("index", position-1);
					i.putExtra("photoId", data.get(position-1).getPhotoid());
					i.putExtra("typeA", "xiugai");
					i.putExtra("pathA", Path.SERVER_ADRESS
							+ "characteristic/photoList.htm");
					i.putExtra("a", "");
					i.putExtra("id", data.get(position-1).getId());
					i.putExtra("title", "" + data.get(position-1).getTitle());
					i.putExtra("text", "" + data.get(position-1).getText());
					i.putExtra("timer", "" + data.get(position-1).getNdays());
					i.setClass(TravelStrategyActivity.this, NewIntroductionsdActivity.class);
					TravelStrategyActivity.this.startActivity(i);							
				}else{
					Toast.makeText(TravelStrategyActivity.this, "非本人发布不可修改", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		builder.setNegativeButton("查看", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Intent i = new Intent();
				i.putExtra("photoId", data.get(position-1).getPhotoid());
				i.putExtra("path", Path.SERVER_ADRESS
						+ "characteristic/photoList.htm");
				i.setClass(TravelStrategyActivity.this, PhotoAlbumActivity.class);
				TravelStrategyActivity.this.startActivity(i);
			
			}
		});
		builder.create().show();
		
	}

	
}
