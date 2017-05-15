package com.landfilleforms.android.landfille_forms.model;

import java.util.List;

/**
 * ImportedData.java
 * Purpose: An object that holds the User/Instrument data that is retrieved from the web app.
 */
public class ImportedData {
    private List<Instrument> instruments;
    private List<User> users;

    public ImportedData(List<Instrument> instruments, List<User> users) {
        this.instruments = instruments;
        this.users = users;
    }

    public List<Instrument> getInstruments() {
        return instruments;
    }

    public List<User> getUsers() {
        return users;
    }
}
