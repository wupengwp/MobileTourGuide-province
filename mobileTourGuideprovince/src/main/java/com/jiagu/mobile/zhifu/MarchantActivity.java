package com.jiagu.mobile.zhifu;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jiagu.mobile.daohang.RouteGuideDemo;
import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.activities.PhotoAlbumActivity;
import com.jiagu.mobile.tourguide.activities.bases.TitleDrawerActivity;
import com.jiagu.mobile.tourguide.utils.HttpUtil;
import com.jiagu.mobile.tourguide.utils.MyImageLoader;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.tourguide.utils.UesrInfo;
import com.jiagu.mobile.zhifu.adapter.MerchatAdapter;
import com.jiagu.mobile.zhifu.bean.Admission;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MarchantActivity extends TitleDrawerActivity implements OnClickListener,
		OnLastItemVisibleListener, OnRefreshListener<ListView>,
		OnItemClickListener,OnGetGeoCoderResultListener {
	private PullToRefreshListView listView;
	private ImageView mImageView;
	private TextView name,site,phone,number;
	private ArrayList<Admission> data;
	private ArrayList<Admission> list;
	private MerchatAdapter adapter;
	private String id,cargoClassify;
	private int page = 2;
	private int count = 0;
	GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	@Override
	@SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_merchant);
		// 初始化搜索模块，注册事件监听
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		
		id = getIntent().getStringExtra("id");
		cargoClassify = getIntent().getStringExtra("cargoClassify");
		ImageView mReturn = (ImageView) findViewById(R.id.activity_merchant_image_return);
		listView = (PullToRefreshListView) findViewById(R.id.activity_merchant_listview);
		View topView = LayoutInflater.from(MarchantActivity.this).inflate(R.layout.activity_merchant_top, null);
		mImageView = (ImageView) topView.findViewById(R.id.activity_merchant_top_viewpager);
		name = (TextView) topView.findViewById(R.id.activity_merchant_top_name_01);
		site = (TextView) topView.findViewById(R.id.activity_merchant_top_site_02);
		phone = (TextView) topView.findViewById(R.id.activity_merchant_top_phone_03);
		number = (TextView) topView.findViewById(R.id.activity_merchant_top_number_04);
		listView.getRefreshableView().addHeaderView(topView);
		listView.setAdapter(null);
		getData(id,cargoClassify);
		mReturn.setOnClickListener(this);
		site.setOnClickListener(this);
		phone.setOnClickListener(this);
		mImageView.setOnClickListener(this);
		listView.setOnItemClickListener(this);
		listView.setOnRefreshListener(this);
		listView.setOnLastItemVisibleListener(this);
	}

	private void getData(final String id,final String cargoClassify) {
		// TODO Auto-generated method stub
		showProgress();
		RequestParams params = new RequestParams();
		params.put("scenicId", id);
		params.put("cargoClassify", cargoClassify);
		params.put("pages", "1");
		String path = Path.SERVER_ADRESS + "admission/shopDetial.htm";
		HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				super.onFailure(arg0, arg1);
				count++;
				if (count < 3) {
					getData(id, cargoClassify);
				}else{
					count = 0;
					Toast.makeText(MarchantActivity.this, "请检查您的网络",
							Toast.LENGTH_LONG).show();
					onBackPressed();
				}
				hideProgress();
			}

			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				if (response!=null&&!response.equals("")) {
					JSONObject object = JSONObject.parseObject(response);
					JSONArray top = object.getJSONArray("records");
					String result = object.getString("result");
					if (result.equals("0")) {
						data = (ArrayList<Admission>) JSONArray.parseArray(top.toJSONString(), Admission.class);
						list = data.get(0).getAdmissionList();
						if (list.get(0).getImageurl()!=null&&!list.get(0).getImageurl().equals("")) {
							switch (list.get(0).getCargoclassify().trim()) {
							case "2":
								MyImageLoader.loadImage(list.get(0).getImageurl(), mImageView);
								break;
								
							default:
								MyImageLoader.loadImage(Path.IMAGER_ADRESS+list.get(0).getImageurl(), mImageView);
								break;
							}				
							
						}
						name.setText(data.get(0).getPhotoAll().getTitle());
						site.setText(data.get(0).getAdress());
						phone.setText(data.get(0).getPhonenumber());
						number.setText("团购("+data.get(0).getAdmissionCount()+")");
						list = data.get(0).getAdmissionList();
						adapter = new MerchatAdapter(MarchantActivity.this, list);
						listView.setAdapter(adapter);
						listView.onRefreshComplete();
					}else if(result.equals("30")){
						Toast.makeText(MarchantActivity.this, "该商品暂无商铺信息",
								Toast.LENGTH_LONG).show();
						onBackPressed();
					}else{
						Toast.makeText(MarchantActivity.this, "请检查您的网络",
								Toast.LENGTH_LONG).show();
						onBackPressed();
					}
				}else{
					Toast.makeText(MarchantActivity.this, "请检查您的网络",
							Toast.LENGTH_LONG).show();
					onBackPressed();
				}
				hideProgress();
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent i = new Intent();
		switch (v.getId()) {
		case R.id.activity_merchant_image_return:
			onBackPressed();
			break;
		case R.id.activity_merchant_top_viewpager:
			if (cargoClassify.equals("2")) {
				
			}else {
				if (cargoClassify.equals("0")) {
					i.putExtra("id", data.get(0).getPhotoAll().getPhotoid());
					i.putExtra("path", Path.SERVER_ADRESS+"scenic/scenicPhoto.htm");
				}else if(cargoClassify.equals("1")){
					i.putExtra("photoId", data.get(0).getPhotoAll().getPhotoid());
					i.putExtra("path", Path.SERVER_ADRESS+"characteristic/photoList.htm");
				}
				i.setClass(MarchantActivity.this, PhotoAlbumActivity.class);
				startActivity(i);
			}
			
			break;
		case R.id.activity_merchant_top_phone_03:// 打电话
			 i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
					+ data.get(0).getPhonenumber()));
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
			break;
		case R.id.activity_merchant_top_site_02:// 定位
			
			
			if (data.get(0).getX().equals("") || data.get(0).getY().equals("")) {
				mSearch.geocode(new GeoCodeOption().city("").address(data.get(0).getAdress()));	
			} else {
				i.putExtra("x", data.get(0).getX());
				i.putExtra("y", data.get(0).getY());
				i.putExtra("name", list.get(0).getTitle());
				i.setClass(MarchantActivity.this, RouteGuideDemo.class);
				startActivity(i);
			}
			
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent i = new Intent();
		if (arg2>1) {
			i.putExtra("id", list.get(arg2-2).getId());
			i.setClass(MarchantActivity.this, PurchaseActivity.class);
			startActivity(i);
			finish();
		}
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		getData(id, cargoClassify);
	}

	@Override
	public void onLastItemVisible() {
		// TODO Auto-generated method stub
		showProgress();
		listView.setSelected(true);
		RequestParams params = new RequestParams();
		params.put("scenicId", id);
		params.put("cargoClassify", cargoClassify);
		params.put("pages", ""+page);
		String path = Path.SERVER_ADRESS + "admission/shopDetial.htm";
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
				if (response!=null&&!response.equals("")) {
					JSONObject object = JSONObject.parseObject(response);
					JSONArray top = object.getJSONArray("records");
					String result = object.getString("result");
					if (result.equals("0")) {
						data = (ArrayList<Admission>) JSONArray.parseArray(top.toJSONString(), Admission.class);
						ArrayList<Admission> lists = data.get(0).getAdmissionList();
						for (int i = 0; i < lists.size(); i++) {
							list.add(lists.get(i));
						}
						adapter.setData(list);
						adapter.notifyDataSetChanged();
						page++;
					}else if(result.equals("30")){
						Toast.makeText(MarchantActivity.this, "已是最后一页",
								Toast.LENGTH_LONG).show();
					}else {
						Toast.makeText(MarchantActivity.this, "服务器在打盹",
								Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(MarchantActivity.this, "请检查您的网络",
							Toast.LENGTH_LONG).show();
				}
				hideProgress();
			}
		});
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		setAppName(UesrInfo.area);
		appName.setText(UesrInfo.area + "手机导游");
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		// TODO Auto-generated method stub
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(MarchantActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
					.show();
			return;
		}
		Intent i = new Intent();
		i.setClass(MarchantActivity.this, RouteGuideDemo.class);
		i.putExtra("x", "" + result.getLocation().longitude);
		i.putExtra("y", "" + result.getLocation().latitude);
		i.putExtra("name", list.get(0).getTitle());	
		
		startActivity(i);
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void onDestroy() {
		mSearch.destroy();
		super.onDestroy();
	}
}
