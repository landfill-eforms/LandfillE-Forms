package com.landfilleforms.android.landfille_forms;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.landfilleforms.android.landfille_forms.model.InstantaneousData;
import com.landfilleforms.android.landfille_forms.model.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Work on 11/3/2016.
 */

public class IntegratedFormFragment extends Fragment {
    private static final String TAG = "InstantaneousFormFrag";
    private static final String EXTRA_LANDFILL_LOCATION = "com.landfilleforms.android.landfille_forms.landfill_location";
    private SessionManager session;
    private User mUser;


    private RecyclerView mInstantaneousDataRecyclerView;
    private InstantaneousDataAdapter mAdapter;
    private Button mExportButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_instantaneous_form, container, false);

        mExportButton = (Button) view.findViewById(R.id.export_instantaneous);
        mExportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Context context = getActivity();
                InstantaneousForm instantaneousForm = InstantaneousForm.get(getActivity());
                List<InstantaneousData> instantaneousDatas = instantaneousForm.getInstantaneousDatas();
                GsonBuilder builder = new GsonBuilder();
                builder.serializeNulls();
                builder.setDateFormat("EEE, dd MMM yyyy HH:mm:ss");
                Gson gson = builder.create();

                String jsonOutput = gson.toJson(instantaneousDatas);
                Log.d("Json",jsonOutput);
                int messageResId;

                Date d = new Date();
                String name = "Instantaneous" + d.toString() + ".json";
                String path = "LandfillData";
                try {

                    File myFile = new File(Environment.getExternalStorageDirectory()+File.separator+path);
                    myFile.mkdirs();
                    myFile = new File(Environment.getExternalStorageDirectory()+File.separator+path+File.separator+name);

                    myFile.createNewFile();
                    FileOutputStream fOut = new FileOutputStream(myFile);
                    OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                    myOutWriter.append(jsonOutput);

                    myOutWriter.close();
                    fOut.close();
                    messageResId = R.string.export_successful_toast;
                    Toast.makeText(context, messageResId, Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    messageResId = R.string.export_unsuccessful_toast;
                    Toast.makeText(context, messageResId, Toast.LENGTH_SHORT).show();
                }

            }
        });

        mInstantaneousDataRecyclerView = (RecyclerView) view.findViewById(R.id.instantaneous_data_recycler_view);
        mInstantaneousDataRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_instantaneous_form, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                InstantaneousData instantaneousData = new InstantaneousData();
                //Log.d("From FormFrag",getActivity().getIntent().getStringExtra(EXTRA_USERNAME));
                instantaneousData.setLandFillLocation(this.getActivity().getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION));
                instantaneousData.setInspectorName(mUser.getFullName());
                InstantaneousForm.get(getActivity()).addInstantaneousData(instantaneousData);
                Intent intent = InstantaneousDataPagerActivity.newIntent(getActivity(),instantaneousData.getId());

                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI() {
        InstantaneousForm instantaneousForm = InstantaneousForm.get(getActivity());
        List<InstantaneousData> instantaneousDatas = instantaneousForm.getInstantaneousDatas();

        if(mAdapter == null) {
            mAdapter = new InstantaneousDataAdapter(instantaneousDatas);
            mInstantaneousDataRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setInstantaneousDatas(instantaneousDatas);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class InstantaneousDataHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private InstantaneousData mInstantaneousData;

        private TextView mGridIdView;
        private TextView mStartDateView;
        private TextView mStartTimeView;
        private TextView mEndTimeView;
        private TextView mMethaneReadingView;
        private Button mEditButton;


        public InstantaneousDataHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);

            mGridIdView = (TextView) itemView.findViewById(R.id.list_item_instantaneous_data_gridid_view);
            mMethaneReadingView = (TextView) itemView.findViewById(R.id.list_item_instantaneous_data_methane_level_view);
            mStartDateView = (TextView) itemView.findViewById(R.id.list_item_instantaneous_data_start_date_view);
            //mStartTimeView = (TextView) itemView.findViewById(R.id.list_item_instantaneous_data_start_time_view);
            //mEndTimeView = (TextView) itemView.findViewById(R.id.list_item_instantaneous_data_end_time_view);

            mEditButton = (Button)itemView.findViewById(R.id.list_item_instantaneous_edit_button);
            mEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = InstantaneousDataPagerActivity.newIntent(getActivity(), mInstantaneousData.getId());
                    startActivity(intent);
                }
            });
        }

        public void bindInstantaneousData(InstantaneousData instantaneousData) {
            mInstantaneousData = instantaneousData;
            mGridIdView.setText(mInstantaneousData.getGridId());
            mMethaneReadingView.setText(Double.toString(mInstantaneousData.getMethaneReading()));
            mStartDateView.setText(DateFormat.format("M/d/yyyy",mInstantaneousData.getStartDate()));
            //mStartTimeView.setText(DateFormat.format("HH:mm:ss",mInstantaneousData.getStartDate()));
            //mEndTimeView.setText(DateFormat.format("HH:mm:ss",mInstantaneousData.getEndDate()));
        }

        @Override
        public void onClick(View v) {
//            Intent intent = InstantaneousDataPagerActivity.newIntent(getActivity(), mInstantaneousData.getId());
//            startActivity(intent);
        }
    }

    private class InstantaneousDataAdapter extends RecyclerView.Adapter<InstantaneousDataHolder> {

        private List<InstantaneousData> mInstantaneousDatas;

        public InstantaneousDataAdapter(List<InstantaneousData> instantaneousDatas) {
            mInstantaneousDatas = instantaneousDatas;
        }

        @Override
        public InstantaneousDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_instantaneous_data, parent, false);
            return new InstantaneousDataHolder(view);
        }

        @Override
        public void onBindViewHolder(InstantaneousDataHolder holder, int position) {
            InstantaneousData instantaneousData = mInstantaneousDatas.get(position);
            holder.bindInstantaneousData(instantaneousData);
        }

        @Override
        public int getItemCount() {
            return mInstantaneousDatas.size();
        }

        public void setInstantaneousDatas(List<InstantaneousData> instantaneousDatas) {
            mInstantaneousDatas = instantaneousDatas;
        }
    }
}
