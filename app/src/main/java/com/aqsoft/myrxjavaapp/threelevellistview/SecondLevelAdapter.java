package com.aqsoft.myrxjavaapp.threelevellistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.aqsoft.myrxjavaapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SecondLevelAdapter extends BaseExpandableListAdapter {

    private Context context;

    List<String> groups = new ArrayList<String>();
    List<String[]> data;

    String[] headers;
    final SecondLevelExpandableListView parentLV ;


    public SecondLevelAdapter(Context context, SecondLevelExpandableListView parentLV, String[] headers, List<String[]> data) {
        this.context = context;
        this.data = data;
        this.headers = headers;
        this.parentLV = parentLV;
        groups.add("Group 1");
        groups.add("Group 2");
        groups.add("Group 3");
    }

    @Override
    public Object getGroup(int groupPosition) {

        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {

        return groups.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.row_second, null);
        TextView text = (TextView) convertView.findViewById(R.id.rowSecondText);
        String groupText = getGroup(groupPosition).toString();
        text.setText(groupText);

        return convertView;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        String[] childData;

        childData = data.get(groupPosition);


        return childData[childPosition];
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final SecondLevelExpandableListView secondLevelELV = new SecondLevelExpandableListView(context);

//        String[] headers = secondLevel.get(groupPosition);
//
//
//        List<String[]> childData = new ArrayList<>();
//        HashMap<String, String[]> secondLevelData=data.get(groupPosition);
//
//        for(String key : secondLevelData.keySet())
//        {
//
//
//            childData.add(secondLevelData.get(key));
//
//        }



        secondLevelELV.setAdapter(new ThirdLevelAdapter(context, secondLevelELV, headers,data));

        secondLevelELV.setGroupIndicator(null);


        secondLevelELV.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if(parentLV!=null){
                    parentLV.setListViewHeightBasedOnChildren();
                }
                if(groupPosition != previousGroup)
                    secondLevelELV.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });


        return secondLevelELV;

//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        convertView = inflater.inflate(R.layout.row_third, null);
//
//        TextView textView = (TextView) convertView.findViewById(R.id.rowThirdText);
//
//        String[] childArray=data.get(groupPosition);
//
//        String text = childArray[childPosition];
//
//        textView.setText(text);
//
//        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        //String[] children = data.get(groupPosition);


        //return children.length;
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}