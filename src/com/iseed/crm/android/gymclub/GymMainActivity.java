package com.iseed.crm.android.gymclub;

import java.util.Locale;

import com.google.analytics.tracking.android.EasyTracker;
import com.iseed.crm.android.FeedbackActivity;
import com.iseed.crm.android.R;
import com.iseed.crm.android.ScanActivity;
import com.iseed.crm.android.R.id;
import com.iseed.crm.android.R.layout;
import com.iseed.crm.android.R.menu;
import com.iseed.crm.android.R.string;
import com.iseed.crm.android.adapter.GymPagerAdapter;
import com.iseed.crm.android.common.ConnectServer;
import com.iseed.crm.android.login.LoginActivity;
import com.iseed.crm.android.login.UserFunctions;
import com.iseed.crm.android.qrcode.EncoderActivity;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class GymMainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	GymPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gym_main);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new GymPagerAdapter(
				this,
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
		
		if (!ConnectServer.isOnline(this)){
			Toast.makeText(
					this, 
					R.string.msg_no_network_common, 
					Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	public void onStart() {
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);  // Add this method.
	}

	@Override
	public void onStop() {
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);  // Add this method.
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gymclub_main, menu);
		MenuItem loginMenuItem = menu.findItem(R.id.menu_login);
		UserFunctions user = new UserFunctions(this);
        if (user.isLogin()){
            loginMenuItem.setTitle(R.string.menu_logout);
        } else {
            loginMenuItem.setTitle(R.string.menu_login);
        }
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.action_settings:

			return true;
		case R.id.action_scan:
			Intent intent = new Intent(this, ScanActivity.class);
			startActivity(intent);
			return true;
		case R.id.menu_generate:
            Intent encodeIntent = new Intent(this, EncoderActivity.class);
            startActivity(encodeIntent);
            return true;
        case R.id.menu_login:
    		UserFunctions user = new UserFunctions(this);
            if (user.isLogin()){
                user.setLoginState(false);
            } else {
            }
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
            return true;
        case R.id.menu_feedback:
			Intent feedback = new Intent(this, FeedbackActivity.class);
			startActivity(feedback);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

}
