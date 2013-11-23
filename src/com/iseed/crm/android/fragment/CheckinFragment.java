package com.iseed.crm.android.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.iseed.crm.android.R;
import com.iseed.crm.android.ScanForResultActivity;
import com.iseed.crm.android.common.ConnectServer;
import com.iseed.crm.android.common.Constant;
import com.iseed.crm.android.common.Customer;
import com.iseed.crm.android.common.Involvement;
import com.iseed.crm.android.shop.CustomerInfoActivity;

public class CheckinFragment extends Fragment implements OnClickListener{
	private static final String TAG = "CheckingFragment";

	private Context context;

	private ProgressBar progressBar;

	public ImageView imgvCustomerIcon;
	public TextView txtCustomerName;
	public TextView txtGender;
	public TextView txtReputation;
	public TextView txtLocation;
	
	public TextView txtCheckinStatus;
	public Button btnCheckin;
	public Button btnAddMember;
	public Button btnRenew;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View rootView = inflater.inflate(R.layout.fragment_gym_checkin, container, false);

		progressBar = (ProgressBar) rootView.findViewById(R.id.prgbCheckin);
		progressBar.setVisibility(View.INVISIBLE);

		imgvCustomerIcon = (ImageView) rootView.findViewById(R.id.imgvCustomerIcon);
		txtCustomerName = (TextView) rootView.findViewById(R.id.txtCustomerName);
		txtGender = (TextView) rootView.findViewById(R.id.txtGender);
		txtReputation = (TextView) rootView.findViewById(R.id.txtReputationCustomer);
		txtLocation = (TextView) rootView.findViewById(R.id.txtLocation);
		txtCheckinStatus = (TextView) rootView.findViewById(R.id.txtCheckinStatus);

		btnCheckin = (Button) rootView.findViewById(R.id.btnCheckin);
		btnCheckin.setOnClickListener(this);

		btnAddMember = (Button) rootView.findViewById(R.id.btnAddMembership);
		btnAddMember.setOnClickListener(this);
		btnAddMember.setVisibility(View.GONE);
		btnRenew = (Button) rootView.findViewById(R.id.btnRenewMembership);
		btnRenew.setOnClickListener(this);
		btnRenew.setVisibility(View.GONE);

		return rootView;
	}

	@Override
	public void onResume(){
		super.onResume();


	}

	@Override
	public void onPause(){
		super.onPause();


	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
		case R.id.btnCheckin:
			Intent i = new Intent(context, ScanForResultActivity.class);
			startActivityForResult(i, Constant.SCAN_FOR_CUSTOMER_UID);

			break;
		case R.id.btnAddMembership:

			break;
		case R.id.btnRenewMembership:

			break;
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		Log.v(TAG, "On activity Result");
		
		if(requestCode==Constant.SCAN_FOR_CUSTOMER_UID){
			if(null!=data){         
				String uid = data.getStringExtra(Constant.UID); 
				if (ConnectServer.isOnline(context)){
		        	if (uid != null) {
		        		new GetCustomerInfoTask().execute(uid);
		        		new CheckInvolvementTask().execute(uid);
		        	}
		        } else {
		        	Toast.makeText(
							context, 
							R.string.msg_no_network_function, 
							Toast.LENGTH_LONG).show();
		        }
				
			}
		}
	}
	
	private class GetCustomerInfoTask extends AsyncTask<String, Void, Customer> {
        public Customer customer;
        private ConnectServer connect;
        protected void onPreExecute (){
            progressBar.setVisibility(View.VISIBLE);
        }
        protected Customer doInBackground(String... uid) {
            connect = new ConnectServer(context);
			customer = connect.getCustomerInfo(uid[0]);
            return customer;
        }

        protected void onPostExecute(Customer result) {
            progressBar.setVisibility(View.GONE);
            
            if (connect.resultCode==Constant.SUCCESS){
            	txtCustomerName.setText(customer.displayName);
	            txtGender.setText(customer.gender);
	            txtReputation.setText(Integer.toString(customer.reputation));
	            txtLocation.setText(customer.location);
            } else if(connect.resultCode==Constant.REQUEST_LOGIN){
            	Toast.makeText(
    					context, 
    					R.string.msg_err_request_login, 
    					Toast.LENGTH_LONG).show();
            } else if(connect.resultCode==Constant.NOT_FOUND){
            	Toast.makeText(
            			context, 
    					R.string.msg_err_not_found, 
    					Toast.LENGTH_LONG).show();
            } else {
            	Toast.makeText(
            			context, 
    					R.string.msg_err_error, 
    					Toast.LENGTH_LONG).show();
            }
        }
    }
	
	private class CheckInvolvementTask extends AsyncTask<String, Void, Involvement> {
        public Involvement customer;
        private ConnectServer connect = new ConnectServer(context);
        protected void onPreExecute (){
        	progressBar.setVisibility(View.GONE);
        }
        protected Involvement doInBackground(String... uid) {
            customer = connect.getInvolvementCustomer(uid[0]);
            return customer;
        }

        protected void onPostExecute(Involvement result) {
        	progressBar.setVisibility(View.INVISIBLE);
            
            if (connect.resultCode==Constant.SUCCESS){
            	// Customer already involved
            	txtCheckinStatus.setText(R.string.msg_checkin_valid);
            	txtCheckinStatus.setTextColor(context.getResources().getColor(R.color.blue));
            	
            } else if (connect.resultCode==Constant.REQUEST_LOGIN){
            	Toast.makeText(
            			context, 
    					R.string.msg_err_request_login, 
    					Toast.LENGTH_LONG).show();
            } else if (connect.resultCode==Constant.NEED_INVOLVED){
            	// Customer not involved yet.
            	txtCheckinStatus.setText(R.string.msg_checkin_outdate);
            	txtCheckinStatus.setTextColor(context.getResources().getColor(R.color.red));
            	
            	btnAddMember.setVisibility(View.VISIBLE);
            	btnRenew.setVisibility(View.VISIBLE);
            } else {
            	Toast.makeText(
    					context, 
    					R.string.msg_err_error, 
    					Toast.LENGTH_LONG).show();
            }
        }
    }
	
	// TODO : Separate thread to check if user membership valid?.

}
