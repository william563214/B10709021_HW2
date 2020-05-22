package com.example.b10709021_hw2;

import android.provider.BaseColumns;

public class WaitlistContract {

    public static final class WaitlistEntry implements BaseColumns {
        public static final String TABLE_NAME = "waitlist";
        public static final String COLUMN_GUEST_NAME = "guestName";
        public static final String COLUMN_PARTY_SIZE = "partySize";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }

}
