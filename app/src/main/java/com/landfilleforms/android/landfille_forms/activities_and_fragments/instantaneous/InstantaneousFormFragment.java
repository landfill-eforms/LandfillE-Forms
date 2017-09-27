package com.landfilleforms.android.landfille_forms.activities_and_fragments.instantaneous;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.landfilleforms.android.landfille_forms.activities_and_fragments.DatePickerFragment;
import com.landfilleforms.android.landfille_forms.R;
import com.landfilleforms.android.landfille_forms.util.SessionManager;
import com.landfilleforms.android.landfille_forms.database.dao.InstantaneousDao;
import com.landfilleforms.android.landfille_forms.model.InstantaneousData;
import com.landfilleforms.android.landfille_forms.model.User;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Work on 11/3/2016.
 */

public class InstantaneousFormFragment extends Fragment {
    private static final String TAG = "InstantaneousFormFragm";
    private static final String EXTRA_LANDFILL_LOCATION = "com.landfilleforms.android.landfille_forms.landfill_location";
    private static final Double DEFAULT_BAROMETRIC_PRESSURE = 30.02;
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;

    private Date currentDate;

    private SessionManager session;
    private User mUser;
    private List<InstantaneousData> mInstantaneousDatas;

    private TextView mCurrentLocation;

    private Button mStartDateButton;

    private EditText mBarometricPressureField;
    private Button mBarometricPressureButton;

    private RecyclerView mInstantaneousDataRecyclerView;
    private InstantaneousDataAdapter mAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        //Set Action Bar Title
        getActivity().setTitle(getResources().getString(R.string.instantaneous_form_header) + ": " + this.getActivity().getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION));

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");

        session = new SessionManager(getActivity().getApplicationContext());
        session.checkLogin();


        mUser = session.getCurrentUser();
        Log.d("UserName:", mUser.getUsername());

        currentDate = new Date();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_instantaneous_form, container, false);
        final InstantaneousDao instantaneousDao = InstantaneousDao.get(getActivity());

        String[] args = {this.getActivity().getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION)};
        mInstantaneousDatas = instantaneousDao.getInstantaneousDatasByLocation(args);

        List<InstantaneousData> filteredByDateInstantaneousDatas = new ArrayList<>();
        for (int i = 0; i < mInstantaneousDatas.size(); i++) {
            if (currentDate.getMonth() == mInstantaneousDatas.get(i).getStartDate().getMonth() && currentDate.getYear() == mInstantaneousDatas.get(i).getStartDate().getYear() && currentDate.getDate() == mInstantaneousDatas.get(i).getStartDate().getDate())
                filteredByDateInstantaneousDatas.add(mInstantaneousDatas.get(i));
        }
        mInstantaneousDatas = filteredByDateInstantaneousDatas;
        Log.d("filteredSize", Integer.toString(filteredByDateInstantaneousDatas.size()));


        mCurrentLocation = (TextView) v.findViewById(R.id.location);
