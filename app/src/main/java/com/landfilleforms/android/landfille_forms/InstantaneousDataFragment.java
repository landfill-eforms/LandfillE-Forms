package com.landfilleforms.android.landfille_forms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Work on 11/4/2016.
 */

public class InstantaneousDataFragment extends Fragment {
    private static final String EXTRA_USERNAME = "com.landfilleforms.android.landfille_forms.username";

    public String[] locations = new String[]{"Bishops","Gaffey","Lopez","Sheldon","Toyon"};//Will be replaced

    private static final String ARG_INSTANTANEOUS_DATA_ID = "instantaneous_data_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;
    private static final String DIALOG_TIME = "DialogTime";
    private static final int REQUEST_START_TIME = 1;
    private static final int REQUEST_END_TIME = 2;

    private InstantaneousData mInstantaneousData;
    private TextView mInspectorLabel;
    private EditText mGridIdField;
    private EditText mMethaneLevelField;
    private Button mStartDateButton;
    private Button mStartTimeButton;
    private Button mEndTimeButton;
    private Button mSubmitButton;
    private EditText imeField;
    private TextView mLocationLabel;

    public static InstantaneousDataFragment newInstance(UUID instantaneousDataId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_INSTANTANEOUS_DATA_ID, instantaneousDataId);

        InstantaneousDataFragment fragment = new InstantaneousDataFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID instantaneousDataId = (UUID) getArguments().getSerializable(ARG_INSTANTANEOUS_DATA_ID);
        mInstantaneousData = InstantaneousForm.get(getActivity()).getInstantaneousData(instantaneousDataId);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPause() {
        super.onPause();

        InstantaneousForm.get(getActivity()).updateInstantanteousData(mInstantaneousData);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_instantaneous_data, menu);
    }

    //Toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_delete_instantaneous_data:
                InstantaneousForm.get(getActivity()).removeInstantaneousData(mInstantaneousData);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_instantaneous_data, container, false);

        mInspectorLabel = (TextView)v.findViewById(R.id.inspector_name);
        mInspectorLabel.setText(mInstantaneousData.getInspectorName());

        mMethaneLevelField = (EditText)v.findViewById(R.id.methane_reading);
        mMethaneLevelField.setText(Double.toString(mInstantaneousData.getMethaneReading()));
        mMethaneLevelField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s=="" || count == 0) mInstantaneousData.setMethaneReading(0);
                else mInstantaneousData.setMethaneReading(Double.parseDouble(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mLocationLabel = (TextView) v.findViewById(R.id.location);
        mLocationLabel.setText(mInstantaneousData.getLandFillLocation());
        
        mGridIdField = (EditText)v.findViewById(R.id.grid_id);
        mGridIdField.setText(mInstantaneousData.getGridId());
        mGridIdField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mInstantaneousData.setGridId(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        imeField = (EditText)v.findViewById(R.id.ime_field);
        imeField.setText(mInstantaneousData.getImeNumber());
        imeField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mInstantaneousData.setImeNumber(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mStartDateButton = (Button)v.findViewById(R.id.start_date);
        updateDate();
        mStartDateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mInstantaneousData.getStartDate());
                dialog.setTargetFragment(InstantaneousDataFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mStartTimeButton = (Button)v.findViewById(R.id.start_time);
        updateStartTime();
        mStartTimeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance(mInstantaneousData.getStartDate());
                dialog.setTargetFragment(InstantaneousDataFragment.this, REQUEST_START_TIME);
                dialog.show(manager, DIALOG_TIME);
            }
        });

        mEndTimeButton = (Button)v.findViewById(R.id.end_time);
        updateEndTime();
        mEndTimeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance(mInstantaneousData.getEndDate());
                dialog.setTargetFragment(InstantaneousDataFragment.this, REQUEST_END_TIME);
                dialog.show(manager, DIALOG_TIME);
            }
        });

        mSubmitButton = (Button)v.findViewById(R.id.submit);
        mSubmitButton.setText(R.string.submit_button_label);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                getActivity().finish();
            }
        });


        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {//Accidentally setting this as (resultCode == REQUEST_DATE) cost me 30 min of debug time orz
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mInstantaneousData.setStartDate(date);
            mInstantaneousData.setEndDate(date);
            updateDate();
            updateStartTime();
            updateEndTime();
        }

        else if (requestCode == REQUEST_START_TIME) {//Accidentally setting this as (resultCode == REQUEST_DATE) cost me 30 min of debug time orz
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mInstantaneousData.setStartDate(date);
            updateStartTime();
        }

        else if (requestCode == REQUEST_END_TIME) {//Accidentally setting this as (resultCode == REQUEST_DATE) cost me 30 min of debug time orz
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mInstantaneousData.setEndDate(date);
            updateEndTime();
        }
    }

    private void updateDate() {
        mStartDateButton.setText(DateFormat.format("EEEE, MMM d, yyyy",mInstantaneousData.getStartDate()));
    }

    private void updateStartTime() {
        mStartTimeButton.setText(DateFormat.format("HH:mm",mInstantaneousData.getStartDate()));
    }

    private void updateEndTime() {
        mEndTimeButton.setText(DateFormat.format("HH:mm",mInstantaneousData.getEndDate()));
    }


}
