package com.iseed.crm.android.adapter;

import java.util.Locale;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.iseed.crm.android.R;
import com.iseed.crm.android.fragment.CustomerHomeFragment;
import com.iseed.crm.android.fragment.ReportCustomersFragment;
import com.iseed.crm.android.fragment.ReportNewCustomerFragment;
import com.iseed.crm.android.fragment.ReportPointSumFragment;
import com.iseed.crm.android.fragment.ShopCustomerListFragment;
import com.iseed.crm.android.fragment.ShopToolsFragment;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class ReportPagerAdapter extends FragmentPagerAdapter {
	
	private Context context;

	public ReportPagerAdapter(Context ctx, FragmentManager fm) {		
		super(fm);
		context = ctx;
	}

	@Override
	public Fragment getItem(int position) {
		// getItem is called to instantiate the fragment for the given page.
		// Return a DummySectionFragment (defined as a static inner class
		// below) with the page number as its lone argument.
		Fragment homeFragment = new ReportCustomersFragment();
		switch (position) {
		case 0:
			return homeFragment;
		case 1:
			return new ReportNewCustomerFragment();
		case 2:
			return new ReportPointSumFragment();
		}
		return homeFragment;
	}

	@Override
	public int getCount() {
		// Show 3 total pages.
		return 3;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		Locale l = Locale.getDefault();
		switch (position) {
		case 0:
			return context.getString(R.string.title_report_section1).toUpperCase(l);
		case 1:
			return context.getString(R.string.title_report_section2).toUpperCase(l);
		case 2:
			return context.getString(R.string.title_report_section3).toUpperCase(l);
		}
		return null;
	}
}