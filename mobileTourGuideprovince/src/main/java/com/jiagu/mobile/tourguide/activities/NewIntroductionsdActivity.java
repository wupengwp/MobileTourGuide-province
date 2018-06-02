package com.jiagu.mobile.tourguide.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.ab.activity.AbActivity;
import com.ab.global.AbConstant;
import com.ab.util.AbFileUtil;
import com.ab.util.AbStrUtil;
import com.ab.util.AbViewUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.adapter.ImageShowAdapter;
import com.jiagu.mobile.tourguide.bean.PhotoAlbum;
import com.jiagu.mobile.tourguide.utils.HttpUtil;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.tourguide.utils.UesrInfo;
import com.jiagu.mobile.tourguide.utils.Utils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
/**
 * 
 * @author Administrator 发布新的攻略
 */
public class NewIntroductionsdActivity extends AbActivity implements
		OnClickListener {
	private GridView myGrid;
	private ImageShowAdapter mImagePathAdapter = null;
	private ArrayList<String> mPhotoList = new ArrayList<String>();
	private int selectIndex = 0;
	private int camIndex = 0;
	private View mAvatarView = null;
	/* 用来标识请求照相功能的activity */
	private static final int CAMERA_WITH_DATA = 3023;
	/* 用来标识请求gallery的activity */
	private static final int PHOTO_PICKED_WITH_DATA = 3021;
	/* 用来标识请求裁剪图片后的activity */
	private static final int CAMERA_CROP_DATA = 3022;
	/* 拍照的照片存储位置 */
	private File PHOTO_DIR = null;
	// 照相机拍照得到的图片
	private static File mCurrentPhotoFile;
	private String mFileName;
	private static final boolean D = Utils.DEBUG;
	private static final String TAG = "NewIntroductionsdActivity";
	private LayoutInflater mLayoutInflater;
	private Button sumben;
	private List<File> files = new ArrayList<File>();
	private EditText new_introduction_title_tv, new_introduction_content_tv,
			new_date;
	protected View mProgressView;
	private Intent i;
	private String a,title,text,date;
//	private ArrayList<PhotoAlbum> photodata;
	File dbf;

	String types;
	private String idA;
	private String titleA;
	private String textA;
	private String timerA;
	private String typeA;
	private String pathA;
	private String photoId;
	private int indexA;
	private ImageView image;
	
	int degreee;
	private Bitmap mybitmap;
	
	private Handler hander = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 2:
				mImagePathAdapter.notifyAdapter(mPhotoList);
				AbViewUtil.setAbsListViewHeight(myGrid, 3, 20);
				break;
			default:
				break;
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLayoutInflater = LayoutInflater.from(this);
		setContentView(R.layout.activity_new_introduction);
		if (savedInstanceState != null) {
			mPhotoList = savedInstanceState.getStringArrayList("temp");
		}
		initView();
	}

	private void initView() {
		myGrid = (GridView) findViewById(R.id.myGrid);
		sumben = (Button) findViewById(R.id.new_introduction_submit_btn);
		sumben.setOnClickListener(this);
		new_introduction_title_tv = (EditText) findViewById(R.id.new_introduction_title_tv);
		i = getIntent();
		a = i.getStringExtra("a");
		idA = i.getStringExtra("id");
		titleA = i.getStringExtra("title");
		textA = i.getStringExtra("text");
		timerA = i.getStringExtra("timer");
		typeA = i.getStringExtra("typeA");
		pathA = i.getStringExtra("pathA");	
		indexA = i.getIntExtra("index", -1);
		
		if (i.getStringExtra("a").equals("a")) {
			new_introduction_title_tv.setVisibility(View.GONE);
		}
		new_introduction_content_tv = (EditText) findViewById(R.id.new_introduction_content_tv);
		new_date = (EditText) findViewById(R.id.new_date);
		
		if ("xiugai".equals(typeA)) {
			new_introduction_title_tv.setText(titleA);
			new_introduction_content_tv.setText(textA);
			new_date.setText(timerA);
			photoId = i.getStringExtra("photoId");
//			getPhotoAlbum(pathA, photoId);
		}
		
		mProgressView = findViewById(android.R.id.progress);
		String laststring = String.valueOf(R.drawable.cam_photo);
		if (mPhotoList.size() <= 0) {
			mPhotoList.add(laststring);
		} else if (!laststring.equals(mPhotoList.get(mPhotoList.size() - 1))) {
			mPhotoList.add(laststring);
		}
		mImagePathAdapter = new ImageShowAdapter(this, mPhotoList, 180, 180,"");
		myGrid.setAdapter(mImagePathAdapter);
		AbViewUtil.setAbsListViewHeight(myGrid, 3, 20);
		mAvatarView = mLayoutInflater.inflate(R.layout.choose_avatar, null);
		types = getIntent().getStringExtra("type");
		// 初始化图片保存路径
		String photo_dir = AbFileUtil.getFullImageDownPathDir();
		if (AbStrUtil.isEmpty(photo_dir)) {
			showToast("存储卡不存在");
		} else {
			PHOTO_DIR = new File(photo_dir);
		}

		Button albumButton = (Button) mAvatarView
				.findViewById(R.id.choose_album);
		Button camButton = (Button) mAvatarView.findViewById(R.id.choose_cam);
		Button cancelButton = (Button) mAvatarView
				.findViewById(R.id.choose_cancel);
		albumButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				removeDialog(AbConstant.DIALOGBOTTOM);
				// 从相册中去获取
				try {
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
					intent.setType("image/*");
					startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
				} catch (ActivityNotFoundException e) {
					showToast("没有找到照片");
				}
			}

		});

		camButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				removeDialog(AbConstant.DIALOGBOTTOM);
				doPickPhotoAction();
			}

		});

		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				removeDialog(AbConstant.DIALOGBOTTOM);
			}

		});

		myGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selectIndex = position;
				if (selectIndex == mImagePathAdapter.getCount()-1) {
					showDialog(1, mAvatarView);
					camIndex++;
				}else if (mPhotoList.size()-1>position){
					mPhotoList.remove(position);
					mImagePathAdapter.setmImagePaths(mPhotoList);
					mImagePathAdapter.notifyDataSetChanged();
					camIndex--;
					showDialog(1, mAvatarView);
				} else {
					for (int i = 0; i < mImagePathAdapter.getCount(); i++) {
						ImageShowAdapter.ViewHolder mViewHolder = (ImageShowAdapter.ViewHolder) myGrid
								.getChildAt(i).getTag();
						if (mViewHolder != null) {
							mViewHolder.mImageView2.setBackgroundDrawable(null);
						}
					}
				}
			}

		});

	}

	/**
	 * 描述：从照相机获取
	 */
	private void doPickPhotoAction() {
		String status = Environment.getExternalStorageState();
		// 判断是否有SD卡,如果有sd卡存入sd卡在说，没有sd卡直接转换为图片
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			mFileName = System.currentTimeMillis() + ".jpg";
			doTakePhoto();
		} else {
			showToast("没有可用的存储卡");
		}
	}

	/**
	 * 拍照获取图片
	 */
	protected void doTakePhoto() {
		try {
			mCurrentPhotoFile = new File(PHOTO_DIR, mFileName);
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(mCurrentPhotoFile));
			startActivityForResult(intent, CAMERA_WITH_DATA);			
		} catch (Exception e) {
			showToast("未找到系统相机程序");
		}
	}

	protected void showProgress() {
		if (mProgressView != null) {
			mProgressView.setVisibility(View.VISIBLE);
		}
	}

	protected void hideProgress() {
		if (mProgressView != null) {
			mProgressView.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.new_introduction_submit_btn:
			getdataSizePhoto();
			break;
		}
	}

	private void getdataSizePhoto() {
		int i = mPhotoList.size();

		if (i == 1 && !("xiugai".equals(typeA))) {
			showToast("请选择您所要上传的图片！");
		} else if (i > 1 ) {
			for (int j = 0; j < mPhotoList.size(); j++) {
				String imagepath = mPhotoList.get(j);
				files.add(new File(imagepath));
			}
			title = new_introduction_title_tv.getText().toString();// 标题
			text = new_introduction_content_tv.getText().toString();// 内容
			date = new_date.getText().toString();// 时间
			if (a.equals("a")) {
				if (text == null || text.equals("") || date == null
						|| date.equals("")) {
					showToast("内容不能为空！");
				} else {
					try {
						getFilePhoto(files, titleA, text, date, "2", idA);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else {
				if (title == null || title.equals("") || text == null
						|| title.equals("") || date == null || date.equals("")) {
					showToast("内容不能为空！");
				} else {
					try {
						getFilePhoto(files, title, text, date, "1", null);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	// 图片上传
	private void getFilePhoto(List<File> file, String title, String text,
			String date, String type, String strategyId) throws Exception {
		showProgress();
		new_date.setFocusable(false);
		new_introduction_content_tv.setFocusable(false);
		new_introduction_title_tv.setFocusable(false);
		// 地址
		String path = Path.SERVER_ADRESS + "personalCenter/insertStrategy.htm";// 攻略;
		if ("xiugai".equals(typeA)) {
			path = Path.SERVER_ADRESS + "personalCenter/updateTouristNotesById.htm";// 攻略
		}else {
			path = Path.SERVER_ADRESS + "personalCenter/insertStrategy.htm";// 攻略
		}
		
		String username = UesrInfo.getUsername();
		String id = UesrInfo.getTourIDid();

		RequestParams params = new RequestParams();
		
		params.put("index", indexA+"");
		params.put("id", idA);
		params.put("tourId", id);
		params.put("tourname", username);
		params.put("title", title);
		params.put("text", text);
		params.put("date", date);
		params.put("type", type);
		params.put("strategid", strategyId);
		copyBigDataBase();
		if (file.size()==1) {
			params.put("fileSize", 0 + "");	
			for (int i = 0; i < 4; i++) {
				params.put("file" + i, dbf);
			}
		}else {
			params.put("fileSize", (file.size() - 1) + "");		
			for (int i = 0; i < file.size() - 1; i++) {
				params.put("file" + i, file.get(i));
			}
			int size = file.size() - 1;
			for (int i = size; i < 4; i++) {
				params.put("file" + i, file.get(0));
			}
		}		
		
		HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				super.onFailure(arg0, arg1);
				hideProgress();
				files.clear();
				showToast("上传失败");
				new_date.setFocusable(true);
				new_introduction_content_tv.setFocusable(true);
				new_introduction_title_tv.setFocusable(true);
			}

			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				JSONObject json = JSONObject.parseObject(arg0);
				String result = json.getString("result");
				if (result.equals("0")) {
					showToast("上传成功");
					finish();
				} else {
					showToast("网络异常，请稍后再试！");
				}
			}

		});
	}

	/**
	 * 描述：因为调用了Camera和Gally所以要判断他们各自的返回情况, 他们启动时是这样的startActivityForResult
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent mIntent) {
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case PHOTO_PICKED_WITH_DATA:
			if (mIntent == null) {
				Toast.makeText(this, "您已放弃选择", Toast.LENGTH_SHORT).show();
				return;
			}
			Uri uri = mIntent.getData();
			if (uri == null) {
				Toast.makeText(this, "您已放弃选择", Toast.LENGTH_SHORT).show();
				return;
			}
			String currentFilePath = getPath(uri);
			if (mPhotoList.size() > 6) {
				showToast("图片最多可以上传6张!");
				return;
			}
			setPicter(currentFilePath);
			
//			Uri uri = mIntent.getData();
//			String currentFilePath = getPath(uri);
//			if (!AbStrUtil.isEmpty(currentFilePath)) {
//				Intent intent1 = new Intent(this, CropImageActivity.class);
//				intent1.putExtra("PATH", currentFilePath);
//				startActivityForResult(intent1, CAMERA_CROP_DATA);
//			} else {
//				showToast("未在存储卡中找到这个文件");
//			}
			break;
		case CAMERA_WITH_DATA:
			String currentFilePath2 = mCurrentPhotoFile.getPath();
			if (mPhotoList.size() > 6) {
				showToast("图片最多可以上传6张!");
				return;
			}
			setPicter(currentFilePath2);
//			String currentFilePath2 = mCurrentPhotoFile.getPath();
//			Intent intent2 = new Intent(this, CropImageActivity.class);
//			intent2.putExtra("PATH", currentFilePath2);
//			startActivityForResult(intent2, CAMERA_CROP_DATA);
			break;
		case CAMERA_CROP_DATA:
//			String path = mIntent.getStringExtra("PATH");
//			mPhotoList.add(mPhotoList.size()-1, path);
//			// if (D)
//			// if (types == null || types.equals("")) {
//			if (mImagePathAdapter.getCount() > 4) {
//				showToast("图片最多可以上传4张!");
//				return;
//			}
//			mImagePathAdapter.notifyAdapter(mPhotoList);
////			mImagePathAdapter.addItem(mImagePathAdapter.getCount() - 1, path);
//			AbViewUtil.setAbsListViewHeight(myGrid, 3, 20);
			break;
		}
	}
	private void setPicter(String PhotoName) {
		if (PhotoName != null) {

			// image.setImageBitmap(BitmapFactory.decodeFile(PhotoName));
			// 有的系统返回的图片是旋转了，有的没有旋转，所以处理
			degreee = readBitmapDegree(PhotoName);
			mybitmap = createBitmap(PhotoName);

			if (mybitmap != null) {

				libJpeg(mybitmap);

			} else {
				PhotoName = null;
				Toast.makeText(this, "选择图片错误", Toast.LENGTH_LONG).show();
			}
		} else {
			PhotoName = null;
			Toast.makeText(this, "选择图片文件不正确", Toast.LENGTH_LONG).show();
		}

	}
	// 读取图像的旋转度
	private int readBitmapDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}
	/**
	 * 创建图片进行修改防止内存溢出
	 * 
	 * @param path
	 * @return
	 */
	private Bitmap createBitmap(String path) {
		if (path == null) {
			return null;
		}
		InputStream is;
		Bitmap bitmap = null;
		try {
			is = new FileInputStream(path);
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inTempStorage = new byte[100 * 1024];
			opts.inPreferredConfig = Bitmap.Config.ARGB_4444;
			opts.inPurgeable = true;
			opts.inSampleSize = 2;
			opts.inInputShareable = true;
			bitmap = BitmapFactory.decodeStream(is, null, opts);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bitmap;
	}
	private void libJpeg(final Bitmap bitmap) {
		new Thread(new Runnable() {
			public void run() {
				try {
					int quality = 30;
					File dirFile = getExternalCacheDir();
					if (!dirFile.exists()) {
						dirFile.mkdirs();
					}
					File originalFile = new File(dirFile,
							System.currentTimeMillis() + ".png");
					mPhotoList.add(mPhotoList.size() - 1,
							originalFile.toString());

					FileOutputStream fileOutputStream = new FileOutputStream(
							originalFile);
					bitmap.compress(CompressFormat.JPEG, quality,
							fileOutputStream);

					Message message = new Message();
					message.what = 2;
					hander.sendMessage(message);// 通知主线程刷新界面

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}).start();
	}
	
	/**
	 * 从相册得到的url转换为SD卡中图片路径
	 */
	public String getPath(Uri uri) {
		if (AbStrUtil.isEmpty(uri.getAuthority())) {
			return null;
		}
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String path = cursor.getString(column_index);
		return path;
	}
	boolean tag = true;

	public void onBackPressed() {
		title = new_introduction_title_tv.getText().toString();// 标题
		text = new_introduction_content_tv.getText().toString();// 内容
		date = new_date.getText().toString();// 时间
		if (title.equals("")&&text.equals("")&&date.equals("")&&mImagePathAdapter.getCount()==0) {
			finish();
		}else if (tag) {
			tag = false;
			Toast.makeText(NewIntroductionsdActivity.this, "如果您放弃此次编辑,请再按返回键", Toast.LENGTH_SHORT)
					.show();
			new Thread() {
				public void run() {
					try {
						Thread.sleep(3000);
						tag = true;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				};
			}.start();
		} else {
			finish();
		}
	}
//	//获取图片
//	private void getPhotoAlbum(String path, String photoId) {
//		RequestParams params = new RequestParams(); // 绑定参数
//		if (photoId != null&&!photoId.equals("")) {
//			params.put("photoId", photoId);
//		}
//		HttpUtil.post(path, params, new AsyncHttpResponseHandler() {
//			@Override
//			public void onFailure(Throwable arg0, String arg1) {
//				super.onFailure(arg0, arg1);
//			}
//
//			@Override
//			public void onSuccess(String response) {
//				super.onSuccess(response);
//				if (response.equals("") || response == null) {
//					Toast.makeText(NewIntroductionsdActivity.this, "服务器在打盹11",
//							Toast.LENGTH_SHORT).show();
//				} else {
//					JSONObject object = JSONObject.parseObject(response);
//					JSONArray top = object.getJSONArray("records");
//					String result = object.getString("result");
//					if (result.equals("0")) {
//						photodata = (ArrayList<PhotoAlbum>) JSONArray.parseArray(
//								top.toJSONString(), PhotoAlbum.class);
//						if (photodata.size()>0) {
//							mPhotoList.clear();
//							for (int i = 0; i < photodata.size(); i++) {
//								mPhotoList.add(photodata.get(i).getImgaeurl());
//							}
//							mPhotoList.add(String.valueOf(R.drawable.cam_photo));
//							mImagePathAdapter = new ImageShowAdapter(NewIntroductionsdActivity.this, mPhotoList, 180, 180,"xiugai");
//							myGrid.setAdapter(mImagePathAdapter);
//						}
//					} else {
//						Toast.makeText(NewIntroductionsdActivity.this, "服务器在打盹",
//								Toast.LENGTH_SHORT).show();
//					}
//				}
//			}
//		});
//	}
	
	private static String DB_PATH = "/mnt/sdcard/";
	private static String DB_NAME = "cam_photo.png";
	private static String ASSETS_NAME = "cam_photo.png";// 这个就是assets下的一个文件

	/**
	 * 复制assets下的文件时用这个方法
	 * 
	 * @throws IOException
	 */
	private void copyBigDataBase() throws IOException {
		InputStream myInput;
		File dir = new File(DB_PATH);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		 dbf = new File(DB_PATH + DB_NAME);
		if (dbf.exists()) {
			dbf.delete();
		}
		String outFileName = DB_PATH + DB_NAME;
		OutputStream myOutput = new FileOutputStream(outFileName);
		myInput = this.getAssets().open(ASSETS_NAME);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		myOutput.flush();
		myInput.close();
		myOutput.close();
}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putStringArrayList("temp", mPhotoList);
	}

}
