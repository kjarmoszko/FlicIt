package com.example.flicit;

import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Button.db";

    public static class Table implements BaseColumns {
        public static final String TABLE_NAME = "button";
        public static final String COLUMN_NAME_MAC = "mac";
        public static final String COLUMN_NAME_SINGLE_CLICK = "single_click";
        public static final String COLUMN_NAME_DOUBLE_CLICK = "double_click";
        public static final String COLUMN_NAME_HOLD = "hold";
    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Table.TABLE_NAME + " (" +
                    Table.COLUMN_NAME_MAC + " TEXT PRIMARY KEY," +
                    Table.COLUMN_NAME_SINGLE_CLICK + " INTEGER," +
                    Table.COLUMN_NAME_DOUBLE_CLICK + " INTEGER," +
                    Table.COLUMN_NAME_HOLD + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Table.TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public long addButton(String mac) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Table.COLUMN_NAME_MAC, mac);
        contentValues.put(Table.COLUMN_NAME_SINGLE_CLICK, 0);
        contentValues.put(Table.COLUMN_NAME_DOUBLE_CLICK, 0);
        contentValues.put(Table.COLUMN_NAME_HOLD, 0);
        long newRowId = db.insert(Table.TABLE_NAME, null, contentValues);
        return newRowId;
    }

    public int changeFunctionality(String mac, int click, int value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        switch (click) {
            case 0:
                contentValues.put(Table.COLUMN_NAME_HOLD, value);
                break;
            case 1:
                contentValues.put(Table.COLUMN_NAME_SINGLE_CLICK, value);
                break;
            case 2:
                contentValues.put(Table.COLUMN_NAME_DOUBLE_CLICK, value);
                break;
        }
        String selection = Table.COLUMN_NAME_MAC + " LIKE ?";
        String[] selectionArgs = {mac};
        int count = db.update(Table.TABLE_NAME, contentValues, selection, selectionArgs);
        return count;
    }

    public int getFunctionality(String mac, int click) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                Table.COLUMN_NAME_MAC,
                Table.COLUMN_NAME_SINGLE_CLICK,
                Table.COLUMN_NAME_DOUBLE_CLICK,
                Table.COLUMN_NAME_HOLD
        };
        String selection = Table.COLUMN_NAME_MAC + " = ?";
        String[] selectionArgs = { mac };

        Cursor cursor = db.query(
                Table.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        cursor.moveToNext();
        int func = 0;
        switch (click) {
            case 0:
                func = cursor.getInt(cursor.getColumnIndexOrThrow(Table.COLUMN_NAME_HOLD));
                break;
            case 1:
                func = cursor.getInt(cursor.getColumnIndexOrThrow(Table.COLUMN_NAME_SINGLE_CLICK));
                break;
            case 2:
                func = cursor.getInt(cursor.getColumnIndexOrThrow(Table.COLUMN_NAME_DOUBLE_CLICK));
                break;
        }
        return func;
    }

    public Cursor getAllData() { //db test
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+ Table.TABLE_NAME, null);
        return cursor;
    }
}
