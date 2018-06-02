package com.jiagu.mobile.zhifu;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.activities.bases.TitleDrawerActivity;
import com.jiagu.mobile.tourguide.utils.HttpUtil;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.tourguide.utils.UesrInfo;
import com.jiagu.mobile.zhifu.adapter.ArticleAdapter;
import com.jiagu.mobile.zhifu.bean.Article;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ArticleActivity extends TitleDrawerActivity implements
		OnClickListener {
	private ListView mListView;
	private String id, name, adress, phonenumber,nameS,cargoclassify;
	private TextView text;
	private ArrayList<Article> list;
	private ArticleAdapter adapter;
	private int count = 0;

	@Override
	@SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article);
		ImageView mReturn = (ImageView) findViewById(R.id.activity_article_iv_01);
		text = (TextView) findViewById(R.id.activity_article_tv_01);
		mListView = (ListView) findViewById(R.id.activity_article_listview);
		mListView.setAdapter(null);
		mReturn.setOnClickListener(this);
		text.setOnClickListener(this);
		id = getIntent().getStringExtra("id");
		name = getIntent().getStringExtra("name");
		nameS = getIntent().getStringExtra("nameS");
		adress = getIntent().getStringExtra("adress");
		phonenumber = getIntent().getStringExtra("phonenumber");
		cargoclassify = getIntent().getStringExtra("cargoclassify");
		getData();
	}

	private void getData() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.put("orderId", id);
		params.put("type", "1");
		String path = Path.SERVER_ADRESS + "admission/orderRefund.htm";
		HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				super.onFailure(arg0, arg1);
				count++;
				if (count<3) {
					getData();
				}else{
					count=0;
					Toast.makeText(getApplicationContext(), "请检查您的网络",Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				if (response == null||response.equals("")) {
					Toast.makeText(ArticleActivity.this, "服务器在打盹",Toast.LENGTH_SHORT).show();
				} else {
					JSONObject object = JSONObject.parseObject(response);
					JSONArray top = object.getJSONArray("records");
					String result = object.getString("result");
					if (result.equals("0")) {
						list = (ArrayList<Article>) JSONArray.parseArray(top.toJSONString(), Article.class);
						adapter = new ArticleAdapter(ArticleActivity.this,list, name, adress, phonenumber,cargoclassify);
						mListView.setAdapter(adapter);
					} else {
						if (top==null||top.toString().equals("")) {
							Toast.makeText(ArticleActivity.this, "暂无数据",Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(ArticleActivity.this, result,Toast.LENGTH_SHORT).show();
							list = (ArrayList<Article>) JSONArray.parseArray(top.toJSONString(), Article.class);
							adapter = new ArticleAdapter(ArticleActivity.this,list, name, adress, phonenumber,cargoclassify);
							mListView.setAdapter(adapter);
						}
					}
				}
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.activity_article_iv_01:
			onBackPressed();
			break;
		case R.id.activity_article_tv_01:
			String fenxiang = "";
			ArrayList<Article> data = adapter.getData();
			for (int i = 0; i < data.size(); i++) {
				if (data.get(i).getChecked()) {
					fenxiang = fenxiang + data.get(i).getNumber() + ",";
				}
			}
			if (fenxiang.equals("")) {
				Toast.makeText(ArticleActivity.this, "请选择要分享的商品",Toast.LENGTH_SHORT).show();
			} else {
				showShare(fenxiang.substring(0, fenxiang.length() - 1));
			}
			break;

		default:
			break;
		}
	}

	// 分享
	private void showShare(String text) {
		ShareSDK.initSDK(ArticleActivity.this);
		OnekeyShare oks = new OnekeyShare();
		// 分享时Notification的图标和文字
		oks.setNotification(R.drawable.ic_launcher,ArticleActivity.this.getString(R.string.app_name));
		oks.setText("[手机导游]"+"商家名称:"+ nameS+ ",物品名称:" + name + "," + "商家地址:" + adress + ","+ "商家电话:" + phonenumber + "," + "劵码:" + text);
		oks.setTitle("");
		oks.setTitleUrl("");
		oks.setImageUrl("");
		// oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/05/21/oESpJ78_533x800.jpg");
		// oks.setFilePath("http://www.shoujidaoyou.cn/download/sjdy.apk");
		// 启动分享GUI
		oks.show(ArticleActivity.this);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		setAppName(UesrInfo.area);
		appName.setText(UesrInfo.area + "手机导游");
	}
}
