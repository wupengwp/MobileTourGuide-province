package com.jiagu.mobile.tourguide.wxzf;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.util.InetAddressUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import com.alipay.sdk.app.PayTask;
import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.utils.HttpUtil;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.zhifu.PayDemoActivity;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PayActivitywx extends Activity {

	private static final String TAG = "com.jiagu.mobile.tourguide.wxzf.PayActivity";
	private String title;// 名称
	private String total_price;// 合计
	private String count;
	private String idstate;// 名称
	private int countint;
	private TextView product_subjectwx, tv_tvnamewx, product_pricewx;
	private String orderIdwx;
	public static PayActivitywx payActivitywx;
	PayReq req;
	final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
	Map<String,String> resultunifiedorder;
//	StringBuffer sb;
	private ImageView iv_return_fromwx;
	private ProgressDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wxzf_pay);
		payActivitywx=this;
		req = new PayReq();
//		sb=new StringBuffer();

		msgApi.registerApp(Constants.APP_ID);
		
		product_subjectwx = (TextView) findViewById(R.id.product_subjectwx);
		tv_tvnamewx = (TextView) findViewById(R.id.tv_tvnamewx);
		product_pricewx = (TextView) findViewById(R.id.product_pricewx);
		iv_return_fromwx = (ImageView) findViewById(R.id.iv_return_fromwx);
		iv_return_fromwx.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		Intent intent = getIntent();
		idstate = intent.getStringExtra("idstate");
		title = intent.getStringExtra("title");// 名称
		total_price = intent.getStringExtra("total_price");// 合计
//		System.out.println("total_price------"+String.valueOf((int)(Float.parseFloat(total_price)*100)));
		count = intent.getStringExtra("count");// 数量
		orderIdwx = intent.getStringExtra("orderId");// 订单号
		product_subjectwx.setText(title);
		tv_tvnamewx.setText(count);
		product_pricewx.setText(total_price);

		Button payBtn = (Button) findViewById(R.id.paywx);
		payBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isWXAppInstalledAndSupported(payActivitywx, msgApi)) {
					Toast.makeText(payActivitywx, "微信客户端未安装", Toast.LENGTH_SHORT).show();
				}else {
					getJYState();
				}

			}
		});
		

//		String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();

	}
	

	
	/**
	 生成签名
	 */

	private String genPackageSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Constants.API_KEY);
		

		String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
		Log.e("orion",packageSign);
		return packageSign;
	}
	private String genAppSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Constants.API_KEY);

