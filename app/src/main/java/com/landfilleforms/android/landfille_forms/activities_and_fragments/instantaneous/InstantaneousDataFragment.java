package com.landfilleforms.android.landfille_forms.activities_and_fragments.instantaneous;

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

import com.landfilleforms.android.landfille_forms.activities_and_fragments.DatePickerFragment;
import com.landfilleforms.android.landfille_forms.R;
import com.landfilleforms.android.landfille_forms.util.SessionManager;
import com.landfilleforms.android.landfille_forms.activities_and_fragments.TimePickerFragment;
import com.landfilleforms.android.landfille_forms.database.dao.ImeDao;
import com.landfilleforms.android.landfille_forms.database.dao.InstantaneousDao;
import com.landfilleforms.android.landfille_forms.database.dao.InstrumentDao;
import com.landfilleforms.android.landfille_forms.activities_and_fragments.ime.ImeDataPagerActivity;
import com.landfilleforms.android.landfille_forms.model.ImeData;
import com.landfilleforms.android.landfille_forms.model.InstantaneousData;
import com.landfilleforms.android.landfille_forms.model.Instrument;
import com.landfilleforms.android.landfille_forms.model.User;
import com.landfilleforms.android.landfille_forms.model.WarmSpotData;
import com.landfilleforms.android.landfille_forms.activities_and_fragments.warmspot.WarmSpotDataPagerActivity;
import com.landfilleforms.android.landfille_forms.database.dao.WarmSpotDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Work on 11/4/2016.
 */

public class InstantaneousDataFragment extends Fragment {
    private static final String TAG = "InstantaneousDataFrag:";
    private static final String EXTRA_LANDFILL_LOCATION = "com.landfilleforms.android.landfille_forms.landfill_location";
    private static final String ARG_INSTANTANEOUS_DATA_ID = "instantaneous_data_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;
    private static final String DIALOG_TIME = "DialogTime";
    private static final int REQUEST_START_TIME = 1;
    private static final int REQUEST_END_TIME = 2;

    List<Instrument> mInstruments;
    private double tempMethaneLevel;//For the dialogs
    private InstantaneousData mInstantaneousData;
    private TextView mInspectorLabel;
    private Spinner mGridIdSpinner;
    private EditText mMethaneLevelField;
    private TextView mBaroLevelField;
    private TextView mstartDateText;
    private Button mStartDateButton;
    private Button mStartTimeButton;
    private Button mEndTimeButton;
    private Button mSubmitButton;
    private EditText mInstrumentField;
    private TextView imeField;
    private TextView mLocationLabel;
    private Spinner mInstrumentSerialNoSpinner;
    private Spinner mInstrumentSpinner;
    private String mCurrentImeNumber;
    private boolean newlyCreatedData;

    //chris added this
    private User mUser;
    private SessionManager session;

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
        mInstantaneousData = InstantaneousDao.get(getActivity()).getInstantaneousData(instantaneousDataId);
        if(mInstantaneousData.getGridId() == null)
            newlyCreatedData = true;
        else
            newlyCreatedData = false;

        setHasOptionsMenu(true);

        //chris added this
        session = new SessionManager(getActivity().getApplicationContext());
        session.checkLogin();

