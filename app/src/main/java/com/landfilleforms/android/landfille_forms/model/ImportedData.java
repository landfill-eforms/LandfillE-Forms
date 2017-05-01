package com.landfilleforms.android.landfille_forms.model;

import java.util.List;

/**
 * Created by Work on 4/28/2017.
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

    public void setInstruments(List<Instrument> instruments) {
        this.instruments = instruments;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
