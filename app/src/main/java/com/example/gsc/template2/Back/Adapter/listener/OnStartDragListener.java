package com.example.gsc.template2.Back.Adapter.listener;

import android.support.v7.widget.RecyclerView;

/**
 * Created by GSC on 24/12/2016.
 */

public interface OnStartDragListener {
    /**
     * Called when a view is requesting a start of a drag.
     *
     * @param viewHolder The holder of the view to drag.
     */
    void onStartDrag(RecyclerView.ViewHolder viewHolder);
}