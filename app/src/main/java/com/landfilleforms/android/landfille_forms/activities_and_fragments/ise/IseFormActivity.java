package com.landfilleforms.android.landfille_forms.activities_and_fragments.ise;

import android.support.v4.app.Fragment;
import com.landfilleforms.android.landfille_forms.activities_and_fragments.SingleFragmentActivity;


/**
 * Created by owchr on 4/5/2017.
 */

public class IseFormActivity extends SingleFragmentActivity{
    private static final String EXTRA_LANDFILL_LOCATION = "com.landfilleforms.android.landfille_forms.landfill_location";
    @Override
    protected Fragment createFragment() {
        return new IseFormFragment();
    }
}
