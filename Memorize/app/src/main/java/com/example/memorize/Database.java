package com.example.memorize;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Database extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "memorizer_database";

    //GROUPS TABLE PARAMETERS
    private static final String TABLE_GROUPS = "groups";
    private static String GROUP_ID = "group_id";
    private static String GROUP_NAME = "group_name";

    //WORDS TABLE PARAMETERS
    private static final String TABLE_WORDS = "words";
    private static String WORD_ID = "word_id";
    private static String WORD_GROUP_ID = "word_group_id";
    private static String WORD_NAME = "word_name";
    private static String WORD_INFO = "word_info";

    //COLORS TABLE PARAMETERS
    private static final String TABLE_COLORS = "colors";
    private static String COLOR_ID = "color_id";
    private static String COLOR_GROUP_ID = "color_group_id";
    private static String COLOR_HEX = "color_hex";


    private static final String CREATE_TABLE_GROUPS = "CREATE TABLE " + TABLE_GROUPS
            + "(" + GROUP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + GROUP_NAME + " TEXT" + ")";

    private static final String CREATE_TABLE_WORDS = "CREATE TABLE " + TABLE_WORDS
            + "(" + WORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + WORD_GROUP_ID + " INTEGER,"
            + WORD_NAME + " TEXT,"
            + WORD_INFO + " TEXT" + ")";

    private static final String CREATE_TABLE_COLORS = "CREATE TABLE " + TABLE_COLORS
            + "(" + COLOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLOR_GROUP_ID + " INTEGER,"
            + COLOR_HEX + " INTEGER" + ")";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_WORDS);
        db.execSQL(CREATE_TABLE_GROUPS);
        db.execSQL(CREATE_TABLE_COLORS);
    }

    //GROUP TABLE METHODS
    public void group_add(String group_name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GROUP_NAME, group_name);
        db.insert(TABLE_GROUPS, null, values);
        db.close();
    }

    public  ArrayList<HashMap<String, String>> group_list()
    {

        //Bu methodda ise tablodaki tüm değerleri alıyoruz
        //ArrayList adı üstünde Array lerin listelendiği bir Array.Burda hashmapleri listeleyeceğiz
        //Herbir satırı değer ve value ile hashmap a atıyoruz. Her bir satır 1 tane hashmap arrayı demek.
        //olusturdugumuz tüm hashmapleri ArrayList e atıp geri dönüyoruz(return).

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_GROUPS;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> grouplist = new ArrayList<HashMap<String, String>>();
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }

                grouplist.add(map);
            } while (cursor.moveToNext());
        }
        db.close();
        // return grouplist
        return grouplist;
    }

    public  ArrayList<HashMap<String, String>> group_find(int id)
    {
        //Bu methodda ise tablodaki tüm değerleri alıyoruz
        //ArrayList adı üstünde Array lerin listelendiği bir Array.Burda hashmapleri listeleyeceğiz
        //Herbir satırı değer ve value ile hashmap a atıyoruz. Her bir satır 1 tane hashmap arrayı demek.
        //olusturdugumuz tüm hashmapleri ArrayList e atıp geri dönüyoruz(return).

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM "+ TABLE_GROUPS +" WHERE group_id="+id;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> grouplist = new ArrayList<HashMap<String, String>>();
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }

                grouplist.add(map);
            } while (cursor.moveToNext());
        }
        db.close();
        // return grouplist
        return grouplist;
    }

    public void group_update(String group_name, int group_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GROUP_NAME, group_name);

        db.update(TABLE_GROUPS, values, GROUP_ID+" =?", new String[]{String.valueOf(group_id)});
    }

    public void group_delete(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GROUPS, GROUP_ID + " =?",
                new String[]{ String.valueOf(id)});

        db.delete(TABLE_WORDS, WORD_GROUP_ID + " =?",
                new String[]{ String.valueOf(id)});

        db.delete(TABLE_COLORS, COLOR_GROUP_ID + " =?",
                new String[]{ String.valueOf(id)});
        db.close();
    }


    //WORD TABLE METHODS
    public void word_add(String word_name, String word_info, int word_group_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(WORD_GROUP_ID, word_group_id);
        values.put(WORD_NAME, word_name);
        values.put(WORD_INFO, word_info);
        db.insert(TABLE_WORDS, null, values);
        db.close();
    }

    public  ArrayList<HashMap<String, String>> word_list(int id)
    {
        //Bu methodda ise tablodaki tüm değerleri alıyoruz
        //ArrayList adı üstünde Array lerin listelendiği bir Array.Burda hashmapleri listeleyeceğiz
        //Herbir satırı değer ve value ile hashmap a atıyoruz. Her bir satır 1 tane hashmap arrayı demek.
        //olusturdugumuz tüm hashmapleri ArrayList e atıp geri dönüyoruz(return).

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM "+ TABLE_WORDS +" WHERE word_group_id="+id;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> wordlist = new ArrayList<HashMap<String, String>>();
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }

                wordlist.add(map);
            } while (cursor.moveToNext());
        }
        db.close();
        // return wordlist
        return wordlist;
    }

    public  ArrayList<HashMap<String, String>> word_find(int id)
    {
        //Bu methodda ise tablodaki tüm değerleri alıyoruz
        //ArrayList adı üstünde Array lerin listelendiği bir Array.Burda hashmapleri listeleyeceğiz
        //Herbir satırı değer ve value ile hashmap a atıyoruz. Her bir satır 1 tane hashmap arrayı demek.
        //olusturdugumuz tüm hashmapleri ArrayList e atıp geri dönüyoruz(return).

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM "+ TABLE_WORDS +" WHERE word_id="+id;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> wordlist = new ArrayList<HashMap<String, String>>();
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }

                wordlist.add(map);
            } while (cursor.moveToNext());
        }
        db.close();
        // return wordlist
        return wordlist;
    }

    public void word_update(String word_name, String word_info, int word_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(WORD_NAME, word_name);
        values.put(WORD_INFO, word_info);

        db.update(TABLE_WORDS, values, WORD_ID+" =?", new String[]{String.valueOf(word_id)});
    }

    public void word_delete(int id)
    { //id si belli olan row u silmek için

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WORDS, WORD_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }


    //COLORS TABLE METHODS
    public void color_add(int color_hex, int color_group_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLOR_GROUP_ID, color_group_id);
        values.put(COLOR_HEX, color_hex);
        db.insert(TABLE_COLORS, null, values);
        db.close();
    }

    public  ArrayList<HashMap<String, String>> color_list()
    {
        //Bu methodda ise tablodaki tüm değerleri alıyoruz
        //ArrayList adı üstünde Array lerin listelendiği bir Array.Burda hashmapleri listeleyeceğiz
        //Herbir satırı değer ve value ile hashmap a atıyoruz. Her bir satır 1 tane hashmap arrayı demek.
        //olusturdugumuz tüm hashmapleri ArrayList e atıp geri dönüyoruz(return).

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_COLORS;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> colorlist = new ArrayList<HashMap<String, String>>();
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }

                colorlist.add(map);
            } while (cursor.moveToNext());
        }
        db.close();
        // return colorlist
        return colorlist;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
