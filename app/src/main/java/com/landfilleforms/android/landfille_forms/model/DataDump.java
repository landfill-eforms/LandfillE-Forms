package com.landfilleforms.android.landfille_forms.model;

import java.util.List;

/**
 * DataDump.java
 * Purpose: Object used to get all the data so that it can be exported.
 */
public class DataDump {
    List<InstantaneousData> mInstantaneousDatas;
    List<ImeData> mImeDatas;
    List<WarmSpotData> mWarmSpotDatas;
    List<ProbeData> mProbeDatas;
    List<IntegratedData> mIntegratedDatas;
    List<IseData> mIseDatas;

    public DataDump(List<InstantaneousData> instantaneousDatas, List<ImeData> imeDatas, List<WarmSpotData> warmSpotDatas, List<IntegratedData> integratedDatas
    ,List<IseData> iseDatas, List<ProbeData> probeDatas) {
        mInstantaneousDatas = instantaneousDatas;
        mImeDatas = imeDatas;
        mWarmSpotDatas = warmSpotDatas;
        mIntegratedDatas = integratedDatas;
        mIseDatas = iseDatas;
        mProbeDatas = probeDatas;
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

    public List<IntegratedData> getIntegratedDatas() {
        return mIntegratedDatas;
    }

    public void setIntegratedDatas(List<IntegratedData> integratedDatas) {
        mIntegratedDatas = integratedDatas;
    }

    public List<IseData> getIseDatas() {
        return mIseDatas;
    }

    public void setIseDatas(List<IseData> iseDatas) {
            mIseDatas = iseDatas;
    }

    public List<ProbeData> getProbeDatas() {
        return mProbeDatas;
    }

    public void setProbeDatas(List<ProbeData> probeDatas) {
        mProbeDatas = probeDatas;
    }

    public boolean containsNoData() {
        if (mInstantaneousDatas.isEmpty() && mImeDatas.isEmpty() && mWarmSpotDatas.isEmpty() && mIntegratedDatas.isEmpty()
                && mIseDatas.isEmpty() && mProbeDatas.isEmpty()) {
            return true;
        }
        else{
            return false;
        }
    }
}
