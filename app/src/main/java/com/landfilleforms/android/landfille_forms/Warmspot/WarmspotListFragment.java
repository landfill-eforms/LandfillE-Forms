package com.landfilleforms.android.landfille_forms.Warmspot;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.landfilleforms.android.landfille_forms.InstantaneousDataPagerActivity;
import com.landfilleforms.android.landfille_forms.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by owchr on 2/15/2017.
 */

//TODO: onviewbindholder line 80
public class WarmspotListFragment extends Fragment {
    private RecyclerView mWarmspotRecyclerView;
    private WarmspotAdapter mWarmspotAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_warmspot_form,container,false);
        //reuse textviews
        mWarmspotRecyclerView = (RecyclerView) v.findViewById(R.id.warmspot_data_recycler_view);
        //position the views in a vertical order
        mWarmspotRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return v;
    }

    private void updateUI(){
        WarmspotList form = WarmspotList.get(getActivity());
        List<WarmspotData> wsData = form.getWarmspotData();

        mWarmspotAdapter = new WarmspotAdapter(wsData);
        mWarmspotRecyclerView.setAdapter(mWarmspotAdapter);
    }

    //holder class for warmspot
    private class WarmspotHolder extends RecyclerView.ViewHolder {

        private WarmspotData mWarmspotData;

        private TextView mGridIdView;
        private TextView mDateView;
        private TextView mMethaneReadingView;
        private TextView mSizeView;
        private Button mEditButton;

        public WarmspotHolder(View itemView) {
            super(itemView);


            mGridIdView = (TextView) itemView.findViewById(R.id.list_item_warmspot_data_gridid_view);
            mDateView = (TextView) itemView.findViewById(R.id.list_item_warmspot_data_start_date_view);
            mMethaneReadingView = (TextView) itemView.findViewById(R.id.list_item_warmspot_data_methane_level_view);
            mSizeView = (TextView) itemView.findViewById(R.id.list_item_warmspot_size_view);
            mEditButton = (Button) itemView.findViewById(R.id.list_item_warmspot_edit_button);

            mEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = InstantaneousDataPagerActivity.newIntent(getActivity(), mWarmspotData.getId());
                    startActivity(intent);
                }
            });
        }
        public void bindData(WarmspotData warmspotData) {
            mWarmspotData = warmspotData;
            mGridIdView.setText(mWarmspotData.getGridId());
            mMethaneReadingView.setText(Double.toString(mWarmspotData.getMethaneReading()));
            mDateView.setText(new SimpleDateFormat("MM/dd").format(mWarmspotData.getDate()));//not sure if the format is correct
            mGridIdView.setTextColor(Color.rgb(255,165,0));
            mMethaneReadingView.setTextColor(Color.rgb(255,165,0));
            mDateView.setTextColor(Color.rgb(255,165,0));
            //mDateView.setText(DateFormat.format("M/d",mWarmspotData.getDate()));
        }


    }

    //adapter creates the viewholders and binding viewholders to data
    //asks adapter when it needs a view object to display
    private class WarmspotAdapter extends RecyclerView.Adapter<WarmspotHolder> {
        private List<WarmspotData> mWarmspotDataEntries;

        public WarmspotAdapter(List<WarmspotData> warmspotDataEntries) {
            this.mWarmspotDataEntries = warmspotDataEntries;

        }


        //called by recyclerviewer when it needs new view to display item
        @Override
        public WarmspotHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View v = layoutInflater.inflate(R.layout.list_item_warmspot_data,parent,false);
            return new WarmspotHolder(v);
        }

        //bind Viewholders view to model object, recieves viewholder anf position, to bind view, use position
        //to find model data, then update view
        @Override
        public void onBindViewHolder(WarmspotHolder holder, int position) {
            WarmspotData warmspotData = mWarmspotDataEntries.get(position);
            holder.bindData(warmspotData);

        }

        @Override
        public int getItemCount() {
            return mWarmspotDataEntries.size();
        }
    }


}
