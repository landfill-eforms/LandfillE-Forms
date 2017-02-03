package com.landfilleforms.android.landfille_forms.database;

/**
 * Created by Work on 10/30/2016.
 */

public class LandFillDbSchema {
    public static final class InstantaneousDataTable {
        public static final String NAME = "instantaneous_data";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String LOCATION = "location";
            public static final String GRID_ID = "grid_id";
            public static final String INSPECTOR_NAME = "inspector_name";
            public static final String START_DATE = "start_date";
            public static final String END_DATE = "end_date";
            public static final String METHANE_READING = "methane_reading";
            public static final String IME_NUMBER = "ime_number";
        }
    }

    public static final class UsersTable {
        public static final String NAME = "users";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String USERNAME = "username";
            public static final String PASSWORD = "password";//In the real thing, the password won't be stored like this.
            public static final String FULLNAME = "fullname";
        }
    }
}
