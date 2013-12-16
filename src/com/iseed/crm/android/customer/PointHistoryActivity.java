package com.iseed.crm.android.customer;

import java.util.HashMap;
import java.util.List;

import com.google.analytics.tracking.android.EasyTracker;
import com.iseed.crm.android.R;
import com.iseed.crm.android.adapter.PointListAdapter;
import com.iseed.crm.android.adapter.PointParent;
import com.iseed.crm.android.common.ConnectServer;
import com.iseed.crm.android.common.Constant;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ExpandableListActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class PointHistoryActivity extends ExpandableListActivity {

	ConnectServer connect;
	private ProgressBar progressBar;
	
	PointListAdapter listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;

	private List<PointParent> parents;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_point_history);
		progressBar = (ProgressBar) findViewById(R.id.prgbPointHistory);

		getExpandableListView().setGroupIndicator(null);
		getExpandableListView().setDividerHeight(1);
		registerForContextMenu(getExpandableListView());
		
		new GetPointHistoryTask().execute();
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

	private void loadHosts(final List<PointParent> newParents)
	{
		if (newParents == null)
			return;

		parents = newParents;

		// Check for ExpandableListAdapter object
		if (this.getExpandableListAdapter() == null)
		{
			//Create ExpandableListAdapter Object
			final PointListAdapter mAdapter = new PointListAdapter(this, parents);

			// Set Adapter to ExpandableList Adapter
			this.setListAdapter(mAdapter);
		}
		else
		{
			// Refresh ExpandableListView data 
			((PointListAdapter)getExpandableListAdapter()).notifyDataSetChanged();
		}   
	}
	
	private class GetPointHistoryTask extends AsyncTask<Void, Void, Integer> {
        protected void onPreExecute (){
        	progressBar.setVisibility(View.VISIBLE);
        }
        
        protected Integer doInBackground(Void... uid) {
        	ConnectServer connect = new ConnectServer(PointHistoryActivity.this);
        	parents = connect.getPointHistory(0);
            return connect.resultCode;
        }

        protected void onPostExecute(Integer result) {
            progressBar.setVisibility(View.GONE);
            if (result == Constant.SUCCESS){
	            // Adding ArrayList data to ExpandableListView values
	    		loadHosts(parents);
	    		
	    		// Then expand all the list
	    		// Check for ExpandableListAdapter object
	    		if (PointHistoryActivity.this.getExpandableListAdapter() != null)
	    		{
	    			int count = PointHistoryActivity.this.getExpandableListAdapter().getGroupCount();
	    			for (int position = 1; position <= count; position++)
	    				PointHistoryActivity.this.getExpandableListView().expandGroup(position -1);
	    		}
            } else {
            	// Error: Create toast for user
                CharSequence text = PointHistoryActivity.this.getResources().getString(R.string.msg_point_history_failed);
                Toast toast = Toast.makeText(PointHistoryActivity.this, text, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.point_history, menu);
		return true;
	}

}
