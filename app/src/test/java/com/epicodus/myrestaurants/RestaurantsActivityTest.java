package com.epicodus.myrestaurants;

import android.os.Build;
import android.widget.ListView;

import com.epicodus.myrestaurants.ui.RestaurantsActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by Guest on 9/5/17.
 */

@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)

public class RestaurantsActivityTest {

    // Need to update our automation tests to use the recyclerView

//    private RestaurantsActivity activity;
//    private ListView mRestaurantListView;
//
//    @Before
//    public void setup() {
//        activity = Robolectric.setupActivity(RestaurantsActivity.class);
//        mRestaurantListView = (ListView) activity.findViewById(R.id.listView);
//    }
//
//    @Test
//    public void restaurantListViewPopulates() {
//        assertNotNull(mRestaurantListView.getAdapter());
//        assertEquals(mRestaurantListView.getAdapter().getCount(), 15);
//    }
}
