package com.landfilleforms.android.landfille_forms.activities_and_fragments;
//Need to change this to a single fragment activity

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.landfilleforms.android.landfille_forms.R;
import com.landfilleforms.android.landfille_forms.model.User;
import com.landfilleforms.android.landfille_forms.util.SessionManager;

/**
 * Created by aaleman on 2/2/17.
 */

public class LocationActivity extends AppCompatActivity {
    private static final String TAG = "LocationActivity";
    private static final String EXTRA_LANDFILL_LOCATION = "com.landfilleforms.android.landfille_forms.landfill_location";
    SessionManager session;
    User mUser;
    private DrawerLayout mDrawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        session = new SessionManager(getApplicationContext());
        session.checkLogin();

        mUser = session.getCurrentUser();
        Log.d("UserName:", mUser.getUsername());
    }



    //Event handler for Bishops button on Location Activity
    public void onBishopsLocationClick(View view) {

        //Create Intent to grab TestsActivity
        Intent getTestsActivityIntent = new Intent(this, TestsActivity.class);
        getTestsActivityIntent.putExtra(EXTRA_LANDFILL_LOCATION, "Bishops");
        //Call Location Activity to open
        startActivity(getTestsActivityIntent);
    }

    //Event handler for Gaffey button on Location Activity
    public void onGaffeyLocationClick(View view) {

        //Create Intent to grab TestsActivity
        Intent getTestsActivityIntent = new Intent(this, TestsActivity.class);
        getTestsActivityIntent.putExtra(EXTRA_LANDFILL_LOCATION, "Gaffey");
        //Call Location Activity to open
        startActivity(getTestsActivityIntent);
    }

    //Event handler for Toyon button on Location Activity
    public void onToyonLocationClick(View view) {

        //Create Intent to grab TestsActivity
        Intent getTestsActivityIntent = new Intent(this, TestsActivity.class);
        getTestsActivityIntent.putExtra(EXTRA_LANDFILL_LOCATION, "Toyon");
        //Call Location Activity to open
        startActivity(getTestsActivityIntent);
    }


    //Event handler for Lopez button on Location Activity
    public void onLopezLocationClick(View view) {

        //Create Intent to grab TestsActivity
        Intent getTestsActivityIntent = new Intent(this, TestsActivity.class);
        getTestsActivityIntent.putExtra(EXTRA_LANDFILL_LOCATION, "Lopez");
        //Call Location Activity to open
        startActivity(getTestsActivityIntent);
    }

    //Event handler for Sheldon button on Location Activity
    public void onSheldonLocationClick(View view) {

        //Create Intent to grab TestsActivity
        Intent getTestsActivityIntent = new Intent(this, TestsActivity.class);
        getTestsActivityIntent.putExtra(EXTRA_LANDFILL_LOCATION, "Sheldon");
        //Call Location Activity to open
        startActivity(getTestsActivityIntent);
    }

    @Override
    public void onStart() {
        super.onStart();
        session.checkLogin();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        session.checkLogin();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }
}
