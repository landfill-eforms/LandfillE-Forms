package com.landfilleforms.android.landfille_forms;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Work on 10/30/2016.
 */

public class InstantaneousData {
    private UUID mId;
    private String landFillLocation;//Auto-generated
    private String gridId;
    private String inspectorName;//Should make this a user object, auto-generated
    private Date mStartDate;
    private Date mEndDate;//This is going to be the same date as the start date but we need this to set the end time
    private double methaneReading;
    private String imeNumber;//Auto-generated


    public InstantaneousData() {
        this(UUID.randomUUID());
    }

    public InstantaneousData(UUID id) {
        mId = id;
        mStartDate = new Date();
        mEndDate = new Date();
    }


    public UUID getId() {
        return mId;
    }

    public String getLandFillLocation() { return landFillLocation; }

    public void setLandFillLocation(String landFillLocation) { this.landFillLocation = landFillLocation; }

    public String getGridId() {
        return gridId;
    }

    public void setGridId(String gridId) {
        this.gridId = gridId;
    }

    public String getInspectorName() { return inspectorName; }

    public void setInspectorName(String inspectorName) { this.inspectorName = inspectorName; }

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

    public double getMethaneReading() {
        return methaneReading;
    }

    public void setMethaneReading(double methaneReading) {
        this.methaneReading = methaneReading;
    }

    public String getImeNumber() { return imeNumber; }

    public void setImeNumber(String imeNumber) { this.imeNumber = imeNumber; }
}
