package com.epicodus.myrestaurants.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.epicodus.myrestaurants.Constants;
import com.epicodus.myrestaurants.R;
import com.epicodus.myrestaurants.adapters.PaginationScrollListener;
import com.epicodus.myrestaurants.adapters.RestaurantListAdapter;
import com.epicodus.myrestaurants.models.Restaurant;
import com.epicodus.myrestaurants.services.YelpService;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RestaurantsListActivity extends AppCompatActivity {
    public static final String TAG = RestaurantsListActivity.class.getSimpleName();

    //Added as psrt of pagination test
    LinearLayoutManager linearLayoutManager;
    // RecyclerView rv;
    @Bind(R.id.progressBar) ProgressBar progressBar;
    // Index from which pagination should start (0 is 1st page in our case)
    private static final int PAGE_START = 0;
    // Indicates if footer ProgressBar is shown (i.e. next page is loading)
    private boolean isLoading = false;
    // If current page is the last page (Pagination will stop after this page load)
    private boolean isLastPage = false;
    // total no. of pages to load. Initial load is page 0, after which 2 more pages will load.
    private int TOTAL_PAGES = 3;
    // indicates the current page which Pagination is fetching.
    private int currentPage = PAGE_START;


    private SharedPreferences mSharedPreferences; //This is a way to store preferred zip code
    private SharedPreferences.Editor mEditor; // We need an editor to save prefs
    private String mRecentAddress; // the string to be saved

    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;
    private RestaurantListAdapter mAdapter;

    public ArrayList<Restaurant> mRestaurants = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String location = intent.getStringExtra("location");

        getRestaurants(location);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mRecentAddress = mSharedPreferences.getString(Constants.PREFERENCES_LOCATION_KEY, null);

        if (mRecentAddress != null) {
            getRestaurants(mRecentAddress);
        }

    }

    //This method fires during/after OnCreate IF you have an action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        ButterKnife.bind(this);

        //We should add here...
        // grab the shared preferences zipcode
        // set the search box text to the zipcode
        // put cursor at end of string

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();

        // Question? Why is this getting bound after Butterknife?
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                try {
                    getRestaurants(query);
                    Log.d(TAG, "The query string is: " + query);
                    addToSharedPreferences(query);
                } catch (Exception e) {
                    Log.d(TAG, "OOOops somethingbad happened");
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });

        return true;
    }

    // This Method fires when someone selects a menu item
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void addToSharedPreferences(String location) {
        mEditor.putString(Constants.PREFERENCES_LOCATION_KEY, location).apply();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG, "The OnStop Method Fired");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "The OnDestroy Method Fired");
    }

    private void getRestaurants(String location) {
        final YelpService yelpService = new YelpService();

        yelpService.findRestaurants(location, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) {
                mRestaurants = yelpService.processResults(response);

                RestaurantsListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        mAdapter = new RestaurantListAdapter(getApplicationContext(), mRestaurants);
//                        mRecyclerView.setAdapter(mAdapter);
//                        RecyclerView.LayoutManager layoutManager =
//                                new LinearLayoutManager(RestaurantsListActivity.this);
//                        mRecyclerView.setLayoutManager(layoutManager);
//                        mRecyclerView.setHasFixedSize(true);

                        RestaurantListAdapter adapter = new RestaurantListAdapter(getApplicationContext(),mRestaurants);
                        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                        mRecyclerView.setLayoutManager(linearLayoutManager);
                        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        mRecyclerView.setAdapter(adapter);

                        mRecyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
                            @Override
                            protected void loadMoreItems() {
                                isLoading = true;
                                currentPage += 1;

                                // mocking network delay for API call
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        loadNextPage();
                                    }
                                }, 1000);
                            }

                            @Override
                            public int getTotalPageCount() {
                                return TOTAL_PAGES;
                            }

                            @Override
                            public boolean isLastPage() {
                                return isLastPage;
                            }

                            @Override
                            public boolean isLoading() {
                                return isLoading;
                            }
                        });



                    }
                }); //end of runOnUIThread
            } //end of on Response
        }); //end of FindRestaurants
    } //end of get restaurants

    private void loadFirstPage() {
        Log.d(TAG, "loadFirstPage: ");
        List<Restaurant> movies = Restaurant.createRestaurants(mAdapter.getItemCount());
        progressBar.setVisibility(View.GONE);
        adapter.addAll(movies);

        if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
        else isLastPage = true;

    }

    private void loadNextPage() {
        Log.d(TAG, "loadNextPage: " + currentPage);
        List<Restaurant> movies = Restaurant.createRestaurants(mAdapter.getItemCount());

        adapter.removeLoadingFooter();
        isLoading = false;

        adapter.addAll(movies);

        if (currentPage != TOTAL_PAGES) mAdapter.addLoadingFooter();
        else isLastPage = true;
    }
} //end of class