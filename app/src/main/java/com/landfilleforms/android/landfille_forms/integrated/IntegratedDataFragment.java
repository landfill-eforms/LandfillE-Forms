package com.landfilleforms.android.landfille_forms.integrated;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
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
import com.landfilleforms.android.landfille_forms.SessionManager;
import com.landfilleforms.android.landfille_forms.TimePickerFragment;
import com.landfilleforms.android.landfille_forms.database.Site;
import com.landfilleforms.android.landfille_forms.database.dao.IntegratedDao;
import com.landfilleforms.android.landfille_forms.model.IntegratedData;
import com.landfilleforms.android.landfille_forms.model.User;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Work on 3/28/2017.
 */

//TODO: Finish this
public class IntegratedDataFragment extends Fragment {
    private static final String EXTRA_LANDFILL_LOCATION = "com.landfilleforms.android.landfille_forms.landfill_location";
    private static final String ARG_INTEGRATED_DATA_ID = "integrated_data_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;
    private static final String DIALOG_TIME = "DialogTime";
    private static final int REQUEST_START_TIME = 1;
    private static final int REQUEST_END_TIME = 2;

    private double tempMethaneLevel;//For the dialogs
    private IntegratedData mIntegratedData;

    private TextView mInspectorField;
    private TextView mLocationField;
    private Spinner mGridIdSpinner;
    private TextView mStartDateField;
    private TextView mSampleIdField;
    private EditText mBagNumberField;
    private TextView mBaroLevelField;
    private Button mStartDateButton;
    private Button mStartTimeButton;
    private Button mEndTimeButton;
    private EditText mMethaneLevelField;
    private EditText mVolumeField;
    private Button mSubmitButton;



    private Spinner mInstrumentSerialNoSpinner;

    //chris added this
    private User mUser;
    private SessionManager session;

    public static IntegratedDataFragment newInstance(UUID integratedDataId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_INTEGRATED_DATA_ID, integratedDataId);
        IntegratedDataFragment fragment = new IntegratedDataFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID integratedDataId = (UUID) getArguments().getSerializable(ARG_INTEGRATED_DATA_ID);
        mIntegratedData = IntegratedDao.get(getActivity()).getIntegratedData(integratedDataId);

        setHasOptionsMenu(true);

        //chris added this
        session = new SessionManager(getActivity().getApplicationContext());
        session.checkLogin();

