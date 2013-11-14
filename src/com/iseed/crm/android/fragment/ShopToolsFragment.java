package com.iseed.crm.android.fragment;

import com.iseed.crm.android.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ShopToolsFragment extends ListFragment{

	Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		context = getActivity();
		String[] tools = {
				context.getString(R.string.shop_tool_contact),
				context.getString(R.string.shop_tool_report),
				context.getString(R.string.shop_tool_product),
				context.getString(R.string.shop_tool_sale_order),
				context.getString(R.string.shop_tool_purchase_order),
				context.getString(R.string.shop_tool_financial)
		};
		
		ListAdapter myListAdapter = new ArrayAdapter<String>(
				context,
				android.R.layout.simple_list_item_1,
				tools);
		setListAdapter(myListAdapter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_shop_tools, container, false);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Toast.makeText(
				context, 
				getListView().getItemAtPosition(position).toString(), 
				Toast.LENGTH_LONG).show();
	}

}
