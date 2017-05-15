package com.landfilleforms.android.landfille_forms.activities_and_fragments.integrated;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.landfilleforms.android.landfille_forms.R;
import com.landfilleforms.android.landfille_forms.database.dao.IntegratedDao;
import com.landfilleforms.android.landfille_forms.model.IntegratedData;

import java.util.List;
import java.util.UUID;

/**
 * Created by Work on 3/28/2017.
 */

public class IntegratedDataPagerActivity extends AppCompatActivity {
    private static final String EXTRA_INTEGRATED_DATA_ID = "com.landfilleforms.android.landfille_forms.integrated_data_id";

    private ViewPager mViewPager;
    private List<IntegratedData> mIntegratedDataList;

    public static Intent newIntent(Context packageContext, UUID integratedDataId) {
        Intent intent = new Intent(packageContext, IntegratedDataPagerActivity.class);
        intent.putExtra(EXTRA_INTEGRATED_DATA_ID, integratedDataId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integrated_data_pager);

        UUID integratedDataId = (UUID) getIntent().getSerializableExtra(EXTRA_INTEGRATED_DATA_ID);

        mViewPager = (ViewPager) findViewById(R.id.activity_integrated_data_pager_view_pager);

        mIntegratedDataList = IntegratedDao.get(this).getIntegratedDatas();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                IntegratedData integratedData = mIntegratedDataList.get(position);
                return IntegratedDataFragment.newInstance(integratedData.getId());
            }

            @Override
            public int getCount() {
                return mIntegratedDataList.size();
            }
        });

        for(int i = 0; i < mIntegratedDataList.size(); i++) {
            if (mIntegratedDataList.get(i).getId().equals(integratedDataId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    //TODO: Implement so that it asks if you wanna discard changes when back is pressed. Issues: Data pager might mess up how data is saved.
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);


        this.finish();
    }
}
