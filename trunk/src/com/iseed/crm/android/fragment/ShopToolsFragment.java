package com.iseed.crm.android.fragment;

import com.iseed.crm.android.R;
import com.iseed.crm.android.common.Constant;
import com.iseed.crm.android.shop.ShopReportActivity;

import android.content.Context;
import android.content.Intent;
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
				//context.getString(R.string.shop_tool_contact),
				context.getString(R.string.shop_tool_report),
				//context.getString(R.string.shop_tool_product),
				//context.getString(R.string.shop_tool_sale_order),
				//context.getString(R.string.shop_tool_purchase_order),
				//context.getString(R.string.shop_tool_financial)
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
		
		switch (position){
			case 0:
				Intent intent = new Intent(context, ShopReportActivity.class);
                startActivity(intent);
				break;
			case 1:
				
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				break;
			case 5:
				break;
			default:
					
		}
	}

}
