package com.landfilleforms.android.landfille_forms.activities_and_fragments.warmspot;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
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

import com.google.gson.internal.bind.ArrayTypeAdapter;
import com.landfilleforms.android.landfille_forms.activities_and_fragments.DatePickerFragment;
import com.landfilleforms.android.landfille_forms.R;
import com.landfilleforms.android.landfille_forms.database.dao.ImeDao;
import com.landfilleforms.android.landfille_forms.database.dao.InstrumentDao;
import com.landfilleforms.android.landfille_forms.database.dao.WarmSpotDao;
import com.landfilleforms.android.landfille_forms.model.Instrument;
import com.landfilleforms.android.landfille_forms.model.WarmSpotData;
import com.landfilleforms.android.landfille_forms.util.StringUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;

//Done?
public class WarmSpotDataFragment extends Fragment {
    private static final String TAG = "WarmspotDataFrag:";
    private static final String ARG_WARM_SPOT_DATA_ID = "warm_spot_data_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;

    //List<Instrument> mInstruments;
    Set<String> grids = new TreeSet<>();
    private WarmSpotData mWarmSpotData;
    private boolean newlyCreatedData;

    private TextView mLocationLabel;
    private Spinner mGridIdSpinner;//Text
    private TextView mInspectorLabel;
    private EditText mDescriptionField;//Text
    private EditText mEstimatedSizeField;//Number
    private EditText mMethaneLevelField;
    //private Spinner mInstrumentSpinner;
    private Button mDateButton;
    private Button mSubmitButton;

    private Button insert,delete;
    private TextView mGridList;
    private StringBuilder gridBuilder = new StringBuilder();

    private static int selectedPosition = 0;
    private TextView mInstrumentLabel;
    private Spinner mInstrumentSpinner;
    private List<Instrument> mInstrumentList;


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

        mInstrumentList = InstrumentDao.get(getActivity()).getInstruments().stream()
                .filter(i -> i.getInstrumentType().isInstantaneous())
                .collect(Collectors.toList());

        UUID warmspotDataId = (UUID) getArguments().getSerializable(ARG_WARM_SPOT_DATA_ID);
        mWarmSpotData = WarmSpotDao.get(getActivity()).getWarmSpotData(warmspotDataId);
        if(mWarmSpotData.getGrids() == null)
            newlyCreatedData = true;
        else
            newlyCreatedData = false;

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
        //mInstruments = InstrumentDao.get(getActivity()).getInstrumentsBySiteForSurface(mWarmSpotData.getLocation());

        mInstrumentSpinner = (Spinner)v.findViewById(R.id.warm_spinner);

