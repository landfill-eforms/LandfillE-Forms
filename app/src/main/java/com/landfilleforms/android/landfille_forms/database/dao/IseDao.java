package com.landfilleforms.android.landfille_forms.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.landfilleforms.android.landfille_forms.database.Site;
import com.landfilleforms.android.landfille_forms.database.cursorwrapper.IseDataCursorWrapper;
import com.landfilleforms.android.landfille_forms.database.LandFillBaseHelper;
import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema.IseDataTable;
import com.landfilleforms.android.landfille_forms.model.IseData;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * IseDao.java
 * Purpose: Data access object class for IseData. Instead of using raw SQL queries, we use this
 * class to access the DB to do basic CRUD operations on the ise_data table.
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

    /**
     * Adds a IseData to the DB.
     * @param d The IseData to be added.
     */
    public void addIseData(IseData d) {
        ContentValues values = getContentValues(d);
        mDatabase.insert(IseDataTable.NAME, null, values);
    }

    /**
     * Removes a IseData from the DB.
     * @param d The IseData to be deleted.
     */
    public void removeIseData(IseData d) {
        mDatabase.delete(IseDataTable.NAME, IseDataTable.Cols.UUID + "= ?", new String[] {d.getId().toString()});
    }

    /**
     * Retrieves a list of all IseData in the DB.
     */
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

    /**
     * Gets a list of IseData based on the location.
     * @param location A string array containing the location.
     */
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

    /**
     * Gets a list of IseData based on the location & ise_number.
     * @param locationAndIseNumber A string array containing the location & ise_number.
     */
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

    /**
     * Gets the sequence number that's to be added to the end of a newly created ISE number.
     * The way it currently does this is bad and should be fixed. It currently generates the
     * sequence number based on the number of ise numbers. There will be situations where an
     * already used sequence number will be generated again.
     * @param location The location that will be used to query the DB
     * @param currentDate The date that will be used to query the DB.
     */
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

    /**
     * Get a set of iseNumbers based on location and date.
     * @param location The location that will be used to query the DB
     * @param currentDate The date that will be used to query the DB.
     */
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


    /**
     * Retrieves a IseData object from the ise_data table.
     * @param id The UUID of the IseData object to be retrieved.
     */
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

    /**
     * Updates an entry from the ise_data table.
     * @param iseData The ise data to be updated.
     */
    public void updateIseData(IseData iseData) {
        String uuidString = iseData.getId().toString();
        ContentValues values = getContentValues(iseData);

        mDatabase.update(IseDataTable.NAME, values,
                IseDataTable.Cols.UUID + "= ?",
                new String[] {uuidString});
    }

    /**
     * Generates an ISE number by using the date & location.
     * @param currentSite The site where the ISE is located.
     * @param currentDate The date when the ISE data entry was made.
     */
    public String generateIseNumber(String currentSite, Date currentDate) {
        StringBuilder sb = new StringBuilder();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        int month = cal.get(Calendar.MONTH) + 1;
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
        sequenceNumber = getIseSequenceNumber(args, currentDate) + 1;
        sb.append("-");
        if(sequenceNumber < 10)
            sb.append(0);
        sb.append(sequenceNumber);

        return sb.toString();
    }

    /**
     * Takes the content of an IseData so we can use them to update/add entries from/to the ise_data
     * table in our database.
     * @param iseData The IseData object that content values are from.
     */
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

    /**
     * Returns a cursor wrapper for the ise_data query result set.
     * @param whereClause The where clause for the query.
     * @param whereArgs The where arguments for the query.
     */
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
