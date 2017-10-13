package com.landfilleforms.android.landfille_forms.activities_and_fragments.ime;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.landfilleforms.android.landfille_forms.activities_and_fragments.DatePickerFragment;
import com.landfilleforms.android.landfille_forms.R;
import com.landfilleforms.android.landfille_forms.activities_and_fragments.TimePickerFragment;
import com.landfilleforms.android.landfille_forms.database.dao.ImeDao;
import com.landfilleforms.android.landfille_forms.database.dao.InstrumentDao;
import com.landfilleforms.android.landfille_forms.model.ImeData;
import com.landfilleforms.android.landfille_forms.model.Instrument;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.landfilleforms.android.landfille_forms.R.id.gridList;
import static com.landfilleforms.android.landfille_forms.R.id.remove;

//Done
public class ImeDataFragment extends Fragment {
    private static final String TAG = "ImeDataFrag:";
    private static final String ARG_IME_DATA_ID = "ime_data_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;
    private static final String DIALOG_TIME = "DialogTime";
    private static final int REQUEST_START_TIME = 1;

    private static int selectedPosition = 0;

    private ImeData mImeData;
    private boolean newlyCreatedData;

    private TextView mImeField;
    private TextView mLocationLabel;
    private Spinner mGridIdSpinner;
    private TextView mInspectorLabel;
    private EditText mDescriptionField;
    private EditText mMethaneLevelField;
    private Button mDateButton;
    private Button mStartTimeButton;
    private TextView mInstrumentLabel;

    private Button mSubmitButton;
    private Button insert,delete;
    private TextView mGridList;
    private StringBuilder gridBuilder = new StringBuilder();
    private String value;

    private Spinner mInstrumentSpinner; //Instrument spinner
    private List<Instrument> mInstrumentList; //Instrument List



    public static ImeDataFragment newInstance(UUID imeDataId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_IME_DATA_ID, imeDataId);
        ImeDataFragment fragment = new ImeDataFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Getting Instrument List
        mInstrumentList = InstrumentDao.get(getActivity()).getInstruments();


        UUID imeDataId = (UUID) getArguments().getSerializable(ARG_IME_DATA_ID);
        mImeData = ImeDao.get(getActivity()).getImeData(imeDataId);

        if(mImeData.getGridId() == null)
            newlyCreatedData = true;
        else
            newlyCreatedData = false;

        setHasOptionsMenu(true);
    }

    @Override
    public void onPause() {
        super.onPause();

        ImeDao.get(getActivity()).updateImeData(mImeData);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_ime_data, menu);
    }

    //Toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //TODO
        switch (item.getItemId()){
            case R.id.menu_item_delete_ime_data:
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                dialogDeleteIMEEntry(alertBuilder);
//                ImeDao.get(getActivity()).removeImeData(mImeData);
//                getActivity().finish();
                return true;
           default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ime_data, container, false);

        //Instrument
        mInstrumentSpinner = (Spinner)v.findViewById(R.id.ins_spinner); //Set Instrument spinner to its ID

        //Make arrayadapter of instruments to add items from the list to the spinner
        ArrayAdapter<Instrument> instrumentArrayAdapter = new ArrayAdapter<Instrument>(this.getActivity(), android.R.layout.simple_spinner_item, mInstrumentList);
        instrumentArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        //set the spinner to the arrayadapter
        mInstrumentSpinner.setAdapter(instrumentArrayAdapter);

        //trying to save position but not working
        selectedPosition = mInstrumentSpinner.getSelectedItemPosition();
/*
        if(mImeData.getInstrument() != null) {
            mInstrumentSpinner.setSelection(Integer.parseInt(mImeData.getInstrument()));

        }
        mInstrumentSpinner.setSelection(Integer.parseInt(mImeData.getInstrument()));
*/

//       mInstrumentSpinner.setSelection(Integer.parseInt(mImeData.getInstrument()));

        Toast.makeText(getActivity(), selectedPosition + " ", Toast.LENGTH_SHORT).show();
        mInstrumentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPosition = position;
                mInstrumentSpinner.setSelection(selectedPosition);
                mImeData.setInstrument(parent.getItemAtPosition(position).toString());
