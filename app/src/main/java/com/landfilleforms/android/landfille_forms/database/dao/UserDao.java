package com.landfilleforms.android.landfille_forms.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.landfilleforms.android.landfille_forms.database.LandFillBaseHelper;
import com.landfilleforms.android.landfille_forms.database.cursorwrapper.UserCursorWrapper;
import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema.UsersTable;
import com.landfilleforms.android.landfille_forms.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Work on 11/1/2016.
 */

public class UserDao {
    public static UserDao sUserDao;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static UserDao get(Context context) {
        if (sUserDao == null) {
            sUserDao = new UserDao(context);
        }
        return sUserDao;
    }

    private UserDao(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new LandFillBaseHelper(mContext).getWritableDatabase();
    }

    public void addUser(User u) {
        ContentValues values = getContentValues(u);
        mDatabase.insert(UsersTable.NAME, null, values);
    }

    public void addUsers(List<User> users) {
        for(User u:users){
            ContentValues values = getContentValues(u);
            mDatabase.insert(UsersTable.NAME, null, values);
        }
    }

    public List<User> getUsers() {
        List<User> users = new ArrayList<>();

        UserCursorWrapper cursor = queryUsers(null, null);

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                users.add(cursor.getUser());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return users;
    }

    public User getUser(UUID id) {
        UserCursorWrapper cursor = queryUsers (
                UsersTable.Cols.ID + " = ? ",
                new String[] { id.toString()}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getUser();
        } finally {
            cursor.close();
        }
    }

    public void updateUser(User user) {
        String uuidString = user.getId().toString();
        ContentValues values = getContentValues(user);

        mDatabase.update(UsersTable.NAME, values,
                UsersTable.Cols.ID + "= ?",
                new String[] {uuidString});//We use String[] to avoid SQL injections.
    }

    private static ContentValues getContentValues(User user) {
        ContentValues values = new ContentValues();
        values.put(UsersTable.Cols.ID, user.getId());
        values.put(UsersTable.Cols.USERNAME, user.getUsername());
        values.put(UsersTable.Cols.PASSWORD, user.getPassword());
        values.put(UsersTable.Cols.FIRST_NAME, user.getFirstName());
        values.put(UsersTable.Cols.MIDDLE_NAME, user.getMiddleName());
        values.put(UsersTable.Cols.LAST_NAME, user.getLastName());
        values.put(UsersTable.Cols.EMAIL_ADDRESS, user.getEmailAddress());
        values.put(UsersTable.Cols.EMPLOYEE_ID, user.getEmployeeId());
        values.put(UsersTable.Cols.ENABLED, user.isEnabled());

        return values;
    }

    private UserCursorWrapper queryUsers(String whereClause, String[] whereArgs) {//The wrapper class reduces alot of repeated code
        Cursor cursor = mDatabase.query(
                UsersTable.NAME,    //Name of table
                null,               //Columns, having this null SELECTS all
                whereClause,        //Self-explanatory
                whereArgs,          //Self-explanatory
                null,               //groupBy
                null,               //having
                null                //orderBy
        );

        return new UserCursorWrapper(cursor);
    }
}
