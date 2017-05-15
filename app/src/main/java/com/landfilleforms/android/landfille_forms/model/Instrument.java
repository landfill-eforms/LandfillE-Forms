package com.landfilleforms.android.landfille_forms.model;

import com.google.gson.annotations.SerializedName;

/**
 * Instrument.java
 * Purpose: Model for the Instrument
 */
public class Instrument {
    @SerializedName("id") private int mId;
    @SerializedName("serialNumber") private String mSerialNumber;
    @SerializedName("instrumentType") private InstrumentType mInstrumentType;
    @SerializedName("instrumentStatus") private String mInstrumentStatus;
    @SerializedName("site") private String mSite;
    @SerializedName("description") private String mDescription;

    public Instrument(){}

    public Instrument(int id) {
        this.mId = id;
    }

    public Instrument(int id, String serialNumber, InstrumentType instrumentType, String instrumentStatus, String site, String description) {
        this.mId = id;
        this.mSerialNumber = serialNumber;
        this.mInstrumentType = instrumentType;
        this.mInstrumentStatus = instrumentStatus;
        this.mSite = site;
        this.mDescription = description;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getSerialNumber() {
        return mSerialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.mSerialNumber = serialNumber;
    }

    public InstrumentType getInstrumentType() {
        return mInstrumentType;
    }

    public void setInstrumentType(InstrumentType instrumentType) {
        this.mInstrumentType = instrumentType;
    }

    public String getInstrumentStatus() {
        return mInstrumentStatus;
    }

    public void setInstrumentStatus(String instrumentStatus) {
        this.mInstrumentStatus = instrumentStatus;
    }

    public String getSite() {
        return mSite;
    }

    public void setSite(String site) {
        this.mSite = site;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    @Override
    public String toString() {
        return mSerialNumber;
    }

    @Override
    public boolean equals(Object other) {
        if(other instanceof Instrument && this.getId() == ((Instrument)other).getId()){
            return true;
        }
        else return false;
    }

}
