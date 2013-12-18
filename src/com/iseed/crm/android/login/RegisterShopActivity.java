package com.iseed.crm.android.login;

import com.google.analytics.tracking.android.EasyTracker;
import com.iseed.crm.android.R;
import com.iseed.crm.android.R.layout;
import com.iseed.crm.android.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class RegisterShopActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_shop);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register_shop, menu);
		return true;
	}

	@Override
	public void onStart() {
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);  // Add this method.
	}

	@Override
	public void onStop() {
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);  // Add this method.
	}
}
