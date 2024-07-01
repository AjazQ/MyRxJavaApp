package com.aqsoft.myrxjavaapp.threelevellistview;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

public class ExpandableListViewUtils {

    public static void setExpandableListViewHeightBasedOnChildren(ExpandableListView expandableListView) {
        ExpandableListAdapter listAdapter = expandableListView.getExpandableListAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(expandableListView.getWidth(), View.MeasureSpec.EXACTLY);

        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, expandableListView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += groupItem.getMeasuredHeight();

            if (expandableListView.isGroupExpanded(i)) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null, expandableListView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                    totalHeight += listItem.getMeasuredHeight();
                }
            }
        }

        ViewGroup.LayoutParams params = expandableListView.getLayoutParams();
        int height = totalHeight + (expandableListView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10) height = 200; // min height of expandable listview
        params.height = height;
        //expandableListView.setLayoutParams(params);
        expandableListView.requestLayout();
    }
}
