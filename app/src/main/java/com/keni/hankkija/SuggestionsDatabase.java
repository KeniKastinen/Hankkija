package com.keni.hankkija;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Keni on 2016-12-28.
 */
public class SuggestionsDatabase {


    public static final String DB_SUGGESTION = "SUGGESTION_DB";
    public final static String TABLE_SUGGESTION = "SUGGESTION_TB";
    public final static String FIELD_ID = "_id";
    public final static String FIELD_SUGGESTION = "suggestion";
    public static final String HANKKIJAT = "hankkija_db";
    public final static String HANKKIJA_NIMI = "hankkijaNimi";
    public final static String OIKEA_NIMI = "oikeaNimi";
    public final static String HANKKIJA_ID = "hankkijaId";

    private SQLiteDatabase db;
    private Helper helper;

    public SuggestionsDatabase(Context context) {

        //helper = new Helper(context, DB_SUGGESTION, null, 1);
        helper = new Helper(context, HANKKIJAT, null, 4);
        db = helper.getWritableDatabase();
    }

    public long insertSuggestion(String text, String text2, String nro)
    {
        ContentValues values = new ContentValues();
        //values.put(FIELD_SUGGESTION, text);

        values.put(HANKKIJA_NIMI, text);
        values.put(OIKEA_NIMI, text2);
        values.put(HANKKIJA_ID, nro);
        return db.insert(HANKKIJAT, null, values);

    }

    public Cursor getSuggestions(String text)
    {
        //return db.query(TABLE_SUGGESTION, new String[] {FIELD_ID, FIELD_SUGGESTION}, FIELD_SUGGESTION+" LIKE '"+ text +"%'", null, null, null, null);
        return db.query(HANKKIJAT, new String[] {FIELD_ID, HANKKIJA_NIMI, HANKKIJA_ID}, OIKEA_NIMI+" LIKE '%"+ text +"%' or " +HANKKIJA_NIMI +" LIKE '%"+ text +"%'", null, null, null, null);
        //db.query(HANKKIJAT, new String[] {FIELD_ID, HANKKIJA_NIMI, OIKEA_NIMI}, String selection, String[] selectionArgs, null, null, null, null);

    }


    private class Helper extends SQLiteOpenHelper
    {

        public Helper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                      int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            /**db.execSQL("CREATE TABLE "+TABLE_SUGGESTION+" ("+FIELD_ID+" integer primary key autoincrement, "+FIELD_SUGGESTION+" text);");
            Log.d("SUGGESTION", "DB CREATED");
**/
            //db.execSQL("DROP TABLE IF EXISTS " +HANKKIJAT);
            String query;
            query = "CREATE TABLE IF NOT EXISTS "+HANKKIJAT+"( "+FIELD_ID+" integer primary key autoincrement, "+HANKKIJA_NIMI+" TEXT UNIQUE ON CONFLICT REPLACE, "+OIKEA_NIMI+" TEXT UNIQUE ON CONFLICT REPLACE, "+HANKKIJA_ID+" TEXT UNIQUE ON CONFLICT REPLACE);" ;
            db.execSQL(query);
            Log.d("SUGGESTION", "NimiDB luotu");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

    }
}

