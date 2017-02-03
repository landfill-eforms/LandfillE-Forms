package com.landfilleforms.android.landfille_forms.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.landfilleforms.android.landfille_forms.User;
import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema.UsersTable;

import java.util.UUID;

/**
 * Created by Work on 11/1/2016.
 */

public class UserCursorWrapper extends CursorWrapper {
    public UserCursorWrapper(Cursor cursor) { super(cursor); }

    public User getUser() {
        String uuidString = getString(getColumnIndex(UsersTable.Cols.UUID));
        String username = getString(getColumnIndex(UsersTable.Cols.USERNAME));
        String password = getString(getColumnIndex(UsersTable.Cols.PASSWORD));
        String fullName = getString(getColumnIndex(UsersTable.Cols.FULLNAME));


        User user = new User(UUID.fromString(uuidString));
        user.setUsername(username);
        user.setPassword(password);
        user.setFullName(fullName);


        return user;
    }
}