        HashMap<String,String> currentUser = session.getUserDetails();
        mUser = new User();
        mUser.setUsername(currentUser.get(SessionManager.KEY_USERNAME));
        Log.d("UserName:", mUser.getUsername());
        mUser.setFullName(currentUser.get(SessionManager.KEY_USERFULLNAME));
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_integrated_data, menu);
    }

    //Toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_delete_integrated_data:
                //chris modified this
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                dialogDeleteIntegratedEntry(alertBuilder);
//                InstantaneousDao.get(getActivity()).removeInstantaneousData(mInstantaneousData);
//                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_integrated_data, container, false);

        mInspectorField = (TextView)v.findViewById(R.id.inspector_name);
        mInspectorField.setText(mIntegratedData.getInspectorName());

        mLocationField = (TextView) v.findViewById(R.id.location);
        mLocationField.setText(mIntegratedData.getLocation());

        mStartDateField = (TextView)v.findViewById(R.id.date_label);
        mStartDateField.setText(DateFormat.format("EEEE, MMM d, yyyy",mIntegratedData.getStartDate()));

        mBaroLevelField = (TextView)v.findViewById(R.id.baro_reading);
        if(mIntegratedData.getBarometricPressure() != 0)
            mBaroLevelField.setText(Double.toString(mIntegratedData.getBarometricPressure()));

        mSampleIdField = (TextView) v.findViewById(R.id.sample_id_field);
        //mSampleIdField.setText(mIntegratedData.getSampleId());

        mBagNumberField = (EditText) v.findViewById(R.id.bag_number_field);
        if(mIntegratedData.getBagNumber() != 0)
            mBagNumberField.setText(Integer.toString(mIntegratedData.getBagNumber()));
        mBagNumberField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s=="" || count == 0) mIntegratedData.setBagNumber(0);
                else mIntegratedData.setBagNumber(Integer.parseInt(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mMethaneLevelField = (EditText)v.findViewById(R.id.methane_reading);
        if(mIntegratedData.getMethaneReading() != 0)
            mMethaneLevelField.setText(Double.toString(mIntegratedData.getMethaneReading()));
        mMethaneLevelField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s=="" || count == 0) mIntegratedData.setMethaneReading(0);
                else mIntegratedData.setMethaneReading(Double.parseDouble(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mVolumeField = (EditText)v.findViewById(R.id.volume_reading);
        if(mIntegratedData.getVolumeReading() != 0)
            mVolumeField.setText(Integer.toString(mIntegratedData.getVolumeReading()));
        mVolumeField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s=="" || count == 0 || s.toString().equals(".")) mIntegratedData.setVolumeReading(0);
                else mIntegratedData.setVolumeReading(Integer.parseInt(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //TODO: Create a grid table in the DB and use that instead. UPDATE: I think we can use Alvin's Enum rather than having the Site names be stored in a .xml file.
        mGridIdSpinner = (Spinner)v.findViewById(R.id.grid_id);
        ArrayAdapter<CharSequence> adapter;
        //TODO: Ask Alvin if it's possible to use his Enums for switch/case. While the Enum names are constant, I don't think the String properties of that Enum are.
        switch(mIntegratedData.getLocation()) {
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
        mGridIdSpinner.setSelection(adapter.getPosition(mIntegratedData.getGridId()));



        mGridIdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mIntegratedData.setGridId(parent.getItemAtPosition(position).toString());
                mIntegratedData.setSampleId(generateSampleId(mIntegratedData.getLocation(),mIntegratedData.getStartDate(),mIntegratedData.getGridId()));
                updateTextViews();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        mStartTimeButton = (Button)v.findViewById(R.id.start_time);
        updateStartTime();
        mStartTimeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance((Date)mIntegratedData.getStartDate().clone());
                dialog.setTargetFragment(IntegratedDataFragment.this, REQUEST_START_TIME);
                dialog.show(manager, DIALOG_TIME);
            }
        });

        mEndTimeButton = (Button)v.findViewById(R.id.end_time);
        updateEndTime();
        mEndTimeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance((Date)mIntegratedData.getEndDate().clone());
                dialog.setTargetFragment(IntegratedDataFragment.this, REQUEST_END_TIME);
                dialog.show(manager, DIALOG_TIME);
            }
        });

        mSubmitButton = (Button)v.findViewById(R.id.submit);
        mSubmitButton.setText(R.string.submit_button_label);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //create a new alert dialog
                //Compare methane level of pre-existing entry in the DB
                tempMethaneLevel = mIntegratedData.getMethaneReading();
                IntegratedDao integratedDao = IntegratedDao.get(getActivity());
                IntegratedData originalEntry = integratedDao.getIntegratedData(mIntegratedData.getId());
                if(mIntegratedData.getMethaneReading() < originalEntry.getMethaneReading()){
                    mIntegratedData.setMethaneReading(originalEntry.getMethaneReading());
                }
                integratedDao.get(getActivity()).updateIntegratedData(mIntegratedData);

                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                //System.out.println(mInstantaneousData.getMethaneReading());
                //case where ch4 is over 500, indicated as an IME

                if (tempMethaneLevel == 0){
                    dialogDeleteIntegratedEntry(alertBuilder);

                }
                else{
                    //use this after all other ones are working
                    getActivity().finish();
                    //unsure about implementing text after submitting for instantaneous form
                    //Toast.makeText(getActivity(), R.string.instantaneous_added_toast, Toast.LENGTH_SHORT).show();
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
            mIntegratedData.setStartDate(date);
            mIntegratedData.setEndDate(date);
            updateDate();
            updateStartTime();
            updateEndTime();
        }

        else if (requestCode == REQUEST_START_TIME) {
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            if(mIntegratedData.getEndDate().getTime() < date.getTime()){
                Toast.makeText(getActivity(), R.string.start_end_time_error_toast, Toast.LENGTH_SHORT).show();
            }
            else {
                mIntegratedData.setStartDate(date);
            }
            updateStartTime();
        }

        else if (requestCode == REQUEST_END_TIME) {
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            if(mIntegratedData.getStartDate().getTime() > date.getTime()){
                Toast.makeText(getActivity(), R.string.end_start_time_error_toast, Toast.LENGTH_SHORT).show();
            }
            else {
                mIntegratedData.setEndDate(date);
            }
            updateEndTime();
        }
    }

    private void updateDate() {
        mStartDateButton.setText(DateFormat.format("EEEE, MMM d, yyyy",mIntegratedData.getStartDate()));
    }

    private void updateStartTime() {
        mStartTimeButton.setText(DateFormat.format("HH:mm",mIntegratedData.getStartDate()));
    }

    private void updateEndTime() {
        mEndTimeButton.setText(DateFormat.format("HH:mm",mIntegratedData.getEndDate()));
    }

    private void updateTextViews() {
        mStartDateField.setText(DateFormat.format("EEEE, MMM d, yyyy",mIntegratedData.getStartDate()));
        mStartTimeButton.setText(DateFormat.format("HH:mm",mIntegratedData.getStartDate()));
        mEndTimeButton.setText(DateFormat.format("HH:mm",mIntegratedData.getEndDate()));
        mSampleIdField.setText(mIntegratedData.getSampleId());

    }


    private void dialogEmptyMethaneFieldIntegratedEntryCheck(AlertDialog.Builder alertBuilder) {
        alertBuilder.setMessage("This entry has no methane level. Delete this entry?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        IntegratedDao.get(getActivity()).removeIntegratedData(mIntegratedData);
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
        deleteAlert.setTitle("Delete Integrated Entry");
        deleteAlert.show();
    }

    private void dialogDeleteIntegratedEntry(AlertDialog.Builder alertBuilder) {
        alertBuilder.setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        IntegratedDao.get(getActivity()).removeIntegratedData(mIntegratedData);
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
        deleteAlert.setTitle("Delete Integrated Entry");
        deleteAlert.show();
    }

    private String generateSampleId(String currentSite, Date currentDate, String currentGrid) {
        StringBuilder sb = new StringBuilder();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH) + 1;        //For java's Calendar, January = 0
        int year = cal.get(Calendar.YEAR);

        if (currentSite.equals(Site.BISHOPS.getName()))
            sb.append(Site.BISHOPS.getShortName());
        else if (currentSite.equals(Site.GAFFEY.getName()))
            sb.append(Site.GAFFEY.getShortName());
        else if (currentSite.equals(Site.LOPEZ.getName()))
            sb.append(Site.LOPEZ.getShortName());
        else if (currentSite.equals(Site.SHELDON.getName()))
            sb.append(Site.SHELDON.getShortName());
        else if (currentSite.equals(Site.TOYON.getName()))
            sb.append(Site.TOYON.getShortName());

        sb.append("IS-");
        sb.append(currentGrid);
        sb.append("-");
        sb.append(Integer.toString(year).substring(2,4));
        if(month < 10)
            sb.append(0);
        sb.append(month);
        if(day < 10)
            sb.append(0);
        sb.append(day);

        return sb.toString();
    }
}
