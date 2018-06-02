package com.jiagu.mobile.tourguide.activities;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.activities.bases.TitleDrawerActivity;
import com.jiagu.mobile.tourguide.adapter.SceneDetialListViewAdapter;
import com.jiagu.mobile.tourguide.bean.Appraise;
import com.jiagu.mobile.tourguide.utils.HttpUtil;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.tourguide.utils.UesrInfo;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
/**
 * 
 * @author Administrator 评论查看界面 高磊
 * 
 */
public class CommentActivity extends TitleDrawerActivity implements OnRefreshListener<ListView>, OnLastItemVisibleListener {
	private PullToRefreshListView listview;
	private SceneDetialListViewAdapter adapter;
	private ArrayList<Appraise> list;
	private String id;
	private int a = 2;
	private int count;

	@Override
	@SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commen);
		listview = (PullToRefreshListView) findViewById(R.id.activity_commen_listview);
		id = getIntent().getStringExtra("id");
		getPingLunData(id);
		listview.setOnRefreshListener(this);
		listview.setOnLastItemVisibleListener(this);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		setAppName(UesrInfo.area);
		appName.setText(UesrInfo.area + "手机导游");
	}
	private void getPingLunData(final String id) {
		RequestParams params = new RequestParams();
		params.put("appraiseId", id);
		params.put("pages", "1");
		params.put("type", "8");
		String path = Path.SERVER_ADRESS + "scenic/scenicAppraise.htm";
		HttpUtil.post(path, params, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(Throwable arg0, String arg1) {
				super.onFailure(arg0, arg1);
				count++;
				if (count<3) {
					getPingLunData(id);
				}else{
					Toast.makeText(getApplicationContext(), "请检查您的网络",Toast.LENGTH_LONG).show();
					hideProgress();
				}
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
								CommentActivity.this, list);
						listview.setAdapter(adapter);
						listview.onRefreshComplete();
						a = 2;
					} else {
						Toast.makeText(CommentActivity.this, "服务器在打盹",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(CommentActivity.this, "服务器在打盹",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	public void onLastItemVisible() {
		RequestParams params = new RequestParams();
		params.put("appraiseId", id);
		params.put("pages", ""+a);
		params.put("type", "8");
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
				if (!(response == null)) {
					JSONObject object = JSONObject.parseObject(response);
					JSONArray top = object.getJSONArray("records");
					if (!(top == null)) {
						List<Appraise> data = JSONArray.parseArray(
								top.toJSONString(), Appraise.class);
						for (Appraise appraise : data) {
							list.add(appraise);
						}
						adapter.setData(list);
						adapter.notifyDataSetChanged();
						a++;
					} else {
						Toast.makeText(CommentActivity.this, "服务器在打盹",Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(CommentActivity.this, "服务器在打盹",Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		getPingLunData(id);
	}
}
