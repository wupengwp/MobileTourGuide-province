package com.jiagu.mobile.zhifu;

import java.util.ArrayList;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.activities.bases.TitleDrawerActivity;
import com.jiagu.mobile.tourguide.utils.HttpUtil;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.tourguide.utils.UesrInfo;
import com.jiagu.mobile.zhifu.adapter.SiteAdapter;
import com.jiagu.mobile.zhifu.bean.Site;
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
import android.widget.Toast;

/**
 * @ClassName: MySite
 * @Description: 我的地址
 * @author 高磊
 * @date 2015年1月31日 10:43
 * @company 西安甲骨企业文化传播有限公司
 */
public class MySite extends TitleDrawerActivity implements OnClickListener,
		OnItemClickListener, OnItemLongClickListener {
	private PullToRefreshListView listView;
	private Button activity_mysite_bottom_xinjian;
	private Intent i;
	private ImageView image;
	private String scenicid, cargoid, title, price, cargoClassify, isems;
	private ArrayList<Site> list;
	private SiteAdapter adapter;
	private String type;
	private String childgroupprice;
	private int count = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listview);
		image = (ImageView) findViewById(R.id.activirty_listview_iv_01);
		listView = (PullToRefreshListView) findViewById(R.id.activity_listview);
		activity_mysite_bottom_xinjian = (Button) findViewById(R.id.activity_mysite_bottom_xinjian);// 新建地址

		getData();
		Intent i = getIntent();
		scenicid = i.getStringExtra("scenicid");
		cargoid = i.getStringExtra("cargoid");
		title = i.getStringExtra("title");
		price = i.getStringExtra("price");
		childgroupprice = i.getStringExtra("price");
		type = i.getStringExtra("type");
		cargoClassify = i.getStringExtra("cargoClassify");// 景区还是商户
		isems = i.getStringExtra("isems");// 是否需要物流

		activity_mysite_bottom_xinjian.setOnClickListener(this);
		image.setOnClickListener(this);
		listView.setOnItemClickListener(this);
		listView.getRefreshableView().setOnItemLongClickListener(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		setAppName(UesrInfo.area);
		appName.setText(UesrInfo.area + "手机导游");
	}

	private void getData() {
		// TODO Auto-generated method stub
		showProgress();
		RequestParams params = new RequestParams();
		params.put("tourId", UesrInfo.getTourIDid());
		String path = Path.SERVER_ADRESS + "personalCenter/findUserAdress.htm";
		HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				super.onFailure(arg0, arg1);
				hideProgress();
				count++;
				if (count < 3) {
					getData();
				}else{
					count = 0;
					Toast.makeText(getApplicationContext(), "请检查您的网络",
							Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				if (response.equals("") || response == null) {
					Toast.makeText(MySite.this, "服务器在打盹", Toast.LENGTH_SHORT)
							.show();
				} else {
					JSONObject object = JSONObject.parseObject(response);
					JSONArray top = object.getJSONArray("records");
					String result = object.getString("result");
					if (!result.equals("0")) {
						Toast.makeText(MySite.this, "服务器在打盹",
								Toast.LENGTH_SHORT).show();
					} else {
						list = (ArrayList<Site>) JSONArray.parseArray(
								top.toJSONString(), Site.class);
						adapter = new SiteAdapter(MySite.this, list);
						listView.setAdapter(adapter);
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
		case R.id.activity_mysite_bottom_xinjian:
			i = new Intent();
			i.setClass(MySite.this, MyNewSite.class);
			i.putExtra("name", "");
			startActivity(i);
			finish();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		if (scenicid.equals("")) {
			intent.setClass(MySite.this, MyNewSite.class);
			intent.putExtra("name", list.get(position - 1).getUsername());
			intent.putExtra("dianhua", list.get(position - 1).getMobilephone());
			intent.putExtra("dizhi", list.get(position - 1).getAddress());
			intent.putExtra("id", list.get(position - 1).getId());
			intent.putExtra("postcode", list.get(position - 1).getPostcode());
			intent.putExtra("IsDefault", list.get(position - 1).getIsDefault());
			startActivity(intent);
			finish();
		} else {
			intent.setClass(MySite.this, OrderFromActivity.class);
			intent.putExtra("scenicid", scenicid);
			intent.putExtra("cargoid", cargoid);
			intent.putExtra("title", title);
			intent.putExtra("price", price);
			intent.putExtra("cargoClassify", cargoClassify);
			intent.putExtra("isems", isems);
			intent.putExtra("id", list.get(position - 1).getId());
			intent.putExtra("name", list.get(position - 1).getUsername());
			intent.putExtra("dianhua", list.get(position - 1).getMobilephone());
			intent.putExtra("dizhi", list.get(position - 1).getAddress());
			intent.putExtra("type",type);
			intent.putExtra("childgroupprice",childgroupprice);
			startActivity(intent);
			finish();
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
			final int arg2, long arg3) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new Builder(MySite.this);
		builder.setMessage("您确定要删除该收货地址吗？");
		builder.setTitle("地址删除");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				RequestParams params = new RequestParams();
				params.put("id", list.get(arg2 - 1).getId());
				HttpUtil.post(Path.SERVER_ADRESS
						+ "personalCenter/deleteUserAdress.htm", params,
						new AsyncHttpResponseHandler() {

							@Override
							public void onFailure(Throwable arg0, String arg1) {
								Toast.makeText(MySite.this, "请检查您的网络",
										Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onSuccess(String response) {
								super.onSuccess(response);
								JSONObject object = JSONObject
										.parseObject(response);
								String result = object.getString("result");
								if (result.equals("0")) {
									Toast.makeText(MySite.this, "删除成功",
											Toast.LENGTH_SHORT).show();
									list.remove(arg2 - 1);
									adapter.setData(list);
									adapter.notifyDataSetChanged();
								} else if (result.equals("34")) {
									Toast.makeText(MySite.this, "默认地址不可删除",
											Toast.LENGTH_SHORT).show();
								} else {

								}
							}
						});
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
}
