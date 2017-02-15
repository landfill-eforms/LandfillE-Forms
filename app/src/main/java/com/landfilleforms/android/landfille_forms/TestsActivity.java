package com.landfilleforms.android.landfille_forms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by aaleman on 2/2/17.
 */


public class TestsActivity extends AppCompatActivity {
    private static final String EXTRA_LANDFILL_LOCATION = "com.landfilleforms.android.landfille_forms.landfill_location";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tests);
    }


    //Event handler for Instantaneous button on Tests Activity
    public void onInstantaneousTestClick(View view) {

//        //Create Intent to grab TestsActivity
//        Intent getTestsActivityIntent = new Intent(this, TestsActivity.class);
//
//        //Call Location Activity to open
//        startActivity(getTestsActivityIntent);

        //Create Intent to grab InstantaneousFormActivity
        Intent getInstantaneousFormActivity = new Intent(this, InstantaneousFormActivity.class);
        getInstantaneousFormActivity.putExtra(EXTRA_LANDFILL_LOCATION, this.getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION));
        //Call Instantaneous Activity to open
        startActivity(getInstantaneousFormActivity);
    }

    //Event handler for Probe button on Tests Activity
    public void onProbeTestClick(View view) {
        Toast.makeText(this, R.string.coming_soon_toast, Toast.LENGTH_SHORT).show();
        //Create Intent to grab ProbeFormActivity
        //Intent getProbeFormActivity = new Intent(this, ProbeFormActivity.class);

        //Call Instantaneous Activity to open
        //startActivity(getProbeFormActivity);

    }

    //Event handler for Integrated button on Tests Activity
    public void onISETestClick(View view) {
        Toast.makeText(this, R.string.coming_soon_toast, Toast.LENGTH_SHORT).show();
        //Create Intent to grab IntegratedFormActivity
        //Intent getIntegratedFormActivity = new Intent(this, IntegratedFormActivity.class);

        //Call Probe activity to open
        //startActivity(getIntegratedFormActivity);
    }

    public void onIMETestClick(View view) {
        Toast.makeText(this, R.string.coming_soon_toast, Toast.LENGTH_SHORT).show();
        //Create Intent to grab IntegratedFormActivity
        //Intent getIntegratedFormActivity = new Intent(this, IntegratedFormActivity.class);

        //Call Probe activity to open
        //startActivity(getIntegratedFormActivity);
    }

    public void onIntegratedTestClick(View view) {
        Toast.makeText(this, R.string.coming_soon_toast, Toast.LENGTH_SHORT).show();
        //Create Intent to grab IntegratedFormActivity
        //Intent getIntegratedFormActivity = new Intent(this, IntegratedFormActivity.class);

        //Call Probe activity to open
        //startActivity(getIntegratedFormActivity);
    }




}
