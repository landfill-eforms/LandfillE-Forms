package com.landfilleforms.android.landfille_forms.activities_and_fragments.ime;

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
import com.landfilleforms.android.landfille_forms.database.dao.ImeDao;
import com.landfilleforms.android.landfille_forms.model.ImeData;
import com.landfilleforms.android.landfille_forms.model.User;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


// TODO: Create Layout file, include all the fields
public class ImeFormFragment extends Fragment {
    private static final String TAG = "ImeFormFrag";
    private static final String EXTRA_LANDFILL_LOCATION = "com.landfilleforms.android.landfille_forms.landfill_location";
    private SessionManager session;
    private User mUser;
    List<ImeData> mImeDatas;
    private String mCurrentImeNumber;




    private TextView mCurrentLocation;
    private TextView mImeGridsField;
    private Spinner mImeNumberSpinner;
    private Button mImeNumbersSetButton;



    private RecyclerView mImeDataRecyclerView;
    private ImeDataAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getActivity().setTitle(getResources().getString(R.string.ime_form_header) + ": " + this.getActivity().getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION));
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");

        session = new SessionManager(getActivity().getApplicationContext());
        session.checkLogin();

        mUser = session.getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ime_form, container, false);

        final ImeDao imeDao = ImeDao.get(getActivity());
        String [] args = {this.getActivity().getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION)};
        mImeDatas = imeDao.getImeDatasByLocation(args);

        //ImeNumbers for spinner
        Set<String> imeNumbers = new HashSet<String>();
        for(ImeData imeData: mImeDatas) {
            if(imeData.getImeNumber() != null && imeData.getImeNumber().trim().length() != 0)
                imeNumbers.add(imeData.getImeNumber());
        }
        imeNumbers.add("");
        mImeNumberSpinner = (Spinner) v.findViewById(R.id.ime_spinner);
        List<String> imeNumbersList = new ArrayList<String>(imeNumbers);
        ArrayAdapter<String> imeNumberSpinnerItems = new ArrayAdapter<String>(this.getActivity(), R.layout.dark_spinner_layout, imeNumbersList);
        mImeNumberSpinner.setAdapter(imeNumberSpinnerItems);
        mImeNumberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCurrentImeNumber = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mCurrentImeNumber = "";
            }
        });

        mImeNumbersSetButton = (Button) v.findViewById(R.id.ime_button);
        mImeNumbersSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CurrentLoc:",getActivity().getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION));
                String [] args = {getActivity().getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION), mCurrentImeNumber};
                /*List<ImeData> imeDatasFilteredByImeNumber = new ArrayList<ImeData>();
                for(int i = 0; i < mImeDatas.size(); i++) {
                    if(mImeDatas.get(i).getImeNumber() == mCurrentImeNumber)
                        imeDatasFilteredByImeNumber.add(mImeDatas.get(i));
                }
                Log.d("CurrentIME:", mCurrentImeNumber);
                mImeDatas = imeDatasFilteredByImeNumber;
                Log.d("FilteredSize:" ,Integer.toString(mImeDatas.size()));*/
                updateUIwithImeNumber(args);
            }
        });



        mCurrentLocation = (TextView) v.findViewById(R.id.location);
        mCurrentLocation.setText(this.getActivity().getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION));

        //Finds all the grids related to a list of IME entries
        Set<String> imeGrids = new HashSet<String>();
        for(int i = 0; i < mImeDatas.size(); i++) {
            if(mImeDatas.get(i).getGridId() != null && mImeDatas.get(i).getGridId().trim().length() != 0)
                imeGrids.add(mImeDatas.get(i).getGridId());
        }
        mImeGridsField = (TextView) v.findViewById(R.id.ime_grids);
        mImeGridsField.setText(imeGrids.toString());


        mImeDataRecyclerView = (RecyclerView) v.findViewById(R.id.ime_data_recycler_view);
        mImeDataRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


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
        inflater.inflate(R.menu.fragment_ime_form, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_ime:
                if(mImeDatas.size() !=0){
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                    dialogIMENavigation(alertBuilder);
                }
                else{
                    ImeData imeData = new ImeData();
                    //Log.d("From FormFrag",getActivity().getIntent().getStringExtra(EXTRA_USERNAME));
                    imeData.setLocation(getActivity().getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION));
                    imeData.setInspectorFullName(mUser.getFullName());
                    imeData.setInspectorUserName(mUser.getUsername());
                    imeData.setImeNumber(ImeDao.get(getActivity()).generateIMEnumber(mCurrentLocation.getText().toString(),new Date()));
                    ImeDao.get(getActivity()).addImeData(imeData);
                    Intent intent = ImeDataPagerActivity.newIntent(getActivity(),imeData.getId());
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
        ImeDao imeDao = ImeDao.get(getActivity());
        String [] args = {this.getActivity().getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION)};
        List<ImeData> imeDatas = imeDao.getImeDatasByLocation(args);

        if(mAdapter == null) {
            mAdapter = new ImeDataAdapter(imeDatas);
            mImeDataRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setImeDatas(imeDatas);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void updateUIwithImeNumber(String [] args) {
        ImeDao imeDao = ImeDao.get(getActivity());
        List<ImeData> imeDatas = imeDao.getImeDatasByLocationAndIme(args);

        if(mAdapter == null) {
            mAdapter = new ImeDataAdapter(imeDatas);
            mImeDataRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setImeDatas(imeDatas);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void dialogIMENavigation(AlertDialog.Builder redirectionAlert) {
        redirectionAlert.setMessage("Would you like to add to current IME or create a new one?")
                //set positive as "Add to Existing IME",
                .setCancelable(false).setPositiveButton("Add to Current IME", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /*ImeData imeData = new ImeData();
                //Log.d("From FormFrag",getActivity().getIntent().getStringExtra(EXTRA_USERNAME));
                imeData.setLocation(getActivity().getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION));
                imeData.setInspectorFullName(mUser.getFullName());
                imeData.setInspectorUserName(mUser.getUsername());
                imeData.setImeNumber(mCurrentImeNumber);
                ImeDao.get(getActivity()).addImeData(imeData);
                Intent intent = ImeDataPagerActivity.newIntent(getActivity(),imeData.getId());
                startActivity(intent);*/
                Toast.makeText(getActivity(),R.string.option_repair_toast, Toast.LENGTH_SHORT).show();
            }//temp fix, set this to cancel to rearrange order of options
        }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setNegativeButton("Create a new IME", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ImeData imeData = new ImeData();
                //Log.d("From FormFrag",getActivity().getIntent().getStringExtra(EXTRA_USERNAME));
                imeData.setLocation(getActivity().getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION));
                imeData.setInspectorFullName(mUser.getFullName());
                imeData.setInspectorUserName(mUser.getUsername());
                imeData.setImeNumber(ImeDao.get(getActivity()).generateIMEnumber(mCurrentLocation.getText().toString(),new Date()));
                ImeDao.get(getActivity()).addImeData(imeData);
                Intent intent = ImeDataPagerActivity.newIntent(getActivity(),imeData.getId());
                startActivity(intent);
            }
        });
        AlertDialog alert = redirectionAlert.create();
        alert.setTitle("New IME Entry");
        alert.show();

    }

    //For RecycleView
    private class ImeDataHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImeData mImeData;

        private TextView mGridIdView;
        private TextView mStartDateView;

        private TextView mMethaneReadingView;
        private Button mEditButton;
        private CardView mCardView;
        private TextView mIMEno;


        public ImeDataHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);

            mGridIdView = (TextView) itemView.findViewById(R.id.list_item_ime_data_gridid_view);
            mMethaneReadingView = (TextView) itemView.findViewById(R.id.list_item_ime_data_methane_level_view);
            mStartDateView = (TextView) itemView.findViewById(R.id.list_item_ime_date_view);
            mIMEno = (TextView) itemView.findViewById(R.id.list_item_ime_no_view);

            mGridIdView.setTextColor(Color.RED);
            mMethaneReadingView.setTextColor(Color.RED);
            mStartDateView.setTextColor(Color.RED);
            mIMEno.setTextColor(Color.RED);

            mCardView = (CardView)itemView.findViewById(R.id.ime_data_cv);
            mCardView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v){
                    Intent intent = ImeDataPagerActivity.newIntent(getActivity(), mImeData.getId());
                    startActivity(intent);
                }

            });


