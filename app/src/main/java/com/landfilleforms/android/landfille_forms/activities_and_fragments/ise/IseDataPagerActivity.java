package com.landfilleforms.android.landfille_forms.activities_and_fragments.ise;

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
import com.landfilleforms.android.landfille_forms.database.dao.IseDao;
import com.landfilleforms.android.landfille_forms.model.IseData;

import java.util.List;
import java.util.UUID;

/**
 * Created by owchr on 4/5/2017.
 */

public class IseDataPagerActivity extends AppCompatActivity{
    private static final String EXTRA_ISE_DATA_ID = "com.landfilleforms.android.landfille_forms.ise_data_id";

    private ViewPager mViewPager;
    private List<IseData> mIseDataList;

    public static Intent newIntent(Context packageContext, UUID iseDataId) {
        Intent intent = new Intent (packageContext, IseDataPagerActivity.class);
        intent.putExtra(EXTRA_ISE_DATA_ID, iseDataId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ise_data_pager);

        UUID iseDataId = (UUID) getIntent().getSerializableExtra(EXTRA_ISE_DATA_ID);
        mViewPager = (ViewPager) findViewById(R.id.activity_ise_data_pager_view_pager);
        mIseDataList = IseDao.get(this).getIseDatas();
        FragmentManager fragmentManager = getSupportFragmentManager();

        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                IseData iseData = mIseDataList.get(position);
                return IseDataFragment.newInstance(iseData.getId());
            }

            @Override
            public int getCount() {
                return mIseDataList.size();
            }
        });

        for(int i = 0; i < mIseDataList.size(); i++) {
            if (mIseDataList.get(i).getId().equals(iseDataId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onBackPressed() {
        //this generates the dialog when user backs out
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        IseDataFragment t = new IseDataFragment();

        t.halt(alertBuilder);
        Toast.makeText(getBaseContext(), "Hello", Toast.LENGTH_LONG).show();


    }
}
