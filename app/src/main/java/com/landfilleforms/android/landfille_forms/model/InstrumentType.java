package com.landfilleforms.android.landfille_forms.model;

import com.google.gson.annotations.SerializedName;

/**
 * InstrumentType.java
 * Purpose: Model for the InstrumentType
 */
public class InstrumentType {
    @SerializedName("id") private int mId;
    @SerializedName("type") private String mType;
    @SerializedName("manufacturer") private String mManufacturer;
    @SerializedName("description") private String mDescription;
    @SerializedName("instantaneous") private boolean mInstantaneous;
    @SerializedName("probe") private boolean mProbe;
    @SerializedName("methanePercent") private boolean mMethanePercent;
    @SerializedName("methanePpm") private boolean mMethanePpm;
    @SerializedName("hydrogenSulfidePpm") private boolean mHydrogenSulfidePpm;
    @SerializedName("oxygenPercent") private boolean mOxygenPercent;
    @SerializedName("carbonDioxidePercent") private boolean mCarbonDioxidePercent;
    @SerializedName("nitrogenPercent") private boolean mNitrogenPercent;
    @SerializedName("pressure") private boolean mPressure;

    public InstrumentType(){}

    public InstrumentType(int id) {
        this.mId = id;
    }

    public InstrumentType(int id, String type, String manufacturer, String description, boolean instantaneous, boolean probe, boolean methanePercent, boolean methanePpm, boolean hydrogenSulfidePpm, boolean oxygenPercent, boolean carbonDioxidePercent, boolean nitrogenPercent, boolean pressure) {
        this.mId = id;
        this.mType = type;
        this.mManufacturer = manufacturer;
        this.mDescription = description;
        this.mInstantaneous = instantaneous;
        this.mProbe = probe;
        this.mMethanePercent = methanePercent;
        this.mMethanePpm = methanePpm;
        this.mHydrogenSulfidePpm = hydrogenSulfidePpm;
        this.mOxygenPercent = oxygenPercent;
        this.mCarbonDioxidePercent = carbonDioxidePercent;
        this.mNitrogenPercent = nitrogenPercent;
        this.mPressure = pressure;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        this.mType = type;
    }

    public String getManufacturer() {
        return mManufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.mManufacturer = manufacturer;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public boolean isInstantaneous() {
        return mInstantaneous;
    }

    public void setInstantaneous(boolean instantaneous) {
        this.mInstantaneous = instantaneous;
    }

    public boolean isProbe() {
        return mProbe;
    }

    public void setProbe(boolean probe) {
        this.mProbe = probe;
    }

    public boolean isMethanePercent() {
        return mMethanePercent;
    }

    public void setMethanePercent(boolean methanePercent) {
        this.mMethanePercent = methanePercent;
    }

    public boolean isMethanePpm() {
        return mMethanePpm;
    }

    public void setMethanePpm(boolean methanePpm) {
        this.mMethanePpm = methanePpm;
    }

    public boolean isHydrogenSulfidePpm() {
        return mHydrogenSulfidePpm;
    }

    public void setHydrogenSulfidePpm(boolean hydrogenSulfidePpm) {
        this.mHydrogenSulfidePpm = hydrogenSulfidePpm;
    }

    public boolean isOxygenPercent() {
        return mOxygenPercent;
    }

    public void setOxygenPercent(boolean oxygenPercent) {
        this.mOxygenPercent = oxygenPercent;
    }

    public boolean isCarbonDioxidePercent() {
        return mCarbonDioxidePercent;
    }

    public void setCarbonDioxidePercent(boolean carbonDioxidePercent) {
        this.mCarbonDioxidePercent = carbonDioxidePercent;
    }

    public boolean isNitrogenPercent() {
        return mNitrogenPercent;
    }

    public void setNitrogenPercent(boolean nitrogenPercent) {
        this.mNitrogenPercent = nitrogenPercent;
    }

    public boolean isPressure() {
        return mPressure;
    }

    public void setPressure(boolean pressure) {
        this.mPressure = pressure;
    }
}