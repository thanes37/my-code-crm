package com.iseed.crm.android.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.iseed.crm.android.R;
import com.iseed.crm.android.common.PointTrack;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class PointListAdapter extends BaseExpandableListAdapter{

	private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    private List<PointParent> list;
 
    public PointListAdapter(Context context, List<PointParent> listData) {
        this._context = context;
//        this._listDataHeader = listDataHeader;
//        this._listDataChild = listChildData;
        list = listData;
    }
 
    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.list.get(groupPosition).children.get(childPosititon);
    }
 
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
 
    @Override
    public View getChildView(int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
 
        final PointTrack child = (PointTrack) getChild(groupPosition, childPosition);
 
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.point_item, null);
        }
 
        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);
        txtListChild.setText(child.detail);
        TextView txtPoint = (TextView) convertView.findViewById(R.id.txtPointHistory);
        txtPoint.setText(Integer.toString(child.point));
        TextView txtDate = (TextView) convertView.findViewById(R.id.txtPointTimestamp);
        txtDate.setText(child.timestamp);
        return convertView;
    }
 
    @Override
    public int getChildrenCount(int groupPosition) {
        return this.list.get(groupPosition).children.size();
    }
 
    @Override
    public Object getGroup(int groupPosition) {
        return this.list.get(groupPosition);
    }
 
    @Override
    public int getGroupCount() {
        return this.list.size();
    }
 
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
 
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
    	PointParent header = (PointParent) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.shop_list_group, null);
        }
 
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(header.shopName);
        TextView txtPointSum = (TextView) convertView.findViewById(R.id.txtPointSum);
        txtPointSum.setText(Integer.toString(header.pointSum));
 
        return convertView;
    }
 
    @Override
    public boolean hasStableIds() {
        return false;
    }
 
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
