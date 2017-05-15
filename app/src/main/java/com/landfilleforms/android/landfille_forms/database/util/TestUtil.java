package com.landfilleforms.android.landfille_forms.database.util;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * TestUtil.java
 * Purpose: Used to be used to add a bunch of dummy data to the DB. Now it just adds the admin
 * to the DB. The default password should be 'password'.
 */
public class TestUtil {

    /**
     * Inserts the android_admin user to the DB. More of a test account than anything. Doesn't have
     * any extra privileges. The web app may not accept exported data created by this account as the
     * web application does not have this user in its database.
     * @param db
     */
    public static void insertDummyUserData(SQLiteDatabase db) {
        if (db == null){
            return;
        }

        List<ContentValues> list = new ArrayList<ContentValues>();

        ContentValues cv = new ContentValues();
        cv.put(LandFillDbSchema.UsersTable.Cols.ID, 0);
        cv.put(LandFillDbSchema.UsersTable.Cols.USERNAME, "android_admin");
        cv.put(LandFillDbSchema.UsersTable.Cols.PASSWORD, "$2a$07$RVBGN544b9xFzS//B6Y8FedBXwj0kE..3XzGeWcSyJMvBoOokpSFS");
        cv.put(LandFillDbSchema.UsersTable.Cols.FIRST_NAME, "Android");
        cv.put(LandFillDbSchema.UsersTable.Cols.MIDDLE_NAME, "");
        cv.put(LandFillDbSchema.UsersTable.Cols.LAST_NAME, "Admin");
        cv.put(LandFillDbSchema.UsersTable.Cols.EMAIL_ADDRESS, "3s.grantkang@gmail.com");
        cv.put(LandFillDbSchema.UsersTable.Cols.EMPLOYEE_ID, "");
        cv.put(LandFillDbSchema.UsersTable.Cols.ENABLED, true);
        list.add(cv);
		
        try {
            db.beginTransaction();
            db.delete(LandFillDbSchema.UsersTable.NAME,null,null);
            for(ContentValues c:list){
                db.insert(LandFillDbSchema.UsersTable.NAME, null, c);
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {

        } finally {
            db.endTransaction();
        }
    }

}
