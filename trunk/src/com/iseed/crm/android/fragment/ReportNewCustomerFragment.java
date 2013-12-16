package com.iseed.crm.android.fragment;

import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.iseed.crm.android.R;
import com.iseed.crm.android.common.ConnectServer;
import com.iseed.crm.android.common.Constant;
import com.iseed.crm.android.common.DateStatistic;
import com.iseed.crm.android.common.GraphDateData;
import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;

public class ReportNewCustomerFragment extends Fragment{
	private Context context;
	private ProgressBar progressBar;
	protected List<DateStatistic> dateStatList;
	protected int dayBack;
	private LinearLayout layout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		context = getActivity();
		dayBack = Constant.DEFAULT_DAY_BACK;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_report_new_customer, container, false);
		
		progressBar = (ProgressBar) rootView.findViewById(R.id.prgbReportNewCustomer);
		progressBar.setVisibility(View.INVISIBLE);
		layout = (LinearLayout)  rootView.findViewById(R.id.graphNewCustomer);
		if (ConnectServer.isOnline(context)){
        	new GetReportNewCustomerTask().execute();
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
	
	private class GetReportNewCustomerTask extends AsyncTask<Void, Void, Integer> {
		
        protected void onPreExecute (){
        	progressBar.setVisibility(View.VISIBLE);
        }
        
        protected Integer doInBackground(Void... uid) {
        	ConnectServer connect = new ConnectServer(context);
        	dateStatList = connect.getNewCustomerReport(dayBack);
            return connect.resultCode;
        }

        protected void onPostExecute(Integer result) {
            progressBar.setVisibility(View.GONE);
            
            if (result == Constant.SUCCESS){
	            drawGraph();
            } else {
            	// Error: Create toast for user
                CharSequence text = context.getResources().getString(R.string.msg_err_error);
                Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
	
	private void drawGraph(){
		GraphDateData graphDate = new GraphDateData(dateStatList, dayBack);
		
		GraphViewData[] graphViewData = new GraphViewData[dayBack];
		for (int i = 0; i<dayBack; i++){
			graphViewData[i] = new GraphViewData((long)i, (long)graphDate.data.get(i));
		}
		GraphViewSeriesStyle style = new GraphViewSeriesStyle();
		style.color = context.getResources().getColor(R.color.orange);
		GraphViewSeries graphSeries = new GraphViewSeries(
				context.getResources().getString(R.string.report_heading_new_customer),
				style,
				graphViewData);
		GraphView graphView = new BarGraphView(
				context, 
				context.getResources().getString(R.string.report_heading_new_customer));
		//((BarGraphView) graphView).setDrawValuesOnTop(true);
		graphView.addSeries(graphSeries);
		graphView.setManualYAxisBounds(graphDate.getMaxValue(), 0d);
		graphView.getGraphViewStyle().setTextSize(9);
		graphView.getGraphViewStyle().setVerticalLabelsWidth(20);
		graphView.setHorizontalLabels(new String[] {
			context.getResources().getString(R.string.report_txt_yesterday)
			});
		graphView.setVerticalLabels(new String[] {
			String.valueOf(graphDate.getMaxValue()),
			"0"
			});
		layout.addView(graphView);
	}
}
