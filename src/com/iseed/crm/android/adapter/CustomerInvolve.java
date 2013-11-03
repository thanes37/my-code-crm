package com.iseed.crm.android.adapter;

import com.iseed.crm.android.common.Customer;
import com.iseed.crm.android.common.Involvement;

public class CustomerInvolve {
	public Customer customer;
	public Involvement involvement;
	
	public CustomerInvolve(){
		customer = new Customer();
		involvement = new Involvement();
	}
}
