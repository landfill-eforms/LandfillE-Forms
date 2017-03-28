package com.landfilleforms.android.landfille_forms.database.cursorwrapper;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema.ProbeDataTable;
import com.landfilleforms.android.landfille_forms.model.ProbeData;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Work on 3/27/2017.
 */

public class ProbeDataCursorWrapper extends CursorWrapper {
    public ProbeDataCursorWrapper(Cursor cursor) { super(cursor); }

    public ProbeData getProbeData() {
        String uuidString = getString(getColumnIndex(ProbeDataTable.Cols.UUID));
        String location = getString(getColumnIndex(ProbeDataTable.Cols.LOCATION));
        long date = getLong(getColumnIndex(ProbeDataTable.Cols.DATE));
        String inspectorName = getString(getColumnIndex(ProbeDataTable.Cols.INSPECTOR_NAME));
        String inspectorUserName = getString(getColumnIndex(ProbeDataTable.Cols.INSPECTOR_USERNAME));
        double barometricPressure = getDouble(getColumnIndex(ProbeDataTable.Cols.BARO_PRESSURE));
        String probeNumber = getString(getColumnIndex(ProbeDataTable.Cols.PROBE_NUMBER));
        double waterPressure = getDouble(getColumnIndex(ProbeDataTable.Cols.WATER_PRESSURE));
        double methanePercentage = getDouble(getColumnIndex(ProbeDataTable.Cols.METHANE_PERCENTAGE));
        String remarks = getString(getColumnIndex(ProbeDataTable.Cols.REMARKS));

        ProbeData probeData = new ProbeData(UUID.fromString(uuidString));
        probeData.setLocation(location);
        probeData.setDate(new Date(date));
        probeData.setInspectorName(inspectorName);
        probeData.setInspectorUserName(inspectorUserName);
        probeData.setBarometricPressure(barometricPressure);
        probeData.setProbeNumber(probeNumber);
        probeData.setWaterPressure(waterPressure);
        probeData.setMethanePercentage(methanePercentage);
        probeData.setRemarks(remarks);

        return probeData;
    }
}
