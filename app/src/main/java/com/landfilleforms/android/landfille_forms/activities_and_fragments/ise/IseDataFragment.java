package com.landfilleforms.android.landfille_forms.activities_and_fragments.ise;

import android.app.Activity;
import android.graphics.Color;
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

import com.landfilleforms.android.landfille_forms.activities_and_fragments.DatePickerFragment;
import com.landfilleforms.android.landfille_forms.R;
import com.landfilleforms.android.landfille_forms.activities_and_fragments.TimePickerFragment;
import com.landfilleforms.android.landfille_forms.database.dao.IseDao;
import com.landfilleforms.android.landfille_forms.model.IseData;

import java.util.Date;
import java.util.UUID;


public class IseDataFragment extends Fragment {
    private static final String TAG = "IseDataFrag:";
    private static final String ARG_ISE_DATA_ID = "ise_data_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;
    private static final String DIALOG_TIME = "DialogTime";
    private static final int REQUEST_START_TIME = 1;

    private boolean newlyCreatedData;

    private IseData mIseData;
    private TextView mIseField;
    private TextView mLocationLabel;
    private Spinner mGridIdSpinner;
    private TextView mInspectorLabel;
    private EditText mDescriptionField;
    private EditText mMethaneLevelField;
    private Button mDateButton;
    private Button mStartTimeButton;
    private Button mSubmitButton;

    public static IseDataFragment newInstance(UUID iseDataId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_ISE_DATA_ID, iseDataId);
        IseDataFragment fragment = new IseDataFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //TODO: Check if we need user info for Ise/Probe/Warmspot
        super.onCreate(savedInstanceState);
        UUID iseDataId = (UUID) getArguments().getSerializable(ARG_ISE_DATA_ID);
        mIseData = IseDao.get(getActivity()).getIseData(iseDataId);

        if(mIseData.getGridId() == null)
            newlyCreatedData = true;
        else
            newlyCreatedData = false;

        setHasOptionsMenu(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        IseDao.get(getActivity()).updateIseData(mIseData);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_ise_data, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_delete_ise_data:
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                dialogDeleteISEEntry(alertBuilder);
//                IseDao.get(getActivity()).removeIseData(mIseData);
//                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ise_data, container, false);

        mInspectorLabel = (TextView)v.findViewById(R.id.ise_inspector_name);
        mInspectorLabel.setText(mIseData.getInspectorFullName());

        mMethaneLevelField = (EditText)v.findViewById(R.id.ise_methane_reading);
        mMethaneLevelField.setText(Double.toString(mIseData.getMethaneReading()));
        mMethaneLevelField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s=="" || count == 0 || s.toString().equals(".")) mIseData.setMethaneReading(0);
                else mIseData.setMethaneReading(Double.parseDouble(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mIseData.getMethaneReading() < 25) {
                    mMethaneLevelField.setBackgroundColor(Color.RED);
                    mSubmitButton.setEnabled(false);

                } else {
                    mMethaneLevelField.setBackgroundColor(Color.GREEN);
                    mSubmitButton.setEnabled(true);
                }

            }
        });

        mLocationLabel = (TextView) v.findViewById(R.id.ise_location);
        mLocationLabel.setText(mIseData.getLocation());

        mGridIdSpinner = (Spinner)v.findViewById(R.id.ise_grid_id);
        ArrayAdapter<CharSequence> adapter;
        switch(mIseData.getLocation()) {
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
        mGridIdSpinner.setSelection(adapter.getPosition(mIseData.getGridId()));

        mGridIdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mIseData.setGridId(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        mIseField = (TextView)v.findViewById(R.id.ise_field);
        mIseField.setText(mIseData.getIseNumber());

        mDescriptionField = (EditText)v.findViewById(R.id.ise_description);
        mDescriptionField.setText(mIseData.getDescription());
        mDescriptionField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mIseData.setDescription(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDateButton = (Button)v.findViewById(R.id.ise_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mIseData.getDate());
                dialog.setTargetFragment(IseDataFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mStartTimeButton = (Button)v.findViewById(R.id.ise_start_time);
        updateStartTime();
        mStartTimeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance(mIseData.getDate());
                dialog.setTargetFragment(IseDataFragment.this, REQUEST_START_TIME);
                dialog.show(manager, DIALOG_TIME);
            }
        });


        mSubmitButton = (Button)v.findViewById(R.id.ise_submit_button);
        mSubmitButton.setText(R.string.submit_button_label);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(mIseData.getMethaneReading() < 25){
                    Toast.makeText(getActivity(), R.string.improper_methane_ise_toast, Toast.LENGTH_SHORT).show();
                } else {
                    getActivity().finish();
                    Toast.makeText(getActivity(), R.string.ise_added_toast, Toast.LENGTH_SHORT).show();
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
                        IseDao.get(getActivity()).removeIseData(mIseData);
                        Toast.makeText(getActivity(), R.string.new_ise_cancelation_toast, Toast.LENGTH_SHORT).show();
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

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mIseData.setDate(date);
            updateDate();
            updateStartTime();
        }

        else if (requestCode == REQUEST_START_TIME) {
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mIseData.setDate(date);
            updateStartTime();
        }

    }

    private void updateDate() {
        mDateButton.setText(DateFormat.format("EEEE, MMM d, yyyy",mIseData.getDate()));
    }

    private void updateStartTime() {
        mStartTimeButton.setText(DateFormat.format("HH:mm",mIseData.getDate()));
    }

    private void dialogDeleteISEEntry(AlertDialog.Builder alertBuilder) {
        alertBuilder.setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        IseDao.get(getActivity()).removeIseData(mIseData);
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
        deleteAlert.setTitle("Delete ISE Entry");
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
}