//            mEditButton = (Button)itemView.findViewById(R.id.list_item_ime_edit_button);
//            mEditButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = ImeDataPagerActivity.newIntent(getActivity(), mImeData.getId());
//                    startActivity(intent);
//                }
//            });
        }

        public void bindImeData(ImeData imeData) {
            mImeData = imeData;
            mGridIdView.setText(mImeData.getGridId());
            mMethaneReadingView.setText(Double.toString(mImeData.getMethaneReading()));
            mStartDateView.setText(DateFormat.format("yyyy-MM-dd",mImeData.getDate()));
            mIMEno.setText(mImeData.getImeNumber());
            //mStartTimeView.setText(DateFormat.format("HH:mm:ss",mImeData.getStartDate()));
            //mEndTimeView.setText(DateFormat.format("HH:mm:ss",mImeData.getEndDate()));

            //Changed RecyclerView text to Red
            mGridIdView.setTextColor(Color.RED);
            mMethaneReadingView.setTextColor(Color.RED);
            mStartDateView.setTextColor(Color.RED);
            mIMEno.setTextColor(Color.RED);
        }

        @Override
        public void onClick(View v) {
//            Intent intent = ImeDataPagerActivity.newIntent(getActivity(), mImeData.getId());
//            startActivity(intent);
        }


    }

    private class ImeDataAdapter extends RecyclerView.Adapter<ImeDataHolder> {

        private List<ImeData> mImeDatas;

        public ImeDataAdapter(List<ImeData> imeDatas) {
            mImeDatas = imeDatas;
        }

        @Override
        public ImeDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_ime_data, parent, false);
            return new ImeDataHolder(view);
        }

        @Override
        public void onBindViewHolder(ImeDataHolder holder, int position) {
            ImeData imeData = mImeDatas.get(position);
            holder.bindImeData(imeData);
        }

        @Override
        public int getItemCount() {
            return mImeDatas.size();
        }

        public void setImeDatas(List<ImeData> imeDatas) {
            mImeDatas = imeDatas;
        }
    }


}
