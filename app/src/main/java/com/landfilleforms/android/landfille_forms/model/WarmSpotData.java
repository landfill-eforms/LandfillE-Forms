package com.landfilleforms.android.landfille_forms.model;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Work on 2/16/2017.
 */

public class WarmSpotData {
    private UUID mId;
    private String mLocation;
    private String mGridId;
    private Date mDate;
    private String mDescription;
    private double mEstimatedSize;
    private String mInspectorFullName;
    private String mInspectorUserName;
    private double mMaxMethaneReading;
    private String mInstrumentSerial;

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

    public double getEstimatedSize() {
        return mEstimatedSize;
    }

    public void setEstimatedSize(double estimatedSize) {
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

    public String getInstrumentSerial() {
        return mInstrumentSerial;
    }

    public void setInstrumentSerial(String instrumentSerial) {
        mInstrumentSerial = instrumentSerial;
    }
}
