package com.landfilleforms.android.landfille_forms.ime;


import android.support.v4.app.Fragment;

import com.landfilleforms.android.landfille_forms.SingleFragmentActivity;

/**
 * Created by Work on 2/16/2017.
 */

//Done
public class ImeFormActivity extends SingleFragmentActivity {
    private static final String EXTRA_LANDFILL_LOCATION = "com.landfilleforms.android.landfille_forms.landfill_location";
    @Override
    protected Fragment createFragment() {
        return new ImeFormFragment();
    }
}
