package com.landfilleforms.android.landfille_forms.warmspot;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
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
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.landfilleforms.android.landfille_forms.R;
import com.landfilleforms.android.landfille_forms.SessionManager;
import com.landfilleforms.android.landfille_forms.model.WarmSpotData;
import com.landfilleforms.android.landfille_forms.model.User;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Work on 11/3/2016.
 */

public class WarmSpotFormFragment extends Fragment {
    private static final String TAG = "WarmSpotFormFrag";
    private static final String EXTRA_LANDFILL_LOCATION = "com.landfilleforms.android.landfille_forms.landfill_location";
    private SessionManager session;
    private User mUser;


    private RecyclerView mWarmSpotDataRecyclerView;
    private WarmSpotDataAdapter mAdapter;
    private Button mExportButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");

        session = new SessionManager(getActivity().getApplicationContext());
        session.checkLogin();

        HashMap<String,String> currentUser = session.getUserDetails();
        mUser = new User();
        mUser.setUsername(currentUser.get(SessionManager.KEY_USERNAME));
        Log.d("UserName:", mUser.getUsername());
        mUser.setFullName(currentUser.get(SessionManager.KEY_USERFULLNAME));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_warm_spot_form, container, false);

        mWarmSpotDataRecyclerView = (RecyclerView) view.findViewById(R.id.warm_spot_data_recycler_view);
        mWarmSpotDataRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //inflater.inflate(R.menu.fragment_warm_spot_form, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
//        switch (item.getItemId()) {
//            case R.id.menu_item_new_warm_spot:
//                WarmSpotData warmSpotData = new WarmSpotData();
//                //Log.d("From FormFrag",getActivity().getIntent().getStringExtra(EXTRA_USERNAME));
//                warmSpotData.setLocation(this.getActivity().getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION));
//                warmSpotData.setInspectorFullName(mUser.getFullName());
//                warmSpotData.setInspectorUserName(mUser.getUsername());
//                WarmSpotForm.get(getActivity()).addWarmSpotData(warmSpotData);
//                Intent intent = WarmSpotDataPagerActivity.newIntent(getActivity(),warmSpotData.getId());
//
//                startActivity(intent);
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
    }

    private void updateUI() {
        WarmSpotForm warmSpotForm = WarmSpotForm.get(getActivity());
        String [] args = {this.getActivity().getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION)};
        List<WarmSpotData> warmSpotDatas = warmSpotForm.getWarmSpotDatasByLocation(args);

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

            mEditButton = (Button)itemView.findViewById(R.id.list_item_warm_spot_edit_button);
            mEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = WarmSpotDataPagerActivity.newIntent(getActivity(), mWarmSpotData.getId());
                    startActivity(intent);
                }
            });
        }

        public void bindWarmSpotData(WarmSpotData warmSpotData) {
            mWarmSpotData = warmSpotData;
            mGridIdView.setText(mWarmSpotData.getGridId());
            mMethaneReadingView.setText(Double.toString(mWarmSpotData.getMaxMethaneReading()));
            mStartDateView.setText(DateFormat.format("M/d/yyyy",mWarmSpotData.getDate()));
            //mStartTimeView.setText(DateFormat.format("HH:mm:ss",mWarmSpotData.getStartDate()));
            //mEndTimeView.setText(DateFormat.format("HH:mm:ss",mWarmSpotData.getEndDate()));
        }

        @Override
        public void onClick(View v) {
//            Intent intent = WarmSpotDataPagerActivity.newIntent(getActivity(), mWarmSpotData.getId());
//            startActivity(intent);
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
