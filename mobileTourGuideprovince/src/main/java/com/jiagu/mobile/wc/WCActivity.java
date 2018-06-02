package com.jiagu.mobile.wc;

import java.util.ArrayList;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.activities.bases.TitleDrawerActivity;
import com.jiagu.mobile.tourguide.utils.HttpUtil;
import com.jiagu.mobile.tourguide.utils.UesrInfo;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
* @ClassName: WCActivity 
* @Description: gaolei    没有在清单文件中注册
* @author A18ccms a18ccms_gmail_com 
* @date 2015-5-28 下午5:20:19
 */
public class WCActivity extends TitleDrawerActivity implements OnClickListener, OnItemClickListener{
	private PullToRefreshListView listView;
	private MapView map;
	private int count;
	private String path;
	private WCAdapter adapter;
	private Button sb;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_wc);
		RadioButton listRadioButton = (RadioButton) findViewById(R.id.wc_1_but);
		RadioButton mapRadioButton = (RadioButton) findViewById(R.id.wc_2_but);
		ImageButton image = (ImageButton) findViewById(R.id.activity_wc_image_return);
		listView = (PullToRefreshListView) findViewById(R.id.activity_wc_listview);
		map = (MapView) findViewById(R.id.activity_wc_bmapView);
		sb = (Button) findViewById(R.id.wc_btn_sb);
		
		listView.setVisibility(View.VISIBLE);
		map.setVisibility(View.GONE);
		listView.setAdapter(null);
//		getData();
		listRadioButton.setOnClickListener(this);
		mapRadioButton.setOnClickListener(this);
		image.setOnClickListener(this);
		listView.setOnItemClickListener(this);
		sb.setOnClickListener(this);
	}
	//获得数据
	private void getData() {
		// TODO Auto-generated method stub
		showProgress();
		RequestParams params = new RequestParams();
		params.put("x", ""+UesrInfo.myX);
		params.put("y", ""+UesrInfo.myY);
		
		HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				super.onFailure(arg0, arg1);
				hideProgress();
				count++;
				if (count < 3) {
					getData();
				} else {
					count = 0;
					Toast.makeText(getApplicationContext(), "请检查您的网络",
							Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				if (response == null || response.equals("")) {
					Toast.makeText(WCActivity.this, "服务器在打盹",
							Toast.LENGTH_SHORT).show();
				} else {
					JSONObject object = JSONObject.parseObject(response);
					JSONArray top = object.getJSONArray("records");
					String result = object.getString("result");
					if (top == null || top.equals("") || result.equals("4")) {
						Toast.makeText(WCActivity.this, "服务器在打盹", Toast.LENGTH_SHORT).show();
					} else if (result.equals("0")) {
//						data = (ArrayList<Exercise>) JSONArray.parseArray(
//								top.toJSONString(), Exercise.class);
//						adapter = new WCAdapter(WCActivity.this,
//								data);
//						listView.setAdapter(adapter);
//						a = 2;
					} else if (result.equals("30")) {
						Toast.makeText(WCActivity.this, "该景区暂无活动信息",
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
		case R.id.wc_1_but://列表查看
			listView.setVisibility(View.VISIBLE);
			map.setVisibility(View.GONE);
			break;
		case R.id.wc_2_but://地图查看
			listView.setVisibility(View.GONE);
			map.setVisibility(View.VISIBLE);
			break;
		case R.id.wc_btn_sb://上报厕所
			AlertDialog.Builder builder = new Builder(WCActivity.this);
			View inflate = getLayoutInflater().inflate(R.layout.wc_pop_shangbao, null);
			EditText et = (EditText) inflate.findViewById(R.id.wc_pop_sb_wz);
			Switch sw_1 = (Switch) inflate.findViewById(R.id.wc_pop_sb_switch_1);
			Switch sw_2 = (Switch) inflate.findViewById(R.id.wc_pop_sb_switch_2);
			et.setText(UesrInfo.at);
			builder.setView(inflate);
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							
						}
					});
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder.create().show();
			break;
		case R.id.activity_wc_image_return://返回按钮
			onBackPressed();
			break;

		default:
			break;
		}
	}
	//列表查看的Item点击事件
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
	//为列表查看简历的Adapter
	class WCAdapter extends BaseAdapter{
		private Context context;
		private ArrayList<String> data;
		private LayoutInflater inflater;
		
		public WCAdapter(Context context, ArrayList<String> data) {
			super();
			this.context = context;
			this.data = data;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return convertView;
		}
		
	}
	class ViewHolder {
		public TextView name,time;
		public ImageView image;
	}
}
