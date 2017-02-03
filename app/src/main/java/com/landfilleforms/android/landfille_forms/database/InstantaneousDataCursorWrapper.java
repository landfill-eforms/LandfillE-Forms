package com.landfilleforms.android.landfille_forms.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.landfilleforms.android.landfille_forms.InstantaneousData;
import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema.InstantaneousDataTable;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Work on 11/1/2016.
 */

public class InstantaneousDataCursorWrapper extends CursorWrapper {
    public InstantaneousDataCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public InstantaneousData getInstantaneousData() {
        String uuidString = getString(getColumnIndex(InstantaneousDataTable.Cols.UUID));
        String location = getString(getColumnIndex(InstantaneousDataTable.Cols.LOCATION));
        String gridId = getString(getColumnIndex(InstantaneousDataTable.Cols.GRID_ID));
        String inspector_name = getString(getColumnIndex(InstantaneousDataTable.Cols.INSPECTOR_NAME));
        long startDate = getLong(getColumnIndex(InstantaneousDataTable.Cols.START_DATE));
        long endDate = getLong(getColumnIndex(InstantaneousDataTable.Cols.END_DATE));
        double methaneReading = getDouble(getColumnIndex(InstantaneousDataTable.Cols.METHANE_READING));
        String ime_number = getString(getColumnIndex(InstantaneousDataTable.Cols.IME_NUMBER));

        InstantaneousData instantaneousData = new InstantaneousData(UUID.fromString(uuidString));
        instantaneousData.setLandFillLocation(location);
        instantaneousData.setGridId(gridId);
        instantaneousData.setInspectorName(inspector_name);
        instantaneousData.setStartDate(new Date(startDate));
        instantaneousData.setEndDate(new Date(endDate));
        instantaneousData.setMethaneReading(methaneReading);
        instantaneousData.setImeNumber(ime_number);

        return instantaneousData;
    }
}
