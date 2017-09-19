package com.epicodus.myrestaurants.services;

import android.util.Log;

import com.epicodus.myrestaurants.Constants;
import com.epicodus.myrestaurants.R;
import com.epicodus.myrestaurants.models.Restaurant;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by Guest on 9/11/17.
 */

public class YelpService {
    public static void findRestaurants(String location, Callback callback) {


        OkHttpClient client = new OkHttpClient();


        //Example using strings.xml
        // HttpUrl.Builder urlBuilder = HttpUrl.parse(getResources().).newBuilder();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.YELP_BASE_URL).newBuilder();
        urlBuilder.addQueryParameter(Constants.YELP_LOCATION_QUERY_PARAMETER, location);
        String url = urlBuilder.build().toString();


        Request request= new Request.Builder()
                .header("Authorization", "Bearer " + Constants.YELP_TOKEN)
                .url(url)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public ArrayList<Restaurant> processResults(Response response) {
        ArrayList<Restaurant> restaurants = new ArrayList<>();

        try {
            Log.v("At the TRY, response = ", response.toString());
            String jsonData = response.body().string();
            if (response.isSuccessful()) {
                JSONObject yelpJSON = new JSONObject(jsonData);
                JSONArray businessesJSON = yelpJSON.getJSONArray("businesses");
                for (int i = 0; i < businessesJSON.length(); i++) {
                    JSONObject restaurantJSON = businessesJSON.getJSONObject(i);
                    String name = restaurantJSON.getString("name");
                    String phone = restaurantJSON.optString("display_phone", "Phone not available");
                    String website = restaurantJSON.getString("url");
                    double rating = restaurantJSON.getDouble("rating");
                    String imageUrl = restaurantJSON.getString("image_url");

                    //Adding code to insure there is an image...as some Yelp businesses
                    // do not have images in theDB

                    //String imageUrl;
//                    if (restaurantJSON.getString("image_url").isEmpty()) {
//                        imageUrl = "https://www.zaske.com/images/me.png";
//                        // iview.setImageResource(R.drawable.placeholder);
//                    } else{
//                        imageUrl = restaurantJSON.getString("image_url");
//                    }
//
                    double latitude = restaurantJSON.getJSONObject("coordinates")
                            .getDouble("latitude");
                    double longitude = restaurantJSON.getJSONObject("coordinates")
                            .getDouble("longitude");
                    ArrayList<String> address = new ArrayList<>();
                    JSONArray addressJSON = restaurantJSON.getJSONObject("location")
                            .getJSONArray("display_address");
                    for (int y = 0; y < addressJSON.length(); y++) {
                        address.add(addressJSON.get(y).toString());
                    }

                    ArrayList<String> categories = new ArrayList<>();
                    JSONArray categoriesJSON = restaurantJSON.getJSONArray("categories");

                    for (int y = 0; y < categoriesJSON.length(); y++) {
                        categories.add(categoriesJSON.getJSONObject(y).get("title").toString());
                    }
                    Restaurant restaurant = new Restaurant(name, phone, website, rating,
                            imageUrl, address, latitude, longitude, categories);
                    restaurants.add(restaurant);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return restaurants;
    }
}
