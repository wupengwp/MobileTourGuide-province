package com.jiagu.mobile.zhifu;

import java.io.UnsupportedEncodingException;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.utils.HttpUtil;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.tourguide.utils.UesrInfo;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class PayDemoActivity extends FragmentActivity {
	
	private String idstate;
	private int count;
	// 商户PID
	public static final String PARTNER = "2088711171471952";
	// 商户收款账号
	public static final String SELLER = "1552152520@qq.com";
	// 商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAMTjonwjk51xRlh6QHTz4Ip3j6otyFO287tg0U+sVHkZaLO1UmPD0EeS8W4eJPYeRRBwsp2hlRVAeft25mvnxwXH6i00XMSXH0bz53E5LnFgdsnmC0dYJcTy0G2lepXRKO6sEzKuKgkmUd6FvwoJdaoIfhIzfoFPn3GV4m6BVO+fAgMBAAECgYA7H6BLZuONA9b7+rogZJuo+P5LPDRL70SBoDE6m6VCOD8Yt/pluwdwds/JIYQ35B8ZML5yV3NNTm9S9AF9pbw9mL37VfNA2SvuTiKru7Kr1qmaNrpGtkQIUFq+DDpd4gHZjtrXgJIquPMjLn8LrfHHO1RdkCM4JIIuaZksLb0YAQJBAPZ/49oF2z22gPpo2FbBXIjp9PNbofeyP6XZJBQy72V6/BRqZMu0TDJ70RXXDHls9DDuAvPzjSLiyKW1sz29B2ECQQDMekJu9nV3oUmXCUqmFtFJU5WsElU9WHt2Is6uGbrdUm2Wj771e2s4jAItRo6rH77USjhmPqulHCdT+n87kFb/AkAJE2pjQ6Xv3sFe9u9IOnRawip3r18GTLTVOWp/p+RymxWe9s/hRDEuBqsLH9Dgw6c2Cf1n2AbRSwJSP59q3YPhAkAKptvLF/+qibZhrdQ7rricT0RewF28Yl2cG9gUbba0TNT0oOxbsWA2g+ShIynPdEnmmS8y9FTQtxS9ZTE3kx9VAkAseRhWAEq6A8keO5On5vzu70RP4XCQd5jwZi94ixiW/sTpseW5tYxBV5ZVmZmB1wvqaoFj0Y6tH/Vy1smw0j1h";
	// 支付宝公钥
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDE46J8I5OdcUZYekB08+CKd4+qLchTtvO7YNFPrFR5GWiztVJjw9BHkvFuHiT2HkUQcLKdoZUVQHn7duZr58cFx+otNFzElx9G8+dxOS5xYHbJ5gtHWCXE8tBtpXqV0SjurBMyrioJJlHehb8KCXWqCH4SM36BT59xleJugVTvnwIDAQAB";

	private static final int SDK_PAY_FLAG = 1;

	private static final int SDK_CHECK_FLAG = 2;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);
				

				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
