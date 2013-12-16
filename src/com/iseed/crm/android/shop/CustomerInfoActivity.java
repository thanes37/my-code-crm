package com.iseed.crm.android.shop;

import com.google.analytics.tracking.android.EasyTracker;
import com.iseed.crm.android.R;
import com.iseed.crm.android.common.ConnectServer;
import com.iseed.crm.android.common.Constant;
import com.iseed.crm.android.common.Customer;
import com.iseed.crm.android.common.Involvement;
import com.iseed.crm.android.customer.ShopInfoActivity;
import com.iseed.crm.android.login.UserFunctions;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class CustomerInfoActivity extends Activity {
    
    public static final String TAG = "CustomerInfo";
    
    private ConnectServer connect;
    private ProgressBar progressCustomer;
    private ProgressBar progressInvolve;
    
    public ImageView imgvCustomerIcon;
    public TextView txtCustomerName;
    public TextView txtGender;
    public TextView txtReputation;
    public TextView txtLocation;
    public ToggleButton tgbAddInvolve;
    public Button btnToAddPoint;
    
    private String uid;
    
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_info);
        
        Bundle extras = getIntent().getExtras();
        uid = null;
        if(extras !=null) {
            uid = extras.getString(Constant.UID);
        }
        Log.v(TAG, "UID = "+uid);
        
        progressCustomer = (ProgressBar) findViewById(R.id.prgbCustomerInfo);
        progressInvolve = (ProgressBar) findViewById(R.id.prgbAddInvolvement);
        progressInvolve.setVisibility(View.INVISIBLE);
        
        connect = new ConnectServer(this);
        
        imgvCustomerIcon = (ImageView) findViewById(R.id.imgvCustomerIcon);
        txtCustomerName = (TextView) findViewById(R.id.txtCustomerName);
        txtGender = (TextView) findViewById(R.id.txtGender);
        txtReputation = (TextView) findViewById(R.id.txtReputationCustomer);
        txtLocation = (TextView) findViewById(R.id.txtLocation);
        tgbAddInvolve = (ToggleButton) findViewById(R.id.tgbAddInvolvement);
        
        btnToAddPoint = (Button) findViewById(R.id.btnToAddPoint);
        btnToAddPoint.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), AddCustomerPointActivity.class);
        		i.putExtra(Constant.UID,uid);
        		startActivity(i);
			}
		});

        if (ConnectServer.isOnline(this)){
        	if (uid != null){
                new GetCustomerInfoTask().execute(uid);
                new CheckInvolvementTask().execute(uid);
            }
		} else {
			tgbAddInvolve.setEnabled(false);
            btnToAddPoint.setEnabled(false);
			Toast.makeText(
					this, 
					R.string.msg_no_network_common, 
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
        getMenuInflater().inflate(R.menu.activity_customer_info, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_settings:
                
                return true;
            case R.id.menu_add_point:
                Intent intent = new Intent(this, AddCustomerPointActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    private class GetCustomerInfoTask extends AsyncTask<String, Void, Customer> {
        public Customer customer;
        protected void onPreExecute (){
            tgbAddInvolve.setEnabled(false);
            btnToAddPoint.setEnabled(false);
        }
        protected Customer doInBackground(String... uid) {
            customer = connect.getCustomerInfo(uid[0]);
            return customer;
        }

        protected void onPostExecute(Customer result) {
            progressCustomer.setVisibility(View.GONE);
            tgbAddInvolve.setEnabled(true);
            btnToAddPoint.setEnabled(true);
            
            if (connect.resultCode==Constant.SUCCESS){
            	txtCustomerName.setText(customer.displayName);
                txtGender.setText(customer.gender);
                txtReputation.setText(Integer.toString(customer.reputation));
                txtLocation.setText(customer.location);
            } else if(connect.resultCode==Constant.REQUEST_LOGIN){
            	Toast.makeText(
    					CustomerInfoActivity.this, 
    					R.string.msg_err_request_login, 
    					Toast.LENGTH_LONG).show();
            } else if(connect.resultCode==Constant.NOT_FOUND){
            	Toast.makeText(
    					CustomerInfoActivity.this, 
    					R.string.msg_err_not_found, 
    					Toast.LENGTH_LONG).show();
            } else {
            	Toast.makeText(
    					CustomerInfoActivity.this, 
    					R.string.msg_err_error, 
    					Toast.LENGTH_LONG).show();
            }
        }
    }
    
    private class CheckInvolvementTask extends AsyncTask<String, Void, Involvement> {
        public Involvement customer;
        private ConnectServer connect = new ConnectServer(CustomerInfoActivity.this);
        protected void onPreExecute (){
        	progressInvolve.setVisibility(View.VISIBLE);
            tgbAddInvolve.setEnabled(false);
        }
        protected Involvement doInBackground(String... uid) {
            customer = connect.getInvolvementCustomer(uid[0]);
            return customer;
        }

        protected void onPostExecute(Involvement result) {
        	progressInvolve.setVisibility(View.INVISIBLE);
            tgbAddInvolve.setEnabled(true);
            btnToAddPoint.setEnabled(true);
            
            if (connect.resultCode==Constant.SUCCESS){
            	// Customer already involved
            	tgbAddInvolve.setChecked(true);
            	tgbAddInvolve.setEnabled(true);
            } else if (connect.resultCode==Constant.REQUEST_LOGIN){
            	Toast.makeText(
    					CustomerInfoActivity.this, 
    					R.string.msg_err_request_login, 
    					Toast.LENGTH_LONG).show();
            } else if (connect.resultCode==Constant.NEED_INVOLVED){
            	// Customer not involved yet.
            	tgbAddInvolve.setChecked(false);
            } else {
            	Toast.makeText(
    					CustomerInfoActivity.this, 
    					R.string.msg_err_error, 
    					Toast.LENGTH_LONG).show();
            }
        }
    }
    
    private class AddInvolvementTask extends AsyncTask<String, Void, Integer> {
        protected void onPreExecute (){
            progressInvolve.setVisibility(View.VISIBLE);
            tgbAddInvolve.setEnabled(false);
            
        }
        
        protected Integer doInBackground(String... uid) {
            Integer result = (Integer) connect.addMyCustomer(uid[0]);
            return result;
        }

        protected void onPostExecute(Integer result) {
            progressInvolve.setVisibility(View.INVISIBLE);
            if (result == Constant.SUCCESS){
                Log.v(TAG, "Add involvement successful");
                
                tgbAddInvolve.setChecked(true);
                tgbAddInvolve.setEnabled(true);
                
                
                // Create toast for user
                CharSequence text = CustomerInfoActivity.this.getResources().getString(R.string.msg_add_involve_successful);
                Toast toast = Toast.makeText(CustomerInfoActivity.this, text, Toast.LENGTH_SHORT);
                toast.show();
            } else if (connect.resultCode==Constant.REQUEST_LOGIN){
            	Toast.makeText(
    					CustomerInfoActivity.this, 
    					R.string.msg_err_request_login, 
    					Toast.LENGTH_LONG).show();
            } else if (connect.resultCode==Constant.CUSTOMER_INVOLVED){
            	// Customer not involved yet.
            	tgbAddInvolve.setChecked(true);
            } else {
            	Toast.makeText(
    					CustomerInfoActivity.this, 
    					R.string.msg_err_error, 
    					Toast.LENGTH_LONG).show();
            }
        }
    }
    
    private class RemoveInvolvementTask extends AsyncTask<String, Void, Integer> {
        protected void onPreExecute (){
            progressInvolve.setVisibility(View.VISIBLE);
            tgbAddInvolve.setEnabled(false);
        }
        
        protected Integer doInBackground(String... uid) {
            Integer result = (Integer) connect.removeInvolvement(uid[0]);
            return result;
        }

        protected void onPostExecute(Integer result) {
            progressInvolve.setVisibility(View.INVISIBLE);
            if (result == Constant.SUCCESS){
                Log.v(TAG, "Remove involvement successful");
                
                tgbAddInvolve.setChecked(false);
                tgbAddInvolve.setEnabled(true);
                
                // Create toast for user
                CharSequence text = CustomerInfoActivity.this.getResources().getString(R.string.msg_remove_involve_successful);
                Toast toast = Toast.makeText(CustomerInfoActivity.this, text, Toast.LENGTH_SHORT);
                toast.show();
            } else {
                // TODO
            }
        }
    }
    
    /**
     * Method listener for toggle button
     * @param view
     */
    public void onToggleClicked(View view) {
        
        // Is the toggle on AFTER user tap.
        boolean on = ((ToggleButton) view).isChecked();
        
        if (on) {
            Log.v(TAG, "Add involve");
            new AddInvolvementTask().execute(uid);
        } else {
            Log.v(TAG, "Remove involve");
            new RemoveInvolvementTask().execute(uid);
        }
    }
    
    private void updateRelationPan(){
    	UserFunctions user = new UserFunctions(this);
    	String role = user.getRole();
    	if (role.equals(Constant.ROLE_CUSTOMER)){
    		tgbAddInvolve.setVisibility(View.GONE);
    		btnToAddPoint.setVisibility(View.GONE);
    	}
    }

}
