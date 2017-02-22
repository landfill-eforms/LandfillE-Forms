package com.landfilleforms.android.landfille_forms.ime;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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


import com.landfilleforms.android.landfille_forms.R;
import com.landfilleforms.android.landfille_forms.SessionManager;
import com.landfilleforms.android.landfille_forms.model.ImeData;
import com.landfilleforms.android.landfille_forms.model.User;


import java.util.HashMap;
import java.util.List;

// TODO: Create Layout file, include all the fields
public class ImeFormFragment extends Fragment {
    private static final String TAG = "ImeFormFrag";
    private static final String EXTRA_LANDFILL_LOCATION = "com.landfilleforms.android.landfille_forms.landfill_location";
    private SessionManager session;
    private User mUser;


    private RecyclerView mImeDataRecyclerView;
    private ImeDataAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");

        session = new SessionManager(getActivity().getApplicationContext());
        session.checkLogin();

        HashMap<String,String> currentUser = session.getUserDetails();
        mUser = new User();
        mUser.setUsername(currentUser.get(SessionManager.KEY_USERNAME));
        Log.d("UserName:", mUser.getUsername());
        mUser.setFullName(currentUser.get(SessionManager.KEY_USERFULLNAME));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ime_form, container, false);

        mImeDataRecyclerView = (RecyclerView) view.findViewById(R.id.ime_data_recycler_view);
        mImeDataRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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
        inflater.inflate(R.menu.fragment_ime_form, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_ime:
                ImeData imeData = new ImeData();
                //Log.d("From FormFrag",getActivity().getIntent().getStringExtra(EXTRA_USERNAME));
                imeData.setLocation(this.getActivity().getIntent().getStringExtra(EXTRA_LANDFILL_LOCATION));
                imeData.setInspectorFullName(mUser.getFullName());
                imeData.setInspectorUserName(mUser.getUsername());
                ImeForm.get(getActivity()).addImeData(imeData);
                Intent intent = ImeDataPagerActivity.newIntent(getActivity(),imeData.getId());
                Log.d(TAG,"Hoi");
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI() {
        ImeForm imeForm = ImeForm.get(getActivity());
        List<ImeData> imeDatas = imeForm.getImeDatas();

        if(mAdapter == null) {
            mAdapter = new ImeDataAdapter(imeDatas);
            mImeDataRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setImeDatas(imeDatas);
            mAdapter.notifyDataSetChanged();
        }
    }

    //For RecyleView
    private class ImeDataHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImeData mImeData;

        private TextView mGridIdView;
        private TextView mStartDateView;

        private TextView mMethaneReadingView;
        private Button mEditButton;


        public ImeDataHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);

            mGridIdView = (TextView) itemView.findViewById(R.id.list_item_ime_data_gridid_view);
            mMethaneReadingView = (TextView) itemView.findViewById(R.id.list_item_ime_data_methane_level_view);
            mStartDateView = (TextView) itemView.findViewById(R.id.list_item_ime_date_view);

            mEditButton = (Button)itemView.findViewById(R.id.list_item_ime_edit_button);
            mEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = ImeDataPagerActivity.newIntent(getActivity(), mImeData.getId());
                    startActivity(intent);
                }
            });
        }

        public void bindImeData(ImeData imeData) {
            mImeData = imeData;
            mGridIdView.setText(mImeData.getGridId());
            mMethaneReadingView.setText(Double.toString(mImeData.getMethaneReading()));
            mStartDateView.setText(DateFormat.format("M/d/yyyy",mImeData.getDate()));
            //mStartTimeView.setText(DateFormat.format("HH:mm:ss",mImeData.getStartDate()));
            //mEndTimeView.setText(DateFormat.format("HH:mm:ss",mImeData.getEndDate()));

            //Changed RecyclerView text to White
            mGridIdView.setTextColor(Color.WHITE);
            mMethaneReadingView.setTextColor(Color.WHITE);
            mStartDateView.setTextColor(Color.WHITE);
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
