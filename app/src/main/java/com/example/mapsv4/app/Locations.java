package com.example.mapsv4.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Skorepa on 18.květen.2014.
 */
public class Locations {

    protected static final String DATABASE_NAME = "locations";
    protected static final int DATABASE_VERSION = 1;

    protected static final String TB_NAME = "location";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LATITUDE = "lat";
    public static final String COLUMN_LONGITUDE = "lon";
    public static final String COLUMN_CZECH = "cz";
    public static final String COLUMN_DEUTCH = "de";
    public static final String COLUMN_ENGLISH = "en";

    private SQLiteOpenHelper openHelper;

    static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TB_NAME + " ("
                    + COLUMN_ID        + " INTEGER PRIMARY KEY,"
                    + COLUMN_LATITUDE  + " REAL NOT NULL,"
                    + COLUMN_LONGITUDE + " REAL NOT NULL,"
                    + COLUMN_CZECH     + " TEXT NOT NULL,"
                    + COLUMN_DEUTCH    + " TEXT NOT NULL,"
                    + COLUMN_ENGLISH   + " TEXT"
                    + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //onCreate(db);
        }
    }

    public Locations(Context context) {
        openHelper = new DatabaseHelper(context);
    }

    public static final String[] columns = {
            COLUMN_ID,
            COLUMN_LATITUDE,
            COLUMN_LONGITUDE,
            COLUMN_CZECH,
            COLUMN_DEUTCH,
            COLUMN_ENGLISH
    };

    protected static final String ORDER_BY = COLUMN_ID + " DESC";


    public Cursor getLocations() {
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db.query(TB_NAME, columns, null, null, null, null, ORDER_BY);
    }

    public Cursor getLocation(long id) {
        SQLiteDatabase db = openHelper.getReadableDatabase();
        String[] selectionArgs = { String.valueOf(id) };
        return db.query(TB_NAME, columns, COLUMN_ID + "= ?", selectionArgs, null, null, ORDER_BY);
    }

    public boolean deleteLocation(long id) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        String[] selectionArgs = { String.valueOf(id) };

        int deletedCount = db.delete(TB_NAME, COLUMN_ID + "= ?", selectionArgs);
        db.close();
        return deletedCount > 0;
    }

    public long insertLocation(double lat, double lon, String cz, String de, String en) {
        SQLiteDatabase db = openHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_LATITUDE, lat);
        values.put(COLUMN_LONGITUDE, lon);
        values.put(COLUMN_CZECH, cz);
        values.put(COLUMN_DEUTCH, de);
        values.put(COLUMN_ENGLISH, en);

        long id = db.insert(TB_NAME, null, values);
        db.close();
        return id;
    }

    public void close() {
        openHelper.close();
    }
}
