
package com.iseed.crm.android.shop;

import com.iseed.crm.android.R;
import com.iseed.crm.android.common.ConnectServer;
import com.iseed.crm.android.common.Constant;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class AddCustomerPointActivity extends Activity {
    
    public static final String TAG = "Add Point";
    
    public EditText edtAddPointDetail;
    public EditText edtAddPoint;
    
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
                String type = "sell";
                String detail = edtAddPointDetail.getText().toString();
                String point = edtAddPoint.getText().toString();
                if (validate(detail, point)==null){
                    new AddPointTask().execute(uid, type, detail, point);
                } else {
                    // TODO
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
            } else {
                // TODO
            }
            
        }
    }
    
    private String validate(String detail, String point){
        return null;
    }
    
}
