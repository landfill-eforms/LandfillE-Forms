package com.landfilleforms.android.landfille_forms.activities_and_fragments.ise;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.landfilleforms.android.landfille_forms.R;
import com.landfilleforms.android.landfille_forms.util.SessionManager;
import com.landfilleforms.android.landfille_forms.database.dao.IseDao;
import com.landfilleforms.android.landfille_forms.model.IseData;
import com.landfilleforms.android.landfille_forms.model.User;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class IseFormFragment extends Fragment {
    private static final String TAG = "IseFormFrag";
    private static final String EXTRA_LANDFILL_LOCATION = "com.landfilleforms.android.landfille_forms.landfill_location";
    private SessionManager session;
    private User mUser;
    List<IseData> mIseDatas;
    private String mCurrentIseNumber;

    private TextView mCurrentLocation;
    private TextView mIseGridsField;
    private Spinner mIseNumberSpinner;
    private Button mIseNumbersSetButton;



    private RecyclerView mIseDataRecyclerView;
    private IseDataAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getActivity().setTitle(getResources().getString(R.string.ise_form_header) + ": " + this.getActivity().getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION));
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");

        session = new SessionManager(getActivity().getApplicationContext());
        session.checkLogin();

        mUser = session.getCurrentUser();
        Log.d("UserName:", mUser.getUsername());;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ise_form, container, false);

        final IseDao iseDao = IseDao.get(getActivity());
        String [] args = {this.getActivity().getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION)};
        mIseDatas = iseDao.getIseDatasByLocation(args);

        Set<String> iseNumbers = new HashSet<String>();
        for(IseData iseData: mIseDatas) {
            if(iseData.getIseNumber() != null && iseData.getIseNumber().trim().length() != 0)
                iseNumbers.add(iseData.getIseNumber());
        }
        iseNumbers.add("");
        mIseNumberSpinner = (Spinner) v.findViewById(R.id.ise_spinner);
        List<String> iseNumbersList = new ArrayList<String>(iseNumbers);
        ArrayAdapter<String> iseNumberSpinnerItems = new ArrayAdapter<String>(this.getActivity(), R.layout.dark_spinner_layout, iseNumbersList);
        mIseNumberSpinner.setAdapter(iseNumberSpinnerItems);
        mIseNumberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCurrentIseNumber = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mCurrentIseNumber = "";
            }
        });

        mIseNumbersSetButton = (Button) v.findViewById(R.id.ise_button_form);//MAYBE THIS MIGHT BE A PROB
        mIseNumbersSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CurrentLoc:",getActivity().getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION));
                String [] args = {getActivity().getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION), mCurrentIseNumber};
                /*List<IseData> iseDatasFilteredByIseNumber = new ArrayList<IseData>();
                for(int i = 0; i < mIseDatas.size(); i++) {
                    if(mIseDatas.get(i).getIseNumber() == mCurrentIseNumber)
                        iseDatasFilteredByIseNumber.add(mIseDatas.get(i));
                }
                Log.d("CurrentISE:", mCurrentIseNumber);
                mIseDatas = iseDatasFilteredByIseNumber;
                Log.d("FilteredSize:" ,Integer.toString(mIseDatas.size()));*/
                updateUIwithIseNumber(args);
            }
        });



