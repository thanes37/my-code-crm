package com.iseed.crm.android;

import com.google.analytics.tracking.android.EasyTracker;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.webkit.WebView;

public class FeedbackActivity extends Activity {
	
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		
		webView = (WebView) findViewById(R.id.wvFeedback);

		String feedbackForm = "<iframe src=\"https://docs.google.com/forms/d/1-nEw2CCzIuUqK"
				+ "oD6XaulytxXdmt8kH0ohjrDnjpisRk/viewform?embedded=true\" width=\"300\" height=\"500\""
				+ " frameborder=\"0\" marginheight=\"0\" marginwidth=\"0\">Loading...</iframe>";
		
//		String feedbackForm = "<iframe src=\"https://docs.google.com/forms/d/190fN05W9YV2tDBcD7Q8YsDU_RZpZmScgzZEcylh-KEU/" +
//		"viewform?embedded=true\" width=\"300\" height=\"500\" frameborder=\"0\" marginheight=\"0\" " +
//		"marginwidth=\"0\">Loading...</iframe>";
		
		webView.loadDataWithBaseURL(null, feedbackForm, null, "UTF-8", null);

		webView.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onStart() {
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
	}

}
