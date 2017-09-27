package com.landfilleforms.android.landfille_forms.activities_and_fragments.probe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.landfilleforms.android.landfille_forms.R;
import com.landfilleforms.android.landfille_forms.database.dao.ProbeDao;
import com.landfilleforms.android.landfille_forms.model.ProbeData;

import java.util.List;
import java.util.UUID;

/**
 * Created by Work on 3/27/2017.
 */

public class ProbeDataPagerActivity extends AppCompatActivity {
    private static final String EXTRA_PROBE_DATA_ID = "com.landfilleforms.android.landfille_forms.probe_data_id";

    private ViewPager mViewPager;
    private List<ProbeData> mProbeDataList;

    public static Intent newIntent(Context packageContext, UUID probeDataId) {
        Intent intent = new Intent(packageContext, ProbeDataPagerActivity.class);
        intent.putExtra(EXTRA_PROBE_DATA_ID, probeDataId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_probe_data_pager);

        UUID probeDataId = (UUID) getIntent().getSerializableExtra(EXTRA_PROBE_DATA_ID);

        mViewPager = (ViewPager) findViewById(R.id.activity_probe_data_pager_view_pager);

        mProbeDataList = ProbeDao.get(this).getProbeDatas();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                ProbeData probeData = mProbeDataList.get(position);
                return ProbeDataFragment.newInstance(probeData.getId());
            }

            @Override
            public int getCount() {
                return mProbeDataList.size();
            }
        });

        for(int i = 0; i < mProbeDataList.size(); i++) {
            if (mProbeDataList.get(i).getId().equals(probeDataId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //TODO: Implement so that it asks if you wanna discard changes when back is pressed. Issues: Data pager might mess up how data is saved.
    @Override
    public void onBackPressed() {
        //this generates the dialog when user backs out
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        ProbeDataFragment t = new ProbeDataFragment();

        t.halt(alertBuilder);
        Toast.makeText(getBaseContext(), "Hello", Toast.LENGTH_LONG).show();

    }
}
