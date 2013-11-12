package com.iseed.crm.android.adapter;

import java.util.Locale;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.iseed.crm.android.R;
import com.iseed.crm.android.customer.CustomerMainActivity.DummySectionFragment;
import com.iseed.crm.android.fragment.CustomerAdsFragment;
import com.iseed.crm.android.fragment.CustomerHistoryFragment;
import com.iseed.crm.android.fragment.CustomerHomeFragment;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
	
	private Context context;

	public SectionsPagerAdapter(Context ctx, FragmentManager fm) {		
		super(fm);
		context = ctx;
	}

	@Override
	public Fragment getItem(int position) {
		// getItem is called to instantiate the fragment for the given page.
		// Return a DummySectionFragment (defined as a static inner class
		// below) with the page number as its lone argument.
		Fragment homeFragment = new CustomerHomeFragment();
		switch (position) {
		case 0:
			Bundle args0= new Bundle();
			args0.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			homeFragment.setArguments(args0);
			return homeFragment;
		case 1:
			return new CustomerAdsFragment();
		case 2:
			return new CustomerHistoryFragment();
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
			return context.getString(R.string.title_section1).toUpperCase(l);
		case 1:
			return context.getString(R.string.title_section2).toUpperCase(l);
		case 2:
			return context.getString(R.string.title_section3).toUpperCase(l);
		}
		return null;
	}
}