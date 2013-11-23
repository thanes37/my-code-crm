
package com.iseed.crm.android.customer;

import com.iseed.crm.android.R;
import com.iseed.crm.android.common.ConnectServer;
import com.iseed.crm.android.common.Constant;
import com.iseed.crm.android.common.Shop;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ShopInfoActivity extends Activity {
    
    private ConnectServer connect;
    private ProgressBar mProgress;
    
    public ImageView imgvShopIcon;
    public TextView txtShopName;
    public TextView txtAddress;
    public TextView txtPhoneNumber;
    public TextView txtContactEmail;
    public TextView txtWebsite;
    public TextView txtReputation;
    public TextView txtDescription;
    
    
    public Shop shop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_info);
        
        Bundle extras = getIntent().getExtras();
        String uid = null;
        if(extras !=null) {
            uid = extras.getString(Constant.UID);
        }
        
        mProgress = (ProgressBar) findViewById(R.id.prgbShopInfo);
        
        connect = new ConnectServer(this);
        
        imgvShopIcon = (ImageView) findViewById(R.id.imgvShopIcon);
        txtShopName = (TextView) findViewById(R.id.txtShopName);
        txtAddress = (TextView) findViewById(R.id.txtAddress);
        txtPhoneNumber = (TextView) findViewById(R.id.txtPhoneNumber);
        txtContactEmail = (TextView) findViewById(R.id.txtContactEmail);
        txtWebsite = (TextView) findViewById(R.id.txtWebsite);
        txtReputation = (TextView) findViewById(R.id.txtReputation);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        
        if (isOnline()){
        	if (uid != null) new GetShopInfoTask().execute(uid);
        } else {
        	Toast.makeText(
					this, 
					R.string.msg_no_network_function, 
					Toast.LENGTH_LONG).show();
        }
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
            mProgress.setVisibility(View.GONE);
            
            if (connect.resultCode == Constant.SUCCESS){
	            txtShopName.setText(shop.displayName);
	            txtAddress.setText(shop.address);
	            txtPhoneNumber.setText(shop.phoneNumber);
	            txtContactEmail.setText(shop.contactEmail);
	            txtWebsite.setText(shop.website);
	            txtReputation.setText(Integer.toString(shop.reputation));
	            txtDescription.setText(shop.description);
            } else if (connect.resultCode == Constant.REQUEST_LOGIN){
            	Toast.makeText(
    					ShopInfoActivity.this, 
    					R.string.msg_err_request_login, 
    					Toast.LENGTH_LONG).show();
            } else {
            	Toast.makeText(
    					ShopInfoActivity.this, 
    					R.string.msg_err_not_found, 
    					Toast.LENGTH_LONG).show();
            }
        }
    }

    public boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
}
