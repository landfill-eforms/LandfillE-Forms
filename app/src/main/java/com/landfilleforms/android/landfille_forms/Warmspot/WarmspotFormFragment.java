package com.landfilleforms.android.landfille_forms.Warmspot;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.landfilleforms.android.landfille_forms.DatePickerFragment;
import com.landfilleforms.android.landfille_forms.R;


/**
 * Created by owchr on 2/16/2017.
 */

public class WarmspotFormFragment  extends Fragment{

    private WarmspotData mWarmspotData;
    private TextView mInspectorLabel;
    private TextView mLocationLabel;
    private TextView mGridLabel;
    private TextView mMethaneLevelLabel;
    private Button mTimeButton;
    private EditText mSizeField;
    private EditText mDescriptionField;
    private Button mSubmit;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_warmspot_data,container,false);
        mInspectorLabel = (TextView) v.findViewById(R.id.warmspot_inspector_name);
        mLocationLabel = (TextView) v.findViewById(R.id.warmspot_location);
        mGridLabel = (TextView) v.findViewById(R.id.warmspot_grid_id);
        mMethaneLevelLabel = (TextView) v.findViewById(R.id.warmspot_methane_reading);
        mTimeButton = (Button) v.findViewById(R.id.warmspot_time_button);
        mTimeButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        //add time button here
                    }
                });

        mSizeField = (EditText) v.findViewById(R.id.warmspot_size_field);
        mSizeField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mDescriptionField = (EditText) v.findViewById(R.id.warmspot_description_field);
        mDescriptionField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mSubmit = (Button) v.findViewById(R.id.warmspot_submit_button);
        mSubmit.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        //submit here
                    }
                });

        return v;

    }
}
