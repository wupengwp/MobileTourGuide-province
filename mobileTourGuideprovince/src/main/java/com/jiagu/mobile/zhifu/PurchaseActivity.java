package com.jiagu.mobile.zhifu;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.jiagu.mobile.daohang.RouteGuideDemo;
import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.activities.LoginActivity;
import com.jiagu.mobile.tourguide.activities.PhotoAlbumActivity;
import com.jiagu.mobile.tourguide.activities.bases.TitleDrawerActivity;
import com.jiagu.mobile.tourguide.adapter.SceneDetialListViewAdapter;
import com.jiagu.mobile.tourguide.bean.Appraise;
import com.jiagu.mobile.tourguide.utils.HttpUtil;
import com.jiagu.mobile.tourguide.utils.MyImageLoader;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.tourguide.utils.UesrInfo;
import com.jiagu.mobile.zhifu.bean.Purchase;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.R.bool;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//购买详情页	高磊	2015-01-30
public class PurchaseActivity extends TitleDrawerActivity implements
		OnClickListener, OnGetGeoCoderResultListener {
	private ImageView collect, photoAlbum;
	private TextView tv02, tv03, tv04, tv05, tv06, tv07, tv08, tv09, tv10,
			tv11, phone, tv13, tv14, grouppurchase_datestart,
			grouppurchase_dateend;
	private Button btn, dingdan;
	private String id;
	private boolean collects = false;
	private long diff;
	private long days;
	private long hours;
	private long minutes;
	private long seconds;
	private String phonenumber;
	private List<Purchase> data;
	private ListView listView;
	private int Pages = 2;
	private ArrayList<Appraise> list;
	private SceneDetialListViewAdapter adapter = null;
	private int count = 0;
	private LinearLayout start_end;
	GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	private TextView tv_refund;
	private View refund_line;
	private boolean istype = false;//标记是敬请期待还是活动进行中

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_purchase_details);
		// 初始化搜索模块，注册事件监听
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);

		Intent i = getIntent();
		id = i.getStringExtra("id");

