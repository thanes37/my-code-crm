package com.iseed.crm.android.adapter;

import java.util.List;

import com.iseed.crm.android.R;
import com.iseed.crm.android.common.PointTrack;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PointTrackArrayAdapter  extends ArrayAdapter<PointTrack>{

	protected static final String TAG = "PointTrackArray";
	private final Context context;
	private final List<PointTrack> pointTracks;

	public PointTrackArrayAdapter(Context context,List<PointTrack> pointTracks) {
		super(context, R.layout.item_point_track, pointTracks);
		
		this.context = context;
		this.pointTracks = pointTracks;
	}
	/*
	 * TODO : declare a static inner class ViewHolder inside to improve performance
	 * https://dl.google.com/googleio/2010/android-world-of-listview-android.pdf
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		View rowView = inflater.inflate(R.layout.item_point_track, parent, false);
		
		TextView txtTrackDetail = (TextView) rowView.findViewById(R.id.txtTrackDetail);
		TextView txtTrackTimestamp = (TextView) rowView.findViewById(R.id.txtTrackTimestamp);
		TextView txtTrackPoint = (TextView) rowView.findViewById(R.id.txtTrackPoint);
		
		txtTrackDetail.setText(pointTracks.get(position).detail);
		txtTrackTimestamp.setText(pointTracks.get(position).timestamp);
		txtTrackPoint.setText(String.valueOf(pointTracks.get(position).point));
 
		return rowView;
	}
}
