package com.landfilleforms.android.landfille_forms.activities_and_fragments.probe;

import android.support.v4.app.Fragment;

import com.landfilleforms.android.landfille_forms.activities_and_fragments.SingleFragmentActivity;

/**
 * Created by Work on 3/27/2017.
 */

public class ProbeFormActivity extends SingleFragmentActivity{
    @Override
    protected Fragment createFragment() { return new ProbeFormFragment(); }
}