//		Message message = handler.obtainMessage(1);
//		handler.sendMessageDelayed(message, 1000);

		ImageView image = (ImageView) findViewById(R.id.grouppurchase_details_iv_01);
		collect = (ImageView) findViewById(R.id.grouppurchase_details_iv_02);
		listView = (ListView) findViewById(R.id.grouppurchase_details_listview);
		// 添加头部试图
		View topView = LayoutInflater.from(PurchaseActivity.this).inflate(
				R.layout.activity_group_top, null);
		photoAlbum = (ImageView) topView
				.findViewById(R.id.grouppurchase_details_iv_03);
		phone = (TextView) topView
				.findViewById(R.id.grouppurchase_details_iv_04);// 电话
		// tv01 = (TextView) topView.findViewById(R.id.grouppurchase_tv_01);//
		// 标题
		
		tv_refund = (TextView) topView.findViewById(R.id.grouppurchase_tv_refund);
		refund_line = (View) topView.findViewById(R.id.refund_line);
		tv02 = (TextView) topView.findViewById(R.id.grouppurchase_tv_02);// 那个景区的票
		tv03 = (TextView) topView.findViewById(R.id.grouppurchase_tv_03);// 现价
		tv04 = (TextView) topView.findViewById(R.id.grouppurchase_tv_04);// 原价
		tv04.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);// 加横线

		tv05 = (TextView) topView.findViewById(R.id.grouppurchase_tv_05);// 支持退款模式
		tv06 = (TextView) topView.findViewById(R.id.grouppurchase_tv_06);// 同上
		tv07 = (TextView) topView.findViewById(R.id.grouppurchase_tv_07);// 时间
		tv08 = (TextView) topView.findViewById(R.id.grouppurchase_tv_08);// 已售
		tv09 = (TextView) topView.findViewById(R.id.grouppurchase_tv_09);// 位置
		tv10 = (TextView) topView.findViewById(R.id.grouppurchase_tv_10);// 景区简介
		tv11 = (TextView) topView.findViewById(R.id.grouppurchase_tv_11);// 简介
		tv13 = (TextView) topView.findViewById(R.id.grouppurchase_tv_13);// 限时退款
		tv14 = (TextView) topView.findViewById(R.id.grouppurchase_tv_14);
		start_end = (LinearLayout) topView.findViewById(R.id.start_end);
		grouppurchase_datestart = (TextView) topView
				.findViewById(R.id.grouppurchase_datestart);
		grouppurchase_dateend = (TextView) topView
				.findViewById(R.id.grouppurchase_dateend);

		LinearLayout shangpu = (LinearLayout) topView
				.findViewById(R.id.activity_group_shangpu);
		// tv12 = (TextView) topView.findViewById(R.id.grouppurchase_tv_12);//
		// 有效期
		btn = (Button) topView.findViewById(R.id.grouppurchase_btn);// 购买
		dingdan = (Button) topView.findViewById(R.id.dingdan);// 我的订单
		listView.addHeaderView(topView);
		listView.setAdapter(null);
		// 添加底部试
		// View bottom = LayoutInflater.from(PurchaseActivity.this).inflate(
		// R.layout.bottom_button, null);
		// TextView more = (TextView) bottom.findViewById(R.id.bottom_textview);
		// listView.addFooterView(bottom);
		listView.setAdapter(null);
		image.setOnClickListener(this);
		tv09.setOnClickListener(this);
		collect.setOnClickListener(this);
		phone.setOnClickListener(this);
		photoAlbum.setOnClickListener(this);
		btn.setOnClickListener(this);
		dingdan.setOnClickListener(this);
		shangpu.setOnClickListener(this);
		// more.setOnClickListener(this);
		getData();
		// getTextData();
	}

	// private void getTextData() {
	// showProgress();
	// RequestParams params = new RequestParams();
	// params.put("appraiseId", id);
	// params.put("pages", "1");
	// params.put("type", "2");
	// String path = Path.SERVER_ADRESS + "scenic/scenicAppraise.htm";
	// HttpUtil.post(path, params, new AsyncHttpResponseHandler() {
	//
	// @Override
	// public void onFailure(Throwable arg0, String arg1) {
	// super.onFailure(arg0, arg1);
	// Toast.makeText(getApplicationContext(), "请检查您的网络",
	// Toast.LENGTH_LONG).show();
	// hideProgress();
	// }
	//
	// @Override
	// public void onSuccess(String response) {
	// super.onSuccess(response);
	// if (response != null) {
	// JSONObject object = JSONObject.parseObject(response);
	// JSONArray top = object.getJSONArray("records");
	// if (top != null) {
	// list = (ArrayList<Appraise>) JSONArray.parseArray(
	// top.toJSONString(), Appraise.class);
	// if (list != null && list.size() != 0) {
	// adapter = new SceneDetialListViewAdapter(
	// PurchaseActivity.this, list);
	// listView.setAdapter(adapter);
	// } else {
	// Toast.makeText(getApplicationContext(), "请检查您的网络",
	// Toast.LENGTH_LONG).show();
	// }
	// } else {
	// Toast.makeText(PurchaseActivity.this, "服务器在打盹",
	// Toast.LENGTH_SHORT).show();
	// }
	// } else {
	// Toast.makeText(PurchaseActivity.this, "服务器在打盹",
	// Toast.LENGTH_SHORT).show();
	// }
	// hideProgress();
	// }
	// });
	// }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		Intent i = new Intent();
		switch (v.getId()) {
		case R.id.grouppurchase_details_iv_01:// 返回
			onBackPressed();
			break;
		case R.id.activity_group_shangpu:// 商铺
			i.putExtra("id", data.get(0).getScenicid());
			i.putExtra("cargoClassify", data.get(0).getCargoclassify());
			i.setClass(PurchaseActivity.this, MarchantActivity.class);
			startActivity(i);
			finish();
			break;
		case R.id.dingdan:// 订单
			if (UesrInfo.getTourIDid().equals("")) {
				i = new Intent();
				i.setClass(PurchaseActivity.this, LoginActivity.class);
			} else {
				i.setClass(PurchaseActivity.this, IndentListViewActivity.class);
			}
			startActivity(i);
			break;
		case R.id.bottom_textview:// 评论加载更多
			RequestParams params = new RequestParams();
			params.put("appraiseId", id);
			params.put("pages", "" + Pages);
			params.put("type", "2");
			String path = Path.SERVER_ADRESS + "scenic/scenicAppraise.htm";
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
					if (!(response == null)) {
						JSONObject object = JSONObject.parseObject(response);
						JSONArray top = object.getJSONArray("records");
						String result = object.getString("result");
						if (top != null) {
							ArrayList<Appraise> lists = (ArrayList<Appraise>) JSONArray
									.parseArray(top.toJSONString(),
											Appraise.class);
							for (Appraise appraise : lists) {
								list.add(appraise);
							}
							adapter.setData(list);
							adapter.notifyDataSetChanged();
							Pages++;
						} else if (result.equals("30")) {
							Toast.makeText(PurchaseActivity.this, "已是最后一页",
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(PurchaseActivity.this, "服务器在打盹",
									Toast.LENGTH_SHORT).show();
						}
					} else {

					}
				}
			});
			break;
		case R.id.grouppurchase_details_iv_02:// 收藏
			if (UesrInfo.getTourIDid().equals("")) {
				i.setClass(PurchaseActivity.this, LoginActivity.class);
				startActivity(i);
			} else {
				setCollect();
			}
			break;
		case R.id.grouppurchase_details_iv_03:// 相册
