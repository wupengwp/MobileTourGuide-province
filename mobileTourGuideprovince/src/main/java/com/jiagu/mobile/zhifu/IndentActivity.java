package com.jiagu.mobile.zhifu;

import java.util.ArrayList;

import cn.sharesdk.framework.authorize.e;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.activities.LoginActivity;
import com.jiagu.mobile.tourguide.activities.PhotoAlbumActivity;
import com.jiagu.mobile.tourguide.activities.bases.TitleDrawerActivity;
import com.jiagu.mobile.tourguide.utils.HttpUtil;
import com.jiagu.mobile.tourguide.utils.MyImageLoader;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.tourguide.utils.UesrInfo;
import com.jiagu.mobile.zhifu.bean.Indents;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @ClassName: IndentActivity
 * @Description: 订单详细界面
 * @author 高磊
 * @date 2015年2月03日 13:42
 * @company 西安甲骨企业文化传播有限公司
 */
public class IndentActivity extends TitleDrawerActivity implements
		OnClickListener {
	private ImageView photo;
	private TextView tv02, tv03, tv04, tv05, tv06, tv07, tv08, tv09, tv10,
			tv11, tv12, tv13, tv14, tv15, tv16, tv17, tv18,yudingtime;
	private String id, type,stateid;
	private int btnInteger = 3;
	private Button btn;
	private ArrayList<Indents> list;
	private int count = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dingdanxiangqing);

		Intent i = getIntent();
		id = i.getStringExtra("id");
		stateid = i.getStringExtra("stateid");
		type = i.getStringExtra("type");
		LinearLayout mIndent = (LinearLayout) findViewById(R.id.dingdanxiangqing_layout_zhuangtai);
		ImageView image = (ImageView) findViewById(R.id.activity_dingdanxiangqing_iv_01);// 返回
		photo = (ImageView) findViewById(R.id.activity_dingdanxiangqing_iv_03);// 相册
		tv02 = (TextView) findViewById(R.id.activity_dingdanxiangqing_tv_02);// 票名
		tv03 = (TextView) findViewById(R.id.activity_dingdanxiangqing_tv_03);// 现价
		tv04 = (TextView) findViewById(R.id.activity_dingdanxiangqing_tv_04);// 原价
		tv04.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);// 加横线
		tv05 = (TextView) findViewById(R.id.activity_dingdanxiangqing_tv_05);// 退款方式
		tv06 = (TextView) findViewById(R.id.activity_dingdanxiangqing_tv_06);// 同上
		tv07 = (TextView) findViewById(R.id.activity_dingdanxiangqing_tv_07);// 票名
		tv08 = (TextView) findViewById(R.id.activity_dingdanxiangqing_tv_08);// 数量
		tv09 = (TextView) findViewById(R.id.activity_dingdanxiangqing_tv_09);// 价格
		tv10 = (TextView) findViewById(R.id.activity_dingdanxiangqing_tv_10);// 订单号
		tv11 = (TextView) findViewById(R.id.activity_dingdanxiangqing_tv_11);// 购买手机
		tv12 = (TextView) findViewById(R.id.activity_dingdanxiangqing_tv_12);// 下单时间
		tv13 = (TextView) findViewById(R.id.activity_dingdanxiangqing_tv_13);// 数量
		tv14 = (TextView) findViewById(R.id.activity_dingdanxiangqing_tv_14);// 总价
		tv15 = (TextView) findViewById(R.id.activity_dingdanxiangqing_tv_15);// 状态
		tv16 = (TextView) findViewById(R.id.activity_dingdanxiangqing_tv_16);// 限时
		tv17 = (TextView) findViewById(R.id.activity_dingdanxiangqing_tv_17);// 退款时间
		tv18 = (TextView) findViewById(R.id.activity_dingdanxiangqing_tv_18);// 支付时间
		yudingtime = (TextView) findViewById(R.id.activity_dingdanxiangqing_yudingtime);// 预定时间
		
		btn = (Button) findViewById(R.id.activity_dingdanxiangqing_btn);

		getData(id);

		image.setOnClickListener(this);
		mIndent.setOnClickListener(this);
		// collect.setOnClickListener(this);
		btn.setOnClickListener(this);
		photo.setOnClickListener(this);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		setAppName(UesrInfo.area);
		appName.setText(UesrInfo.area + "手机导游");
		getData(id);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent i = new Intent();
		switch (v.getId()) {
		case R.id.activity_dingdanxiangqing_iv_01:// 返回
			onBackPressed();
			break;
		case R.id.dingdanxiangqing_layout_zhuangtai:// 订单详情
			if (type.equals("未消费")) {
				i.putExtra("id", id);
				i.putExtra("name", list.get(0).getAdmission().getTitle());
				i.putExtra("cargoclassify", list.get(0).getAdmission().getCargoclassify());
				i.putExtra("adress", list.get(0).getAdmission().getAdress());
				i.putExtra("phonenumber", list.get(0).getAdmission()
						.getPhonenumber());
				i.putExtra("nameS", list.get(0).getAdmission().getShop());
				i.setClass(IndentActivity.this, ArticleActivity.class);
				startActivity(i);
			} else {
				Toast.makeText(getApplicationContext(), "该单无法使用",
						Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.activity_dingdanxiangqing_iv_03:// 相册
			if ("2".equals(list.get(0).getAdmission().getCargoclassify().trim())) {
				
			}else {
				i.putExtra("photoId", list.get(0).getCargoid());
				i.putExtra("path", Path.SERVER_ADRESS
						+ "characteristic/photoList.htm");
				i.setClass(this, PhotoAlbumActivity.class);
				startActivity(i);
			}
			
			break;
		case R.id.activity_dingdanxiangqing_btn:// 立即购买或退款
			if (type.equals("退款中")) {
				Toast.makeText(getApplicationContext(), "退款中请耐心等待",
						Toast.LENGTH_LONG).show();
			} else {
				if (btnInteger == 0) {
					// 退款
					if (!("2".equals(list.get(0).getAdmission().getCargoclassify().trim()))) {
					if (list.get(0).getAdmission().getRefundOver().equals("1")
							|| list.get(0).getAdmission().getRefundOver()
									.equals("1")) {
						Toast.makeText(getApplicationContext(), "此物品不支持退款",
								Toast.LENGTH_LONG).show();
					}else{
						i.putExtra("paytype", list.get(0).getPaytype());
						i.putExtra("id", id);
						i.putExtra("cargoClassify", list.get(0).getAdmission()
								.getCargoclassify());
						i.setClass(this, RefundActivity.class);
						startActivity(i);
					}
					}else {
						i.putExtra("paytype", list.get(0).getPaytype());
						i.putExtra("id", id);
						i.putExtra("cargoClassify", list.get(0).getAdmission()
								.getCargoclassify());
						i.setClass(this, RefundActivity.class);
						startActivity(i);
					}
					
				} else if (btnInteger == 2) {
					// 购买
					getJYState();
					
				} else if (btnInteger == 1) {
					i = new Intent();
					i.putExtra("title", list.get(0).getAdmission().getTitle());
					i.putExtra("num", list.get(0).getCargoPayment());//合计
					i.putExtra("price", list.get(0).getAdmission().getPrice());
					if (list.get(0).getChildAmount().equals("")) {
						i.putExtra("count", list.get(0).getAmount());
					} else {
						i.putExtra(
								"count",
								(Integer.parseInt(list.get(0).getAmount())
										+ Integer.parseInt(list.get(0)
												.getChildAmount()))+"");
					}

					i.putExtra("orderId", list.get(0).getOrderid());
					i.putExtra("idstate", list.get(0).getAdmission().getId());
					
					i.setClass(IndentActivity.this, AffirmOrderActivity.class);
					startActivity(i);
				} else if (btnInteger == 3) {
					// 取消退款

					RequestParams params = new RequestParams();
					params.put("orderId", id);
					String path = Path.SERVER_ADRESS
							+ "admission/cancelRefund.htm";
					HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

						@Override
						public void onFailure(Throwable arg0, String arg1) {
							super.onFailure(arg0, arg1);
							Toast.makeText(getApplicationContext(), "请检查您的网络",
									Toast.LENGTH_LONG).show();
						}

						@Override
						public void onSuccess(String response) {
							super.onSuccess(response);
							JSONObject object = JSONObject
									.parseObject(response);
							String result = object.getString("result");
							if (result.equals("0")) {
								getData(id);
							} else if (result.equals("37")) {
								Toast.makeText(getApplicationContext(),
										"正在退款,不可取消", Toast.LENGTH_LONG).show();
							}
						}
					});	
			}
			}
			break;
		}
	}

	private void getData(final String id) {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.put("orderId", id);
		String path = Path.SERVER_ADRESS + "admission/orderDetial.htm";
		HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				super.onFailure(arg0, arg1);
				count++;
				if (count < 3) {
					getData(id);
				} else {
					Toast.makeText(getApplicationContext(), "请检查您的网络",
							Toast.LENGTH_LONG).show();
					count = 0;
				}
			}

			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				if (response == null || response.equals("")) {
					Toast.makeText(IndentActivity.this, "服务器在打盹",
							Toast.LENGTH_SHORT).show();
				} else {
					JSONObject object = JSONObject.parseObject(response);
					JSONArray top = object.getJSONArray("records");
					String result = object.getString("result");
					if (result != null && result.equals("0")) {
						list = (ArrayList<Indents>) JSONArray.parseArray(
								top.toJSONString(), Indents.class);
						if (list.size() == 0) {
							Toast.makeText(IndentActivity.this, "暂无订单!!!!",
									Toast.LENGTH_SHORT).show();
						} else {
							if (list.size() != 0) {
								Indents indents = list.get(0);														
								
								if (indents.getAdmission().getImageurl() != null) {
									if ("2".equals(indents.getAdmission().getCargoclassify().trim())) {
										MyImageLoader.loadImage(indents.getAdmission()
														.getImageurl(), photo);
									}else {
										MyImageLoader.loadImage(Path.IMAGER_ADRESS
												+ indents.getAdmission()
														.getImageurl(), photo);
									}
									
								}
								tv02.setText(indents.getAdmission().getTitle());
								tv03.setText("¥"
										+ indents.getAdmission().getPrice()
										+ "元");
						if (indents.getAdmission().getCargoclassify().equals("0")
										|| indents.getAdmission().getCargoclassify()
												.equals("1")) {
								tv04.setText("¥"
										+ indents.getAdmission().getOldPrice()
										+ "元");
								if (indents.getAdmission().getRefundOver()
										.equals("0")) {
								
									tv05.setText("支持过期退款");
									tv17.setText("退款时限:截止至"
											+ indents.getAdmission()
													.getRefundtime());
									tv17.setVisibility(View.VISIBLE);
									
								} else {
									tv05.setText("不支持过期退款");
			
								}
								if (indents.getAdmission().getRefundNow()
										.equals("0")) {
									tv06.setText("支持随时退款");
								} else {
									tv06.setText("不支持随时退款");
								}
								if (indents.getAdmission().getRefundLimit() != null) {
									if (indents.getAdmission().getRefundLimit()
											.equals("0")) {
										tv16.setText("支持限时退款");
										String[] split = indents.getAdmission()
												.getRefundtime().toString()
												.split("-");
										tv17.setText("退款时限:" + split[0]
												+ ":00至" + split[1] + ":59");
										tv17.setVisibility(View.VISIBLE);
									} else {
										tv16.setText("不支持限时退款");
									}
								} else {
									tv16.setVisibility(View.GONE);
								}
						}else {
							if ("".equals(indents.getAdmission().getZwyoldprice())||indents.getAdmission().getZwyoldprice()==null) {
								tv04.setText("¥0");
							}else {
							tv04.setText("¥"
									+ indents.getAdmission().getZwyoldprice()
									+ "元");
							}
							tv05.setVisibility(View.GONE);
							tv06.setVisibility(View.GONE);
							tv16.setVisibility(View.GONE);
							tv17.setVisibility(View.VISIBLE);
							tv17.setCompoundDrawables(null,null,null,null);  
							if (indents.getAdmission().getRefundLimit()!=null && !"".equals(indents.getAdmission().getRefundLimit())) {					
								tv17.setText("退款说明:"+indents.getAdmission().getRefundLimit().trim());
							}
						}
								tv07.setText(indents.getAdmission().getTitle());
//								tv08.setText(indents.getAmount());//"1"
								tv09.setText(indents.getCargoPayment());//indents.getAdmission().getPrice()
								tv10.setText("订单号:" + indents.getOrderid());
								tv11.setText("购买手机:"
										+ indents.getMobilenumber());
								if ("2".equals(indents.getAdmission().getCargoclassify().trim())) {
									yudingtime.setVisibility(View.VISIBLE);
								    yudingtime.setText("预定时间:"
										+ indents.getConsumetime());
								}
								
								tv12.setText("下单时间:" + indents.getTimer());
								if (!indents.getPaydate().equals("")) {
									tv18.setText("支付时间:" + indents.getPaydate());
									tv18.setVisibility(View.VISIBLE);
								}
								if ("2".equals(indents.getAdmission().getCargoclassify().trim())) {
									tv13.setText("数量:" + indents.getAmount());
									tv08.setText(indents.getAmount());
								}else{
								if (indents.getChildAmount().equals("")) {
									tv13.setText("数量:" + indents.getAmount());
									tv08.setText(indents.getAmount());
								} else {
									tv08.setText(String.valueOf((Integer.parseInt(indents
											.getAmount()) + Integer
											.parseInt(indents
													.getChildAmount()))));
									tv13.setText("数量:"
											+ (Integer.parseInt(indents
													.getAmount()) + Integer
													.parseInt(indents
															.getChildAmount()))
											+ "张(成人票:" + indents.getAmount()
											+ "张       儿童票:"
											+ indents.getChildAmount() + "张)");
								}
								}
								tv14.setText("总价:" + indents.getCargoPayment()
										+ "元");
								tv15.setText("当前状态:" + type);
								tv15.setVisibility(View.GONE);
								if (type.equals("退款中")) {
									btn.setText("退款中");
								} else {
									if (indents.getIsenable().equals("0")
											&& indents.getStatus().equals("1")) {
										btn.setText("退款");
										btnInteger = 0;
										if (!("2".equals(list.get(0).getAdmission().getCargoclassify().trim()))) {
											if (list.get(0).getAdmission()
													.getRefundOver().equals("1")
													|| list.get(0).getAdmission()
															.getRefundOver()
															.equals("1")) {
												btn.setBackgroundResource(R.drawable.btn_ok_bg_perssed);
											}
										}
										
									} else {
										if (indents.getStatus().equals("0")) {
											btn.setText("付款");
											btnInteger = 1;
										} else if (indents.getIsenable()
												.equals("1")) {
											btn.setText("立即购买");
											btnInteger = 2;
										} else if (indents.getStatus().equals(
												"3")) {
												btn.setText("取消退款");
												btnInteger = 3;																					
										}
									}
								}						
							
							}
						}
					} else if (result.equals("30")) {
						Toast.makeText(IndentActivity.this, "已是最后一页",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(IndentActivity.this, "服务器在打盹",
								Toast.LENGTH_SHORT).show();
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
		params.put("cargoId", stateid);
		String path = Path.SERVER_ADRESS + "admission/findadmissionState1.htm";
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
					Toast.makeText(IndentActivity.this, "服务器在打盹",
							Toast.LENGTH_SHORT).show();
				} else {
					JSONObject object = JSONObject.parseObject(response);
					String result = object.getString("result");
					Intent intent;
					Intent i = new Intent();
					if (UesrInfo.getTourIDid().equals("")) {
						i.setClass(IndentActivity.this, LoginActivity.class);
						startActivity(i);
					} else {
						if (result.equals("1")) {
							Toast.makeText(IndentActivity.this, "该商品已被管理员禁用",
									Toast.LENGTH_SHORT).show();
						}else {
							i = new Intent();
							i.setClass(IndentActivity.this, OrderFromActivity.class);
							i.putExtra("scenicid", list.get(0).getAdmission()
									.getScenicid());
							i.putExtra("cargoid", list.get(0).getAdmission()
									.getCargoid());
							i.putExtra("title", list.get(0).getAdmission().getTitle());
							i.putExtra("price", list.get(0).getAdmission().getPrice());
							i.putExtra("cargoClassify", list.get(0).getAdmission()
									.getCargoclassify());
							i.putExtra("isems", list.get(0).getAdmission().getIsems());
							i.putExtra("id", "");
							i.putExtra("name", "");
							i.putExtra("dianhua", "");
							i.putExtra("dizhi", "");
							i.putExtra("type", list.get(0).getAdmission().getClassify());
							i.putExtra("childgroupprice", list.get(0).getAdmission()
									.getChildgroupprice());
							startActivity(i);
						}
					}

				}
			}
		});
	}
}
