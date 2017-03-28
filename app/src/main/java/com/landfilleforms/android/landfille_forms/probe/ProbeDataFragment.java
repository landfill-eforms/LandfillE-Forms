package com.landfilleforms.android.landfille_forms.probe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.landfilleforms.android.landfille_forms.DatePickerFragment;
import com.landfilleforms.android.landfille_forms.R;
import com.landfilleforms.android.landfille_forms.database.dao.ProbeDao;
import com.landfilleforms.android.landfille_forms.model.ProbeData;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Work on 3/27/2017.
 */

public class ProbeDataFragment extends Fragment {
    private static final String EXTRA_LANDFILL_LOCATION = "com.landfilleforms.android.landfille_forms.landfill_location";
    private static final String ARG_PROBE_DATA_ID = "probe_data_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;

    private ProbeData mProbeData;

    private TextView mLocationLabel;
    private Spinner mProbeNumberSpinner;
    private TextView mInspectorLabel;
    private EditText mRemarksField;
    private EditText mWaterPressureField;
    private EditText mMethanePercentageField;
    private Button mDateButton;
    private Button mSubmitButton;

    public static ProbeDataFragment newInstance(UUID probeDataId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PROBE_DATA_ID, probeDataId);
        ProbeDataFragment fragment = new ProbeDataFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID probeDataId = (UUID) getArguments().getSerializable(ARG_PROBE_DATA_ID);
        mProbeData = ProbeDao.get(getActivity()).getProbeData(probeDataId);

        setHasOptionsMenu(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        ProbeDao.get(getActivity()).updateProbeData(mProbeData);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_probe_data, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_delete_probe_data:
                AlertDialog.Builder alertBuilder =  new AlertDialog.Builder(getActivity());
                dialogDeleteProbeEntry(alertBuilder);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_probe_data, container, false);




        return v;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mProbeData.setDate(date);
            updateDate();
        }

    }

    private void updateDate() {
        mDateButton.setText(DateFormat.format("EEEE, MMM d, yyyy",mProbeData.getDate()));
    }

    private void dialogDeleteProbeEntry(AlertDialog.Builder alertBuilder) {
        alertBuilder.setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ProbeDao.get(getActivity()).removeProbeData(mProbeData);
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
        deleteAlert.setTitle("Delete Probe Entry");
        deleteAlert.show();
    }
}
