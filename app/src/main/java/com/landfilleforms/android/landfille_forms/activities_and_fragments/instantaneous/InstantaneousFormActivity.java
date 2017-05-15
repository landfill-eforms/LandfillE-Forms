package com.landfilleforms.android.landfille_forms.activities_and_fragments.instantaneous;

import android.support.v4.app.Fragment;

import com.landfilleforms.android.landfille_forms.activities_and_fragments.SingleFragmentActivity;

/**
 * Created by Work on 11/3/2016.
 */

public class InstantaneousFormActivity extends SingleFragmentActivity {
    private static final String EXTRA_LANDFILL_LOCATION = "com.landfilleforms.android.landfille_forms.landfill_location";
    @Override
    protected Fragment createFragment() {
        return new InstantaneousFormFragment();
    }
}
