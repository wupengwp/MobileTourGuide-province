package com.jiagu.mobile.tourguide.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class A extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_a);
		Intent intent = new Intent(this,MainActivity.class);
		startActivity(intent);
		finish();
		
	}
}
