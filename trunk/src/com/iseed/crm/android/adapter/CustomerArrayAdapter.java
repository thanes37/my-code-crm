package com.iseed.crm.android.adapter;

import java.util.List;

import com.iseed.crm.android.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomerArrayAdapter extends ArrayAdapter<CustomerInvolve>{
	
	private final Context context;
	private final List<CustomerInvolve> customerInvolves;

	public CustomerArrayAdapter(Context context,List<CustomerInvolve> customerInvolves) {
		super(context, R.layout.list_customer, customerInvolves);
		
		this.context = context;
		this.customerInvolves = customerInvolves;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		View rowView = inflater.inflate(R.layout.list_customer, parent, false);
		
		ImageView imgvCustomerAvatar = (ImageView) rowView.findViewById(R.id.imgvCustomerAvatar);
		TextView txtCustomerName = (TextView) rowView.findViewById(R.id.txtCustomerName);
		TextView txtCustomerTimestamp = (TextView) rowView.findViewById(R.id.txtCustomerTimestamp);
		TextView txtCustomerPoint = (TextView) rowView.findViewById(R.id.txtCustomerPoint);
		
		// TODO : set imgvCustomerAvatar
		
		txtCustomerName.setText(customerInvolves.get(position).customer.displayName);
		txtCustomerTimestamp.setText(customerInvolves.get(position).involvement.timestamp);
		txtCustomerPoint.setText(String.valueOf(customerInvolves.get(position).involvement.pointSum));
 
		return rowView;
	}
}
