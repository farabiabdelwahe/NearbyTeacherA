package com.example.gsc.template2.Back.Adapter;

/**
 * Created by GSC on 23/12/2016.
 */

public interface ItemTouchHelperAdapter {

    void onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}