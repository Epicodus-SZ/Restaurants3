package com.epicodus.myrestaurants.util;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.epicodus.myrestaurants.ui.RestaurantsListActivity;

/**
 * Created by Guest on 9/25/17.
 */

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {
    public static final String TAG = RestaurantsListActivity.class.getSimpleName();
    private final ItemTouchHelperAdapter mAdapter;

    //  This constructor takes an ItemTouchHelperAdapter parameter. When implemented in
    //  FirebaseRestaurantListAdapter, the ItemTouchHelperAdapter instance will pass the gesture event back to the
    //  Firebase adapter where we will define what occurs when an item is moved or dismissed.

    public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
        mAdapter = adapter;
    }

    //  The method below informs the ItemTouchHelperAdapter that drag gestures are enabled.
    //  We could also disable drag gestures by returning 'false'.

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    //  The method below informs the ItemTouchHelperAdapter that swipe gestures are enabled.
    //  We could also disable them by returning 'false'.

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    //  getMovementFlags informs the ItemTouchHelper which movement directions are supported.
    // For example, when a user drags a list item, they press 'Down' to begin the drag and lift their finger, 'Up',  to end the drag.

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    //  The method below notifies the adapter that an item has moved.
    //  This triggers the onItemMove override in our Firebase adapter,
    //  which will eventually handle updating the restaurants ArrayList to reflect the item's new position.

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source,
                          RecyclerView.ViewHolder target) {
        Log.d("Zaske Test", "onMove: old pos: " + source.getOldPosition() + ", new pos:" + target.getPosition());
        if (source.getItemViewType() != target.getItemViewType()) {
            return false;
        }
        mAdapter.onItemMove(source.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    //  The method below notifies the adapter that an item was dismissed.
    //  This triggers the onItemDismiss override in our Firebase adapter
    //  which will eventually handle deleting this item from the user's "Saved Restaurants" in Firebase.

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
        Log.d(TAG, "onSwiped: You destroyed it");
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

    //   The method below triggers the callback in ItemTouchHelperViewHolder which is then sent to our
    //  RestaurantListViewHolder where we will later add animations.

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {

        //  This conditional ensures we only change appearance of active items:

        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof ItemTouchHelperViewHolder) {

                //  This tells the viewHolder that an item is being moved or dragged:

                ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
                itemViewHolder.onItemSelected();
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    //  This triggers the callback in the ItemTouchHelperViewHolder which will be sent to our RestaurantListViewHolder.
    //  Then, in the clearView override in RestaurantListViewHolder, we will remove the animations attached
    //   to 'selected' items, since this item will no longer be actively selected.


    //Clearview is a built in Firebase event, that fires when the MOVE event is done

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (viewHolder instanceof ItemTouchHelperViewHolder) {

            //  Tells the view holder to return the item back to its normal appearance:

            ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
            itemViewHolder.onItemClear();
        }
    }
}
