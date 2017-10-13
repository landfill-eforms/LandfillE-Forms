package com.landfilleforms.android.landfille_forms.activities_and_fragments.probe;

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
import com.landfilleforms.android.landfille_forms.database.dao.ProbeDao;
import com.landfilleforms.android.landfille_forms.model.ProbeData;
import com.landfilleforms.android.landfille_forms.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Work on 3/27/2017.
 */

//TODO: I think I might just not have a ProbeDataFragment at all. I'll just make the recycler view for every probe and have all the fields that you'd want to edit(Only 3 fields - H2O/CH4/Remarks)
public class ProbeFormFragment extends Fragment {
    private static final String TAG = "ProbeFormFrag";
    private static final String EXTRA_LANDFILL_LOCATION = "com.landfilleforms.android.landfille_forms.landfill_location";

    //For ProbeFormFragment, InstananeousFormFragment and IntegratedFormFragment I changed all default values to 2 sig figs
    private static final Double defaultBarometricPressure = 30.01;
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;

    private Date currentDate;

    private SessionManager session;
    private User mUser;
    List<ProbeData> mProbeDatas;

    private TextView mCurrentLocation;

    private Button mDateButton;

    private TextView mInspectorsInvolved;

    private EditText mBarometricPressureField;
    private Button mBarometricPressureButton;

    private RecyclerView mProbeDataRecyclerView;
    private ProbeDataAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getActivity().setTitle(getResources().getString(R.string.probe_form_header) + ": " + this.getActivity().getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION));
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        session = new SessionManager(getActivity().getApplicationContext());
        session.checkLogin();

        mUser = session.getCurrentUser();
        Log.d("UserName:", mUser.getUsername());

        currentDate = new Date();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_probe_form, container, false);

        final ProbeDao probeDao = ProbeDao.get(getActivity());
        String [] args = {this.getActivity().getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION)};
        mProbeDatas = probeDao.getProbeDatasByLocation(args);
        List<ProbeData> probeDatasFilteredByDates = new ArrayList<>();
        for(int i = 0; i < mProbeDatas.size(); i++) {
            if(currentDate.getMonth() == mProbeDatas.get(i).getDate().getMonth() && currentDate.getYear() == mProbeDatas.get(i).getDate().getYear() && currentDate.getDate() == mProbeDatas.get(i).getDate().getDate())
                probeDatasFilteredByDates.add(mProbeDatas.get(i));
        }
        mProbeDatas = probeDatasFilteredByDates;

        mCurrentLocation = (TextView) v.findViewById(R.id.location);
