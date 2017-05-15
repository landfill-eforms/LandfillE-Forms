package com.landfilleforms.android.landfille_forms.database;

/**
 * LandFillBaseHelper.java
 * Purpose: Creates all the tables in our DB. This class holds the names of all the tables/columns of our database.
 * There is a inner class for each table and each of these inner classes has the name of the table
 * and columns that belong to that table.
 * The String constants are used when pulling data from the DB through queries or other methods.
 */
public class LandFillDbSchema {
    public static final class InstantaneousDataTable {
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
            public static final String INSTRUMENT_ID = "instrument_id";
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
            public static final String DATE = "date";
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
            public static final String DATE = "date";
            public static final String DESCRIPTION = "description";
            public static final String ESTIMATED_SIZE = "estimated_size";
            public static final String INSPECTOR_NAME = "inspector_name";
            public static final String INSPECTOR_USERNAME = "inspector_username";
            public static final String MAX_METHANE_READING = "max_methane_reading";
            public static final String INSTRUMENT_ID = "instrument_id";

        }
    }

    public static final class IntegratedDataTable {
        public static final String NAME = "integrated_data";
        public static class Cols {
            public static final String UUID = "uuid";
            public static final String LOCATION = "location";
            public static final String GRID_ID = "grid_id";
            public static final String INSTRUMENT_ID = "instrument_id";
            public static final String BARO_PRESSURE = "barometric_pressure";
            public static final String INSPECTOR_NAME = "inspector_name";
            public static final String INSPECTOR_USERNAME = "inspector_username";
            public static final String SAMPLE_ID = "sample_id";
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
            public static final String PASSWORD = "password";
            public static final String FIRST_NAME = "first_name";
            public static final String MIDDLE_NAME = "middle_name";
            public static final String LAST_NAME = "last_name";
            public static final String EMAIL_ADDRESS = "email_address";
            public static final String EMPLOYEE_ID = "employee_id";
            public static final String ENABLED = "enabled";
        }
    }

    public static final class InstrumentsTable {
        public static final String NAME = "instruments";
        public static class Cols {
            public static final String ID = "id";
            public static final String SERIAL_NUMBER = "serial_no";
            public static final String INSTRUMENT_STATUS = "instrument_status";
            public static final String SITE = "site";
            public static final String DESCRIPTION = "description";
            public static final String INSTRUMENT_TYPE_ID = "instrument_type_id";
        }
    }

    public static final class InstrumentTypesTable {
        public static final String NAME = "instrument_types";
        public static class Cols {
            public static final String ID = "id";
            public static final String TYPE = "type";
            public static final String MANUFACTURER = "manufacturer";
            public static final String DESCRIPTION = "description";
            public static final String INSTANTANEOUS = "instantaneous";
            public static final String PROBE = "probe";
            public static final String METHANE_PERCENT = "methane_percent";
            public static final String METHANE_PPM = "methane_ppm";
            public static final String HYDROGEN_SULFIDE_PPM = "hydrogen_sulfide_ppm";
            public static final String OXYGEN_PERCENT = "oxygen_percent";
            public static final String CARBON_DIOXIDE_PERCENT = "carbon_dioxide_percent";
            public static final String NITROGEN_PERCENT = "nitrogen_percent";
            public static final String PRESSURE = "pressure";
        }
    }
}
