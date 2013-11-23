package com.iseed.crm.android.adapter;

import java.util.List;

import com.iseed.crm.android.R;
import com.iseed.crm.android.ShopCustomerPointsActivity;
import com.iseed.crm.android.common.Constant;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomerArrayAdapter extends ArrayAdapter<CustomerInvolve>{
	
	protected static final String TAG = "CustomerArrayAdapter";
	private final Context context;
	private final List<CustomerInvolve> customerInvolves;

	public CustomerArrayAdapter(Context context,List<CustomerInvolve> customerInvolves) {
		super(context, R.layout.list_customer, customerInvolves);
		
		this.context = context;
		this.customerInvolves = customerInvolves;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		View rowView = inflater.inflate(R.layout.list_customer, parent, false);
		
		ImageView imgvCustomerAvatar = (ImageView) rowView.findViewById(R.id.imgvCustomerAvatar);
		TextView txtCustomerName = (TextView) rowView.findViewById(R.id.txtCustomerName);
		TextView txtCustomerTimestamp = (TextView) rowView.findViewById(R.id.txtCustomerTimestamp);
		TextView txtCustomerPoint = (TextView) rowView.findViewById(R.id.txtCustomerPoint);
		

		ImageView imgvPointDetail = (ImageView) rowView.findViewById(R.id.imgvPointDetail);
		imgvPointDetail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.v(TAG, "onClick Point");
				int involvementId = customerInvolves.get(position).involvement.id;
				Intent i = new Intent(context, ShopCustomerPointsActivity.class);
        		i.putExtra(Constant.INVOLVEMENT_ID,involvementId);
        		context.startActivity(i);
			}
		});
		
		// TODO : set imgvCustomerAvatar
		
		txtCustomerName.setText(customerInvolves.get(position).customer.displayName);
		txtCustomerTimestamp.setText(customerInvolves.get(position).involvement.timestamp);
		txtCustomerPoint.setText(String.valueOf(customerInvolves.get(position).involvement.pointSum));
 
		return rowView;
	}
	
}
