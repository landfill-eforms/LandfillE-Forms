package com.landfilleforms.android.landfille_forms.model;

import java.util.Date;
import java.util.UUID;

/**
 * IntegratedData.java
 * Purpose: Model for the IntegratedData
 */
public class IntegratedData {
    private UUID mId;
    private String mLocation;
    private String mGridId;
    private Instrument mInstrument;
    private double mBarometricPressure;
    private String mInspectorName;
    private String mInspectorUserName;
    private String mSampleId;
    private int mBagNumber;
    private Date mStartDate;
    private Date mEndDate;
    private int mVolumeReading;
    private double mMethaneReading;


    public IntegratedData() { this(UUID.randomUUID()); }

    public IntegratedData(UUID id) {
        mId = id;
        mStartDate = new Date();
        mEndDate = new Date();
        mEndDate.setTime(mStartDate.getTime() + 1800000);
    }

    public UUID getId() {
        return mId;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public String getGridId() {
        return mGridId;
    }

    public void setGridId(String gridId) {
        mGridId = gridId;
    }

    public Instrument getInstrument() {
        return mInstrument;
    }

    public void setInstrument(Instrument instrument) {
        mInstrument = instrument;
    }

    public double getBarometricPressure() {
        return mBarometricPressure;
    }

    public void setBarometricPressure(double barometricPressure) {
        mBarometricPressure = barometricPressure;
    }

    public String getInspectorName() {
        return mInspectorName;
    }

    public void setInspectorName(String inspectorName) {
        mInspectorName = inspectorName;
    }

    public String getInspectorUserName() {
        return mInspectorUserName;
    }

    public void setInspectorUserName(String inspectorUserName) {
        mInspectorUserName = inspectorUserName;
    }

    public String getSampleId() {
        return mSampleId;
    }

    public void setSampleId(String sampleId) {
        mSampleId = sampleId;
    }

    public int getBagNumber() {
        return mBagNumber;
    }

    public void setBagNumber(int bagNumber) {
        mBagNumber = bagNumber;
    }

    public Date getStartDate() {
        return mStartDate;
    }

    public void setStartDate(Date startDate) {
        mStartDate = startDate;
    }

    public Date getEndDate() {
        return mEndDate;
    }

    public void setEndDate(Date endDate) {
        mEndDate = endDate;
    }

    public int getVolumeReading() {
        return mVolumeReading;
    }

    public void setVolumeReading(int volumeReading) {
        mVolumeReading = volumeReading;
    }

    public double getMethaneReading() {
        return mMethaneReading;
    }

    public void setMethaneReading(double methaneReading) {
        mMethaneReading = methaneReading;
    }
}
