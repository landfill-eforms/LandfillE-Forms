package com.landfilleforms.android.landfille_forms.database;

/**
 * Created by Work on 10/30/2016.
 */

//TODO: Turn the DB to relational one and use foreign keys

public class LandFillDbSchema {
    public static final class InstantaneousDataTable {
        //Kinda like a contract class
        public static final String NAME = "instantaneous_data";
        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String LOCATION = "location";
            public static final String BARO_PRESSURE = "barometric_pressure";
            public static final String GRID_ID = "grid_id";
            public static final String INSPECTOR_NAME = "inspector_name";//Should get rid of this later
            public static final String INSPECTOR_USERNAME = "inspector_username";
            public static final String START_DATE = "start_date";
            public static final String END_DATE = "end_date";
            public static final String INSTRUMENT_SERIAL = "instrument_serial_number";
            public static final String MAX_METHANE_READING = "max_methane_reading";
            public static final String IME_NUMBER = "ime_number";
        }
    }

    public static final class ImeDataTable {
        public static final String NAME = "ime_data";
        public static class Cols {
            public static final String UUID = "uuid";
            public static final String IME_NUMBER = "ime_number";
            public static final String LOCATION = "location";
            public static final String GRID_ID = "grid_id";
            public static final String DATE = "date";               ///Derive time from date
            public static final String DESCRIPTION = "description";
            public static final String INSPECTOR_NAME = "inspector_name";
            public static final String INSPECTOR_USERNAME = "inspector_username";
            public static final String MAX_METHANE_READING = "max_methane_reading";

        }
    }

    public static final class WarmSpotDataTable {
        public static final String NAME = "warmspot_data";
        public static class Cols {
            public static final String UUID = "uuid";
            public static final String LOCATION = "location";
            public static final String GRID_ID = "grid_id";
            public static final String DATE = "date";               ///Derive time from date
            public static final String DESCRIPTION = "description";
            public static final String ESTIMATED_SIZE = "estimated_size";
            public static final String INSPECTOR_NAME = "inspector_name";
            public static final String INSPECTOR_USERNAME = "inspector_username";
            public static final String MAX_METHANE_READING = "max_methane_reading";
            public static final String INSTRUMENT_SERIAL = "instrument_serial_number";

        }
    }

    public static final class IntegratedDataTable {
        public static final String NAME = "integrated_data";
        public static class Cols {
            public static final String UUID = "uuid";
            public static final String LOCATION = "location";
            public static final String GRID_ID = "grid_id";
            public static final String INSTRUMENT_SERIAL = "instrument_serial";
            public static final String BARO_PRESSURE = "barometric_pressure";
            public static final String INSPECTOR_NAME = "inspector_name";
            public static final String INSPECTOR_USERNAME = "inspector_username";
            public static final String SAMPLE_ID = "sample_id"; //Seems similar to IME #(LocationCode-grid#-YearMonthDay
            public static final String BAG_NUMBER = "bag_number";
            public static final String START_DATE = "start_date";
            public static final String END_DATE = "end_date";
            public static final String VOLUME_READING = "volume";
            public static final String MAX_METHANE_READING = "max_methane_reading";
        }
    }

    public static final class IseDataTable {
        public static final String NAME = "ise_data";
        public static class Cols {
            public static final String UUID = "uuid";
            public static final String ISE_NUMBER = "ise_number";
            public static final String LOCATION = "location";
            public static final String GRID_ID = "grid_id";
            public static final String DATE = "date";
            public static final String DESCRIPTION = "description";
            public static final String INSPECTOR_NAME = "inspector_name";
            public static final String INSPECTOR_USERNAME = "inspector_username";
            public static final String MAX_METHANE_READING = "max_methane_reading";

        }
    }

    public static final class ProbeDataTable {
        public static final String NAME = "probe_data";
        public static class Cols {
            public static final String UUID = "uuid";
            public static final String LOCATION = "location";
            public static final String DATE = "date";
            public static final String INSPECTOR_NAME = "inspector_name";
            public static final String INSPECTOR_USERNAME = "inspector_username";
            public static final String BARO_PRESSURE = "barometric_pressure";
            public static final String PROBE_NUMBER = "probe_number";
            public static final String WATER_PRESSURE = "water_pressure";
            public static final String METHANE_PERCENTAGE = "methane_percentage";
            public static final String REMARKS = "remarks";
        }
    }

    public static final class UsersTable {
        public static final String NAME = "users";
        public static final class Cols {
            public static final String ID = "id";
            public static final String USERNAME = "username";
            public static final String PASSWORD = "password";//In the real thing, the password won't be stored like this.
            public static final String FULLNAME = "fullname";
        }
    }
}
