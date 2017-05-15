package com.landfilleforms.android.landfille_forms.database.cursorwrapper;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema.InstrumentsTable;
import com.landfilleforms.android.landfille_forms.database.dao.InstrumentTypeDao;
import com.landfilleforms.android.landfille_forms.model.Instrument;
import com.landfilleforms.android.landfille_forms.model.InstrumentType;

/**
 * InstrumentCursorWrapper.java
 * Purpose: Cursor wrapper class for the instruments table query result set.
 * It pulls the data from the current row that the cursor is at & uses the data
 * to construct a Instrument Java object.
 */

public class InstrumentCursorWrapper extends CursorWrapper {
    public InstrumentCursorWrapper(Cursor cursor) { super(cursor); }

    /*
    * Returns a Instrument object built using the data taken from each of the columns
    * from the current row.
    * */
    public Instrument getInstrument() {
        int id = getInt(getColumnIndex(InstrumentsTable.Cols.ID));
        String serialNumber = getString(getColumnIndex(InstrumentsTable.Cols.SERIAL_NUMBER));
        String instrumentStatus = getString(getColumnIndex(InstrumentsTable.Cols.INSTRUMENT_STATUS));
        String site = getString(getColumnIndex(InstrumentsTable.Cols.SITE));
        String description = getString(getColumnIndex(InstrumentsTable.Cols.DESCRIPTION));
        InstrumentType instrumentType = new InstrumentType(getInt(getColumnIndex(InstrumentsTable.Cols.INSTRUMENT_TYPE_ID)));

        Instrument instrument = new Instrument(id);
        instrument.setSerialNumber(serialNumber);
        instrument.setInstrumentStatus(instrumentStatus);
        instrument.setSite(site);
        instrument.setDescription(description);
        instrument.setInstrumentType(instrumentType);

        return instrument;
    }
}
