package com.example.flicit.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper instance;

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "Button.db";

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    public static class TableFlicButton implements BaseColumns {
        public static final String NAME = "flic_button";
        public static final String COLUMN_MAC = "mac";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SINGLE_CLICK = "single_click";
        public static final String COLUMN_DOUBLE_CLICK = "double_click";
        public static final String COLUMN_HOLD = "hold";
    }

    public static class TableFunction implements BaseColumns {
        public static final String NAME = "function";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_NUMBER = "number";
        public static final String COLUMN_MESSAGE = "message";
    }

    private static final String CREATE_FLIC_BUTTON_TABLE =
            "CREATE TABLE " + TableFlicButton.NAME + " (" +
                    TableFlicButton.COLUMN_MAC + " TEXT PRIMARY KEY," +
                    TableFlicButton.COLUMN_NAME + " INTEGER," +
                    TableFlicButton.COLUMN_SINGLE_CLICK + " INTEGER," +
                    TableFlicButton.COLUMN_DOUBLE_CLICK + " INTEGER," +
                    TableFlicButton.COLUMN_HOLD + " INTEGER);";

    private static final String CREATE_FUNCTION_TABLE =
            "CREATE TABLE " + TableFunction.NAME + " (" +
                    TableFunction.COLUMN_ID + " INTEGER PRIMARY KEY," +
                    TableFunction.COLUMN_TYPE + " TEXT NOT NULL," +
                    TableFunction.COLUMN_NUMBER + " INTEGER," +
                    TableFunction.COLUMN_MESSAGE + " INTEGER);";

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_FUNCTION_TABLE);
        db.execSQL(CREATE_FLIC_BUTTON_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TableFlicButton.NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TableFunction.NAME);
        onCreate(db);
    }

    private long createFunction() {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TableFunction.COLUMN_TYPE, "NONE");

        long functionId = db.insert(TableFunction.NAME, null, contentValues);
        return functionId;
    }

    public long createFlicButton(String mac) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TableFlicButton.COLUMN_MAC, mac);
        contentValues.put(TableFlicButton.COLUMN_SINGLE_CLICK, createFunction());
        contentValues.put(TableFlicButton.COLUMN_DOUBLE_CLICK, createFunction());
        contentValues.put(TableFlicButton.COLUMN_HOLD, createFunction());

        long flicButtonId = db.insert(TableFlicButton.NAME, null, contentValues);
        return flicButtonId;
    }

    public FlicButton getFlicButton(String mac) {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        FlicButton flicButton = new FlicButton();
        String query = "SELECT * FROM " + TableFlicButton.NAME + " WHERE " + TableFlicButton.COLUMN_MAC + " LIKE ?";
        Cursor cursor = db.rawQuery(query, new String[]{mac});

        if(cursor != null) {
            cursor.moveToNext();
            flicButton.setMac(cursor.getString(cursor.getColumnIndex(TableFlicButton.COLUMN_MAC)));
            flicButton.setName(cursor.getString(cursor.getColumnIndex(TableFlicButton.COLUMN_NAME)));
            flicButton.setSingleClick(cursor.getLong(cursor.getColumnIndex(TableFlicButton.COLUMN_SINGLE_CLICK)));
            flicButton.setDoubleClick(cursor.getLong(cursor.getColumnIndex(TableFlicButton.COLUMN_DOUBLE_CLICK)));
            flicButton.setHold(cursor.getLong(cursor.getColumnIndex(TableFlicButton.COLUMN_HOLD)));
        }
        return flicButton;
    }



    public List<Function> getAllFlicButtonFunctions(String mac) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Function> functions = new ArrayList<Function>();

        functions.add(getFunction(mac, 0));
        functions.add(getFunction(mac, 1));
        functions.add(getFunction(mac, 2));

        return functions;
    }

    private long getFlicForeignKey(String mac, int click) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TableFlicButton.NAME + " WHERE " + TableFlicButton.COLUMN_MAC + " LIKE ?";// + mac;
        Cursor cursor = db.rawQuery(query,new String[] {mac});

        long functionId = -1;
        if(cursor != null) {
            cursor.moveToNext();

            switch (click) {
                case 0:
                    functionId = cursor.getInt(cursor.getColumnIndex(TableFlicButton.COLUMN_HOLD));
                    break;
                case 1:
                    functionId = cursor.getInt(cursor.getColumnIndex(TableFlicButton.COLUMN_SINGLE_CLICK));
                    break;
                case 2:
                    functionId = cursor.getInt(cursor.getColumnIndex(TableFlicButton.COLUMN_DOUBLE_CLICK));
                    break;
            }
        }
        return functionId;
    }

    public long updateFunction(String mac, int click, Function function){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TableFunction.COLUMN_TYPE, function.getType());
        contentValues.put(TableFunction.COLUMN_NUMBER, function.getNumber());
        contentValues.put(TableFunction.COLUMN_MESSAGE, function.getMessage());

        long functionId = getFlicForeignKey(mac, click);
        if(functionId >= 0) {
            String selection = TableFunction.COLUMN_ID + " LIKE ?";
            String[] selectionArgs = {String.valueOf(functionId)};
            long result = db.update(TableFunction.NAME, contentValues, selection, selectionArgs);
            return result;
        }
        else {
            return functionId;
        }
    }

    public long updateFunction(Function function) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TableFunction.COLUMN_TYPE, function.getType());
        contentValues.put(TableFunction.COLUMN_NUMBER, function.getNumber());
        contentValues.put(TableFunction.COLUMN_MESSAGE, function.getMessage());
        String selection = TableFunction.COLUMN_ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(function.getId())};
        long result = db.update(TableFunction.NAME, contentValues, selection, selectionArgs);
        return result;
    }

    public long updateFlicButton(FlicButton flicButton) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TableFlicButton.COLUMN_NAME, flicButton.getName());
        String selection = TableFlicButton.COLUMN_MAC + " LIKE ?";
        String[] selectionArgs = {flicButton.getMac()};
        long result = db.update(TableFlicButton.NAME, contentValues, selection, selectionArgs);
        return result;
    }



