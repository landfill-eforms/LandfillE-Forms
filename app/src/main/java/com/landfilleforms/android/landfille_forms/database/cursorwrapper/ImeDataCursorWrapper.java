package com.landfilleforms.android.landfille_forms.database.cursorwrapper;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.landfilleforms.android.landfille_forms.model.ImeData;
import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema.ImeDataTable;

import java.util.Date;
import java.util.UUID;

/**
 * ImeDataCursorWrapper.java
 * Purpose: Cursor wrapper class for the ime_data table query result set.
 * It pulls the data from the current row that the cursor is at & uses the data
 * to construct a ImeData Java object.
 */

public class ImeDataCursorWrapper extends CursorWrapper{
    public ImeDataCursorWrapper(Cursor cursor) { super(cursor); }

    /*
    * Returns a ImeData object built using the data taken from each of the columns
    * from the current row.
    * */
    public ImeData getImeData() {
        String uuidString = getString(getColumnIndex(ImeDataTable.Cols.UUID));
        String imeNumber = getString(getColumnIndex(ImeDataTable.Cols.IME_NUMBER));
        String location = getString(getColumnIndex(ImeDataTable.Cols.LOCATION));
        String gridId = getString(getColumnIndex(ImeDataTable.Cols.GRID_ID));
        long date = getLong(getColumnIndex(ImeDataTable.Cols.DATE));
        String description = getString(getColumnIndex(ImeDataTable.Cols.DESCRIPTION));
        String inspectorFullName = getString(getColumnIndex(ImeDataTable.Cols.INSPECTOR_NAME));
        String inspectorUserName = getString(getColumnIndex(ImeDataTable.Cols.INSPECTOR_USERNAME));
        double methaneReading = getDouble(getColumnIndex(ImeDataTable.Cols.MAX_METHANE_READING));

        ImeData imeData = new ImeData(UUID.fromString(uuidString));
        imeData.setImeNumber(imeNumber);
        imeData.setLocation(location);
        imeData.setGridId(gridId);
        imeData.setDate(new Date(date));
        imeData.setDescription(description);
        imeData.setInspectorFullName(inspectorFullName);
        imeData.setInspectorUserName(inspectorUserName);
        imeData.setMethaneReading(methaneReading);

        return imeData;
    }
}
