package com.landfilleforms.android.landfille_forms.activities_and_fragments.integrated;

import android.support.v4.app.Fragment;

import com.landfilleforms.android.landfille_forms.activities_and_fragments.SingleFragmentActivity;

/**
 * Created by Work on 3/28/2017.
 */

public class IntegratedFormActivity extends SingleFragmentActivity {
    private static final String EXTRA_LANDFILL_LOCATION = "com.landfilleforms.android.landfille_forms.landfill_location";
    @Override
    protected Fragment createFragment() {
        return new IntegratedFormFragment();
    }
}