//                mImeData.setInstrument(String.valueOf(selectedPosition));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        mInspectorLabel = (TextView)v.findViewById(R.id.inspector_name);
        mInspectorLabel.setText(mImeData.getInspectorFullName());

        //handles adding and remove grids from builder
        insert = (Button) v.findViewById(R.id.add);
        delete = (Button) v.findViewById(remove);
        mGridList = (TextView) v.findViewById(gridList);

        mMethaneLevelField = (EditText)v.findViewById(R.id.methane_reading);
        mMethaneLevelField.setText(Double.toString(mImeData.getMethaneReading()));
        mMethaneLevelField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s=="" || count == 0 || s.equals(".")) mImeData.setMethaneReading(0);
                else mImeData.setMethaneReading(Double.parseDouble(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mImeData.getMethaneReading() < 500) {
                    mMethaneLevelField.setBackgroundColor(Color.RED);
                    mSubmitButton.setEnabled(false);

                } else {
                    mMethaneLevelField.setBackgroundColor(Color.GREEN);
                    mSubmitButton.setEnabled(true);
                }

            }
        });

        mLocationLabel = (TextView) v.findViewById(R.id.location);
        mLocationLabel.setText(mImeData.getLocation());

        mGridIdSpinner = (Spinner)v.findViewById(R.id.grid_id);
        ArrayAdapter<CharSequence> adapter;
        switch(mImeData.getLocation()) {
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
        mGridIdSpinner.setSelection(adapter.getPosition(mImeData.getGridId()));

        mGridIdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                value = parent.getItemAtPosition(position).toString();
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

        mGridList.setText(mImeData.getGridId());

        if (mImeData.getGridId() != null) {
            gridBuilder = new StringBuilder(mImeData.getGridId());

        }
//        mImeData.setGridId(gridBuilder.toString());

        //mGridList.setText((gridBuilder.append(String.valueOf(mGridIdSpinner.getSelectedItem()) + " ")).toString());
        //mGridList.setText(mImeData.setGridId(gridBuilder.toString()));
//        mImeData.setGridId(gridBuilder.toString());

        mImeField = (TextView)v.findViewById(R.id.ime_field);
        mImeField.setText(mImeData.getImeNumber());



        mDescriptionField = (EditText)v.findViewById(R.id.description);
        mDescriptionField.setText(mImeData.getDescription());
        mDescriptionField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mImeData.setDescription(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Work on making sure keyboard does not auto-focus
        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(mDescriptionField.getWindowToken(), 0);

        mDateButton = (Button)v.findViewById(R.id.date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mImeData.getDate());
                dialog.setTargetFragment(ImeDataFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mStartTimeButton = (Button)v.findViewById(R.id.start_time);
        updateStartTime();
        mStartTimeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance(mImeData.getDate());
                dialog.setTargetFragment(ImeDataFragment.this, REQUEST_START_TIME);
                dialog.show(manager, DIALOG_TIME);
            }
        });


        mSubmitButton = (Button)v.findViewById(R.id.submit);
        mSubmitButton.setText(R.string.submit_button_label);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(mImeData.getMethaneReading() < 500){
                    Toast.makeText(getActivity(), R.string.improper_methane_ime_toast, Toast.LENGTH_SHORT).show();
                } else {
                    getActivity().finish();
                    Toast.makeText(getActivity(), R.string.ime_added_toast, Toast.LENGTH_SHORT).show();

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
                        ImeDao.get(getActivity()).removeImeData(mImeData);
                        Toast.makeText(getActivity(), R.string.new_ime_cancelation_toast, Toast.LENGTH_SHORT).show();
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
            mImeData.setDate(date);
            updateDate();
            updateStartTime();
        }

        else if (requestCode == REQUEST_START_TIME) {//Accidentally setting this as (resultCode == REQUEST_DATE) cost me 30 min of debug time orz
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mImeData.setDate(date);
            updateStartTime();
        }

    }

    private void updateDate() {
        mDateButton.setText(DateFormat.format("EEEE, MMM d, yyyy",mImeData.getDate()));
    }

    private void updateStartTime() {
        mStartTimeButton.setText(DateFormat.format("HH:mm",mImeData.getDate()));
    }

    private void dialogDeleteIMEEntry(AlertDialog.Builder alertBuilder) {
        alertBuilder.setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ImeDao.get(getActivity()).removeImeData(mImeData);
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
        deleteAlert.setTitle("Delete IME Entry");
        deleteAlert.show();
    }

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void halt(android.support.v7.app.AlertDialog.Builder alertBuilder) {
        alertBuilder.setMessage("You are leaving fields blank!\n If you would like to save hit submit.")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        android.support.v7.app.AlertDialog deleteAlert = alertBuilder.create();
        deleteAlert.setTitle("Active Data");
        deleteAlert.show();
    }

    private void addGrid(){
        mGridList.setText((gridBuilder.append(String.valueOf(mGridIdSpinner.getSelectedItem()) + " ")).toString());
        mImeData.setGridId(gridBuilder.toString());
    }

    private void removeGrid(){
        String grid = String.valueOf(mGridIdSpinner.getSelectedItem());
        int index = gridBuilder.indexOf(grid);
        if(gridBuilder.length() > 0 && index > -1)  {
            gridBuilder.replace(index, index + grid.length(), "");
//            remove white space after deleting grid
            int temp = gridBuilder.indexOf("  ");

            while(temp > -1) {
                gridBuilder.replace(index, index + 1, "");
                temp = gridBuilder.indexOf("  ");
            }

            mGridList.setText(gridBuilder.toString());
            mImeData.setGridId(gridBuilder.toString());
        }
    }



}
