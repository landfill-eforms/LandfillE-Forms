package com.landfilleforms.android.landfille_forms.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.google.gson.Gson;
import com.landfilleforms.android.landfille_forms.activities_and_fragments.LoginActivity;
import com.landfilleforms.android.landfille_forms.model.User;

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

    public static final String KEY_USER_JSON = "currentUserJson";

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, 0);
        editor = pref.edit();
    }

    public void createLoginSession(User currentUser) {
        editor.putBoolean(IS_LOGGED_IN, true);
        Gson gson = new Gson();
        String userJson = gson.toJson(currentUser);
        editor.putString(KEY_USER_JSON, userJson);
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

    public User getCurrentUser() {
        Gson gson = new Gson();
        String json = pref.getString(KEY_USER_JSON, "");
        return gson.fromJson(json, User.class);
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
