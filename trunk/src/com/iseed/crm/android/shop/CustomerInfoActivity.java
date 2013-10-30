package com.iseed.crm.android.shop;

import com.iseed.crm.android.R;
import com.iseed.crm.android.common.ConnectServer;
import com.iseed.crm.android.common.Constant;
import com.iseed.crm.android.common.Customer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
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
        
        if (uid != null){
            new GetCustomerInfoTask().execute(uid);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_customer_info, menu);
        return true;
    }
    
    private class GetCustomerInfoTask extends AsyncTask<String, Void, Customer> {
        public Customer customer;
        protected Customer doInBackground(String... uid) {
            customer = connect.getCustomerInfo(uid[0]);
            return customer;
        }

        protected void onPostExecute(Customer result) {
            progressCustomer.setVisibility(View.GONE);
            
            txtCustomerName.setText(customer.displayName);
            txtGender.setText(customer.gender);
            txtReputation.setText(Integer.toString(customer.reputation));
            txtLocation.setText(customer.location);
        }
    }
    
    private class AddInvolvementTask extends AsyncTask<String, Void, Integer> {
        protected void onPreExecute (){
            progressInvolve.setVisibility(View.VISIBLE);
            tgbAddInvolve.setEnabled(false);
        }
        
        protected Integer doInBackground(String... uid) {
            Integer result = (Integer) connect.addInvolvement(uid[0]);
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
            } else {
                // TODO
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

}