//        mCurrentLocation = (TextView) v.findViewById(R.id.ise_location);
//        mCurrentLocation.setText(this.getActivity().getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION));

        //Finds all the grids related to a list of ISE entries
        Set<String> iseGrids = new HashSet<String>();
        for(int i = 0; i < mIseDatas.size(); i++) {
            if(mIseDatas.get(i).getGridId() != null && mIseDatas.get(i).getGridId().trim().length() != 0)
                iseGrids.add(mIseDatas.get(i).getGridId());
        }
        mIseGridsField = (TextView) v.findViewById(R.id.ise_grids);
        mIseGridsField.setText(iseGrids.toString());


        mIseDataRecyclerView = (RecyclerView) v.findViewById(R.id.ise_data_recycler_view);
        mIseDataRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_ise_form, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_ise:
                if(mIseDatas.size() != 0){
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                    dialogISENavigation(alertBuilder);
                }
                else {
                    IseData iseData = new IseData();
                    iseData.setLocation(getActivity().getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION));
                    iseData.setInspectorFullName(mUser.getFullName());
                    iseData.setInspectorUserName(mUser.getUsername());
                    iseData.setIseNumber(IseDao.get(getActivity()).generateIseNumber(iseData.getLocation(),new Date()));
                    IseDao.get(getActivity()).addIseData(iseData);
                    Intent intent = IseDataPagerActivity.newIntent(getActivity(),iseData.getId());
                    startActivity(intent);
                }

                return true;
            case android.R.id.home:
                this.getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI() {
        IseDao iseDao = IseDao.get(getActivity());
        String [] args = {this.getActivity().getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION)};
        List<IseData> iseDatas = iseDao.getIseDatasByLocation(args);

        if(mAdapter == null) {
            mAdapter = new IseDataAdapter(iseDatas);
            mIseDataRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setIseDatas(iseDatas);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void updateUIwithIseNumber(String [] args) {
        IseDao iseDao = IseDao.get(getActivity());
        List<IseData> iseDatas = iseDao.getIseDatasByLocationAndIse(args);

        if(mAdapter == null) {
            mAdapter = new IseDataAdapter(iseDatas);
            mIseDataRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setIseDatas(iseDatas);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void dialogISENavigation(AlertDialog.Builder redirectionAlert) {
        redirectionAlert.setMessage("Would you like to add to current ISE or create a new one?")
                .setCancelable(false).setPositiveButton("Add to Current ISE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /*IseData iseData = new IseData();
                //Log.d("From FormFrag",getActivity().getIntent().getStringExtra(EXTRA_USERNAME));
                iseData.setLocation(getActivity().getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION));
                iseData.setInspectorFullName(mUser.getFullName());
                iseData.setInspectorUserName(mUser.getUsername());
                iseData.setIseNumber(mCurrentIseNumber);
                IseDao.get(getActivity()).addIseData(iseData);
                Intent intent = IseDataPagerActivity.newIntent(getActivity(),iseData.getId());
                startActivity(intent);*/
                Toast.makeText(getActivity(),R.string.option_repair_toast, Toast.LENGTH_SHORT).show();
            }
        }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setNegativeButton("Create a new ISE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                IseData iseData = new IseData();
                iseData.setLocation(getActivity().getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION));
                iseData.setInspectorFullName(mUser.getFullName());
                iseData.setInspectorUserName(mUser.getUsername());
                iseData.setIseNumber(IseDao.get(getActivity()).generateIseNumber(iseData.getLocation(),new Date()));
                IseDao.get(getActivity()).addIseData(iseData);
                Intent intent = IseDataPagerActivity.newIntent(getActivity(),iseData.getId());
                startActivity(intent);
            }
        });
        AlertDialog alert = redirectionAlert.create();
        alert.setTitle("New ISE Entry");
        alert.show();

    }

    //For RecycleView
    private class IseDataHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private IseData mIseData;

        private TextView mGridIdView;
        private TextView mStartDateView;

        private TextView mMethaneReadingView;
        private Button mEditButton;
        private CardView mCardView;

        public IseDataHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);

            mGridIdView = (TextView) itemView.findViewById(R.id.list_item_ise_data_gridid_view);
            mMethaneReadingView = (TextView) itemView.findViewById(R.id.list_item_ise_data_methane_level_view);
            mStartDateView = (TextView) itemView.findViewById(R.id.list_item_ise_date_view);

            mCardView = (CardView)itemView.findViewById(R.id.ise_data_cv);
            mCardView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v){
                    Intent intent = IseDataPagerActivity.newIntent(getActivity(), mIseData.getId());
                    startActivity(intent);
                }

            });

//            mEditButton = (Button)itemView.findViewById(R.id.list_item_ise_edit_button);
//            mEditButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = IseDataPagerActivity.newIntent(getActivity(), mIseData.getId());
//                    startActivity(intent);
//                }
//            });
        }

        public void bindIseData(IseData iseData) {
            mIseData = iseData;
            mGridIdView.setText(mIseData.getGridId());
            //display 2 sig figs
            mMethaneReadingView.setText(String.format("%.2f", mIseData.getMethaneReading()));
            mStartDateView.setText(DateFormat.format("yyyy-MM-dd",mIseData.getDate()));
            mGridIdView.setTextColor(Color.RED);
            mMethaneReadingView.setTextColor(Color.RED);
            mStartDateView.setTextColor(Color.WHITE);
        }

        @Override
        public void onClick(View v) {
//            Intent intent = IseDataPagerActivity.newIntent(getActivity(), mIseData.getId());
//            startActivity(intent);
        }


    }


    private class IseDataAdapter extends RecyclerView.Adapter<IseDataHolder> {

        private List<IseData> mIseDatas;

        public IseDataAdapter(List<IseData> iseDatas) {
            mIseDatas = iseDatas;
        }

        @Override
        public IseDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_ise_data, parent, false);
            return new IseDataHolder(view);
        }

        @Override
        public void onBindViewHolder(IseDataHolder holder, int position) {
            IseData iseData = mIseDatas.get(position);
            holder.bindIseData(iseData);
        }

        @Override
        public int getItemCount() {
            return mIseDatas.size();
        }

        public void setIseDatas(List<IseData> iseDatas) {
            mIseDatas = iseDatas;
        }
    }


}
