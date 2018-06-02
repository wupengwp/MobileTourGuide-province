package com.jiagu.mobile.zhifu;

import java.math.BigDecimal;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.activities.bases.TitleDrawerActivity;
import com.jiagu.mobile.tourguide.utils.HttpUtil;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.zhifu.adapter.RefundAdapter;
import com.jiagu.mobile.zhifu.bean.Refund;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class RefundActivity extends TitleDrawerActivity {
	private EditText text;
	private Button btn;
	private ImageView image;
	private String id;
	private int x = 1;
	private ListView mListView;
	private ArrayList<Refund> data;
	private TextView text01, text02, text03, text04, text05, text06, text07;
	private CheckBox mCheckBox01, mCheckBox02, mCheckBox03, mCheckBox04,
			mCheckBox05, mCheckBox06;
	private EditText mEditText;
	private BigDecimal jiage = new BigDecimal("0");
	private RefundAdapter adapter;
	private int count = 0;
	public String paytype,cargoClassify;
	public static RefundActivity refundActivity;
	@Override
	@SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_refund);
		refundActivity = this;
		image = (ImageView) findViewById(R.id.activity_refund_image_return);
		mListView = (ListView) findViewById(R.id.activity_refund_listview);
		id = getIntent().getStringExtra("id");
		paytype = getIntent().getStringExtra("paytype");
		cargoClassify  = getIntent().getStringExtra("cargoClassify");
		mListView.setAdapter(null);
		getData();
		// 添加头部试图
		View topView = LayoutInflater.from(this).inflate(
				R.layout.activity_refund_listview_top, null);
		mListView.addHeaderView(topView);
		// 添加底部试图
		View bottomView = LayoutInflater.from(this).inflate(
				R.layout.activity_refund_bottom, null);
		text01 = (TextView) bottomView
				.findViewById(R.id.activity_refund_textview_01);
		text02 = (TextView) bottomView
				.findViewById(R.id.activity_refund_textview_02);
		text03 = (TextView) bottomView
				.findViewById(R.id.activity_refund_textview_03);
		text04 = (TextView) bottomView
				.findViewById(R.id.activity_refund_textview_04);
		text05 = (TextView) bottomView
				.findViewById(R.id.activity_refund_textview_05);
		text06 = (TextView) bottomView
				.findViewById(R.id.activity_refund_textview_06);
		text07 = (TextView) bottomView
				.findViewById(R.id.activity_refund_textview_07);
		mCheckBox01 = (CheckBox) bottomView
				.findViewById(R.id.activity_refund_checkbox_01);
		mCheckBox02 = (CheckBox) bottomView
				.findViewById(R.id.activity_refund_checkbox_02);
		mCheckBox03 = (CheckBox) bottomView
				.findViewById(R.id.activity_refund_checkbox_03);
		mCheckBox04 = (CheckBox) bottomView
				.findViewById(R.id.activity_refund_checkbox_04);
		mCheckBox05 = (CheckBox) bottomView
				.findViewById(R.id.activity_refund_checkbox_05);
		mCheckBox06 = (CheckBox) bottomView
				.findViewById(R.id.activity_refund_checkbox_06);
		mEditText = (EditText) bottomView
				.findViewById(R.id.activity_refund_edittext);
		mListView.addFooterView(bottomView);

		image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btn = (Button) findViewById(R.id.activity_refund_bottom_btn);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (x == 1) {
					x++;
					RequestParams params = new RequestParams();
					params.put("orderId", id);
					String ids = "";
					ArrayList<Refund> list = adapter.getData();
					for (int i = 0; i < list.size(); i++) {
						if (data.get(i).getIsuse().equals("0")) {
							if (list.get(i).getChecked()) {
								ids = ids + list.get(i).getExchangeid() + ",";
							}
						}
					}
					if (ids.length() > 0) {
						params.put("ids", ids.substring(0, ids.length() - 1)
								.toString());
					}
				
					if ((!text07.getText().toString().equals("0.0")) && (!text07.getText().toString().equals("0.00"))) {
						params.put("amount", text07.getText().toString());
					}else {
						Toast.makeText(RefundActivity.this, "请选择劵码",
								Toast.LENGTH_SHORT).show();
						x = 1;
						return;
					}
					
					String reason = "";
					if (mCheckBox01.isChecked()) {
						reason = reason + text01.getText().toString() + ",";
					}
					if (mCheckBox02.isChecked()) {
						reason = reason + text02.getText().toString() + ",";
					}
					if (mCheckBox03.isChecked()) {
						reason = reason + text03.getText().toString() + ",";
					}
					if (mCheckBox04.isChecked()) {
						reason = reason + text04.getText().toString() + ",";
					}
					if (mCheckBox05.isChecked()) {
						reason = reason + text05.getText().toString() + ",";
					}
					if (mCheckBox06.isChecked()) {
						reason = reason + text06.getText().toString() + ",";
					}
					if (!reason.equals("")) {
						params.put("reason",
								reason.substring(0, reason.length() - 1));
					} else {
						Toast.makeText(RefundActivity.this, "请选择至少一种退款原因",
								Toast.LENGTH_SHORT).show();
						x = 1;
						return;
					}
					params.put("description", mEditText.getText().toString());
					String path = Path.SERVER_ADRESS
							+ "admission/orderRefundOK.htm";
					HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

						@Override
						public void onFailure(Throwable arg0, String arg1) {
							Toast.makeText(getApplicationContext(), "请检查您的网络",
									Toast.LENGTH_LONG).show();
							super.onFailure(arg0, arg1);
							x = 1;
						}

						@Override
						public void onSuccess(String response) {
							super.onSuccess(response);
							if (response.equals("") || response == null) {
								Toast.makeText(RefundActivity.this, "服务器在打盹",
										Toast.LENGTH_SHORT).show();
								x = 1;
							} else {
								JSONObject object = JSONObject
										.parseObject(response);
								String result = object.getString("result");
								
								if (result.equals("0")) {
									Toast.makeText(RefundActivity.this,
											"申请成功,请耐心等待", Toast.LENGTH_SHORT)
											.show();
									finish();
								}else if (result.equals("2")) {
									Toast.makeText(RefundActivity.this,
											"长时间未操作,请重新申请", Toast.LENGTH_SHORT)
											.show();
									finish();
								}else if (result.equals("35")) {
									if(object.getJSONArray("records")!=null && object.getJSONArray("records").get(0)!=null){
										String showresult = (String) object.getJSONArray("records").get(0);
										Toast.makeText(RefundActivity.this,
												showresult, Toast.LENGTH_SHORT)
												.show();
										x = 1;
									}
									
								} else {
									Toast.makeText(RefundActivity.this,
											"请检查您的网络", Toast.LENGTH_SHORT)
											.show();
									x = 1;
								}
							}
						}
					});
				} else if (x != 1) {
					Toast.makeText(RefundActivity.this, "退款中,请耐心等待",
							Toast.LENGTH_SHORT).show();
				} else if (text.getText().toString().equals("")) {
					Toast.makeText(RefundActivity.this, "请填写退款原因",
							Toast.LENGTH_SHORT).show();
				} else {

				}
			}
		});
	}

	private void getData() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.put("orderId", id);
		params.put("type", "0");
		String path = Path.SERVER_ADRESS + "admission/orderRefund.htm";
		HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				super.onFailure(arg0, arg1);
				count++;
				if (count < 3) {
					getData();
				} else {
					Toast.makeText(getApplicationContext(), "请检查您的网络",
							Toast.LENGTH_LONG).show();
					count = 0;
				}
			}

			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				JSONObject object = JSONObject.parseObject(response);
				String result = object.getString("result");
				JSONArray top = object.getJSONArray("records");
				if (result.equals("0")) {
					data = (ArrayList<Refund>) JSONArray.parseArray(
							top.toJSONString(), Refund.class);
					for (int i = 0; i < data.size(); i++) {
						if (data.get(i).getIsuse().equals("0")) {
							if (!data.get(i).getPrice().equals("")
									&& data.get(i).getPrice() != null) {
								jiage = jiage.add(new BigDecimal(data.get(i)
										.getPrice()));
							} else {
								jiage = jiage.add(new BigDecimal("0"));
							}
						}
					}
					text07.setText(0.00 + "");
					adapter = new RefundAdapter(data, RefundActivity.this,
							text07);
					mListView.setAdapter(adapter);
				
				} else {
					Toast.makeText(RefundActivity.this, result,
							Toast.LENGTH_SHORT).show();
					finish();
				}
			}
		});
	}
}
