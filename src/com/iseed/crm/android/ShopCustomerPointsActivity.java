package com.iseed.crm.android;

import java.util.List;

import com.google.analytics.tracking.android.EasyTracker;
import com.iseed.crm.android.adapter.PointTrackArrayAdapter;
import com.iseed.crm.android.common.ConnectServer;
import com.iseed.crm.android.common.Constant;
import com.iseed.crm.android.common.PointTrack;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ShopCustomerPointsActivity extends ListActivity {
	
public static final String TAG = "Shop-Customer Point";
	
	private ProgressBar progressBar;
	
	private List<PointTrack> trackList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_customer_points);
		
		Bundle extras = getIntent().getExtras();
        int involvementId = 0;
        if(extras !=null) {
        	involvementId = extras.getInt(Constant.INVOLVEMENT_ID);
        }
		
		progressBar = (ProgressBar) findViewById(R.id.prgbPointHistory);
		progressBar.setVisibility(View.INVISIBLE);
	
		if (ConnectServer.isOnline(this)){
			new GetCustomerListTask().execute(involvementId);
		} else {
			Toast.makeText(
					this, 
					R.string.msg_no_network_function, 
					Toast.LENGTH_LONG).show();
				}
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
		getMenuInflater().inflate(R.menu.shop_customer_points, menu);
		return true;
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// do something with the data
		Log.v(TAG, "onClick");
	}
	
	private void loadHosts(final List<PointTrack> newList)
	{
		if (newList == null)
			return;

		trackList = newList;

		if (this.getListAdapter() == null)
		{
			final PointTrackArrayAdapter mAdapter = new PointTrackArrayAdapter(this, trackList);

			this.setListAdapter(mAdapter);
		}
		else
		{
			((PointTrackArrayAdapter)getListAdapter()).notifyDataSetChanged();
		}   
	}
	
	private class GetCustomerListTask extends AsyncTask<Integer, Void, Integer> {
        protected void onPreExecute (){
        	progressBar.setVisibility(View.VISIBLE);
        }
        
        protected Integer doInBackground(Integer... uid) {
        	ConnectServer connect = new ConnectServer(ShopCustomerPointsActivity.this);
        	trackList = connect.getShopCustomerPoints(uid[0]);
            return connect.resultCode;
        }

        protected void onPostExecute(Integer result) {
            progressBar.setVisibility(View.GONE);
            if (result == Constant.SUCCESS){
	            // Adding ArrayList data to ListView values
	    		loadHosts(trackList);
            } else {
            	// Error: Create toast for user
            	// TODO : clarify this for some other error code
                CharSequence text = ShopCustomerPointsActivity.this.getResources().getString(R.string.msg_err_error);
                Toast toast = Toast.makeText(ShopCustomerPointsActivity.this, text, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

	
}
