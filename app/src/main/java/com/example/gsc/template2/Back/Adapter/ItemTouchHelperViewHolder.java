package com.example.gsc.template2.Back.Adapter;

/**
 * Created by GSC on 24/12/2016.
 */

public interface ItemTouchHelperViewHolder {
    /**
     * Implementations should update the item view to indicate it's active state.
     */
    void onItemSelected();


    /**
     * state should be cleared.
     */
    void onItemClear();
}