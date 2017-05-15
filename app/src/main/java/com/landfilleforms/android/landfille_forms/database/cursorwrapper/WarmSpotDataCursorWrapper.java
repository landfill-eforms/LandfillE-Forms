package com.landfilleforms.android.landfille_forms.database.cursorwrapper;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.landfilleforms.android.landfille_forms.model.Instrument;
import com.landfilleforms.android.landfille_forms.model.WarmSpotData;
import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema.WarmSpotDataTable;

import java.util.Date;
import java.util.UUID;

/**
 * WarmSpotDataCursorWrapper.java
 * Purpose: Cursor wrapper class for the warmspot_data table query result set.
 * It pulls the data from the current row that the cursor is at & uses the data
 * to construct a WarmSpotData Java object.
 */

public class WarmSpotDataCursorWrapper extends CursorWrapper{
    public WarmSpotDataCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    /*
    * Returns a WarmSpotData object built using the data taken from each of the columns
    * from the current row.
    * */
    public WarmSpotData getWarmSpotData() {
        String uuidString = getString(getColumnIndex(WarmSpotDataTable.Cols.UUID));
        String location = getString(getColumnIndex(WarmSpotDataTable.Cols.LOCATION));
        String gridId = getString(getColumnIndex(WarmSpotDataTable.Cols.GRID_ID));
        long date = getLong(getColumnIndex(WarmSpotDataTable.Cols.DATE));
        String description = getString(getColumnIndex(WarmSpotDataTable.Cols.DESCRIPTION));
        String estimatedSize = getString(getColumnIndex(WarmSpotDataTable.Cols.ESTIMATED_SIZE));
        String inspectorFullName = getString(getColumnIndex(WarmSpotDataTable.Cols.INSPECTOR_NAME));
        String inspectorUserName = getString(getColumnIndex(WarmSpotDataTable.Cols.INSPECTOR_USERNAME));
        double methaneReading = getDouble(getColumnIndex(WarmSpotDataTable.Cols.MAX_METHANE_READING));
        Instrument instrument = new Instrument(getInt(getColumnIndex(WarmSpotDataTable.Cols.INSTRUMENT_ID)));

        WarmSpotData warmSpotData = new WarmSpotData(UUID.fromString(uuidString));
        warmSpotData.setLocation(location);
        warmSpotData.setGridId(gridId);
        warmSpotData.setDate(new Date(date));
        warmSpotData.setDescription(description);
        warmSpotData.setEstimatedSize(estimatedSize);
        warmSpotData.setInspectorFullName(inspectorFullName);
        warmSpotData.setInspectorUserName(inspectorUserName);
        warmSpotData.setMaxMethaneReading(methaneReading);
        warmSpotData.setInstrument(instrument);

        return warmSpotData;
    }
}
