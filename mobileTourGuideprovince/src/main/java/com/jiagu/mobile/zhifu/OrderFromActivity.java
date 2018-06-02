package com.jiagu.mobile.zhifu;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiagu.mobile.dateselect.DateSelectActivity;
import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.activities.LoginActivity;
import com.jiagu.mobile.tourguide.activities.bases.TitleDrawerActivity;
import com.jiagu.mobile.tourguide.utils.HttpUtil;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.tourguide.utils.UesrInfo;
import com.jiagu.mobile.tourguide.widget.JudgeDate;
import com.jiagu.mobile.tourguide.widget.MyAlertDialogto;
import com.jiagu.mobile.tourguide.widget.ScreenInfo;
import com.jiagu.mobile.tourguide.widget.WheelMain;
import com.jiagu.mobile.zhifu.bean.Site;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @ClassName: OrderFromActivity
 * @Description: 立刻付款
 * @author 高磊
 * @date 2015年1月30日 14:07
 * @company 西安甲骨企业文化传播有限公司
 */
public class OrderFromActivity extends TitleDrawerActivity implements
		OnClickListener {

	private TextView name;
	public TextView money;
	public TextView numbersd;
	public TextView moneys;
	private TextView site;
	private TextView orderfrom_tv_name;
	private TextView orderfrom_tv_dianhua;
	private TextView orderfrom_tv_dizhi;
	private TextView crnumbersd;
	private TextView etnumbersd;
	public TextView crmoney;
	private TextView etmoney;
	private TextView textView1;
	private TextView textView2;
	private TextView textView3;
	private ImageView jian, jia, crjian, crjia, etjian, etjia;
	private Button btn;
	private int count = 1;// 数量
	private int crcount = 0;// 成人数量
	private int etcount = 0;// 儿童数量
	private String scenicid, cargoid, title, price, cargoClassify, isems;
	private RelativeLayout orderfrom_relativelayout;
	private Intent intent;
	private String userid = UesrInfo.getTourIDid();
	private String sitetype = "a";
	private ArrayList<Site> list;
	private String id;
	private String idstate;
	private String myName;
	private String dianhua;
	private String dizhi, childgroupprice, type,ratio;
	private LinearLayout fenlei, nameLinearLayout, iDnumberLinearLayout,orderform_linearlayout_rederdata;
	private RelativeLayout nofenlei;
	private EditText nameS, iDnumber;
	public TextView orderform_selectdata;
	WheelMain wheelMain;
	public static OrderFromActivity orderFromActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		orderFromActivity = this;
		setContentView(R.layout.activity_orderform);
		ImageView image = (ImageView) findViewById(R.id.iv_return_01);
		TextView userphone = (TextView) findViewById(R.id.userphone);
		String phone = UesrInfo.getUserphone();
		String mPhone = (phone.substring(0, 3) + "****" + phone
				.substring(7, 11)).toString();
		userphone.setText("注意:兑换码将发送至" + mPhone + "上");
		name = (TextView) findViewById(R.id.TV_TITLE);// 景区名称
		money = (TextView) findViewById(R.id.tv_yuan);// 单价
		crmoney = (TextView) findViewById(R.id.tv_cryuan);// 成人单价
		etmoney = (TextView) findViewById(R.id.tv_etyuan);// 儿童单价
		numbersd = (TextView) findViewById(R.id.numbersd);// 数量
		crnumbersd = (TextView) findViewById(R.id.linearlayout_isfenlei_chengren_numbersd);// 成人数量
		etnumbersd = (TextView) findViewById(R.id.linearlayout_isfenlei_ertong_numbersd);// 儿童数量
		moneys = (TextView) findViewById(R.id.tv_pricess);// 总价
		jian = (ImageView) findViewById(R.id.btn_subtraction);// 减
		jia = (ImageView) findViewById(R.id.btn_add);// 加
		crjian = (ImageView) findViewById(R.id.linearlayout_isfenlei_chengren_subtraction);// 成人减
		crjia = (ImageView) findViewById(R.id.linearlayout_isfenlei_chengren_add);// 成人加
		etjian = (ImageView) findViewById(R.id.linearlayout_isfenlei_ertong_subtraction);// 儿童减
		etjia = (ImageView) findViewById(R.id.linearlayout_isfenlei_ertong_add);// 儿童加
		btn = (Button) findViewById(R.id.btn_sumber_order);// 提交按钮
		site = (TextView) findViewById(R.id.orderfrom_dizhixinxi);
		orderfrom_tv_name = (TextView) findViewById(R.id.orderfrom_tv_name);
		orderfrom_tv_dianhua = (TextView) findViewById(R.id.orderfrom_tv_dianhua);
		orderfrom_tv_dizhi = (TextView) findViewById(R.id.orderfrom_tv_dizhi);
		orderfrom_relativelayout = (RelativeLayout) findViewById(R.id.orderfrom_relativelayout);// 地址
		fenlei = (LinearLayout) findViewById(R.id.linearlayout_isfenlei);
		nameLinearLayout = (LinearLayout) findViewById(R.id.orderform_linearlayout_username);
		iDnumberLinearLayout = (LinearLayout) findViewById(R.id.orderform_linearlayout_usershenfen);
		nofenlei = (RelativeLayout) findViewById(R.id.linearlayout_nofenlei);
		textView1 = (TextView) findViewById(R.id.textView1);
		textView2 = (TextView) findViewById(R.id.textView2);
		textView3 = (TextView) findViewById(R.id.textView3);
		nameS = (EditText) findViewById(R.id.orderform_username);
		iDnumber = (EditText) findViewById(R.id.orderform_usershenfen);
		orderform_linearlayout_rederdata = (LinearLayout) findViewById(R.id.orderform_linearlayout_rederdata);
		orderform_selectdata = (TextView) findViewById(R.id.orderform_selectdata);
		orderform_selectdata.setOnClickListener(this);

		Intent i = getIntent();
		idstate = i.getStringExtra("idstate");
		scenicid = i.getStringExtra("scenicid");
		cargoid = i.getStringExtra("cargoid");
		title = i.getStringExtra("title");
		price = i.getStringExtra("price");
		cargoClassify = i.getStringExtra("cargoClassify");// 景区还是商户
		isems = i.getStringExtra("isems");// 是否需要物流
		id = i.getStringExtra("id");
		myName = i.getStringExtra("name");
		dianhua = i.getStringExtra("dianhua");
		dizhi = i.getStringExtra("dizhi");
		childgroupprice = i.getStringExtra("childgroupprice");
		type = i.getStringExtra("type");
		ratio = i.getStringExtra("ratio");
		
		if (cargoClassify.trim().equals("2")) {
			orderform_linearlayout_rederdata.setVisibility(View.VISIBLE);
		}
		if (isems.equals("0")) {
			orderfrom_relativelayout.setVisibility(View.GONE);
			site.setVisibility(View.GONE);
		} else if (!id.equals("")) {
			orderfrom_tv_name.setText(myName);
			orderfrom_tv_dianhua.setText(dianhua);
			orderfrom_tv_dizhi.setText(dizhi);
			sitetype = "b";
		} else {
			setSite();
		}
		if (type.equals("2") || type.equals("6")) {
			nameLinearLayout.setVisibility(View.VISIBLE);
		} else {
			nameLinearLayout.setVisibility(View.GONE);
		}
		
		if (type.equals("2")) {
			iDnumberLinearLayout.setVisibility(View.VISIBLE);
		} else {
			iDnumberLinearLayout.setVisibility(View.GONE);
		}
		if (title!=null && !title.equals("")) {
			name.setText(title);
		}
		if (childgroupprice==null ||childgroupprice.equals("")) {
			fenlei.setVisibility(View.GONE);
			nofenlei.setVisibility(View.VISIBLE);
			crmoney.setVisibility(View.GONE);
			etmoney.setVisibility(View.GONE);
			textView2.setVisibility(View.GONE);
			textView3.setVisibility(View.GONE);
			money.setText(price);
			moneys.setText(price);
		} else {
			fenlei.setVisibility(View.VISIBLE);
			nofenlei.setVisibility(View.GONE);
			money.setVisibility(View.GONE);
			textView1.setVisibility(View.GONE);
			crmoney.setText(price);
			etmoney.setText(childgroupprice);
			moneys.setText("0");
		}
		image.setOnClickListener(this);
		jian.setOnClickListener(this);
		jia.setOnClickListener(this);
		crjian.setOnClickListener(this);
		crjia.setOnClickListener(this);
		etjian.setOnClickListener(this);
		etjia.setOnClickListener(this);
		btn.setOnClickListener(this);
		orderfrom_relativelayout.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		setAppName(UesrInfo.area);
		appName.setText(UesrInfo.area + "手机导游");
	}

	@Override
	public void onClick(View v) {
		double i = 1;
		double cr = 0;
		double et = 0;
		switch (v.getId()) {
		case R.id.iv_return_01:
			onBackPressed();
			break;
		case R.id.btn_subtraction:
			if (count > 1) {
				count--;
				i = Double.parseDouble(money.getText().toString());
				numbersd.setText("" + count);
				moneys.setText("" + new DecimalFormat("######0.00").format(i * count));
			}
			break;
		case R.id.btn_add:
			count++;
			i = Double.parseDouble(money.getText().toString());
			numbersd.setText("" + count);
			moneys.setText("" + new DecimalFormat("######0.00").format(i * count));
			break;
		case R.id.linearlayout_isfenlei_chengren_subtraction:
			if (crcount > 0) {
				crcount--;
				cr = Double.parseDouble(crmoney.getText().toString());
				et = Double.parseDouble(etmoney.getText().toString());
				crnumbersd.setText("" + crcount);
				moneys.setText("" + new DecimalFormat("######0.00").format(et * etcount + cr * crcount));
			}
			break;
		case R.id.linearlayout_isfenlei_chengren_add:
			crcount++;
			cr = Double.parseDouble(crmoney.getText().toString());
			et = Double.parseDouble(etmoney.getText().toString());
			crnumbersd.setText("" + crcount);
			moneys.setText("" + new DecimalFormat("######0.00").format(et * etcount + cr * crcount));
			break;
		case R.id.linearlayout_isfenlei_ertong_subtraction:
			if (etcount > 0) {
				etcount--;
				cr = Double.parseDouble(crmoney.getText().toString());
				et = Double.parseDouble(etmoney.getText().toString());
				etnumbersd.setText("" + etcount);
				moneys.setText("" + new DecimalFormat("######0.00").format(et * etcount + cr * crcount));
			}
			break;
		case R.id.linearlayout_isfenlei_ertong_add:
			etcount++;
			cr = Double.parseDouble(crmoney.getText().toString());
			et = Double.parseDouble(etmoney.getText().toString());
			etnumbersd.setText("" + etcount);
			moneys.setText("" + new DecimalFormat("######0.00").format(et * etcount + cr * crcount));
			break;
		case R.id.orderfrom_relativelayout:
			intent = new Intent();
			intent.setClass(OrderFromActivity.this, MySite.class);
			intent.putExtra("scenicid", scenicid);
			intent.putExtra("cargoid", cargoid);
			intent.putExtra("title", title);
			intent.putExtra("price", price);
			intent.putExtra("cargoClassify", cargoClassify);
			intent.putExtra("isems", isems);
			intent.putExtra("type", type);
			intent.putExtra("childgroupprice", childgroupprice);
			startActivity(intent);
			finish();
			break;
		case R.id.btn_sumber_order:
		getJYState();
			break;
			
		case R.id.orderform_selectdata:
			//选择日期
			intent = new Intent();
			intent.setClass(OrderFromActivity.this, DateSelectActivity.class);
			intent.putExtra("cargoid", cargoid);
			intent.putExtra("ratio", ratio);
			startActivity(intent);
//			LayoutInflater inflater1 = LayoutInflater.from(OrderFromActivity.this);
//			final View timepickerview1 = inflater1.inflate(R.layout.timepicker,
//					null);
//			ScreenInfo screenInfo1 = new ScreenInfo(OrderFromActivity.this);
//			wheelMain = new WheelMain(timepickerview1);
//			wheelMain.screenheight = screenInfo1.getHeight();
//			Calendar calendar1 = Calendar.getInstance();
////			if (JudgeDate.isDate(time1, "yyyy-MM-dd")) {
////				try {
////					calendar1.setTime(dateFormat.parse(time1));
////				} catch (ParseException e) {
////					// TODO Auto-generated catch block
////					e.printStackTrace();
////				}
////			}
//			int year1 = calendar1.get(Calendar.YEAR);
//			int month1 = calendar1.get(Calendar.MONTH);
//			int day1 = calendar1.get(Calendar.DAY_OF_MONTH);
//			wheelMain.initDateTimePicker(year1, month1, day1);
//			final MyAlertDialogto dialog = new MyAlertDialogto(OrderFromActivity.this)
//					.builder()
//					.setTitle("选择日期")
//					.setView(timepickerview1)
//					.setCancelable(false)
//					.setNegativeButton("取消", new OnClickListener() {
//						@Override
//						public void onClick(View v) {
//
//						}
//					});
//			dialog.setPositiveButton("保存", new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					orderform_selectdata.setText(wheelMain.getTime());
//				}
//			});
//			dialog.show();
				break;
		}
	}

	private void setMoney() {
		int crs = Integer.parseInt(crnumbersd.getText().toString());
		int et = Integer.parseInt(etnumbersd.getText().toString());
		if (childgroupprice.equals("")) {
			money();
		} else {
			if (crs + et > 0) {
				money();
			}else{
				Toast.makeText(OrderFromActivity.this, "买几个呗", Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	private void money(){
		showProgress();
		RequestParams params = new RequestParams();
		params.put("cargoID", cargoid);
		params.put("tourId", userid);
		params.put("busiID", scenicid);
		params.put("price", moneys.getText().toString());
		params.put("destineTime", orderform_selectdata.getText().toString().trim());
		
		// 单价(有儿童的时候传成人的,没有就是成人)
		if (childgroupprice.equals("")) {
			params.put("cargoPrice", money.getText().toString());
		} else {
			params.put("cargoPrice", crmoney.getText().toString());
		}
		// 数量(有儿童的时候传成人的,没有就是总量)
		if (childgroupprice.equals("")) {
			params.put("amount", numbersd.getText().toString());
		} else {
			params.put("amount", crnumbersd.getText().toString());
		}
		params.put("cargoclassify", cargoClassify);
		// 物流
		if (isems.equals("1")) {
			params.put("adressId", id);
		}
		// 儿童价格
		if (!childgroupprice.equals("")) {
			params.put("childPrice", etmoney.getText().toString());
		}
		// 儿童数量
		if (!childgroupprice.equals("")) {
			params.put("childAmount", etnumbersd.getText().toString());
		}
		// 姓名
		if (type.equals("2") || type.equals("6")) {
			params.put("name", nameS.getText().toString());
		}
		// 身份证号
		if (type.equals("2")) {
			params.put("idcard", iDnumber.getText().toString());
		}
		String path = Path.SERVER_ADRESS + "admission/insertOrder.htm";
		HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				Toast.makeText(getApplicationContext(), "请检查您的网络",
						Toast.LENGTH_LONG).show();
				super.onFailure(arg0, arg1);
				hideProgress();
			}

			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				if (response.equals("") || response == null) {
					Toast.makeText(OrderFromActivity.this, "服务器在打盹",
							Toast.LENGTH_SHORT).show();
				} else {
					JSONObject object = JSONObject.parseObject(response);
					String result = object.getString("result");
					JSONObject results = JSONObject.parseObject(result);
					String resultss = results.getString("result");
					String orderId = results.getString("orderId");
					if (resultss.equals("0")) {
						Intent intent = new Intent(OrderFromActivity.this,
								AffirmOrderActivity.class);
						intent.putExtra("idstate", idstate);
						intent.putExtra("orderId", orderId);
						if (childgroupprice.equals("")) {
							intent.putExtra("count", numbersd.getText()
									.toString());
							intent.putExtra("price", money.getText().toString());
						} else {
							int crs = Integer.parseInt(crnumbersd.getText()
									.toString());
							int et = Integer.parseInt(etnumbersd.getText()
									.toString());
						
							
							
						   intent.putExtra("count", "" + (crs + et));
						
							intent.putExtra("price", crmoney.getText().toString());
						}
						intent.putExtra("num", moneys.getText().toString());
						intent.putExtra("title", name.getText().toString());
						startActivity(intent);
						finish();
					}else{
						Toast.makeText(OrderFromActivity.this, resultss,
								Toast.LENGTH_SHORT).show();
						finish();
					}
				}
				hideProgress();
			}
		});
	}
	private void setSite() {
		RequestParams params = new RequestParams();
		params.put("tourId", userid);

		String path = Path.SERVER_ADRESS + "admission/findDefaultAdress.htm";
		HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				Toast.makeText(getApplicationContext(), "请检查您的网络",
						Toast.LENGTH_LONG).show();
				super.onFailure(arg0, arg1);
			}

			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				if (response.equals("") || response == null) {
					Toast.makeText(OrderFromActivity.this, "服务器在打盹",
							Toast.LENGTH_SHORT).show();
				} else {
					JSONObject object = JSONObject.parseObject(response);
					String result = object.getString("result");
					JSONArray records = object.getJSONArray("records");
					if (result.equals("0")) {
						list = (ArrayList<Site>) JSONArray.parseArray(
								records.toJSONString(), Site.class);
						id = list.get(0).getId();
						if (list.get(0) == null) {
							orderfrom_tv_name.setText("");
							orderfrom_tv_dianhua.setText("去建立地址吧");
							orderfrom_tv_dizhi.setText("");
							sitetype = "a";
						} else {
							orderfrom_tv_name
									.setText(list.get(0).getUsername());
							orderfrom_tv_dianhua.setText(list.get(0)
									.getMobilephone());
							orderfrom_tv_dizhi
									.setText(list.get(0).getAddress());
							sitetype = "b";
						}
					}
				}
			}
		});
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
					Toast.makeText(OrderFromActivity.this, "服务器在打盹",
							Toast.LENGTH_SHORT).show();
				} else {
					JSONObject object = JSONObject.parseObject(response);				
					String result = object.getString("result");
					
						if ("1".trim().equals(result.trim())) {
							Toast.makeText(OrderFromActivity.this, "该商品已被管理员禁用",
									Toast.LENGTH_SHORT).show();
						}else{
							
							if (cargoClassify.trim().equals("2")) {
								orderform_linearlayout_rederdata.setVisibility(View.VISIBLE);
								if (JudgeDate.isDate(orderform_selectdata.getText().toString().trim(), "yyyy-MM-dd")) {
								try {
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}else {
								Toast.makeText(OrderFromActivity.this, "请选择预定日期",
										Toast.LENGTH_SHORT).show();
								return;
							}
							}
							if (UesrInfo.getTourIDid().equals("")) {
								Toast.makeText(OrderFromActivity.this, "请您在个人中心登录",
										Toast.LENGTH_SHORT).show();
							} else if (!isems.equals("0")) {
								if (orderfrom_tv_name.getText().equals("")) {
									intent = new Intent();
									intent.setClass(OrderFromActivity.this, MySite.class);
									startActivity(intent);
								} else {
									setMoney();
								}
							} else if (type.equals("2")) {
								if (nameS.getText().toString().equals("")) {
									Toast.makeText(OrderFromActivity.this, "请填写姓名",
											Toast.LENGTH_SHORT).show();
								} else if (iDnumber.getText().toString().equals("")) {
									Toast.makeText(OrderFromActivity.this, "请填写身份证号",
											Toast.LENGTH_SHORT).show();
								} else {
									setMoney();
								}
							} else if (type.equals("6")) {
								if (nameS.getText().toString().equals("")) {
									Toast.makeText(OrderFromActivity.this, "请填写姓名",
											Toast.LENGTH_SHORT).show();
								} else {
									setMoney();
								}
							} else {
								setMoney();
							}
						}
										
				}
			}
		});
	}

}
