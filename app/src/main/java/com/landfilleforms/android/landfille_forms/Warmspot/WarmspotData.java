package com.landfilleforms.android.landfille_forms.Warmspot;

import java.util.Date;
import java.util.UUID;

/**
 * Created by owchr on 2/15/2017.
 */

public class WarmspotData {
    private UUID mId;
    private String mLocation;
    private String mGridId;
    private Date mDate;
    private String mDescription;
    private String mEstimatedSize;
    private String mInspectorFullName;
    private String mInspectorUserName;
    private double mMaxMethaneReading;
    private String mInstrumentSerial;




    public WarmspotData() {
        this(UUID.randomUUID());
    }


    public WarmspotData(UUID id) {
        mId = id;
        mDate = new Date();

    }

    public UUID getId() {
        return mId;
    }


    public Date getDate() {
        return mDate;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getSize() {
        return mEstimatedSize;
    }

    public void setSize(String size) {
        this.mEstimatedSize = size;
    }

    public String getInspectorUserName() {
        return mInspectorUserName;
    }

    public void setInspectorUserName(String inspectorUserName) {
        this.mInspectorUserName = inspectorUserName;
    }

    public double getMethaneReading() {
        return mMaxMethaneReading;
    }

    public void setMethaneReading(double methaneReading) {
        this.mMaxMethaneReading = methaneReading;
    }

    public String getInstrumentSerialNumber() {
        return mInstrumentSerial;
    }

    public void setInstrumentSerialNumber(String instrumentSerialNumber) {
        this.mInstrumentSerial = instrumentSerialNumber;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        this.mLocation = location;
    }

    public String getGridId() {
        return mGridId;
    }

    public void setGridId(String mGridId) {
        this.mGridId = mGridId;
    }

    public String getInspectorFullName() {
        return mInspectorFullName;
    }

    public void setmnspectorFullName(String mInspectorFullName) {
        this.mInspectorFullName = mInspectorFullName;
    }
}
