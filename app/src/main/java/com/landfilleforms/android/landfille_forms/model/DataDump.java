package com.landfilleforms.android.landfille_forms.model;

import java.util.List;

/**
 * Created by Work on 2/23/2017.
 */

public class DataDump {
    List<InstantaneousData> mInstantaneousDatas;
    List<ImeData> mImeDatas;
    List<WarmSpotData> mWarmSpotDatas;

    public DataDump(List<InstantaneousData> instantaneousDatas, List<ImeData> imeDatas, List<WarmSpotData> warmSpotDatas) {
        mInstantaneousDatas = instantaneousDatas;
        mImeDatas = imeDatas;
        mWarmSpotDatas = warmSpotDatas;
    }

    public List<InstantaneousData> getInstantaneousDatas() {
        return mInstantaneousDatas;
    }

    public void setInstantaneousDatas(List<InstantaneousData> instantaneousDatas) {
        mInstantaneousDatas = instantaneousDatas;
    }

    public List<ImeData> getImeDatas() {
        return mImeDatas;
    }

    public void setImeDatas(List<ImeData> imeDatas) {
        mImeDatas = imeDatas;
    }

    public List<WarmSpotData> getWarmSpotDatas() {
        return mWarmSpotDatas;
    }

    public void setWarmSpotDatas(List<WarmSpotData> warmSpotDatas) {
        mWarmSpotDatas = warmSpotDatas;
    }
}
