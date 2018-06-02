package com.jiagu.mobile.zhifu;

import java.util.ArrayList;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.activities.MainActivity;
import com.jiagu.mobile.tourguide.activities.bases.TitleDrawerActivity;
import com.jiagu.mobile.tourguide.utils.FileTools;
import com.jiagu.mobile.tourguide.utils.HttpUtil;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.tourguide.utils.UesrInfo;
import com.jiagu.mobile.zhifu.adapter.IndentListViewAdapte;
import com.jiagu.mobile.zhifu.bean.Indents;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * @ClassName: IndentActivity
 * @Description: 订单列表界面
 * @author 高磊
 * @date 2015年2月04日 16:08
 * @company 西安甲骨企业文化传播有限公司
 */
public class IndentListViewActivity extends TitleDrawerActivity implements
		OnCheckedChangeListener, OnRefreshListener<ListView>,
		OnItemClickListener, OnClickListener, OnLastItemVisibleListener,
		OnItemLongClickListener {
	private PullToRefreshListView listView;
	private String path = Path.SERVER_ADRESS + "admission/orderList.htm";
	private int pages = 2;
	private IndentListViewAdapte adapte;
	private ArrayList<Indents> data;
	private String isEnable = "";
	private String status = "";
	private String state = "";
	private boolean is = true;
	private int count = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_indentlistview);
		
		RadioGroup radioGroup = (RadioGroup) findViewById(R.id.activity_indentlistview_radioGroup);
		listView = (PullToRefreshListView) findViewById(R.id.activity_indentlistview_listview);
		ImageView mImageView = (ImageView) findViewById(R.id.activirty_listview_iv_01);

		getData(path, isEnable, status, state);

		radioGroup.setOnCheckedChangeListener(this);
		mImageView.setOnClickListener(this);
		listView.setOnItemClickListener(this);
		listView.setOnRefreshListener(this);
		listView.setOnLastItemVisibleListener(this);
		listView.getRefreshableView().setOnItemLongClickListener(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		setAppName(UesrInfo.area);
		appName.setText(UesrInfo.area + "手机导游");
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case R.id.activity_indentlistview_radioGroup_01:
			isEnable = "";
			status = "";
			state = "";
			// path = "";
			getData(path, isEnable, status, state);
			break;
		case R.id.activity_indentlistview_radioGroup_02:
			isEnable = "";
			status = "0";
			state = "";
			// path = "";
			getData(path, isEnable, status, state);
			break;
		case R.id.activity_indentlistview_radioGroup_03:
			isEnable = "";
			status = "1";
			state = "";
			// path = "";
			getData(path, isEnable, status, state);
			break;
		case R.id.activity_indentlistview_radioGroup_04:
			isEnable = "";
			status = "2";
			state = "";
			// path = "";
			getData(path, isEnable, status, state);
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent i = new Intent();		
		i.setClass(IndentListViewActivity.this, IndentActivity.class);
		i.putExtra("id", data.get(position - 1).getOrderid());
		i.putExtra("stateid", data.get(position - 1).getCargoid());
		if (data.get(position - 1).getStatus().equals("0")) {
			i.putExtra("type", "未支付");
		} else if (data.get(position - 1).getStatus().equals("1")) {
			if (data.get(position - 1).getIsenable().equals("0")) {
				i.putExtra("type", "未消费");
			} else {
				i.putExtra("type", "已消费");
			}
		} else if (data.get(position - 1).getStatus().equals("2")) {
			i.putExtra("type", "退款中");
		} else if (data.get(position - 1).getStatus().equals("3")) {
			i.putExtra("type", "退款成功");
		} else {
			i.putExtra("type", "退款失败");
		}
		startActivity(i);
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		getData(path, isEnable, status, state);
	}

	@Override
	public void onLastItemVisible() {
		// TODO Auto-generated method stub
		showProgress();
		RequestParams params = new RequestParams();
		params.put("pages", "" + pages);
		params.put("tourId", UesrInfo.getTourIDid());
		if (!isEnable.equals("")) {
			params.put("isEnable", isEnable);
		}
		if (!status.equals("")) {
			params.put("status", status);
		}
		if (!state.equals("")) {
			params.put("state", state);
		}
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
						Toast.makeText(IndentListViewActivity.this, "服务器在打盹",
								Toast.LENGTH_SHORT).show();
					} else {
						JSONObject object = JSONObject.parseObject(response);
						JSONArray top = object.getJSONArray("records");
						String result = object.getString("result");
						if (result.equals("0")) {
							ArrayList<Indents> list = (ArrayList<Indents>) JSONArray
									.parseArray(top.toJSONString(), Indents.class);
							for (Indents indent : list) {
								data.add(indent);
							}
							adapte.setData(data);
							adapte.notifyDataSetChanged();
							pages++;
						} else if (result.equals("30")) {
							Toast.makeText(IndentListViewActivity.this, "已是最后一页",
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(IndentListViewActivity.this, "服务器在打盹",
									Toast.LENGTH_SHORT).show();
						}
					}
					is = true;
					hideProgress();
				}
			});
		}
	}

	private void getData(final String path, final String isEnable, final String status,
			final String state) {
		// TODO Auto-generated method stub
		showProgress();
		RequestParams params = new RequestParams();
		params.put("pages", "1");
		params.put("tourId", UesrInfo.getTourIDid());
		if (!isEnable.equals("")) {
			params.put("isEnable", isEnable);
		}
		if (!status.equals("")) {
			params.put("status", status);
		}
		if (!state.equals("")) {
			params.put("state", state);
		}
		HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				super.onFailure(arg0, arg1);
				count++;
				if (count<3) {
					getData(path, isEnable, status, state);
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
				if (response == null || response.equals("")) {
					Toast.makeText(IndentListViewActivity.this, "服务器在打盹",
							Toast.LENGTH_SHORT).show();
				} else {
					if (data!=null && data.size()>0) {
						data.clear();
					}
					JSONObject object = JSONObject.parseObject(response);
					JSONArray top = object.getJSONArray("records");
					String result = object.getString("result");
					if (result.equals("0")) {
												
						data = (ArrayList<Indents>) JSONArray.parseArray(
								top.toJSONString(), Indents.class);
						adapte = new IndentListViewAdapte(
								IndentListViewActivity.this, data);
						listView.setAdapter(adapte);
						listView.onRefreshComplete();
						pages = 2;
					} else if (result.equals("1")) {
						listView.setAdapter(null);
						Toast.makeText(IndentListViewActivity.this, "暂无订单",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(IndentListViewActivity.this, "服务器在打盹",
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

		default:
			break;
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
			final int arg2, long arg3) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new Builder(IndentListViewActivity.this);
		builder.setMessage("您确定要删除该订单信息吗？");
		builder.setTitle("订单删除");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (data.get(arg2 - 1).getStatus().equals("0")) {
					delete(arg2);
				} else if (data.get(arg2 - 1).getStatus().equals("1")) {
					if (data.get(arg2 - 1).getIsenable().equals("0")) {
						Toast.makeText(IndentListViewActivity.this,
								"为了确保您的财产安全,未消费订单不可删除", Toast.LENGTH_SHORT)
								.show();
						dialog.dismiss();
					} else {
						delete(arg2);
					}
				} else if (data.get(arg2 - 1).getStatus().equals("2")) {
					Toast.makeText(IndentListViewActivity.this,
							"该单正在核对,暂时无法删除", Toast.LENGTH_SHORT).show();
					dialog.dismiss();
				} else {
					delete(arg2);
				}

			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
		return true;
	}

	private void delete(final int arg2) {
		RequestParams params = new RequestParams();
		params.put("orderId", data.get(arg2 - 1).getOrderid());
		params.put("tourId", UesrInfo.getTourIDid());
		HttpUtil.post(Path.SERVER_ADRESS + "admission/deleteOrder.htm", params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(Throwable arg0, String arg1) {
						Toast.makeText(IndentListViewActivity.this, "请检查您的网络",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(String response) {
						super.onSuccess(response);
						JSONObject object = JSONObject.parseObject(response);
						String result = object.getString("result");
						if (result.equals("0")) {
							Toast.makeText(IndentListViewActivity.this, "删除成功",
									Toast.LENGTH_SHORT).show();
							data.remove(arg2 - 1);
							adapte.setData(data);
							adapte.notifyDataSetChanged();
						} else if (result.equals("36")) {
							Toast.makeText(IndentListViewActivity.this,
									"该单正在处理", Toast.LENGTH_SHORT).show();
						} else {

						}
					}
				});
	}
}
