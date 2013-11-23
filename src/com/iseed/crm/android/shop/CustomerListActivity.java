package com.iseed.crm.android.shop;

import java.util.List;

import com.iseed.crm.android.R;
import com.iseed.crm.android.R.id;
import com.iseed.crm.android.R.layout;
import com.iseed.crm.android.R.menu;
import com.iseed.crm.android.R.string;
import com.iseed.crm.android.adapter.CustomerArrayAdapter;
import com.iseed.crm.android.adapter.CustomerInvolve;
import com.iseed.crm.android.adapter.PointListAdapter;
import com.iseed.crm.android.adapter.PointParent;
import com.iseed.crm.android.common.ConnectServer;
import com.iseed.crm.android.common.Constant;
import com.iseed.crm.android.customer.PointHistoryActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class CustomerListActivity extends ListActivity {
	
	public static final String TAG = "CustomerList";
	
	private ProgressBar progressBar;
	
	private List<CustomerInvolve> customerList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customer_list);
		
		// XXX Fix this
		progressBar = (ProgressBar) findViewById(R.id.prgbPointHistory);
		progressBar.setVisibility(View.INVISIBLE);

		if (ConnectServer.isOnline(this)){
			new GetCustomerListTask().execute();;
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
		getMenuInflater().inflate(R.menu.customer_list, menu);
		return true;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// do something with the data
		Log.v(TAG, "onClick");
	}
	
	private void loadHosts(final List<CustomerInvolve> newList)
	{
		if (newList == null)
			return;

		customerList = newList;

		if (this.getListAdapter() == null)
		{
			final CustomerArrayAdapter mAdapter = new CustomerArrayAdapter(this, customerList);

			this.setListAdapter(mAdapter);
		}
		else
		{
			((CustomerArrayAdapter)getListAdapter()).notifyDataSetChanged();
		}   
	}
	
	private class GetCustomerListTask extends AsyncTask<Void, Void, Integer> {
        protected void onPreExecute (){
        	progressBar.setVisibility(View.VISIBLE);
        }
        
        protected Integer doInBackground(Void... uid) {
        	ConnectServer connect = new ConnectServer(CustomerListActivity.this);
        	customerList = connect.getCustomerList(0);
            return connect.resultCode;
        }

        protected void onPostExecute(Integer result) {
            progressBar.setVisibility(View.GONE);
            if (result == Constant.SUCCESS){
	            // Adding ArrayList data to ExpandableListView values
	    		loadHosts(customerList);
            } else {
            	// Error: Create toast for user
                CharSequence text = CustomerListActivity.this.getResources().getString(R.string.msg_customer_list_fail);
                Toast toast = Toast.makeText(CustomerListActivity.this, text, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

}
