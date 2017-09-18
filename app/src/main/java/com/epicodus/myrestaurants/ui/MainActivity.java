package com.epicodus.myrestaurants.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.epicodus.myrestaurants.Constants;
import com.epicodus.myrestaurants.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.Bind;
import butterknife.ButterKnife;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private String mRecentAddress;

    private DatabaseReference mSearchedLocationReference;


    public static final String TAG = RestaurantsActivity.class.getSimpleName();
    @Bind(R.id.findRestaurantsButton) Button mFindRestaurantsButton;
    @Bind(R.id.locationEditText) EditText mLocationEditText;
    @Bind(R.id.appNameTextView) TextView mAppNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mSearchedLocationReference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(Constants.FIREBASE_CHILD_SEARCHED_LOCATION);


        mSearchedLocationReference.addValueEventListener(new ValueEventListener() { //attach listener

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) { //something changed!
                for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {
                    String location = locationSnapshot.getValue().toString();
                    Log.d("Locations updated", "location: " + location); //log
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { //update UI here if error occurred.

            }
        });



        Log.v(TAG, "The OnCreate Method Fired");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();

        Typeface ostrichFont = Typeface.createFromAsset(getAssets(), "fonts/ostrichregular.ttf");
        mAppNameTextView.setTypeface(ostrichFont);

//        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        mRecentAddress = mSharedPreferences.getString(Constants.PREFERENCES_LOCATION_KEY, null);
//        if (mRecentAddress != null) {
//            mLocationEditText.setText(mRecentAddress);
//            putCursorAtEnd();
//        }



        mFindRestaurantsButton.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG, "The OnStart Method Fired");
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

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.v(TAG, "The OnPostResume Method Fired");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG, "The OnPause Method Fired");
    }

    @Override
    public void onClick(View v) {
        if(v == mFindRestaurantsButton) {
            String location = mLocationEditText.getText().toString();
//            if(!(location).equals("")) {
//                addToSharedPreferences(location); //add Validation here, entering alpha characters crashes the app
//            }

            saveLocationToFirebase(location);

            Intent intent = new Intent(MainActivity.this, RestaurantsActivity.class);
            intent.putExtra("location", location);
            startActivity(intent);
        }
    }

    public void saveLocationToFirebase(String location) {
        mSearchedLocationReference.push().setValue(location);
    }

    private void addToSharedPreferences(String location) {
        mEditor.putString(Constants.PREFERENCES_LOCATION_KEY, location).apply();
    }

    private void putCursorAtEnd(){
        int position = mLocationEditText.length();
        //Editable etext = mLocationEditText.getText();
        mLocationEditText.setSelection(position);
    }
}

