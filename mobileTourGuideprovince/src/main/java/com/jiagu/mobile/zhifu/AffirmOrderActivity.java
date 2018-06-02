package com.jiagu.mobile.zhifu;

import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.activities.bases.TitleDrawerActivity;
import com.jiagu.mobile.tourguide.utils.UesrInfo;
import com.jiagu.mobile.tourguide.wxzf.PayActivitywx;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @ClassName: AffirmOrderActivity
 * @Description:支付宝支付
 * @author 高磊
 * @date 2015年1月15日 上午11:57:42
 * @version 1
 * @company 西安甲骨企业文化传播有限公司
 */
public class AffirmOrderActivity extends TitleDrawerActivity implements
		OnClickListener {

	private String idstate;
	private RadioButton radioZFB;
	private RadioButton radioWXZF;
	private TextView number, tv_price, tv_tvdd, tv_formprice, tv_orderId;
	private Button sumberorder;
	private ImageView image;
	public static AffirmOrderActivity affirmOrderActivity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_affirm_order);
		affirmOrderActivity=this;
		initView();
		getIntents();

	}

	private void initView() {
		tv_tvdd = (TextView) findViewById(R.id.tv_tvdd);
		tv_formprice = (TextView) findViewById(R.id.tv_formprice);
		number = (TextView) this.findViewById(R.id.number);
		tv_price = (TextView) this.findViewById(R.id.tv_priceSSSS);
		tv_orderId = (TextView) this.findViewById(R.id.tv_orderId);
		sumberorder = (Button) findViewById(R.id.btn_sumber_order);
		radioZFB = (RadioButton) findViewById(R.id.radioZFB);
		radioWXZF = (RadioButton) findViewById(R.id.radioWXZF);
		image = (ImageView) findViewById(R.id.iv_return_01);
		sumberorder.setOnClickListener(this);
		image.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		setAppName(UesrInfo.area);
		appName.setText(UesrInfo.area + "手机导游");
	}

	String orderId;

	/*
	 * 获取商品数量有问题
	 */
	private void getIntents() {
		Intent intent = getIntent();
		idstate = intent.getStringExtra("idstate");
		String count = intent.getStringExtra("count");// 数量
		String price = intent.getStringExtra("price");// 价格
		String num = intent.getStringExtra("num");// 合计
		String title = intent.getStringExtra("title");// 标题
		orderId = intent.getStringExtra("orderId");// 订单号
		tv_tvdd.setText(title);
		number.setText(count);
		tv_price.setText(num);
		tv_formprice.setText(price);
		tv_orderId.setText(orderId);

		Toast.makeText(this, count, Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_return_01:
			onBackPressed();
			break;
		case R.id.btn_sumber_order:
			getAlipayOrder();
			break;
		}

	}

	/**
	 * @author Administrator 提交订单
	 */
	private void getAlipayOrder() {
		// RequestParams params = new RequestParams();
		// params.put("tourId", userid);
		//
		// String path = Path.SERVER_ADRESS + "admission/findDefaultAdress.htm";
		// HttpUtil.post(path, params, new AsyncHttpResponseHandler() {
		//
		// @Override
		// public void onFailure(Throwable arg0, String arg1) {
		// Toast.makeText(getApplicationContext(), "请检查您的网络",
		// Toast.LENGTH_LONG).show();
		// super.onFailure(arg0, arg1);
		// }
		//
		// @Override
		// public void onSuccess(String response) {
		// super.onSuccess(response);
		// if (response.equals("") || response == null) {
		// Toast.makeText(AffirmOrderActivity.this, "服务器在打盹",
		// Toast.LENGTH_SHORT).show();
		// } else {
		// JSONObject object = JSONObject.parseObject(response);
		// String result = object.getString("result");
		// JSONArray records = object.getJSONArray("records");
		// if (result.equals("0")) {
		if (radioZFB.isChecked()) {

			Intent intent = new Intent(AffirmOrderActivity.this,
					PayDemoActivity.class);
			intent.putExtra("idstate", idstate);
			intent.putExtra("title", tv_tvdd.getText().toString());
			intent.putExtra("total_price", tv_price.getText().toString());
			intent.putExtra("count", number.getText().toString());
			intent.putExtra("orderId", orderId);
			startActivity(intent);
			// finish();
		} 
		else if (radioWXZF.isChecked()) {
			Intent intent = new Intent(AffirmOrderActivity.this,
					PayActivitywx.class);
			intent.putExtra("idstate", idstate);
			intent.putExtra("title", tv_tvdd.getText().toString());
			intent.putExtra("total_price", tv_price.getText().toString());
			intent.putExtra("count", number.getText().toString());
			intent.putExtra("orderId", orderId);
			startActivity(intent);
			// finish();
		}
		// } else {
		//
		// }
		// }
		// }
		// });

	}

}
