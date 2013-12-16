package com.iseed.crm.android.customer;

import com.google.analytics.tracking.android.EasyTracker;
import com.iseed.crm.android.R;
import com.iseed.crm.android.common.ConnectServer;
import com.iseed.crm.android.common.Constant;
import com.iseed.crm.android.common.Involvement;
import com.iseed.crm.android.common.Shop;
import com.iseed.crm.android.login.UserFunctions;
import com.iseed.crm.android.shop.CustomerInfoActivity;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ShopInfoActivity extends Activity {

	public static final String TAG = "ShopInfo";
	
	public static boolean isInvolved;
	public static String uid;
	
	private ConnectServer connect;
	private ProgressBar progressBar;

	public ImageView imgvShopIcon;
	public TextView txtShopName;
	public TextView txtAddress;
	public TextView txtPhoneNumber;
	public TextView txtContactEmail;
	public TextView txtWebsite;
	public TextView txtReputation;
	public TextView txtDescription;

	protected Button btnInvolveThisShop;

	public Shop shop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_info);

		Bundle extras = getIntent().getExtras();
		uid = null;
		if (extras != null) {
			uid = extras.getString(Constant.UID);
		}

		progressBar = (ProgressBar) findViewById(R.id.prgbShopInfo);

		connect = new ConnectServer(this);

		imgvShopIcon = (ImageView) findViewById(R.id.imgvShopIcon);
		txtShopName = (TextView) findViewById(R.id.txtShopName);
		txtAddress = (TextView) findViewById(R.id.txtAddress);
		txtPhoneNumber = (TextView) findViewById(R.id.txtPhoneNumber);
		txtContactEmail = (TextView) findViewById(R.id.txtContactEmail);
		txtWebsite = (TextView) findViewById(R.id.txtWebsite);
		txtReputation = (TextView) findViewById(R.id.txtReputation);
		txtDescription = (TextView) findViewById(R.id.txtDescription);

		btnInvolveThisShop = (Button) findViewById(R.id.btnInvolveThisShop);
		btnInvolveThisShop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ( ! isInvolved) {
					new AddInvolvementTask().execute(uid);
				} else {
					new RemoveInvolvementTask().execute(uid);
				}
			}
		});

		if (ConnectServer.isOnline(this)) {
			if (uid != null) {
				new GetShopInfoTask().execute(uid);
			}
		} else {
			Toast.makeText(this, R.string.msg_no_network_function,
					Toast.LENGTH_LONG).show();
		}
		
		updateRelationPan();
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
		getMenuInflater().inflate(R.menu.activity_shop_info, menu);
		return true;
	}

	private class GetShopInfoTask extends AsyncTask<String, Void, Shop> {
		protected Shop doInBackground(String... uid) {
			shop = connect.getShopInfo(uid[0]);
			return shop;
		}

		protected void onPostExecute(Shop result) {
			progressBar.setVisibility(View.GONE);

			if (connect.resultCode == Constant.SUCCESS) {
				txtShopName.setText(shop.displayName);
				txtAddress.setText(shop.address);
				txtPhoneNumber.setText(shop.phoneNumber);
				txtContactEmail.setText(shop.contactEmail);
				txtWebsite.setText(shop.website);
				txtReputation.setText(Integer.toString(shop.reputation));
				txtDescription.setText(shop.description);
			} else if (connect.resultCode == Constant.REQUEST_LOGIN) {
				Toast.makeText(ShopInfoActivity.this,
						R.string.msg_err_request_login, Toast.LENGTH_LONG)
						.show();
			} else {
				Toast.makeText(ShopInfoActivity.this,
						R.string.msg_err_not_found, Toast.LENGTH_LONG).show();
			}
		}
	}

	private class AddInvolvementTask extends AsyncTask<String, Void, Integer> {
		protected void onPreExecute() {
			progressBar.setVisibility(View.VISIBLE);
			btnInvolveThisShop.setEnabled(false);

		}

		protected Integer doInBackground(String... uid) {
			Integer result = (Integer) connect.addMyShop(uid[0]);
			return result;
		}

		protected void onPostExecute(Integer result) {
			progressBar.setVisibility(View.INVISIBLE);

			if (result == Constant.SUCCESS) {
				Log.v(TAG, "Add involvement successful");

				btnInvolveThisShop.setEnabled(true);
				btnInvolveThisShop.setText(ShopInfoActivity.this.getResources()
						.getString(R.string.btn_uninvolve_this_shop));

				// Create toast for user
				CharSequence text = ShopInfoActivity.this.getResources()
						.getString(R.string.msg_involve_shop_successful);
				Toast toast = Toast.makeText(ShopInfoActivity.this, text,
						Toast.LENGTH_SHORT);
				toast.show();
			} else if (connect.resultCode == Constant.REQUEST_LOGIN) {
				Toast.makeText(ShopInfoActivity.this,
						R.string.msg_err_request_login, Toast.LENGTH_LONG)
						.show();
			} else if (connect.resultCode == Constant.CUSTOMER_INVOLVED) {
				// Customer not involved yet.
				btnInvolveThisShop.setEnabled(true);
				btnInvolveThisShop.setText(ShopInfoActivity.this.getResources()
						.getString(R.string.btn_uninvolve_this_shop));
			} else {
				Toast.makeText(ShopInfoActivity.this, R.string.msg_err_error,
						Toast.LENGTH_LONG).show();
			}
		}
	}

	private class CheckInvolvementTask extends
			AsyncTask<String, Void, Involvement> {
		public Involvement customer;
		private ConnectServer connect = new ConnectServer(ShopInfoActivity.this);

		protected void onPreExecute() {
			progressBar.setVisibility(View.VISIBLE);
			btnInvolveThisShop.setEnabled(false);
		}

		protected Involvement doInBackground(String... uid) {
			customer = connect.getInvolvementShop(uid[0]);
			return customer;
		}

		protected void onPostExecute(Involvement result) {
			progressBar.setVisibility(View.INVISIBLE);

			if (connect.resultCode == Constant.SUCCESS) {
				// Customer already involved
				btnInvolveThisShop.setEnabled(true);
				btnInvolveThisShop.setText(ShopInfoActivity.this.getResources()
						.getString(R.string.btn_uninvolve_this_shop));
				isInvolved = true;
			} else if (connect.resultCode == Constant.REQUEST_LOGIN) {
				Toast.makeText(ShopInfoActivity.this,
						R.string.msg_err_request_login, Toast.LENGTH_LONG)
						.show();
			} else if (connect.resultCode == Constant.NEED_INVOLVED) {
				// Customer not involved yet.
				btnInvolveThisShop.setEnabled(true);
				btnInvolveThisShop.setText(ShopInfoActivity.this.getResources()
						.getString(R.string.btn_involve_this_shop));
				isInvolved = false;
			} else {
				Toast.makeText(ShopInfoActivity.this, R.string.msg_err_error,
						Toast.LENGTH_LONG).show();
			}
		}
	}

	// TODO : Remove involvement task. Not implemented yet.
	private class RemoveInvolvementTask extends
			AsyncTask<String, Void, Integer> {
		protected void onPreExecute() {
			progressBar.setVisibility(View.VISIBLE);
			btnInvolveThisShop.setEnabled(false);
		}

		protected Integer doInBackground(String... uid) {
			Integer result = (Integer) connect.addMyShop(uid[0]);
			return result;
		}

		protected void onPostExecute(Integer result) {
			progressBar.setVisibility(View.INVISIBLE);

			if (result == Constant.SUCCESS) {
				Log.v(TAG, "Add involvement successful");

				btnInvolveThisShop.setEnabled(true);
				btnInvolveThisShop.setText(ShopInfoActivity.this.getResources()
						.getString(R.string.btn_involve_this_shop));

				// Create toast for user
				CharSequence text = ShopInfoActivity.this.getResources()
						.getString(R.string.msg_involve_shop_successful);
				Toast toast = Toast.makeText(ShopInfoActivity.this, text,
						Toast.LENGTH_SHORT);
				toast.show();
			} else if (connect.resultCode == Constant.REQUEST_LOGIN) {
				Toast.makeText(ShopInfoActivity.this,
						R.string.msg_err_request_login, Toast.LENGTH_LONG)
						.show();
			} else if (connect.resultCode == Constant.CUSTOMER_INVOLVED) {
				// Customer not involved yet.
				btnInvolveThisShop.setEnabled(true);
				btnInvolveThisShop.setText(ShopInfoActivity.this.getResources()
						.getString(R.string.btn_uninvolve_this_shop));
			} else {
				Toast.makeText(ShopInfoActivity.this, R.string.msg_err_error,
						Toast.LENGTH_LONG).show();
			}
		}
	}

	private void updateRelationPan() {
		UserFunctions user = new UserFunctions(this);
		String role = user.getRole();
		if (role.equals(Constant.ROLE_CUSTOMER)) {
			if (ConnectServer.isOnline(this)) {
				if (uid != null) {
					new CheckInvolvementTask().execute(uid);
				}
			} else {
			}
			
		} else {
			btnInvolveThisShop.setVisibility(View.GONE);
		}
	}
}