//    public long updateFunction(String mac, int click, String type) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(TableFunction.COLUMN_TYPE, type);
//
//        long functionId = getFlicForeignKey(mac, click);
//        if(functionId >= 0) {
//            String selection = TableFunction.COLUMN_ID + " LIKE ?";
//            String[] selectionArgs = {String.valueOf(functionId)};
//            long result = db.update(TableFunction.NAME, contentValues, selection, selectionArgs);
//            return result;
//        }
//        else {
//            return functionId;
//        }
//    }
//    public long updateFunction(String mac, int click, String type, String number) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(TableFunction.COLUMN_TYPE, type);
//        contentValues.put(TableFunction.COLUMN_NUMBER, number);
//
//        long functionId = getFlicForeignKey(mac, click);
//        if(functionId >= 0) {
//            String selection = TableFunction.COLUMN_ID + " LIKE ?";
//            String[] selectionArgs = {String.valueOf(functionId)};
//            long result = db.update(TableFunction.NAME, contentValues, selection, selectionArgs);
//            return result;
//        }
//        else {
//            return functionId;
//        }
//    }
//    public long updateFunction(String mac, int click, String type, String number, String message) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(TableFunction.COLUMN_TYPE, type);
//        contentValues.put(TableFunction.COLUMN_NUMBER, number);
//        contentValues.put(TableFunction.COLUMN_MESSAGE, message);
//
//        long functionId = getFlicForeignKey(mac, click);
//        if(functionId >= 0) {
//            String selection = TableFunction.COLUMN_ID + " LIKE ?";
//            String[] selectionArgs = {String.valueOf(functionId)};
//            long result = db.update(TableFunction.NAME, contentValues, selection, selectionArgs);
//            return result;
//        }
//        else {
//            return functionId;
//        }
//    }

    public Function getFunction(String mac, int click){
        SQLiteDatabase db = this.getReadableDatabase();
        Function function = new Function();
        long functionId = getFlicForeignKey(mac, click);
        if(functionId >= 0) {
            String query = "SELECT * FROM " + TableFunction.NAME + " WHERE " + TableFunction.COLUMN_ID + " LIKE ?";
            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(functionId)});
            if(cursor != null) {
                cursor.moveToNext();
                function.setId(cursor.getInt(cursor.getColumnIndex(TableFunction.COLUMN_ID)));
                function.setType(cursor.getString(cursor.getColumnIndex(TableFunction.COLUMN_TYPE)));
                function.setNumber(cursor.getString(cursor.getColumnIndex(TableFunction.COLUMN_NUMBER)));
                function.setMessage(cursor.getString(cursor.getColumnIndex(TableFunction.COLUMN_MESSAGE)));
            }
        }
        return function;
    }

    public Function getFunction(long functionId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Function function = new Function();
        String query = "SELECT * FROM "+ TableFunction.NAME + " WHERE " + TableFunction.COLUMN_ID + " LIKE ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(functionId)});
        if(cursor != null) {
            cursor.moveToNext();
            function.setId(cursor.getInt(cursor.getColumnIndex(TableFunction.COLUMN_ID)));
            function.setType(cursor.getString(cursor.getColumnIndex(TableFunction.COLUMN_TYPE)));
            function.setNumber(cursor.getString(cursor.getColumnIndex(TableFunction.COLUMN_NUMBER)));
            function.setMessage(cursor.getString(cursor.getColumnIndex(TableFunction.COLUMN_MESSAGE)));
        }
        return function;
    }

    public void deleteFlicButton(String mac) {
        SQLiteDatabase db = this.getWritableDatabase();

        for(Function function : getAllFlicButtonFunctions(mac)){
            db.delete(TableFunction.NAME, TableFunction.COLUMN_ID + " LIKE ?", new String[] {String.valueOf(function.getId())});
        }
        db.delete(TableFlicButton.NAME, TableFlicButton.COLUMN_MAC + " LIKE ?", new String[] {mac});
    }


    public Cursor getAllData() { //db test
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TableFlicButton.NAME, null);
        return cursor;
    }
    public Cursor getAllFunction() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TableFunction.NAME, null);
        return cursor;
    }
}
