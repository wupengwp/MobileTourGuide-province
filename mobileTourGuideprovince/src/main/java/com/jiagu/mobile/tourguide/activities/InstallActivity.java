package com.jiagu.mobile.tourguide.activities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.activities.bases.TitleDrawerActivity;
import com.jiagu.mobile.tourguide.utils.UesrInfo;
import com.jiagu.mobile.tourguide.utils.DownloadStatistics;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Secure;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
/**
 * @author 2014-12-30 软件更新 高磊
 * 
 */
public class InstallActivity extends TitleDrawerActivity {

	public int fileSize;
	public int downLoadFileSize;
	public String url;
	public String dir;
	TextView textView;
	Button button;
	ProgressBar progressBar;
	private boolean isboolean = false;
	private DownloadStatistics statistics;

	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {// 定义一个Handler，用于处理下载线程与UI间通讯
			if (!Thread.currentThread().isInterrupted()) {
				switch (msg.what) {
				case 0:
					progressBar.setMax(fileSize);
				case 1:
					// 更新下载进度
					progressBar.setProgress(downLoadFileSize);
					int result = downLoadFileSize * 100 / fileSize;
					textView.setText(result + "%");
					break;
				case 2:
					// 下载完成
					Toast.makeText(InstallActivity.this, "下载完成",
							Toast.LENGTH_SHORT).show();
					
					statistics.setStatus("0");//下载完成
					statistics.submitStatistics();
					installApk();
					break;

				case -1:
					// 网络异常
					Toast.makeText(InstallActivity.this, "网络异常",
							Toast.LENGTH_SHORT).show();
					
					statistics.setStatus("1");//下载失败
					statistics.submitStatistics();
					onBackPressed();
					break;
				}
			}
			super.handleMessage(msg);
		}
	};
	private	Runnable runnable = new Runnable() {
		public void run() {
			// 下载文件，参数：第一个URL，第二个存放路径
			try {
				down_file(url, dir);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.zzz);
		progressBar = (ProgressBar) this.findViewById(R.id.button10);
		textView = (TextView) this.findViewById(R.id.button11);
		button = (Button) this.findViewById(R.id.zzz_btn);
		Intent intent = getIntent();
		dir = Environment.getExternalStorageDirectory() + File.separator
				+ "MobileTourGuideprovince" + File.separator + "apk"
				+ File.separator;
		File dirt = new File(dir);
		if (!dirt.exists()) {
			dirt.mkdirs();
		}
		url = intent.getStringExtra("path");
		Thread thread = new Thread(runnable);
		thread.start();
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isboolean = true;
				Intent intent = new Intent(InstallActivity.this,
						MainActivity.class);
				InstallActivity.this.startActivity(intent);
				InstallActivity.this.finish();
				
				statistics.setStatus("2");//下载取消
				
				statistics.submitStatistics();
			}
		});
		
		
		//下载统计信息
		statistics = new DownloadStatistics();
		
		String androidid = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
		statistics.setMobileid(androidid);
		
		String filename = url.substring(url.lastIndexOf("/") + 1);
		statistics.setDownloadfile(filename);
		
		statistics.setMobilebrand(android.os.Build.MODEL);
		
		
		try {
			PackageManager pm = getPackageManager();
			 PackageInfo pinfo = pm.getPackageInfo(getPackageName(), PackageManager.GET_CONFIGURATIONS);
			 statistics.setOldversion(pinfo.packageName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		statistics.setNewversion(intent.getStringExtra("newversion"));
		
		ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netinfo = connectivityManager.getActiveNetworkInfo(); 
		if (netinfo!=null) {
			statistics.setNettype(netinfo.getTypeName());
		}else{
			NetworkInfo netinfo1 = connectivityManager.getActiveNetworkInfo();
			if (netinfo1!=null) {
				statistics.setNettype(netinfo1.getTypeName());
			}else{
				
			}
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		setAppName(UesrInfo.area);
		appName.setText(UesrInfo.area + "手机导游");
	}
	public void down_file(String url, String dir) throws IOException {
		
		// 下载函数
		// filename = url.substring(url.lastIndexOf("/") + 1);
		// 获取文件名
		URL myURL = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) myURL.openConnection();
		int resCode = conn.getResponseCode();
		if (resCode != 200) {
			sendMsg(-1);
			return;
		}
		conn.connect();
		InputStream is = conn.getInputStream();
		this.fileSize = conn.getContentLength();// 根据响应获取文件大小
		if (this.fileSize <= 0)
			throw new RuntimeException("无法获知文件大小 ");
		if (is == null)
			throw new RuntimeException("stream is null");
		FileOutputStream fos = new FileOutputStream(dir
				+ "MobileTourGuideprovince.apk");
		// 把数据存入路径+文件名
		byte buf[] = new byte[1024];
		downLoadFileSize = 0;
		sendMsg(0);
		do {
			// 循环读取
			int numread = is.read(buf);
			if (numread == -1) {
				break;
			}
			fos.write(buf, 0, numread);
			downLoadFileSize += numread;

			sendMsg(1);// 更新进度条
			if (downLoadFileSize == this.fileSize) {
				sendMsg(2);// 通知下载完成
			}
		} while (!isboolean);
		is.close();
		fos.close();
	}

	private void sendMsg(int flag) {
		Message msg = new Message();
		msg.what = flag;
		handler.sendMessage(msg);
	}

	/**
	 * 
	 * Description:执行安装应用程序
	 */
	public void installApk() {
		File apkfile = new File(dir + "MobileTourGuideprovince.apk");
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		this.startActivity(i);
	}
}
