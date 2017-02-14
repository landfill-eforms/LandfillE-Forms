package com.landfilleforms.android.landfille_forms;

import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * Created by Work on 2/3/2017.
 */

public class UserHubActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new UserHubFragment();
    }
}
