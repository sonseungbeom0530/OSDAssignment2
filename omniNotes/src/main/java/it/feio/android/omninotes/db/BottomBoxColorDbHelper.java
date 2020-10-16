package it.feio.android.omninotes.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import it.feio.android.omninotes.OmniNotes;
import it.feio.android.omninotes.helpers.LogDelegate;
import it.feio.android.omninotes.models.BottomBoxColorData;

/**
 * sishin 2020.10.08
 */
public class BottomBoxColorDbHelper extends SQLiteOpenHelper {

    private final Context mContext;
    private static BottomBoxColorDbHelper instance = null;
    private SQLiteDatabase db;

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BottomBoxColorContract.Colors.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(BottomBoxColorContract.Colors.SQL_DELETE_ENTRIES);

        onCreate(db);
    }


    public static synchronized BottomBoxColorDbHelper getInstance() {
        return getInstance(OmniNotes.getAppContext());
    }


    public static synchronized BottomBoxColorDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new BottomBoxColorDbHelper(context);
        }
        return instance;
    }


    public static synchronized BottomBoxColorDbHelper getInstance(boolean forcedNewInstance) {
        if (instance == null || forcedNewInstance) {
            Context context = (instance == null || instance.mContext == null) ? OmniNotes.getAppContext() : instance.mContext;
            instance = new BottomBoxColorDbHelper(context);
        }
        return instance;
    }


    private BottomBoxColorDbHelper(Context mContext) {
        super(mContext, BottomBoxColorContract.DATABASE_NAME, null, BottomBoxColorContract.DATABASE_VERSION);
        this.mContext = mContext;
    }

    public SQLiteDatabase getDatabase(boolean forceWritable) {
        try {
            return forceWritable ? getWritableDatabase() : getReadableDatabase();
        } catch (IllegalStateException e) {
            return this.db;
        }
    }

    /**
     * BottomColor to be updated or inserted
     * New BottomColor insertion
     */
    public void updateBottomColor(BottomBoxColorData bottomColorData) {
        String noteId = bottomColorData.getNoteID();
        String color = bottomColorData.getColor();
        long check = getColorID(noteId);
        if(check>=0) {
            ContentValues values = new ContentValues();
            values.put(BottomBoxColorContract.Colors._ID, bottomColorData.getNoteID());
            values.put(BottomBoxColorContract.Colors.NOTE_ID, bottomColorData.getNoteID());
            values.put(BottomBoxColorContract.Colors.COLOR, bottomColorData.getColor());
            getDatabase(true).insertWithOnConflict(BottomBoxColorContract.Colors.TABLE_NAME, BottomBoxColorContract.Colors._ID, values, SQLiteDatabase
                    .CONFLICT_REPLACE);
        }else{
            insert(bottomColorData);
        }
    }

    /**
     *Delete row
     */
    public long deleteBottomColor (long noteID) {
        long deleted;

        SQLiteDatabase db = getDatabase(true);
        // Delete category
        deleted = db.delete(BottomBoxColorContract.Colors.TABLE_NAME, BottomBoxColorContract.Colors._ID + " = ?",
                new String[]{String.valueOf(noteID)});
        return deleted;
    }

    /**
     * Select DB
     * Check if the ID exists in the DB.
     * @param val
     * @return
     */
   private long getColorID(String val){
        long result = -1;
        SQLiteDatabase db = getDatabase(false);
        String sql = String.format("select %s from %s where %s = '%s'",
                BottomBoxColorContract.Colors._ID,
                BottomBoxColorContract.Colors.TABLE_NAME,
                BottomBoxColorContract.Colors.NOTE_ID,val);

        Cursor cursor = db.rawQuery(sql,null);
        if(null != cursor && cursor.moveToNext()){
            result = cursor.getInt(cursor.getColumnIndex(BottomBoxColorContract.Colors._ID));
            cursor.close();
        }
        db.close();

        return result;
    }

    /**
     * Insert DB
     * Save data in DB.
     * @param bottomColorData
     */
    private void insert(BottomBoxColorData bottomColorData){
        SQLiteDatabase db = getDatabase(true);
        ContentValues row = new ContentValues();
        row.put( BottomBoxColorContract.Colors._ID, bottomColorData.getNoteID());
        row.put( BottomBoxColorContract.Colors.NOTE_ID, bottomColorData.getNoteID());
        row.put( BottomBoxColorContract.Colors.COLOR, bottomColorData.getColor());
        db.insert( BottomBoxColorContract.Colors.TABLE_NAME,null,row);
        db.close();
    }

    /**
     * Select DB
     * Searches the saved value of DB.
     * @param noteID
     * @return BottomBoxColorData
     */
    public BottomBoxColorData getColor(long noteID){
        String query = "SELECT "+ BottomBoxColorContract.Colors.NOTE_ID + ","
                + BottomBoxColorContract.Colors.COLOR+" FROM " + BottomBoxColorContract.Colors.TABLE_NAME
                + " WHERE " + BottomBoxColorContract.Colors.NOTE_ID + " = " + noteID;;

        BottomBoxColorData bottomColorData = null;

        try (Cursor cursor = getDatabase(false).rawQuery(query, null)) {

            if (cursor.moveToFirst()) {
                do {
                    int i = 0;
                    bottomColorData = new BottomBoxColorData();
                    bottomColorData.setNoteID(cursor.getString(i++));
                    bottomColorData.setColor(cursor.getString(i++));
                } while (cursor.moveToNext());
            }

        }
        return bottomColorData;
    }

    /**
     * Select DB
     * Search the current DB total value
     * For Only Dev(Development)
     * Use only for development purposes (to inquire what values have been stored in the DB).
     */
    public void getColorSelect(){
        ArrayList<BottomBoxColorData> bottomColorDataArrayList = new ArrayList<>();
        String query = "SELECT "+ BottomBoxColorContract.Colors.NOTE_ID + ","
                + BottomBoxColorContract.Colors.COLOR+" FROM " + BottomBoxColorContract.Colors.TABLE_NAME;

        try (Cursor cursor = getDatabase(false).rawQuery(query, null)) {

            if (cursor.moveToFirst()) {
                do {
                    int i = 0;
                    BottomBoxColorData bottomColorData = new BottomBoxColorData();
                    bottomColorData.setNoteID(cursor.getString(i++));
                    bottomColorData.setColor(cursor.getString(i++));
                    bottomColorDataArrayList.add(bottomColorData);
                } while (cursor.moveToNext());
            }

        }
        for(int i =0;i<bottomColorDataArrayList.size();i++){
            Log.i("sishin123","sishin123 colorDB");
            Log.i("sishin123","sishin123 noteID ="+bottomColorDataArrayList.get(i).getNoteID());
            Log.i("sishin123","sishin123 color ="+bottomColorDataArrayList.get(i).getColor());

        }
    }


}