package com.landfilleforms.android.landfille_forms;

import java.util.UUID;

/**
 * Created by Work on 11/1/2016.
 * Probably going to have to get rid of the Username/password setters
 */

public class User {
    private UUID mId;
    private String mUsername;
    private String mPassword;
    private String mFullName;

    //Will most likely have to change the constructors later on
    public User() {
        this(UUID.randomUUID());
    }

    public User(UUID id) {
        mId = id;
    }

    public UUID getId() {
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

    public String getFullName() { return mFullName; }

    public void setFullName(String fullName) { mFullName = fullName; }
}
