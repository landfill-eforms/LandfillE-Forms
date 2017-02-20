package com.landfilleforms.android.landfille_forms.ime;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.landfilleforms.android.landfille_forms.database.ImeDataCursorWrapper;
import com.landfilleforms.android.landfille_forms.database.LandFillBaseHelper;
import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema.ImeDataTable;
import com.landfilleforms.android.landfille_forms.model.ImeData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Work on 2/16/2017.
 */

//Done
public class ImeForm {
    public static ImeForm sImeForm;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static ImeForm get(Context context) {
        if (sImeForm == null) {
            sImeForm = new ImeForm(context);
        }
        return sImeForm;
    }

    private ImeForm(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new LandFillBaseHelper(mContext).getWritableDatabase();
    }

    public void addImeData(ImeData d) {
        ContentValues values = getContentValues(d);
        mDatabase.insert(ImeDataTable.NAME, null, values);
    }

    public void removeImeData(ImeData d) {
        mDatabase.delete(ImeDataTable.NAME, ImeDataTable.Cols.UUID + "= ?", new String[] {d.getId().toString()});
    }

    public List<ImeData> getImeDatas() {
        List<ImeData> imeForm = new ArrayList<>();

        ImeDataCursorWrapper cursor = queryImeData(null, null);//Having both values null effectively selects all

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                imeForm.add(cursor.getImeData());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return imeForm;
    }

    public ImeData getImeData(UUID id) {
        ImeDataCursorWrapper cursor = queryImeData(
                ImeDataTable.Cols.UUID + "= ? ",
                new String[] {id.toString()}
        );
        try {
            if (cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getImeData();
        } finally {
            cursor.close();
        }
    }

    public void updateImeData(ImeData imeData) {
        String uuidString = imeData.getId().toString();
        ContentValues values = getContentValues(imeData);

        mDatabase.update(ImeDataTable.NAME, values,
                ImeDataTable.Cols.UUID + "= ?",
                new String[] {uuidString});
    }

    private static ContentValues getContentValues(ImeData imeData) {
        ContentValues values = new ContentValues();
        values.put(ImeDataTable.Cols.UUID, imeData.getId().toString());
        values.put(ImeDataTable.Cols.IME_NUMBER, imeData.getImeNumber());
        values.put(ImeDataTable.Cols.LOCATION, imeData.getLocation());
        values.put(ImeDataTable.Cols.GRID_ID, imeData.getGridId());
        values.put(ImeDataTable.Cols.DATE, imeData.getDate().getTime());
        values.put(ImeDataTable.Cols.DESCRIPTION, imeData.getDescription());
        values.put(ImeDataTable.Cols.INSPECTOR_NAME, imeData.getInspectorFullName());
        values.put(ImeDataTable.Cols.INSPECTOR_USERNAME, imeData.getInspectorUserName());
        values.put(ImeDataTable.Cols.MAX_METHANE_READING, imeData.getMethaneReading());

        return values;
    }

    private ImeDataCursorWrapper queryImeData(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ImeDataTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new ImeDataCursorWrapper(cursor);
    }

}
