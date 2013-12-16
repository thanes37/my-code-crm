package com.iseed.crm.android;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.analytics.tracking.android.EasyTracker;
import com.iseed.crm.android.common.Constant;
import com.iseed.crm.android.customer.CustomerMainActivity;
import com.iseed.crm.android.gymclub.GymMainActivity;
import com.iseed.crm.android.login.LoginActivity;
import com.iseed.crm.android.login.RegisterActivity;
import com.iseed.crm.android.login.UserFunctions;
import com.iseed.crm.android.qrcode.EncoderActivity;
import com.iseed.crm.android.shop.CustomerInfoActivity;
import com.iseed.crm.android.shop.ShopMainActivity;
import com.iseed.crm.android.shop.ShopReportActivity;
import com.jwetherell.quick_response_code.CaptureActivity;

public class MainActivity extends Activity implements OnClickListener{

	private static final String TAG = "MainActivity";
	String role;
	public static Button btnLogin;
	public static Button btnRegister;
	public static Button btnTest;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnLogin = (Button) findViewById(R.id.btnMainLogin);
		btnLogin.setOnClickListener(this);
		btnRegister = (Button) findViewById(R.id.btnMainRegister);
		btnRegister.setOnClickListener(this);
//		btnTest = (Button) findViewById(R.id.btnTest);
//		btnTest.setOnClickListener(this);

		Log.v(TAG, "Welcome screen");

		// XXX : Delay 2 second
		new Handler().postDelayed(new Runnable() {

			/*
			 * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */

			@Override
			public void run() {
				new PrefetchData().execute();
			}
		}, 500);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_scan:
			Intent intent = new Intent(this, CaptureActivity.class);
			startActivity(intent);
			return true;
		case R.id.menu_generate:
			Intent encodeIntent = new Intent(this, EncoderActivity.class);
			startActivity(encodeIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()){
		case R.id.btnMainLogin:
			intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			break;
		case R.id.btnMainRegister:
			intent = new Intent(this, RegisterActivity.class);
			startActivity(intent);
			break;
			// XXX : Button for testing only
//		case R.id.btnTest:
//			intent = new Intent(this, ShopReportActivity.class);
//			intent.putExtra(Constant.UID, "qwertyui");
//			startActivity(intent);
//			break;
		default :
			break;
		}
	}

	/**
	 * Loading process
	 */
	private class PrefetchData extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();       

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Do anything here.
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			UserFunctions user = new UserFunctions(MainActivity.this);
			Intent i;
			if (user.isLogin()){
				role = user.getRole();
				if (role.equals(Constant.ROLE_SHOP)){
					i = new Intent(MainActivity.this, ShopMainActivity.class);
				} else if (role.equals(Constant.ROLE_CLUB)){
					i = new Intent(MainActivity.this, GymMainActivity.class);
				} else {
					i = new Intent(MainActivity.this, CustomerMainActivity.class);
				}
				startActivity(i);
				// close this activity
				finish();
			} else {
				// Doing nothing, keep in this activity
			}
		}

	}

}
