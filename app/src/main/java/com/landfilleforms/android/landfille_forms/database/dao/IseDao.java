package com.landfilleforms.android.landfille_forms.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.landfilleforms.android.landfille_forms.database.cursorwrapper.IseDataCursorWrapper;
import com.landfilleforms.android.landfille_forms.database.LandFillBaseHelper;
import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema.IseDataTable;
import com.landfilleforms.android.landfille_forms.model.IseData;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by owchr on 4/5/2017.
 */

public class IseDao {
    public static IseDao sIseDao;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static IseDao get(Context context) {
        if (sIseDao == null) {
            sIseDao = new IseDao(context);
        }

        return sIseDao;
    }

    private IseDao(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new LandFillBaseHelper(mContext).getWritableDatabase();
    }

    public void addIseData(IseData d) {
        ContentValues values = getContentValues(d);
        mDatabase.insert(IseDataTable.NAME, null, values);
    }

    public void removeIseData(IseData d) {
        mDatabase.delete(IseDataTable.NAME, IseDataTable.Cols.UUID + "= ?", new String[] {d.getId().toString()});
    }

    public List<IseData> getIseDatas() {
        List<IseData> iseDatas = new ArrayList<>();
        IseDataCursorWrapper cursor = queryIseData(null, null);

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                iseDatas.add(cursor.getIseData());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return iseDatas;
    }

    public List<IseData> getIseDatasByLocation(String[] location) {
        List<IseData> iseDatas = new ArrayList<>();
        IseDataCursorWrapper cursor = queryIseData(IseDataTable.Cols.LOCATION + "= ? ", location);

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                iseDatas.add(cursor.getIseData());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return iseDatas;
    }

    public List<IseData> getIseDatasByLocationAndIse(String[] locationAndIseNumber) {
        List<IseData> iseDatas = new ArrayList<>();
        IseDataCursorWrapper cursor = queryIseData(IseDataTable.Cols.LOCATION + " = ? " + "AND " + IseDataTable.Cols.ISE_NUMBER + "= ? ", locationAndIseNumber);

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                iseDatas.add(cursor.getIseData());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return iseDatas;
    }

    public int getIseSequenceNumber (String[] location, Date currentDate) {
        List<IseData> iseDatas = new ArrayList<>();
        IseDataCursorWrapper cursor = queryIseData(IseDataTable.Cols.LOCATION + "= ? ", location);

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                iseDatas.add(cursor.getIseData());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        Set<String> iseNumbers = new HashSet<String>();

        for(int i = 0; i < iseDatas.size(); i++) {
            if(currentDate.getMonth() == iseDatas.get(i).getDate().getMonth() && currentDate.getYear() == iseDatas.get(i).getDate().getYear())
                iseNumbers.add(iseDatas.get(i).getIseNumber());
        }

        return iseNumbers.size();
    }

    public Set<String> getIseNumbers (String[] location, Date currentDate) {
        List<IseData> iseDatas = new ArrayList<>();
        IseDataCursorWrapper cursor = queryIseData(IseDataTable.Cols.LOCATION + "= ? ", location);

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                iseDatas.add(cursor.getIseData());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        Set<String> iseNumbers = new HashSet<String>();

        for(int i = 0; i < iseDatas.size(); i++) {
            if(currentDate.getMonth() == iseDatas.get(i).getDate().getMonth() && currentDate.getYear() == iseDatas.get(i).getDate().getYear())
                iseNumbers.add(iseDatas.get(i).getIseNumber());
        }

        return iseNumbers;
    }



    public IseData getIseData(UUID id) {
        IseDataCursorWrapper cursor = queryIseData(
                IseDataTable.Cols.UUID + "= ? ",
                new String[] {id.toString()}
        );
        try {
            if (cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getIseData();
        } finally {
            cursor.close();
        }
    }

    public void updateIseData(IseData iseData) {
        String uuidString = iseData.getId().toString();
        ContentValues values = getContentValues(iseData);

        mDatabase.update(IseDataTable.NAME, values,
                IseDataTable.Cols.UUID + "= ?",
                new String[] {uuidString});
    }

    private static ContentValues getContentValues(IseData iseData) {
        ContentValues values = new ContentValues();
        values.put(IseDataTable.Cols.UUID, iseData.getId().toString());
        values.put(IseDataTable.Cols.ISE_NUMBER, iseData.getIseNumber());
        values.put(IseDataTable.Cols.LOCATION, iseData.getLocation());
        values.put(IseDataTable.Cols.GRID_ID, iseData.getGridId());
        values.put(IseDataTable.Cols.DATE, iseData.getDate().getTime());
        values.put(IseDataTable.Cols.DESCRIPTION, iseData.getDescription());
        values.put(IseDataTable.Cols.INSPECTOR_NAME, iseData.getInspectorFullName());
        values.put(IseDataTable.Cols.INSPECTOR_USERNAME, iseData.getInspectorUserName());
        values.put(IseDataTable.Cols.MAX_METHANE_READING, iseData.getMethaneReading());

        return values;
    }

    private IseDataCursorWrapper queryIseData(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                IseDataTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new IseDataCursorWrapper(cursor);
    }

}
