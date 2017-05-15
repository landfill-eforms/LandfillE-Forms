package com.landfilleforms.android.landfille_forms.activities_and_fragments.warmspot;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.landfilleforms.android.landfille_forms.R;
import com.landfilleforms.android.landfille_forms.util.SessionManager;
import com.landfilleforms.android.landfille_forms.database.dao.WarmSpotDao;
import com.landfilleforms.android.landfille_forms.model.WarmSpotData;
import com.landfilleforms.android.landfille_forms.model.User;


import java.util.List;

/**
 * Created by Work on 11/3/2016.
 */

public class WarmSpotFormFragment extends Fragment {
    private static final String TAG = "WarmSpotFormFrag";
    private static final String EXTRA_LANDFILL_LOCATION = "com.landfilleforms.android.landfille_forms.landfill_location";
    private SessionManager session;
    private User mUser;
    public List<WarmSpotData> mWarmSpotDatas;

    private TextView mWarmspotGridsField;
    private TextView mCurrentLocation;

    private RecyclerView mWarmSpotDataRecyclerView;
    private WarmSpotDataAdapter mAdapter;
    private Button mExportButton;
    private CardView mCardView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getActivity().setTitle(getResources().getString(R.string.warm_spot_form_header) + ": " + this.getActivity().getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION));
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");

        session = new SessionManager(getActivity().getApplicationContext());
        session.checkLogin();

        mUser = session.getCurrentUser();
        Log.d("UserName:", mUser.getUsername());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_warm_spot_form, container, false);

        final WarmSpotDao warmspotDao = WarmSpotDao.get(getActivity());
        String [] args = {this.getActivity().getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION)};
        mWarmSpotDatas = warmspotDao.getWarmSpotDatasByLocation(args);

//        mCurrentLocation = (TextView) v.findViewById(R.id.location);
//        mCurrentLocation.setText(this.getActivity().getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION));


        //TODO: Deleted the code for Grid array since it isn't needed for Warmspots. Should delete the code related to this on the DAO end as well.

        //TODO: Include a spinner so users can filter by Quarter

        mWarmSpotDataRecyclerView = (RecyclerView) v.findViewById(R.id.warm_spot_data_recycler_view);
        mWarmSpotDataRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_warm_spot_form, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.menu_item_new_warm_spot:
                WarmSpotData warmSpotData = new WarmSpotData();
                //Log.d("From FormFrag",getActivity().getIntent().getStringExtra(EXTRA_USERNAME));
                warmSpotData.setLocation(this.getActivity().getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION));
                warmSpotData.setInspectorFullName(mUser.getFullName());
                warmSpotData.setInspectorUserName(mUser.getUsername());
                WarmSpotDao.get(getActivity()).addWarmSpotData(warmSpotData);
                Intent intent = WarmSpotDataPagerActivity.newIntent(getActivity(),warmSpotData.getId());

                startActivity(intent);
                return true;
            case android.R.id.home:
                this.getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI() {
        WarmSpotDao warmSpotDao = WarmSpotDao.get(getActivity());
        String [] args = {this.getActivity().getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION)};
        List<WarmSpotData> warmSpotDatas = warmSpotDao.getWarmSpotDatasByLocation(args);

        if(mAdapter == null) {
            mAdapter = new WarmSpotDataAdapter(warmSpotDatas);
            mWarmSpotDataRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setWarmSpotDatas(warmSpotDatas);
            mAdapter.notifyDataSetChanged();
        }
    }

    //For RecyleView
    private class WarmSpotDataHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private WarmSpotData mWarmSpotData;

        private TextView mGridIdView;
        private TextView mStartDateView;
        private TextView mMethaneReadingView;
        private Button mEditButton;


        public WarmSpotDataHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);

            mGridIdView = (TextView) itemView.findViewById(R.id.list_item_warm_spot_data_gridid_view);
            mMethaneReadingView = (TextView) itemView.findViewById(R.id.list_item_warm_spot_data_methane_level_view);
            mStartDateView = (TextView) itemView.findViewById(R.id.list_item_warm_spot_data_start_date_view);
            mGridIdView.setTextColor(Color.rgb(255,165,0));
            mMethaneReadingView.setTextColor(Color.rgb(255,165,0));
            mStartDateView.setTextColor(Color.rgb(255,165,0));

            mCardView = (CardView)itemView.findViewById(R.id.instantaneous_data_cv);
            mCardView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v){
                    Intent intent = WarmSpotDataPagerActivity.newIntent(getActivity(), mWarmSpotData.getId());
                    startActivity(intent);
                }

            });


//            mEditButton = (Button)itemView.findViewById(R.id.list_item_warm_spot_edit_button);
//            mEditButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = WarmSpotDataPagerActivity.newIntent(getActivity(), mWarmSpotData.getId());
//                    startActivity(intent);
//                }
//            });
        }

        public void bindWarmSpotData(WarmSpotData warmSpotData) {
            mWarmSpotData = warmSpotData;
            mGridIdView.setText(mWarmSpotData.getGridId());
            mMethaneReadingView.setText(Double.toString(mWarmSpotData.getMaxMethaneReading()));
            mStartDateView.setText(DateFormat.format("yyyy-MM-dd",mWarmSpotData.getDate()));
            //mStartTimeView.setText(DateFormat.format("HH:mm:ss",mWarmSpotData.getStartDate()));
            //mEndTimeView.setText(DateFormat.format("HH:mm:ss",mWarmSpotData.getEndDate()));
        }

        @Override
        public void onClick(View v) {
            Intent intent = WarmSpotDataPagerActivity.newIntent(getActivity(), mWarmSpotData.getId());
            startActivity(intent);
        }
    }


    private class WarmSpotDataAdapter extends RecyclerView.Adapter<WarmSpotDataHolder> {

        private List<WarmSpotData> mWarmSpotDatas;

        public WarmSpotDataAdapter(List<WarmSpotData> warmSpotDatas) {
            mWarmSpotDatas = warmSpotDatas;
        }

        @Override
        public WarmSpotDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_warm_spot_data, parent, false);
            return new WarmSpotDataHolder(view);
        }

        @Override
        public void onBindViewHolder(WarmSpotDataHolder holder, int position) {
            WarmSpotData warmSpotData = mWarmSpotDatas.get(position);
            holder.bindWarmSpotData(warmSpotData);
        }

        @Override
        public int getItemCount() {
            return mWarmSpotDatas.size();
        }

        public void setWarmSpotDatas(List<WarmSpotData> warmSpotDatas) {
            mWarmSpotDatas = warmSpotDatas;
        }
    }
}