//			switch (data.get(0).getCargoclassify().trim()) {
//			case "2":
//
//				break;
//
//			default:
			
				i.putExtra("type_type", data.get(0).getCargoclassify().trim());
				i.putExtra("photoId", data.get(0).getCargoid());
				i.putExtra("path", Path.SERVER_ADRESS
						+ "characteristic/photoList.htm");
				i.setClass(PurchaseActivity.this, PhotoAlbumActivity.class);
				startActivity(i);
//				break;
//			}

			break;
		case R.id.grouppurchase_details_iv_04:// 打电话
			intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
					+ phonenumber));
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
		case R.id.grouppurchase_tv_09:// 定位
//			if (data.get(0).getExchangeaddress() != null
//					&& !"".equals(data.get(0).getExchangeaddress())) {
//
//				String address = data.get(0).getExchangeaddress()
//						.replace("-", "").trim();
//				// Geo搜索
//				mSearch.geocode(new GeoCodeOption().city("").address(address));
//
//			} else {
				if (data.get(0).getAdressx().equals("")
						|| data.get(0).getAdressy().equals("")) {
					String address = data.get(0).getExchangeaddress()
							.replace("-", "").trim();
					// Geo搜索
					mSearch.geocode(new GeoCodeOption().city("").address(address));
				} else {
					i.setClass(getApplication(), RouteGuideDemo.class);
					i.putExtra("x", "" + data.get(0).getAdressx());
					i.putExtra("y", "" + data.get(0).getAdressy());
					i.putExtra("name", tv09.getText().toString());
					startActivity(i);
				}
