package com.jiagu.mobile.tourguide.activities;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.jiagu.mobile.tourguide.adapter.SceneDetialListViewAdapter;
import com.jiagu.mobile.tourguide.bean.Appraise;
import com.jiagu.mobile.tourguide.bean.ScenicPlanning;
import com.jiagu.mobile.tourguide.utils.FileTools;
import com.jiagu.mobile.tourguide.utils.HttpUtil;
import com.jiagu.mobile.tourguide.utils.MyImageLoader;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.tourguide.utils.UesrInfo;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

//活动详情		高磊
public class ExerciseDetailActivity extends TitleDrawerActivity implements
		OnClickListener {
	private ImageButton imageReturn;
	private ImageView iv02;
	private TextView tv01, tv02, tv03, tv04, tv05, tv06;
	private ArrayList<ScenicPlanning> data;
	private String id;
	private String photoId;
	private TextView text;
	private EditText et;
	private ListView listView;
	private int Pages = 2;
	private SceneDetialListViewAdapter adapter;
	private ArrayList<Appraise> list;
	static int count = 0;

	@Override
	@SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exerxisedetail);

		Intent i = getIntent();
		id = i.getStringExtra("id");
		photoId = i.getStringExtra("photoId");

		imageReturn = (ImageButton) findViewById(R.id.activity_exercisedetail_image_return);
		listView = (ListView) findViewById(R.id.exercisedetail_listview);

		// 添加头部试图
		View topView = LayoutInflater.from(ExerciseDetailActivity.this)
				.inflate(R.layout.activity_exercisedetail_top, null);
		iv02 = (ImageView) topView
				.findViewById(R.id.activity_exercisedetail_image_02);
		tv01 = (TextView) topView.findViewById(R.id.exercisedetail_tv_01);
		tv02 = (TextView) topView.findViewById(R.id.exercisedetail_tv_02);
		tv03 = (TextView) topView.findViewById(R.id.exercisedetail_tv_03);
		tv04 = (TextView) topView.findViewById(R.id.exercisedetail_tv_04);
		tv05 = (TextView) topView.findViewById(R.id.exercisedetail_tv_05);
		tv06 = (TextView) topView.findViewById(R.id.exercisedetail_tv_06);
		text = (TextView) topView.findViewById(R.id.qingjia);
		et = (EditText) topView.findViewById(R.id.feature_details_et);
		et.setVisibility(View.GONE);
		tv02.setVisibility(View.GONE);
		tv03.setVisibility(View.GONE);
		text.setVisibility(View.GONE);
		listView.addHeaderView(topView);
		// 添加底部试图
		View bottom = LayoutInflater.from(ExerciseDetailActivity.this).inflate(
				R.layout.bottom_button, null);
		TextView more = (TextView) bottom.findViewById(R.id.bottom_textview);
		more.setOnClickListener(this);
		listView.addFooterView(bottom);

		getData(id);
		getPingLunData(id);

		imageReturn.setOnClickListener(this);
		iv02.setOnClickListener(this);
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
	
	/*
	 * 获取近期活动加入缓存
	 */
	private void getData(final String id) {
		showProgress();
		RequestParams params = new RequestParams();
		params.put("id", id);
		String path = Path.SERVER_ADRESS+ "scenicPlanning/scenicPlanningDetial.htm";
		HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				super.onFailure(arg0, arg1);
				hideProgress();
				count++;
				if (count < 3){
					FileTools.writeLog("exercisdetail.txt", "======>>>>>>活动详情数据------count:"+count+"Throwable:"+arg0+"--------"+arg1);
					getData(id);
				}else{
					count= 0;
					Toast.makeText(getApplicationContext(), "请检查您的网络",
							Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				if (response!=null&&!response.equals("")) {
					JSONObject object = JSONObject.parseObject(response);
					JSONArray top = object.getJSONArray("records");
					String result = object.getString("result");
					if (top == null || top.equals("") || result.equals("4")) {
						Toast.makeText(ExerciseDetailActivity.this, "服务器连接失败",
								Toast.LENGTH_SHORT).show();
					} else if(result.equals("0")){
						data = (ArrayList<ScenicPlanning>) JSONArray.parseArray(
								top.toJSONString(), ScenicPlanning.class);
						if (data.get(0).getImagurl1()!=null&&!data.get(0).getImagurl1().equals("")) {
							MyImageLoader.loadImage(Path.IMAGER_ADRESS+ data.get(0).getImagurl1(), iv02);
						}
						if (data.get(0).getTitle()!=null&&!data.get(0).getTitle().equals("")) {
							tv01.setText(data.get(0).getTitle());
						}
//						tv02.setText("活动时间:" + data.get(0).getTimeStar());
//						tv03.setText("订票电话:"+ data.get(0).getScenic().getPhonenumber());
						if (data.get(0).getScenic()!=null&&data.get(0).getScenic().getSteet()!=null&&!data.get(0).getScenic().getSteet().equals("")) {
							tv04.setText("景区地址:" + data.get(0).getScenic().getSteet());
						}else if(data.get(0).getTouristadmin()!=null&&data.get(0).getTouristadmin().getAdress()!=null&&!data.get(0).getTouristadmin().getAdress().equals("")){
							tv04.setText("主办地址:" + data.get(0).getTouristadmin().getAdress());
						}else{
							tv04.setVisibility(View.GONE);
						}
						if (data.get(0).getText()!=null&&!data.get(0).getText().equals("")) {
							tv05.setText(data.get(0).getText());
						}else{
							tv05.setVisibility(View.GONE);
						}
						if (data.get(0).getScenic()!=null&&data.get(0).getScenic().getScenicname()!=null&!data.get(0).getScenic().getScenicname().equals("")) {
							tv06.setText("发布单位:"+ data.get(0).getScenic().getScenicname());
						}else if(data.get(0).getTouristadmin()!=null&&data.get(0).getTouristadmin().getTitle()!=null&&!data.get(0).getTouristadmin().getTitle().equals("")){
							tv06.setText("发布单位:"+ data.get(0).getTouristadmin().getTitle());
						}else{
							tv06.setVisibility(View.GONE);
						}
						et.setVisibility(View.VISIBLE);
						text.setVisibility(View.VISIBLE);
					}else{
						Toast.makeText(ExerciseDetailActivity.this, "服务器连接失败",
								Toast.LENGTH_SHORT).show();
					}
					hideProgress();
				}else{
					finish();
				}
				
			}
		});
	}

	private void getPingLunData(String id) {
		RequestParams params = new RequestParams();
		params.put("appraiseId", id);
		params.put("pages", "1");
		params.put("type", "3");
		String path = Path.SERVER_ADRESS + "scenic/scenicAppraise.htm";
		HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				Toast.makeText(getApplicationContext(), "请检查您的网络",
						Toast.LENGTH_LONG).show();
				super.onFailure(arg0, arg1);
				hideProgress();
				FileTools.writeLog("exercisdetail.txt", "======>>>>>>活动详情评论数据------Throwable:"+arg0+"--------"+arg1);
			}

			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				
				if (response != null&&!response.equals("")) {
					JSONObject object = JSONObject.parseObject(response);
					JSONArray top = object.getJSONArray("records");
					if (top != null&&!top.equals("")) {
						list = (ArrayList<Appraise>) JSONArray.parseArray(
								top.toJSONString(), Appraise.class);
						adapter = new SceneDetialListViewAdapter(
								ExerciseDetailActivity.this, list);
						listView.setAdapter(adapter);
					} else {

					}
				} else {

				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.activity_exercisedetail_image_return:
			onBackPressed();
			break;
		case R.id.bottom_textview:
			// 评论查看更多
			RequestParams params = new RequestParams();
			params.put("appraiseId", id);
			params.put("pages", "" + Pages);
			params.put("type", "3");
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
					if (response != null) {
						JSONObject object = JSONObject.parseObject(response);
						JSONArray top = object.getJSONArray("records");
						String result = object.getString("result");
						if (top != null&&!top.equals("")) {
							ArrayList<Appraise> lists = (ArrayList<Appraise>) JSONArray
									.parseArray(top.toJSONString(),
											Appraise.class);
							for (Appraise appraise : lists) {
								list.add(appraise);
							}
							adapter.setData(list);
							adapter.notifyDataSetChanged();
							Pages++;
						} else if(result.equals("30")){
							Toast.makeText(getApplicationContext(), "已是最后一页",
									Toast.LENGTH_LONG).show();
						}
					} else {

					}
				}
			});
			break;
		case R.id.qingjia:
			if (et.getText().toString().trim() == null||et.getText().toString().trim().equals("")) {
				Toast.makeText(ExerciseDetailActivity.this, "请输入评价内容",
						Toast.LENGTH_SHORT).show();
			} else {
				UesrInfo.getText(ExerciseDetailActivity.this, UesrInfo
						.getTourIDid(), UesrInfo.getUsername(), id, "3", et
						.getText().toString().trim(), data.get(0).getTitle(),
						et);
				getPingLunData(id);
			}
			break;
		case R.id.activity_exercisedetail_image_02:
			Intent i = new Intent();
			i.putExtra("photoId", photoId);
			i.putExtra("path", Path.SERVER_ADRESS
					+ "characteristic/photoList.htm");
			i.setClass(ExerciseDetailActivity.this, PhotoAlbumActivity.class);
			startActivity(i);
			break;
		}
	}
}