//        mCurrentLocation.setText(this.getActivity().getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION));

        mDateButton = (Button) v.findViewById(R.id.current_date_button);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(currentDate);
                dialog.setTargetFragment(ProbeFormFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mBarometricPressureField = (EditText)v.findViewById(R.id.barometric_field);
        if(mProbeDatas.size() != 0 && mProbeDatas.get(0).getBarometricPressure() != 0)
            mBarometricPressureField.setText(Double.toString(mProbeDatas.get(0).getBarometricPressure()));
        else
            mBarometricPressureField.setText(Double.toString(defaultBarometricPressure));

        mBarometricPressureButton = (Button) v.findViewById(R.id.barometric_button);
        mBarometricPressureButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                for(int i = 0; i < mProbeDatas.size(); i++) {
                    if (mBarometricPressureField.getText().toString().trim().length() == 0) {
                        mBarometricPressureField.setText("30.02");
                        mProbeDatas.get(i).setBarometricPressure(Double.parseDouble(mBarometricPressureField.getText().toString()));
                    }
                    else
                        mProbeDatas.get(i).setBarometricPressure(Double.parseDouble(mBarometricPressureField.getText().toString()));
                }
                probeDao.updateProbeDatas(mProbeDatas);
                Toast.makeText(getActivity(), R.string.updated_barometric_toast,Toast.LENGTH_SHORT).show();
            }
        });

        //TODO: Write code for the list of Inspectors involved

        mProbeDataRecyclerView = (RecyclerView) v.findViewById(R.id.probe_data_recycler_view);
        mProbeDataRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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
        inflater.inflate(R.menu.fragment_probe_form, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_probe:
                ProbeData probeData = new ProbeData();
                //Log.d("From FormFrag",getActivity().getIntent().getStringExtra(EXTRA_USERNAME));
                probeData.setLocation(this.getActivity().getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION));
                probeData.setInspectorName(mUser.getFullName());
                probeData.setInspectorUserName(mUser.getUsername());
                probeData.setDate(currentDate);

                if(mBarometricPressureField.getText().toString().trim().length() == 0)
                    probeData.setBarometricPressure(defaultBarometricPressure);
                else
                    probeData.setBarometricPressure(Double.parseDouble(mBarometricPressureField.getText().toString()));
                ProbeDao.get(getActivity()).addProbeData(probeData);
                Intent intent = ProbeDataPagerActivity.newIntent(getActivity(),probeData.getId());


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

        if (requestCode == REQUEST_DATE) {
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
        ProbeDao probeDao = ProbeDao.get(getActivity());
        String [] args = {this.getActivity().getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION)};
        mProbeDatas = probeDao.getProbeDatasByLocation(args);




        //TODO: Change this so it checks for date in the SQL query.         Issues: DB stores date as a long. So if we query by date, it might just look for entries w/ that exact time+date.
        List<ProbeData> probeDatasFilteredByDate = new ArrayList<>();
        for(int i = 0; i < mProbeDatas.size(); i++) {
            if(currentDate.getMonth() == mProbeDatas.get(i).getDate().getMonth() && currentDate.getYear() == mProbeDatas.get(i).getDate().getYear() && currentDate.getDate() == mProbeDatas.get(i).getDate().getDate())
                probeDatasFilteredByDate.add(mProbeDatas.get(i));
        }
        mProbeDatas = probeDatasFilteredByDate;

        if(mAdapter == null) {
            mAdapter = new ProbeDataAdapter(mProbeDatas);
            mProbeDataRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setProbeDatas(mProbeDatas);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void updateDate() {
        mDateButton.setText(DateFormat.format("EEEE, MMM d, yyyy",currentDate));
    }

    private void updateBarometricPressure() {
        if(mProbeDatas.size() != 0 && mProbeDatas.get(0).getBarometricPressure() != 0)
            mBarometricPressureField.setText(Double.toString(mProbeDatas.get(0).getBarometricPressure()));
        else
            mBarometricPressureField.setText(Double.toString(defaultBarometricPressure));
    }

    //For RecycleView
    private class ProbeDataHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ProbeData mProbeData;

        private TextView mProbeNumberView;
        private TextView mWaterPressureView;
        private TextView mMethanePercentageView;
        private Button mEditButton;
        private CardView mCardView;
        private TextView mDateView;


        public ProbeDataHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            mProbeNumberView = (TextView) itemView.findViewById(R.id.list_item_probe_data_probe_number_view);
            mWaterPressureView = (TextView) itemView.findViewById(R.id.list_item_probe_data_water_pressure_view);
            mMethanePercentageView = (TextView) itemView.findViewById(R.id.list_item_probe_data_methane_percentage_view);

            mDateView = (TextView) itemView.findViewById(R.id.list_item_probe_data_start_date_view);

            mCardView = (CardView)itemView.findViewById(R.id.probe_data_cv);
            mCardView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = ProbeDataPagerActivity.newIntent(getActivity(), mProbeData.getId());
                    startActivity(intent);
                }

            });
