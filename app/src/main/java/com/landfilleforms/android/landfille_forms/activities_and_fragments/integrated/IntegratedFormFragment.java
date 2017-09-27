package com.landfilleforms.android.landfille_forms.activities_and_fragments.integrated;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.landfilleforms.android.landfille_forms.activities_and_fragments.DatePickerFragment;
import com.landfilleforms.android.landfille_forms.R;
import com.landfilleforms.android.landfille_forms.util.SessionManager;
import com.landfilleforms.android.landfille_forms.database.dao.InstrumentDao;
import com.landfilleforms.android.landfille_forms.database.dao.IntegratedDao;
import com.landfilleforms.android.landfille_forms.model.Instrument;
import com.landfilleforms.android.landfille_forms.model.IntegratedData;
import com.landfilleforms.android.landfille_forms.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Work on 3/28/2017.
 */

public class IntegratedFormFragment extends Fragment {
    private static String TAG = "IntegratedFormFrag";
    private static final String EXTRA_LANDFILL_LOCATION = "com.landfilleforms.android.landfille_forms.landfill_location";
    private static final Double defaultBarometricPressure = 30.01;
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;

    private Date currentDate;
    private List<Instrument> mInstruments;

    private SessionManager session;
    private User mUser;

    List<IntegratedData> mIntegratedDatas;

    private TextView mCurrentLocation;

    private Button mStartDateButton;

    private EditText mBarometricPressureField;
    private Button mBarometricPressureButton;
    private Spinner mInstrumentSerialNoSpinner;
    private Button mInstrumentSerialNoButton;

