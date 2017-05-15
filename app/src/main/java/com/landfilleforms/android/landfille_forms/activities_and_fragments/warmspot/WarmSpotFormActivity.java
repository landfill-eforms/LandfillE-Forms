package com.landfilleforms.android.landfille_forms.activities_and_fragments.warmspot;

import android.support.v4.app.Fragment;

import com.landfilleforms.android.landfille_forms.activities_and_fragments.SingleFragmentActivity;

//Done
public class WarmSpotFormActivity extends SingleFragmentActivity {
    private static final String EXTRA_LANDFILL_LOCATION = "com.landfilleforms.android.landfille_forms.landfill_location";
    @Override
    protected Fragment createFragment() {
        return new WarmSpotFormFragment();
    }

}
