package com.iseed.crm.android.fragment;

import java.util.List;

import com.iseed.crm.android.R;
import com.iseed.crm.android.adapter.CustomerArrayAdapter;
import com.iseed.crm.android.adapter.CustomerInvolve;
import com.iseed.crm.android.common.ConnectServer;
import com.iseed.crm.android.common.Constant;
import com.iseed.crm.android.shop.CustomerInfoActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ShopCustomerListFragment extends ListFragment {

	public static final String TAG = "CustomerListFragment";

	private ProgressBar progressBar;
	private TextView txtStateCustomers;

	private List<CustomerInvolve> customerList;
	private Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_shop_customer_list, container, false);
        
        progressBar = (ProgressBar) rootView.findViewById(R.id.prgbCustomerList);
        txtStateCustomers = (TextView) rootView.findViewById(R.id.txtStateCustomers);
        txtStateCustomers.setVisibility(View.GONE);
        progressBar.setVisibility(View.INVISIBLE);
        
        if (isOnline()){
        	new GetCustomerListTask().execute();
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
	public void onListItemClick(ListView l, View v, int position, long id) {

		// Open customer info activity
		super.onListItemClick(l, v, position, id);
		CustomerInvolve customer = (CustomerInvolve) l.getItemAtPosition(position);
		Intent i = new Intent(context, CustomerInfoActivity.class);
		String uid = customer.customer.uid;
		i.putExtra(Constant.UID,uid);
		startActivity(i);
	}

	private void loadHosts(final List<CustomerInvolve> newList)
	{
		if (newList == null)
			return;

		customerList = newList;

		if (this.getListAdapter() == null)
		{
			final CustomerArrayAdapter mAdapter = new CustomerArrayAdapter(context, customerList);

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
			ConnectServer connect = new ConnectServer(getActivity());
			customerList = connect.getCustomerList(0);
			return connect.resultCode;
		}

		protected void onPostExecute(Integer result) {
			progressBar.setVisibility(View.GONE);
			if (result == Constant.SUCCESS){
				// Adding ArrayList data to ExpandableListView values
				loadHosts(customerList);
				if (customerList.isEmpty()){
					txtStateCustomers.setText(context.getResources().getString(R.string.txt_state_customers_none));
				} else {
					
				}
			} else {
				// Error: Create toast for user
				CharSequence text = context.getResources().getString(R.string.msg_customer_list_fail);
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
