package com.jiagu.mobile.tourguide.fragments;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.activities.EditPassActivity;
import com.jiagu.mobile.tourguide.activities.ListenActivity;
import com.jiagu.mobile.tourguide.activities.LoginActivity;
import com.jiagu.mobile.tourguide.activities.NewAssessActivity;
import com.jiagu.mobile.tourguide.activities.StrategyActivity;
import com.jiagu.mobile.tourguide.adapter.MyMenuListAdapter;
import com.jiagu.mobile.tourguide.application.MobileTourGuideApplication;
import com.jiagu.mobile.tourguide.bean.UesrIcon;
import com.jiagu.mobile.tourguide.utils.BitmapY;
import com.jiagu.mobile.tourguide.utils.HttpUtil;
import com.jiagu.mobile.tourguide.utils.ImageTools;
import com.jiagu.mobile.tourguide.utils.MyImageLoader;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.tourguide.utils.UesrInfo;
import com.jiagu.mobile.tourguide.widget.CircleImageView;
import com.jiagu.mobile.zhifu.CollectListViewActivity;
import com.jiagu.mobile.zhifu.IndentListViewActivity;
import com.jiagu.mobile.zhifu.MySite;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

//个人中心的fragment		高磊
@SuppressLint("InflateParams")
public class PersonalFragment extends Fragment implements OnClickListener,
		OnItemClickListener {
	private ArrayList<HashMap<String, Object>> data;
	private Intent i;
	private static final int TAKE_PICTURE = 0;
	private static final int CHOOSE_PICTURE = 1;
	private static final int SCALE = 8;// 照片缩小比例
	private CircleImageView mCircleImageView;
	private Bitmap bitmap;
	private File photoFile;
	public TextView name;
	public static PersonalFragment personalFragment;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		personalFragment = this;
		return inflater.inflate(R.layout.fragment_personal, container, false);
	}

	@SuppressLint("InflateParams")
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ListView listView = (ListView) view
				.findViewById(R.id.personal_center_list);

		// 添加头部试图
		View topView = LayoutInflater.from(getActivity()).inflate(
				R.layout.personal_top, null);
		mCircleImageView = (CircleImageView) topView
				.findViewById(R.id.login_user_logo_iv);
		name = (TextView) topView.findViewById(R.id.text_personal_name);
		listView.addHeaderView(topView);
		// 添加底部试图
		View bottomView = LayoutInflater.from(getActivity()).inflate(
				R.layout.personal_bottom, null);
		Button but = (Button) bottomView.findViewById(R.id.personal_bottom_btn);
		listView.addFooterView(bottomView);

		but.setOnClickListener(this);
		mCircleImageView.setOnClickListener(this);
		getData();
		MyMenuListAdapter adapter = new MyMenuListAdapter(getActivity(), data,
				R.layout.item_srttingandpersonal_listview);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (UesrInfo.getUsername().equals("")) {
			String username = UesrInfo.getUsername();
			if (!username.equals("")) {
				name.setText(UesrInfo.getUsername());
			}
		} else {
			name.setText(UesrInfo.getUsername());
		}
		String userIcon = UesrInfo.getUserIcon();
		if (!userIcon.equals("") && userIcon != null) {
			MyImageLoader.loadImage(Path.IMAGER_ADRESS + userIcon,
					mCircleImageView);
		}
	}
	private void getData() {
		// String[] textData = { "我的收听", "我的评价", "我的攻略", "我的订单", "我的地址", "我的收藏",
		// "修改密码" };
		String[] textData = { "我的收听", "我的评价", "我的攻略", "我的订单", "我的收藏", "修改密码" };
		// int[] imagerData = { R.drawable.icon_shouting,
		// R.drawable.icon_pingjia,
		// R.drawable.icon_gonglue, R.drawable.icon_dingdan,
		// R.drawable.icon_dizi, R.drawable.icon_shoucang,
		// R.drawable.icon_gaimima };
		int[] imagerData = { R.drawable.icon_shouting, R.drawable.icon_pingjia,
				R.drawable.icon_gonglue, R.drawable.icon_dingdan,
				R.drawable.icon_shoucang, R.drawable.icon_gaimima };

		// String[] textData = { "我的收听", "我的评价", "我的攻略", "修改密码" };
		// int[] imagerData = { R.drawable.icon_shouting,
		// R.drawable.icon_pingjia,
		// R.drawable.icon_gonglue, R.drawable.icon_gaimima };
		data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < textData.length; i++) {
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put("text", textData[i]);
			hashMap.put("imager", imagerData[i]);
			data.add(hashMap);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 点击退出登陆时
		case R.id.personal_bottom_btn:// 退出登录
			i = new Intent(getActivity(), LoginActivity.class);
			MobileTourGuideApplication.getUserPreferences().edit().clear()
					.commit();
			startActivity(i);
			getActivity().recreate();
			break;
		case R.id.login_user_logo_iv:
			showPicturePicker(getActivity());
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (position) {
		case 0:

			break;
		case 1:
			i = new Intent(getActivity(), ListenActivity.class);// 我的收听
			startActivity(i);
			break;
		case 2:
			i = new Intent(getActivity(), NewAssessActivity.class);// 我的评价
			startActivity(i);
			break;
		case 3:
			i = new Intent(getActivity(), StrategyActivity.class);// 我的攻略
			i.putExtra("judge", "my");
			startActivity(i);
			break;
		case 4:
			i = new Intent(getActivity(), IndentListViewActivity.class);// 我的订单
			startActivity(i);
			break;
//		case 5:
//			i = new Intent(getActivity(), MySite.class);// 我的地址
//			i.putExtra("scenicid", "");
//			i.putExtra("cargoid", "");
//			i.putExtra("title", "");
//			i.putExtra("price", "");
//			i.putExtra("cargoClassify", "");
//			i.putExtra("isems", "");
//			startActivity(i);
//			break;
		case 5:
			i = new Intent(getActivity(), CollectListViewActivity.class);// 我的收藏
			startActivity(i);
			break;
		case 6:
			i = new Intent(getActivity(), EditPassActivity.class);// 修改密码
			startActivity(i);
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case TAKE_PICTURE:
				// 将保存在本地的图片取出并缩小后显示在界面上
				bitmap = BitmapFactory.decodeFile(Environment
						.getExternalStorageDirectory() + "/image.jpg");
				Bitmap newBitmap = ImageTools.zoomBitmap(bitmap,
						bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);
				// 由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
				bitmap.recycle();
				// 将处理过的图片显示在界面上，并保存到本地
				mCircleImageView.setImageBitmap(BitmapY
						.makeRoundCorner(newBitmap));
				photoFile = ImageTools.savePhotoToSDCard(BitmapY
						.makeRoundCorner(newBitmap), Environment
						.getExternalStorageDirectory().getAbsolutePath(),
						String.valueOf(System.currentTimeMillis()));
				try {
					getPostImage(photoFile);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				break;

			case CHOOSE_PICTURE:
				ContentResolver resolver = getActivity().getContentResolver();
				// 照片的原始资源地址
				Uri originalUri = data.getData();
				try {
					// 使用ContentProvider通过URI获取原始图片
					Bitmap photo = MediaStore.Images.Media.getBitmap(resolver,
							originalUri);
					if (photo != null) {
						// 为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
						Bitmap smallBitmap = ImageTools.zoomBitmap(photo,
								photo.getWidth() / SCALE, photo.getHeight()
										/ SCALE);
						// 释放原始图片占用的内存，防止out of memory异常发生
						photo.recycle();

						Bitmap icon = BitmapY.makeRoundCorner(smallBitmap);
						mCircleImageView.setImageBitmap(icon);
						photoFile = ImageTools.savePhotoToSDCard(BitmapY
								.makeRoundCorner(smallBitmap), Environment
								.getExternalStorageDirectory()
								.getAbsolutePath(), String.valueOf(System
								.currentTimeMillis()));
						try {
							getPostImage(photoFile);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;

			default:
				break;
			}
		}
	}

	/*
	 * 头像上传
	 */
	private void getPostImage(File photoFile) throws Exception {
		String path = Path.SERVER_ADRESS + "personalCenter/updateUserInfo.htm";
		RequestParams params = new RequestParams(); // 绑定参数
		params.put("tourId", UesrInfo.getTourIDid());
		params.put("tourIcon", photoFile);
		params.put("userName", UesrInfo.getUsername());
		HttpUtil.post(path, params, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(Throwable arg0, String arg1) {
				super.onFailure(arg0, arg1);
				Toast.makeText(getActivity(), "上传失败", Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				UesrIcon icon = JSONArray.parseObject(arg0.toString(),
						UesrIcon.class);
				if (icon.getResult().equals("0")) {
					ArrayList<String> list = icon.getRecords();
					if (list.size() >= 1) {
						UesrInfo.userIcon = list.get(0);
						UesrInfo.saveIcon();
						MyImageLoader.loadImage(
								Path.IMAGER_ADRESS + UesrInfo.getUserIcon(),
								mCircleImageView);
					}

				}
			}
		});
	}

	/*
	 * 上传头像
	 */
	public void showPicturePicker(FragmentActivity fragmentActivity) {
		AlertDialog.Builder builder = new AlertDialog.Builder(fragmentActivity);
		builder.setTitle("上传头像");
		builder.setNegativeButton("取消", null);
		builder.setItems(new String[] { "拍照", "相册" },
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case TAKE_PICTURE:
							Intent openCameraIntent = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							Uri imageUri = Uri.fromFile(new File(Environment
									.getExternalStorageDirectory(), "image.jpg"));
							openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
									imageUri);
							startActivityForResult(openCameraIntent,
									TAKE_PICTURE);
							break;

						case CHOOSE_PICTURE:
							Intent openAlbumIntent = new Intent(
									Intent.ACTION_GET_CONTENT);
							openAlbumIntent.setType("image/*");
							startActivityForResult(openAlbumIntent,
									CHOOSE_PICTURE);
							break;
						}
					}
				});
		builder.create().show();
	}
}
