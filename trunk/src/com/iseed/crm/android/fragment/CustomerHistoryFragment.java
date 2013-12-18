package com.iseed.crm.android.fragment;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.sax.RootElement;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.iseed.crm.android.R;
import com.iseed.crm.android.adapter.PointListAdapter;
import com.iseed.crm.android.adapter.PointParent;
import com.iseed.crm.android.common.ConnectServer;
import com.iseed.crm.android.common.Constant;
import com.iseed.crm.android.customer.PointHistoryActivity;

public class CustomerHistoryFragment extends Fragment{
	
	private ProgressBar progressBar;
	private TextView txtStateHistory;
	private Context context;
	
	PointListAdapter listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;
	
	private List<PointParent> parents;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_customer_history, container, false);
        
        progressBar = (ProgressBar) rootView.findViewById(R.id.prgbPointHistory);
        progressBar.setVisibility(View.GONE);
        txtStateHistory = (TextView) rootView.findViewById(R.id.txtStateHistory);
        txtStateHistory.setVisibility(View.GONE);
        expListView = (ExpandableListView) rootView.findViewById(R.id.expCustomerHistory);
        
        if (isOnline()){
        	new GetPointHistoryTask().execute();
        } else {
        	// XXX
//        	Toast.makeText(
//					context, 
//					R.string.msg_no_network_function, 
//					Toast.LENGTH_LONG).show();
        }
        return rootView;
    }
	
	@Override
	public void onCreate(Bundle savedInstaceState){
		super.onCreate(savedInstaceState);
		
		context = getActivity();
		
		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
	}
	
	@Override
	public void onStart(){
		super.onStart();
		
	}
	
	@Override
	public void onResume(){
		super.onResume();
		
	}
	
	private void loadHosts(final List<PointParent> newParents)
	{
		if (newParents == null)
			return;

		parents = newParents;
		expListView = (ExpandableListView) getActivity().findViewById(R.id.expCustomerHistory);

		// Check for ExpandableListAdapter object
		if (listAdapter == null)
		{
			//Create ExpandableListAdapter Object
			final PointListAdapter mAdapter = new PointListAdapter(context, parents);

			// Set Adapter to ExpandableList Adapter
			expListView.setAdapter(mAdapter);
		}
		else
		{
			// Refresh ExpandableListView data 
			listAdapter.notifyDataSetChanged();
		}   
	}
	
	private class GetPointHistoryTask extends AsyncTask<Void, Void, Integer> {
        protected void onPreExecute (){
        	progressBar.setVisibility(View.VISIBLE);
        }
        
        protected Integer doInBackground(Void... uid) {
        	ConnectServer connect = new ConnectServer(context);
        	parents = connect.getPointHistory(0);
            return connect.resultCode;
        }

        protected void onPostExecute(Integer result) {
            progressBar.setVisibility(View.GONE);
            if (result == Constant.SUCCESS){
	            // Adding ArrayList data to ExpandableListView values
	    		loadHosts(parents);
	    		
	    		if (parents.isEmpty()){
	    			txtStateHistory.setText(context.getResources().getString(R.string.txt_state_history_none));
	    		} else {
	    			
	    		}
	    		
//	    		// Then expand all the list
//	    		// Check for ExpandableListAdapter object
//	    		if (listAdapter != null)
//	    		{
//	    			txtStateHistory.setVisibility(View.GONE);
//	    			int count = listAdapter.getGroupCount();
//	    			for (int position = 1; position <= count; position++)
//	    				expListView.expandGroup(position -1);
//	    		} else {
//	    			txtStateHistory.setText(context.getResources().getString(R.string.txt_state_history_none));
//	    		}
            } else {
            	// Error: Create toast for user
                CharSequence text = context.getResources().getString(R.string.msg_point_history_failed);
                Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
	
	public boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
}
