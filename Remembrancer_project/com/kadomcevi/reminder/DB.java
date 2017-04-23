package com.kadomcevi.reminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DB {
    public static final String COLUMN_DATE = "dt";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TXT = "txt";
    private static final String DB_CREATE = "create table memos(_id integer primary key autoincrement, dt long, txt text);";
    private static final String DB_NAME = "dbReminder";
    private static final String DB_TABLE = "memos";
    private static final int DB_VERSION = 1;
    private final Context mCtx;
    private SQLiteDatabase mDB;
    private DBHelper mDBHelper;

    private class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB.DB_CREATE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    public DB(Context ctx) {
        this.mCtx = ctx;
    }

    public void open() {
        this.mDBHelper = new DBHelper(this.mCtx, DB_NAME, null, DB_VERSION);
        this.mDB = this.mDBHelper.getWritableDatabase();
    }

    public void close() {
        if (this.mDBHelper != null) {
            this.mDBHelper.close();
        }
    }

    public Cursor getAllData() {
        return this.mDB.query(DB_TABLE, null, null, null, null, null, COLUMN_DATE);
    }

    public long addRec(String txt, long dt) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TXT, txt);
        cv.put(COLUMN_DATE, Long.valueOf(dt));
        return this.mDB.insert(DB_TABLE, null, cv);
    }

    public void delRec(long id) {
        this.mDB.delete(DB_TABLE, "_id = " + id, null);
    }

    public String getMemo(long id) {
        String idS = Long.toString(id);
        String retS = "";
        SQLiteDatabase sQLiteDatabase = this.mDB;
        String str = DB_TABLE;
        String[] strArr = new String[DB_VERSION];
        strArr[0] = COLUMN_TXT;
        String[] strArr2 = new String[DB_VERSION];
        strArr2[0] = idS;
        Cursor cur = sQLiteDatabase.query(str, strArr, "_id = ?", strArr2, null, null, null);
        if (cur.moveToFirst()) {
            return cur.getString(cur.getColumnIndex(COLUMN_TXT));
        }
        return retS;
    }

    public long getDateTime(long id) {
        String idS = Long.toString(id);
        SQLiteDatabase sQLiteDatabase = this.mDB;
        String str = DB_TABLE;
        String[] strArr = new String[DB_VERSION];
        strArr[0] = COLUMN_DATE;
        String[] strArr2 = new String[DB_VERSION];
        strArr2[0] = idS;
        Cursor cur = sQLiteDatabase.query(str, strArr, "_id = ?", strArr2, null, null, null);
        if (cur.moveToFirst()) {
            return cur.getLong(cur.getColumnIndex(COLUMN_DATE));
        }
        return 0;
    }

    public void updateRec(long id, String txt, long dt) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TXT, txt);
        cv.put(COLUMN_DATE, Long.valueOf(dt));
        String[] strArr = new String[DB_VERSION];
        strArr[0] = Long.toString(id);
        int updCount = this.mDB.update(DB_TABLE, cv, "_id = ?", strArr);
    }
}
