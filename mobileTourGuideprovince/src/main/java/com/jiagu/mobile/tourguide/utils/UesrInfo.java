package com.jiagu.mobile.tourguide.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.jiagu.mobile.tourguide.activities.LoginActivity;
import com.jiagu.mobile.tourguide.activities.bases.BaseActivity;
import com.jiagu.mobile.tourguide.application.MobileTourGuideApplication;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
/**
 * @author Administrator 贮存所有的静态常量
 */
public class UesrInfo {
	// 版本
	public static final String version = "1";
	// 软件名
	public static final String name = "3";
	public static String abc = "";
	// 地区切换的默认值
	public static String area = "北京市";
	// 地区切换默认值的类型参数
	// 省:1 市:2 区:3 景区:4
	public static String areaType = "2";
	// photoId
//	public static String photoId = "ar12240";//西安
	public static String photoId = "ar111111";//北京
//	当前位置经纬度和地名
	public static Double myX=0.0;
	public static Double myY=0.0;
	public static String at = "";
	
	// 定制版级别参数
	// 省:1 市:2 区:3 景区:4
	public static String sceneId = "S0004";
	// 定制版级别的Voiceurl
	public static String voiceurl = "app/scenic/S0004/scenic/cuihuashan.wav";
	public static String x = "116.403906";
	public static String y = "39.915175";

	/** 用户登录id */
	public static String tourIDid = "";
	public static final String User_tourIDid = "tourIDid";
	/** 用户登录成功的状态 */
	public static String userphone = "";
	public static final String User_phone = "userphone";
	/** 用户登录名称 */
	public static String username = "";
	public static final String USER_username = "username";
	/** 用户登录密码 */
	public static String userpass = "";
	public static final String USER_userpass = "loginPwd";
	public static final String scenicID = "S0001";
	/** 用户头像 */
	public static String userIcon = "";
	public static final String User_Icon = "userIcon";
	// 验证码
	public static String code = "";
	public static final String USER_code = "code";
	/** 程序缓存信息文件名称 */
	public final static String m_sharedPreferences = "daoyou";
	// 跳转保存值
	public static int tiaozhaun = 1;

	// 保存登陆信息
	public static void save() {
		SharedPreferences.Editor editor = MobileTourGuideApplication
				.getUserPreferences().edit();
		editor.putString(User_tourIDid, tourIDid);
		editor.putString(User_phone, userphone);
		editor.putString(USER_username, username);
		editor.putString(USER_userpass, userpass);
		editor.putString(User_Icon, userIcon);
		editor.commit();
	}

	// 用户id
	public static String getTourIDid() {
		tourIDid = MobileTourGuideApplication.getUserPreferences().getString(
				User_tourIDid, "");
		return tourIDid;
	}

	// 用户电话
	public static String getUserphone() {
		userphone = MobileTourGuideApplication.getUserPreferences().getString(
				User_phone, "");
		return userphone;
	}

	// 用户名
	public static String getUsername() {
		username = MobileTourGuideApplication.getUserPreferences().getString(
				USER_username, "");
		return username;
	}

	// 用户头像
	public static String getUserIcon() {
		userIcon = MobileTourGuideApplication.getUserPreferences().getString(
				User_Icon, "");
		return userIcon;
	}

	// 保存，替换头像地址
	public static void saveIcon() {
		SharedPreferences.Editor editor = MobileTourGuideApplication
				.getUserPreferences().edit();
		editor.putString(User_Icon, userIcon);
		editor.commit();
	}

	// 加入我的收听,收藏的上传接口
	public static void HttpListen(final Context context, String id,
			String tourIDid, String scenicid, String characteristicId,
			String attractId, String type) {
		RequestParams params = new RequestParams();
		params.put("id", id);
		params.put("userId", tourIDid);
		params.put("scenicId", scenicid);
		params.put("characteristicId", characteristicId);
		params.put("attractId", attractId);
		params.put("type", type);
		String path = Path.SERVER_ADRESS + "scenic/mylisten.htm";
		HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				Toast.makeText(context, "请检查您的网络", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				if (UesrInfo.getTourIDid().equals("")) {
					
				}else{
					JSONObject object1 = JSONObject.parseObject(response);
					String result = object1.getString("result");
					if (result != null && result.equals("0")) {
						Toast.makeText(context, "已加入我的收听", Toast.LENGTH_SHORT)
								.show();
					} else if (result != null && result.equals("31")) {
						
					} else {

					}
				}
			}
		});
	}

	// 上传评价
	public static void getText(final Context context, String tourId,
			String tourName, String appraiseId, String aprClass, String note,
			String title, final EditText text) {
		if (UesrInfo.getTourIDid() == null || UesrInfo.getTourIDid().equals("")) {
//			AlertDialog.Builder builder = new Builder(context);
//			builder.setMessage("您准备好了吗？");
//			builder.setTitle("登录");
//			builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
//
//						@Override
//						public void onClick(DialogInterface dialog,int which) {
							Intent i = new Intent();
							i.setClass(context, LoginActivity.class);
							context.startActivity(i);
//						}
//					});
//			builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
//
//						@Override
//						public void onClick(DialogInterface dialog,
//								int which) {
//							dialog.dismiss();
//						}
//					});
//			builder.create().show();
		} else {
			BaseActivity.baseActivity.showProgress();
			// 评价分类景区0景点1团购2景区活动3民俗文化4，历史沿革5，著名人物6，本地特产7攻略8
			RequestParams params = new RequestParams();
			params.put("tourId", tourId);// 用户ID
			params.put("tourName", tourName);// 用户名
			params.put("appraiseId", appraiseId);// 物品id
			params.put("aprClass", aprClass);// 分类
			params.put("note", note);// 内容
			params.put("title", title);// 物品名
			String path = Path.SERVER_ADRESS + "scenic/insertAppr.htm";
			HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

				@Override
				public void onFailure(Throwable arg0, String arg1) {
					BaseActivity.baseActivity.hideProgress();
					Toast.makeText(context, "请检查您的网络", Toast.LENGTH_SHORT)
							.show();
				}

				@Override
				public void onSuccess(String response) {
					super.onSuccess(response);
					BaseActivity.baseActivity.hideProgress();
					JSONObject object = JSONObject.parseObject(response);
					String result = object.getString("result");
					if (result.equals("0")) {
						Toast.makeText(context, "评论成功", Toast.LENGTH_SHORT)
								.show();
						text.setText("");
					} else {
						Toast.makeText(context, "服务器在打盹", Toast.LENGTH_SHORT)
								.show();
					}
				}
			});
		}
	}

	// 删除
	public static void setRemove(final Context context, String path, String id,
			String type) {
		RequestParams params = new RequestParams();
		params.put("id", id);
		params.put("type", type);
		HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				Toast.makeText(context, "请检查您的网络", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				JSONObject object = JSONObject.parseObject(response);
				String result = object.getString("result");
				if (result.equals("0")) {
					Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
				} else {

				}
			}
		});
	}
}
