package com.jiagu.mobile.dateselect;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ab.activity.AbActivity.Method;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.activities.bases.BaseActivity;
import com.jiagu.mobile.tourguide.application.MobileTourGuideApplication;
import com.jiagu.mobile.tourguide.bean.Area;
import com.jiagu.mobile.tourguide.utils.HttpUtil;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.zhifu.OrderFromActivity;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class DateSelectActivity extends Activity implements OnClickListener {
	private static final String TAG = DateSelectActivity.class.getSimpleName();
	private ScrollView mScrollView;
	private DatepickerParam mDatepickerParam;
	private Context context = this;
	private int scrollHeight = 0;
	private LinearLayout mLinearLayoutSelected;
	private String cargoid;
	private JSONArray pricearray;
	private int numprice = 0;
	private ProgressDialog dialog;
	private String ratio;
	private float precent = 100;

	// Handler myHandler = new Handler(){
	//
	// public void handleMessage(Message msg) {
	// switch (msg.what) {
	// case 1:
	// dialog.dismiss();
	// break;
	// }
	// };
	//
	// };

	// 获取对应的属性值 Android框架自带的属性 attr
	int pressed = android.R.attr.state_pressed;
	int enabled = android.R.attr.state_enabled;
	int selected = android.R.attr.state_selected;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mScrollView = new ScrollView(this);
		mScrollView.setLayoutParams(new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT));
		setContentView(mScrollView);
		mScrollView.setVisibility(View.VISIBLE);
		mDatepickerParam = new DatepickerParam();
		mDatepickerParam.startDate = DateTimeUtils.getCurrentDateTime();
		mDatepickerParam.dateRange = 66;
		cargoid = getIntent().getStringExtra("cargoid");

		ratio = getIntent().getStringExtra("ratio");
		if (ratio != null && !(ratio.equals(""))) {
			precent = Float.parseFloat(ratio);
		}
		getPrice();

	}

	/**
	 * 获取一行 七天的LinearLayout
	 * 
	 * @return
	 */
	private LinearLayout getOneLineDayLinearLayout() {
		LinearLayout localLinearLayout = new LinearLayout(this);
		localLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
		localLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		for (int i = 0; i < 7; i++) {
			float height = (MobileTourGuideApplication.getScreenWidth()
					- ScreenUtil.dip2px(context, 10f) - ScreenUtil.dip2px(
					context, 1.5f * 6)) / 7;
			Log.i(TAG, "height:" + height);
			LinearLayout.LayoutParams localLayoutParams4 = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, (int) height, 1.0F);
			RelativeLayout localRelativeLayout = new RelativeLayout(context);
			localRelativeLayout.setLayoutParams(localLayoutParams4);
			localLayoutParams4.setMargins(ScreenUtil.dip2px(this, 1.5F),
					ScreenUtil.dip2px(this, 1.5F),
					ScreenUtil.dip2px(this, 1.5F),
					ScreenUtil.dip2px(this, 1.5F));
			TextView localTextView3 = new TextView(this);
			localTextView3.setLayoutParams(localLayoutParams4);
			localTextView3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.0F);
			localTextView3.setBackgroundDrawable(getBackGroundDrawable());
			localTextView3.setTextColor(getTextColorBlack());
			localTextView3.setGravity(Gravity.CENTER);
			localTextView3.setOnClickListener(this);
			localTextView3.setVisibility(View.VISIBLE);
			localRelativeLayout.addView(localTextView3, 0);

			RelativeLayout localRelativeLayout2 = new RelativeLayout(this);
			localRelativeLayout2.setLayoutParams(localLayoutParams4);
			localRelativeLayout2.setOnClickListener(this);
			localRelativeLayout2.setBackgroundDrawable(getBackGroundDrawable());
			TextView localTextView1 = new TextView(this);
			localTextView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.0F);
			localTextView1.setId(1);
			localTextView1.setTextColor(getTextColorBlack());
			TextView localTextView2 = new TextView(this);
			localTextView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10.0F);
			localTextView2.setTextColor(context.getResources().getColor(
					R.color.calendar_color_orange));
			// localTextView2.setText("出发");
			RelativeLayout.LayoutParams localLayoutParams2 = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			localLayoutParams2.addRule(RelativeLayout.CENTER_HORIZONTAL);
			localLayoutParams2.topMargin = ScreenUtil.dip2px(context, 4f);
			localRelativeLayout2.addView(localTextView1, 0, localLayoutParams2);
			RelativeLayout.LayoutParams localLayoutParams3 = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			localLayoutParams3.addRule(RelativeLayout.CENTER_HORIZONTAL);
			localLayoutParams3.addRule(RelativeLayout.BELOW, 1);
			localRelativeLayout2.addView(localTextView2, 1, localLayoutParams3);
			localRelativeLayout2.setVisibility(View.INVISIBLE);
			localRelativeLayout.addView(localRelativeLayout2, 1);

			localLinearLayout.addView(localRelativeLayout, i);

		}
		return localLinearLayout;
	}

	@Override
	public void onClick(View paramView) {
		if (paramView.getTag() != null) {
			Calendar localCalendar = Calendar.getInstance();
			localCalendar.setTimeInMillis(((Long) paramView.getTag())
					.longValue());
			// setResult(-1, getIntent().putExtra("pickedDate", localCalendar));
			String setdate = localCalendar.get(Calendar.YEAR) + "-"
					+ (localCalendar.get(Calendar.MONTH) + 1) + "-"
					+ localCalendar.get(Calendar.DAY_OF_MONTH);

			if (getDate(localCalendar)) {

				for (int l = 0; l < pricearray.size(); l++) {
					JSONObject price = (JSONObject) pricearray.get(l);
					if (compareCal(
							DateTimeUtils.getCalendar(price.get("date")
									.toString().trim()), localCalendar) == 0) {

						OrderFromActivity.orderFromActivity.money.setText(Math
								.round(Float.parseFloat(price
										.get("SettlementPrice").toString()
										.trim())
										* (precent / 100)
										+ Float.parseFloat(price
												.get("SettlementPrice")
												.toString().trim()))
								+ "");
						OrderFromActivity.orderFromActivity.moneys
								.setText(Math.round(Float.parseFloat(price
										.get("SettlementPrice").toString()
										.trim())
										* (precent / 100)
										+ Float.parseFloat(price
												.get("SettlementPrice")
												.toString().trim()))
										* Integer
												.parseInt(OrderFromActivity.orderFromActivity.numbersd
														.getText().toString()
														.trim()) + "");
					}
				}

				OrderFromActivity.orderFromActivity.orderform_selectdata
						.setText(setdate);
				finish();

			} else {
				Toast.makeText(context, "当前日期不可预订", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public boolean getDate(Calendar localCalendar) {
		for (int l = 0; l < pricearray.size(); l++) {
			JSONObject price = (JSONObject) pricearray.get(l);
			if (compareCal(
					DateTimeUtils.getCalendar(price.get("date").toString()
							.trim()), localCalendar) == 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 比较两个日期的大小
	 * 
	 * @param paramCalendar1
	 * @param paramCalendar2
	 * @return
	 */
	private int compareCal(Calendar paramCalendar1, Calendar paramCalendar2) {
		if (paramCalendar1.get(Calendar.YEAR) > paramCalendar2
				.get(Calendar.YEAR)) {
			return 1;
		} else if (paramCalendar1.get(Calendar.YEAR) < paramCalendar2
				.get(Calendar.YEAR)) {
			return -1;
		} else {
			if (paramCalendar1.get(Calendar.MONTH) > paramCalendar2
					.get(Calendar.MONTH)) {
				return 1;
			} else if (paramCalendar1.get(Calendar.MONTH) < paramCalendar2
					.get(Calendar.MONTH)) {
				return -1;
			} else {
				if (paramCalendar1.get(Calendar.DAY_OF_MONTH) > paramCalendar2
						.get(Calendar.DAY_OF_MONTH)) {
					return 1;
				} else if (paramCalendar1.get(Calendar.DAY_OF_MONTH) < paramCalendar2
						.get(Calendar.DAY_OF_MONTH)) {
					return -1;
				} else {
					return 0;
				}
			}
		}
	}

	/**
	 * 点击背景切换
	 * 
	 * @return
	 */
	private StateListDrawable getBackGroundDrawable() {
		// 获取对应的属性值 Android框架自带的属性 attr
		int pressed = android.R.attr.state_pressed;
		int enabled = android.R.attr.state_enabled;
		int selected = android.R.attr.state_selected;

		StateListDrawable localStateListDrawable = new StateListDrawable();
		ColorDrawable localColorDrawable1 = new ColorDrawable(context
				.getResources().getColor(android.R.color.transparent));
		// ColorDrawable localColorDrawable2 = new ColorDrawable(context
		// .getResources().getColor(R.color.blue));
		Drawable localColorDrawable2 = context.getResources().getDrawable(
				R.drawable.bg_calendar_seleced);
		ColorDrawable localColorDrawable3 = new ColorDrawable(context
				.getResources().getColor(android.R.color.transparent));
		localStateListDrawable.addState(new int[] { pressed, enabled },
				localColorDrawable2);
		localStateListDrawable.addState(new int[] { selected, enabled },
				localColorDrawable2);
		localStateListDrawable.addState(new int[] { enabled },
				localColorDrawable1);
		localStateListDrawable.addState(new int[0], localColorDrawable3);
		return localStateListDrawable;
	}

	/**
	 * 字体颜色 切换
	 * 
	 * @return
	 */
	private ColorStateList getTextColorBlack()

	{
		return new ColorStateList(new int[][] { { pressed, enabled },
				{ selected, enabled }, { enabled }, new int[0] }, new int[] {
				-1, -1,
				context.getResources().getColor(R.color.calendar_color_black),
				context.getResources().getColor(R.color.calendar_color_white) });
	}

	private ColorStateList getTextColorRed()

	{
		return new ColorStateList(new int[][] { { pressed, enabled },
				{ selected, enabled }, { enabled }, new int[0] }, new int[] {
				-1, -1,
				context.getResources().getColor(R.color.calendar_color_orange),
				context.getResources().getColor(R.color.calendar_color_white) });
	}

	private ColorStateList getTextColorGreen()

	{
		return new ColorStateList(new int[][] { { pressed, enabled },
				{ selected, enabled }, { enabled }, new int[0] }, new int[] {
				-1, -1,
				context.getResources().getColor(R.color.calendar_color_green),
				context.getResources().getColor(R.color.calendar_color_white) });
	}

	// 获取价格表
	public void getPrice() {
		RequestParams params = new RequestParams(); // 绑定参数
		params.put("cargoId", cargoid); // "7830052"

		dialog = ProgressDialog.show(context, null, "正在初始化日历...", false, true);

		HttpUtil.post(Path.SERVER_ADRESS + "admission/priceDate.htm", params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onFailure(Throwable arg0, String arg1) {
						// TODO Auto-generated method stub
						super.onFailure(arg0, arg1);
						dialog.dismiss();
					}

					@Override
					public void onSuccess(String response) {
						// TODO Auto-generated method stub
						super.onSuccess(response);

						if (response.equals("") || response == null) {
							dialog.dismiss();
							finish();
							Toast.makeText(context, "服务器在打盹",
									Toast.LENGTH_SHORT).show();
						} else {
							JSONObject pricejson = JSONObject
									.parseObject(response);
							if ("1".trim().equals(pricejson.get("status").toString().trim())) {
								if (null!=pricejson.get("prices") && !"".equals(pricejson.get("prices"))) {
									pricearray = pricejson.getJSONObject("prices")
											.getJSONArray("price");
									if (pricearray!=null && pricearray.size()>0) {
										setDate(pricearray);
									}
								}else {
									dialog.dismiss();
									finish();
									Toast.makeText(context, "该商品暂不能预定",
											Toast.LENGTH_SHORT).show();
								}													
								// runOnUiThread(new Runnable(){
								//
								// @Override
								// public void run() {
								// //更新UI
								// setDate(pricearray);
								// Message msg = new Message();
								// msg.what = 1;
								// myHandler.sendMessage(msg);
								// }
								//
								// });

							}else {
								dialog.dismiss();
								finish();
								Toast.makeText(context, "该商品暂不能预定",
										Toast.LENGTH_SHORT).show();
							}
						}
					}
				});
	}

	public void setDate(JSONArray pricearray) {
		LinearLayout localLinearLayout1 = new LinearLayout(this);
		localLinearLayout1.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		localLinearLayout1.setOrientation(LinearLayout.VERTICAL);

		// LinearLayout topbar = (LinearLayout) View.inflate(
		// context, R.layout.topbar, null);
		// topbar.setLayoutParams(new LinearLayout.LayoutParams(
		// LinearLayout.LayoutParams.MATCH_PARENT,
		// 80));
		// topbar.findViewById(R.id.backimg).setOnClickListener(new
		// OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// finish();
		// }
		// });
		// localLinearLayout1.addView(topbar);

		mScrollView.addView(localLinearLayout1);
		localLinearLayout1.setPadding(ScreenUtil.dip2px(context, 5f),
				ScreenUtil.dip2px(context, 5f), ScreenUtil.dip2px(context, 5f),
				0);
		Calendar localCalendar1 = (Calendar) mDatepickerParam.startDate.clone();
		Calendar calendarToday = (Calendar) localCalendar1.clone();
		Calendar calendarTomorrow = (Calendar) localCalendar1.clone();
		calendarTomorrow.add(Calendar.DAY_OF_MONTH, 1);
		Calendar calendarTwoMore = (Calendar) localCalendar1.clone();
		calendarTwoMore.add(Calendar.DAY_OF_MONTH, 2);
		// Calendar selectedCalendar = (Calendar) mDatepickerParam.selectedDay
		// .clone();
		int yearOfLocalCalendar1 = localCalendar1.get(Calendar.YEAR);
		int monthOfLocalCalendar1 = localCalendar1.get(Calendar.MONTH);
		Calendar localCalendarEnd = (Calendar) mDatepickerParam.startDate
				.clone();
		localCalendarEnd.add(Calendar.DAY_OF_MONTH,
				mDatepickerParam.dateRange - 1);
		int yearOfLocalCalendar2 = localCalendarEnd.get(Calendar.YEAR);
		int monthOfLocalCalendar2 = localCalendarEnd.get(Calendar.MONTH);

		int differOfYear = yearOfLocalCalendar2 - yearOfLocalCalendar1;
		int differOfMonth = monthOfLocalCalendar2 - monthOfLocalCalendar1;

		// 涉及到的月份数
		int totalDiffer = differOfYear * 12 + differOfMonth + 1;

		for (int i = 0; i < totalDiffer; i++) {
			LinearLayout localLinearLayout2 = (LinearLayout) View.inflate(
					context, R.layout.date_pick_head, null);
			localLinearLayout1.addView(localLinearLayout2,
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			TextView localTextView1 = (TextView) localLinearLayout2
					.findViewById(R.id.tv_cal_year);
			TextView localTextView2 = (TextView) localLinearLayout2
					.findViewById(R.id.tv_cal_month);
			Calendar tempCalendar = (Calendar) localCalendar1.clone();
			tempCalendar.add(Calendar.YEAR, i / 11);// TODO
			localTextView1.setText(tempCalendar.get(Calendar.YEAR) + "年");
			Calendar tempCalendar2 = (Calendar) localCalendar1.clone();
			tempCalendar2.add(Calendar.MONTH, i);
			localTextView2.setText(tempCalendar2.get(Calendar.MONTH) + 1 + "月");
			tempCalendar2.set(Calendar.DAY_OF_MONTH, 1);
			// 星期天-星期六 Calendar.DAY_OF_WEEK = 1-7
			int weekOfDay = tempCalendar2.get(Calendar.DAY_OF_WEEK) - 1;
			Log.i(TAG, "weekOfDay:" + weekOfDay);
			int maxOfMonth = tempCalendar2
					.getActualMaximum(Calendar.DAY_OF_MONTH);
			Log.i(TAG, "maxOfMonth:" + maxOfMonth);
			int lines = (int) Math.ceil((weekOfDay + maxOfMonth) / 7.0f);
			Log.i(TAG, "lines:" + lines);
			// 开始日期之前和结束日期之后的变灰
			int startDay = localCalendar1.get(Calendar.DAY_OF_MONTH);

			for (int j = 0; j < lines; j++) {
				LinearLayout oneLineLinearLayout = getOneLineDayLinearLayout();
				if (j == 0) {// 第一行
					for (int k = 0; k < 7; k++) {
						TextView localTextView = (TextView) (((RelativeLayout) oneLineLinearLayout
								.getChildAt(k)).getChildAt(0));
						RelativeLayout localSelectedRela = (RelativeLayout) (((RelativeLayout) oneLineLinearLayout
								.getChildAt(k)).getChildAt(1));
						TextView localTextViewSelected = (TextView) localSelectedRela
								.getChildAt(0);
						if (k >= weekOfDay) {
							int index = k - weekOfDay + 1;
							localTextView.setText(index + "");
							localTextViewSelected.setText(index + "");
							Calendar tempCalendar3 = (Calendar) tempCalendar2
									.clone();
							tempCalendar3.set(Calendar.DAY_OF_MONTH, index);
							String date = tempCalendar3.get(Calendar.YEAR)
									+ "-"
									+ (tempCalendar3.get(Calendar.MONTH) + 1)
									+ "-"
									+ tempCalendar3.get(Calendar.DAY_OF_MONTH);

							localTextView.setTag(Long.valueOf(tempCalendar3
									.getTimeInMillis()));
							localSelectedRela.setTag(Long.valueOf(tempCalendar3
									.getTimeInMillis()));
							TextView localTextViewPrice = (TextView) localSelectedRela
									.getChildAt(1);
							if (numprice <= pricearray.size()) {
								Calendar localCalendar = Calendar.getInstance();
								localCalendar
										.setTimeInMillis(((Long) localTextView
												.getTag()).longValue());

								for (int l = 0; l < pricearray.size(); l++) {
									JSONObject price = (JSONObject) pricearray
											.get(l);
									if (compareCal(
											DateTimeUtils.getCalendar(price
													.get("date").toString()
													.trim()), localCalendar) == 0) {
										localTextView
												.setVisibility(View.INVISIBLE);
										localSelectedRela
												.setVisibility(View.VISIBLE);
										localTextViewPrice
												.setText("¥"
														+ Math.round(Float
																.parseFloat(price
																		.get("SettlementPrice")
																		.toString()
																		.trim())
																* (precent / 100)
																+ Float.parseFloat(price
																		.get("SettlementPrice")
																		.toString()
																		.trim())));
										numprice++;
										break;
									}
								}
							}
							if (compareCal(tempCalendar3, calendarToday) == -1) {// 小于当天
								localTextView.setTextColor(getResources()
										.getColor(R.color.calendar_color_gray));
								localTextView.setEnabled(false);
							}

							if (Constants.HOLIDAYS.get(date) != null) {
								localTextView.setText(Constants.HOLIDAYS
										.get(date));
								localTextViewSelected
										.setText(Constants.HOLIDAYS.get(date));
								localTextView.setTextSize(
										TypedValue.COMPLEX_UNIT_SP, 14.0f);
								localTextViewSelected.setTextSize(
										TypedValue.COMPLEX_UNIT_SP, 14.0f);
								localTextView.setTextColor(getTextColorGreen());
							}

							if (compareCal(tempCalendar3, calendarToday) == 0) {// 今天
								localTextView.setTextColor(getTextColorRed());
								localTextView.setText("今天");
								localTextViewSelected.setText("今天");
								localTextView.setTextSize(
										TypedValue.COMPLEX_UNIT_SP, 16.0f);
							}

							if (compareCal(tempCalendar3, localCalendarEnd) == 1) {// 大于截止日
								localTextView.setTextColor(getResources()
										.getColor(R.color.calendar_color_gray));
								localTextView.setEnabled(false);
							}

						} else {
							localTextView.setVisibility(View.INVISIBLE);
						}
					}
				} else if (j == lines - 1) {// 最后一行
					int temp = maxOfMonth - (lines - 2) * 7 - (7 - weekOfDay);
					for (int k = 0; k < 7; k++) {
						TextView localTextView = (TextView) (((RelativeLayout) oneLineLinearLayout
								.getChildAt(k)).getChildAt(0));
						RelativeLayout localSelectedRela = (RelativeLayout) (((RelativeLayout) oneLineLinearLayout
								.getChildAt(k)).getChildAt(1));
						TextView localTextViewSelected = (TextView) localSelectedRela
								.getChildAt(0);
						if (k < temp) {
							int index = (7 - weekOfDay) + (j - 1) * 7 + k + 1;
							localTextView.setText(index + "");
							localTextViewSelected.setText(index + "");
							Calendar tempCalendar3 = (Calendar) tempCalendar2
									.clone();
							tempCalendar3.set(Calendar.DAY_OF_MONTH, index);
							String date = tempCalendar3.get(Calendar.YEAR)
									+ "-"
									+ (tempCalendar3.get(Calendar.MONTH) + 1)
									+ "-"
									+ tempCalendar3.get(Calendar.DAY_OF_MONTH);
							localTextView.setTag(Long.valueOf(tempCalendar3
									.getTimeInMillis()));
							localSelectedRela.setTag(Long.valueOf(tempCalendar3
									.getTimeInMillis()));
							TextView localTextViewPrice = (TextView) localSelectedRela
									.getChildAt(1);

							if (numprice <= pricearray.size()) {
								Calendar localCalendar = Calendar.getInstance();
								localCalendar
										.setTimeInMillis(((Long) localTextView
												.getTag()).longValue());

								for (int l = 0; l < pricearray.size(); l++) {
									JSONObject price = (JSONObject) pricearray
											.get(l);
									if (compareCal(
											DateTimeUtils.getCalendar(price
													.get("date").toString()
													.trim()), localCalendar) == 0) {
										localTextView
												.setVisibility(View.INVISIBLE);
										localSelectedRela
												.setVisibility(View.VISIBLE);
										localTextViewPrice
												.setText("¥"
														+ Math.round(Float
																.parseFloat(price
																		.get("SettlementPrice")
																		.toString()
																		.trim())
																* (precent / 100)
																+ Float.parseFloat(price
																		.get("SettlementPrice")
																		.toString()
																		.trim())));
										numprice++;
										break;
									}
								}
							}

							if (compareCal(tempCalendar3, calendarToday) == -1) {// 小于当天
								localTextView.setTextColor(getResources()
										.getColor(R.color.calendar_color_gray));
								localTextView.setEnabled(false);
							}
							if (Constants.HOLIDAYS.get(date) != null) {
								localTextView.setText(Constants.HOLIDAYS
										.get(date));
								localTextViewSelected
										.setText(Constants.HOLIDAYS.get(date));
								localTextView.setTextSize(
										TypedValue.COMPLEX_UNIT_SP, 14.0f);
								localTextViewSelected.setTextSize(
										TypedValue.COMPLEX_UNIT_SP, 14.0f);
								localTextView.setTextColor(getTextColorGreen());
							}

							if (compareCal(tempCalendar3, calendarToday) == 0) {// 今天
								localTextView.setTextColor(getTextColorRed());
								localTextView.setText("今天");
								localTextViewSelected.setText("今天");
								localTextView.setTextSize(
										TypedValue.COMPLEX_UNIT_SP, 16.0f);
							}

							if (compareCal(tempCalendar3, localCalendarEnd) == 1) {// 大于截止日
								localTextView.setTextColor(getResources()
										.getColor(R.color.calendar_color_gray));
								localTextView.setEnabled(false);
							}

						} else {
							localTextView.setVisibility(View.INVISIBLE);
						}
					}

				} else {// 中间
					for (int k = 0; k < 7; k++) {

						TextView localTextView = (TextView) (((RelativeLayout) oneLineLinearLayout
								.getChildAt(k)).getChildAt(0));
						RelativeLayout localSelectedRela = (RelativeLayout) (((RelativeLayout) oneLineLinearLayout
								.getChildAt(k)).getChildAt(1));
						TextView localTextViewSelected = (TextView) localSelectedRela
								.getChildAt(0);
						TextView localTextViewPrice = (TextView) localSelectedRela
								.getChildAt(1);

						int index = (7 - weekOfDay) + (j - 1) * 7 + k + 1;
						localTextView.setText(index + "");
						localTextViewSelected.setText(index + "");
						Calendar tempCalendar3 = (Calendar) tempCalendar2
								.clone();
						tempCalendar3.set(Calendar.DAY_OF_MONTH, index);
						String date = tempCalendar3.get(Calendar.YEAR) + "-"
								+ (tempCalendar3.get(Calendar.MONTH) + 1) + "-"
								+ tempCalendar3.get(Calendar.DAY_OF_MONTH);
						localTextView.setTag(Long.valueOf(tempCalendar3
								.getTimeInMillis()));
						localSelectedRela.setTag(Long.valueOf(tempCalendar3
								.getTimeInMillis()));

						if (numprice <= pricearray.size()) {
							Calendar localCalendar = Calendar.getInstance();
							localCalendar.setTimeInMillis(((Long) localTextView
									.getTag()).longValue());

							for (int l = 0; l < pricearray.size(); l++) {
								JSONObject price = (JSONObject) pricearray
										.get(l);
								if (compareCal(
										DateTimeUtils.getCalendar(price
												.get("date").toString().trim()),
										localCalendar) == 0) {
									localTextView.setVisibility(View.INVISIBLE);
									localSelectedRela
											.setVisibility(View.VISIBLE);
									localTextViewPrice
											.setText("¥"
													+ Math.round(Float
															.parseFloat(price
																	.get("SettlementPrice")
																	.toString()
																	.trim())
															* (precent / 100)
															+ Float.parseFloat(price
																	.get("SettlementPrice")
																	.toString()
																	.trim())));

									numprice++;
									break;
								}
							}
						}

						if (compareCal(tempCalendar3, calendarToday) == -1) {// 小于当天
							localTextView.setTextColor(getResources().getColor(
									R.color.calendar_color_gray));
							localTextView.setEnabled(false);
						}
						if (Constants.HOLIDAYS.get(date) != null) {
							localTextView.setText(Constants.HOLIDAYS.get(date));
							localTextViewSelected.setText(Constants.HOLIDAYS
									.get(date));
							localTextView.setTextSize(
									TypedValue.COMPLEX_UNIT_SP, 14.0f);
							localTextViewSelected.setTextSize(
									TypedValue.COMPLEX_UNIT_SP, 14.0f);
							localTextView.setTextColor(getTextColorGreen());
						}

						if (compareCal(tempCalendar3, calendarToday) == 0) {// 今天
							localTextView.setTextColor(getTextColorRed());
							localTextView.setText("今天");
							localTextViewSelected.setText("今天");
							localTextView.setTextSize(
									TypedValue.COMPLEX_UNIT_SP, 16.0f);
						}

						if (compareCal(tempCalendar3, localCalendarEnd) == 1) {// 大于截止日
							localTextView.setTextColor(getResources().getColor(
									R.color.calendar_color_gray));
							localTextView.setEnabled(false);
						}

					}
				}
				localLinearLayout1.addView(oneLineLinearLayout);
			}

		}
		dialog.dismiss();

	}
}
