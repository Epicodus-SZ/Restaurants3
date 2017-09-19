package com.epicodus.myrestaurants.adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.epicodus.myrestaurants.R;
import com.epicodus.myrestaurants.models.Restaurant;
import com.epicodus.myrestaurants.ui.RestaurantDetailActivity;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Guest on 9/12/17.
 */

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.RestaurantViewHolder> {
    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private ArrayList<Restaurant> mRestaurants = new ArrayList<>();
    private Context mContext;
    private boolean isLoadingAdded = false;

    public RestaurantListAdapter(Context context, ArrayList<Restaurant> restaurants) {
        mContext = context;
        mRestaurants = restaurants;
    }

    @Override
    public RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_list_item, parent, false);
        RestaurantViewHolder viewHolder = new RestaurantViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RestaurantViewHolder holder, int position) {
        holder.bindRestaurant(mRestaurants.get(position));
    }

    @Override
    public int getItemCount() {

        return mRestaurants ==null ? 0 : mRestaurants.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == mRestaurants.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void add(Restaurant mc) {
        mRestaurants.add(mc);
        notifyItemInserted(mRestaurants.size() - 1);
    }

    public void addAll(List<Restaurant> mcList) {
        for (Restaurant mc : mcList) {
            add(mc);
        }
    }

    public void remove(Restaurant city) {
        int position = mRestaurants.indexOf(city);
        if (position > -1) {
            mRestaurants.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Restaurant());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = mRestaurants.size() - 1;
        Restaurant item = getItem(position);

        if (item != null) {
            mRestaurants.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Restaurant getItem(int position) {
        return mRestaurants.get(position);
    }





    ///////////////
    //This is our View Holder Class
    //
    //
    //

    public class RestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.restaurantImageView) ImageView mRestaurantImageView;
        @Bind(R.id.restaurantNameTextView) TextView mNameTextView;
        @Bind(R.id.categoryTextView) TextView mCategoryTextView;
        @Bind(R.id.ratingTextView) TextView mRatingTextView;
        private Context mContext;


        public RestaurantViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        public void bindRestaurant(Restaurant restaurant) {

            //this is the Recycler View

            //Validation code to insure there is an image
            String imageToLoad;
            if(restaurant.getImageUrl().isEmpty()){
                // Uri.parse("android.resource://your.package.name/" + R.drawable.sample_1);
                imageToLoad = "android.resource://com.epicodus.myrestaurants/" + R.drawable.placeholder;
            } else {
                imageToLoad = restaurant.getImageUrl();
            }

            Picasso.with(mContext).load(imageToLoad).into(mRestaurantImageView);
            mNameTextView.setText(restaurant.getName());
            mCategoryTextView.setText(restaurant.getCategories().get(0));
            mRatingTextView.setText("Rating: " + restaurant.getRating() + "/5");
        }

        @Override
        public void onClick(View v) {
            int itemPosition = getLayoutPosition();
            Intent intent = new Intent(mContext, RestaurantDetailActivity.class);
            intent.putExtra("position", itemPosition);
            intent.putExtra("restaurants", Parcels.wrap(mRestaurants));
            mContext.startActivity(intent);
        }
    }
}
