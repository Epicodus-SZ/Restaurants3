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

import butterknife.Bind;
import butterknife.ButterKnife;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private String mRecentAddress;


    public static final String TAG = RestaurantsActivity.class.getSimpleName();
    @Bind(R.id.findRestaurantsButton) Button mFindRestaurantsButton;
    @Bind(R.id.locationEditText) EditText mLocationEditText;
    @Bind(R.id.appNameTextView) TextView mAppNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "The OnCreate Method Fired");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();

        Typeface ostrichFont = Typeface.createFromAsset(getAssets(), "fonts/ostrichregular.ttf");
        mAppNameTextView.setTypeface(ostrichFont);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mRecentAddress = mSharedPreferences.getString(Constants.PREFERENCES_LOCATION_KEY, null);
        if (mRecentAddress != null) {
            mLocationEditText.setText(mRecentAddress);
            putCursorAtEnd();
        }


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
            addToSharedPreferences(location);
            Intent intent = new Intent(MainActivity.this, RestaurantsActivity.class);
            intent.putExtra("location", location);
            startActivity(intent);
        }
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