//        this.sb.append("sign str\n"+sb.toString()+"\n\n");
		String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
		Log.e("orion",appSign);
		return appSign;
	}
	private String toXml(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		for (int i = 0; i < params.size(); i++) {
			sb.append("<"+params.get(i).getName()+">");


			sb.append(params.get(i).getValue());
			sb.append("</"+params.get(i).getName()+">");
		}
		sb.append("</xml>");

		Log.e("orion",sb.toString());
		return sb.toString();
	}

	private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String,String>> {

//		private ProgressDialog dialog;


		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(PayActivitywx.this, getString(R.string.app_tip), getString(R.string.getting_prepayid));
		}

		@Override
		protected void onPostExecute(Map<String,String> result) {
			if (dialog != null) {
				dialog.dismiss();
			}
//			sb.append("prepay_id\n"+result.get("prepay_id")+"\n\n");
			
			resultunifiedorder=result;
			if (result.get("result_code").trim().equals("FAIL".trim())) {
				if (result.get("err_code").equals("OUT_TRADE_NO_USED")) {
					getOrderId();
				}else if (result.get("err_code").equals("ORDERPAID")) {
					Toast.makeText(payActivitywx, result.get("err_code_des"), Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(payActivitywx, "支付失败", Toast.LENGTH_SHORT).show();
				}			
			}else {
				genPayReq();
			}		
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected Map<String,String>  doInBackground(Void... params) {

			String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
			String entity = genProductArgs();

			Log.e("orion",entity);

			byte[] buf = Util.httpPost(url, entity);

			String content = new String(buf);
			Log.e("orion", content);
			Map<String,String> xml=decodeXml(content);

			return xml;
		}
	}



	public Map<String,String> decodeXml(String content) {

		try {
			Map<String, String> xml = new HashMap<String, String>();
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(content));
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {

				String nodeName=parser.getName();
				switch (event) {
					case XmlPullParser.START_DOCUMENT:

						break;
					case XmlPullParser.START_TAG:

						if("xml".equals(nodeName)==false){
							//实例化student对象
							xml.put(nodeName,parser.nextText());
						}
						break;
					case XmlPullParser.END_TAG:
						break;
				}
				event = parser.next();
			}

			return xml;
		} catch (Exception e) {
			Log.e("orion",e.toString());
		}
		return null;

	}


	private String genNonceStr() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
	}
	
	private long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}
	


	private String genOutTradNo() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
	}
	

   //
	private String genProductArgs() {
		StringBuffer xml = new StringBuffer();

		try {
			String	nonceStr = genNonceStr();


			xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams.add(new BasicNameValuePair("appid", Constants.APP_ID));
			packageParams.add(new BasicNameValuePair("body", title));
			packageParams.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
			packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
			packageParams.add(new BasicNameValuePair("notify_url", Constants.NOTYFI_URL));
			packageParams.add(new BasicNameValuePair("out_trade_no",orderIdwx));//商户订单号genOutTradNo()
			packageParams.add(new BasicNameValuePair("spbill_create_ip",getLocalHostlp()));
			packageParams.add(new BasicNameValuePair("total_fee", String.valueOf((int)(Float.parseFloat(total_price)*100))));//只能为整数,单位分
			packageParams.add(new BasicNameValuePair("trade_type", "APP"));


			String sign = genPackageSign(packageParams);
			packageParams.add(new BasicNameValuePair("sign", sign));


		   String xmlstring =toXml(packageParams);
		   xmlstring = new String(xmlstring.toString().getBytes(), "ISO8859-1");
			return xmlstring;

		} catch (Exception e) {
			Log.e(TAG, "genProductArgs fail, ex = " + e.getMessage());
			return null;
		}
		

	}
	private void genPayReq() {

		req.appId = Constants.APP_ID;
		req.partnerId = Constants.MCH_ID;
		req.prepayId = resultunifiedorder.get("prepay_id");
		req.packageValue = "Sign=WXPay";
		req.nonceStr = genNonceStr();
		req.timeStamp = String.valueOf(genTimeStamp());


		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

		req.sign = genAppSign(signParams);

//		sb.append("sign\n"+req.sign+"\n\n");
		sendPayReq();
		Log.e("orion", signParams.toString());

	}
	private void sendPayReq() {
		
		msgApi.registerApp(Constants.APP_ID);
		msgApi.sendReq(req);
	}


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
				countint++;
				if (countint < 3) {
					getJYState();
				} else {
					countint = 0;
					Toast.makeText(getApplicationContext(), "请检查您的网络",
							Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				if (response == null || response.equals("")) {
					Toast.makeText(PayActivitywx.this, "服务器在打盹",
							Toast.LENGTH_SHORT).show();
				} else {

					try {
						JSONObject object = new JSONObject(response);
						String result = object.getString("result");
						if ("1".trim().equals(result.trim())) {
							Toast.makeText(PayActivitywx.this, "该商品已被管理员禁用",
									Toast.LENGTH_SHORT).show();
						} else {						
							GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
							getPrepayId.execute();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		});
	}
	/*
	 * 获取订单号
	 */
	private void getOrderId() {
		dialog = ProgressDialog.show(PayActivitywx.this, getString(R.string.app_tip), getString(R.string.seting_orderid));
		RequestParams params = new RequestParams();
		params.put("orderId", orderIdwx);
		String path = Path.SERVER_ADRESS + "wechatPay/updateOrderId.htm";
		HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				super.onFailure(arg0, arg1);
				if (dialog != null) {
					dialog.dismiss();
				}
				countint++;
				if (countint < 3) {
					getJYState();
				} else {
					countint = 0;
					Toast.makeText(getApplicationContext(), "请检查您的网络",
							Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				if (response == null || response.equals("")) {
					Toast.makeText(PayActivitywx.this, "服务器在打盹",
							Toast.LENGTH_SHORT).show();
				} else {

					try {
						JSONObject object = new JSONObject(response);
						String result = object.getString("result");
						if ((!result.equals(""))&&(result!=null)) {
							orderIdwx = result.trim();
							if (dialog != null) {
								dialog.dismiss();
							}
							GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
							getPrepayId.execute();
						}
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		});
	}
	// 获取本机的ip地址
	public String getLocalHostlp() {
		String ipaddress = null;
		try {
			Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces();
			// 便利所用的网络接口
			while (en.hasMoreElements()) {
				NetworkInterface nif = en.nextElement();// 得到每一个网络接口绑定的所有ip
				Enumeration<InetAddress> inet = nif.getInetAddresses();
				// 遍历每一个接口绑定的所有ip
				while (inet.hasMoreElements()) {
					InetAddress ip = inet.nextElement();
					if (!ip.isLoopbackAddress()
							&& InetAddressUtils.isIPv4Address(ip
									.getHostAddress())) {
						ipaddress = ip.getHostAddress();
						return ipaddress;
					}
				}
			}
		} catch (SocketException e) {
			Log.e("jiagu", "获取本地ip地址失败");
			e.printStackTrace();
		}
		return ipaddress;

	}
	/*
	 * 判断微信是否安装
	 */
	private  boolean isWXAppInstalledAndSupported(Context context,
			IWXAPI api) {
		// LogOutput.d(TAG, "isWXAppInstalledAndSupported");
		boolean sIsWXAppInstalledAndSupported;
		sIsWXAppInstalledAndSupported = api.isWXAppInstalled()
				&& api.isWXAppSupportAPI();
		return sIsWXAppInstalledAndSupported;
	}
	
}
