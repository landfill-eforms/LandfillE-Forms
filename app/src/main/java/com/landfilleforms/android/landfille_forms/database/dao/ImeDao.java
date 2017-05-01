package com.landfilleforms.android.landfille_forms.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.landfilleforms.android.landfille_forms.database.Site;
import com.landfilleforms.android.landfille_forms.database.cursorwrapper.ImeDataCursorWrapper;
import com.landfilleforms.android.landfille_forms.database.LandFillBaseHelper;
import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema.ImeDataTable;
import com.landfilleforms.android.landfille_forms.model.ImeData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Work on 2/16/2017.
 */

//Done
public class ImeDao {
    public static ImeDao sImeDao;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static ImeDao get(Context context) {
        if (sImeDao == null) {
            sImeDao = new ImeDao(context);
        }
        return sImeDao;
    }

    private ImeDao(Context context) {
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
        List<ImeData> imeDatas = new ArrayList<>();

        ImeDataCursorWrapper cursor = queryImeData(null, null);//Having both values null effectively selects all

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                imeDatas.add(cursor.getImeData());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return imeDatas;
    }

    public List<ImeData> getImeDatasByLocation(String[] location) {
        List<ImeData> imeDatas = new ArrayList<>();

        //Gotta create a WHERE clause
        ImeDataCursorWrapper cursor = queryImeData(ImeDataTable.Cols.LOCATION + "= ? ", location);

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                imeDatas.add(cursor.getImeData());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return imeDatas;
    }

    public List<ImeData> getImeDatasByLocationAndIme(String[] locationAndImeNumber) {
        List<ImeData> imeDatas = new ArrayList<>();

        ImeDataCursorWrapper cursor = queryImeData(ImeDataTable.Cols.LOCATION + " = ? " + "AND " + ImeDataTable.Cols.IME_NUMBER + "= ? ", locationAndImeNumber);

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                imeDatas.add(cursor.getImeData());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return imeDatas;
    }

    public int getImeSequenceNumber (String[] location, Date currentDate) {
        List<ImeData> imeDatas = new ArrayList<>();

        ImeDataCursorWrapper cursor = queryImeData(ImeDataTable.Cols.LOCATION + "= ? ", location);

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                imeDatas.add(cursor.getImeData());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        Set<String> imeNumbers = new HashSet<String>();
        for(int i = 0; i < imeDatas.size(); i++) {
            if(currentDate.getMonth() == imeDatas.get(i).getDate().getMonth() && currentDate.getYear() == imeDatas.get(i).getDate().getYear())
                imeNumbers.add(imeDatas.get(i).getImeNumber());
        }
        return imeNumbers.size();
    }

    public Set<String> getImeNumbers (String[] location, Date currentDate) {
        List<ImeData> imeDatas = new ArrayList<>();

        //Gotta create a WHERE clause
        ImeDataCursorWrapper cursor = queryImeData(ImeDataTable.Cols.LOCATION + "= ? ", location);

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                imeDatas.add(cursor.getImeData());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        Set<String> imeNumbers = new HashSet<String>();
        for(int i = 0; i < imeDatas.size(); i++) {
            if(currentDate.getMonth() == imeDatas.get(i).getDate().getMonth() && currentDate.getYear() == imeDatas.get(i).getDate().getYear())
                imeNumbers.add(imeDatas.get(i).getImeNumber());
        }
        return imeNumbers;
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

    public String generateIMEnumber(String currentSite, Date currentDate) {
        StringBuilder sb = new StringBuilder();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        int month = cal.get(Calendar.MONTH) + 1;        //For java's Calendar, January = 0
        int year = cal.get(Calendar.YEAR);
        int sequenceNumber;

        if (currentSite.equals(Site.BISHOPS.getName()))
            sb.append(Site.BISHOPS.getShortName());
        else if (currentSite.equals(Site.GAFFEY.getName()))
            sb.append(Site.GAFFEY.getShortName());
        else if (currentSite.equals(Site.LOPEZ.getName()))
            sb.append(Site.LOPEZ.getShortName());
        else if (currentSite.equals(Site.SHELDON.getName()))
            sb.append(Site.SHELDON.getShortName());
        else if (currentSite.equals(Site.TOYON.getName()))
            sb.append(Site.TOYON.getShortName());

        sb.append(Integer.toString(year).substring(2,4));
        if(month < 10)
            sb.append(0);
        sb.append(month);
        //TODO: generate sequence number by getting info from DB(Maybe by COUNT)
        String[] args = {currentSite};
        sequenceNumber = getImeSequenceNumber(args, currentDate) + 1;
        sb.append("-");
        if(sequenceNumber < 10)
            sb.append(0);
        sb.append(sequenceNumber);

        return sb.toString();
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
