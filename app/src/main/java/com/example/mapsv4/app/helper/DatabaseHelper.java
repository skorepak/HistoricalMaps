package com.example.mapsv4.app.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.mapsv4.app.model.Favorities;
import com.example.mapsv4.app.model.Places;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "mapsManager";

    // Table Names
    private static final String TABLE_FAV = "favorities";
    private static final String TABLE_PLACES = "places";

    // Common column names
    private static final String KEY_ID = "id";

    // FAVORITIES Table - column names
    private static final String KEY_PLACES_ID = "place_id";
    private static final String KEY_FAV_LAT = "latitude";
    private static final String KEY_FAV_LNG = "longitude";
    private static final String KEY_FAV_TITLE = "title";
    // Table Create Statements
    // Favorities table create statement
    private static final String CREATE_TABLE_FAV = "CREATE TABLE "
            + TABLE_FAV + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PLACES_ID + " INTEGER,"
            + KEY_FAV_LAT + " REAL," + KEY_FAV_LNG + " REAL," + KEY_FAV_TITLE + " TEXT" + ")";
    // PLACES Table - column names
    private static final String KEY_PLACES_LAT = "latitude";
    private static final String KEY_PLACES_LNG = "longitude";
    private static final String KEY_PLACES_CZ = "placecz";
    private static final String KEY_PLACES_DE = "placede";
    private static final String KEY_PLACES_EN = "placeen";
    // Tag table create statement
    private static final String CREATE_TABLE_PLACES = "CREATE TABLE "
            + TABLE_PLACES + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_PLACES_LAT + " REAL," + KEY_PLACES_LNG + " REAL,"
            + KEY_PLACES_CZ + " TEXT," + KEY_PLACES_DE + " TEXT," + KEY_PLACES_EN + " TEXT" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_FAV);
        db.execSQL(CREATE_TABLE_PLACES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAV);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACES);

        // create new tables
        onCreate(db);
    }

    // ------------------------ "favorities" table methods ----------------//

    /*
     * Creating a favorities
     */
    public long createFav(Favorities favs) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PLACES_ID, favs.getPlaceId());
        values.put(KEY_FAV_LAT, favs.getLatitude());
        values.put(KEY_FAV_LNG, favs.getLongitude());
        values.put(KEY_FAV_TITLE, favs.getTitle());

        // insert row
        long favs_id = db.insert(TABLE_FAV, null, values);

        return favs_id;
    }

    /*
     * get single favorit
     */
    public Favorities getFav(long favs_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_FAV + " WHERE "
                + KEY_ID + " = " + favs_id;

        Log.e(LOG, "Query from Places " + selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Favorities fav = new Favorities();
        fav.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        fav.setPlaceId(c.getInt(c.getColumnIndex(KEY_PLACES_ID)));
        fav.setLatitude(c.getFloat(c.getColumnIndex(KEY_FAV_LAT)));
        fav.setLongitude(c.getFloat(c.getColumnIndex(KEY_FAV_LNG)));
        fav.setTitle(c.getString(c.getColumnIndex(KEY_FAV_TITLE)));

        return fav;
    }

    /**
     * getting all favorities
     */
    public List<Favorities> getAllFavs() {
        List<Favorities> favs = new ArrayList<Favorities>();
        String selectQuery = "SELECT  * FROM " + TABLE_FAV;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Favorities fav = new Favorities();
                fav.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                fav.setPlaceId(c.getInt(c.getColumnIndex(KEY_PLACES_ID)));
                fav.setLatitude(c.getFloat(c.getColumnIndex(KEY_FAV_LAT)));
                fav.setLongitude(c.getFloat(c.getColumnIndex(KEY_FAV_LNG)));
                fav.setTitle(c.getString(c.getColumnIndex(KEY_FAV_TITLE)));

                // adding to favs list
                favs.add(fav);
            } while (c.moveToNext());
        }

        return favs;
    }

    /*
     * getting favorities count
     */
    public int getFavCount() {
        String countQuery = "SELECT  * FROM " + TABLE_FAV;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /*
     * Updating a favorities
     */
    public int updateToDo(Favorities fav) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PLACES_ID, fav.getPlaceId());
        values.put(KEY_FAV_LAT, fav.getLatitude());
        values.put(KEY_FAV_LNG, fav.getLongitude());
        values.put(KEY_FAV_TITLE, fav.getTitle());

        // updating row
        return db.update(TABLE_FAV, values, KEY_ID + " = ?",
                new String[]{String.valueOf(fav.getId())});
    }

    /*
     * Deleting a favorities
     */
    public void deleteToDo(long fav_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAV, KEY_ID + " = ?",
                new String[]{String.valueOf(fav_id)});
    }

    // ------------------------ "places" table methods ----------------//

    /*
     * Creating tag
     */
    public long createPlace(Places places) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PLACES_LAT, places.getLatitude());
        values.put(KEY_PLACES_LNG, places.getLongitude());
        values.put(KEY_PLACES_CZ, places.getPlaceCZ());
        values.put(KEY_PLACES_DE, places.getPlaceDE());
        values.put(KEY_PLACES_EN, places.getPlaceEN());

        // insert row
        long places_id = db.insert(TABLE_PLACES, null, values);

        return places_id;
    }

    /**
     * getting all places
     */
    public List<Places> getAllPlaces() {
        List<Places> places = new ArrayList<Places>();
        String selectQuery = "SELECT  * FROM " + TABLE_PLACES;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Places plc = new Places();
                plc.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                plc.setLatitude(c.getFloat(c.getColumnIndex(KEY_PLACES_LAT)));
                plc.setLongitude(c.getFloat(c.getColumnIndex(KEY_PLACES_LNG)));
                plc.setPlaceCZ(c.getString(c.getColumnIndex(KEY_PLACES_CZ)));
                plc.setPlaceDE(c.getString(c.getColumnIndex(KEY_PLACES_DE)));
                plc.setPlaceEN(c.getString(c.getColumnIndex(KEY_PLACES_EN)));

                // adding to tags list
                places.add(plc);
            } while (c.moveToNext());
        }
        return places;
    }

    /*
     * Updating a place
     */
    public int updatePlace(Places places) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PLACES_LAT, places.getLatitude());
        values.put(KEY_PLACES_LNG, places.getLongitude());
        values.put(KEY_PLACES_CZ, places.getPlaceCZ());
        values.put(KEY_PLACES_DE, places.getPlaceDE());
        values.put(KEY_PLACES_EN, places.getPlaceEN());

        // updating row
        return db.update(TABLE_PLACES, values, KEY_ID + " = ?",
                new String[]{String.valueOf(places.getId())});
    }

    /*
     * Deleting a place
     */
    public void deletePlace(Places place) {
        SQLiteDatabase db = this.getWritableDatabase();

        // now delete the tag
        db.delete(TABLE_PLACES, KEY_ID + " = ?",
                new String[]{String.valueOf(place.getId())});
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