//			}

			break;
		case R.id.grouppurchase_btn:// 提交订单
			getJYState();
			break;
		}
	}

	private void getData() {
		showProgress();
		RequestParams params = new RequestParams();
		params.put("id", id);
		params.put("tourId", UesrInfo.getTourIDid());
		String path = Path.SERVER_ADRESS + "admission/findAdmissionDetial.htm";
		HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				super.onFailure(arg0, arg1);
				count++;
				if (count < 3) {
					getData();
				} else {
					count = 0;
					Toast.makeText(getApplicationContext(), "请检查您的网络",
							Toast.LENGTH_LONG).show();
				}
				hideProgress();
			}

			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				if (response == null || response.equals("")) {
					Toast.makeText(PurchaseActivity.this, "服务器在打盹",
							Toast.LENGTH_SHORT).show();
				} else {
					JSONObject object = JSONObject.parseObject(response);
					JSONArray top = object.getJSONArray("records");
					String result = object.getString("result");
					if (top == null || top.equals("") || result.equals("4")) {
						Toast.makeText(PurchaseActivity.this, "服务器在打盹",
								Toast.LENGTH_SHORT).show();
					} else if (result.equals("30")) {
						Toast.makeText(PurchaseActivity.this, "已是最后一页",
								Toast.LENGTH_SHORT).show();
					} else if (result.equals("0")) {
						data = JSONArray.parseArray(top.toJSONString(),
								Purchase.class);						
						
						if (data.size() != 0) {
							tv02.setText(data.get(0).getTitle());
							tv03.setText("¥" + data.get(0).getPrice());
							tv04.setText("¥" + data.get(0).getOldPrice());

							if (data.get(0).getCargoclassify().equals("0")
									|| data.get(0).getCargoclassify()
											.equals("1")) {
								start_end.setVisibility(View.VISIBLE);
								if (data.get(0).getExchangeDateStart() != null
										&& !"".equals(data.get(0)
												.getExchangeDateStart())) {
									grouppurchase_datestart.setText("兑换开始:"
											+ data.get(0)
													.getExchangeDateStart());
								}
								if (data.get(0).getExchangeDateOver() != null
										&& !"".equals(data.get(0)
												.getExchangeDateOver())) {
									grouppurchase_dateend
											.setText("兑换截止:"
													+ data.get(0)
															.getExchangeDateOver());
								}

								if (data.get(0).getRefundNow().equals("0")) {
									tv05.setText("支持随时退款");
								} else {
									tv05.setText("不支持随时退款");
								}
								if (data.get(0).getRefundOver().equals("0")) {
									tv06.setText("支持过期退款");
									if ("".equals(data.get(0).getRefundtime())||equals(data.get(0).getRefundtime()==null)) {
										tv14.setVisibility(View.GONE);
									}else {
										tv14.setText("退款时限:截止至"
												+ data.get(0).getRefundtime());
										tv14.setVisibility(View.VISIBLE);
									}
									
								} else {
									tv06.setText("不支持过期退款");
								}
								if (data.get(0).getRefundLimit() != null) {
									if (data.get(0).getRefundLimit()
											.equals("0")) {
										tv13.setText("支持限时退款");
										String[] split = data.get(0)
												.getRefundtime().toString()
												.split("-");
							if (split==null||split.length<=0) {
										tv14.setVisibility(View.GONE);
								}else {
										tv14.setText("退款时限:" + split[0]
												+ ":00至" + split[1] + ":59");
										tv14.setVisibility(View.VISIBLE);
										}
									} else {
										tv13.setText("不支持限时退款");
									}
								} else {
									tv13.setVisibility(View.GONE);
								}

							} else {
								if ("".equals(data.get(0).getZwyoldprice())||data.get(0).getZwyoldprice()==null) {
									tv04.setText("¥0");
								}else {
								tv04.setText("¥" + data.get(0).getZwyoldprice());//显示原价
								}
								tv05.setVisibility(View.GONE);
								tv06.setVisibility(View.GONE);
								tv13.setVisibility(View.GONE);
								tv07.setVisibility(View.GONE);
								if (data.get(0).getRefundLimit()!=null && !"".equals(data.get(0).getRefundLimit())) {
									tv_refund.setVisibility(View.VISIBLE);
									refund_line.setVisibility(View.VISIBLE);
									tv_refund.setText("退款说明:"+data.get(0).getRefundLimit().trim());
								}
							}

							getdataTime(data.get(0).getTimeOver(), data.get(0)
									.getTimeStart(), data.get(0).getNowDate());
//							Message message = handler.obtainMessage(1);
//							handler.sendMessageDelayed(message, 1000);
							
							tv08.setText("已售" + data.get(0).getSoldNumber());
							if (data.get(0).getExchangeaddress() != null
									&& !"".equals(data.get(0)
											.getExchangeaddress())) {
								tv09.setText(data.get(0).getExchangeaddress());
							} else {
								tv09.setText(data.get(0).getAdress());
							}

							tv10.setText(data.get(0).getText());
							tv11.setText(data.get(0).getCashNotice());
							if (data.get(0).getImageurl() != null) {
								switch (data.get(0).getCargoclassify().trim()) {
								case "2":
									MyImageLoader.loadImage(data.get(0)
											.getImageurl(), photoAlbum);
									break;

								default:
									MyImageLoader.loadImage(Path.IMAGER_ADRESS
											+ data.get(0).getImageurl(),
											photoAlbum);
									break;
								}
							}
							String isCollect = data.get(0).getIsCollect();
							if (isCollect.equals("0")) {
								collect.setImageResource(R.drawable.shoucang1);
								collects = false;
							} else {
								collect.setImageResource(R.drawable.shoucang);
								collects = true;
							}
							phonenumber = data.get(0).getPhonenumber();
							phone.setText("   " + phonenumber);
						} else {
							Toast.makeText(PurchaseActivity.this, "服务器在打盹!",
									Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(PurchaseActivity.this, "服务器在打盹!",
								Toast.LENGTH_SHORT).show();
					}
				}
				hideProgress();
			}
		});
	}

	/*
	 * 获取禁用状态
	 */
	private void getJYState() {

		RequestParams params = new RequestParams();
		params.put("id", id);
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
					Toast.makeText(PurchaseActivity.this, "服务器在打盹",
							Toast.LENGTH_SHORT).show();
				} else {
					JSONObject object = JSONObject.parseObject(response);
					String result = object.getString("result");
					Intent intent;
					Intent i = new Intent();
					if (UesrInfo.getTourIDid().equals("")) {
						i.setClass(PurchaseActivity.this, LoginActivity.class);
						startActivity(i);
					} else {
						if (result.equals("1")) {
							Toast.makeText(PurchaseActivity.this, "该商品已被管理员禁用",
									Toast.LENGTH_SHORT).show();
						}/*
						 * else if
						 * (data.get(0).getCargoclassify().trim().equals("2")) {
						 * Toast.makeText(PurchaseActivity.this, "该商品暂时无法购买",
						 * Toast.LENGTH_SHORT).show();
						 */
						/* } */else {
							intent = new Intent(PurchaseActivity.this,
									OrderFromActivity.class);
							intent.putExtra("scenicid", data.get(0)
									.getScenicid());
							intent.putExtra("cargoid", data.get(0).getCargoid());
							intent.putExtra("title", data.get(0).getTitle());
							intent.putExtra("price", data.get(0).getPrice());
							intent.putExtra("cargoClassify", data.get(0)
									.getCargoclassify());
							intent.putExtra("isems", data.get(0).getIsems());
							intent.putExtra("childgroupprice", data.get(0)
									.getChildgroupprice());
							intent.putExtra("id", "");
							intent.putExtra("name", "");
							intent.putExtra("dianhua", "");
							intent.putExtra("idstate", id);// 查询禁用状态
							intent.putExtra("dizhi", "");
							intent.putExtra("type", data.get(0).getClassify());
							intent.putExtra("ratio", data.get(0).getRatio());
							startActivity(intent);
						}
					}

				}
			}
		});
	}

	// 时间计算
	private void getdataTime(String over, String start, String nowDate) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date d1 = df.parse(over);
			Date d2 = df.parse(start);
			Date d3 = df.parse(nowDate);
			if (d1.getTime() < d3.getTime()) {
				tv07.setText("活动已结束");
				btn.setClickable(false);
				btn.setBackgroundResource(R.drawable.btn_ok_bg_perssed);
				return;
			}else if (d2.getTime() > d3.getTime()) {
				istype = false;
				diff = d2.getTime() - d3.getTime();// 这样得到的差值是微秒级别
				btn.setText("敬请期待");
				btn.setClickable(false);
				btn.setBackgroundResource(R.drawable.btn_ok_bg_perssed);
				
			} else {
				istype = true;
				diff = d1.getTime() - d3.getTime();
				btn.setText("立即购买");
				btn.setClickable(true);
				btn.setBackgroundResource(R.drawable.btn_ok_bg_normal);
			}
			days = diff / (1000 * 60 * 60 * 24);
			hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
			minutes = (diff - days * (1000 * 60 * 60 * 24) - hours
					* (1000 * 60 * 60))
					/ (1000 * 60);
			seconds = (diff - days * (1000 * 60 * 60 * 24) - hours
					* (1000 * 60 * 60) - minutes * (1000 * 60)) / (1000);
			tv07.setText("" + days + "天" + hours + "小时" + minutes + "分"
					+ seconds + "秒");
			getTime(diff);
		} catch (Exception e) {

		}
	}

	private void getTime(long diff) {
		days = diff / (1000 * 60 * 60 * 24);

		hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);

		minutes = (diff - days * (1000 * 60 * 60 * 24) - hours
				* (1000 * 60 * 60))
				/ (1000 * 60);

		seconds = (diff - days * (1000 * 60 * 60 * 24) - hours
				* (1000 * 60 * 60) - minutes * (1000 * 60)) / (1000);
		tv07.setText("" + days + "天" + hours + "小时" + minutes + "分" + seconds
				+ "秒");
		handlertime.postDelayed(runnable, 1000);  
	}

