package it.feio.android.omninotes.db;

import android.provider.BaseColumns;

public final class BottomBoxColorContract {

    public static final String DATABASE_NAME = "bottom_box_color.db";

    public static final int DATABASE_VERSION = 1;

    public static class Colors implements BaseColumns {

        public static final String _ID = BaseColumns._ID;
        public static final String NOTE_ID = "note_id";
        public static final String COLOR = "color";

        public static final String TABLE_NAME = "bottom_color";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + "(" +
                        _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        NOTE_ID + " TEXT," +
                        COLOR + " TEXT" +
                        ")";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