        ArrayAdapter<Instrument> instrumentArrayAdapter= new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, mInstrumentList);
        instrumentArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mInstrumentSpinner.setAdapter(instrumentArrayAdapter);
        selectedPosition = mInstrumentSpinner.getSelectedItemPosition();

        int position = 0;
        int index = 0;
        for(Instrument instrument: this.mInstrumentList){
            if(instrument.getId() == mWarmSpotData.getInstrument()){
                position = index;
                break;
            }
            index++;
        }
        mInstrumentSpinner.setSelection(position);


        mInstrumentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPosition = position;
                mInstrumentSpinner.setSelection(selectedPosition);

                Object o = parent.getItemAtPosition(position);
                if(o instanceof Instrument){
                    mWarmSpotData.setInstrument(((Instrument)parent.getItemAtPosition(position)).getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //sample setting up for early button use
        mSubmitButton = (Button)v.findViewById(R.id.submit);

        mInspectorLabel = (TextView)v.findViewById(R.id.inspector_name);
        mInspectorLabel.setText(mWarmSpotData.getInspectorFullName());

        insert = (Button) v.findViewById(R.id.add);
        delete = (Button) v.findViewById(R.id.remove);
        mGridList = (TextView) v.findViewById(R.id.gridList);

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

            //gives a red background for validation when its wrong and green if its good to go
            @Override
            public void afterTextChanged(Editable s) {
                if (mWarmSpotData.getMaxMethaneReading() < 200 || mWarmSpotData.getMaxMethaneReading() > 499) {
                    mMethaneLevelField.setBackgroundColor(Color.RED);
                    mSubmitButton.setEnabled(false);
                    //mSubmitButton.setBackgroundColor(Color.rgb(135,133,128));

                } else {
                    mMethaneLevelField.setBackgroundColor(Color.GREEN);
                    mSubmitButton.setEnabled(true);
                    //changes background color for button will find a better way to validate 'maybe'
                    //goes with line 206
//                    mSubmitButton.setBackgroundColor(Color.parseColor("#204f5e"));
                }
            }
        });

        mLocationLabel = (TextView) v.findViewById(R.id.location);
        mLocationLabel.setText(mWarmSpotData.getLocation());

        String[] gridArray = null;
        if (mWarmSpotData.getGrids() != null) {
            gridArray = mWarmSpotData.getGrids().split(", ");
            grids.addAll(Arrays.asList(gridArray));
            updateGridListString();
        }

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
        mGridIdSpinner.setSelection(gridArray != null ? adapter.getPosition(gridArray[gridArray.length - 1]) : 0);

        mGridIdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Do nothing.
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGrid();
            }
        } );
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeGrid();
            }
        });


        /*mInstrumentSpinner = (Spinner) v.findViewById(R.id.instrument_serial_no_spinner);
        ArrayAdapter<Instrument> instrumentAdapter = new ArrayAdapter<Instrument>(this.getActivity(), R.layout.dark_spinner_layout, mInstruments);
        mInstrumentSpinner.setAdapter(instrumentAdapter);
        mInstrumentSpinner.setSelection(instrumentAdapter.getPosition(mWarmSpotData.getInstrument()));
        mInstrumentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {*/
            /*@Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mWarmSpotData.setInstrument((Instrument)parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });*/



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


        mSubmitButton.setText(R.string.submit_button_label);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(mWarmSpotData.getMaxMethaneReading() < 200 || mWarmSpotData.getMaxMethaneReading() >=500) {
                    Toast.makeText(getActivity(), R.string.improper_methane_warmspot_toast, Toast.LENGTH_SHORT).show();

                }
                else if(grids.isEmpty()){
                    Toast.makeText(getActivity(), "Do not leave the grids empty.", Toast.LENGTH_LONG).show();
                }
                else {

                    mWarmSpotData.setGrids(StringUtils.collectionToSortedString(grids, ", "));

                    getActivity().finish();
                    Toast.makeText(getActivity(), R.string.warmspot_added_toast, Toast.LENGTH_SHORT).show();
                }
            }
        });

        //TODO: fix this
        //This deletes newly created entries if you back out but I don't think this is a good solution
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i(TAG, "keyCode: " + keyCode);
                if( keyCode == KeyEvent.KEYCODE_BACK ) {
                    System.out.println(mWarmSpotData);
                    if(newlyCreatedData) {
                        WarmSpotDao.get(getActivity()).removeWarmSpotData(mWarmSpotData);
                        Toast.makeText(getActivity(), R.string.new_ime_cancelation_toast, Toast.LENGTH_SHORT).show();

                        //NEW
                        //ADDING THIS THIS TO WARN A DELETE
//                          AlertDialog.Builder alertBuilder =  new AlertDialog.Builder(getActivity());
//                        dialogDeleteWarmspotEntry(alertBuilder);
//                        Instead of this
//                        WarmSpotDao.get(getActivity()).removeWarmSpotData(mWarmSpotData);
//                        Toast.makeText(getActivity(), R.string.new_warmspot_cancelation_toast, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getActivity(), R.string.unsaved_changes_discarded_toast, Toast.LENGTH_SHORT).show();
                    }
                    getActivity().finish();
                    return true;
                } else {
                    return false;
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

    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void halt(AlertDialog.Builder alertBuilder) {
        alertBuilder.setMessage("You are leaving fields blank!\n If you would like to save hit submit.")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog deleteAlert = alertBuilder.create();
        deleteAlert.setTitle("Active Data");
        deleteAlert.show();
    }

    private void updateGridListString() {
        mGridList.setText(StringUtils.collectionToSortedString(grids, ", "));
    }

    private void addGrid(){
        grids.add(String.valueOf(mGridIdSpinner.getSelectedItem()));
        updateGridListString();
    }

    private void removeGrid(){
        grids.remove(String.valueOf(mGridIdSpinner.getSelectedItem()));
        updateGridListString();
    }

}
