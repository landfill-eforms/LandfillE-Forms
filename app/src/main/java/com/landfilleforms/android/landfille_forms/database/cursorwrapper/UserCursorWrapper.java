package com.landfilleforms.android.landfille_forms.database.cursorwrapper;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.landfilleforms.android.landfille_forms.model.User;
import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema.UsersTable;

import java.util.UUID;

/**
 * UserCursorWrapper.java
 * Purpose: Cursor wrapper class for the users table query result set.
 * It pulls the data from the current row that the cursor is at & uses the data
 * to construct a User Java object.
 */

public class UserCursorWrapper extends CursorWrapper {
    public UserCursorWrapper(Cursor cursor) { super(cursor); }

    /*
    * Returns a User object built using the data taken from each of the columns
    * from the current row.
    * */
    public User getUser() {
        String id = getString(getColumnIndex(UsersTable.Cols.ID));
        String username = getString(getColumnIndex(UsersTable.Cols.USERNAME));
        String password = getString(getColumnIndex(UsersTable.Cols.PASSWORD));
        String firstName = getString(getColumnIndex(UsersTable.Cols.FIRST_NAME));
        String middleName = getString(getColumnIndex(UsersTable.Cols.MIDDLE_NAME));
        String lastName = getString(getColumnIndex(UsersTable.Cols.LAST_NAME));
        String emailAddress = getString(getColumnIndex(UsersTable.Cols.EMAIL_ADDRESS));
        String employeeId = getString(getColumnIndex(UsersTable.Cols.EMPLOYEE_ID));
        boolean enabled = getInt(getColumnIndex(UsersTable.Cols.ENABLED)) != 0;


        User user = new User(id);
        user.setUsername(username);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setMiddleName(middleName);
        user.setLastName(lastName);
        user.setEmailAddress(emailAddress);
        user.setEmployeeId(employeeId);
        user.setEnabled(enabled);


        return user;
    }
}