    private RecyclerView mIntegratedDataRecyclerView;
    private IntegratedDataAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getActivity().setTitle(getResources().getString(R.string.integrated_form_header) + ": " + this.getActivity().getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION));
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_integrated_form, container, false);
        mInstruments = InstrumentDao.get(getActivity()).getInstrumentsBySiteForSurface(this.getActivity().getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION));

        final IntegratedDao integratedDao = IntegratedDao.get(getActivity());
        String [] args = {this.getActivity().getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION)};
        mIntegratedDatas = integratedDao.getIntegratedDatasByLocation(args);
        List<IntegratedData> integratedDatasFilteredByDate = new ArrayList<>();
        for(int i = 0; i < mIntegratedDatas.size(); i++) {
            if(currentDate.getMonth() == mIntegratedDatas.get(i).getStartDate().getMonth() && currentDate.getYear() == mIntegratedDatas.get(i).getStartDate().getYear() && currentDate.getDate() == mIntegratedDatas.get(i).getStartDate().getDate())
                integratedDatasFilteredByDate.add(mIntegratedDatas.get(i));
        }
        mIntegratedDatas = integratedDatasFilteredByDate;
        Log.d("filteredSize", Integer.toString(integratedDatasFilteredByDate.size()));

        mCurrentLocation = (TextView) v.findViewById(R.id.location);
        mCurrentLocation.setText(this.getActivity().getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION));

        mStartDateButton = (Button)v.findViewById(R.id.current_date);
        updateDate();
        mStartDateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(currentDate);
                dialog.setTargetFragment(IntegratedFormFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mBarometricPressureField = (EditText)v.findViewById(R.id.barometric_field);
        Log.d("iDatas.size():", Integer.toString(mIntegratedDatas.size()));
        if(mIntegratedDatas.size() != 0 && mIntegratedDatas.get(0).getBarometricPressure() != 0)
            mBarometricPressureField.setText(Double.toString(mIntegratedDatas.get(0).getBarometricPressure()));
        else
            mBarometricPressureField.setText(Double.toString(defaultBarometricPressure));


        mBarometricPressureButton = (Button) v.findViewById(R.id.barometric_button);
        mBarometricPressureButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                for(int i = 0; i < mIntegratedDatas.size(); i++) {
                    if (mBarometricPressureField.getText().toString().trim().length() == 0) {
                        mBarometricPressureField.setText("30.02");
                        mIntegratedDatas.get(i).setBarometricPressure(Double.parseDouble(mBarometricPressureField.getText().toString()));
                        Toast.makeText(getActivity(), R.string.blank_barometric_toast, Toast.LENGTH_SHORT).show();
                    }
                    else
                        mIntegratedDatas.get(i).setBarometricPressure(Double.parseDouble(mBarometricPressureField.getText().toString()));
                }
                integratedDao.updateIntegratedDatas(mIntegratedDatas);
                Toast.makeText(getActivity(), R.string.updated_barometric_toast,Toast.LENGTH_SHORT).show();
            }
        });

        mInstrumentSerialNoSpinner = (Spinner) v.findViewById(R.id.instrument_serial_no_spinner);
        ArrayAdapter<Instrument> instrumentAdapter = new ArrayAdapter<Instrument>(this.getActivity(), R.layout.dark_spinner_layout, mInstruments);
        mInstrumentSerialNoSpinner.setAdapter(instrumentAdapter);

        mInstrumentSerialNoButton = (Button) v.findViewById(R.id.instrument_serial_no_button);
        mInstrumentSerialNoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d("InstrumentSerialNo",(((Instrument) mInstrumentSerialNoSpinner.getItemAtPosition(mInstrumentSerialNoSpinner.getSelectedItemPosition())).getSerialNumber()));
                for(int i = 0; i < mIntegratedDatas.size(); i++) {
                    mIntegratedDatas.get(i).setInstrument((Instrument)mInstrumentSerialNoSpinner.getItemAtPosition(mInstrumentSerialNoSpinner.getSelectedItemPosition()));
                }
                integratedDao.updateIntegratedDatas(mIntegratedDatas);
                Toast.makeText(getActivity(), R.string.updated_instrument_ime_datas,Toast.LENGTH_SHORT).show();
            }
        });

        mIntegratedDataRecyclerView = (RecyclerView) v.findViewById(R.id.integrated_data_recycler_view);
        mIntegratedDataRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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
        inflater.inflate(R.menu.fragment_integrated_form, menu);
    }
    //changed timer from 30mins to 25mins
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_integrated:
                IntegratedData integratedData = new IntegratedData();
                integratedData.setLocation(this.getActivity().getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION));
                integratedData.setVolumeReading(9);
                integratedData.setInspectorName(mUser.getFullName());
                integratedData.setInspectorUserName(mUser.getUsername());
                integratedData.setStartDate(currentDate);
                integratedData.setEndDate(new Date(integratedData.getStartDate().getTime() + 1500000));
                integratedData.setInstrument((Instrument)mInstrumentSerialNoSpinner.getItemAtPosition(mInstrumentSerialNoSpinner.getSelectedItemPosition()));

                if(mBarometricPressureField.getText().toString().trim().length() == 0)
                    integratedData.setBarometricPressure(defaultBarometricPressure);
                else
                    integratedData.setBarometricPressure(Double.parseDouble(mBarometricPressureField.getText().toString()));
                //TODO: I commented this out since the entry shouldn't be added to the DB until submit is pressed.
                IntegratedDao.get(getActivity()).addIntegratedData(integratedData);
                Intent intent = IntegratedDataPagerActivity.newIntent(getActivity(),integratedData.getId());


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

    private void updateUI() {
        IntegratedDao integratedDao = IntegratedDao.get(getActivity());
        String [] args = {this.getActivity().getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION)};
        mIntegratedDatas = integratedDao.getIntegratedDatasByLocation(args);




        //TODO: Change this so it checks for date in the SQL query.         Issues: DB stores date as a long. So if we query by date, it might just look for entries w/ that exact time+date.
        List<IntegratedData> filteredByDateIntegratedDatas = new ArrayList<>();
        for(int i = 0; i < mIntegratedDatas.size(); i++) {
            if(currentDate.getMonth() == mIntegratedDatas.get(i).getStartDate().getMonth() && currentDate.getYear() == mIntegratedDatas.get(i).getStartDate().getYear() && currentDate.getDate() == mIntegratedDatas.get(i).getStartDate().getDate())
                filteredByDateIntegratedDatas.add(mIntegratedDatas.get(i));
        }
        mIntegratedDatas = filteredByDateIntegratedDatas;
        Log.d("filteredSize", Integer.toString(filteredByDateIntegratedDatas.size()));

        if(mAdapter == null) {
            mAdapter = new IntegratedDataAdapter(mIntegratedDatas);
            mIntegratedDataRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setIntegratedDatas(mIntegratedDatas);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void updateDate() {
        mStartDateButton.setText(DateFormat.format("EEEE, MMM d, yyyy",currentDate));
    }

    private void updateBarometricPressure() {
        if(mIntegratedDatas.size() != 0 && mIntegratedDatas.get(0).getBarometricPressure() != 0)
            mBarometricPressureField.setText(Double.toString(mIntegratedDatas.get(0).getBarometricPressure()));
        else
            mBarometricPressureField.setText(Double.toString(defaultBarometricPressure));
    }

    //For RecyclerView
    private class IntegratedDataHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private IntegratedData mIntegratedData;

        private TextView mGridIdView;
        private TextView mBagNumberView;
        private TextView mMethaneReadingView;
        private Button mEditButton;
        private CardView mCardView;


        public IntegratedDataHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            mGridIdView = (TextView) itemView.findViewById(R.id.list_item_integrated_data_gridid_view);
            mMethaneReadingView = (TextView) itemView.findViewById(R.id.list_item_integrated_data_methane_level_view);
            mBagNumberView = (TextView) itemView.findViewById(R.id.list_item_integrated_data_bag_number_view);

            mCardView = (CardView)itemView.findViewById(R.id.integrated_data_cv);
            mCardView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v){
                    Intent intent = IntegratedDataPagerActivity.newIntent(getActivity(), mIntegratedData.getId());
                    startActivity(intent);
                }

            });
