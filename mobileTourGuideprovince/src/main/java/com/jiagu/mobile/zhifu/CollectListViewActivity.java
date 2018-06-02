package com.jiagu.mobile.zhifu;

import java.util.ArrayList;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.activities.bases.TitleDrawerActivity;
import com.jiagu.mobile.tourguide.utils.HttpUtil;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.tourguide.utils.UesrInfo;
import com.jiagu.mobile.zhifu.adapter.IndentAdapter;
import com.jiagu.mobile.zhifu.bean.Collect;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @ClassName: IndentActivity
 * @Description: 收藏列表界面
 * @author 高磊
 * @date 2015年2月04日 16:08
 * @company 西安甲骨企业文化传播有限公司
 */
public class CollectListViewActivity extends TitleDrawerActivity implements
		OnItemClickListener, OnItemLongClickListener,
		OnRefreshListener<ListView>, OnLastItemVisibleListener, OnClickListener {
	private PullToRefreshListView listView;
	private int pages;
	private ArrayList<Collect> data;
	private IndentAdapter adapter;
	private boolean is = true;
	private int count = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listview);
		TextView name = (TextView) findViewById(R.id.activirty_listview_tv_01);
		ImageView image = (ImageView) findViewById(R.id.activirty_listview_iv_01);
		name.setText("我的收藏");
		listView = (PullToRefreshListView) findViewById(R.id.activity_listview);
		Button mButton = (Button) findViewById(R.id.activity_mysite_bottom_xinjian);
		mButton.setVisibility(View.GONE);
		getData();

		image.setOnClickListener(this);
		listView.setOnItemClickListener(this);
		listView.getRefreshableView().setOnItemLongClickListener(this);
		listView.setOnRefreshListener(this);
		listView.setOnLastItemVisibleListener(this);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		setAppName(UesrInfo.area);
		appName.setText(UesrInfo.area + "手机导游");
	}
	private void getData() {
		showProgress();
		RequestParams params = new RequestParams();
		params.put("pages", "1");
		params.put("tourId", UesrInfo.getTourIDid());
		String path = Path.SERVER_ADRESS
				+ "personalCenter/touristCollectByTourId.htm";
		HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				super.onFailure(arg0, arg1);
				count++;
				if (count<3) {
					getData();
				}else{
					count = 0;
					hideProgress();
					Toast.makeText(getApplicationContext(), "请检查您的网络",
							Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				if (response.equals("") || response == null) {
					Toast.makeText(CollectListViewActivity.this, "服务器在打盹",
							Toast.LENGTH_SHORT).show();
				} else {
					JSONObject object = JSONObject.parseObject(response);
					JSONArray top = object.getJSONArray("records");
					String result = object.getString("result");
					if (result.equals("0")) {
						data = (ArrayList<Collect>) JSONArray.parseArray(
								top.toJSONString(), Collect.class);
						if (data.size() == 0) {
							Toast.makeText(CollectListViewActivity.this,
									"赶紧去收藏吧!!!!", Toast.LENGTH_SHORT).show();
						} else {
							adapter = new IndentAdapter(
									CollectListViewActivity.this, data);
						}
						listView.setAdapter(adapter);
						listView.onRefreshComplete();
						pages = 2;
					} else if (result.equals("30")) {
						Toast.makeText(CollectListViewActivity.this, "已是最后一页",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(CollectListViewActivity.this, "服务器在打盹",
								Toast.LENGTH_SHORT).show();
					}
				}
				hideProgress();
			}
		});
	}

	@Override
	public void onLastItemVisible() {
		// TODO Auto-generated method stub
		showProgress();
		RequestParams params = new RequestParams();
		params.put("pages", "" + pages);
		params.put("tourId", UesrInfo.getTourIDid());
		String path = Path.SERVER_ADRESS + "personalCenter/touristCollectByTourId.htm";
		if (is) {
			is = false;
			HttpUtil.post(path, params, new AsyncHttpResponseHandler() {
				@Override
				public void onFailure(Throwable arg0, String arg1) {
					Toast.makeText(getApplicationContext(), "请检查您的网络",
							Toast.LENGTH_LONG).show();
					super.onFailure(arg0, arg1);
					is = true;
					hideProgress();
				}

				@Override
				public void onSuccess(String response) {
					super.onSuccess(response);
					if (response.equals("") || response == null) {
						Toast.makeText(CollectListViewActivity.this, "服务器在打盹",
								Toast.LENGTH_SHORT).show();
					} else {
						JSONObject object = JSONObject.parseObject(response);
						JSONArray top = object.getJSONArray("records");
						String result = object.getString("result");
						if (result.equals("0")) {
							ArrayList<Collect> list = (ArrayList<Collect>) JSONArray
									.parseArray(top.toJSONString(), Collect.class);
							if (list.size() == 0) {
								Toast.makeText(CollectListViewActivity.this,
										"赶紧去收藏吧!!!!", Toast.LENGTH_SHORT).show();
							} else {
								for (Collect indent : list) {
									data.add(indent);
								}
								adapter.setData(data);
								adapter.notifyDataSetChanged();
								pages++;
							}
						} else if (result.equals("30")) {
							Toast.makeText(CollectListViewActivity.this, "已是最后一页",
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(CollectListViewActivity.this, "服务器在打盹",
									Toast.LENGTH_SHORT).show();
						}
					}
					is = true;
					hideProgress();
				}
			});
		}
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		getData();
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			final int position, long id) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new Builder(CollectListViewActivity.this);
		builder.setMessage("您确定要删除该收藏记录吗？");
		builder.setTitle("收藏删除");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				setCollect(data.get(position - 1).getId(), position);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent i = new Intent(this, PurchaseActivity.class);
		i.putExtra("id", data.get(position-1).getCollectid());
		startActivity(i);
	}

	/**
	 * 删除收藏
	 */
	private void setCollect(String id, final int position) {
		showProgress();
		RequestParams params = new RequestParams();
		params.put("id", id);
		String path = Path.SERVER_ADRESS + "personalCenter/deleteCollect.htm";
		HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				hideProgress();
				Toast.makeText(getApplicationContext(), "请检查您的网络",
						Toast.LENGTH_LONG).show();
				super.onFailure(arg0, arg1);
			}

			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				if (response.equals("") || response == null) {
					Toast.makeText(CollectListViewActivity.this, "服务器在打盹",
							Toast.LENGTH_SHORT).show();
				} else {
					JSONObject object = JSONObject.parseObject(response);
					String result = object.getString("result");
					if (result.equals("0")) {
						Toast.makeText(CollectListViewActivity.this, "操作成功",
								Toast.LENGTH_SHORT).show();
						data.remove(position - 1);
						adapter.setData(data);
						adapter.notifyDataSetChanged();
					} else {
						Toast.makeText(CollectListViewActivity.this, "服务器在打盹",
								Toast.LENGTH_SHORT).show();
					}
				}
				hideProgress();
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.activirty_listview_iv_01:
			onBackPressed();
			break;
		}
	}
}
