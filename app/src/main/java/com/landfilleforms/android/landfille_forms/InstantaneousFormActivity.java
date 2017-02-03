package com.landfilleforms.android.landfille_forms;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.UUID;

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
