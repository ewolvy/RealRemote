package com.mooo.ewolvy.realremote.database;

import android.provider.BaseColumns;

public final class AirConditionersContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private AirConditionersContract(){}

    public static final int AA_KAYSUN = 0;
    public static final int AA_PROKLIMA = 1;

    // Inner class to define the table contents
    public static class AvailableAA implements BaseColumns{
        public static final String TABLE_NAME = "availableaa";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_BRAND = "brand";
        public static final String COLUMN_NAME_SERVER = "server";
        public static final String COLUMN_NAME_PORT = "port";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_CERTIFICATE = "certificate";
        public static final String COLUMN_NAME_ALIAS = "alias";
    }
}
