package com.example.huma.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

///////////////////////////////////////////////////////////////////////////
// AutoFitRecyclerView
///////////////////////////////////////////////////////////////////////////
public class AutoFitRecyclerView extends GridLayoutManager {
    private int minItemWidth;

    public AutoFitRecyclerView(Context context, int minItemWidth) {
        super(context, 1);
        this.minItemWidth = minItemWidth;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        updateSpanCount();
        super.onLayoutChildren(recycler, state);
    }

    private void updateSpanCount() {
        int spanCount = getWidth() / minItemWidth;
        if (spanCount < 1) spanCount = 1;
        if (spanCount > 4) spanCount = 4;
        this.setSpanCount(spanCount);
    }
}
