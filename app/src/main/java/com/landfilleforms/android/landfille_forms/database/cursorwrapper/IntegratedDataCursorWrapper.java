package com.landfilleforms.android.landfille_forms.database.cursorwrapper;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema.IntegratedDataTable;
import com.landfilleforms.android.landfille_forms.model.Instrument;
import com.landfilleforms.android.landfille_forms.model.IntegratedData;

import java.util.Date;
import java.util.UUID;


/**
 * IntegratedDataCursorWrapper.java
 * Purpose: Cursor wrapper class for the integrated_data table query result set.
 * It pulls the data from the current row that the cursor is at & uses the data
 * to construct a IntegratedData Java object.
 */

public class IntegratedDataCursorWrapper extends CursorWrapper{
    public IntegratedDataCursorWrapper(Cursor cursor) { super(cursor); }

    /*
    * Returns a IntegratedData object built using the data taken from each of the columns
    * from the current row.
    * */
    public IntegratedData getIntegratedData() {
        String uuidString = getString(getColumnIndex(IntegratedDataTable.Cols.UUID));
        String location =  getString(getColumnIndex(IntegratedDataTable.Cols.LOCATION));
        String gridId =  getString(getColumnIndex(IntegratedDataTable.Cols.GRID_ID));
        double barometricPressure =  getDouble(getColumnIndex(IntegratedDataTable.Cols.BARO_PRESSURE));
        String inspectorName =  getString(getColumnIndex(IntegratedDataTable.Cols.INSPECTOR_NAME));
        String inspectorUsername =  getString(getColumnIndex(IntegratedDataTable.Cols.INSPECTOR_USERNAME));
        String sampleId =  getString(getColumnIndex(IntegratedDataTable.Cols.SAMPLE_ID));
        int bagNumber =  getInt(getColumnIndex(IntegratedDataTable.Cols.BAG_NUMBER));
        long startDate =  getLong(getColumnIndex(IntegratedDataTable.Cols.START_DATE));
        long endDate =  getLong(getColumnIndex(IntegratedDataTable.Cols.END_DATE));
        int volumeReading =  getInt(getColumnIndex(IntegratedDataTable.Cols.VOLUME_READING));
        double methaneReading =  getDouble(getColumnIndex(IntegratedDataTable.Cols.MAX_METHANE_READING));
        Instrument instrument = new Instrument(getInt(getColumnIndex(IntegratedDataTable.Cols.INSTRUMENT_ID)));

        IntegratedData integratedData = new IntegratedData(UUID.fromString(uuidString));
        integratedData.setLocation(location);
        integratedData.setGridId(gridId);
        integratedData.setInstrument(instrument);
        integratedData.setBarometricPressure(barometricPressure);
        integratedData.setInspectorName(inspectorName);
        integratedData.setInspectorUserName(inspectorUsername);
        integratedData.setSampleId(sampleId);
        integratedData.setBagNumber(bagNumber);
        integratedData.setStartDate(new Date(startDate));
        integratedData.setEndDate(new Date(endDate));
        integratedData.setVolumeReading(volumeReading);
        integratedData.setMethaneReading(methaneReading);

        return integratedData;
    }
}
