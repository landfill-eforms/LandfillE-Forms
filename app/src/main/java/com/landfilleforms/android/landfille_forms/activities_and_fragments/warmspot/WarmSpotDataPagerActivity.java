package com.landfilleforms.android.landfille_forms.activities_and_fragments.warmspot;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.landfilleforms.android.landfille_forms.R;
import com.landfilleforms.android.landfille_forms.database.dao.WarmSpotDao;
import com.landfilleforms.android.landfille_forms.model.WarmSpotData;

import java.util.List;
import java.util.UUID;

/**
 * Created by Work on 2/17/2016.
 */
//DONE
public class WarmSpotDataPagerActivity extends AppCompatActivity {
    private static final String EXTRA_WARMSPOT_DATA_ID = "com.landfilleforms.android.landfille_forms.warmspot_data_id";

    private ViewPager mViewPager;
    private List<WarmSpotData> mWarmSpotDataList;

    public static Intent newIntent(Context packageContext, UUID warmSpotDataId) {
        Intent intent = new Intent (packageContext, WarmSpotDataPagerActivity.class);
        intent.putExtra(EXTRA_WARMSPOT_DATA_ID, warmSpotDataId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warm_spot_data_pager);

        UUID warmSpotDataId = (UUID) getIntent().getSerializableExtra(EXTRA_WARMSPOT_DATA_ID);

        mViewPager = (ViewPager) findViewById(R.id.activity_warm_spot_data_pager_view_pager);

        mWarmSpotDataList = WarmSpotDao.get(this).getWarmSpotDatas();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                WarmSpotData warmSpotData = mWarmSpotDataList.get(position);
                return WarmSpotDataFragment.newInstance(warmSpotData.getId());
            }

            @Override
            public int getCount() {
                return mWarmSpotDataList.size();
            }
        });

        for(int i = 0; i < mWarmSpotDataList.size(); i++) {
            if (mWarmSpotDataList.get(i).getId().equals(warmSpotDataId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
