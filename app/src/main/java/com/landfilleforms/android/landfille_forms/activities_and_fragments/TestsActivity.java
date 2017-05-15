package com.landfilleforms.android.landfille_forms.activities_and_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.landfilleforms.android.landfille_forms.R;
import com.landfilleforms.android.landfille_forms.activities_and_fragments.instantaneous.InstantaneousFormActivity;
import com.landfilleforms.android.landfille_forms.activities_and_fragments.ime.ImeFormActivity;
import com.landfilleforms.android.landfille_forms.activities_and_fragments.integrated.IntegratedFormActivity;
import com.landfilleforms.android.landfille_forms.activities_and_fragments.ise.IseFormActivity;
import com.landfilleforms.android.landfille_forms.activities_and_fragments.probe.ProbeFormActivity;
import com.landfilleforms.android.landfille_forms.activities_and_fragments.warmspot.WarmSpotFormActivity;

/**
 * Created by aaleman on 2/2/17.
 */

/*TODO: Atm, the instantaneous/IME/warmspot forms' parent activity is the location activity rather than the tests activity
  TODO: This is due to the extra location info being lost when back is pressed. Possible solution is storing the location info in shared preferences.

  TODO: Also, make a TestsFragment and don't make the .xml file directly call the event handlers.
 */
public class TestsActivity extends AppCompatActivity {
    private static final String EXTRA_LANDFILL_LOCATION = "com.landfilleforms.android.landfille_forms.landfill_location";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setTitle(getResources().getString(R.string.tests_header) + ": " + this.getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION));
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


    public void onWarmspotTestClick(View view) {
        //create an Intent for Warmspot form
        Intent getWarmspotFormActivity = new Intent(this, WarmSpotFormActivity.class);
        getWarmspotFormActivity.putExtra(EXTRA_LANDFILL_LOCATION, this.getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION));
        //navigates to warmspot list
        startActivity(getWarmspotFormActivity);
    }

    //Event handler for Probe button on Tests Activity
    public void onProbeTestClick(View view) {
        //Toast.makeText(this, R.string.coming_soon_toast, Toast.LENGTH_SHORT).show();
        //Create Intent to grab ProbeFormActivity
        Intent getProbeFormActivity = new Intent(this, ProbeFormActivity.class);
        getProbeFormActivity.putExtra(EXTRA_LANDFILL_LOCATION, this.getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION));

        //Start Activity
        startActivity(getProbeFormActivity);
    }

    //Event handler for ISE button on Tests Activity
    public void onISETestClick(View view) {
        Intent getIseFormActivity = new Intent(this, IseFormActivity.class);
        getIseFormActivity.putExtra(EXTRA_LANDFILL_LOCATION, this.getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION));

        //Start Activity
        startActivity(getIseFormActivity);
    }

    public void onIMETestClick(View view) {
        //Toast.makeText(this, R.string.coming_soon_toast, Toast.LENGTH_SHORT).show();
        Intent getImeFormActivity = new Intent(this, ImeFormActivity.class);
        getImeFormActivity.putExtra(EXTRA_LANDFILL_LOCATION, this.getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION));

        //Start Activity
        startActivity(getImeFormActivity);
    }

    public void onIntegratedTestClick(View view) {
        Intent getIntegratedFormActivity = new Intent(this, IntegratedFormActivity.class);
        getIntegratedFormActivity.putExtra(EXTRA_LANDFILL_LOCATION, this.getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION));

        //Start Activity
        startActivity(getIntegratedFormActivity);
    }




}