//            mEditButton = (Button)itemView.findViewById(R.id.list_item_integrated_edit_button);
//            mEditButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = IntegratedDataPagerActivity.newIntent(getActivity(), mIntegratedData.getId());
//                    startActivity(intent);
//                }
//            });
        }

        public void bindIntegratedData(IntegratedData integratedData) {
            mIntegratedData = integratedData;
            mGridIdView.setText(mIntegratedData.getGridId());

            //convers to 2 sig figs updated comment
            mMethaneReadingView.setText(String.format("%.2f", mIntegratedData.getMethaneReading()));
            mBagNumberView.setText(Integer.toString(mIntegratedData.getBagNumber()));
            //Set colors depending on ch4 level in RecyclerView
            if (mIntegratedData.getMethaneReading() >= 25) {
                mGridIdView.setTextColor(Color.RED);
                mMethaneReadingView.setTextColor(Color.RED);
                mBagNumberView.setTextColor(Color.RED);
            }
            else if (mIntegratedData.getMethaneReading() >= 10 && mIntegratedData.getMethaneReading() < 25) {
                mGridIdView.setTextColor(Color.rgb(255,165,0));
                mMethaneReadingView.setTextColor(Color.rgb(255,165,0));
                mBagNumberView.setTextColor(Color.rgb(255,165,0));
            }
            else {
                mGridIdView.setTextColor(Color.GREEN);
                mMethaneReadingView.setTextColor(Color.GREEN);
                mBagNumberView.setTextColor(Color.GREEN);
            }
        }

        @Override
        public void onClick(View v) {
        }
    }

    private class IntegratedDataAdapter extends RecyclerView.Adapter<IntegratedDataHolder> {

        private List<IntegratedData> mIntegratedDatas;
        private IntegratedData d;

        public IntegratedDataAdapter(List<IntegratedData> integratedDatas) {
            mIntegratedDatas = integratedDatas;

        }

        @Override
        public IntegratedDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_integrated_data, parent, false);
            return new IntegratedDataHolder(view);
        }

        @Override
        public void onBindViewHolder(IntegratedDataHolder holder, int position) {
            IntegratedData integratedData = mIntegratedDatas.get(position);
            holder.bindIntegratedData(integratedData);
        }

        @Override
        public int getItemCount() {
            return mIntegratedDatas.size();
        }

        public void setIntegratedDatas(List<IntegratedData> integratedDatas) {
            mIntegratedDatas = integratedDatas;
        }
    }

}
