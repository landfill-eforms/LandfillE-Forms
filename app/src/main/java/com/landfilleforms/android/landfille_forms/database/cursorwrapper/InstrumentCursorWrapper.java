package com.landfilleforms.android.landfille_forms.database.cursorwrapper;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema.InstrumentsTable;
import com.landfilleforms.android.landfille_forms.database.dao.InstrumentTypeDao;
import com.landfilleforms.android.landfille_forms.model.Instrument;
import com.landfilleforms.android.landfille_forms.model.InstrumentType;

public class InstrumentCursorWrapper extends CursorWrapper {
    public InstrumentCursorWrapper(Cursor cursor) { super(cursor); }

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
