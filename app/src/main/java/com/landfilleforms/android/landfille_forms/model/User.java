package com.landfilleforms.android.landfille_forms.model;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

/**
 * User.java
 * Purpose: Model for the User
 */
public class User {
    @SerializedName("id") private String mId;
    @SerializedName("username") private String mUsername;
    @SerializedName("password") private String mPassword;
    @SerializedName("firstname") private String mFirstName;
    @SerializedName("middlename") private String mMiddleName;
    @SerializedName("lastname") private String mLastName;
    @SerializedName("emailAddress") private String mEmailAddress;
    @SerializedName("employeeId") private String mEmployeeId;
    @SerializedName("enabled") private boolean mEnabled;

    //Will most likely have to change the constructors later on
    public User() {
    }

    public User(String id) {
        mId = id;
    }

    public String getId() {
        return mId;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getMiddleName() {
        return mMiddleName;
    }

    public void setMiddleName(String middleName) {
        mMiddleName = middleName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getEmailAddress() {
        return mEmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        mEmailAddress = emailAddress;
    }

    public String getEmployeeId() {
        return mEmployeeId;
    }

    public void setEmployeeId(String employeeId) {
        mEmployeeId = employeeId;
    }

    public boolean isEnabled() {
        return mEnabled;
    }

    public void setEnabled(boolean enabled) {
        mEnabled = enabled;
    }

    public String getFullName() {
        return mFirstName + " " + mLastName;
    }


}