//        mCurrentLocation.setText(this.getActivity().getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION));

        mStartDateButton = (Button) v.findViewById(R.id.current_date);
        updateDate();
        mStartDateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(currentDate);
                dialog.setTargetFragment(InstantaneousFormFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });


        mBarometricPressureField = (EditText) v.findViewById(R.id.barometric_field);
        Log.d("iDatas.size():", Integer.toString(mInstantaneousDatas.size()));
        if (mInstantaneousDatas.size() != 0 && mInstantaneousDatas.get(0).getBarometricPressure() != 0)
            mBarometricPressureField.setText(Double.toString(mInstantaneousDatas.get(0).getBarometricPressure()));
        else
            mBarometricPressureField.setText(Double.toString(DEFAULT_BAROMETRIC_PRESSURE));


        mBarometricPressureButton = (Button) v.findViewById(R.id.barometric_button);
        mBarometricPressureButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                for (int i = 0; i < mInstantaneousDatas.size(); i++) {
                    if (mBarometricPressureField.getText().toString().trim().length() == 0) {
                        mBarometricPressureField.setText("30.01");
                        mInstantaneousDatas.get(i).setBarometricPressure(Double.parseDouble(mBarometricPressureField.getText().toString()));
                        Toast.makeText(getActivity(), R.string.blank_barometric_toast, Toast.LENGTH_SHORT).show();
                    } else
                        mInstantaneousDatas.get(i).setBarometricPressure(Double.parseDouble(mBarometricPressureField.getText().toString()));
                }
                instantaneousDao.updateInstantaneousDatas(mInstantaneousDatas);
                Toast.makeText(getActivity(), R.string.updated_barometric_toast, Toast.LENGTH_SHORT).show();
            }
        });


        mInstantaneousDataRecyclerView = (RecyclerView) v.findViewById(R.id.instantaneous_data_recycler_view);
        mInstantaneousDataRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_instantaneous_form, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_instantaneous:
                InstantaneousData instantaneousData = new InstantaneousData();
                //Log.d("From FormFrag",getActivity().getIntent().getStringExtra(EXTRA_USERNAME));
                instantaneousData.setLandFillLocation(this.getActivity().getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION));
                instantaneousData.setInspectorName(mUser.getFullName());
                instantaneousData.setInspectorUserName(mUser.getUsername());
                instantaneousData.setStartDate(currentDate);
                instantaneousData.setEndDate(new Date(instantaneousData.getStartDate().getTime() + 1500000));

                if (mBarometricPressureField.getText().toString().trim().length() == 0)
                    instantaneousData.setBarometricPressure(DEFAULT_BAROMETRIC_PRESSURE);
                else
                    instantaneousData.setBarometricPressure(Double.parseDouble(mBarometricPressureField.getText().toString()));
                //TODO: I commented this out since the entry shouldn't be added to the DB until submit is pressed.
                InstantaneousDao.get(getActivity()).addInstantaneousData(instantaneousData);
                Intent intent = InstantaneousDataPagerActivity.newIntent(getActivity(), instantaneousData.getId());


                startActivity(intent);
                return true;
            case android.R.id.home:
                this.getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {//Accidentally setting this as (resultCode == REQUEST_DATE) cost me 30 min of debug time orz
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            //TODO: Update UI
            currentDate = date;
            updateDate();
            updateUI();
            updateBarometricPressure();
            //TODO: just make updateDate&pressure be in updateUI
        }
    }

    //TODO: Make it query based on date as well as location(Location done), date should be changed from dropdown
    private void updateUI() {
        InstantaneousDao instantaneousDao = InstantaneousDao.get(getActivity());
        String[] args = {this.getActivity().getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION)};
        mInstantaneousDatas = instantaneousDao.getInstantaneousDatasByLocation(args);


        //TODO: Change this so it checks for date in the SQL query.         Issues: DB stores date as a long. So if we query by date, it might just look for entries w/ that exact time+date.
        List<InstantaneousData> filteredByDateInstantaneousDatas = new ArrayList<>();
        for (int i = 0; i < mInstantaneousDatas.size(); i++) {
            if (currentDate.getMonth() == mInstantaneousDatas.get(i).getStartDate().getMonth() && currentDate.getYear() == mInstantaneousDatas.get(i).getStartDate().getYear() && currentDate.getDate() == mInstantaneousDatas.get(i).getStartDate().getDate())
                filteredByDateInstantaneousDatas.add(mInstantaneousDatas.get(i));
        }
        mInstantaneousDatas = filteredByDateInstantaneousDatas;
        Log.d("filteredSize", Integer.toString(filteredByDateInstantaneousDatas.size()));

        if (mAdapter == null) {
            mAdapter = new InstantaneousDataAdapter(mInstantaneousDatas);
            mInstantaneousDataRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setInstantaneousDatas(mInstantaneousDatas);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void updateDate() {
        mStartDateButton.setText(DateFormat.format("EEEE, MMM d, yyyy", currentDate));
    }

    private void updateBarometricPressure() {
        if (mInstantaneousDatas.size() != 0 && mInstantaneousDatas.get(0).getBarometricPressure() != 0)
            mBarometricPressureField.setText(Double.toString(mInstantaneousDatas.get(0).getBarometricPressure()));
        else
            mBarometricPressureField.setText(Double.toString(DEFAULT_BAROMETRIC_PRESSURE));
    }

    //For RecyclerView
    private class InstantaneousDataHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private InstantaneousData mInstantaneousData;

        private TextView mGridIdView;
        private TextView mStartDateView;
        private TextView mStartTimeView;
        private TextView mEndTimeView;
        private TextView mMethaneReadingView;
        private Button mEditButton;
        private CardView mCardView;
        private TextView mInstUser;

        //Form Data Holder

        public InstantaneousDataHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mGridIdView = (TextView) itemView.findViewById(R.id.list_item_instantaneous_data_gridid_view);
            mMethaneReadingView = (TextView) itemView.findViewById(R.id.list_item_instantaneous_data_methane_level_view);
            mStartDateView = (TextView) itemView.findViewById(R.id.list_item_instantaneous_data_start_date_view);
            //mStartTimeView = (TextView) itemView.findViewById(R.id.list_item_instantaneous_data_start_time_view);
            //mEndTimeView = (TextView) itemView.findViewById(R.id.list_item_instantaneous_data_end_time_view);
            mInstUser = (TextView) itemView.findViewById(R.id.list_item_instantaneous_inspector_name);

            mCardView = (CardView) itemView.findViewById(R.id.instantaneous_data_cv);
            mCardView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = InstantaneousDataPagerActivity.newIntent(getActivity(), mInstantaneousData.getId());
                    startActivity(intent);
                }

            });


