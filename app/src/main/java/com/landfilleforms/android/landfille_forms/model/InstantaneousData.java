package com.landfilleforms.android.landfille_forms.model;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Work on 10/30/2016.
 */

public class InstantaneousData {
    private UUID mId;
    private String mLocation;//Auto-generated
    private double mBarometricPressure;
    private String gridId;
    private String mInspectorName;//Should make this a user object, auto-generated
    private String mInspectorUserName;
    private Date mStartDate;
    private Date mEndDate;//This is going to be the same date as the start date but we need this to set the end time
    private String instrumentSerialNumber;
    private double methaneReading;
    private String imeNumber;//Auto-generated


    public InstantaneousData() {
        this(UUID.randomUUID());
    }

    public InstantaneousData(UUID id) {
        mId = id;
        mStartDate = new Date();
        mEndDate = new Date();
        mEndDate.setTime(mStartDate.getTime() + 1800000);
    }


    public UUID getId() {
        return mId;
    }

    public String getLandFillLocation() { return mLocation; }

    public void setLandFillLocation(String landFillLocation) { this.mLocation = landFillLocation; }

    public double getBarometricPressure() {
        return mBarometricPressure;
    }

    public void setBarometricPressure(double barometricPressure) {
        this.mBarometricPressure = barometricPressure;
    }

    public String getGridId() {
        return gridId;
    }

    public void setGridId(String gridId) {
        this.gridId = gridId;
    }

    public String getInspectorName() { return mInspectorName; }

    public void setInspectorName(String inspectorName) { this.mInspectorName = inspectorName; }

    public String getInspectorUserName() {
        return mInspectorUserName;
    }

    public void setInspectorUserName(String inspectorUserName) {
        this.mInspectorUserName = inspectorUserName;
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

    public String getInstrumentSerialNumber() { return instrumentSerialNumber; }

    public void setInstrumentSerialNumber(String instrumentSerialNumber) { this.instrumentSerialNumber = instrumentSerialNumber; }

    public double getMethaneReading() {
        return methaneReading;
    }

    public void setMethaneReading(double methaneReading) {
        this.methaneReading = methaneReading;
    }

    public String getImeNumber() { return imeNumber; }

    public void setImeNumber(String imeNumber) { this.imeNumber = imeNumber; }
}
