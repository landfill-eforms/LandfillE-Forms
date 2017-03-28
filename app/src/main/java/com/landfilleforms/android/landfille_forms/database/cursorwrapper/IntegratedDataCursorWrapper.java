package com.landfilleforms.android.landfille_forms.database.cursorwrapper;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema.IntegratedDataTable;
import com.landfilleforms.android.landfille_forms.model.IntegratedData;

import java.util.Date;
import java.util.UUID;


/**
 * Created by Work on 3/27/2017.
 */

public class IntegratedDataCursorWrapper extends CursorWrapper{
    public IntegratedDataCursorWrapper(Cursor cursor) { super(cursor); }

    public IntegratedData getIntegratedData() {
        String uuidString = getString(getColumnIndex(IntegratedDataTable.Cols.UUID));
        String location =  getString(getColumnIndex(IntegratedDataTable.Cols.LOCATION));
        String gridId =  getString(getColumnIndex(IntegratedDataTable.Cols.GRID_ID));
        String instrumentSerialNumber =  getString(getColumnIndex(IntegratedDataTable.Cols.INSTRUMENT_SERIAL));
        double barometricPressure =  getDouble(getColumnIndex(IntegratedDataTable.Cols.BARO_PRESSURE));
        String inspectorName =  getString(getColumnIndex(IntegratedDataTable.Cols.INSPECTOR_NAME));
        String inspectorUsername =  getString(getColumnIndex(IntegratedDataTable.Cols.INSPECTOR_USERNAME));
        String sampleId =  getString(getColumnIndex(IntegratedDataTable.Cols.SAMPLE_ID));
        int bagNumber =  getInt(getColumnIndex(IntegratedDataTable.Cols.BAG_NUMBER));
        long startDate =  getLong(getColumnIndex(IntegratedDataTable.Cols.START_DATE));
        long endDate =  getLong(getColumnIndex(IntegratedDataTable.Cols.END_DATE));
        int volumeReading =  getInt(getColumnIndex(IntegratedDataTable.Cols.VOLUME_READING));
        double methaneReading =  getDouble(getColumnIndex(IntegratedDataTable.Cols.MAX_METHANE_READING));

        IntegratedData integratedData = new IntegratedData(UUID.fromString(uuidString));
        integratedData.setLocation(location);
        integratedData.setGridId(gridId);
        integratedData.setInstrumentSerialNumber(instrumentSerialNumber);
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
