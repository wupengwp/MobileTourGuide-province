package com.jiagu.mobile.tourguide.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.activities.AboutActivity;
import com.jiagu.mobile.tourguide.activities.HelpActivity;
import com.jiagu.mobile.tourguide.activities.InstallActivity;
import com.jiagu.mobile.tourguide.activities.MatterActivity;
import com.jiagu.mobile.tourguide.adapter.MyMenuListAdapter;
import com.jiagu.mobile.tourguide.utils.FileTools;
import com.jiagu.mobile.tourguide.utils.HttpUtil;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.tourguide.utils.UesrInfo;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

//设置中心的Fragment
public class SettingsFragment extends Fragment implements OnItemClickListener {
	private ArrayList<HashMap<String, Object>> data;
	private Intent i;
	private ListView listview;
	private String versionnumber;
	private String apkurl;
	private String newversion;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_settings, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		listview = (ListView) view.findViewById(R.id.settings_center_list);
		getdata();
		listview.setOnItemClickListener(this);
	}

	private void getListViewData(String versionnumber) {
		String[] textData = { "使用帮助", "版本查询", "系统问题", "关于", "分享" };
		int[] imagerData = { R.drawable.icon_shiyong, R.drawable.icon_banben,
				R.drawable.icon_xitong, R.drawable.icon_guanyu,
				R.drawable.icon_fenxiang };
		data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < textData.length; i++) {
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put("text", textData[i]);
			hashMap.put("imager", imagerData[i]);
			hashMap.put("versionnumber", versionnumber);
			data.add(hashMap);
		}
		MyMenuListAdapter adapter = new MyMenuListAdapter(getActivity(), data,
				R.layout.item_srttingandpersonal_listview);
		listview.setAdapter(adapter);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (position) {
		case 0:
			i = new Intent(getActivity(), HelpActivity.class);
			startActivity(i);
			break;
		case 1:// 更新版本

			FileTools
					.writeLog("realtime.txt", "versionnumber:" + versionnumber);

			if (versionnumber.equals("已经是最新版本")) {

				Toast.makeText(getActivity(), "当前已是最新版本", Toast.LENGTH_SHORT)
						.show();
			} else {
				i = new Intent(getActivity(), InstallActivity.class);
				i.putExtra("path", apkurl);
				i.putExtra("newversion", newversion);
				startActivity(i);
			}
			break;
		case 2:
			i = new Intent(getActivity(), MatterActivity.class);// 系统问题
			startActivity(i);
			break;
		case 3:
			i = new Intent(getActivity(), AboutActivity.class);// 关于
			startActivity(i);
			break;
		case 4:
			showShare();
			break;
		}
	}

	// 分享
	private void showShare() {
		ShareSDK.initSDK(getActivity());
		OnekeyShare oks = new OnekeyShare();
		// 分享时Notification的图标和文字
		oks.setNotification(R.drawable.ic_launcher,
				getActivity().getString(R.string.app_name));
		oks.setText("《手机导游平台》安卓版下载地址:www.shoujidaoyou.cn/download/sjdy.apk");
		oks.setTitle("《手机导游平台》");
		oks.setTitleUrl("http://www.shoujidaoyou.cn/download/sjdy.apk");
		oks.setImageUrl("http://www.shoujidaoyou.cn/download/sjdy.png");
		// oks.setFilePath("http://www.shoujidaoyou.cn/download/sjdy.apk");
		// 启动分享GUI
		oks.show(getActivity());
	}

	private void getdata() {
		getListViewData("已经是最新版本");
		RequestParams params = new RequestParams();
		try {
			PackageManager pm = getActivity().getPackageManager();
			final PackageInfo pinfo = pm.getPackageInfo(getActivity().getPackageName(), PackageManager.GET_CONFIGURATIONS);
			params.put("version", pinfo.versionName);
			params.put("os", UesrInfo.version);
			params.put("type", UesrInfo.name);
			params.put("appName", getString(R.string.app_name));
			String path = Path.SERVER_ADRESS + "personalCenter/versionEx.htm";
			HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

				@Override
				public void onFailure(Throwable arg0, String arg1) {
					// getListViewData("已是最新版本");// 获得ListView的数据
					versionnumber = "已经是最新版本";
				}

				@Override
				public void onSuccess(String arg0) {
					super.onSuccess(arg0);
					try {
						JSONObject object = new JSONObject(arg0);
						versionnumber = object.getString("result");
						if (versionnumber.equals("已经是最新版本")) {
							getListViewData("已经是最新版本");
						} else {
							JSONArray jsonArray = object.getJSONArray("records");

							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject jsonObject = jsonArray.getJSONObject(i);
								String text = jsonObject.getString("version");
								apkurl = Path.VERSION_ADRESS
										+ jsonObject.getString("url");
								newversion = text;
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (newversion == null || newversion.length() == 0) {
						// 没有最新版本
						versionnumber = "已经是最新版本";
						getListViewData("已经是最新版本");
					} else if (newversion.compareTo(pinfo.versionName) > 0) {
						getListViewData("最新版本:" + newversion);
					} else {
						versionnumber = "已经是最新版本";
						getListViewData("已经是最新版本");
						newversion = null;
					}
				}
			});
		} catch (NameNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
}
