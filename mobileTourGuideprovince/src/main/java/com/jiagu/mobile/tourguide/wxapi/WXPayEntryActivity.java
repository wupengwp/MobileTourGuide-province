package com.jiagu.mobile.tourguide.wxapi;


import java.util.List;
import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.wxzf.Constants;
import com.jiagu.mobile.tourguide.wxzf.MyActionSheetDialog;
import com.jiagu.mobile.tourguide.wxzf.MyActionSheetDialog.OnSheetItemClickListener;
import com.jiagu.mobile.tourguide.wxzf.MyActionSheetDialog.SheetItemColor;
import com.jiagu.mobile.tourguide.wxzf.MyAlertDialog;
import com.jiagu.mobile.tourguide.wxzf.PayActivitywx;
import com.jiagu.mobile.zhifu.AffirmOrderActivity;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.pay_result);
        
    	api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//			AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			builder.setTitle(R.string.app_tip);
//			builder.setMessage(getString(R.string.pay_result_success));
//			builder.show();
			switch (resp.errCode) {
			case 0:
				ShowResultView(getString(R.string.pay_result_success),0);
				break;
			case -1:
				ShowResultView(getString(R.string.pay_result_fail),-1);
				break;
			case -2:
				ShowResultView(getString(R.string.pay_result_cancle),-2);
				break;

			default:
				ShowResultView(getString(R.string.pay_result_fail),1);
				break;
			}					
		}
	}
	public void ShowResultView(String state,final int s) {
		new MyAlertDialog(WXPayEntryActivity.this).builder().setTitle(state)
		.setCancelable(false)
		.setCanceledOnTouchOutside(false)
		.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (s) {
				case 0:
					WXPayEntryActivity.this.finish();
					PayActivitywx.payActivitywx.finish();
					AffirmOrderActivity.affirmOrderActivity.finish();
					break;
				case -1:
					WXPayEntryActivity.this.finish();
					break;
				case -2:
					WXPayEntryActivity.this.finish();
					break;
				default:
					WXPayEntryActivity.this.finish();
					break;
				}
			}
		}).show();
										
	}
}