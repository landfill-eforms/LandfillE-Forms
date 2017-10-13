package com.landfilleforms.android.landfille_forms.model;

import java.util.Date;
import java.util.UUID;

/**
 * ProbeData.java
 * Purpose: Model for the ProbeData
 */
public class ProbeData {
    private UUID mId;
    private String mLocation;
    private String mInstrument;
    private Date mDate;
    private String mInspectorName;
    private String mInspectorUserName;
    private double mBarometricPressure;
    private String mProbeNumber;
    private double mWaterPressure;
    private double mMethanePercentage;
    private String mRemarks;


    public ProbeData() { this(UUID.randomUUID()); }

    public ProbeData(UUID id) {
        mId = id;
        mDate = new Date();
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

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
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

    public double getBarometricPressure() {
        return mBarometricPressure;
    }

    public void setBarometricPressure(double barometricPressure) {
        mBarometricPressure = barometricPressure;
    }

    public String getProbeNumber() {
        return mProbeNumber;
    }

    public void setProbeNumber(String probeNumber) {
        mProbeNumber = probeNumber;
    }

    public double getWaterPressure() {
        return mWaterPressure;
    }

    public void setWaterPressure(double waterPressure) {
        mWaterPressure = waterPressure;
    }

    public double getMethanePercentage() {
        return mMethanePercentage;
    }

    public void setMethanePercentage(double methanePercentage) {
        mMethanePercentage = methanePercentage;
    }

    public String getRemarks() {
        return mRemarks;
    }

    public void setRemarks(String remarks) {
        mRemarks = remarks;
    }

    public String getInstrument() {
        return mInstrument;
    }

    public void setInstrument(String mInstrument) {
        this.mInstrument = mInstrument;
    }
}