//            mEditButton = (Button)itemView.findViewById(R.id.list_item_instantaneous_edit_button);
//            mEditButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = InstantaneousDataPagerActivity.newIntent(getActivity(), mInstantaneousData.getId());
//                    startActivity(intent);
//                }
//            });
        }

        public void bindInstantaneousData(InstantaneousData instantaneousData) {
            mInstantaneousData = instantaneousData;
            mGridIdView.setText(mInstantaneousData.getGridId());
            //this display value with 2 sig figs
            mMethaneReadingView.setText(String.format("%.2f", mInstantaneousData.getMethaneReading()));
            mStartDateView.setText(DateFormat.format("yyyy-MM-dd", mInstantaneousData.getStartDate()));
            //Set colors depending on ch4 level in RecyclerView
            if (mInstantaneousData.getMethaneReading() >= 500) {
                mGridIdView.setTextColor(Color.RED);
                mMethaneReadingView.setTextColor(Color.RED);
                mStartDateView.setTextColor(Color.RED);
            } else if (mInstantaneousData.getMethaneReading() >= 200 && mInstantaneousData.getMethaneReading() < 500) {
                mGridIdView.setTextColor(Color.rgb(255, 165, 0));
                mMethaneReadingView.setTextColor(Color.rgb(255, 165, 0));
                mStartDateView.setTextColor(Color.rgb(255, 165, 0));
            } else {
                mGridIdView.setTextColor(Color.WHITE);
                mMethaneReadingView.setTextColor(Color.WHITE);
                mStartDateView.setTextColor(Color.WHITE);
            }
            //mStartTimeView.setText(DateFormat.format("HH:mm:ss",mInstantaneousData.getStartDate()));
            //mEndTimeView.setText(DateFormat.format("HH:mm:ss",mInstantaneousData.getEndDate()));
        }

        @Override
        public void onClick(View v) {
//            Intent intent = InstantaneousDataPagerActivity.newIntent(getActivity(), mInstantaneousData.getId());
//            startActivity(intent);
        }
    }


    //list_item_instantaneous_data.xml
    private class InstantaneousDataAdapter extends RecyclerView.Adapter<InstantaneousDataHolder> {

        private List<InstantaneousData> mInstantaneousDatas;
        private InstantaneousData d;

        public InstantaneousDataAdapter(List<InstantaneousData> instantaneousDatas) {
            mInstantaneousDatas = instantaneousDatas;

        }

        @Override
        public InstantaneousDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_instantaneous_data, parent, false);
            return new InstantaneousDataHolder(view);
        }

        @Override
        public void onBindViewHolder(InstantaneousDataHolder holder, int position) {
            InstantaneousData instantaneousData = mInstantaneousDatas.get(position);
            holder.bindInstantaneousData(instantaneousData);
        }

        @Override
        public int getItemCount() {
            return mInstantaneousDatas.size();
        }

        public void setInstantaneousDatas(List<InstantaneousData> instantaneousDatas) {
            mInstantaneousDatas = instantaneousDatas;
        }
    }

}
