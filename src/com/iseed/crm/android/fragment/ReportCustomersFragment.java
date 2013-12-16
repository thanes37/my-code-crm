package com.iseed.crm.android.fragment;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.iseed.crm.android.R;
import com.iseed.crm.android.common.AgeGroup;
import com.iseed.crm.android.common.ConnectServer;
import com.iseed.crm.android.common.Constant;
import com.iseed.crm.android.common.Location;
import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewDataInterface;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jjoe64.graphview.ValueDependentColor;

public class ReportCustomersFragment extends Fragment{
	private Context context;
	private ProgressBar progressBar;
	protected List<AgeGroup> ageStatList;
	protected AgeGroup genderStat;
	protected List<Location> locationStatsList;
	protected int dayBack;
	private LinearLayout layoutAge;
	private LinearLayout layoutGender;
	private LinearLayout layoutLocation;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// TODO : check if reload. Not loading data again when change orientation.
		
		
		context = getActivity();
		dayBack = Constant.DEFAULT_DAY_BACK;
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_report_customers, container, false);
		
		progressBar = (ProgressBar) rootView.findViewById(R.id.prgbReportNewCustomer);
		progressBar.setVisibility(View.INVISIBLE);
		layoutAge = (LinearLayout)  rootView.findViewById(R.id.graphReportAge);
		layoutGender = (LinearLayout)  rootView.findViewById(R.id.graphReportGender);
		
		layoutLocation = (LinearLayout) rootView.findViewById(R.id.layoutReportLocation);
		
		if (ConnectServer.isOnline(context)){
        	new GetReportAgeTask().execute();
        	new GetReportGenderTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        	new GetReportLocationTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
        	// XXX
//        	Toast.makeText(
//					context, 
//					R.string.msg_no_network_function, 
//					Toast.LENGTH_LONG).show();
        }
		
		return rootView;
	}

	@Override
	public void onResume(){
		super.onResume();
	}

	@Override
	public void onPause(){
		super.onPause();
	}
	
	private class GetReportAgeTask extends AsyncTask<Void, Void, Integer> {
		
        protected void onPreExecute (){
        	progressBar.setVisibility(View.VISIBLE);
        }
        
        protected Integer doInBackground(Void... uid) {
        	ConnectServer connect = new ConnectServer(context);
        	ageStatList = connect.getAgeReport();
            return connect.resultCode;
        }

        protected void onPostExecute(Integer result) {
            progressBar.setVisibility(View.GONE);
            
            if (result == Constant.SUCCESS){
	            drawGraphAge();
            } else {
            	// Error: Create toast for user
                CharSequence text = context.getResources().getString(R.string.msg_err_error);
                Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
	
	private class GetReportGenderTask extends AsyncTask<Void, Void, Integer> {
		
        protected void onPreExecute (){
        	progressBar.setVisibility(View.VISIBLE);
        }
        
        protected Integer doInBackground(Void... uid) {
        	ConnectServer connect = new ConnectServer(context);
        	genderStat = connect.getGenderReport();
            return connect.resultCode;
        }

        protected void onPostExecute(Integer result) {
            progressBar.setVisibility(View.GONE);
            
            if (result == Constant.SUCCESS){
	            drawGraphGender();
            } else {
            	// Error: Create toast for user
                CharSequence text = context.getResources().getString(R.string.msg_err_error);
                Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
	
	private class GetReportLocationTask extends AsyncTask<Void, Void, Integer> {
		
        protected void onPreExecute (){
        	progressBar.setVisibility(View.VISIBLE);
        }
        
        protected Integer doInBackground(Void... uid) {
        	ConnectServer connect = new ConnectServer(context);
        	locationStatsList = connect.getLocationReport();
            return connect.resultCode;
        }

        protected void onPostExecute(Integer result) {
            progressBar.setVisibility(View.GONE);
            
            if (result == Constant.SUCCESS){
	            drawGraphLocation();
            } else {
            	// Error: Create toast for user
                CharSequence text = context.getResources().getString(R.string.msg_err_error);
                Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
	
	private void drawGraphAge(){
		int size = ageStatList.size();
		
		GraphViewData[] graphViewData = new GraphViewData[size*2];
		GraphViewData[] graphViewData2 = new GraphViewData[size*2];
		String[] label = new String[size];
		int maxValue = 0;
		for (int i = 0; i<size; i++){
			graphViewData[2*i] = new GraphViewData((long)2*i, (long)ageStatList.get(i).womenNumber);
			graphViewData2[2*i] = new GraphViewData((long)(2*i), (long)0);
			graphViewData[2*i+1] = new GraphViewData((long)2*i+1, (long)0);
			graphViewData2[2*i+1] = new GraphViewData((long)(2*i+1), (long)ageStatList.get(i).menNumber);
			if (ageStatList.get(i).menNumber > maxValue) maxValue = ageStatList.get(i).menNumber;
			if (ageStatList.get(i).womenNumber > maxValue) maxValue = ageStatList.get(i).womenNumber;
			label[i] = ageStatList.get(i).groupName;
		}
		GraphViewSeriesStyle style = new GraphViewSeriesStyle();
		style.color = context.getResources().getColor(R.color.orange);
		GraphViewSeries graphSeries = new GraphViewSeries(
				context.getResources().getString(R.string.report_txt_men),
				style,
				graphViewData);
		GraphViewSeriesStyle style2 = new GraphViewSeriesStyle();
		style2.color = context.getResources().getColor(R.color.orange_light);
		GraphViewSeries graphSeries2 = new GraphViewSeries(
				context.getResources().getString(R.string.report_txt_women),
				style2,
				graphViewData2);
		GraphView graphView = new BarGraphView(
				context, 
				context.getResources().getString(R.string.report_heading_age));
		//((BarGraphView) graphView).setDrawValuesOnTop(true);
		graphView.addSeries(graphSeries);
		graphView.addSeries(graphSeries2);
		graphView.setManualYAxisBounds(maxValue, 0d);
		graphView.getGraphViewStyle().setTextSize(9);
		graphView.getGraphViewStyle().setVerticalLabelsWidth(20);
		graphView.setHorizontalLabels(label);
		graphView.setVerticalLabels(new String[] {
			String.valueOf(maxValue),
			"0"
			});
		layoutAge.addView(graphView);
	}
	
	private void drawGraphGender(){
		
		
		GraphViewData[] graphViewData = new GraphViewData[2];
		
		graphViewData[0] = new GraphViewData((long)0, (long)genderStat.womenNumber);
		graphViewData[1] = new GraphViewData((long)1, (long)genderStat.menNumber);
		int maxValue;
		if (genderStat.womenNumber > genderStat.menNumber){
			maxValue = genderStat.womenNumber;
		} else {
			maxValue = genderStat.menNumber;
		}
		GraphViewSeriesStyle style = new GraphViewSeriesStyle();
		style.setValueDependentColor(new ValueDependentColor() {
			  @Override
			  public int get(GraphViewDataInterface data) {
				  if (data.getX() == 0) {
					  return context.getResources().getColor(R.color.orange);
				  } else {
					  return context.getResources().getColor(R.color.orange_light);
				  }
			  }
			});
		style.color = context.getResources().getColor(R.color.orange);
		GraphViewSeries graphSeries = new GraphViewSeries(
				context.getResources().getString(R.string.report_heading_gender),
				style,
				graphViewData);
		GraphView graphView = new BarGraphView(
				context, 
				context.getResources().getString(R.string.report_heading_gender));
		((BarGraphView) graphView).setDrawValuesOnTop(true);
		graphView.addSeries(graphSeries);
		graphView.setManualYAxisBounds(maxValue, 0);
		graphView.getGraphViewStyle().setTextSize(9);
		graphView.getGraphViewStyle().setVerticalLabelsWidth(20);
		graphView.setHorizontalLabels(new String[] {
			context.getResources().getString(R.string.report_txt_women),
			context.getResources().getString(R.string.report_txt_men)
			});
		graphView.setVerticalLabels(new String[] {
			String.valueOf(maxValue),
			"0"
			});
		layoutGender.addView(graphView);
	}
	
	private void drawGraphLocation() {
		TextView txtLocation, txtFans;
		ViewGroup parentGroup = (ViewGroup) layoutLocation;
		for (Location location : locationStatsList){
			View child = getActivity().getLayoutInflater().inflate(R.layout.item_report_location, null);
			txtLocation = (TextView) child.findViewById(R.id.txtReportLocation);
			txtFans = (TextView) child.findViewById(R.id.txtReportFans);
			
			txtLocation.setText(location.districtName + ", " + location.cityName);
			txtFans.setText(String.valueOf(location.fans));
			
			parentGroup.addView(child);
		}
	}
}
