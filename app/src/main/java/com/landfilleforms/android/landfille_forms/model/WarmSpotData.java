package com.landfilleforms.android.landfille_forms.model;

import java.util.Date;
import java.util.UUID;

/**
 * WarmSpotData.java
 * Purpose: Model for the WarmSpotData
 */
public class WarmSpotData {
    private UUID mId;
    private String mLocation;
    private String mGridId;
    private Date mDate;
    private String mDescription;
    private String mEstimatedSize;
    private String mInspectorFullName;
    private String mInspectorUserName;
    private double mMaxMethaneReading;
    private Instrument mInstrument;

    public WarmSpotData() {
        this(UUID.randomUUID());
    }

    public WarmSpotData(UUID id) {
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

    public String getGridId() {
        return mGridId;
    }

    public void setGridId(String gridId) {
        mGridId = gridId;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getEstimatedSize() {
        return mEstimatedSize;
    }

    public void setEstimatedSize(String estimatedSize) {
        mEstimatedSize = estimatedSize;
    }

    public String getInspectorFullName() {
        return mInspectorFullName;
    }

    public void setInspectorFullName(String inspectorFullName) {
        mInspectorFullName = inspectorFullName;
    }

    public String getInspectorUserName() {
        return mInspectorUserName;
    }

    public void setInspectorUserName(String inspectorUserName) {
        mInspectorUserName = inspectorUserName;
    }

    public double getMaxMethaneReading() {
        return mMaxMethaneReading;
    }

    public void setMaxMethaneReading(double maxMethaneReading) {
        mMaxMethaneReading = maxMethaneReading;
    }

    public Instrument getInstrument() {
        return mInstrument;
    }

    public void setInstrument(Instrument instrument) {
        mInstrument = instrument;
    }
}
