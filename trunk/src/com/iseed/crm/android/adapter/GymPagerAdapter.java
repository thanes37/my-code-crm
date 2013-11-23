package com.iseed.crm.android.adapter;

import java.util.Locale;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.iseed.crm.android.R;
import com.iseed.crm.android.fragment.CheckinFragment;
import com.iseed.crm.android.fragment.CustomerHomeFragment;
import com.iseed.crm.android.fragment.ShopCustomerListFragment;
import com.iseed.crm.android.fragment.ShopToolsFragment;

public class GymPagerAdapter extends FragmentPagerAdapter{
	private Context context;

	public GymPagerAdapter(Context ctx, FragmentManager fm) {		
		super(fm);
		context = ctx;
	}

	@Override
	public Fragment getItem(int position) {
		// getItem is called to instantiate the fragment for the given page.
		// Return a DummySectionFragment (defined as a static inner class
		// below) with the page number as its lone argument.
		Fragment homeFragment = new CheckinFragment();
		switch (position) {
		case 0:
			return homeFragment;
		case 1:
			return new ShopCustomerListFragment();
		case 2:
			return new ShopToolsFragment();
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
			return context.getString(R.string.title_gym_section1).toUpperCase(l);
		case 1:
			return context.getString(R.string.title_gym_section2).toUpperCase(l);
		case 2:
			return context.getString(R.string.title_gym_section3).toUpperCase(l);
		}
		return null;
	}
}
