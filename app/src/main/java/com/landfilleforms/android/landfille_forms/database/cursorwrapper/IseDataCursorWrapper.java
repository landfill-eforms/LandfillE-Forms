package com.landfilleforms.android.landfille_forms.database.cursorwrapper;

import android.database.Cursor;
import android.database.CursorWrapper;
import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema.IseDataTable;
import com.landfilleforms.android.landfille_forms.model.IseData;
import java.util.Date;
import java.util.UUID;

/**
 * IseDataCursorWrapper.java
 * Purpose: Cursor wrapper class for the ise_data table query result set.
 * It pulls the data from the current row that the cursor is at & uses the data
 * to construct a IseData Java object.
 */

public class IseDataCursorWrapper extends CursorWrapper{
    public IseDataCursorWrapper(Cursor cursor) { super(cursor); }

    /*
    * Returns a IseData object built using the data taken from each of the columns
    * from the current row.
    * */
    public IseData getIseData() {
        String uuidString = getString(getColumnIndex(IseDataTable.Cols.UUID));
        String iseNumber = getString(getColumnIndex(IseDataTable.Cols.ISE_NUMBER));
        String location = getString(getColumnIndex(IseDataTable.Cols.LOCATION));
        String gridId = getString(getColumnIndex(IseDataTable.Cols.GRID_ID));
        long date = getLong(getColumnIndex(IseDataTable.Cols.DATE));
        String description = getString(getColumnIndex(IseDataTable.Cols.DESCRIPTION));
        String inspectorFullName = getString(getColumnIndex(IseDataTable.Cols.INSPECTOR_NAME));
        String inspectorUserName = getString(getColumnIndex(IseDataTable.Cols.INSPECTOR_USERNAME));
        double methaneReading = getDouble(getColumnIndex(IseDataTable.Cols.MAX_METHANE_READING));

        IseData iseData = new IseData(UUID.fromString(uuidString));
        iseData.setIseNumber(iseNumber);
        iseData.setLocation(location);
        iseData.setGridId(gridId);
        iseData.setDate(new Date(date));
        iseData.setDescription(description);
        iseData.setInspectorFullName(inspectorFullName);
        iseData.setInspectorUserName(inspectorUserName);
        iseData.setMethaneReading(methaneReading);

        return iseData;
    }
}
