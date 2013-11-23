
package com.iseed.crm.android.shop;

import com.iseed.crm.android.R;
import com.iseed.crm.android.ScanActivity;
import com.iseed.crm.android.common.ConnectServer;
import com.iseed.crm.android.common.Constant;
import com.iseed.crm.android.login.LoginActivity;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class AddCustomerPointActivity extends Activity implements OnClickListener{
    
    public static final String TAG = "Add Point";
    
    public EditText edtAddPointDetail;
    public EditText edtAddPoint;
    public Button btnScanProduct;
    
    private String uid;
    private ProgressBar progressAddPoint;
    private ConnectServer connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer_point);
        
        Bundle extras = getIntent().getExtras();
        uid = null;
        if(extras !=null) {
            uid = extras.getString(Constant.UID);
        }
        
        progressAddPoint = (ProgressBar) findViewById(R.id.prgbAddPoint);
        progressAddPoint.setVisibility(View.GONE);
        
        connect = new ConnectServer(this);
        
        edtAddPointDetail = (EditText) findViewById(R.id.edtAddPointDetail);
        edtAddPoint = (EditText) findViewById(R.id.edtAddPoint);
        btnScanProduct = (Button) findViewById(R.id.btnScanProduct);
        btnScanProduct.setOnClickListener(this);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_add_customer_point, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_add_point:
            	if(ConnectServer.isOnline(this)){
	                String type = "sell";
	                String detail = edtAddPointDetail.getText().toString();
	                String point = edtAddPoint.getText().toString();
	                if (validate(detail, point)==null){
	                    new AddPointTask().execute(uid, type, detail, point);
	                } else {
	                    // TODO
	                }
            	} else {
            		Toast.makeText(
							this, 
							R.string.msg_no_network_function, 
							Toast.LENGTH_LONG).show();
            	}
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case Constant.SCAN_FOR_PRODUCT_UID:
                if (data == null) {return;}
                String productUid = data.getStringExtra(Constant.UID);
                Log.v(TAG, "ProductUID = "+ productUid);
                // TODO: Continue with product ID
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        Log.v(TAG, "here");
        switch (v.getId()){
            case R.id.btnScanProduct:
                Log.v(TAG, "here");
                Intent intent=new Intent(this, ScanActivity.class);
                startActivityForResult(intent, Constant.SCAN_FOR_PRODUCT_UID);
                break;
            default:
                break;
        }
        
    }
    
    /**
     * @author MrHung
     * Parameters: String uid, String type, String detail, int point
     */
    private class AddPointTask extends AsyncTask<String, Void, Integer> {
        protected void onPreExecute (){
            progressAddPoint.setVisibility(View.VISIBLE);
        }
        
        protected Integer doInBackground(String... params) {
            Integer result = (Integer) connect.addPoint(params[0], params[1],params[2],Integer.valueOf(params[3]));
            return result;
        }

        protected void onPostExecute(Integer result) {
            progressAddPoint.setVisibility(View.GONE);
            if (result == Constant.SUCCESS){
                Log.v(TAG, "Add POINT successful");
                
                // Create toast for user
                CharSequence text = AddCustomerPointActivity.this.getResources().getString(R.string.msg_add_point_successful);
                Toast toast = Toast.makeText(AddCustomerPointActivity.this, text, Toast.LENGTH_SHORT);
                toast.show();
                finish();
            } else {
                // TODO
            }
        }
    }
    
    private String validate(String detail, String point){
    	// TODO
    	
        return null;
    }
}