        mUser = session.getCurrentUser();
        Log.d("UserName:", mUser.getUsername());
    }

    @Override
    public void onPause() {
        super.onPause();
        //TODO: It shouldn't update the instantaneous data on exit.
        //InstantaneousDao.get(getActivity()).updateInstantaneousData(mInstantaneousData);
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
                //chris modified this
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                dialogDeleteInstantaneousEntry(alertBuilder);
//                InstantaneousDao.get(getActivity()).removeInstantaneousData(mInstantaneousData);
//                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_instantaneous_data, container, false);
        mInstruments = InstrumentDao.get(getActivity()).getInstrumentsBySiteForSurface(mInstantaneousData.getLandFillLocation());
        Log.d("InstrumentsSize",Integer.toString(mInstruments.size()));
        for(Instrument i:mInstruments){
            Log.d("Instrument",Integer.toString(i.getId()));
        }


        mBaroLevelField = (TextView)v.findViewById(R.id.baro_reading);
        if(mInstantaneousData.getBarometricPressure() != 0)
            mBaroLevelField.setText(Double.toString(mInstantaneousData.getBarometricPressure()));


        mstartDateText = (TextView)v.findViewById(R.id.date_label);
        mstartDateText.setText(DateFormat.format("EEEE, MMM d, yyyy",mInstantaneousData.getStartDate()));

        mInspectorLabel = (TextView)v.findViewById(R.id.inspector_name);
        mInspectorLabel.setText(mInstantaneousData.getInspectorName());

        //between 0.1 - 70000, round before storing, if "1", 3 sig fig, else 2 sig fig
        mMethaneLevelField = (EditText)v.findViewById(R.id.methane_reading);
        if(mInstantaneousData.getMethaneReading() != 0)
            mMethaneLevelField.setText(Double.toString(mInstantaneousData.getMethaneReading()));
        mMethaneLevelField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.equals("") || count == 0||s.toString().equals(".")) mInstantaneousData.setMethaneReading(0);
                else mInstantaneousData.setMethaneReading(Double.parseDouble(s.toString()));
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mLocationLabel = (TextView) v.findViewById(R.id.location);
        mLocationLabel.setText(mInstantaneousData.getLandFillLocation());

        //TODO: Create a grid table in the DB and use that instead. UPDATE: I think we can use Alvin's Enum rather than having the Site names be stored in a .xml file.
        mGridIdSpinner = (Spinner)v.findViewById(R.id.grid_id);
        ArrayAdapter<CharSequence> gridIdAdapter;
        //TODO: Ask Alvin if it's possible to use his Enums for switch/case. While the Enum names are constant, I don't think the String properties of that Enum are.
        switch(mInstantaneousData.getLandFillLocation()) {
            case "Bishops":
                gridIdAdapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.bishops_grid, R.layout.dark_spinner_layout);
                break;
            case "Gaffey":
                gridIdAdapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.gaffey_grid, R.layout.dark_spinner_layout);
                break;
            case "Lopez":
                gridIdAdapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.lopez_grid, R.layout.dark_spinner_layout);
                break;
            case "Sheldon":
                gridIdAdapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.sheldon_grid, R.layout.dark_spinner_layout);
                break;
            case "Toyon":
                gridIdAdapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.toyon_grid, R.layout.dark_spinner_layout);
                break;
            default:
                gridIdAdapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.empty_array, R.layout.dark_spinner_layout);;
        }
        mGridIdSpinner.setAdapter(gridIdAdapter);
        mGridIdSpinner.setSelection(gridIdAdapter.getPosition(mInstantaneousData.getGridId()));



        mGridIdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mInstantaneousData.setGridId(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mInstrumentSpinner = (Spinner) v.findViewById(R.id.instrument_serial_no_spinner);
        ArrayAdapter<Instrument> instrumentAdapter = new ArrayAdapter<Instrument>(this.getActivity(), R.layout.dark_spinner_layout, mInstruments);
        mInstrumentSpinner.setAdapter(instrumentAdapter);
        mInstrumentSpinner.setSelection(instrumentAdapter.getPosition(mInstantaneousData.getInstrument()));
        mInstrumentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mInstantaneousData.setInstrument((Instrument)parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


/*        imeField = (TextView) v.findViewById(R.id.ime_field);
        imeField.setText(mInstantaneousData.getImeNumber());*/

//        mStartDateButton = (Button)v.findViewById(R.id.start_date);
//        updateDate();
//        mStartDateButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                FragmentManager manager = getActivity().getSupportFragmentManager();
//                DatePickerFragment dialog = DatePickerFragment.newInstance(mInstantaneousData.getStartDate());
//                dialog.setTargetFragment(InstantaneousDataFragment.this, REQUEST_DATE);
//                dialog.show(manager, DIALOG_DATE);
//            }
//        });

        mStartTimeButton = (Button)v.findViewById(R.id.start_time);
            updateStartTime();
        mStartTimeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance((Date)mInstantaneousData.getStartDate().clone());
                dialog.setTargetFragment(InstantaneousDataFragment.this, REQUEST_START_TIME);
                dialog.show(manager, DIALOG_TIME);
            }
        });

        mEndTimeButton = (Button)v.findViewById(R.id.end_time);
        updateEndTime();
        mEndTimeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance((Date)mInstantaneousData.getEndDate().clone());
                dialog.setTargetFragment(InstantaneousDataFragment.this, REQUEST_END_TIME);
                dialog.show(manager, DIALOG_TIME);
        }
        });

        mSubmitButton = (Button)v.findViewById(R.id.submit);
        mSubmitButton.setText(R.string.submit_button_label);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //create a new alert dialog
                //Compare methane level of pre-existing entry in the DB
                tempMethaneLevel = mInstantaneousData.getMethaneReading();
                InstantaneousDao instantaneousDao = InstantaneousDao.get(getActivity());
                InstantaneousData originalEntry = instantaneousDao.getInstantaneousData(mInstantaneousData.getId());
                if(mInstantaneousData.getMethaneReading() < originalEntry.getMethaneReading()){
                    mInstantaneousData.setMethaneReading(originalEntry.getMethaneReading());
                }
                instantaneousDao.get(getActivity()).updateInstantaneousData(mInstantaneousData);

                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                //System.out.println(mInstantaneousData.getMethaneReading());
                //case where ch4 is over 500, indicated as an IME

                if(isSameGridTimeConflict()) {
                    Toast.makeText(getActivity(),R.string.same_grid_time_conflict_toast,Toast.LENGTH_SHORT).show();
                    return;
                }

                if (tempMethaneLevel >= 500) {
                    dialogIME(alertBuilder);
                }
                //case where ch4 is between 200 and 499, indicated as a Warmspot
                else if (tempMethaneLevel >= 200 && tempMethaneLevel <=499) {
                    dialogWarmspot(alertBuilder);
                }
                else if (tempMethaneLevel == 0){
                    dialogDeleteInstantaneousEntry(alertBuilder);

                }
                else{
                    //use this after all other ones are working
                    getActivity().finish();
                    //unsure about implementing text after submitting for instantaneous form
                    //Toast.makeText(getActivity(), R.string.instantaneous_added_toast, Toast.LENGTH_SHORT).show();
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
                    if(newlyCreatedData) {
                        InstantaneousDao.get(getActivity()).removeInstantaneousData(mInstantaneousData);
                        Toast.makeText(getActivity(), R.string.new_instantaneous_cancelation_toast, Toast.LENGTH_SHORT).show();
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
            mInstantaneousData.setStartDate(date);
            mInstantaneousData.setEndDate(date);
            updateDate();
            updateStartTime();
            updateEndTime();
        }

        else if (requestCode == REQUEST_START_TIME) {
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            if(mInstantaneousData.getEndDate().getTime() < date.getTime()){
                Toast.makeText(getActivity(), R.string.start_end_time_error_toast, Toast.LENGTH_SHORT).show();
            }
            else {
                mInstantaneousData.setStartDate(date);
            }
            updateStartTime();
        }

        else if (requestCode == REQUEST_END_TIME) {
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            if(mInstantaneousData.getStartDate().getTime() > date.getTime()){
                Toast.makeText(getActivity(), R.string.end_start_time_error_toast, Toast.LENGTH_SHORT).show();
            }
            else {
                mInstantaneousData.setEndDate(date);
            }
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

    //dialog when there is an IME
    //TODO: Declining to add either the warmspot/IME dialog should take you back to the InstantaneousDao fragment or you'd just be stuck in the data entry forever
    //Grant: I removed dialog.cancel() and just made it end the activity instead.
    private void dialogIME(AlertDialog.Builder alertBuilder) {
        final AlertDialog.Builder redirectionAlert = new AlertDialog.Builder(getActivity());
        alertBuilder.setMessage("CH4 levels are over 500! There is an IME! Navigate to IME form?")
                .setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //comment this out temp
                //getActivity().finish();
                dialogIMENavigation(redirectionAlert);
                InstantaneousDao.get(getActivity()).updateInstantaneousData(mInstantaneousData);
                //later, need to redirect to appropriate location's IME list
                //Toast.makeText(getActivity(), R.string.coming_soon_toast, Toast.LENGTH_SHORT).show();

            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), R.string.instantaneous_added_toast, Toast.LENGTH_SHORT).show();
                getActivity().finish();
                //dialog.cancel();
            }
        });
        AlertDialog alert = alertBuilder.create();
        alert.setTitle("New IME");
        alert.show();
    }
    //TODO: Rewrite the way it adds an existing IME data & replace all the Strings w/ the ones from the resource folder
    //dialog to create a new IME form or add to an existing IME
    private void dialogIMENavigation(final AlertDialog.Builder redirectionAlert) {
        redirectionAlert.setMessage("Would you like to create a new IME or add to an existing one?")
                //set positive as "Add to Existing IME",
                .setCancelable(false).setPositiveButton("Add to Existing IME", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO: Should open up another dialog on click w/ a Spinner composed of all the IME#s for that month.(Or maybe just include every single IME# but that sounds like a bad idea.
                //Check if IME #s exist. If not show a toast saying that there are no available.
                final ImeDao imeDao = ImeDao.get(getActivity());
                String [] args = {mInstantaneousData.getLandFillLocation()};
                List<ImeData> imeDatas = imeDao.getImeDatasByLocation(args);
                if(imeDatas.size() == 0) {
                    //TODO: Create string resource
                    Toast.makeText(getActivity(),"No IME entries exist.",Toast.LENGTH_SHORT).show();
                }
                else {
                    final AlertDialog.Builder newRedirectionAlert = new AlertDialog.Builder(getActivity());
                    dialogExistingIme(newRedirectionAlert);
                }
            }//temp fix, set this to cancel to rearrange order of options
        }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                getActivity().finish();
                //dialog.cancel();
            }
        }).setNegativeButton("Create a new IME", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //make a new IME
                Log.d("Location:",mInstantaneousData.getLandFillLocation());
                String generatedImeNumber = ImeDao.get(getActivity()).generateIMEnumber(mInstantaneousData.getLandFillLocation(), mInstantaneousData.getStartDate());

                mInstantaneousData.setImeNumber(generatedImeNumber);//Don't think this line will edit the Instantaneous entry in the DB since the block that does that is in the submit
                ImeData imeData = new ImeData();
                imeData.setLocation(mInstantaneousData.getLandFillLocation());
                imeData.setDate(mInstantaneousData.getStartDate());
                imeData.setGridId(mInstantaneousData.getGridId());
                imeData.setImeNumber(mInstantaneousData.getImeNumber());
                imeData.setMethaneReading(tempMethaneLevel);
                imeData.setInspectorFullName(mUser.getFullName());
                imeData.setInspectorUserName(mUser.getUsername());
                ImeDao.get(getActivity()).addImeData(imeData);
                Intent navigateIMEForm = ImeDataPagerActivity.newIntent(getActivity(),imeData.getId());
                startActivity(navigateIMEForm);
                //Toast.makeText(getActivity(), R.string.ime_added_toast, Toast.LENGTH_SHORT).show();

            }
        });
        AlertDialog alert = redirectionAlert.create();
        alert.setTitle("IME Navigation");
        alert.show();

    }

    //TODO:
    private void dialogExistingIme(AlertDialog.Builder redirectionAlert) {

        redirectionAlert.setTitle("Add to existing IME");
        redirectionAlert.setMessage("Which existing IME would you like to use?").setCancelable(false).setPositiveButton("Generate IME", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String generatedImeNumber = ImeDao.get(getActivity()).generateIMEnumber(mInstantaneousData.getLandFillLocation(), mInstantaneousData.getStartDate());

                mInstantaneousData.setImeNumber(generatedImeNumber);//Don't think this line will edit the Instantaneous entry in the DB since the block that does that is in the submit
                ImeData imeData = new ImeData();
                imeData.setImeNumber(mCurrentImeNumber);
                imeData.setLocation(mInstantaneousData.getLandFillLocation());
                imeData.setDate(mInstantaneousData.getStartDate());
                imeData.setGridId(mInstantaneousData.getGridId());
                imeData.setMethaneReading(tempMethaneLevel);
                imeData.setInspectorFullName(mUser.getFullName());
                imeData.setInspectorUserName(mUser.getUsername());
                ImeDao.get(getActivity()).addImeData(imeData);
                Intent navigateIMEForm = ImeDataPagerActivity.newIntent(getActivity(),imeData.getId());
                startActivity(navigateIMEForm);
            }//temp fix, set this to cancel to rearrange order of options
        }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
                //dialog.cancel();
            }
        });
        redirectionAlert.setView(R.layout.dialog_add_to_existing_ime);
        Spinner existingImeSpinner;
        final ImeDao imeDao = ImeDao.get(getActivity());
        String [] args = {mInstantaneousData.getLandFillLocation()};
        List<ImeData> mImeDatas = imeDao.getImeDatasByLocation(args);
        //ImeNumbers for spinner
        Set<String> imeNumbers = new HashSet<String>();
        for(ImeData imeData: mImeDatas) {
            if(imeData.getImeNumber() != null && imeData.getImeNumber().trim().length() != 0)
                imeNumbers.add(imeData.getImeNumber());
        }
        existingImeSpinner = (Spinner) redirectionAlert.show().findViewById(R.id.existing_ime_spinner);
        List<String> imeNumbersList = new ArrayList<String>(imeNumbers);
        Collections.sort(imeNumbersList);
        ArrayAdapter<String> imeNumberSpinnerItems = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_list_item_1, imeNumbersList){
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {

                View v = super.getDropDownView(position, null, parent);

                parent.setVerticalScrollBarEnabled(false);
                return v;
            }
        };

        existingImeSpinner.setAdapter(imeNumberSpinnerItems);
        existingImeSpinner.setSelection(imeNumberSpinnerItems.getPosition(imeNumberSpinnerItems.getItem(0).toString()));
        existingImeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCurrentImeNumber = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mCurrentImeNumber = "";
            }
        });


    }

    private void dialogWarmspot(AlertDialog.Builder alertBuilder) {
        alertBuilder.setMessage("CH4 levels are between 200-499! There is a Warmspot. Navigate to Warmspot form?")
                .setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //later, need to redirect to appropriate location's IME list
                // Toast.makeText(getActivity(), R.string.coming_soon_toast, Toast.LENGTH_SHORT).show();
                WarmSpotData warmSpotData = new WarmSpotData();
                //Log.d("From FormFrag",getActivity().getIntent().getStringExtra(EXTRA_USERNAME));
                //temp default is lopez
                warmSpotData.setGridId(mInstantaneousData.getGridId());
                warmSpotData.setMaxMethaneReading(tempMethaneLevel);
                warmSpotData.setDate(mInstantaneousData.getStartDate());
                warmSpotData.setLocation(mInstantaneousData.getLandFillLocation());
                //TODO: related to instrument change
                //warmSpotData.setInstrument(mInstantaneousData.getInstrument());
                warmSpotData.setInspectorFullName(mUser.getFullName());
                warmSpotData.setInspectorUserName(mUser.getUsername());
                WarmSpotDao.get(getActivity()).addWarmSpotData(warmSpotData);
                //still need to pass through some data between warmspots
                Intent navigateWarmspotForm = WarmSpotDataPagerActivity.newIntent(getActivity(),warmSpotData.getId());
                startActivity(navigateWarmspotForm);
                //note: add this one to the warmspot form.
                //Toast.makeText(getActivity(), R.string.warmspot_added_toast, Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), R.string.instantaneous_added_toast, Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });
        AlertDialog alert = alertBuilder.create();
        alert.setTitle("New Warmspot");
        alert.show();

    }


    private void dialogEmptyMethaneFieldInstantaneousEntryCheck(AlertDialog.Builder alertBuilder) {
        alertBuilder.setMessage("This entry has no methane level. Delete this entry?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        InstantaneousDao.get(getActivity()).removeInstantaneousData(mInstantaneousData);
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
        deleteAlert.setTitle("Delete Instantaneous Entry");
        deleteAlert.show();
    }

    private void dialogDeleteInstantaneousEntry(AlertDialog.Builder alertBuilder) {
        alertBuilder.setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        InstantaneousDao.get(getActivity()).removeInstantaneousData(mInstantaneousData);
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
        deleteAlert.setTitle("Delete Instantaneous Entry");
        deleteAlert.show();
    }

    private boolean isSameGridTimeConflict() {
        String[] args = {mInstantaneousData.getLandFillLocation(),mInstantaneousData.getGridId()};
        List<InstantaneousData> instantaneousDatas = InstantaneousDao.get(this.getActivity()).getInstantaneousDatasByLocationGrid(args);

        for(int i = 0; i < instantaneousDatas.size(); i++) {
            if(instantaneousDatas.get(i).getId() != mInstantaneousData.getId()){
                long otherInstantaneousStartTime = instantaneousDatas.get(i).getStartDate().getTime();
                long otherInstantaneousEndTime = instantaneousDatas.get(i).getEndDate().getTime();
                long thisInstantaneousStartTime = mInstantaneousData.getStartDate().getTime();
                long thisInstantaneousEndTime = mInstantaneousData.getEndDate().getTime();
                //This function already assumes that an Instantaneous Entry's startTime is always before its endTime
                //There should be four cases when time conflicts.(TOOT,OTTO,TOTO,OTOT)
                //OTTO
                if (thisInstantaneousStartTime > otherInstantaneousStartTime && thisInstantaneousEndTime < otherInstantaneousEndTime){
                    return true;
                }

                //TOOT
                else if (thisInstantaneousStartTime < otherInstantaneousStartTime && thisInstantaneousEndTime > otherInstantaneousEndTime) {
                    return true;
                }
                //TOTO
                else if (thisInstantaneousStartTime < otherInstantaneousStartTime && thisInstantaneousEndTime > otherInstantaneousStartTime && otherInstantaneousStartTime < thisInstantaneousEndTime) {
                    return true;
                }
                //OTOT
                else if (thisInstantaneousStartTime > otherInstantaneousStartTime && thisInstantaneousStartTime < otherInstantaneousEndTime && thisInstantaneousEndTime > otherInstantaneousEndTime) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkMethaneWithinRange(double value) {
        if (value >= 0.1 && value <= 70000) {
            return true;
        }
        else{
            return false;
        }
    }
    
}
