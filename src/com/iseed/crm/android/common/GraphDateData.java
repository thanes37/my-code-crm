package com.iseed.crm.android.common;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.util.Log;

public class GraphDateData {
	private static final String TAG = "GraphDate";
	public static final long DAY_TIME = (24*60*60*1000); 
	private int size;
	private int minValue;
	private int maxValue;
	private Date startDate;
	
	public List<Integer> data;
	
	public GraphDateData(List<DateStatistic> dateStatList, int dayBack){
		size = dayBack;
		data = new ArrayList<Integer>();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.DAY_OF_YEAR, -dayBack );
		Date dateStart = new Date(calendar.getTimeInMillis());
		dateStart = Date.valueOf(dateStart.toString());
		startDate = dateStart;
		Date dateEnd = new Date(System.currentTimeMillis());
		dateEnd = Date.valueOf(dateEnd.toString());
		
		DateStatistic dateStat = new DateStatistic(dateEnd, 0);
		dateStatList.add(dateStat);
		
		// Check first day of this period
		int offset = (int)((dateStatList.get(0).date.getTime() - dateStart.getTime())/DAY_TIME);
		Log.v(TAG, "offset = "+String.valueOf(offset));
		Log.v(TAG, "DateStart" + dateStart.toString());
		Log.v(TAG, "DateEnd" + dateEnd.toString());
		Log.v(TAG, "date = "+dateStatList.get(0).date.toString());
		
		if (offset != 0){
			for (int j =0; j<offset; j++){
				data.add(0);
			}
		}
		minValue = maxValue = dateStatList.get(0).value;
		for (int i =0; i<dateStatList.size()-1; i++){
			data.add(dateStatList.get(i).value);
			if (dateStatList.get(i).value < minValue) minValue = dateStatList.get(i).value;
			else if (dateStatList.get(i).value > maxValue) maxValue = dateStatList.get(i).value;
			offset = (int)((dateStatList.get(i+1).date.getTime() 
					- dateStatList.get(i).date.getTime())/DAY_TIME);
			Log.v(TAG, "offset = "+String.valueOf(offset));
			for (int j =0; j<offset-1; j++){
				data.add(0);
			}
		}
		
		Log.v(TAG, "min = "+String.valueOf(minValue)+ " MAX= "+ String.valueOf(maxValue));
		Log.v(TAG, data.toString());
	}
	
	public int getMinValue(){
		return this.minValue;
	}
	
	public int getMaxValue(){
		return this.maxValue;
	}
	
	public int getSize(){
		return this.size;
	}
	
	public Date getStartDate(){
		return this.startDate;
	}
}