//	final Handler handler = new Handler() {
//
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case 1:
//				getShowTime();
//				if (diff >= 0) {
//					
//					Message message = handler.obtainMessage(1);
//					handler.sendMessageDelayed(message, 1000);
//					diff = diff - 1000;
//				} else {
//					// tv07.setVisibility(View.GONE);
//					if(!data.get(0).getCargoclassify().equals("2")){
//						tv07.setText("活动已结束");
//						btn.setClickable(false);
//						btn.setBackgroundResource(R.drawable.btn_ok_bg_perssed);
//					}
//				}
//				break;
//			default:
//				break;
//			}
//			super.handleMessage(msg);
//		}
//	};

	/**
	 * 获得要显示的时间
	 */
	private void getShowTime() {
		days = diff / (1000 * 60 * 60 * 24);
		hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		minutes = (diff - days * (1000 * 60 * 60 * 24) - hours
				* (1000 * 60 * 60))
				/ (1000 * 60);
		seconds = (diff - days * (1000 * 60 * 60 * 24) - hours
				* (1000 * 60 * 60) - minutes * (1000 * 60)) / (1000);
		tv07.setText("" + days + "天" + hours + "小时" + minutes + "分" + seconds
				+ "秒");
	}

	/**
	 * 收藏
	 */
	private void setCollect() {
		RequestParams params = new RequestParams();
		params.put("tourId", UesrInfo.getTourIDid());
		params.put("collectId", id);
		params.put("type", "1");
		String path = Path.SERVER_ADRESS
				+ "personalCenter/insertTouristCollect.htm";
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
					Toast.makeText(PurchaseActivity.this, "服务器在打盹",
							Toast.LENGTH_SHORT).show();
				} else {
					JSONObject object = JSONObject.parseObject(response);
					String result = object.getString("result");
					if (result.equals("0")) {
						if (collects) {
							collect.setImageResource(R.drawable.shoucang1);
							collects = false;
							Toast.makeText(PurchaseActivity.this, "已取消收藏",
									Toast.LENGTH_SHORT).show();
						} else {
							collect.setImageResource(R.drawable.shoucang);
							collects = true;
							Toast.makeText(PurchaseActivity.this, "已成功收藏",
									Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(PurchaseActivity.this, "操作失败",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		// TODO Auto-generated method stub
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(PurchaseActivity.this, "抱歉，未能找到结果",
					Toast.LENGTH_LONG).show();
			return;
		}
		Intent i = new Intent();
		i.setClass(getApplication(), RouteGuideDemo.class);
		i.putExtra("x", "" + result.getLocation().longitude);
		i.putExtra("y", "" + result.getLocation().latitude);
		i.putExtra("name", tv09.getText().toString());
		startActivity(i);
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {

		super.onResume();
		// setAppName(UesrInfo.area);
		appName.setText(UesrInfo.area + "手机导游");
		handlertime.removeCallbacks(runnable);
		getData();
		setMap();
	}

	@Override
	protected void onDestroy() {
		mSearch.destroy();
		super.onDestroy();
	}
	//倒计时
	 Handler handlertime = new Handler();  
	 Runnable runnable = new Runnable() {  
	 @Override  
	 public void run() {  
		 getShowTime();
		 if (diff >= 0) {			 
				diff = diff - 500;
			} else {
				// tv07.setVisibility(View.GONE);
				if(!data.get(0).getCargoclassify().equals("2")){
					if (istype) {
						tv07.setText("活动已结束");
						btn.setClickable(false);
						btn.setBackgroundResource(R.drawable.btn_ok_bg_perssed);
						handlertime.removeCallbacks(runnable);
					}else {
						btn.setText("立即购买");
						btn.setClickable(true);
						btn.setBackgroundResource(R.drawable.btn_ok_bg_normal);
						getData();
					}
					
				}
			}
		 handlertime.postDelayed(this, 1000);  
		
	  }  
	  }; 

}
