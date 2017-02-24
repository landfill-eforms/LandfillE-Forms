package com.landfilleforms.android.landfille_forms.warmspot;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.landfilleforms.android.landfille_forms.DatePickerFragment;
import com.landfilleforms.android.landfille_forms.R;
import com.landfilleforms.android.landfille_forms.model.WarmSpotData;

import java.util.Date;
import java.util.UUID;

//Done?
public class WarmSpotDataFragment extends Fragment {
    private static final String ARG_WARM_SPOT_DATA_ID = "warm_spot_data_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;

    private WarmSpotData mWarmSpotData;

    private TextView mLocationLabel;
    private EditText mGridIdField;//Text
    private TextView mInspectorLabel;
    private EditText mDescriptionField;//Text
    private EditText mEstimatedSizeField;//Number
    private EditText mMethaneLevelField;
    private Button mDateButton;
    private Button mSubmitButton;



    public static WarmSpotDataFragment newInstance(UUID imeDataId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_WARM_SPOT_DATA_ID, imeDataId);
        WarmSpotDataFragment fragment = new WarmSpotDataFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID imeDataId = (UUID) getArguments().getSerializable(ARG_WARM_SPOT_DATA_ID);
        mWarmSpotData = WarmSpotForm.get(getActivity()).getWarmSpotData(imeDataId);

        setHasOptionsMenu(true);
    }

    @Override
    public void onPause() {
        super.onPause();

        WarmSpotForm.get(getActivity()).updateWarmSpotData(mWarmSpotData);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_warm_spot_data, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_delete_warm_spot_data:
            AlertDialog.Builder alertBuilder =  new AlertDialog.Builder(getActivity());
            dialogDeleteWarmspotEntry(alertBuilder);
            return true;
           default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_warm_spot_data, container, false);


        mInspectorLabel = (TextView)v.findViewById(R.id.inspector_name);
        mInspectorLabel.setText(mWarmSpotData.getInspectorFullName());

        mMethaneLevelField = (EditText)v.findViewById(R.id.methane_reading);
        mMethaneLevelField.setText(Double.toString(mWarmSpotData.getMaxMethaneReading()));
        mMethaneLevelField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s=="" || count == 0) mWarmSpotData.setMaxMethaneReading(0);
                else mWarmSpotData.setMaxMethaneReading(Double.parseDouble(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mLocationLabel = (TextView) v.findViewById(R.id.location);
        mLocationLabel.setText(mWarmSpotData.getLocation());

        mGridIdField = (EditText)v.findViewById(R.id.grid_id);
        mGridIdField.setText(mWarmSpotData.getGridId());
        mGridIdField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mWarmSpotData.setGridId(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDescriptionField = (EditText)v.findViewById(R.id.description);
        mDescriptionField.setText(mWarmSpotData.getDescription());
        mDescriptionField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mWarmSpotData.setDescription(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mEstimatedSizeField = (EditText)v.findViewById(R.id.estimated_size);
        mEstimatedSizeField.setText(Double.toString(mWarmSpotData.getEstimatedSize()));
        mEstimatedSizeField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s=="" || count == 0) mWarmSpotData.setEstimatedSize(0);
                else mWarmSpotData.setEstimatedSize(Double.parseDouble(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDateButton = (Button)v.findViewById(R.id.date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mWarmSpotData.getDate());
                dialog.setTargetFragment(WarmSpotDataFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });


        mSubmitButton = (Button)v.findViewById(R.id.submit);
        mSubmitButton.setText(R.string.submit_button_label);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                getActivity().finish();
                Toast.makeText(getActivity(), R.string.warmspot_added_toast, Toast.LENGTH_SHORT).show();
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
            mWarmSpotData.setDate(date);
            updateDate();
        }

    }

    private void updateDate() {
        mDateButton.setText(DateFormat.format("EEEE, MMM d, yyyy",mWarmSpotData.getDate()));
    }

    private void dialogDeleteWarmspotEntry(AlertDialog.Builder alertBuilder) {
        alertBuilder.setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        WarmSpotForm.get(getActivity()).removeWarmSpotData(mWarmSpotData);
                        getActivity().finish();
                        Toast.makeText(getActivity(), R.string.entry_deleted_toast, Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog deleteAlert = alertBuilder.create();
        deleteAlert.setTitle("Delete Warmspot Entry");
        deleteAlert.show();
    }

}