//				String resultInfo = payResult.getResult();

				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(PayDemoActivity.this, "支付成功",
							Toast.LENGTH_SHORT).show();
					PayDemoActivity.this.finish();
					AffirmOrderActivity.affirmOrderActivity.finish();
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(PayDemoActivity.this, "支付结果确认中",
								Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(PayDemoActivity.this, "支付失败",
								Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(PayDemoActivity.this, "检查结果为：" + msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			}
			}
		};
	};
	private View fragment;
	private TextView product_subject, tv_tvname, product_price;
	private String orderId;
	private ImageView iv_return_fromzfb;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_main);
		fragment = findViewById(R.id.fragment);
		product_subject = (TextView) fragment
				.findViewById(R.id.product_subject);
		tv_tvname = (TextView) fragment.findViewById(R.id.tv_tvname);
		product_price = (TextView) fragment.findViewById(R.id.product_price);
		iv_return_fromzfb = (ImageView) findViewById(R.id.iv_return_fromzfb);
		iv_return_fromzfb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		Intent intent = getIntent();
		idstate = intent.getStringExtra("idstate");
		String title = intent.getStringExtra("title");// 名称
		String total_price = intent.getStringExtra("total_price");// 合计
		String count = intent.getStringExtra("count");// 数量
		orderId = intent.getStringExtra("orderId");// 订单号
		product_subject.setText(title);
		tv_tvname.setText(count);
		product_price.setText(total_price);
	}

	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	public void pay(View v) {
		getJYState();
	}

	/**
	 * check whether the device has authentication alipay account.
	 * 查询终端设备是否存在支付宝认证账户
	 * 
	 */
	public void check(View v) {
		Runnable checkRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask payTask = new PayTask(PayDemoActivity.this);
				// 调用查询接口，获取查询结果
				boolean isExist = payTask.checkAccountIfExist();

				Message msg = new Message();
				msg.what = SDK_CHECK_FLAG;
				msg.obj = isExist;
				mHandler.sendMessage(msg);
			}
		};

		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();

	}

	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(this);
		String version = payTask.getVersion();
		Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public String getOrderInfo(String subject, String body, String price) {
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + orderId + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url="+ "\""+ "http://www.shoujidaoyou.cn/provinces/admission/notifyUrl.htm"+ "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	public String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}

//	private void setOrderId(String orderId) {
//		RequestParams params = new RequestParams();
//		params.put("orderId", orderId);
//		String path = Path.SERVER_ADRESS + "admission/notifyUrl.htm";
//		HttpUtil.post(path, params, new AsyncHttpResponseHandler() {
//
//			@Override
//			public void onFailure(Throwable arg0, String arg1) {
//				Toast.makeText(getApplicationContext(), "请检查您的网络",
//						Toast.LENGTH_LONG).show();
//				super.onFailure(arg0, arg1);
//			}
//
//			@Override
//			public void onSuccess(String response) {
//				super.onSuccess(response);
//				if (response.equals("") || response == null) {
//
//				} else {
//					JSONObject object = JSONObject.parseObject(response);
//					JSONArray top = object.getJSONArray("records");
//					String result = object.getString("result");
//					if (top.equals("") || top == null || result.equals("4")) {
//
//					} else if (result.equals("30")) {
//
//					} else {
//
//					}
//				}
//			}
//		});
//	}
	
	/*
	 * 获取禁用状态
	 */
	private void getJYState() {
		
		RequestParams params = new RequestParams();
		params.put("id", idstate);
		String path = Path.SERVER_ADRESS + "admission/findadmissionState.htm";
		HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				super.onFailure(arg0, arg1);
				count++;
				if (count < 3) {
					getJYState();
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
					Toast.makeText(PayDemoActivity.this, "服务器在打盹",
							Toast.LENGTH_SHORT).show();
				} else {
					JSONObject object = JSONObject.parseObject(response);				
					String result = object.getString("result");
						if ("1".trim().equals(result.trim())) {
							Toast.makeText(PayDemoActivity.this, "该商品已被管理员禁用",
									Toast.LENGTH_SHORT).show();
						}else{
							// 订单
							String product = product_subject.getText().toString();// 名称
							String body = tv_tvname.getText().toString();// 商品数量
							String price = product_price.getText().toString();// 合计
							String orderInfo = getOrderInfo(product, body, price);

							// 对订单做RSA 签名
							String sign = sign(orderInfo);
							try {
								// 仅需对sign 做URL编码
								sign = URLEncoder.encode(sign, "UTF-8");
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}

							// 完整的符合支付宝参数规范的订单信息
							final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
									+ getSignType();

							Runnable payRunnable = new Runnable() {

								@Override
								public void run() {
									// 构造PayTask 对象
									PayTask alipay = new PayTask(PayDemoActivity.this);
									// 调用支付接口，获取支付结果
									String result = alipay.pay(payInfo);
									Message msg = new Message();
									msg.what = SDK_PAY_FLAG;
									msg.obj = result;
									mHandler.sendMessage(msg);
								}
							};

							// 必须异步调用
							Thread payThread = new Thread(payRunnable);
							payThread.start();
						}
										
				}
			}
		});
	}
}
