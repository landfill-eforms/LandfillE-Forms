package com.landfilleforms.android.landfille_forms.activities_and_fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import com.landfilleforms.android.landfille_forms.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Work on 11/4/2016.
 */

//TODO:Fix timepicker not showing the correct time upon initialization

public class TimePickerFragment extends DialogFragment {
    public static final String EXTRA_TIME = "com.landfilleforms.android.landfille_forms.time";

    private static final String ARG_TIME = "time";

    private TimePicker mTimePicker;

    public static TimePickerFragment newInstance(Date date) {//This sets the time shown in the timepicker
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME, date);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);

        Date date = (Date) getArguments().getSerializable(ARG_TIME);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        mTimePicker = (TimePicker) v.findViewById(R.id.dialog_time_time_picker);
        mTimePicker.setIs24HourView(true);
        mTimePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
        mTimePicker.setCurrentMinute(cal.get(Calendar.MINUTE));


        return new AlertDialog.Builder(getActivity()).setView(v).setTitle(R.string.time_picker_title).setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {    //Once you click confirm
                        Date date = (Date) getArguments().getSerializable(ARG_TIME);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);

                        //Might have to write conditionals to check build version?
                        int hour = mTimePicker.getCurrentHour();
                        int minutes = mTimePicker.getCurrentMinute();
                        date.setHours(hour);
                        date.setMinutes(minutes);

                        sendResult(Activity.RESULT_OK, date);
                    }
                }).create();
    }

    private void sendResult(int resultCode, Date date) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME, date);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
