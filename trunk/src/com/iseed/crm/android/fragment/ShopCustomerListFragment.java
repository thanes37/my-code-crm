package com.iseed.crm.android.fragment;

import java.util.List;

import com.iseed.crm.android.R;
import com.iseed.crm.android.adapter.CustomerArrayAdapter;
import com.iseed.crm.android.adapter.CustomerInvolve;
import com.iseed.crm.android.common.ConnectServer;
import com.iseed.crm.android.common.Constant;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ShopCustomerListFragment extends ListFragment {

	public static final String TAG = "CustomerListFragment";

	private ProgressBar progressBar;

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
        
        new GetCustomerListTask().execute();
        
        return rootView;
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
			} else {
				// Error: Create toast for user
				CharSequence text = context.getResources().getString(R.string.msg_customer_list_fail);
				Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
				toast.show();
			}
		}
	}

}