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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.landfilleforms.android.landfille_forms.DatePickerFragment;
import com.landfilleforms.android.landfille_forms.R;
import com.landfilleforms.android.landfille_forms.database.dao.WarmSpotDao;
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
    private Spinner mGridIdSpinner;//Text
    private TextView mInspectorLabel;
    private EditText mDescriptionField;//Text
    private EditText mEstimatedSizeField;//Number
    private EditText mMethaneLevelField;
    private Spinner mInstrumentSerialNoSpinner;
    private Button mDateButton;
    private Button mSubmitButton;



    public static WarmSpotDataFragment newInstance(UUID warmspotDataId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_WARM_SPOT_DATA_ID, warmspotDataId);
        WarmSpotDataFragment fragment = new WarmSpotDataFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID warmspotDataId = (UUID) getArguments().getSerializable(ARG_WARM_SPOT_DATA_ID);
        mWarmSpotData = WarmSpotDao.get(getActivity()).getWarmSpotData(warmspotDataId);

        setHasOptionsMenu(true);
    }

    @Override
    public void onPause() {
        super.onPause();

        WarmSpotDao.get(getActivity()).updateWarmSpotData(mWarmSpotData);
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
                if (s=="" || count == 0 || s.toString().equals(".")) mWarmSpotData.setMaxMethaneReading(0);
                else mWarmSpotData.setMaxMethaneReading(Double.parseDouble(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mLocationLabel = (TextView) v.findViewById(R.id.location);
        mLocationLabel.setText(mWarmSpotData.getLocation());

        //TODO: Create a grid table in the DB and use that instead.
        mGridIdSpinner = (Spinner)v.findViewById(R.id.grid_id);
        ArrayAdapter<CharSequence> adapter;
        switch(mWarmSpotData.getLocation()) {
            case "Bishops":
                adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.bishops_grid, R.layout.dark_spinner_layout);
                break;
            case "Gaffey":
                adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.gaffey_grid, R.layout.dark_spinner_layout);
                break;
            case "Lopez":
                adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.lopez_grid, R.layout.dark_spinner_layout);
                break;
            case "Sheldon":
                adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.sheldon_grid, R.layout.dark_spinner_layout);
                break;
            case "Toyon":
                adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.toyon_grid, R.layout.dark_spinner_layout);
                break;
            default:
                adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.empty_array, R.layout.dark_spinner_layout);;
        }
        mGridIdSpinner.setAdapter(adapter);
        mGridIdSpinner.setSelection(adapter.getPosition(mWarmSpotData.getGridId()));



        mGridIdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mWarmSpotData.setGridId(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mInstrumentSerialNoSpinner = (Spinner) v.findViewById(R.id.instrument_serial_no_spinner);
        ArrayAdapter<CharSequence> instrumentSerialAdapter;
        instrumentSerialAdapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.instantaneous_integrated_probe, R.layout.dark_spinner_layout);
        mInstrumentSerialNoSpinner.setAdapter(instrumentSerialAdapter);
        mInstrumentSerialNoSpinner.setSelection(instrumentSerialAdapter.getPosition(mWarmSpotData.getInstrumentSerial()));

        mInstrumentSerialNoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mWarmSpotData.setInstrumentSerial(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
        mEstimatedSizeField.setText(mWarmSpotData.getEstimatedSize());
        mEstimatedSizeField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mWarmSpotData.setEstimatedSize(s.toString());
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
                if(mWarmSpotData.getMaxMethaneReading() < 200 || mWarmSpotData.getMaxMethaneReading() >=500) {
                    Toast.makeText(getActivity(), R.string.improper_methane_warmspot_toast, Toast.LENGTH_SHORT).show();
                }
                else {
                    getActivity().finish();
                    Toast.makeText(getActivity(), R.string.warmspot_added_toast, Toast.LENGTH_SHORT).show();
                }
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
                        WarmSpotDao.get(getActivity()).removeWarmSpotData(mWarmSpotData);
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
