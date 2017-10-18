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
 * ImeDao.java
 * Purpose: Data access object class for ImeData. Instead of using raw SQL queries, we use this
 * class to access the DB to do basic CRUD operations on the ime_data table.
 */
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

    /**
     * Adds a ImeData to the DB.
     * @param d The ImeData to be added.
     */
    public void addImeData(ImeData d) {
        ContentValues values = getContentValues(d);
        mDatabase.insert(ImeDataTable.NAME, null, values);
    }

    /**
     * Removes a ImeData from the DB.
     * @param d The ImeData to be deleted.
     */
    public void removeImeData(ImeData d) {
        mDatabase.delete(ImeDataTable.NAME, ImeDataTable.Cols.UUID + "= ?", new String[] {d.getId().toString()});
    }

    /**
     * Gets a list of all ImeData in the DB.
     */
    public List<ImeData> getImeDatas() {
        List<ImeData> imeDatas = new ArrayList<>();
        ImeDataCursorWrapper cursor = queryImeData(null, null);
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

    /**
     * Gets a list of ImeData based on the location.
     * @param location A string array containing the location.
     */
    public List<ImeData> getImeDatasByLocation(String[] location) {
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
        return imeDatas;
    }

    /**
     * Gets a list of ImeData based on the location & imeNumber.
     * @param locationAndImeNumber A string array containing the location & ime_number.
     */
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

    /**
     * Gets the sequence number that's to be added to the end of a newly created IME number.
     * The way it currently does this is bad and should be fixed. It currently generates the
     * sequence number based on the number of ime numbers. There will be situations where an
     * already used sequence number will be generated again.
     * @param location The location that will be used to query the DB
     * @param currentDate The date that will be used to query the DB.
     */
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

    /**
     * Get a set of imeNumbers based on location and date.
     * @param location The location that will be used to query the DB
     * @param currentDate The date that will be used to query the DB.
     */
    public Set<String> getImeNumbers (String[] location, Date currentDate) {
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
        return imeNumbers;
    }

    /**
     * Retrieves a ImeData object from the ime_data table.
     * @param id The UUID of the ImeData object to be retrieved.
     */
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

    /**
     * Updates an entry from the ime_data table.
     * @param imeData The ime data to be updated.
     */
    public void updateImeData(ImeData imeData) {
        String uuidString = imeData.getId().toString();
        ContentValues values = getContentValues(imeData);
        mDatabase.update(ImeDataTable.NAME, values,
                ImeDataTable.Cols.UUID + "= ?",
                new String[] {uuidString});
    }

    /**
     * Generates an IME number by using the date & location.
     * @param currentSite The site where the IME is located.
     * @param currentDate The date when the IME data entry was made.
     */
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
        String[] args = {currentSite};
        sequenceNumber = getImeSequenceNumber(args, currentDate) + 1;
        sb.append("-");
        if(sequenceNumber < 10)
            sb.append(0);
        sb.append(sequenceNumber);

        return sb.toString();
    }

    /**
     * Takes the content of an ImeData so we can use them to update/add entries from/to the ime_data
     * table in our database.
     * @param imeData The ImeData object that content values are from.
     */
    private static ContentValues getContentValues(ImeData imeData) {
        ContentValues values = new ContentValues();
        values.put(ImeDataTable.Cols.UUID, imeData.getId().toString());
        values.put(ImeDataTable.Cols.IME_NUMBER, imeData.getImeNumber());
        values.put(ImeDataTable.Cols.INSTRUMENT, imeData.getInstrument());
        values.put(ImeDataTable.Cols.LOCATION, imeData.getLocation());
        values.put(ImeDataTable.Cols.GRIDS, imeData.getGrids());
        values.put(ImeDataTable.Cols.DATE, imeData.getDate().getTime());
        values.put(ImeDataTable.Cols.DESCRIPTION, imeData.getDescription());
        values.put(ImeDataTable.Cols.INSPECTOR_NAME, imeData.getInspectorFullName());
        values.put(ImeDataTable.Cols.INSPECTOR_USERNAME, imeData.getInspectorUserName());
        values.put(ImeDataTable.Cols.MAX_METHANE_READING, imeData.getMethaneReading());

        return values;
    }

    /**
     * Returns a cursor wrapper for the ime_data query result set.
     * @param whereClause The where clause for the query.
     * @param whereArgs The where arguments for the query.
     */
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