//            mEditButton = (Button)itemView.findViewById(R.id.list_item_probe_edit_button);
//            mEditButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = ProbeDataPagerActivity.newIntent(getActivity(), mProbeData.getId());
//                    startActivity(intent);
//                }
//            });
        }
        //<----------------------------------------------------------------------------
        //Color changing for methane and h2o occur here need values to be clarified to make appropriate changes
        public void bindProbeData(ProbeData probeData) {
            mProbeData = probeData;
            mProbeNumberView.setText(mProbeData.getProbeNumber());

            //2 sig figs for displayed data
            mWaterPressureView.setText(String.format("%.2f", mProbeData.getWaterPressure()));
            mMethanePercentageView.setText(String.format("%.2f", mProbeData.getMethanePercentage()));

//            mDateView.setText(DateFormat.format("yyyy-MM-dd", mProbeData.getStartDate()));
            //Set colors depending on ch4 level in RecyclerView
            //BELOW IS THE OG
//            if (mProbeData.getMethanePercentage() >= 5.0) {
//                mProbeNumberView.setTextColor(Color.RED);
//                mWaterPressureView.setTextColor(Color.RED);
//                mMethanePercentageView.setTextColor(Color.RED);
//            }
//            else if (mProbeData.getMethanePercentage() >= 0.1 && mProbeData.getMethanePercentage() < 4.9) {
//                mProbeNumberView.setTextColor(Color.rgb(255,165,0));
//                mWaterPressureView.setTextColor(Color.rgb(255,165,0));
//                mMethanePercentageView.setTextColor(Color.rgb(255,165,0));
//            }
//            else {
//                mProbeNumberView.setTextColor(Color.WHITE);
//                mWaterPressureView.setTextColor(Color.WHITE);
//                mMethanePercentageView.setTextColor(Color.WHITE);
//            }
            //END OF OG
            // START OF NEW
            if (mProbeData.getMethanePercentage() >= 5.0) {
                mMethanePercentageView.setTextColor(Color.RED);
            }
            else if (mProbeData.getMethanePercentage() >= 0.1 && mProbeData.getMethanePercentage() < 4.9) {
                mMethanePercentageView.setTextColor(Color.rgb(255,165,0));
            }
            else {
                mMethanePercentageView.setTextColor(Color.WHITE);
            }

            if (mProbeData.getWaterPressure() >= 1.0) {
                mWaterPressureView.setTextColor(Color.RED);
            }
            else if (mProbeData.getWaterPressure() >= 0.2 && mProbeData.getWaterPressure() < 0.9) {
                mWaterPressureView.setTextColor(Color.rgb(255,165,0));
            }
            else {
                mWaterPressureView.setTextColor(Color.WHITE);
            }
            //END OF NEW

//<<<<<<< HEAD
//=======
//                //mProbeNumberView.setTextColor(Color.RED);
//                //mWaterPressureView.setTextColor(Color.RED);
//                mMethanePercentageView.setTextColor(Color.RED);
//            }
//            else if (mProbeData.getMethanePercentage() >= 0.1 && mProbeData.getMethanePercentage() < 4.9) {
//               // mProbeNumberView.setTextColor(Color.rgb(255,165,0));
//                mWaterPressureView.setTextColor(Color.rgb(255,165,0));
//                mMethanePercentageView.setTextColor(Color.rgb(255,165,0));
//            }
//            else {
//               // mProbeNumberView.setTextColor(Color.WHITE);
//                //mWaterPressureView.setTextColor(Color.WHITE);
//                mMethanePercentageView.setTextColor(Color.WHITE);
//            }
//            if(mProbeData.getWaterPressure() >1.0){
//                mWaterPressureView.setTextColor(Color.RED);
//            }
//            else if(mProbeData.getWaterPressure() >= 0.2 && mProbeData.getWaterPressure() <= 0.9){
//                mWaterPressureView.setTextColor(Color.rgb(255,165, 0));
//            }
//            else
//                mWaterPressureView.setTextColor(Color.WHITE);
//>>>>>>> 083e3b555c682a1b2f033eb58d6af9d6640772c3
//=======
//>>>>>>> fc2c43e685f8daab4ede6cc388dcd4e5e652e7f5
        }

        @Override
        public void onClick(View v) {

        }

    }

    //list_item_instantaneous_data.xml
    private class ProbeDataAdapter extends RecyclerView.Adapter<ProbeDataHolder> {

        private List<ProbeData> mProbeDatas;
        private ProbeData d;

        public ProbeDataAdapter(List<ProbeData> probeDatas) {
            mProbeDatas = probeDatas;

        }

        @Override
        public ProbeDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_probe_data, parent, false);
            return new ProbeDataHolder(view);
        }

        @Override
        public void onBindViewHolder(ProbeDataHolder holder, int position) {
            ProbeData probeData = mProbeDatas.get(position);
            holder.bindProbeData(probeData);
        }

        @Override
        public int getItemCount() {
            return mProbeDatas.size();
        }

        public void setProbeDatas(List<ProbeData> probeDatas) {
            mProbeDatas = probeDatas;
        }
    }

}
