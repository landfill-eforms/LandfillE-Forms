package com.landfilleforms.android.landfille_forms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by Work on 2/3/2017.
 * To keep track of user logged in
 * Got some help by looking online for 'Android login sessions with SharedPrefereces'
 * Sources: http://www.androidhive.info/2012/08/android-session-management-using-shared-preferences/
 *          https://developer.android.com/reference/android/content/SharedPreferences.html
 *
 * Might change it so that it only uses the primary key from the User table and just use the table to query for user info
 */

public class SessionManager {
    private SharedPreferences pref;

    private Editor editor;

    protected Context context;

    private static final String PREF_NAME = "LandFillPref";

    private static final String IS_LOGGED_IN = "IsLoggedIn";

    public static final String KEY_USERNAME = "userName";

    public static final String KEY_USERFULLNAME = "fullName";


    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, 0);
        editor = pref.edit();
    }

    public void createLoginSession(String userName, String fullName) {
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(KEY_USERNAME, userName);
        editor.putString(KEY_USERFULLNAME, fullName);

        editor.commit();
    }

    public void checkLogin() {
        if(!this.isLoggedIn()){
            Log.d("checkLogin()","false");
            Intent i = new Intent(context, LoginActivity.class);

            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(i);
        }
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGGED_IN, false);
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));
        user.put(KEY_USERFULLNAME, pref.getString(KEY_USERFULLNAME, null));
        return user;
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();

        Intent i = new Intent(context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(i);
    }

}
