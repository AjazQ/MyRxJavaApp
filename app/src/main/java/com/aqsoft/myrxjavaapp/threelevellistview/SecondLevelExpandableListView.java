package com.aqsoft.myrxjavaapp.threelevellistview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

public class SecondLevelExpandableListView extends ExpandableListView
{

    public SecondLevelExpandableListView(Context context) {
        super(context);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //999999 is a size in pixels. ExpandableListView requires a maximum height in order to do measurement calculations.
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(999999, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    public void setListViewHeightBasedOnChildren() {
        ExpandableListAdapter listAdapter = getExpandableListAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        int desiredWidth = MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY);

        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, this);
            groupItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += groupItem.getMeasuredHeight();

            if (isGroupExpanded(i)) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null, this);
                    listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
                    totalHeight += listItem.getMeasuredHeight();
                }
            }
        }

        ViewGroup.LayoutParams params = getLayoutParams();
        int height = totalHeight + (getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10) height = 200; // min height of expandable listview
        params.height = height;
        setLayoutParams(params);
        requestLayout();
    }
}